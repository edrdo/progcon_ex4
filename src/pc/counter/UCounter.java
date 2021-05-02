package pc.counter;

public class UCounter implements Counter {
  private volatile int value;

  public UCounter(int initial) {
    value = initial;
  }
  
  @Override
  public void increment() { 
    value++; 
  }

  @Override
  public int value() {
    return value;
  }
}
