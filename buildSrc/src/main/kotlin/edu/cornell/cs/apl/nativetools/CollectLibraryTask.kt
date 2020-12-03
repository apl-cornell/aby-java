package edu.cornell.cs.apl.nativetools

import edu.cornell.cs.apl.nativetools.templates.LibraryConstants
import edu.cornell.cs.apl.nativetools.templates.buildMakefile
import edu.cornell.cs.apl.nativetools.templates.cmakeLists
import edu.cornell.cs.apl.nativetools.templates.dockerignore
import edu.cornell.cs.apl.nativetools.templates.getMakefile
import edu.cornell.cs.apl.nativetools.templates.linuxDockerfile
import edu.cornell.cs.apl.nativetools.templates.macosDockerfile
import edu.cornell.cs.apl.nativetools.templates.swigDockerfile
import edu.cornell.cs.apl.nativetools.templates.swigMakefile
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

        getMakefile.generate(constants, outputDirectory)
        swigMakefile.generate(constants, outputDirectory)
        buildMakefile.generate(constants, outputDirectory)
        cmakeLists.generate(constants, outputDirectory)

        swigDockerfile.generate(constants, outputDirectory)
        linuxDockerfile.generate(constants, outputDirectory)
        macosDockerfile.generate(constants, outputDirectory)
        dockerignore.generate(constants, outputDirectory)
        outputDirectory.writeResource("profiles/x86_64-apple-darwin.cmake")
        outputDirectory.writeResource("profiles/x86_64-apple-darwin.conan")

        project.copy {
            from(jniHeadersDirectory)
            into(outputDirectory.dir(constants.jniDirectory))
        }
    }

    /** Copies Java resource named [resource] into this directory. */
    private fun Provider<Directory>.writeResource(resource: String) {
        val file = this.get().file(resource).asFile
        project.mkdir(file.parentFile)
        file.writeBytes(CollectLibraryTask::class.java.getResource(resource).readBytes())
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
