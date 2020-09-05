package de.tu_darmstadt.cs.encrypto.aby;

/**
 * A supertype for binary gate adding methods such as {@link Circuit#putADDGate(Share, Share)} and
 * {@link Circuit#putXORGate(Share, Share)}.
 */
interface BinaryGate {
  Share put(Circuit circuit, Share ina, Share inb);
}
