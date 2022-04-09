/*
 * Copyright © 2017 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jspatial.tests.api.quadtrees;

import com.io7m.jregions.core.unparameterized.areas.AreaD;
import com.io7m.jspatial.api.quadtrees.QuadTreeRaycastResultD;
import com.io7m.jspatial.api.quadtrees.QuadTreeRaycastResultDType;
import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.PrimitiveGenerators;
import org.junit.Assert;
import org.junit.Test;

/**
 * Contract completion for QuadTreeRaycastResultD.
 */

public final class QuadTreeRaycastResultDTest extends
  QuadTreeRaycastResultDContract
{
  @SuppressWarnings("unchecked")
  @Override
  protected <T, A extends QuadTreeRaycastResultDType<T>> Generator<A> generator()
  {
    return new QuadTreeRaycastResultDGenerator(PrimitiveGenerators.integers());
  }

  @Override
  protected <T> QuadTreeRaycastResultDType<T> create(
    final double distance,
    final AreaD area,
    final T object)
  {
    return QuadTreeRaycastResultD.of(distance, area, object);
  }

  /**
   * Builder tests.
   */

  @Test
  public void testBuilder()
  {
    final AreaD area0 = AreaD.of(0.0, 100.0, 0.0, 100.0);
    final AreaD area1 = AreaD.of(1.0, 99.0, 1.0, 99.0);

    final QuadTreeRaycastResultD.Builder<Integer> b =
      QuadTreeRaycastResultD.builder();

    b.setArea(area0);
    b.setDistance(23.0);
    b.setItem(Integer.valueOf(23));

    final QuadTreeRaycastResultD<Integer> r0_eq = b.build();
    final QuadTreeRaycastResultD<Integer> r0 = b.build();

    b.setDistance(24.0);
    final QuadTreeRaycastResultD<Integer> r1 = b.build();

    b.setItem(Integer.valueOf(25));
    final QuadTreeRaycastResultD<Integer> r2 = b.build();

    b.setArea(area1);
    final QuadTreeRaycastResultD<Integer> r3 = b.build();

    Assert.assertEquals(r0, r0);
    Assert.assertEquals(r0, r0_eq);

    Assert.assertNotEquals(r0, r1);
    Assert.assertNotEquals(r0, r2);
    Assert.assertNotEquals(r0, r3);

    Assert.assertNotEquals(r1, r2);
    Assert.assertNotEquals(r1, r3);

    Assert.assertNotEquals(r2, r3);
  }

  /**
   * Builder missing parameters.
   */

  @Test
  public void testBuilderMissing0()
  {
    final AreaD area0 = AreaD.of(0.0, 100.0, 0.0, 100.0);
    final AreaD area1 = AreaD.of(1.0, 99.0, 1.0, 99.0);

    final QuadTreeRaycastResultD.Builder<Integer> b =
      QuadTreeRaycastResultD.builder();

    b.setArea(area0);
    this.expected.expect(IllegalStateException.class);
    b.build();
    Assert.fail();
  }

  /**
   * Builder missing parameters.
   */

  @Test
  public void testBuilderMissing1()
  {
    final AreaD area0 = AreaD.of(0.0, 100.0, 0.0, 100.0);
    final AreaD area1 = AreaD.of(1.0, 99.0, 1.0, 99.0);

    final QuadTreeRaycastResultD.Builder<Integer> b =
      QuadTreeRaycastResultD.builder();

    b.setArea(area0);
    b.setDistance(1.0);
    this.expected.expect(IllegalStateException.class);
    b.build();
    Assert.fail();
  }

  /**
   * Builder missing parameters.
   */

  @Test
  public void testBuilderMissing2()
  {
    final QuadTreeRaycastResultD.Builder<Integer> b =
      QuadTreeRaycastResultD.builder();

    b.setDistance(1.0);
    b.setItem(Integer.valueOf(23));
    this.expected.expect(IllegalStateException.class);
    b.build();
    Assert.fail();
  }

  /**
   * Builder missing parameters.
   */

  @Test
  public void testBuilderMissing3()
  {
    final QuadTreeRaycastResultD.Builder<Integer> b =
      QuadTreeRaycastResultD.builder();

    b.setItem(Integer.valueOf(23));
    this.expected.expect(IllegalStateException.class);
    b.build();
    Assert.fail();
  }

  /**
   * from tests.
   */

  @Test
  public void testFrom()
  {
    final AreaD area0 = AreaD.of(0.0, 100.0, 0.0, 100.0);
    final AreaD area1 = AreaD.of(1.0, 99.0, 1.0, 99.0);

    final QuadTreeRaycastResultD.Builder<Integer> b =
      QuadTreeRaycastResultD.builder();

    b.setArea(area0);
    b.setDistance(23.0);
    b.setItem(Integer.valueOf(23));

    final QuadTreeRaycastResultD<Integer> r0 = b.build();
    final QuadTreeRaycastResultD<Integer> r1 = b.from(r0).build();

    Assert.assertEquals(r0, r1);
  }

  /**
   * copyOf tests.
   */

  @Test
  public void testCopyOf()
  {
    final AreaD area0 = AreaD.of(0.0, 100.0, 0.0, 100.0);
    final AreaD area1 = AreaD.of(1.0, 99.0, 1.0, 99.0);

    final QuadTreeRaycastResultD.Builder<Integer> b =
      QuadTreeRaycastResultD.builder();

    b.setArea(area0);
    b.setDistance(23.0);
    b.setItem(Integer.valueOf(23));

    final QuadTreeRaycastResultD<Integer> r0 = b.build();
    final QuadTreeRaycastResultD<Integer> r1 =
      QuadTreeRaycastResultD.copyOf(r0);
    final QuadTreeRaycastResultD<Integer> r2 =
      QuadTreeRaycastResultD.copyOf(new QuadTreeRaycastResultDType<Integer>()
      {
        @Override
        public double distance()
        {
          return r0.distance();
        }

        @Override
        public AreaD area()
        {
          return r0.area();
        }

        @Override
        public Integer item()
        {
          return r0.item();
        }
      });

    Assert.assertEquals(r0, r1);
    Assert.assertEquals(r0, r2);
  }

  /**
   * With tests.
   */

  @Test
  public void testWith()
  {
    final AreaD area0 = AreaD.of(0.0, 100.0, 0.0, 100.0);
    final AreaD area1 = AreaD.of(1.0, 99.0, 1.0, 99.0);

    final QuadTreeRaycastResultD.Builder<Integer> b =
      QuadTreeRaycastResultD.builder();

    b.setArea(area0);
    b.setDistance(23.0);
    b.setItem(Integer.valueOf(23));

    final QuadTreeRaycastResultD<Integer> r0 = b.build();
    final QuadTreeRaycastResultD<Integer> r1 = r0.withDistance(24.0);
    final QuadTreeRaycastResultD<Integer> r2 = r0.withItem(Integer.valueOf(25));
    final QuadTreeRaycastResultD<Integer> r3 = r0.withArea(area1);

    Assert.assertEquals(r0, r0);

    Assert.assertNotEquals(r0, r1);
    Assert.assertNotEquals(r0, r2);
    Assert.assertNotEquals(r0, r3);

    Assert.assertNotEquals(r1, r2);
    Assert.assertNotEquals(r1, r3);

    Assert.assertNotEquals(r2, r3);
  }
}
