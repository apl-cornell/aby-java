package edu.cornell.cs.apl.nativetools

import edu.cornell.cs.apl.nativetools.templates.LibraryConstants
import edu.cornell.cs.apl.nativetools.templates.buildMakefile
import edu.cornell.cs.apl.nativetools.templates.getMakefile
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
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

    @get:OutputDirectory
    val outputDirectory: Provider<Directory>
        get() {
            val nameVersion = library.map { "${it.name}-${it.version}" }
            return project.layout.buildDirectory.dir("tmp/native-tools").dir(nameVersion)
        }

    private val generatedBaseDir: Provider<Directory>
        get() {
            val nameVersion = library.map { "${it.name}-${it.version}" }
            return project.layout.buildDirectory.dir(SwigLibraryPlugin.generatedSrcBaseDir).dir(nameVersion)
        }

    @get:Internal
    val generatedJavaBaseDir: Provider<Directory>
        get() = generatedBaseDir.dir("java")

    private val generatedJavaDir: Provider<Directory>
        get() = generatedJavaBaseDir.dir(packageDir)

    private val generatedCppFile: Provider<RegularFile>
        get() = generatedBaseDir.dir("cpp").file("wrapper.cpp")

    private val packageDir: Provider<String>
        get() =
            library.map { it.packageName.replace(".", "/") }


    @Internal
    override fun getDescription(): String =
        "Collects files needed to build ${library.get().name} using Docker."

    @Internal
    override fun getGroup(): String =
        SwigLibraryPlugin.taskGroup

    @TaskAction
    fun collect() {
        val constants = LibraryConstants(library.get())

        outputDirectory.addFile(patchFile, "lib", "patch")
        outputDirectory.addFile(interfaceFile, "lib", "i")
        outputDirectory.addFile(conanFile, "conanfile")

        getMakefile.generate(constants, outputDirectory)
        buildMakefile.generate(constants, outputDirectory)
        outputDirectory.writeResource("Dockerfile")
        outputDirectory.writeResource(".dockerignore")
    }

    /** Copies Java resource named [resource] into this directory. */
    private fun Provider<Directory>.writeResource(resource: String) =
        this.get().file(resource).asFile.writeBytes(
            CollectLibraryTask::class.java.getResource(resource).readBytes()
        )

    /**
     * Copies [file] into this directory and renames it to [name].[extension].
     * The original file extension is preserved when no extension is specified.
     */
    private fun Provider<Directory>.addFile(
        file: Provider<RegularFile>,
        name: String,
        extension: String = file.get().asFile.extension
    ) {
        val directory = this
        project.copy {
            from(file)
            into(directory)
            rename { "$name.$extension" }
        }
    }
}
