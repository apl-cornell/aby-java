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
    testImplementation("org.springframework:spring-core:5.3.2")
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
