package io.github.apl_cornell.aby;

/** A function that builds a single-output circuit. */
interface CircuitBuilder {
  Share build(Circuit circuit);
}
