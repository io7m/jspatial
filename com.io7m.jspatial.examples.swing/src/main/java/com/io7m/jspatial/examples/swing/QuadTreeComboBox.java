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

package com.io7m.jspatial.examples.swing;

import javax.swing.JComboBox;

/**
 * A combo box over an enum.
 *
 * @param <T> The precise enum type
 */

final class QuadTreeComboBox<T extends Enum<T>> extends JComboBox<T>
{
  private final T[] values;

  QuadTreeComboBox(final Class<T> c)
  {
    this.values = c.getEnumConstants();
    for (final T cons : this.values) {
      this.addItem(cons);
    }
  }

  @Override
  public T getSelectedItem()
  {
    final int index = this.getSelectedIndex();
    if (index < 0) {
      return null;
    }
    return this.values[index];
  }
}
