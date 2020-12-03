package edu.cornell.cs.apl.nativetools.templates

import edu.cornell.cs.apl.nativetools.Library
import edu.cornell.cs.apl.nativetools.SwigLibraryPlugin
import java.nio.file.Path

/** Provides constants that are used in [Template]s. */
internal class LibraryConstants(val library: Library) {
    private val nameAndVersion
        get() = "${library.name}-${library.version}"

    val patchFile: String
        get() = "lib.patch"

    val swigFile: String
        get() = "lib.i"

    val cmakeFile: String
        get() = "lib.cmake"

    /** Directory containing JNI header files. */
    val jniDirectory: String
        get() = "jni"

    val buildDirectory: String
        get() = "build"

    private val cmakeDirectory: String
        get() = "$buildDirectory/cmake"

    val cmakeSourceBuildDirectory: String
        get() = "$cmakeDirectory/source-build"

    val cmakeSourceInstallDirectory: String
        get() = "$cmakeDirectory/source-install"

    val cmakeWrapperBuildDirectory: String
        get() = "$cmakeDirectory/wrapper-build"

    val cmakeWrapperProjectName: String
        get() = "${library.name}Java"

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

    private val generatedResourcesDirectory: String
        get() = "$buildDirectory/${SwigLibraryPlugin.generatedResourcesBaseDir}/$nameAndVersion"

    fun nativeBinaryBaseDirectory(platform: Platform): String =
        "$generatedResourcesDirectory/${platform.safeName}"

    fun nativeBinaryDirectory(platform: Platform): String =
        "${nativeBinaryBaseDirectory(platform)}/natives/${platform.binaryDirectory}"

    val dockerWorkDirectory: String
        get() = "/work"

    companion object {
        /** Prints [path] using Unix separators. */
        fun unixPath(path: Path): String =
            path.toString().replace(path.fileSystem.separator, "/")

        /** Prints [path] using Unix separators. */
        fun unixPath(path: String): String =
            Path.of(path).let { it.toString().replace(it.fileSystem.separator, "/") }
    }
}
