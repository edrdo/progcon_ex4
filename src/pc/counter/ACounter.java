package pc.counter;

import java.util.concurrent.atomic.AtomicInteger;

public class ACounter implements Counter {

  private final AtomicInteger value;
  
  public ACounter(int initial) {
    value = new AtomicInteger(initial);
  }

  @Override
  public void increment() { 
    int v = value.get() + 1;
    value.set(v);
  }

  @Override
  public int value() {
    return value.get();
  }
}



