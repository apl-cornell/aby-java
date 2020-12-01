package edu.cornell.cs.apl.nativetools.templates

import java.nio.file.Path

/** Makefile for downloading library source code. */
internal val getMakefile = Makefile("get.mk") {
    val submoduleCommands = run {
        val commands = mutableListOf<String>()
        val seenSubmodules = mutableSetOf<Path>()
        library.submodules.forEach { submodule ->
            var parentSubmodule = Path.of("")
            submodule.split(":").forEach { part ->
                val thisSubmodule = parentSubmodule.resolve(part)
                if (seenSubmodules.add(thisSubmodule)) {
                    val workingDirectory = LibraryConstants.unixPath(Path.of("$@").resolve(parentSubmodule))
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
