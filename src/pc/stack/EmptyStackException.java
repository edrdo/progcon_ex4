package pc.stack;

public class EmptyStackException extends RuntimeException {

  public EmptyStackException() { 
    super("Empty stack!"); 
  }
}
