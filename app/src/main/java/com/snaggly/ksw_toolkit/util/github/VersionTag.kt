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

        fun isNewerVersionAvailable(installedVersion: VersionTag, githubVersion: VersionTag) : Boolean {
            if (githubVersion.majorVersion > installedVersion.majorVersion) {
                return true
            }
            else if (githubVersion.majorVersion >= installedVersion.majorVersion &&
                githubVersion.minorVersion > installedVersion.minorVersion) {
                return true
            }
            else if (githubVersion.majorVersion >= installedVersion.majorVersion &&
                githubVersion.minorVersion >= installedVersion.minorVersion &&
                githubVersion.hotfixVersion > installedVersion.hotfixVersion) {
                return true
            }
            else return githubVersion.majorVersion >= installedVersion.majorVersion &&
                    githubVersion.minorVersion >= installedVersion.minorVersion &&
                    githubVersion.hotfixVersion >= installedVersion.hotfixVersion &&
                    githubVersion.iteration > installedVersion.iteration
        }
    }

    override fun toString(): String {
        return tagName
    }
}