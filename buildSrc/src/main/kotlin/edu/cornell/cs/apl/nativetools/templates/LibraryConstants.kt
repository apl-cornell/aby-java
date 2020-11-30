package edu.cornell.cs.apl.nativetools.templates

import edu.cornell.cs.apl.nativetools.Library
import edu.cornell.cs.apl.nativetools.SwigLibraryPlugin
import java.nio.file.Path

/** Provides constants that are used in [Template]s. */
class LibraryConstants(val library: Library) {
    val patchFile: String
        get() = "lib.patch"

    val swigFile: String
        get() = "lib.i"

    val buildDirectory: String
        get() = "build"

    private val cmakeBuildDirectory: String
        get() = "$buildDirectory/cmake"

    private val downloadDirectory: String
        get() = "$buildDirectory/downloaded"

    val originalSourceDirectory: String
        get() = "$downloadDirectory/original-source"

    val patchedSourceDirectory: String
        get() = "$downloadDirectory/patched-source"

    private val swigGeneratedBaseDirectory: String
        get() = "$buildDirectory/${SwigLibraryPlugin.generatedSrcBaseDir}/${library.name}-${library.version}"

    val swigGeneratedJavaBaseDirectory: String
        get() = "$swigGeneratedBaseDirectory/java"

    val swigGeneratedJavaDirectory: String
        get() = "$swigGeneratedJavaBaseDirectory/${library.packageName.replace(".", "/")}"

    val swigGeneratedCppFile: String
        get() = "$swigGeneratedBaseDirectory/cpp/wrapper.cpp"

    companion object {
        /** Prints [path] using Unix separators. */
        fun unixPath(path: Path): String =
            path.toString().replace(path.fileSystem.separator, "/")

        /** Prints [path] using Unix separators. */
        fun unixPath(path: String): String =
            Path.of(path).let { it.toString().replace(it.fileSystem.separator, "/") }
    }
}
