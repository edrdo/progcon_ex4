package pc.stack;

import org.junit.Before;

public class ATest extends StackTest {
  @Override
  public Stack<Integer> createStack() {
    return new AStack<>();
  }
}
