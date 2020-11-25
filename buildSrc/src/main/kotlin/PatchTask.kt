import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
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
    val outputDirectory: File
        get() {
            val from = from.get().asFile
            return into.asFile.getOrElse(from.parentFile.resolve("patch-${from.name}"))
        }

    @TaskAction
    fun patch() {
        project.copy {
            from(from)
            into(outputDirectory)
        }

        project.exec {
            workingDir = outputDirectory
            commandLine = listOf("git", "apply", "${patch.get()}")
        }
    }
}
