package pc.register;

public class UTest extends RegisterTest {
  @Override
  public Register<Integer> createRegister(int v) {
    return new URegister<>(v);
  }
}
