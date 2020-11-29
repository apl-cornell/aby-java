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

                val collect = project.tasks.register<CollectLibraryTask>("collect${library.name}") {
                    this.library.set(library)
                    patchFile.set(project.file("${library.name}.patch"))
                    interfaceFile.set(project.file("${library.name}.i"))
                }

                project.tasks.register<DockerCopyTask>("dockerSwig${library.name}") {
                    baseDirectory.set(collect.map { it.outputDirectory.get() })
                    target.set("swig")
                    outputDirectory.set(collect.map { it.generatedJavaBaseDir.get() })
                }

            }
        }
    }

    internal companion object {
        const val taskGroup = "Native Build"
        const val generatedSrcBaseDir: String = "generated/sources/swig"
    }
}
