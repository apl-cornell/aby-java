/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.tu_darmstadt.cs.encrypto.aby;

public class ABYParty {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected ABYParty(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ABYParty obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        AbyJNI.delete_ABYParty(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public ABYParty(Role pid, String addr, int port, SecurityLevel seclvl, long bitlen, long nthreads, MultiplicationTripleGenerationAlgorithm mg_algo, long reservegates, String abycircdir) {
    this(AbyJNI.new_ABYParty__SWIG_0(pid.swigValue(), addr, port, SecurityLevel.getCPtr(seclvl), seclvl, bitlen, nthreads, mg_algo.swigValue(), reservegates, abycircdir), true);
  }

  public ABYParty(Role pid, String addr, int port, SecurityLevel seclvl, long bitlen, long nthreads, MultiplicationTripleGenerationAlgorithm mg_algo, long reservegates) {
    this(AbyJNI.new_ABYParty__SWIG_1(pid.swigValue(), addr, port, SecurityLevel.getCPtr(seclvl), seclvl, bitlen, nthreads, mg_algo.swigValue(), reservegates), true);
  }

  public ABYParty(Role pid, String addr, int port, SecurityLevel seclvl, long bitlen, long nthreads, MultiplicationTripleGenerationAlgorithm mg_algo) {
    this(AbyJNI.new_ABYParty__SWIG_2(pid.swigValue(), addr, port, SecurityLevel.getCPtr(seclvl), seclvl, bitlen, nthreads, mg_algo.swigValue()), true);
  }

  public ABYParty(Role pid, String addr, int port, SecurityLevel seclvl, long bitlen, long nthreads) {
    this(AbyJNI.new_ABYParty__SWIG_3(pid.swigValue(), addr, port, SecurityLevel.getCPtr(seclvl), seclvl, bitlen, nthreads), true);
  }

  public ABYParty(Role pid, String addr, int port, SecurityLevel seclvl, long bitlen) {
    this(AbyJNI.new_ABYParty__SWIG_4(pid.swigValue(), addr, port, SecurityLevel.getCPtr(seclvl), seclvl, bitlen), true);
  }

  public ABYParty(Role pid, String addr, int port, SecurityLevel seclvl) {
    this(AbyJNI.new_ABYParty__SWIG_5(pid.swigValue(), addr, port, SecurityLevel.getCPtr(seclvl), seclvl), true);
  }

  public ABYParty(Role pid, String addr, int port) {
    this(AbyJNI.new_ABYParty__SWIG_6(pid.swigValue(), addr, port), true);
  }

  public ABYParty(Role pid, String addr) {
    this(AbyJNI.new_ABYParty__SWIG_7(pid.swigValue(), addr), true);
  }

  public ABYParty(Role pid) {
    this(AbyJNI.new_ABYParty__SWIG_8(pid.swigValue()), true);
  }

  public void connectAndBaseOTs() {
    AbyJNI.ABYParty_connectAndBaseOTs(swigCPtr, this);
  }

  public void execCircuit() {
    AbyJNI.ABYParty_execCircuit(swigCPtr, this);
  }

  public void reset() {
    AbyJNI.ABYParty_reset(swigCPtr, this);
  }

  public double getTiming(Phase phase) {
    return AbyJNI.ABYParty_getTiming(swigCPtr, this, phase.swigValue());
  }

  public java.math.BigInteger getSentData(Phase phase) {
    return AbyJNI.ABYParty_getSentData(swigCPtr, this, phase.swigValue());
  }

  public java.math.BigInteger getReceivedData(Phase phase) {
    return AbyJNI.ABYParty_getReceivedData(swigCPtr, this, phase.swigValue());
  }

  public long getTotalGates() {
    return AbyJNI.ABYParty_getTotalGates(swigCPtr, this);
  }

  public long getTotalDepth() {
    return AbyJNI.ABYParty_getTotalDepth(swigCPtr, this);
  }

  public Circuit getCircuitBuilder(SharingType sharing) {
    long cPtr = AbyJNI.ABYParty_getCircuitBuilder(swigCPtr, this, sharing.swigValue());
    return (cPtr == 0) ? null : new Circuit(cPtr, false);
  }

  public long getNumNonLinearOperations(SharingType sharing) {
    return AbyJNI.ABYParty_getNumNonLinearOperations(swigCPtr, this, sharing.swigValue());
  }

}
