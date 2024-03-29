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

package com.io7m.jspatial.api.octtrees;

import com.io7m.jregions.core.unparameterized.volumes.VolumeL;

import java.util.function.BiFunction;

/**
 * The type of mutable octtrees with {@code long} integer coordinates.
 *
 * @param <A> The precise type of octtree members
 *
 * @since 3.0.0
 */

public interface OctTreeLType<A> extends OctTreeReadableLType<A>
{
  /**
   * <p>Insert the object {@code item} into the octtree.</p>
   *
   * <p>The function returns {@code false} if the object could not be
   * inserted for any reason (perhaps due to being too large).</p>
   *
   * <p>If the object is already in the tree, it is replaced. This can be
   * used to update the bounds of an object within the tree.</p>
   *
   * @param item   The object to insert
   * @param bounds The object's bounds
   *
   * @return {@code true} if the object was inserted
   */

  boolean insert(
    A item,
    VolumeL bounds);

  /**
   * <p>Remove the object {@code item} from the octtree.</p>
   *
   * <p>The function returns {@code false} if the object could not be
   * removed for any reason (perhaps due to not being in the tree in the first place).</p>
   *
   * @param item The object to remove
   *
   * @return {@code true} if the object was removed
   */

  boolean remove(
    A item);

  /**
   * Remove all objects from the tree.
   */

  void clear();

  /**
   * Trim all empty quadrants from the tree.
   */

  void trim();

  /**
   * Apply {@code f} to each element of the tree.
   *
   * @param f   A mapping function
   * @param <B> The type of result elements
   *
   * @return A new tree
   */

  @Override
  <B> OctTreeLType<B> map(BiFunction<A, VolumeL, B> f);
}
