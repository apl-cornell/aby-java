package edu.cornell.cs.apl.nativetools

import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

internal class MakefileBuilder {
    private val file = StringBuilder()

    fun defineVariable(name: String, value: String) {
        file.appendln("$name := $value")
    }

    fun addRule(targets: Iterable<String>, prerequisites: Iterable<String>, phony: Boolean = false) {
        file.appendln()
        val joinedTargets = targets.joinToString(" ")
        if (phony)
            file.appendln(".PHONY: $joinedTargets")
        file.appendln("$joinedTargets: ${prerequisites.joinToString(" ")}")
    }

    fun addRule(target: String, prerequisites: Iterable<String>, phony: Boolean = false) =
        addRule(listOf(target), prerequisites, phony)

    fun addLine(commandLine: Iterable<String>, workingDirectory: String = "") {
        val command = commandLine.joinToString(" ")
        file.append('\t')
        if (workingDirectory.isNotBlank())
            file.append("cd $workingDirectory && ")
        file.appendln(command)
    }

    fun addInclude(path: String) {
        file.appendln("include $path")
    }

    fun build(): String = file.toString()

    fun writeTo(file: RegularFile) {
        file.asFile.writeText(build())
    }

    fun writeTo(file: Provider<RegularFile>) {
        writeTo(file.get())
    }
}
