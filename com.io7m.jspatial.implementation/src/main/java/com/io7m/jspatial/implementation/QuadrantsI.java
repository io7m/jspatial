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

import com.io7m.jregions.core.unparameterized.areas.AreaI;
import com.io7m.jregions.core.unparameterized.areas.AreaXYSplitI;
import com.io7m.jregions.core.unparameterized.areas.AreasI;
import com.io7m.junreachable.UnreachableCodeException;

import java.util.Objects;
import java.util.Optional;

/**
 * Functions to divide areas into quadrants.
 */

public final class QuadrantsI
{
  private QuadrantsI()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Subdivide an area into four equal sized quadrants. The area is not split if the width and
   * height are less than 2.
   *
   * @param area The area
   *
   * @return The resulting area
   */

  public static Optional<AreaXYSplitI<AreaI>> subdivide(
    final AreaI area)
  {
    Objects.requireNonNull(area, "Area");

    if (area.sizeX() >= 2 && area.sizeY() >= 2) {
      return Optional.of(
        AreasI.splitAlongXY(
          area,
          area.sizeX() / 2,
          area.sizeY() / 2));
    }
    return Optional.empty();
  }
}
