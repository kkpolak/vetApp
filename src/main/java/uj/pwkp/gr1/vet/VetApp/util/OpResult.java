package uj.pwkp.gr1.vet.VetApp.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public final class OpResult<F, S> {

  private final Optional<F> fail;
  private final Optional<S> success;

  public static <F, S> OpResult<F, S> fail(F value) {
    return new OpResult<>(Optional.of(value), Optional.empty());
  }

  public static <F, S> OpResult<F, S> success(S value) {
    return new OpResult<>(Optional.empty(), Optional.of(value));
  }

  private OpResult(Optional<F> f, Optional<S> s) {
    fail = f;
    success = s;
  }

  public <T> T map(Function<? super F, ? extends T> failFunc, Function<? super S, ? extends T> successFunc) {
    return fail.<T>map(failFunc).orElseGet(()-> success.map(successFunc).get());
  }

  public <T> OpResult<T, S> mapFail(Function<? super F, ? extends T> failFunc) {
    return new OpResult<>(fail.map(failFunc), success);
  }

  public <T> OpResult<F,T> mapSuccess(Function<? super S, ? extends T> successFunc) {
    return new OpResult<>(fail, success.map(successFunc));
  }

  public void apply(Consumer<? super F> failFunc, Consumer<? super S> successFunc) {
    fail.ifPresent(failFunc);
    success.ifPresent(successFunc);
  }

}
