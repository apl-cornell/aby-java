package io.github.apl_cornell.nativetools

import io.github.apl_cornell.nativetools.templates.LibraryConstants
import io.github.apl_cornell.nativetools.templates.Platform
import io.github.apl_cornell.nativetools.templates.buildMakefile
import io.github.apl_cornell.nativetools.templates.cmakeLists
import io.github.apl_cornell.nativetools.templates.dependenciesMakefile
import io.github.apl_cornell.nativetools.templates.dockerignore
import io.github.apl_cornell.nativetools.templates.getMakefile
import io.github.apl_cornell.nativetools.templates.linuxDockerfile
import io.github.apl_cornell.nativetools.templates.macosDockerfile
import io.github.apl_cornell.nativetools.templates.swigDockerfile
import io.github.apl_cornell.nativetools.templates.swigMakefile
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/** Collects all files needed to build a [Library] inside Docker. */
abstract class CollectLibraryTask : DefaultTask() {
    @get:Nested
    abstract val library: Property<Library>

    @get:InputFile
    abstract val patchFile: RegularFileProperty

    @get:InputFile
    abstract val interfaceFile: RegularFileProperty

    @get:InputFile
    abstract val cmakeFile: RegularFileProperty

    @get:InputFile
    abstract val conanFile: RegularFileProperty

    @get:InputDirectory
    abstract val jniHeadersDirectory: DirectoryProperty

    @get:OutputDirectory
    val outputDirectory: Provider<Directory>
        get() {
            val nameVersion = library.map { "${it.name}-${it.version}" }
            return project.layout.buildDirectory.dir(SwigLibraryPlugin.tmpDirectory).dir(nameVersion)
        }

    @Internal
    override fun getDescription(): String =
        "Collects files needed to build ${library.get().name} using Docker."

    @Internal
    override fun getGroup(): String =
        SwigLibraryPlugin.taskGroup

    @TaskAction
    fun collect() {
        val constants = LibraryConstants(library.get())

        outputDirectory.addFile(patchFile, constants.patchFile)
        outputDirectory.addFile(interfaceFile, constants.swigFile)
        outputDirectory.addFile(cmakeFile, constants.cmakeFile)
        outputDirectory.addFile(conanFile, "conanfile.${conanFile.get().asFile.extension}")

        outputDirectory.file(constants.swigFile).get().asFile.appendText(
            """

            // Add code to the generated Java wrapper to automatically load the library.
            %pragma(java) jniclasscode=%{
              static {
                try {
                  org.scijava.nativelib.NativeLoader.loadLibrary("${constants.sharedLibraryName}");
                } catch (java.io.IOException e) {
                  throw new Error(e);
                }
              }
            %}

            """.trimIndent()
        )

        getMakefile.generate(constants, outputDirectory)
        swigMakefile.generate(constants, outputDirectory)
        dependenciesMakefile.generate(constants, outputDirectory)
        buildMakefile.generate(constants, outputDirectory)
        cmakeLists.generate(constants, outputDirectory)

        swigDockerfile.generate(constants, outputDirectory)
        linuxDockerfile.generate(constants, outputDirectory)
        macosDockerfile.generate(constants, outputDirectory)
        dockerignore.generate(constants, outputDirectory)

        // Copy CMake and Conan profiles
        Platform.values().forEach { platform ->
            outputDirectory.writeResource(platform.cmakeProfileFile, skipIfMissing = true)
            outputDirectory.writeResource(platform.conanProfileFile, skipIfMissing = true)
        }

        project.copy {
            from(jniHeadersDirectory)
            into(outputDirectory.dir(constants.jniDirectory))
        }
    }

    /**
     * Copies Java resource named [resource] into this directory.
     * @param skipIfMissing When set, does not raise an exception if the resource is missing.
     */
    private fun Provider<Directory>.writeResource(resource: String, skipIfMissing: Boolean = false) {
        val resourceURL = CollectLibraryTask::class.java.getResource(resource)
        if (skipIfMissing && resourceURL == null)
            return
        val file = this.get().file(resource).asFile
        project.mkdir(file.parentFile)
        file.writeBytes(resourceURL!!.readBytes())
    }

    /** Copies [file] into this directory and renames it to [name]. */
    private fun Provider<Directory>.addFile(file: Provider<RegularFile>, name: String) {
        val directory = this
        project.copy {
            from(file)
            into(directory)
            rename { name }
        }
    }
}
