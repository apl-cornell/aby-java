package io.github.apl_cornell.nativetools.templates

/** Platforms we can generate native binaries for. */
internal enum class Platform {
    LINUX_64 {
        override val crossTriple: String
            get() = "x86_64-linux-gnu"
    },

    MACOS_64 {
        override val binaryDirectory: String
            get() = "osx_64"

        override val crossTriple: String
            get() = "x86_64-apple-darwin"
    },

    WINDOWS_64 {
        override val crossTriple: String
            get() = "" // TODO
    };

    /** A name for this platform that can be used in task and file names. */
    val safeName: String
        get() = name.toLowerCase().replace("_", "")

    /** Resource path where the native binary should be placed. */
    open val binaryDirectory: String
        get() = name.toLowerCase()

    /** Machine, vendor, operating system triple describing the system. */
    abstract val crossTriple: String

    /** Name of the CMake profile file for this platform. */
    val cmakeProfileFile: String
        get() = "profiles/$crossTriple.cmake"

    /** Name of the Conan host profile file for this platform. */
    val conanProfileFile: String
        get() = "profiles/$crossTriple.conan"
}
