package edu.cornell.cs.apl.nativetools

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class DownloadLibraryTask : DefaultTask() {
    @get:Nested
    abstract val library: Property<Library>

    @get:OutputDirectory
    val outputDirectory: Provider<Directory>
        get() {
            val downloadDir = library.map { "downloaded-src/${it.name}-${it.version}" }
            return project.layout.buildDirectory.dir(downloadDir)
        }

    @Internal
    override fun getDescription(): String =
        "Downloads ${library.get().name} source code."

    @Internal
    override fun getGroup(): String =
        SwigLibraryPlugin.taskGroup

    @TaskAction
    fun download() {
        project.mkdir(outputDirectory)
        git("init")
        git("fetch", "--depth", "1", library.get().url, library.get().version)
        git("checkout", library.get().version)
        library.get().submodules.forEach { submodule ->
            var parentSubmodule = outputDirectory.get()
            val parts = submodule.split(":")
            assert(parts.isNotEmpty())
            parts.forEach { part ->
                git("submodule", "update", "--init", "--depth", "1", part, workingDir = parentSubmodule)
                parentSubmodule = parentSubmodule.dir(part)
            }
        }
    }

    private fun git(vararg args: String, workingDir: Directory = outputDirectory.get()) =
        project.exec {
            this.workingDir = workingDir.asFile
            commandLine = listOf("git") + args
        }
}
