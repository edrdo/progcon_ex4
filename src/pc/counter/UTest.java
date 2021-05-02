package pc.counter;

public class UTest extends CounterTest {
  @Override
  public Counter createCounter(int v) {
    return new UCounter(v);
  }
}
