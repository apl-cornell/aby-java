plugins {
    `kotlin-dsl`
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

repositories {
    gradlePluginPortal()
}

gradlePlugin {
    plugins {
        register("swig-library-plugin") {
            id = "swig-library"
            implementationClass = "edu.cornell.cs.apl.nativetools.SwigLibraryPlugin"
        }
    }
}
