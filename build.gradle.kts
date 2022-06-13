import io.github.apl_cornell.nativetools.Library
import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    `java-library`

    // Publishing
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"

    // Versioning
    id("pl.allegro.tech.build.axion-release") version "1.13.6"

    // Style checking
    id("com.diffplug.spotless") version "6.7.2"

    // Testing
    jacoco

    // Generating Native Libraries
    `swig-library`
}

group = "io.github.apl-cornell"

// Compute version from source control
scmVersion.tag.prefix = ""
version = scmVersion.version

/** Java Version */

java {
    sourceCompatibility = VERSION_1_8
    targetCompatibility = VERSION_1_8

    withJavadocJar()
    withSourcesJar()
}

dependencies {
    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // Logging during testing
    testImplementation("org.slf4j:slf4j-simple:1.7.36")

    // Getting a free port
    testImplementation("org.springframework:spring-core:5.3.15")
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
        xml.required.set(true)
        html.required.set(true)
    }
    dependsOn(tasks.test)
}

/** Building Native Binaries */

swigLibrary {
    libraries.add(
        Library(
            name = "ABY",
            group = (group as String).replace('-', '_'),
            version = "e6f75f784af8a93e54a4d79a9cbd9e496065e77e",
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

/** Publishing */

publishing {
    val repository = System.getenv("GITHUB_REPOSITORY") ?: "apl-cornell/aby-java"
    val repositoryUrl = "github.com/$repository"

    publications {
        create<MavenPublication>("default") {
            from(components["java"])

            pom {
                name.set("ABY Java")
                description.set("Java bindings for the ABY framework.")
                url.set("https://$repositoryUrl")

                licenses {
                    license {
                        name.set("GNU Lesser General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/lgpl-3.0.en.html")
                    }
                }

                developers {
                    developer {
                        name.set("Coşku Acay")
                        email.set("coskuacay@gmail.com")
                        url.set("https://github.com/cacay")
                    }
                }

                scm {
                    connection.set("scm:git:git://$repositoryUrl.git")
                    developerConnection.set("scm:git:ssh://$repositoryUrl.git")
                    url.set("https://$repositoryUrl")
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.$repositoryUrl")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    isRequired = signingKey != null
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}
