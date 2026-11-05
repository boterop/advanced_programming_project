package com.uq.bnb.fixtures;

import com.uq.bnb.domain.valueobject.ID;
import java.util.concurrent.atomic.AtomicLong;

public final class TestIds {
  private static final AtomicLong NEXT = new AtomicLong();

  private TestIds() {}

  public static ID next() {
    return new ID(String.format("00000000-0000-0000-0000-%012d", NEXT.incrementAndGet()));
  }
}
