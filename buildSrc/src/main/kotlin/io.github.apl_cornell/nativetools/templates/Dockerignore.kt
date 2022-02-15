package io.github.apl_cornell.nativetools.templates

internal val dockerignore = Template(".dockerignore") {
    """
    $buildDirectory
    """
}
