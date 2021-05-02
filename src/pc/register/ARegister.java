package pc.register;
import java.util.function.Function;
import java.util.concurrent.atomic.AtomicReference;

public class ARegister<T> implements Register<T> {

  private AtomicReference<T> value; 

  public ARegister(T initial) {
    value = new AtomicReference<>(initial);
  }

  @Override
  public T read() { 
    return value.get();
  }

  @Override
  public void write(T v) {
    value.set(v);
  }
  
  @Override
  public void transform(Function<T,T> f) {
    T oldV = value.get();
    T newV = f.apply(oldV);
    value.set(newV);
  }
}
