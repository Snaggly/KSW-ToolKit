package com.snaggly.ksw_toolkit.util.adb

import android.content.Context
import android.net.TrafficStats
import projekt.auto.mcu.adb.AdbManager
import projekt.auto.mcu.adb.lib.AdbConnection
import projekt.auto.mcu.adb.lib.AdbCrypto
import projekt.auto.mcu.adb.lib.AdbStream
import java.io.File
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.security.NoSuchAlgorithmException

object AdbManager {
    private var isConnected = false
    private var socket : Socket? = null
    private var adbConnection: AdbConnection? = null
    private var shellStream: AdbStream? = null
    private var previousShellText = ""
    private var adbShellListener : ShellObserver? = null

    @Throws(AdbConnectionException::class)
    private fun connect(context: Context, address: String) {
        if (isConnected)
            disconnect()
        var outerException: Exception? = null
        val setup = Thread {
            try {
                TrafficStats.setThreadStatsTag(Thread.currentThread().id.toInt())
                val adbCrypto = setupCrypto(context.filesDir)
                socket = Socket()
                socket!!.connect(InetSocketAddress(address, 5555), 5000)
                adbConnection = AdbConnection.create(socket, adbCrypto)
                adbConnection!!.connect()
                shellStream = adbConnection!!.open("shell:")
            } catch (e: Exception) {
                outerException = e
            }
        }
        setup.start()
        setup.join()

        if (outerException != null) {
            throw AdbConnectionException(outerException!!)
        }
        isConnected = shellStream != null
    }

    private fun disconnect() {
        val disconnecter = Thread {
            shellStream?.close()
            adbConnection?.close()
            socket?.close()
        }
        disconnecter.start()
        disconnecter.join()

        isConnected = false
    }

    private fun writeCommand(command: String) {
        val writer = Thread {
            try {
                shellStream?.write(" $command\n")
            }
            catch (e : Exception) {
                isConnected = false
            }
        }
        writer.start()
        writer.join()
    }

    fun sendCommand(command: String, context: Context) {
        if (isConnected) {
            writeCommand(command)
        } else {
            connect(context)
            if (isConnected) {
                writeCommand(command)
            }
            disconnect()
        }
    }

    @Throws(NoSuchAlgorithmException::class, IOException::class)
    private fun setupCrypto(fileDir: File): AdbCrypto? {
        val publicKey = File(fileDir, "public.key")
        val privateKey = File(fileDir, "private.key")
        var c: AdbCrypto? = null
        if (publicKey.exists() && privateKey.exists()) {
            try {
                c = AdbCrypto.loadAdbKeyPair(AdbManager.getBase64Impl(), privateKey, publicKey)
            } catch (ignored: Exception) {
            }
        }
        if (c == null) {
            c = AdbCrypto.generateAdbKeyPair(AdbManager.getBase64Impl())
            c.saveAdbKeyPair(privateKey, publicKey)
        }
        return c
    }

    class AdbConnectionException(private val outerException: Exception) : Exception() {
        override fun getLocalizedMessage(): String? {
            return outerException.localizedMessage
        }

        override fun getStackTrace(): Array<StackTraceElement> {
            return outerException.stackTrace
        }
    }

    fun registerShellListener(listener: ShellObserver, context: Context) {
        adbShellListener = listener

        Thread {
            while (shellStream != null && adbShellListener != null) {
                try {
                    val shellTexts = ArrayList<String>()
                    do {
                        val shellTextBytes : ByteArray? = try {
                            shellStream!!.read()
                        } catch (sc : Exception) {
                            null
                        }
                        if (shellTextBytes != null) {
                            val shellText = String(shellTextBytes , Charsets.US_ASCII)
                            shellTexts.add(shellText)
                            if (shellText.endsWith(":/ \$ "))
                                break
                        }
                        else {
                            break
                        }
                    }
                    while (!shellStream!!.isClosed)
                    adbShellListener?.update(shellTexts)
                    Thread.sleep(100)
                }
                catch (e : Exception) {
                    isConnected = false
                }
            }
        }.start()
        connect(context)
    }

    fun unregisterShellListener() {
        disconnect()
        adbShellListener = null
    }

    fun connect(context: Context) {
        connect(context, "localhost")
    }
}