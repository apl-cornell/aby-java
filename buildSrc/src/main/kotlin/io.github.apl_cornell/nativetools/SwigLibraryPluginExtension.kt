package io.github.apl_cornell.nativetools

import org.gradle.api.provider.ListProperty

abstract class SwigLibraryPluginExtension {
    abstract val libraries: ListProperty<Library>
}
