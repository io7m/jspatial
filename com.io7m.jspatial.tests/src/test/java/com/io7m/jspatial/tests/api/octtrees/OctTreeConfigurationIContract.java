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

package com.io7m.jspatial.tests.api.octtrees;

import com.io7m.jregions.core.unparameterized.volumes.VolumeI;
import com.io7m.jspatial.api.octtrees.OctTreeConfigurationIType;
import net.java.quickcheck.Generator;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Contract for integer tree configurations.
 */

public abstract class OctTreeConfigurationIContract
{
  /**
   * Expected exception.
   */

  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract <A extends OctTreeConfigurationIType> Generator<A> generator();

  protected abstract OctTreeConfigurationIType create(
    final VolumeI area);

  /**
   * Identities.
   */

  @Test
  public final void testIdentities()
  {
    final VolumeI area = VolumeI.of(0, 100, 0, 100, 0, 100);
    final OctTreeConfigurationIType c = this.create(area);

    Assert.assertEquals(area, c.volume());
    Assert.assertEquals(2, c.minimumOctantHeight());
    Assert.assertEquals(2, c.minimumOctantWidth());
    Assert.assertEquals(2, c.minimumOctantDepth());
    Assert.assertFalse(c.trimOnRemove());
  }
}
