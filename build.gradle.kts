import edu.cornell.cs.apl.nativetools.DownloadLibraryTask
import edu.cornell.cs.apl.nativetools.Library
import edu.cornell.cs.apl.nativetools.PatchTask
import edu.cornell.cs.apl.nativetools.SwigLibraryTask
import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    `java-library`
    `maven-publish`

    // Style checking
    id("com.diffplug.spotless") version "5.1.0"

    jacoco
}

val abyGroup: String by project
val abyVersion: String by project

group = abyGroup

version = abyVersion.substring(0..6)

repositories {
    jcenter()
}

/** Java Version */

java {
    sourceCompatibility = VERSION_1_8
    targetCompatibility = VERSION_1_8
}

dependencies {
    implementation("org.scijava:native-lib-loader:2.3.4")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0-M1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.0-M1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0-M1")

    // Logging during testing
    testImplementation("org.slf4j:slf4j-simple:1.7.30")

    // Getting a free port
    testImplementation("org.springframework:spring-core:5.2.8.RELEASE")
}

/** Style */

spotless {
    java {
        val abyPath = "src/main/java/$group/aby".replace(".", "/")
        targetExclude("$abyPath/*.java")
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
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

/** Building Native Binaries */

val aby = Library(
    name = "ABY", group = abyGroup, version = abyVersion, url = "https://github.com/apl-cornell/ABY"
)

val downloadDir = buildDir.resolve("downloaded-src")
val generatedSourcesDir = buildDir.resolve("generated-src")
val generatedResourcesDir = buildDir.resolve("generated-resources")

val downloadAby by tasks.registering(DownloadLibraryTask::class) {
    library.set(aby)
    submodules.addAll(
        "extern/ENCRYPTO_utils",
        "extern/ENCRYPTO_utils:extern/relic",
        "extern/OTExtension"
    )
}

val patchAby by tasks.registering(PatchTask::class) {
    description = "Patches ABY source code."
    dependsOn(downloadAby)

    from.set(downloadAby.get().outputDirectory)
    patch.set(project.file("${aby.name}.patch"))
}

val swigAby by tasks.registering(SwigLibraryTask::class) {
    dependsOn(patchAby)

    library.set(aby)
    source.set(patchAby.get().outputDirectory)
    includeDirectories.addAll("src", "extern/ENCRYPTO_utils/src")
}
