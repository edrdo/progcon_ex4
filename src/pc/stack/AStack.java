package pc.stack;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;

public class AStack<E> implements Stack<E> {

  private static class State<E> {
    Node<E> top;
    int size;
  }

  private AtomicReference<State<E>> ref;
  
  public AStack() {
    State<E> initial = new State<>();
    initial.top = null;
    initial.size = 0;
    ref = new AtomicReference<>(initial);
  }

  @Override
  public int size() {
    return ref.get().size;
  }

  @Override
  public void push(E elem) {
    // TODO
  }

  @Override
  public E pop() {
    State<E> oldState;
    State<E> newState = new State<>();
    E elem = null;
    do {
      oldState = ref.get();
      if (oldState.size == 0) {
        throw new EmptyStackException();
      }
      newState.top = oldState.top.next;
      newState.size = oldState.size - 1;
      elem = oldState.top.data;
    } while(! ref.compareAndSet(oldState, newState));
    return elem;
  }
} 
