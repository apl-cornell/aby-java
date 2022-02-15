package io.github.apl_cornell.aby;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiFunction;

class AbyRunner {
  private final Thread thread;
  private final BlockingQueue<BiFunction<ABYParty, Role, Long>> input = new LinkedBlockingQueue<>();
  private final BlockingQueue<Long> output = new LinkedBlockingQueue<>();

  public AbyRunner(Role role, String address, int port) {
    final ABYParty abyParty = new ABYParty(role, address, port, Aby.getLT(), 32, 1);
    thread =
        new Thread(
            () -> {
              while (true) {
                try {
                  output.put(input.take().apply(abyParty, role));
                } catch (InterruptedException e) {
                  // Stop was called.
                }
              }
            },
            "ABY " + role.toString());
    thread.start();
  }

  public void put(BiFunction<ABYParty, Role, Long> command) throws InterruptedException {
    input.put(command);
  }

  public long get() throws InterruptedException {
    return output.take();
  }

  public void stop() {
    thread.interrupt();
  }
}
