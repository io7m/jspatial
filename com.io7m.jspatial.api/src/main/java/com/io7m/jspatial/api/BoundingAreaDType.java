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

package com.io7m.jspatial.api;

import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2D;
import org.immutables.value.Value;

/**
 * <p>The type of bounding areas with {@code double} precision coordinates.</p>
 *
 * <p>Bounding areas have an inclusive lower bound and an exclusive upper
 * bound.</p>
 *
 * @since 3.0.0
 */

@JSpatialImmutableStyleType
@Value.Immutable
public interface BoundingAreaDType
{
  /**
   * Specification of invariants.
   */

  @Value.Check
  default void checkInvariants()
  {
    final Vector2D lo = this.lower();
    final Vector2D hi = this.upper();

    if (lo.x() >= hi.x()) {
      final StringBuilder sb = new StringBuilder(64);
      sb.append("Malformed area bounds.");
      sb.append(System.lineSeparator());
      sb.append("  Lower X coordinate must be < upper X coordinate.");
      sb.append(System.lineSeparator());
      sb.append("  Lower: ");
      sb.append(lo);
      sb.append(System.lineSeparator());
      sb.append("  Upper: ");
      sb.append(hi);
      sb.append(System.lineSeparator());
      throw new IllegalArgumentException(sb.toString());
    }

    if (lo.y() >= hi.y()) {
      final StringBuilder sb = new StringBuilder(64);
      sb.append("Malformed area bounds.");
      sb.append(System.lineSeparator());
      sb.append("  Lower Y coordinate must be < upper Y coordinate.");
      sb.append(System.lineSeparator());
      sb.append("  Lower: ");
      sb.append(lo);
      sb.append(System.lineSeparator());
      sb.append("  Upper: ");
      sb.append(hi);
      sb.append(System.lineSeparator());
      throw new IllegalArgumentException(sb.toString());
    }
  }

  /**
   * @return The lower corner of the area
   */

  @Value.Parameter(order = 0)
  Vector2D lower();

  /**
   * @return The exclusive upper corner of the area
   */

  @Value.Parameter(order = 1)
  Vector2D upper();

  /**
   * @return The width of the area
   */

  default double width()
  {
    return this.upper().x() - this.lower().x();
  }

  /**
   * @return The height of the area
   */

  default double height()
  {
    return this.upper().y() - this.lower().y();
  }

  /**
   * <p>
   * Determine whether or not this area overlaps another given area.
   * </p>
   * <p>
   * Overlapping is reflexive: {@code ∀a. a.overlaps(a) == true}
   * </p>
   * <p>
   * Overlapping is symmetric: {@code ∀a b. a.overlaps(b) == b.overlaps(a)}
   * </p>
   *
   * @param other The other area
   *
   * @return {@code true} iff this area overlaps {@code other}
   */

  default boolean overlaps(
    final BoundingAreaDType other)
  {
    NullCheck.notNull(other, "other");

    final Vector2D a_lo = this.lower();
    final Vector2D a_hi = this.upper();
    final Vector2D b_lo = other.lower();
    final Vector2D b_hi = other.upper();

    final double a_x0 = a_lo.x();
    final double a_x1 = a_hi.x();
    final double a_y0 = a_lo.y();
    final double a_y1 = a_hi.y();
    final double b_x0 = b_lo.x();
    final double b_x1 = b_hi.x();
    final double b_y0 = b_lo.y();
    final double b_y1 = b_hi.y();

    final boolean c0 = a_x0 < b_x1;
    final boolean c1 = a_x1 > b_x0;
    final boolean c2 = a_y0 < b_y1;
    final boolean c3 = a_y1 > b_y0;

    return c0 && c1 && c2 && c3;
  }

  /**
   * <p>
   * Determine whether or not this area contains another given area.
   * </p>
   * <p>
   * Containment is reflexive:
   * {@code ∀a. a.contains(a) }
   * </p>
   * <p>
   * Containment is transitive:
   * {@code ∀a b c. a.contains(b) → b.contains(c) → a.contains(c)}
   * </p>
   * <p>
   * Containment implies overlapping:
   * {@code ∀a b. a.contains(b) → b.overlaps(a)}
   * </p>
   *
   * @param other The other area
   *
   * @return {@code true} iff this area contains {@code other}
   */

  default boolean contains(
    final BoundingAreaDType other)
  {
    NullCheck.notNull(other, "other");

    final Vector2D a_lo = this.lower();
    final Vector2D a_hi = this.upper();
    final Vector2D b_lo = other.lower();
    final Vector2D b_hi = other.upper();

    final double a_x0 = a_lo.x();
    final double a_x1 = a_hi.x();
    final double a_y0 = a_lo.y();
    final double a_y1 = a_hi.y();
    final double b_x0 = b_lo.x();
    final double b_x1 = b_hi.x();
    final double b_y0 = b_lo.y();
    final double b_y1 = b_hi.y();

    final boolean c0 = b_x0 >= a_x0;
    final boolean c1 = b_x1 <= a_x1;
    final boolean c2 = b_y0 >= a_y0;
    final boolean c3 = b_y1 <= a_y1;

    return c0 && c1 && c2 && c3;
  }
}