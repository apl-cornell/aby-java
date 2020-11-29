package edu.cornell.cs.apl.nativetools

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/** Builds a Docker image and copies generated files out. */
abstract class DockerCopyTask : DefaultTask() {
    @get:InputDirectory
    abstract val baseDirectory: DirectoryProperty

    @get:Input
    abstract val target: Property<String>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @Internal
    override fun getGroup(): String =
        SwigLibraryPlugin.taskGroup

    @TaskAction
    fun swigLibrary() {
        val relativeDir = project.relativePath(outputDirectory)
        project.dockerCopy(
            from = "/root/$relativeDir",
            to = "${outputDirectory.get()}",
            dockerfile = baseDirectory.file("Dockerfile").get(),
            target = target.get()
        )
    }
}
