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

package com.io7m.jspatial.tests.implementation;

import com.io7m.jspatial.api.octtrees.OctTreeConfigurationD;
import com.io7m.jspatial.api.octtrees.OctTreeDType;
import com.io7m.jspatial.implementation.OctTreeD;
import com.io7m.jspatial.implementation.OctTreeSupplierD;
import com.io7m.jspatial.tests.api.octtrees.OctTreeDContract;

/**
 * Test for {@link OctTreeD}
 */

public final class OctTreeDTest extends OctTreeDContract
{
  @Override
  protected <T> OctTreeDType<T> create(final OctTreeConfigurationD config)
  {
    return new OctTreeSupplierD().create(config);
  }
}
