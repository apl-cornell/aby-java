package edu.cornell.cs.apl.nativetools

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class SwigLibraryTask : DefaultTask() {
    @get:Nested
    abstract val library: Property<Library>

    @get:InputDirectory
    abstract val source: DirectoryProperty

    @get:InputFile
    val interfaceFile: Provider<RegularFile>
        get() = project.layout.projectDirectory.file(library.map { "${it.name}.i" })

    @get:OutputFile
    val cppOutput: Provider<RegularFile>
        get() {
            val dir = project.layout.buildDirectory.dir(generatedSrcBaseDir).dir("cpp/main").dir(packageDir)
            val fileName = library.map { "${it.name}-wrap.cpp" }
            return dir.file(fileName)
        }

    @get:OutputDirectory
    val javaOutputDirectory: Provider<Directory>
        get() = project.layout.buildDirectory.dir(generatedSrcBaseDir).dir("java/main").dir(packageDir)

    @Internal
    override fun getDescription(): String =
        "Generates a Java interface for ${library.get().name} with SWIG."

    @Internal
    override fun getGroup(): String =
        SwigLibraryPlugin.taskGroup

    @TaskAction
    fun generateInterface() {
        val includes = library.get().includeDirectories.map { "-I${source.dir(it).get()}" }
        project.exec {
            commandLine = listOf(
                "swig",
                "-Wall", "-Werror", "-macroerrors",
                "-c++",
                "-java", "-package", library.get().packageName
            ) + includes + listOf(
                "-o", "${cppOutput.get()}", "-outdir", "${javaOutputDirectory.get()}",
                "${interfaceFile.get()}"
            )
        }
    }

    private val packageDir: Provider<String>
        get() =
            library.map { it.packageName.replace(".", "/") }

    companion object {
        const val generatedSrcBaseDir: String = "generated/sources/swig"
    }
}

