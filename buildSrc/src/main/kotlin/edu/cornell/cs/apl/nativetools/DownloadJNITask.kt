package edu.cornell.cs.apl.nativetools

import de.undercouch.gradle.tasks.download.DownloadAction
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class DownloadJNITask : DefaultTask() {
    @get:Internal
    val jdkURL: String
        get() = "https://raw.githubusercontent.com/openjdk/jdk"

    @get:Input
    val jdkVersion: String
        get() = "jdk-11+28"

    @get:OutputDirectory
    val outputDirectory: Provider<Directory>
        get() {
            val nameVersion = "jni-$jdkVersion"
            return project.layout.buildDirectory.dir(SwigLibraryPlugin.tmpDirectory).dir(nameVersion)
        }

    @Internal
    override fun getDescription(): String =
        "Downloads Java Native Interface header files."

    @Internal
    override fun getGroup(): String =
        SwigLibraryPlugin.taskGroup

    @TaskAction
    fun downloadJNI() {
        outputDirectory.downloadFile("src/java.base/share/native/include/jni.h")
        outputDirectory.map { it.dir("unix") }.downloadFile("src/java.base/unix/native/include/jni_md.h")
        outputDirectory.map { it.dir("windows") }.downloadFile("src/java.base/windows/native/include/jni_md.h")
    }

    private fun Provider<Directory>.downloadFile(relativeFilePath: String) {
        val fileName = File(relativeFilePath).name
        val destination = this.get().file(fileName).asFile
        DownloadAction(project).apply {
            src("$jdkURL/$jdkVersion/$relativeFilePath")
            dest(destination)
            execute()
        }
    }
}
