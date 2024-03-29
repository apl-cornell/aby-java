package io.github.apl_cornell.nativetools.templates

import java.nio.file.Path
import java.nio.file.Paths

/** Makefile for downloading library source code. */
internal val getMakefile = Makefile("get.mk") {
    val submoduleCommands = run {
        val commands = mutableListOf<String>()
        val seenSubmodules = mutableSetOf<Path>()
        library.submodules.forEach { submodule ->
            var parentSubmodule = Paths.get("")
            submodule.split(":").forEach { part ->
                val thisSubmodule = parentSubmodule.resolve(part)
                if (seenSubmodules.add(thisSubmodule)) {
                    val workingDirectory = LibraryConstants.unixPath(Paths.get("$@").resolve(parentSubmodule))
                    val partPath = LibraryConstants.unixPath(part)
                    commands += "cd $workingDirectory && git submodule update --init --depth 1 $partPath"
                }
                parentSubmodule = thisSubmodule
            }
        }
        commands
    }

    """
    $originalSourceDirectory:
        mkdir -p $@
        cd $@ && git init
        cd $@ && git fetch --depth 1 ${library.url} ${library.version}
        cd $@ && git checkout --quiet ${library.version}

        # Download submodules
        ${submoduleCommands.joinToString("\n    \t")}

        touch $@
    """
}
