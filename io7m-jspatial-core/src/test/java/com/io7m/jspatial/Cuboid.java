package com.io7m.jspatial;

import javax.annotation.Nonnull;

import com.io7m.jtensors.VectorI3I;
import com.io7m.jtensors.VectorReadable3I;

final class Cuboid implements OctTreeMember<Cuboid>
{
  private final @Nonnull VectorI3I lower;
  private final @Nonnull VectorI3I upper;
  private final long               id;

  Cuboid(
    final long id,
    final @Nonnull VectorI3I lower,
    final @Nonnull VectorI3I upper)
  {
    this.id = id;
    this.lower = lower;
    this.upper = upper;
  }

  @Override public @Nonnull VectorReadable3I boundingVolumeLower()
  {
    return this.lower;
  }

  @Override public @Nonnull VectorReadable3I boundingVolumeUpper()
  {
    return this.upper;
  }

  @Override public int compareTo(
    final Cuboid other)
  {
    if (other.id < this.id) {
      return -1;
    }
    if (other.id > this.id) {
      return 1;
    }
    return 0;
  }

  @Override public boolean equals(
    final Object obj)
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
    final Cuboid other = (Cuboid) obj;
    if (this.lower == null) {
      if (other.lower != null) {
        return false;
      }
    } else if (!this.lower.equals(other.lower)) {
      return false;
    }
    if (this.upper == null) {
      if (other.upper != null) {
        return false;
      }
    } else if (!this.upper.equals(other.upper)) {
      return false;
    }
    return true;
  }

  long getId()
  {
    return this.id;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result =
      (prime * result) + ((this.lower == null) ? 0 : this.lower.hashCode());
    result =
      (prime * result) + ((this.upper == null) ? 0 : this.upper.hashCode());
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Cuboid lower=");
    builder.append(this.lower);
    builder.append(", upper=");
    builder.append(this.upper);
    builder.append("]");
    return builder.toString();
  }
}