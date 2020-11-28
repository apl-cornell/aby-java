import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class SwigLibraryTask : DefaultTask() {
    @get:Nested
    abstract val library: Property<Library>

    @get:InputDirectory
    abstract val source: DirectoryProperty

    @get:Input
    abstract val includeDirectories: ListProperty<String>

    @get:InputFile
    val interfaceFile: File
        get() = project.file("${library.get().name}.i")

    @get:OutputFile
    val cppOutput: File
        get() {
            val dir = project.buildDir.resolve(generatedSrcBaseDir).resolve("cpp/main").resolve(packageDir)
            val fileName = "${library.get().name}-wrap.cpp"
            return dir.resolve(fileName)
        }

    @get:OutputDirectory
    val javaOutputDirectory: File
        get() = project.buildDir.resolve(generatedSrcBaseDir).resolve("java/main").resolve(packageDir)

    @Internal
    override fun getDescription(): String {
        return "Generates a Java interface for ${library.get().name} with SWIG."
    }

    @TaskAction
    fun generateInterface() {
        val includes = includeDirectories.get().map { "-I${source.get().asFile.resolve(it)}" }
        project.exec {
            commandLine = listOf(
                "swig",
                "-Wall", "-Werror", "-macroerrors",
                "-c++",
                "-java", "-package", library.get().packageName
            ) + includes + listOf(
                "-o", "$cppOutput", "-outdir", "$javaOutputDirectory",
                "$interfaceFile"
            )
        }
    }

    private val packageDir: String
        get() =
            library.get().packageName.replace(".", "/")

    companion object {
        const val generatedSrcBaseDir: String = "generated/sources/swig"
    }
}
