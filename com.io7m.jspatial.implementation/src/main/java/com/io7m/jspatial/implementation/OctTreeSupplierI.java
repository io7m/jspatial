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

package com.io7m.jspatial.implementation;

import com.io7m.jspatial.api.octtrees.OctTreeConfigurationI;
import com.io7m.jspatial.api.octtrees.OctTreeIType;
import com.io7m.jspatial.api.octtrees.OctTreeSupplierIType;
import org.osgi.service.component.annotations.Component;

/**
 * The default implementation of the {@link OctTreeSupplierIType} interface.
 */

@Component
public final class OctTreeSupplierI implements OctTreeSupplierIType
{
  /**
   * Construct a new supplier.
   */

  public OctTreeSupplierI()
  {

  }

  @Override
  public <A> OctTreeIType<A> create(final OctTreeConfigurationI config)
  {
    return OctTreeI.create(config);
  }
}