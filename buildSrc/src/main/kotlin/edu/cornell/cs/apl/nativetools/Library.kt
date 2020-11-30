package edu.cornell.cs.apl.nativetools

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal

data class Library(
    @Input
    val name: String,

    @Input
    val group: String,

    @Input
    val version: String,

    /** Git repository URL for the library. */
    @Internal
    val url: String,

    /**
     * Git submodules of the library that should also be downloaded. Submodules of submodules should be
     * separated with a colon `:`. For example, `modules/a:external/b:c`.
     */
    @Input
    val submodules: Iterable<String> = listOf(),

    /** Include paths for SWIG relative to library root. */
    @Input
    val includeDirectories: Iterable<String>
) {
    @get:Internal
    val packageName: String
        get() = "$group.${name.toLowerCase().replace("-", "_")}"
}
