package de.tu_darmstadt.cs.encrypto.aby;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

class AbyRunner {
  private final ABYParty abyParty;
  private final Thread thread;
  private final BlockingQueue<Function<ABYParty, Long>> input = new LinkedBlockingQueue<>();
  private final BlockingQueue<Long> output = new LinkedBlockingQueue<>();

  public AbyRunner(Role role, String address, int port) {
    abyParty = new ABYParty(role, address, port, Aby.getLT(), 32, 1);
    thread =
        new Thread(
            () -> {
              while (true) {
                try {
                  output.put(input.take().apply(abyParty));
                } catch (InterruptedException e) {
                  // Stop was called.
                }
              }
            },
            "ABY " + role.toString());
    thread.start();
  }

  public void put(Function<ABYParty, Long> command) throws InterruptedException {
    input.put(command);
  }

  public long get() throws InterruptedException {
    return output.take();
  }

  public void stop() {
    thread.interrupt();
  }
}
