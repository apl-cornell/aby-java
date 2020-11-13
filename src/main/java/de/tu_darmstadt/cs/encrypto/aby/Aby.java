/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.tu_darmstadt.cs.encrypto.aby;

public class Aby {
  public static Share createNewShare(long size, Circuit circ) {
    long cPtr = AbyJNI.createNewShare__SWIG_0(size, Circuit.getCPtr(circ), circ);
    return (cPtr == 0) ? null : new Share(cPtr, false);
  }

  public static Share createNewShare(UInt32Vector vals, Circuit circ) {
    long cPtr = AbyJNI.createNewShare__SWIG_1(UInt32Vector.getCPtr(vals), vals, Circuit.getCPtr(circ), circ);
    return (cPtr == 0) ? null : new Share(cPtr, false);
  }

  public static Share putInt32DIVGate(Circuit circuit, Share lhs, Share rhs) {
    long cPtr = AbyJNI.putInt32DIVGate(Circuit.getCPtr(circuit), circuit, Share.getCPtr(lhs), lhs, Share.getCPtr(rhs), rhs);
    return (cPtr == 0) ? null : new Share(cPtr, false);
  }

  public static Share putMinGate(Circuit circuit, Share lhs, Share rhs) {
    long cPtr = AbyJNI.putMinGate(Circuit.getCPtr(circuit), circuit, Share.getCPtr(lhs), lhs, Share.getCPtr(rhs), rhs);
    return (cPtr == 0) ? null : new Share(cPtr, false);
  }

  public static Share putMaxGate(Circuit circuit, Share lhs, Share rhs) {
    long cPtr = AbyJNI.putMaxGate(Circuit.getCPtr(circuit), circuit, Share.getCPtr(lhs), lhs, Share.getCPtr(rhs), rhs);
    return (cPtr == 0) ? null : new Share(cPtr, false);
  }

  public static SecurityLevel getST() {
    long cPtr = AbyJNI.ST_get();
    return (cPtr == 0) ? null : new SecurityLevel(cPtr, false);
  }

  public static SecurityLevel getMT() {
    long cPtr = AbyJNI.MT_get();
    return (cPtr == 0) ? null : new SecurityLevel(cPtr, false);
  }

  public static SecurityLevel getLT() {
    long cPtr = AbyJNI.LT_get();
    return (cPtr == 0) ? null : new SecurityLevel(cPtr, false);
  }

  public static SecurityLevel getXLT() {
    long cPtr = AbyJNI.XLT_get();
    return (cPtr == 0) ? null : new SecurityLevel(cPtr, false);
  }

  public static SecurityLevel getXXLT() {
    long cPtr = AbyJNI.XXLT_get();
    return (cPtr == 0) ? null : new SecurityLevel(cPtr, false);
  }

}
