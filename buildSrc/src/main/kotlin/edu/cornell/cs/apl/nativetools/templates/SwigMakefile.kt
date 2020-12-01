package edu.cornell.cs.apl.nativetools.templates

/** Makefile for generating the Java interface using SWIG. */
internal val swigMakefile = Makefile("swig.mk") {
    val includes = library.includeDirectories.joinToString(" ") { "-I$</$it" }
    """
    .PHONY: swig
    swig: $swigGeneratedCppFile \
          $swigGeneratedJavaDirectory

    include ${getMakefile.name}

    PATCH_FILE := $(abspath $patchFile)
    $patchedSourceDirectory: $originalSourceDirectory $patchFile
        rsync -a --delete $</ $@
        cd $@ && git apply $(PATCH_FILE)
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
            -o $swigGeneratedCppFile \
            -outdir $swigGeneratedJavaDirectory \
            $swigFile
    """
}
