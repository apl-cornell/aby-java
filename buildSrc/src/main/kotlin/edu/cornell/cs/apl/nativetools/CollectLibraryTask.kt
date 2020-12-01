package edu.cornell.cs.apl.nativetools

import edu.cornell.cs.apl.nativetools.templates.LibraryConstants
import edu.cornell.cs.apl.nativetools.templates.dockerignore
import edu.cornell.cs.apl.nativetools.templates.getMakefile
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

        project.copy {
            from(jniHeadersDirectory)
            into(outputDirectory.dir(constants.jniDirectory))
        }

        outputDirectory.addFile(patchFile, constants.patchFile)
        outputDirectory.addFile(interfaceFile, constants.swigFile)
        outputDirectory.addFile(conanFile, "conanfile.${conanFile.get().asFile.extension}")

        getMakefile.generate(constants, outputDirectory)
        swigMakefile.generate(constants, outputDirectory)
        outputDirectory.writeResource("Dockerfile")
        dockerignore.generate(constants, outputDirectory)
    }

    /** Copies Java resource named [resource] into this directory. */
    private fun Provider<Directory>.writeResource(resource: String) =
        this.get().file(resource).asFile.writeBytes(
            CollectLibraryTask::class.java.getResource(resource).readBytes()
        )

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
