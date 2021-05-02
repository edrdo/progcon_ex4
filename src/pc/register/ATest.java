package pc.register;

public class ATest extends RegisterTest {
  @Override
  public Register<Integer> createRegister(int v) {
    return new ARegister<>(v);
  }
}
