package com.snaggly.ksw_toolkit.core.service.helper

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.Observer
import com.snaggly.ksw_toolkit.IKSWToolKitService
import projekt.auto.mcu.encryption.Base64
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.LinkedList

class CoreServiceClient {
    companion object {
        const val packageName = "com.snaggly.wits.ksw_toolkit.service"
        const val className = "com.snaggly.ksw_toolkit.core.service.CoreService"
    }

    val clientObservers = LinkedList<Observer<Boolean>>()

    private val serviceConnector = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            coreService = IKSWToolKitService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Thread.sleep(100)    // Prevent crashes during service outages
            coreService = null
        }
    }

    fun connectToService(context : Context) {
        if (coreService != null)
            return
        /*val message = context.packageName.toByteArray()
        val key = ServiceKey.getPrivateKey()
        val s = Signature.getInstance("SHA256withRSA/PSS").apply {
            initSign(key)
            update(message)
        }*/

        val intent = Intent()
        intent.component = ComponentName(packageName, className)
        //intent.putExtra("Authentication", s.sign())
        context.bindService(intent, serviceConnector, Context.BIND_EXTERNAL_SERVICE)
    }

    fun disconnectFromService(context : Context) {
        val intent = Intent()
        intent.component = ComponentName(packageName, className)
        context.unbindService(serviceConnector)
        coreService = null
    }

    var coreService: IKSWToolKitService? = null
        set(value) {
            field = value
            clientObservers.forEach {
                it.onChanged(field != null)
            }
        }



    private object ServiceKey {
        fun getPrivateKey() : PrivateKey {
            return KeyFactory.getInstance("RSA")
                .generatePrivate(
                    PKCS8EncodedKeySpec(
                    Base64.decode(privateKeyBase64, Base64.DEFAULT)
                )
            )
        }

        fun getPublicKey() : PublicKey {
            return KeyFactory.getInstance("RSA")
                .generatePublic(
                    X509EncodedKeySpec(
                    Base64.decode(publicKeyBase64, Base64.DEFAULT)
                )
                )
        }

        const val privateKeyBase64 =
                    "MIIEowIBAAKCAQEA3EJuFxL6kgtGpXRmt20eqUfiBQTZiMZyWMhzhwgtHpkcX6RX" +
                    "KiVaqLy0iTvRL7QxfyJcgLAaxYh7e1iS3SHm4cbMCpmF3CvcfAmaCmVmoocUE3Gw" +
                    "XzdSifUcia8XNXiEN8V0polYKMGV6lvbqVFgqpZtZOsznidlJ6clbDJlSV8EJBoR" +
                    "Fw0LsFSvN9BB4LRL0Q+uFEQek5qkJt2VpoBusyHc09+BIr0X/rFpxZPdkBnI5Uy8" +
                    "+0g0aJmEFPwhlP2jSiC4yHq/zzXusiDlqT1/iRQN1XXaJAhgHdFD3HA37fVIAuSU" +
                    "KshcW/0T9xrxp+PDCXZPj1aCFekKnXzaqsgMRwIDAQABAoIBAAQdGYFE0Xk4zLD6" +
                    "Y5C5cQEoXohJduafDjOqSr4S4QTZRzZiE16uI+tS2uxVOaryapo9Qg0FPPrDecrr" +
                    "9JI8t9svo0/vXhimmQ+X/lCDZE9UxoM2dFzS46+/7DCGHKDbB10jSvJxTjw0oWAn" +
                    "Jwsagfuto41m9yfVBIahEIncxxd/re1qAjNhy3WAJbi1XmfU5CtUIyiM8+Xxs6XH" +
                    "vRwsIR4ikgTnM+aN7EaJm/6aIfxbVPHhOhC2Vm4ttQOleok0dsN1qMYxRj4AR+Kt" +
                    "BiT/UPMUkffUiJ4P3stnLS69zj7gYa/Gfh/87oGbaCqZGrvkgGCkn2oKNQX2uahv" +
                    "6LY828ECgYEA7jNSOAa/fv0mC0XYYva2xmE9ATq9/nnlm6iGm+tU9t5bhu9eFysE" +
                    "7gq/QX2bAq6oB79aUXMOnpOpkSFoUHNaGx9u98MIBLfpnaK/FHKPq+vXAv4ZiIm/" +
                    "gdiM8is8DKfNsXREfqnD2Dk0oWfh3sQv6VSKUgEDSz0Zks5xbtbSq70CgYEA7Lfn" +
                    "uCU4ClZm2vtccr3MKV1ahWK0C2qF2cCPQoi26SKk1tbdRaBU2mElfz5TFcI264+9" +
                    "ucRgP43MJN1N7C2PdccM5QaxYpAT3qn2j4kycGTeSYCq3ZvbGCXmf03cHpUVEKsI" +
                    "Qv+Kr/HcJ66/fO3m05xzggTIiMxQYjt5nL0KtlMCgYBUvZdLbUMlF0Q4kHGJy4F8" +
                    "k81TKaW76XvA47R3tcH1TWe8Wl3b2icX1GT0FI5X5vDKSM4F2eviiF+zzPHCZTcy" +
                    "HP2qUQ0COLd4qBRYXuUz4WQNoX5N78tck0CZSr5DNlNcBSP098o/myJZJY7aZGsz" +
                    "FCtdPxT/6E23x4/dQbmS9QKBgQDQXFgUWH5pawACBHqtbK7GQiupeOXtnofO0ZXI" +
                    "BLqrnxjlGh+OZ8AIsLXmuJ89acQZuF5Ro67sgg2M7Vbnanr6Ooj5FtkQXq1+srAa" +
                    "S7MgB0k5/Y1onwVMzh+DMq9sI+F02HbfMjuK/yK/sytN9cHVxcJOUZOKBf1DWgKD" +
                    "mkpRjQKBgBzM1q9KICLDCBD92+abUsrmKfnY73zLECXCdtMj/nYQHMk+mXTfNtfr" +
                    "5ks6WUWiDR5CGFwCW449GLDZYxfXVQQELPBJqGqj9diWa9IlxPx4J+nfls0lrbxi" +
                    "BXE6pvUpWrcHt/1AXKWCoGtjbZM1Z9YayoLl7gYcdRIjQZEhiz9+"

        const val publicKeyBase64 =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3EJuFxL6kgtGpXRmt20e" +
                    "qUfiBQTZiMZyWMhzhwgtHpkcX6RXKiVaqLy0iTvRL7QxfyJcgLAaxYh7e1iS3SHm" +
                    "4cbMCpmF3CvcfAmaCmVmoocUE3GwXzdSifUcia8XNXiEN8V0polYKMGV6lvbqVFg" +
                    "qpZtZOsznidlJ6clbDJlSV8EJBoRFw0LsFSvN9BB4LRL0Q+uFEQek5qkJt2VpoBu" +
                    "syHc09+BIr0X/rFpxZPdkBnI5Uy8+0g0aJmEFPwhlP2jSiC4yHq/zzXusiDlqT1/" +
                    "iRQN1XXaJAhgHdFD3HA37fVIAuSUKshcW/0T9xrxp+PDCXZPj1aCFekKnXzaqsgM" +
                    "RwIDAQAB"
    }
}