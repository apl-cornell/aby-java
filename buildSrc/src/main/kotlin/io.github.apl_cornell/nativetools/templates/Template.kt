package io.github.apl_cornell.nativetools.templates

import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider

/** A template is a function that transforms [LibraryConstants] to file contents. */
internal open class Template(val name: String, private val build: LibraryConstants.() -> String) {
    /** Applies the template to [constants] and writes it to a file in [directory]. */
    fun generate(constants: LibraryConstants, directory: Provider<Directory>) {
        directory.get().file(name).asFile.writeText(build(constants).trimIndent())
    }

    /** Include this template in another. */
    fun include(constants: LibraryConstants): String =
        build(constants)
}

/** A template for a Makefile. Automatically replaces indentation with tabs. */
internal open class Makefile(name: String, private val build: LibraryConstants.() -> String) :
    Template(name, { replaceSpacesWithTabs(build(this)) }) {
    private companion object {
        // TODO: it would be better if this was not hardcoded
        const val tabLength = 4

        fun replaceSpacesWithTabs(contents: String): String =
            contents.trimIndent().replace(" ".repeat(tabLength), "\t")
    }
}
