package de.tu_darmstadt.cs.encrypto.aby;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.util.SocketUtils;

class AbyTest {
  private static AbyRunner client;
  private static AbyRunner server;

  @BeforeAll
  static void initialize() throws UnknownHostException {
    final String address = InetAddress.getByName("localhost").getHostAddress();
    final int port = SocketUtils.findAvailableTcpPort();
    client = new AbyRunner(Role.CLIENT, address, port);
    server = new AbyRunner(Role.SERVER, address, port);
  }

  @AfterAll
  static void shutdown() {
    client.stop();
    server.stop();
  }

  @ParameterizedTest
  @EnumSource
  void constantGate(SharingType sharingType) {
    final int value = 42;
    final CircuitBuilder builder = (circuit) -> circuit.putCONSGate(BigInteger.valueOf(value), 6);
    testCircuit(builder, sharingType, value);
  }

  @ParameterizedTest
  @EnumSource(
      mode = EnumSource.Mode.EXCLUDE,
      names = {"S_YAO_REV"})
  void addGate(SharingType sharingType) {
    final BinaryGate gate = Circuit::putADDGate;
    final BinaryOperator<Integer> evaluate = Integer::sum;

    // Positive numbers
    testBinaryGate(gate, evaluate, sharingType, 1, 2);
    testBinaryGate(gate, evaluate, sharingType, 8, 5);
    testBinaryGate(gate, evaluate, sharingType, 120, 348);

    // Negative numbers
    testBinaryGate(gate, evaluate, sharingType, 2, -2);
    testBinaryGate(gate, evaluate, sharingType, 2, -4);
    testBinaryGate(gate, evaluate, sharingType, -2, -2);
  }

  @ParameterizedTest
  @EnumSource(
      mode = EnumSource.Mode.EXCLUDE,
      names = {"S_YAO_REV"})
  void subGate(SharingType sharingType) {
    final BinaryGate gate = Circuit::putSUBGate;
    final BinaryOperator<Integer> evaluate = (a, b) -> a - b;

    // Positive numbers
    testBinaryGate(gate, evaluate, sharingType, 2, 1);
    testBinaryGate(gate, evaluate, sharingType, 8, 5);
    testBinaryGate(gate, evaluate, sharingType, 400, 150);

    // Negative numbers
    testBinaryGate(gate, evaluate, sharingType, 2, 4);
    testBinaryGate(gate, evaluate, sharingType, -2, 4);
    testBinaryGate(gate, evaluate, sharingType, 2, -4);
    testBinaryGate(gate, evaluate, sharingType, -2, -4);
  }

  @ParameterizedTest
  @EnumSource(
      mode = EnumSource.Mode.EXCLUDE,
      names = {"S_YAO_REV"})
  void mulGate(SharingType sharingType) {
    final BinaryGate gate = Circuit::putMULGate;
    final BinaryOperator<Integer> evaluate = (a, b) -> a * b;

    // Positive numbers
    testBinaryGate(gate, evaluate, sharingType, 2, 1);
    testBinaryGate(gate, evaluate, sharingType, 8, 5);
    testBinaryGate(gate, evaluate, sharingType, 400, 150);

    // Negative numbers
    testBinaryGate(gate, evaluate, sharingType, 2, -2);
    testBinaryGate(gate, evaluate, sharingType, 0, -4);
    testBinaryGate(gate, evaluate, sharingType, -2, -2);
  }

  @ParameterizedTest
  @EnumSource(
      mode = EnumSource.Mode.EXCLUDE,
      names = {"S_ARITH", "S_YAO_REV"})
  void andGate(SharingType sharingType) {
    final BinaryGate gate = Circuit::putANDGate;
    final BinaryOperator<Integer> evaluate = (a, b) -> a & b;
    testBinaryGate(gate, evaluate, sharingType, 0x1, 0x2);
    testBinaryGate(gate, evaluate, sharingType, 0xFFF, 0xF01);
  }

  @ParameterizedTest
  @EnumSource(
      mode = EnumSource.Mode.EXCLUDE,
      names = {"S_ARITH", "S_YAO_REV"})
  void xorGate(SharingType sharingType) {
    final BinaryGate gate = Circuit::putXORGate;
    final BinaryOperator<Integer> evaluate = (a, b) -> a ^ b;
    testBinaryGate(gate, evaluate, sharingType, 0x1, 0x2);
    testBinaryGate(gate, evaluate, sharingType, 0xFFF, 0xF01);
  }

  private static void testBinaryGate(
      BinaryGate gate,
      BinaryOperator<Integer> evaluate,
      SharingType sharingType,
      int serverInput,
      int clientInput) {
    final CircuitBuilder builder =
        (circuit) -> {
          Share serverInputShare = putINGate(circuit, serverInput, Role.SERVER);
          Share clientInputShare = putINGate(circuit, clientInput, Role.CLIENT);
          return gate.put(circuit, serverInputShare, clientInputShare);
        };
    testCircuit(builder, sharingType, evaluate.apply(serverInput, clientInput));
  }

  /**
   * Similar to {@link Circuit#putINGate(BigInteger, long, Role)}, but automatically computes the
   * bit length.
   */
  private static Share putINGate(Circuit circuit, int value, Role role) {
    if (value > 0) {
      final BigInteger bigValue = BigInteger.valueOf(value);
      return circuit.putINGate(bigValue, bigValue.bitLength(), role);
    } else {
      final int bitLength = 32;
      final BigInteger bigValue =
          BigInteger.valueOf(2).pow(bitLength).add(BigInteger.valueOf(value));
      return circuit.putINGate(bigValue, bitLength, role);
    }
  }

  /** Asserts that the given single-output circuit produces the expected result. */
  private static void testCircuit(
      CircuitBuilder circuitBuilder, SharingType sharingType, int expectedResult) {
    final Function<ABYParty, Long> command =
        (abyParty) -> {
          // Build the circuit
          final Circuit circuit = abyParty.getCircuitBuilder(sharingType);
          final Share intermediateShare = circuitBuilder.build(circuit);
          final Share resultShare = circuit.putOUTGate(intermediateShare, Role.ALL);

          // Retrieve circuit output
          abyParty.execCircuit();
          final long result = resultShare.getClearValue32();
          abyParty.reset();
          return result;
        };

    try {
      client.put(command);
      server.put(command);
      assertEquals(expectedResult, (int) client.get(), "Wrong output for client");
      assertEquals(expectedResult, (int) server.get(), "Wrong output for server");
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
