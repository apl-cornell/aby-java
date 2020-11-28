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

    @Internal
    val url: String,

    @Input
    val submodules: Iterable<String> = listOf(),

    @Input
    val includeDirectories: Iterable<String> = listOf()
) {
    @get:Internal
    val packageName: String
        get() = "$group.${name.toLowerCase().replace("-", "_")}"
}
