package edu.cornell.cs.apl.nativetools.templates

/** Makefile for building the library. */
internal val buildMakefile = Makefile("build.mk") {
    val includes = library.includeDirectories.joinToString(" ") { "-I$</$it" }
    """
    include ${getMakefile.name}

    $patchedSourceDirectory: $originalSourceDirectory $patchFile
        rsync -au --delete --exclude=.git $</ $@
        git apply $patchFile --directory $@
        touch $@

    $swigGeneratedCppFile $swigGeneratedJavaDirectory: \
        $patchedSourceDirectory $swigFile
        rm -rf $swigGeneratedJavaDirectory
        mkdir -p $swigGeneratedJavaDirectory
        mkdir -p $(dir $swigGeneratedCppFile)
        swig \
            -Wall -Werror -macroerrors \
            -c++ \
            -java -package ${library.packageName} \
            $includes \
            -o $swigGeneratedCppFile -outdir $swigGeneratedJavaDirectory \
            $swigFile

    .PHONY: swig
    swig: $swigGeneratedCppFile \
          $swigGeneratedJavaDirectory
    """
}
