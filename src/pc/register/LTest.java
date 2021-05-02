package pc.register;

public class LTest extends RegisterTest {
  @Override
  public Register<Integer> createRegister(int v) {
    return new LRegister<>(v);
  }
}
