package com.tomwen.tools;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * from https://github.com/eclipse/jetty.project/blob/jetty-9.4.x/jetty-util/src/main/java/org/eclipse/jetty/util/thread/Locker.java
 * 可以使用jdk7引入的with-resouce的写法
 * Convenience Lock Wrapper.
 * <p>
 * <pre>
 * try(Locker.Lock lock = locker.lock())
 * {
 *   // something
 * }
 * </pre>
 */
public class Locker {
  private static final Lock LOCKED = new Lock();
  private final ReentrantLock _lock = new ReentrantLock();
  private final Lock _unlock = new UnLock();

  public Locker() {
  }

  public Lock lock() {
    if (_lock.isHeldByCurrentThread())
      throw new IllegalStateException("Locker is not reentrant");
    _lock.lock();
    return _unlock;
  }

  public Lock lockIfNotHeld() {
    if (_lock.isHeldByCurrentThread())
      return LOCKED;
    _lock.lock();
    return _unlock;
  }

  public boolean isLocked() {
    return _lock.isLocked();
  }

  public static class Lock implements AutoCloseable {
    @Override
    public void close() {
    }
  }

  public class UnLock extends Lock {
    @Override
    public void close() {
      _lock.unlock();
    }
  }

  public Condition newCondition() {
    return _lock.newCondition();
  }

}
