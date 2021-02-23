package edu.cornell.cs.apl.nativetools.templates

internal val dockerignore = Template(".dockerignore") {
    """
    $buildDirectory
    """
}
