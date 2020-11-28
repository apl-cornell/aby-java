package edu.cornell.cs.apl.nativetools

import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class DownloadLibraryTask : DefaultTask() {
    @get:Nested
    abstract val library: Property<Library>

    @get:Input
    abstract val submodules: ListProperty<String>

    @get:OutputDirectory
    val outputDirectory: File
        get() = project.buildDir.resolve("downloaded-src/${library.get().name}-${library.get().version}")

    @Internal
    override fun getDescription(): String {
        return "Downloads ${library.get().name} source code."
    }

    @TaskAction
    fun download() {
        project.mkdir(outputDirectory)
        git("init")
        git("fetch", "--depth", "1", library.get().url, library.get().version)
        git("checkout", library.get().version)
        submodules.get().forEach { submodule ->
            var parentSubmodule = File("")
            val parts = submodule.split(":")
            assert(parts.isNotEmpty())
            parts.forEach { part ->
                git("submodule", "update", "--init", "--depth", "1", part, cd = parentSubmodule)
                parentSubmodule = parentSubmodule.resolve(part)
            }
        }
    }

    private fun git(vararg args: String, cd: File = File("")) =
        project.exec {
            workingDir = outputDirectory.resolve(cd)
            commandLine = listOf("git") + args
        }
}
