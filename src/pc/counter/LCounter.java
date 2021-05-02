package pc.counter;

public class LCounter implements Counter {
  private int value;

  public LCounter(int initial) {
    value = initial;
  }
  
  @Override
  public void increment() { 
    synchronized(this) {
      value++; 
    }
  }

  @Override
  public int value() {
    synchronized(this) {
      return value;
    }
  }
}
