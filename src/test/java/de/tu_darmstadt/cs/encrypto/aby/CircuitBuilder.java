package de.tu_darmstadt.cs.encrypto.aby;

/** A function that builds a single-output circuit. */
interface CircuitBuilder {
  Share build(Circuit circuit);
}
