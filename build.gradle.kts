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

val downloadDir = buildDir.resolve("downloaded-src")
val generatedSourcesDir = buildDir.resolve("generated-src")
val generatedResourcesDir = buildDir.resolve("generated-resources")

val abyUrl = "https://github.com/apl-cornell/ABY"

val downloadAby by tasks.registering {
    description = "Downloads the ABY source code"

    val output = downloadDir.resolve("ABY-$abyVersion")
    outputs.dir(output)

    fun git(vararg args: String, wd: File = output) =
        exec {
            workingDir = wd
            commandLine = listOf("git") + args
        }

    doLast {
        mkdir(output)
        git("init")
        git("fetch", "--depth", "1", abyUrl, abyVersion)
        git("checkout", abyVersion)
        git("submodule", "update", "--init", "--depth", "1")
        git("submodule", "update", "--init", "--depth", "1", wd = output.resolve("extern/ENCRYPTO_utils"))
    }
}

val patchAby by tasks.registering {
    description = "Patches the ABY source code"

    val input = downloadAby.get().outputs.files.singleFile
    val output = downloadDir.resolve("patched-ABY-$abyVersion")
    inputs.dir(input)
    outputs.dir(output)
    dependsOn(downloadAby)

    doLast {
        copy {
            from(input)
            into(output)
        }

        exec {
            workingDir = output
            commandLine = listOf("git", "apply", project.file("aby.patch").path)
        }
    }
}

val swigABY by tasks.registering {
    description = "Generates the Java interface using SWIG"

    val input = patchAby.get().outputs.files.singleFile
    val abyPackage = "$abyGroup.aby"
    val javaOutput = generatedSourcesDir.resolve("swig/java/${abyPackage.replace(".", "/")}")
    val cppOutput = generatedSourcesDir.resolve("swig/cpp/aby_wrap.cpp")

    inputs.dir(input)
    outputs.dir(javaOutput)
    outputs.file(cppOutput)
    dependsOn(patchAby)

    doLast {
        exec {
            commandLine = listOf(
                "swig",
                "-Wall", "-Werror", "-macroerrors",
                "-c++",
                "-java", "-package", abyPackage,
                "-I$input/src", "-I$input/extern/ENCRYPTO_utils/src",
                "-o", cppOutput.path, "-outdir", javaOutput.path,
                "aby.i"
            )
        }
    }
}
