package edu.cornell.cs.apl.nativetools

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/** Executes [SwigLibraryTask] in a Docker container and copies out the generated files. */
abstract class DockerSwigLibraryTask : DefaultTask() {
    @get:Nested
    abstract val library: Property<Library>

    @get:InputFile
    abstract val patchFile: RegularFileProperty

    @get:InputFile
    abstract val interfaceFile: RegularFileProperty

    @get:OutputDirectory
    abstract val javaOutputDirectory: DirectoryProperty

    @Internal
    override fun getDescription(): String =
        "Generates a Java interface for ${library.get().name} using Docker."

    @Internal
    override fun getGroup(): String =
        SwigLibraryPlugin.taskGroup

    @TaskAction
    fun swigLibrary() {
        val relativeDir = project.rootProject.relativePath(javaOutputDirectory.get())
        val from = "/root/$relativeDir"
        project.dockerCopy(from = from, to = "${javaOutputDirectory.get()}", target = "swig")
    }
}
