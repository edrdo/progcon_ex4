package pc.register;
import java.util.function.Function;

public class LRegister<T> implements Register<T> {

  private T value; 

  public LRegister(T initial) {
    value = initial;
  }

  @Override
  public T read() { 
    synchronized(this) {
      return value;
    }
  }

  @Override
  public void write(T v) {
    synchronized(this) {
      value = v;
    }
  }
  
  @Override
  public void transform(Function<T,T> f) {
    synchronized(this) {
      value = f.apply(value);
    }
  }
}
