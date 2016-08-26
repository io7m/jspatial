/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jspatial.tests.api;

import com.io7m.jspatial.api.BoundingVolumeD;
import com.io7m.jspatial.api.octtrees.OctTreeConfigurationDType;
import com.io7m.jtensors.VectorI2D;
import com.io7m.jtensors.VectorI3D;
import net.java.quickcheck.Generator;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Contract for double precision tree configurations.
 */

public abstract class OctTreeConfigurationDContract
{
  /**
   * Expected exception.
   */

  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract <A extends OctTreeConfigurationDType> Generator<A> generator();

  protected abstract OctTreeConfigurationDType create(
    final BoundingVolumeD volume);

  /**
   * Identities.
   */

  @Test
  public final void testIdentities()
  {
    final VectorI3D lower = new VectorI3D(0.0, 0.0, 0.0);
    final VectorI3D upper = new VectorI3D(100.0, 100.0, 100.0);
    final BoundingVolumeD volume = BoundingVolumeD.of(lower, upper);
    final OctTreeConfigurationDType c = this.create(volume);

    Assert.assertEquals(volume, c.volume());
    Assert.assertEquals(2.0, c.minimumOctantHeight(), 0.0001);
    Assert.assertEquals(2.0, c.minimumOctantWidth(), 0.0001);
    Assert.assertEquals(2.0, c.minimumOctantDepth(), 0.0001);
    Assert.assertFalse(c.trimOnRemove());
  }
}
