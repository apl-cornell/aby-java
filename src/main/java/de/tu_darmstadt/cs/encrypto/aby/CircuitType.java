/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.tu_darmstadt.cs.encrypto.aby;

public enum CircuitType {
  C_BOOLEAN(0),
  C_ARITHMETIC(1);

  public final int swigValue() {
    return swigValue;
  }

  public static CircuitType swigToEnum(int swigValue) {
    CircuitType[] swigValues = CircuitType.class.getEnumConstants();
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (CircuitType swigEnum : swigValues)
      if (swigEnum.swigValue == swigValue)
        return swigEnum;
    throw new IllegalArgumentException("No enum " + CircuitType.class + " with value " + swigValue);
  }

  @SuppressWarnings("unused")
  private CircuitType() {
    this.swigValue = SwigNext.next++;
  }

  @SuppressWarnings("unused")
  private CircuitType(int swigValue) {
    this.swigValue = swigValue;
    SwigNext.next = swigValue+1;
  }

  @SuppressWarnings("unused")
  private CircuitType(CircuitType swigEnum) {
    this.swigValue = swigEnum.swigValue;
    SwigNext.next = this.swigValue+1;
  }

  private final int swigValue;

  private static class SwigNext {
    private static int next = 0;
  }
}

