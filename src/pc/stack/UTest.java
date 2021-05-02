package pc.stack;

import org.junit.Before;

public class UTest extends StackTest {
  @Override
  public Stack<Integer> createStack() {
    return new UStack<>();
  }
}
