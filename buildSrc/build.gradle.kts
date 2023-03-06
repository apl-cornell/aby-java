plugins {
    `kotlin-dsl`

    // Style checking
    id("com.diffplug.spotless") version "6.16.0"
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("de.undercouch:gradle-download-task:4.1.1")
}

spotless {
    kotlin {
        ktlint()
    }

    kotlinGradle {
        ktlint()
    }
}

gradlePlugin {
    plugins {
        register("swig-library-plugin") {
            id = "swig-library"
            implementationClass = "io.github.apl_cornell.nativetools.SwigLibraryPlugin"
        }
    }
}
