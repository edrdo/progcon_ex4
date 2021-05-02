package pc.register;
import java.util.function.Function;

public class URegister<T> implements Register<T> {

  private volatile T value; 

  public URegister(T initial) {
    value = initial;
  }

  @Override
  public T read() { 
    return value;
  }

  @Override
  public void write(T v) {
    value = v;
  }
  
  @Override
  public void transform(Function<T,T> f) {
    value = f.apply(value);
  }
}
