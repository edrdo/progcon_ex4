package pc.counter;

public class LTest extends CounterTest {
  @Override
  public Counter createCounter(int v) {
    return new LCounter(v);
  }
}
