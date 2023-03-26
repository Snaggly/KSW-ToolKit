package com.snaggly.ksw_toolkit.util.list.mcu

data class McuData(val eventName: String, val cmdType : Int, val bytes : ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as McuData

        if (cmdType != other.cmdType) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cmdType
        result = 31 * result + bytes.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "$eventName - $cmdType (${dataBytesToString(bytes)})"
    }

    fun dataBytesToString(data: ByteArray): String {
        var result = ""
        if (data.isEmpty())
            return result
        for (i in 0..data.size - 2) {
            result += data[i].toString(16) + "-"
        }
        result += data[data.size - 1].toString(16)

        return result
    }
}
