package edu.cornell.cs.apl.nativetools

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class PatchTask : DefaultTask() {
    @get:InputDirectory
    abstract val from: DirectoryProperty

    @get:InputFile
    abstract val patch: RegularFileProperty

    @get:OutputDirectory
    @get:Optional
    abstract val into: DirectoryProperty

    @get:OutputDirectory
    val outputDirectory: Provider<Directory>
        get() {
            val default = from.map { it.dir("../patch-${it.asFile.name}") }
            @Suppress("UnstableApiUsage")
            return into.orElse(default)
        }

    @Internal
    override fun getGroup(): String =
        SwigLibraryPlugin.taskGroup

    @TaskAction
    fun patch() {
        project.copy {
            from(from)
            into(outputDirectory)
        }

        project.exec {
            workingDir = outputDirectory.get().asFile
            commandLine = listOf("git", "apply", "${patch.get()}")
        }
    }
}
