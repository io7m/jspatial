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

package com.io7m.jspatial.implementation;

import com.io7m.jregions.core.unparameterized.volumes.VolumeL;
import com.io7m.jregions.core.unparameterized.volumes.VolumeXYZSplitL;
import com.io7m.jregions.core.unparameterized.volumes.VolumesL;
import com.io7m.junreachable.UnreachableCodeException;

import java.util.Objects;
import java.util.Optional;

/**
 * Functions to divide areas into quadrants.
 */

public final class OctantsL
{
  private OctantsL()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Subdivide a volume into eight equal sized quadrants. The volume is not split if the size on any
   * axis is less than 2.
   *
   * @param volume The volume
   *
   * @return The resulting area
   */

  public static Optional<VolumeXYZSplitL<VolumeL>> subdivide(
    final VolumeL volume)
  {
    Objects.requireNonNull(volume, "Volume");

    if (volume.sizeX() >= 2L && volume.sizeY() >= 2L && volume.sizeZ() >= 2L) {
      final long size_x = volume.sizeX() / 2L;
      final long size_y = volume.sizeY() / 2L;
      final long size_z = volume.sizeZ() / 2L;
      final VolumeXYZSplitL<VolumeL> result =
        VolumesL.splitAtXYZ(volume, size_x, size_y, size_z);
      return Optional.of(result);
    }
    return Optional.empty();
  }
}
