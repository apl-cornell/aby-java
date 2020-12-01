package edu.cornell.cs.apl.nativetools.templates

import edu.cornell.cs.apl.nativetools.Library
import edu.cornell.cs.apl.nativetools.SwigLibraryPlugin
import java.nio.file.Path

/** Provides constants that are used in [Template]s. */
class LibraryConstants(val library: Library) {
    private val nameAndVersion
        get() = "${library.name}-${library.version}"

    val patchFile: String
        get() = "lib.patch"

    val swigFile: String
        get() = "lib.i"

    /** Directory containing JNI header files. */
    val jniDirectory: String
        get() = "jni"

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
        get() = "$buildDirectory/${SwigLibraryPlugin.generatedSourcesBaseDir}/$nameAndVersion"

    val swigGeneratedJavaBaseDirectory: String
        get() = "$swigGeneratedBaseDirectory/java"

    val swigGeneratedJavaDirectory: String
        get() = "$swigGeneratedJavaBaseDirectory/${library.packageName.replace(".", "/")}"

    val swigGeneratedCppFile: String
        get() = "$swigGeneratedBaseDirectory/cpp/wrapper.cpp"

    val sharedLibraryName: String
        get() = "${library.safeName}java"

    private val nativeBinaryBaseDirectory: String
        get() = "$buildDirectory/${SwigLibraryPlugin.generatedResourcesBaseDir}/$nameAndVersion/natives"

    val linuxBinary: String
        get() = "$nativeBinaryBaseDirectory/linux_64/lib$sharedLibraryName.so"

    val macosBinary: String
        get() = "$nativeBinaryBaseDirectory/osx_64/lib$sharedLibraryName.dylib"

    val windowsBinary: String
        get() = "$nativeBinaryBaseDirectory/windows_64/$sharedLibraryName.dll"

    companion object {
        /** Prints [path] using Unix separators. */
        fun unixPath(path: Path): String =
            path.toString().replace(path.fileSystem.separator, "/")

        /** Prints [path] using Unix separators. */
        fun unixPath(path: String): String =
            Path.of(path).let { it.toString().replace(it.fileSystem.separator, "/") }
    }
}
