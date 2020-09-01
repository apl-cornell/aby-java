package de.tu_darmstadt.cs.encrypto.aby;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.SocketUtils;

class AbyTest {
  private static String address;
  private static int port;

  @BeforeAll
  static void initialize() throws UnknownHostException {
    address = InetAddress.getByName("localhost").getHostAddress();
    port = SocketUtils.findAvailableTcpPort();
  }

  @Test
  void emptyCircuit() {
    ABYParty server = new ABYParty(Role.SERVER, address, port);
    ABYParty client = new ABYParty(Role.CLIENT, address, port);

    client.delete();
    server.delete();
  }

  @Test
  void constantGate() {
    final int value = 42;
    final CircuitBuilder builder =
        (circuit, bitLength) -> circuit.putCONSGate(BigInteger.valueOf(value), bitLength);
    testCircuit(builder, SharingType.S_YAO, 6, value);
  }

  /** Asserts that the given single output circuit produces the expected result. */
  private void testCircuit(
      CircuitBuilder circuitBuilder, SharingType sharingType, int bitLength, int expectedResult) {
    final ExecutorService pool = Executors.newFixedThreadPool(2);

    // Run the code of each party in a separate thread.
    final Future<BigInteger> serverFuture =
        pool.submit(runCircuitAs(Role.SERVER, circuitBuilder, sharingType, bitLength));
    final Future<BigInteger> clientFuture =
        pool.submit(runCircuitAs(Role.CLIENT, circuitBuilder, sharingType, bitLength));

    // Retrieve each party's result.
    final BigInteger serverResult;
    final BigInteger clientResult;
    try {
      serverResult = serverFuture.get();
      clientResult = clientFuture.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new Error(e);
    }

    assertEquals(expectedResult, serverResult.intValue(), "Wrong output for server");
    assertEquals(expectedResult, clientResult.intValue(), "Wrong output for client");

    pool.shutdown();
  }

  /** Executes the given single output circuit as the given role and returns the result. */
  private Callable<BigInteger> runCircuitAs(
      Role role, CircuitBuilder circuitBuilder, SharingType sharingType, int bitLength) {
    return () -> {
      final ABYParty party = new ABYParty(role, address, port, Aby.getLT(), bitLength);

      // Build the circuit
      final Circuit circuit = party.getCircuitBuilder(sharingType);
      final Share intermediateShare = circuitBuilder.build(circuit, bitLength);
      final Share resultShare = circuit.putOUTGate(intermediateShare, Role.ALL);

      // Retrieve circuit output
      party.execCircuit();
      final BigInteger result = resultShare.getClearValue64();

      party.delete();
      return result;
    };
  }
}
