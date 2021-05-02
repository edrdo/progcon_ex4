package pc.counter;

public class ATest extends CounterTest {
  @Override
  public Counter createCounter(int v) {
    return new ACounter(v);
  }
}
