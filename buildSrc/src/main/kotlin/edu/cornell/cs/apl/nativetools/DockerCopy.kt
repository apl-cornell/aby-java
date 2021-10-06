package edu.cornell.cs.apl.nativetools

import org.apache.tools.ant.util.TeeOutputStream
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import java.io.ByteArrayOutputStream
import java.io.File

/** Builds a Docker image and copies files from the image to local host. */
internal fun Project.dockerCopy(
    from: String,
    to: String,
    dockerfile: RegularFile,
    target: String
) {
    fun docker(vararg arguments: String): Pair<String, String> {
        ByteArrayOutputStream().use { stdout ->
            ByteArrayOutputStream().use { stderr ->
                exec {
                    executable = "docker"
                    args = arguments.toList()
                    standardOutput = TeeOutputStream(this.standardOutput, stdout)
                    errorOutput = TeeOutputStream(this.errorOutput, stderr)
                }
                return Pair(stdout.toString(), stderr.toString())
            }
        }
    }

    val imageID =
        docker("buildx", "build", "--file", "$dockerfile", "--target", target, dockerfile.asFile.parent).second.let {
            Regex("sha256:[0-9a-f]*").findAll(it).last().value
        }
    val containerID = docker("create", imageID).first.trim()

    try {
        project.delete(to)
        mkdir(File(to).parent)
        docker("cp", "$containerID:$from", to)
    } finally {
        docker("rm", containerID)
    }
}
