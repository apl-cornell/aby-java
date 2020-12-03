package edu.cornell.cs.apl.nativetools.templates

/** Platforms we can generate native binaries for. */
internal enum class Platform {
    LINUX_64,

    MACOS_64 {
        override val binaryDirectory: String
            get() = "osx_64"
    },

    WINDOWS_64;

    /** A name for this platform that can be used in task and file names. */
    val safeName: String
        get() = name.toLowerCase().replace("_", "")

    /** Resource path where the native binary should be placed. */
    open val binaryDirectory: String
        get() = name.toLowerCase()
}
