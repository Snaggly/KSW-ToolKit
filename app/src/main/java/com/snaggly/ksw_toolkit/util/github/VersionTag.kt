package com.snaggly.ksw_toolkit.util.github

data class VersionTag(
    val majorVersion: Int,
    val minorVersion: Int,
    val hotfixVersion: Int,
    val iteration: Int,
    val tagName: String
) {
    companion object {
        fun getVersion(tag_name: String) : VersionTag {
            var m  = 0
            var n = 0
            var h = 0
            var i = 0

            var versionNumStr = tag_name.replace("[^0-9.-]".toRegex(), "")
            if (versionNumStr.contains('-')) {
                try {
                    i = versionNumStr.substring(versionNumStr.lastIndexOf('-') + 1).toInt()
                } catch (ignored : Exception) {}
                versionNumStr = versionNumStr.substring(0,versionNumStr.lastIndexOf('-'))
            }
            val versionNumSep = versionNumStr.split('.')
            try {
                m = versionNumSep[0].toInt()
                n = versionNumSep[1].toInt()
                h = versionNumSep[2].toInt()
            } catch (ignored: Exception) {}

            return VersionTag(m, n, h, i, tag_name)
        }
    }

    override fun toString(): String {
        return tagName
    }
}