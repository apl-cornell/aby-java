package edu.cornell.cs.apl.nativetools

import org.gradle.api.provider.ListProperty

abstract class SwigLibraryPluginExtension {
    abstract val libraries: ListProperty<Library>
}
