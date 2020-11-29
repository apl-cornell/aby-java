package edu.cornell.cs.apl.nativetools

import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/** Collects all files needed to build a [Library] inside Docker. */
abstract class CollectLibraryTask : DefaultTask() {
    @get:Nested
    abstract val library: Property<Library>

    @get:InputFile
    abstract val patchFile: RegularFileProperty

    @get:InputFile
    abstract val interfaceFile: RegularFileProperty

    @get:OutputDirectory
    val outputDirectory: Provider<Directory>
        get() {
            val nameVersion = library.map { "${it.name}-${it.version}" }
            @Suppress("UnstableApiUsage")
            return project.layout.buildDirectory.dir("tmp/native-tools").dir(nameVersion)
        }

    private val generatedBaseDir: Provider<Directory>
        get() {
            val nameVersion = library.map { "${it.name}-${it.version}" }
            return project.layout.buildDirectory.dir(SwigLibraryPlugin.generatedSrcBaseDir).dir(nameVersion)
        }

    @get:Internal
    val generatedJavaBaseDir: Provider<Directory>
        get() = generatedBaseDir.dir("java")

    private val generatedJavaDir: Provider<Directory>
        get() = generatedJavaBaseDir.dir(packageDir)

    private val generatedCppFile: Provider<RegularFile>
        get() = generatedBaseDir.dir("cpp").file("wrapper.cpp")

    @Internal
    override fun getDescription(): String =
        "Collects files needed to build ${library.get().name} using Docker."

    @Internal
    override fun getGroup(): String =
        SwigLibraryPlugin.taskGroup

    @TaskAction
    fun collect() {
        project.copy {
            from(patchFile)
            into(outputDirectory)
            rename { "lib.patch" }
        }

        project.copy {
            from(interfaceFile)
            into(outputDirectory)
            rename { "lib.i" }
        }

        variablesMakefile().writeTo(outputDirectory.file("lib.mk"))
        downloadMakefile().writeTo(outputDirectory.file("get.mk"))
        outputDirectory.writeResource("build.mk")
        outputDirectory.writeResource("Dockerfile")
        outputDirectory.writeResource(".dockerignore")
    }

    private val packageDir: Provider<String>
        get() =
            library.map { it.packageName.replace(".", "/") }

    /** Returns a Makefile that defines global variables. */
    private fun variablesMakefile(): MakefileBuilder {
        val builder = MakefileBuilder()
        builder.defineVariable("LIB_NAME", library.get().name)
        builder.defineVariable("LIB_GROUP", library.get().group)
        builder.defineVariable("LIB_VERSION", library.get().version)
        builder.defineVariable("LIB_PACKAGE", library.get().packageName)
        builder.defineVariable("LIB_INCLUDE_DIRS", library.get().includeDirectories.joinToString(" "))
        builder.defineVariable("DOWNLOAD_DIR", project.relativePath(project.layout.buildDirectory.dir("downloaded")))
        builder.defineVariable("GENERATED_JAVA_DIR", project.relativePath(generatedJavaDir))
        builder.defineVariable("GENERATED_CPP_FILE", project.relativePath(generatedCppFile))
        return builder
    }

    /** Returns a Makefile for downloading the library source code. */
    private fun downloadMakefile(): MakefileBuilder {
        val builder = MakefileBuilder()

        fun git(vararg commandLine: String, cd: File = File("")) {
            builder.addLine(
                listOf("git") + commandLine.toList(),
                workingDirectory = File("$@").resolve(cd).toString())
        }

        builder.addInclude("lib.mk")

        builder.addRule("$(DOWNLOAD_DIR)/original-source", listOf())
        builder.addLine(listOf("mkdir", "-p", "$@"))

        // Download library
        git("init")
        git("fetch", "--depth", "1", library.get().url, library.get().version)
        git("checkout", library.get().version)

        // Download submodules
        val downloadedSubmodules = mutableSetOf<File>()
        library.get().submodules.forEach { submodule ->
            var parentSubmodule = File("")
            val parts = submodule.split(":")
            assert(parts.isNotEmpty())
            parts.forEach { part ->
                val thisModule = parentSubmodule.resolve(part)
                if (!downloadedSubmodules.contains(thisModule)) {
                    git("submodule", "update", "--init", "--depth", "1", part, cd = parentSubmodule)
                    downloadedSubmodules.add(thisModule)
                }
                parentSubmodule = thisModule
            }
        }

        builder.addLine(listOf("touch", "$@"))
        return builder
    }

    /** Copies Java resource named [resource] into this directory. */
    private fun Provider<Directory>.writeResource(resource: String) =
        this.get().file(resource).asFile.writeBytes(
            CollectLibraryTask::class.java.getResource(resource).readBytes()
        )
}
