package edu.cornell.cs.apl.nativetools

import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

internal fun Provider<Directory>.dir(path: String): Provider<Directory> =
    this.map { it.dir(path) }

internal fun Provider<Directory>.dir(path: Provider<out CharSequence>): Provider<Directory> =
    @Suppress("UnstableApiUsage")
    this.flatMap { it.dir(path) }

internal fun Provider<Directory>.file(path: String): Provider<RegularFile> =
    this.map { it.file(path) }

internal fun Provider<Directory>.file(path: Provider<out CharSequence>): Provider<RegularFile> =
    @Suppress("UnstableApiUsage")
    this.flatMap { it.file(path) }
