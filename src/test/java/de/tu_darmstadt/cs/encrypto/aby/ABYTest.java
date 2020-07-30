package de.tu_darmstadt.cs.encrypto.aby;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ABYTest {
  private final ABY aby = new ABY();

  @Test
  void hello() {
    assertEquals("hello", aby.hello());
  }
}
