package edu.cornell.cs.apl.nativetools

import java.io.ByteArrayOutputStream
import java.io.File
import org.apache.tools.ant.util.TeeOutputStream
import org.gradle.api.Project
import org.gradle.api.file.RegularFile

/** Builds a Docker image and copies files from the image to local host. */
internal fun Project.dockerCopy(
    from: String,
    to: String,
    dockerfile: RegularFile,
    target: String
) {
    fun docker(vararg arguments: String): String {
        ByteArrayOutputStream().use { os ->
            exec {
                executable = "docker"
                args = arguments.toList()
                standardOutput = TeeOutputStream(this.standardOutput, os)
            }
            return os.toString()
        }
    }

    val imageID =
        docker("build", "--file", "$dockerfile", "--target", target, dockerfile.asFile.parent)
            .lines().dropLast(1).last().trim().split(" ")[2]
    val containerID = docker("create", imageID).trim()

    try {
        project.delete(to)
        mkdir(File(to).parent)
        docker("cp", "$containerID:$from", to)
    } finally {
        docker("rm", containerID)
    }
}
