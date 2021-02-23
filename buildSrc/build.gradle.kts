plugins {
    `kotlin-dsl`
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("de.undercouch:gradle-download-task:4.1.1")
}

gradlePlugin {
    plugins {
        register("swig-library-plugin") {
            id = "swig-library"
            implementationClass = "edu.cornell.cs.apl.nativetools.SwigLibraryPlugin"
        }
    }
}
