package edu.cornell.cs.apl.nativetools

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class SwigLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<SwigLibraryPluginExtension>("swigLibrary")

        // Register tasks for each library
        project.afterEvaluate {
            extension.libraries.get().forEach { library ->
                project.logger.info("Adding library ${library.name}.")

                val download = project.tasks.register<DownloadLibraryTask>("download${library.name}") {
                    this.library.set(library)
                }

                val patch = project.tasks.register<PatchTask>("patch${library.name}") {
                    description = "Patches ${library.name} source code."
                    from.set(download.map { it.outputDirectory.get() })
                    patch.set(project.file("${library.name}.patch"))
                }

                project.tasks.register<SwigLibraryTask>("swig${library.name}") {
                    this.library.set(library)
                    source.set(patch.map { it.outputDirectory.get() })
                }
            }
        }
    }

    companion object {
        internal const val taskGroup = "Native Build"
    }
}
