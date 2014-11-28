/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jspatial.quadtrees;

import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jspatial.BoundingAreaType;

/**
 * <p>
 * An object returned by a successful raycast operation. Objects are ordered
 * by their scalar distance from the origin of the ray.
 * </p>
 * <p>
 * Note that although the <code>QuadTreeRaycastResult</code> class is
 * immutable, whether or not the object of type <code>T</code> returned by
 * {@link #getObject()} is mutable depends entirely on the user and therefore
 * thread-safety is also the responsibility of the user.
 * </p>
 *
 * @param <T>
 *          The precise type of quadtree members.
 */

public final class QuadTreeRaycastResult<T extends BoundingAreaType> implements
Comparable<QuadTreeRaycastResult<T>>
{
  private final double distance;
  private final T      object;

  /**
   * Construct a new result.
   *
   * @param in_object
   *          The object.
   * @param in_distance
   *          The distance to the object.
   */

  public QuadTreeRaycastResult(
    final T in_object,
    final double in_distance)
  {
    this.object = NullCheck.notNull(in_object, "Object");
    this.distance = in_distance;
  }

  @Override public int compareTo(
    final @Nullable QuadTreeRaycastResult<T> other)
  {
    return Double.compare(this.distance, NullCheck.notNull(other).distance);
  }

  @Override public boolean equals(
    final @Nullable Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final QuadTreeRaycastResult<?> other = (QuadTreeRaycastResult<?>) obj;
    if (Double.doubleToLongBits(this.distance) != Double
      .doubleToLongBits(other.distance)) {
      return false;
    }
    if (!this.object.equals(other.object)) {
      return false;
    }
    return true;
  }

  /**
   * @return The distance of this object from the origin of the ray.
   */

  public double getDistance()
  {
    return this.distance;
  }

  /**
   * @return The intersected object.
   */

  public T getObject()
  {
    return this.object;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(this.distance);
    result = (prime * result) + (int) (temp ^ (temp >>> 32));
    result = (prime * result) + this.object.hashCode();
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder b = new StringBuilder();
    b.append("[QuadTreeRaycastResult ");
    b.append(this.distance);
    b.append(" ");
    b.append(this.object);
    b.append("]");
    final String r = b.toString();
    assert r != null;
    return r;
  }
}