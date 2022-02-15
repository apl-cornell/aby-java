package io.github.apl_cornell.nativetools

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/** Builds a Docker image and copies generated files out. */
abstract class DockerCopyTask : DefaultTask() {
    /** The Docker file to build. */
    @get:InputFile
    abstract val dockerfile: RegularFileProperty

    /** The Docker target. */
    @get:Input
    abstract val target: Property<String>

    /** Path to the directory in the Docker image to copy. */
    @get:Input
    abstract val from: Property<String>

    /** Local directory where the contents of the [from] directory will be placed in. */
    @get:OutputDirectory
    abstract val into: DirectoryProperty

    /** Directory that contains the files that will be shipped to Docker. */
    @get:InputDirectory
    val baseDirectory: Provider<Directory>
        get() = dockerfile.map { project.layout.projectDirectory.dir(it.asFile.parent) }

    @Internal
    override fun getGroup(): String =
        SwigLibraryPlugin.taskGroup

    @TaskAction
    fun swigLibrary() {
        project.dockerCopy(
            from = from.get(),
            to = "${into.get()}",
            dockerfile = dockerfile.get(),
            target = target.get()
        )
    }
}
