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

import com.io7m.immutables.styles.ImmutablesStyleType;
import com.io7m.jregions.core.unparameterized.volumes.VolumeL;
import org.immutables.value.Value;

/**
 * The type of long integer octtree configurations.
 *
 * @since 3.0.0
 */

@ImmutablesStyleType
@Value.Immutable
public interface OctTreeConfigurationLType
{
  /**
   * @return The maximum bounding volume of the tree
   */

  @Value.Parameter
  VolumeL volume();

  /**
   * @return The minimum width of octants (must be {@code >= 2})
   */

  @Value.Parameter
  @Value.Default
  default long minimumOctantWidth()
  {
    return 2L;
  }

  /**
   * @return The minimum height of octants (must be {@code >= 2})
   */

  @Value.Parameter
  @Value.Default
  default long minimumOctantHeight()
  {
    return 2L;
  }

  /**
   * @return The minimum depth of octants (must be {@code >= 2})
   */

  @Value.Parameter
  @Value.Default
  default long minimumOctantDepth()
  {
    return 2L;
  }

  /**
   * @return {@code true} iff the implementation should attempt to trim empty leaf nodes when an
   * item is removed
   */

  @Value.Parameter
  @Value.Default
  default boolean trimOnRemove()
  {
    return false;
  }
}
