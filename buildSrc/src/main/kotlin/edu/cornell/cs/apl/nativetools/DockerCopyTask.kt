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
    /** Directory that contains the Docker file. */
    @get:InputDirectory
    abstract val baseDirectory: DirectoryProperty

    /** The Docker target. */
    @get:Input
    abstract val target: Property<String>

    /** Path to the directory in the Docker image to copy. */
    @get:Input
    abstract val from: Property<String>

    /** Local directory where the contents of the [from] directory will be placed in. */
    @get:OutputDirectory
    abstract val into: DirectoryProperty

    @Internal
    override fun getGroup(): String =
        SwigLibraryPlugin.taskGroup

    @TaskAction
    fun swigLibrary() {
        project.dockerCopy(
            from = from.get(),
            to = "${into.get()}",
            dockerfile = baseDirectory.file("Dockerfile").get(),
            target = target.get()
        )
    }
}
