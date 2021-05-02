package pc.stack;

import org.junit.Before;

public class LTest extends StackTest {
  @Override
  public Stack<Integer> createStack() {
    return new LStack<>();
  }
}
