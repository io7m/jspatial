/*
 * Copyright © 2017 <code@io7m.com> http://io7m.com
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

import com.io7m.jaffirm.core.Postconditions;
import com.io7m.jnull.NullCheck;
import com.io7m.jregions.core.unparameterized.volumes.VolumeL;
import com.io7m.jregions.core.unparameterized.volumes.VolumesL;
import net.java.quickcheck.Generator;

import java.util.Random;

public final class VolumeLContainedGenerator implements Generator<VolumeL>
{
  private final VolumeL container;

  public VolumeLContainedGenerator(
    final VolumeL in_container)
  {
    this.container = NullCheck.notNull(in_container, "Contaienr");
  }

  @Override
  public VolumeL next()
  {
    final Random random = new Random();

    final long x_size =
      (long) random.nextInt(Math.toIntExact(this.container.sizeX() / 2L)) + 1L;
    final long y_size =
      (long) random.nextInt(Math.toIntExact(this.container.sizeY() / 2L)) + 1L;
    final long z_size =
      (long) random.nextInt(Math.toIntExact(this.container.sizeZ() / 2L)) + 1L;

    final VolumeL initial = VolumesL.create(
      this.container.minimumX(),
      this.container.minimumY(),
      this.container.minimumZ(),
      x_size,
      y_size,
      z_size);

    final long x_shift = (long) random.nextInt(Math.toIntExact(x_size));
    final long y_shift = (long) random.nextInt(Math.toIntExact(y_size));
    final long z_shift = (long) random.nextInt(Math.toIntExact(z_size));

    final VolumeL volume =
      VolumesL.moveRelative(initial, x_shift, y_shift, z_shift);

    Postconditions.checkPostconditionV(
      VolumesL.contains(this.container, volume),
      "Volume %s contains %s",
      this.container,
      volume);
    return volume;
  }
}
