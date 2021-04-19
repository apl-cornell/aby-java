import edu.cornell.cs.apl.nativetools.Library
import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    `java-library`
    `maven-publish`

    // Style checking
    id("com.diffplug.spotless") version "5.12.1"

    // Testing
    jacoco

    // Generating Native Libraries
    `swig-library`
}

val abyGroup: String by project
val abyVersion: String by project

group = abyGroup

version = abyVersion.substring(0..6)

/** Java Version */

java {
    sourceCompatibility = VERSION_1_8
    targetCompatibility = VERSION_1_8
}

dependencies {
    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")

    // Logging during testing
    testImplementation("org.slf4j:slf4j-simple:1.7.30")

    // Getting a free port
    testImplementation("org.springframework:spring-core:5.3.4")
}

/** Style */

spotless {
    java {
        targetExclude("${project.relativePath(project.layout.buildDirectory)}/**/*.java")
        googleJavaFormat()
    }

    kotlinGradle {
        ktlint()
    }
}

/** Testing */

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
    dependsOn(tasks.test)
}

/** Publishing */

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            version = System.getenv("GITHUB_SHA")
            url = uri("https://maven.pkg.github.com/${System.getenv("GITHUB_REPOSITORY")}")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

/** Building Native Binaries */

swigLibrary {
    libraries.add(
        Library(
            name = "ABY",
            group = abyGroup,
            version = abyVersion,
            url = "https://github.com/apl-cornell/ABY",
            submodules = listOf(
                "extern/ENCRYPTO_utils",
                "extern/ENCRYPTO_utils:extern/relic",
                "extern/OTExtension"
            ),
            includeDirectories = listOf("src", "extern/ENCRYPTO_utils/src")
        )
    )
}
