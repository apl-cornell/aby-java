/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.tu_darmstadt.cs.encrypto.aby;

public class AbyJNI {
  public final static native long new_UInt32Vector__SWIG_0();
  public final static native long new_UInt32Vector__SWIG_1(long jarg1, UInt32Vector jarg1_);
  public final static native long UInt32Vector_capacity(long jarg1, UInt32Vector jarg1_);
  public final static native void UInt32Vector_reserve(long jarg1, UInt32Vector jarg1_, long jarg2);
  public final static native boolean UInt32Vector_isEmpty(long jarg1, UInt32Vector jarg1_);
  public final static native void UInt32Vector_clear(long jarg1, UInt32Vector jarg1_);
  public final static native long new_UInt32Vector__SWIG_2(int jarg1, long jarg2);
  public final static native int UInt32Vector_doSize(long jarg1, UInt32Vector jarg1_);
  public final static native void UInt32Vector_doAdd__SWIG_0(long jarg1, UInt32Vector jarg1_, long jarg2);
  public final static native void UInt32Vector_doAdd__SWIG_1(long jarg1, UInt32Vector jarg1_, int jarg2, long jarg3);
  public final static native long UInt32Vector_doRemove(long jarg1, UInt32Vector jarg1_, int jarg2);
  public final static native long UInt32Vector_doGet(long jarg1, UInt32Vector jarg1_, int jarg2);
  public final static native long UInt32Vector_doSet(long jarg1, UInt32Vector jarg1_, int jarg2, long jarg3);
  public final static native void UInt32Vector_doRemoveRange(long jarg1, UInt32Vector jarg1_, int jarg2, int jarg3);
  public final static native long new_ABYParty__SWIG_0(int jarg1, String jarg2, int jarg3, long jarg4, SecurityLevel jarg4_, long jarg5, long jarg6, int jarg7, long jarg8, String jarg9);
  public final static native long new_ABYParty__SWIG_1(int jarg1, String jarg2, int jarg3, long jarg4, SecurityLevel jarg4_, long jarg5, long jarg6, int jarg7, long jarg8);
  public final static native long new_ABYParty__SWIG_2(int jarg1, String jarg2, int jarg3, long jarg4, SecurityLevel jarg4_, long jarg5, long jarg6, int jarg7);
  public final static native long new_ABYParty__SWIG_3(int jarg1, String jarg2, int jarg3, long jarg4, SecurityLevel jarg4_, long jarg5, long jarg6);
  public final static native long new_ABYParty__SWIG_4(int jarg1, String jarg2, int jarg3, long jarg4, SecurityLevel jarg4_, long jarg5);
  public final static native long new_ABYParty__SWIG_5(int jarg1, String jarg2, int jarg3, long jarg4, SecurityLevel jarg4_);
  public final static native long new_ABYParty__SWIG_6(int jarg1, String jarg2, int jarg3);
  public final static native long new_ABYParty__SWIG_7(int jarg1, String jarg2);
  public final static native long new_ABYParty__SWIG_8(int jarg1);
  public final static native void delete_ABYParty(long jarg1);
  public final static native void ABYParty_connectAndBaseOTs(long jarg1, ABYParty jarg1_);
  public final static native void ABYParty_execCircuit(long jarg1, ABYParty jarg1_);
  public final static native void ABYParty_reset(long jarg1, ABYParty jarg1_);
  public final static native double ABYParty_getTiming(long jarg1, ABYParty jarg1_, int jarg2);
  public final static native java.math.BigInteger ABYParty_getSentData(long jarg1, ABYParty jarg1_, int jarg2);
  public final static native java.math.BigInteger ABYParty_getReceivedData(long jarg1, ABYParty jarg1_, int jarg2);
  public final static native long ABYParty_getTotalGates(long jarg1, ABYParty jarg1_);
  public final static native long ABYParty_getTotalDepth(long jarg1, ABYParty jarg1_);
  public final static native long ABYParty_getCircuitBuilder(long jarg1, ABYParty jarg1_, int jarg2);
  public final static native long Circuit_getShareBitLen(long jarg1, Circuit jarg1_);
  public final static native long Circuit_getMaxDepth(long jarg1, Circuit jarg1_);
  public final static native long Circuit_getNumInputBitsForParty(long jarg1, Circuit jarg1_, int jarg2);
  public final static native long Circuit_getNumOutputBitsForParty(long jarg1, Circuit jarg1_, int jarg2);
  public final static native int Circuit_getContext(long jarg1, Circuit jarg1_);
  public final static native long Circuit_getNumGates(long jarg1, Circuit jarg1_);
  public final static native long Circuit_putCONSGate(long jarg1, Circuit jarg1_, java.math.BigInteger jarg2, long jarg3);
  public final static native long Circuit_putINGate(long jarg1, Circuit jarg1_, java.math.BigInteger jarg2, long jarg3, int jarg4);
  public final static native long Circuit_putDummyINGate(long jarg1, Circuit jarg1_, long jarg2);
  public final static native long Circuit_putSharedINGate(long jarg1, Circuit jarg1_, java.math.BigInteger jarg2, long jarg3);
  public final static native long Circuit_putADDGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, long jarg3, Share jarg3_);
  public final static native long Circuit_putSUBGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, long jarg3, Share jarg3_);
  public final static native long Circuit_putANDGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, long jarg3, Share jarg3_);
  public final static native long Circuit_putXORGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, long jarg3, Share jarg3_);
  public final static native long Circuit_putMULGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, long jarg3, Share jarg3_);
  public final static native long Circuit_putGTGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, long jarg3, Share jarg3_);
  public final static native long Circuit_putEQGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, long jarg3, Share jarg3_);
  public final static native long Circuit_putMUXGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, long jarg3, Share jarg3_, long jarg4, Share jarg4_);
  public final static native long Circuit_putUniversalGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, long jarg3, Share jarg3_, long jarg4);
  public final static native long Circuit_putY2BGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_);
  public final static native long Circuit_putB2AGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_);
  public final static native long Circuit_putB2YGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_);
  public final static native long Circuit_putA2YGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_);
  public final static native long Circuit_putY2AGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, long jarg3, Circuit jarg3_);
  public final static native long Circuit_putA2BGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, long jarg3, Circuit jarg3_);
  public final static native long Circuit_putANDVecGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, long jarg3, Share jarg3_);
  public final static native long Circuit_putPrintValueGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, String jarg3);
  public final static native long Circuit_putRepeaterGate__SWIG_0(long jarg1, Circuit jarg1_, long jarg2, long jarg3, Share jarg3_);
  public final static native long Circuit_putRepeaterGate__SWIG_1(long jarg1, Circuit jarg1_, long jarg2, long jarg3);
  public final static native long Circuit_putOUTGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, int jarg3);
  public final static native long Circuit_putSharedOUTGate(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_);
  public final static native long Circuit_putINVGate(long jarg1, Circuit jarg1_, long jarg2);
  public final static native int Circuit_getCircuitType(long jarg1, Circuit jarg1_);
  public final static native int Circuit_getNumCombGates(long jarg1, Circuit jarg1_);
  public final static native int Circuit_getNumStructCombGates(long jarg1, Circuit jarg1_);
  public final static native int Circuit_getNumPermGates(long jarg1, Circuit jarg1_);
  public final static native int Circuit_getNumSubsetGates(long jarg1, Circuit jarg1_);
  public final static native int Circuit_getNumSplitGates(long jarg1, Circuit jarg1_);
  public final static native int Circuit_getRole(long jarg1, Circuit jarg1_);
  public final static native void Circuit_exportCircuitInBristolFormat(long jarg1, Circuit jarg1_, long jarg2, Share jarg2_, long jarg3, Share jarg3_, long jarg4, Share jarg4_, String jarg5);
  public final static native long createNewShare__SWIG_0(long jarg1, long jarg2, Circuit jarg2_);
  public final static native long createNewShare__SWIG_1(long jarg1, UInt32Vector jarg1_, long jarg2, Circuit jarg2_);
  public final static native long Share_getWires(long jarg1, Share jarg1_);
  public final static native long Share_getWireId(long jarg1, Share jarg1_, long jarg2);
  public final static native long Share_getWireIdsAsShare(long jarg1, Share jarg1_, long jarg2);
  public final static native void Share_setWireId(long jarg1, Share jarg1_, long jarg2, long jarg3);
  public final static native void Share_setWireIds(long jarg1, Share jarg1_, long jarg2, UInt32Vector jarg2_);
  public final static native long Share_getBitlength(long jarg1, Share jarg1_);
  public final static native void Share_setBitlength(long jarg1, Share jarg1_, long jarg2);
  public final static native long Share_getMaxBitlength(long jarg1, Share jarg1_);
  public final static native void Share_setMaxBitlength(long jarg1, Share jarg1_, long jarg2);
  public final static native long Share_getNvals(long jarg1, Share jarg1_);
  public final static native long Share_getNvalsOnWire(long jarg1, Share jarg1_, long jarg2);
  public final static native int Share_getCircuitType(long jarg1, Share jarg1_);
  public final static native int Share_getShareType(long jarg1, Share jarg1_);
  public final static native short Share_getClearValue8(long jarg1, Share jarg1_);
  public final static native int Share_getClearValue16(long jarg1, Share jarg1_);
  public final static native long Share_getClearValue32(long jarg1, Share jarg1_);
  public final static native java.math.BigInteger Share_getClearValue64(long jarg1, Share jarg1_);
  public final static native long ST_get();
  public final static native long MT_get();
  public final static native long LT_get();
  public final static native long XLT_get();
  public final static native long XXLT_get();
  public final static native void SecurityLevel_statbits_set(long jarg1, SecurityLevel jarg1_, long jarg2);
  public final static native long SecurityLevel_statbits_get(long jarg1, SecurityLevel jarg1_);
  public final static native void SecurityLevel_symbits_set(long jarg1, SecurityLevel jarg1_, long jarg2);
  public final static native long SecurityLevel_symbits_get(long jarg1, SecurityLevel jarg1_);
  public final static native void SecurityLevel_ifcbits_set(long jarg1, SecurityLevel jarg1_, long jarg2);
  public final static native long SecurityLevel_ifcbits_get(long jarg1, SecurityLevel jarg1_);
  public final static native long new_SecurityLevel();
}
