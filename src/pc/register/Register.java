package pc.register;
import java.util.function.Function;

public interface Register<T> {
  T read();
  void write(T value);
  void transform(Function<T,T> f);
}
