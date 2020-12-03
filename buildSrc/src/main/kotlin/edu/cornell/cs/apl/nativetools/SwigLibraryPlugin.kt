package edu.cornell.cs.apl.nativetools

import edu.cornell.cs.apl.nativetools.templates.LibraryConstants
import java.io.File
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

                /**
                 * Configures [DockerCopyTask.from] and [DockerCopyTask.into] from a relative path.
                 * @param relativePath A path relative to the working directory of the Docker image.
                 */
                fun DockerCopyTask.configureRelativePath(target: String, relativePath: String) {
                    this.dockerfile.set(collect.map { it.outputDirectory.get().file("$target.dockerfile") })
                    this.target.set(target)
                    from.set("${constants.dockerWorkDirectory}/$relativePath")
                    // Handle the case where Gradle's build directory is renamed.
                    val relativeToBuildDir =
                        File(relativePath).relativeTo(File(constants.buildDirectory)).toString()
                    into.set(project.layout.buildDirectory.dir(relativeToBuildDir))
                }

                val swig = project.tasks.register<DockerCopyTask>("dockerSwig${library.name}") {
                    configureRelativePath("swig", constants.swigGeneratedJavaBaseDirectory)
                }

                project.tasks.register<DockerCopyTask>("dockerCompileLinux${library.name}") {
                    configureRelativePath("linux", constants.linuxBinaryDirectory)
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
    }
}
