package edu.cornell.cs.apl.nativetools

import edu.cornell.cs.apl.nativetools.templates.LibraryConstants
import java.nio.file.Path
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register

class SwigLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<SwigLibraryPluginExtension>("swigLibrary")

        val downloadJNIHeaders = project.tasks.register<DownloadJNITask>("downloadJNIHeaders")

        // Register tasks for each library
        project.afterEvaluate {
            extension.libraries.get().forEach { library ->
                project.logger.info("Adding library ${library.name}.")

                val constants = LibraryConstants(library)

                val collect = project.tasks.register<CollectLibraryTask>("collect${library.name}") {
                    this.library.set(library)
                    patchFile.set(project.file("${library.name}.patch"))
                    interfaceFile.set(project.file("${library.name}.i"))
                    cmakeFile.set(project.file("${library.name}.cmake"))
                    conanFile.set(project.file("${library.name}.conanfile.txt"))
                    jniHeadersDirectory.set(downloadJNIHeaders.map { it.outputDirectory.get() })
                }

                val swig = project.tasks.register<DockerCopyTask>("dockerSwig${library.name}") {
                    baseDirectory.set(collect.map { it.outputDirectory.get() })
                    target.set("swig")
                    from.set("/work/${constants.swigGeneratedJavaBaseDirectory}")
                    val relativeJavaBaseDir =
                        constants.buildDirectory.relativeTo(constants.swigGeneratedJavaBaseDirectory)
                    into.set(project.layout.buildDirectory.dir(relativeJavaBaseDir))
                }

                project.extensions.getByType<SourceSetContainer>().named(SourceSet.MAIN_SOURCE_SET_NAME) {
                    java.srcDir { swig.map { it.into.get() } }
                }
            }
        }
    }

    internal companion object {
        const val taskGroup = "Native Build"
        const val tmpDirectory: String = "tmp/native-tools"
        const val generatedSourcesBaseDir: String = "generated/sources/swig"
        const val generatedResourcesBaseDir: String = "generated/resources/swig"

        /** Same as [Path.relativize] but works over strings. */
        fun String.relativeTo(other: String) =
            Path.of(this).relativize(Path.of(other)).toString()
    }
}
