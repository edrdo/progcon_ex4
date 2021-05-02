package pc.stack;

public class LStack<E> implements Stack<E> {

  private Node<E> top; 
  private int size; 
  
  public LStack() {
    top = null;
    size = 0;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public void push(E elem) {
    synchronized(this) {
      top = new Node<>(elem, top);
    }
    size++;
  }

  @Override
  public E pop() {
    synchronized (this) {
      if (size == 0) {
        throw new EmptyStackException();
      }
      E elem = top.data;
      top = top.next;
      size--;
      return elem;
    }
  }
} 
