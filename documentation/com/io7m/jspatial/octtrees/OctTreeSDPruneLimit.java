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

package com.io7m.jspatial.octtrees;

import java.util.SortedSet;
import java.util.TreeSet;

import com.io7m.jfunctional.PartialFunctionType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jspatial.BoundingVolumeCheck;
import com.io7m.jspatial.BoundingVolumeType;
import com.io7m.jspatial.Dimensions;
import com.io7m.jspatial.RayI3D;
import com.io7m.jspatial.SDType;
import com.io7m.jtensors.VectorI3D;
import com.io7m.jtensors.VectorI3I;
import com.io7m.jtensors.VectorReadable3IType;
import com.io7m.junreachable.UnimplementedCodeException;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * <p>
 * An octtree implementation combining {@link OctTreeSDPrune} and
 * {@link OctTreeSDLimit}.
 * </p>
 *
 * @param <T>
 *          The precise type of octtree members.
 */

@SuppressWarnings("synthetic-access") public final class OctTreeSDPruneLimit<T extends OctTreeMemberType<T>> implements
  OctTreeSDType<T>
{
  private final class Octant implements OctantType
  {
    private boolean                leaf;
    private final VectorI3I        lower;

    private final SortedSet<T>     octant_objects_dynamic;
    private final SortedSet<T>     octant_objects_static;
    private final int              octant_size_x;

    private final int              octant_size_y;
    private final int              octant_size_z;
    private final VectorI3I        upper;
    private @Nullable Octant       x0y0z0;
    private @Nullable Octant       x0y0z1;
    private @Nullable Octant       x0y1z0;
    private @Nullable Octant       x0y1z1;
    private @Nullable Octant       x1y0z0;
    private @Nullable Octant       x1y0z1;

    private @Nullable Octant       x1y1z0;
    private @Nullable Octant       x1y1z1;
    private final @Nullable Octant parent;

    public Octant(
      final @Nullable Octant in_parent,
      final VectorI3I in_lower,
      final VectorI3I in_upper)
    {
      this.parent = in_parent;
      this.lower = in_lower;
      this.upper = in_upper;
      this.octant_size_x = Dimensions.getSpanSizeX(this.lower, this.upper);
      this.octant_size_y = Dimensions.getSpanSizeY(this.lower, this.upper);
      this.octant_size_z = Dimensions.getSpanSizeZ(this.lower, this.upper);

      this.octant_objects_dynamic = new TreeSet<T>();
      this.octant_objects_static = new TreeSet<T>();

      this.leaf = true;
      this.x0y0z0 = null;
      this.x1y0z0 = null;
      this.x0y1z0 = null;
      this.x1y1z0 = null;
      this.x0y0z1 = null;
      this.x1y0z1 = null;
      this.x0y1z1 = null;
      this.x1y1z1 = null;
    }

    /**
     * Attempt to turn this node back into a leaf.
     */

    private void unsplitAttempt()
    {
      if (this.leaf == false) {
        boolean prunable = true;
        assert this.x0y0z0 != null;
        prunable &= this.x0y0z0.unsplitCanPrune();
        assert this.x1y0z0 != null;
        prunable &= this.x1y0z0.unsplitCanPrune();
        assert this.x0y1z0 != null;
        prunable &= this.x0y1z0.unsplitCanPrune();
        assert this.x1y1z0 != null;
        prunable &= this.x1y1z0.unsplitCanPrune();

        assert this.x0y0z1 != null;
        prunable &= this.x0y0z1.unsplitCanPrune();
        assert this.x1y0z1 != null;
        prunable &= this.x1y0z1.unsplitCanPrune();
        assert this.x0y1z1 != null;
        prunable &= this.x0y1z1.unsplitCanPrune();
        assert this.x1y1z1 != null;
        prunable &= this.x1y1z1.unsplitCanPrune();

        if (prunable) {
          this.leaf = true;
          this.x0y0z0 = null;
          this.x0y1z0 = null;
          this.x1y0z0 = null;
          this.x1y1z0 = null;

          this.x0y0z1 = null;
          this.x0y1z1 = null;
          this.x1y0z1 = null;
          this.x1y1z1 = null;
        }
      }
    }

    /**
     * Attempt to turn this node and as many ancestors if this node back into
     * leaves as possible.
     */

    private void unsplitAttemptRecursive()
    {
      this.unsplitAttempt();
      if (this.parent != null) {
        this.parent.unsplitAttemptRecursive();
      }
    }

    private boolean unsplitCanPrune()
    {
      return (this.leaf == true)
        && this.octant_objects_dynamic.isEmpty()
        && this.octant_objects_static.isEmpty();
    }

    @Override public VectorReadable3IType boundingVolumeLower()
    {
      return this.lower;
    }

    @Override public VectorReadable3IType boundingVolumeUpper()
    {
      return this.upper;
    }

    private boolean canSplit()
    {
      final int mx = OctTreeSDPruneLimit.this.minimum_size_x;
      final int my = OctTreeSDPruneLimit.this.minimum_size_y;
      final int mz = OctTreeSDPruneLimit.this.minimum_size_z;
      return (this.octant_size_x >= (mx << 1))
        && (this.octant_size_y >= (my << 1))
        && (this.octant_size_z >= (mz << 1));
    }

    void clear()
    {
      this.octant_objects_dynamic.clear();
      this.octant_objects_static.clear();

      if (this.leaf == false) {
        assert this.x0y0z0 != null;
        this.x0y0z0.clear();
        assert this.x1y0z0 != null;
        this.x1y0z0.clear();
        assert this.x0y1z0 != null;
        this.x0y1z0.clear();
        assert this.x1y1z0 != null;
        this.x1y1z0.clear();

        assert this.x0y0z1 != null;
        this.x0y0z1.clear();
        assert this.x1y0z1 != null;
        this.x1y0z1.clear();
        assert this.x0y1z1 != null;
        this.x0y1z1.clear();
        assert this.x1y1z1 != null;
        this.x1y1z1.clear();
      }
    }

    void clearDynamic()
    {
      this.octant_objects_dynamic.clear();

      if (this.leaf == false) {
        assert this.x0y0z0 != null;
        this.x0y0z0.clearDynamic();
        assert this.x1y0z0 != null;
        this.x1y0z0.clearDynamic();
        assert this.x0y1z0 != null;
        this.x0y1z0.clearDynamic();
        assert this.x1y1z0 != null;
        this.x1y1z0.clearDynamic();

        assert this.x0y0z1 != null;
        this.x0y0z1.clearDynamic();
        assert this.x1y0z1 != null;
        this.x1y0z1.clearDynamic();
        assert this.x0y1z1 != null;
        this.x0y1z1.clearDynamic();
        assert this.x1y1z1 != null;
        this.x1y1z1.clearDynamic();
      }
    }

    private void collectRecursive(
      final SortedSet<T> items)
    {
      items.addAll(this.octant_objects_dynamic);
      items.addAll(this.octant_objects_static);

      if (this.leaf == false) {
        assert this.x0y0z0 != null;
        this.x0y0z0.collectRecursive(items);
        assert this.x0y1z0 != null;
        this.x0y1z0.collectRecursive(items);
        assert this.x1y0z0 != null;
        this.x1y0z0.collectRecursive(items);
        assert this.x1y1z0 != null;
        this.x1y1z0.collectRecursive(items);

        assert this.x0y0z1 != null;
        this.x0y0z1.collectRecursive(items);
        assert this.x0y1z1 != null;
        this.x0y1z1.collectRecursive(items);
        assert this.x1y0z1 != null;
        this.x1y0z1.collectRecursive(items);
        assert this.x1y1z1 != null;
        this.x1y1z1.collectRecursive(items);
      }
    }

    /**
     * Attempt to insert <code>item</code> into this node, or the children of
     * this node.
     *
     * @return <code>true</code>, if the item was inserted and
     *         <code>false</code> otherwise.
     */

    boolean insert(
      final T item,
      final SDType type)
    {
      return this.insertBase(item, type);
    }

    /**
     * Insertion base case: item may or may not fit within node.
     */

    private boolean insertBase(
      final T item,
      final SDType type)
    {
      if (OctTreeSDPruneLimit.this.objects_all_dynamic.contains(item)) {
        return false;
      }
      if (OctTreeSDPruneLimit.this.objects_all_static.contains(item)) {
        return false;
      }
      if (BoundingVolumeCheck.containedWithin(this, item)) {
        return this.insertStep(item, type);
      }
      return false;
    }

    /**
     * Insert the given object into the current octant's object list, and also
     * inserted into the "global" object list.
     */

    private boolean insertObject(
      final T item,
      final SDType type)
    {
      switch (type) {
        case SD_DYNAMIC:
        {
          OctTreeSDPruneLimit.this.objects_all_dynamic.add(item);
          this.octant_objects_dynamic.add(item);
          return true;
        }
        case SD_STATIC:
        {
          OctTreeSDPruneLimit.this.objects_all_static.add(item);
          this.octant_objects_static.add(item);
          return true;
        }
      }

      throw new UnimplementedCodeException();
    }

    /**
     * Insertion inductive case: item fits within node, but may fit more
     * precisely within a child node.
     */

    // CHECKSTYLE:OFF
    private boolean insertStep(
      // CHECKSTYLE:ON
      final T item,
      final SDType type)
    {
      /**
       * The object can fit in this node, but perhaps it is possible to fit it
       * more precisely within one of the child nodes.
       */

      /**
       * If this node is a leaf, and is large enough to split, do so.
       */

      if (this.leaf == true) {
        if (this.canSplit()) {
          this.split();
        } else {

          /**
           * The node is a leaf, but cannot be split further. Insert directly.
           */

          return this.insertObject(item, type);
        }
      }

      /**
       * See if the object will fit in any of the child nodes.
       */

      assert this.leaf == false;

      assert this.x0y0z0 != null;
      if (BoundingVolumeCheck.containedWithin(this.x0y0z0, item)) {
        assert this.x0y0z0 != null;
        return this.x0y0z0.insertStep(item, type);
      }
      assert this.x1y0z0 != null;
      if (BoundingVolumeCheck.containedWithin(this.x1y0z0, item)) {
        assert this.x1y0z0 != null;
        return this.x1y0z0.insertStep(item, type);
      }
      assert this.x0y1z0 != null;
      if (BoundingVolumeCheck.containedWithin(this.x0y1z0, item)) {
        assert this.x0y1z0 != null;
        return this.x0y1z0.insertStep(item, type);
      }
      assert this.x1y1z0 != null;
      if (BoundingVolumeCheck.containedWithin(this.x1y1z0, item)) {
        assert this.x1y1z0 != null;
        return this.x1y1z0.insertStep(item, type);
      }

      assert this.x0y0z1 != null;
      if (BoundingVolumeCheck.containedWithin(this.x0y0z1, item)) {
        assert this.x0y0z1 != null;
        return this.x0y0z1.insertStep(item, type);
      }
      assert this.x1y0z1 != null;
      if (BoundingVolumeCheck.containedWithin(this.x1y0z1, item)) {
        assert this.x1y0z1 != null;
        return this.x1y0z1.insertStep(item, type);
      }
      assert this.x0y1z1 != null;
      if (BoundingVolumeCheck.containedWithin(this.x0y1z1, item)) {
        assert this.x0y1z1 != null;
        return this.x0y1z1.insertStep(item, type);
      }
      assert this.x1y1z1 != null;
      if (BoundingVolumeCheck.containedWithin(this.x1y1z1, item)) {
        assert this.x1y1z1 != null;
        return this.x1y1z1.insertStep(item, type);
      }

      /**
       * Otherwise, insert the object into this node.
       */
      return this.insertObject(item, type);
    }

    void raycast(
      final RayI3D ray,
      final SortedSet<OctTreeRaycastResult<T>> items)
    {
      if (BoundingVolumeCheck.rayBoxIntersects(
        ray,
        this.lower.getXI(),
        this.lower.getYI(),
        this.lower.getZI(),
        this.upper.getXI(),
        this.upper.getYI(),
        this.upper.getZI())) {

        this.raycastObjects(ray, this.octant_objects_dynamic, items);
        this.raycastObjects(ray, this.octant_objects_static, items);

        if (this.leaf == false) {
          assert this.x0y0z0 != null;
          this.x0y0z0.raycast(ray, items);
          assert this.x0y1z0 != null;
          this.x0y1z0.raycast(ray, items);
          assert this.x1y0z0 != null;
          this.x1y0z0.raycast(ray, items);
          assert this.x1y1z0 != null;
          this.x1y1z0.raycast(ray, items);

          assert this.x0y0z1 != null;
          this.x0y0z1.raycast(ray, items);
          assert this.x0y1z1 != null;
          this.x0y1z1.raycast(ray, items);
          assert this.x1y0z1 != null;
          this.x1y0z1.raycast(ray, items);
          assert this.x1y1z1 != null;
          this.x1y1z1.raycast(ray, items);
        }
      }
    }

    void raycastObjects(
      final RayI3D ray,
      final SortedSet<T> objects,
      final SortedSet<OctTreeRaycastResult<T>> items)
    {
      for (final T object : objects) {
        final VectorReadable3IType object_lower =
          object.boundingVolumeLower();
        final VectorReadable3IType object_upper =
          object.boundingVolumeUpper();

        if (BoundingVolumeCheck.rayBoxIntersects(
          ray,
          object_lower.getXI(),
          object_lower.getYI(),
          object_lower.getZI(),
          object_upper.getXI(),
          object_upper.getYI(),
          object_upper.getZI())) {

          final OctTreeRaycastResult<T> r =
            new OctTreeRaycastResult<T>(object, VectorI3D.distance(
              new VectorI3D(
                object_lower.getXI(),
                object_lower.getYI(),
                object_lower.getZI()),
              ray.getOrigin()));
          items.add(r);
        }
      }
    }

    boolean remove(
      final T item)
    {
      if ((OctTreeSDPruneLimit.this.objects_all_dynamic.contains(item) == false)
        && (OctTreeSDPruneLimit.this.objects_all_static.contains(item) == false)) {
        return false;
      }

      /**
       * If an object is in objects_all, then it must be within the bounds of
       * the tree, according to insert().
       */
      assert BoundingVolumeCheck.containedWithin(this, item);
      return this.removeStep(item);
    }

    // CHECKSTYLE:OFF
    private boolean removeStep(
      // CHECKSTYLE:ON
      final T item)
    {
      if (this.octant_objects_dynamic.contains(item)) {
        this.octant_objects_dynamic.remove(item);
        OctTreeSDPruneLimit.this.objects_all_dynamic.remove(item);
        this.unsplitAttemptRecursive();
        return true;
      }
      if (this.octant_objects_static.contains(item)) {
        this.octant_objects_static.remove(item);
        OctTreeSDPruneLimit.this.objects_all_static.remove(item);
        this.unsplitAttemptRecursive();
        return true;
      }

      this.unsplitAttemptRecursive();

      if (this.leaf == false) {
        assert this.x0y0z0 != null;
        if (BoundingVolumeCheck.containedWithin(this.x0y0z0, item)) {
          assert this.x0y0z0 != null;
          return this.x0y0z0.removeStep(item);
        }
        assert this.x1y0z0 != null;
        if (BoundingVolumeCheck.containedWithin(this.x1y0z0, item)) {
          assert this.x1y0z0 != null;
          return this.x1y0z0.removeStep(item);
        }
        assert this.x0y1z0 != null;
        if (BoundingVolumeCheck.containedWithin(this.x0y1z0, item)) {
          assert this.x0y1z0 != null;
          return this.x0y1z0.removeStep(item);
        }
        assert this.x1y1z0 != null;
        if (BoundingVolumeCheck.containedWithin(this.x1y1z0, item)) {
          assert this.x1y1z0 != null;
          return this.x1y1z0.removeStep(item);
        }

        assert this.x0y0z1 != null;
        if (BoundingVolumeCheck.containedWithin(this.x0y0z1, item)) {
          assert this.x0y0z1 != null;
          return this.x0y0z1.removeStep(item);
        }
        assert this.x1y0z1 != null;
        if (BoundingVolumeCheck.containedWithin(this.x1y0z1, item)) {
          assert this.x1y0z1 != null;
          return this.x1y0z1.removeStep(item);
        }
        assert this.x0y1z1 != null;
        if (BoundingVolumeCheck.containedWithin(this.x0y1z1, item)) {
          assert this.x0y1z1 != null;
          return this.x0y1z1.removeStep(item);
        }
        assert this.x1y1z1 != null;
        if (BoundingVolumeCheck.containedWithin(this.x1y1z1, item)) {
          assert this.x1y1z1 != null;
          return this.x1y1z1.removeStep(item);
        }
      }

      /**
       * The object must be in the tree, according to objects_all. Therefore
       * it must be in this node, or one of the child octants.
       */
      throw new UnreachableCodeException();
    }

    /**
     * Split this node into four octants.
     */

    private void split()
    {
      assert this.canSplit();

      final Octants q = Octants.split(this.lower, this.upper);
      this.x0y0z0 = new Octant(this, q.getX0Y0Z0Lower(), q.getX0Y0Z0Upper());
      this.x0y1z0 = new Octant(this, q.getX0Y1Z0Lower(), q.getX0Y1Z0Upper());
      this.x1y0z0 = new Octant(this, q.getX1Y0Z0Lower(), q.getX1Y0Z0Upper());
      this.x1y1z0 = new Octant(this, q.getX1Y1Z0Lower(), q.getX1Y1Z0Upper());
      this.x0y0z1 = new Octant(this, q.getX0Y0Z1Lower(), q.getX0Y0Z1Upper());
      this.x0y1z1 = new Octant(this, q.getX0Y1Z1Lower(), q.getX0Y1Z1Upper());
      this.x1y0z1 = new Octant(this, q.getX1Y0Z1Lower(), q.getX1Y0Z1Upper());
      this.x1y1z1 = new Octant(this, q.getX1Y1Z1Lower(), q.getX1Y1Z1Upper());
      this.leaf = false;
    }

    <E extends Throwable> void traverse(
      final int depth,
      final OctTreeTraversalType<E> traversal)
      throws E
    {
      traversal.visit(depth, this.lower, this.upper);

      if (this.leaf == false) {
        assert this.x0y0z0 != null;
        this.x0y0z0.traverse(depth + 1, traversal);
        assert this.x1y0z0 != null;
        this.x1y0z0.traverse(depth + 1, traversal);
        assert this.x0y1z0 != null;
        this.x0y1z0.traverse(depth + 1, traversal);
        assert this.x1y1z0 != null;
        this.x1y1z0.traverse(depth + 1, traversal);

        assert this.x0y0z1 != null;
        this.x0y0z1.traverse(depth + 1, traversal);
        assert this.x1y0z1 != null;
        this.x1y0z1.traverse(depth + 1, traversal);
        assert this.x0y1z1 != null;
        this.x0y1z1.traverse(depth + 1, traversal);
        assert this.x1y1z1 != null;
        this.x1y1z1.traverse(depth + 1, traversal);
      }
    }

    void volumeContaining(
      final BoundingVolumeType volume,
      final SortedSet<T> items)
    {
      /**
       * If <code>volume</code> completely contains this octant, collect
       * everything in this octant and all children of this octant.
       */

      if (BoundingVolumeCheck.containedWithin(volume, this)) {
        this.collectRecursive(items);
        return;
      }

      /**
       * Otherwise, <code>volume</code> may be overlapping this octant and
       * therefore some items may still be contained within
       * <code>volume</code>.
       */

      for (final T object : this.octant_objects_dynamic) {
        assert object != null;
        if (BoundingVolumeCheck.containedWithin(volume, object)) {
          items.add(object);
        }
      }
      for (final T object : this.octant_objects_static) {
        assert object != null;
        if (BoundingVolumeCheck.containedWithin(volume, object)) {
          items.add(object);
        }
      }

      if (this.leaf == false) {
        assert this.x0y0z0 != null;
        this.x0y0z0.volumeContaining(volume, items);
        assert this.x0y1z0 != null;
        this.x0y1z0.volumeContaining(volume, items);
        assert this.x1y0z0 != null;
        this.x1y0z0.volumeContaining(volume, items);
        assert this.x1y1z0 != null;
        this.x1y1z0.volumeContaining(volume, items);

        assert this.x0y0z1 != null;
        this.x0y0z1.volumeContaining(volume, items);
        assert this.x0y1z1 != null;
        this.x0y1z1.volumeContaining(volume, items);
        assert this.x1y0z1 != null;
        this.x1y0z1.volumeContaining(volume, items);
        assert this.x1y1z1 != null;
        this.x1y1z1.volumeContaining(volume, items);
      }
    }

    void volumeOverlapping(
      final BoundingVolumeType volume,
      final SortedSet<T> items)
    {
      /**
       * If <code>volume</code> overlaps this octant, test each object against
       * <code>volume</code>.
       */

      if (BoundingVolumeCheck.overlapsVolume(volume, this)) {
        for (final T object : this.octant_objects_dynamic) {
          assert object != null;
          if (BoundingVolumeCheck.overlapsVolume(volume, object)) {
            items.add(object);
          }
        }
        for (final T object : this.octant_objects_static) {
          assert object != null;
          if (BoundingVolumeCheck.overlapsVolume(volume, object)) {
            items.add(object);
          }
        }

        if (this.leaf == false) {
          assert this.x0y0z0 != null;
          this.x0y0z0.volumeOverlapping(volume, items);
          assert this.x1y0z0 != null;
          this.x1y0z0.volumeOverlapping(volume, items);
          assert this.x0y1z0 != null;
          this.x0y1z0.volumeOverlapping(volume, items);
          assert this.x1y1z0 != null;
          this.x1y1z0.volumeOverlapping(volume, items);

          assert this.x0y0z1 != null;
          this.x0y0z1.volumeOverlapping(volume, items);
          assert this.x1y0z1 != null;
          this.x1y0z1.volumeOverlapping(volume, items);
          assert this.x0y1z1 != null;
          this.x0y1z1.volumeOverlapping(volume, items);
          assert this.x1y1z1 != null;
          this.x1y1z1.volumeOverlapping(volume, items);
        }
      }
    }
  }

  /**
   * Construct a new octtree with the given size and position.
   *
   * @param size
   *          The size.
   * @param position
   *          The position.
   * @param limit_size
   *          The minimum octant size.
   *
   * @return A new octtree.
   * @param <T>
   *          The precise type of octtree members.
   */

  public static <T extends OctTreeMemberType<T>> OctTreeSDType<T> newOctTree(
    final VectorReadable3IType size,
    final VectorReadable3IType position,
    final VectorReadable3IType limit_size)
  {
    return new OctTreeSDPruneLimit<T>(position, size, limit_size);
  }

  private final SortedSet<T> objects_all_dynamic;
  private final SortedSet<T> objects_all_static;
  private final Octant       root;
  private final int          minimum_size_x;
  private final int          minimum_size_y;
  private final int          minimum_size_z;

  private OctTreeSDPruneLimit(
    final VectorReadable3IType position,
    final VectorReadable3IType size,
    final VectorReadable3IType size_minimum)
  {
    NullCheck.notNull(position, "Position");
    NullCheck.notNull(size, "Size");
    NullCheck.notNull(size_minimum, "Minimum size");

    OctTreeChecks.checkSize(
      "Octtree size",
      size.getXI(),
      size.getYI(),
      size.getZI());
    OctTreeChecks.checkSize(
      "Minimum octant size",
      size_minimum.getXI(),
      size_minimum.getYI(),
      size_minimum.getZI());

    if (size_minimum.getXI() > size.getXI()) {
      final String s =
        String.format(
          "Minimum octant width (%d) is greater than the octtree width (%d)",
          size_minimum.getXI(),
          size.getXI());
      throw new IllegalArgumentException(s);
    }
    if (size_minimum.getYI() > size.getYI()) {
      final String s =
        String
          .format(
            "Minimum octant height (%d) is greater than the octtree height (%d)",
            size_minimum.getYI(),
            size.getYI());
      throw new IllegalArgumentException(s);
    }
    if (size_minimum.getZI() > size.getZI()) {
      final String s =
        String.format(
          "Minimum octant depth (%d) is greater than the octtree depth (%d)",
          size_minimum.getZI(),
          size.getZI());
      throw new IllegalArgumentException(s);
    }

    this.minimum_size_x = size_minimum.getXI();
    this.minimum_size_y = size_minimum.getYI();
    this.minimum_size_z = size_minimum.getZI();
    this.objects_all_dynamic = new TreeSet<T>();
    this.objects_all_static = new TreeSet<T>();

    final VectorI3I lower = new VectorI3I(position);
    final VectorI3I upper =
      new VectorI3I(
        (position.getXI() + size.getXI()) - 1,
        (position.getYI() + size.getYI()) - 1,
        (position.getZI() + size.getZI()) - 1);
    this.root = new Octant(null, lower, upper);
  }

  @Override public void octTreeClear()
  {
    this.objects_all_dynamic.clear();
    this.objects_all_static.clear();
    this.root.clear();
  }

  @Override public int octTreeGetPositionX()
  {
    return this.root.lower.getXI();
  }

  @Override public int octTreeGetPositionY()
  {
    return this.root.lower.getYI();
  }

  @Override public int octTreeGetPositionZ()
  {
    return this.root.lower.getZI();
  }

  @Override public int octTreeGetSizeX()
  {
    return this.root.octant_size_x;
  }

  @Override public int octTreeGetSizeY()
  {
    return this.root.octant_size_y;
  }

  @Override public int octTreeGetSizeZ()
  {
    return this.root.octant_size_z;
  }

  @Override public boolean octTreeInsert(
    final T item)
  {
    BoundingVolumeCheck.checkWellFormed(item);
    return this.root.insert(item, SDType.SD_DYNAMIC);
  }

  @Override public boolean octTreeInsertSD(
    final T item,
    final SDType type)
  {
    BoundingVolumeCheck.checkWellFormed(item);
    NullCheck.notNull(type, "Object type");
    return this.root.insert(item, type);
  }

  @Override public <E extends Throwable> void octTreeIterateObjects(
    final PartialFunctionType<T, Boolean, E> f)
    throws E
  {
    NullCheck.notNull(f, "Function");

    for (final T object : this.objects_all_dynamic) {
      assert object != null;
      final Boolean r = f.call(object);
      if (r.booleanValue() == false) {
        break;
      }
    }
    for (final T object : this.objects_all_static) {
      assert object != null;
      final Boolean r = f.call(object);
      if (r.booleanValue() == false) {
        break;
      }
    }
  }

  @Override public void octTreeQueryRaycast(
    final RayI3D ray,
    final SortedSet<OctTreeRaycastResult<T>> items)
  {
    NullCheck.notNull(ray, "Ray");
    NullCheck.notNull(items, "Items");
    this.root.raycast(ray, items);
  }

  @Override public void octTreeQueryVolumeContaining(
    final BoundingVolumeType volume,
    final SortedSet<T> items)
  {
    BoundingVolumeCheck.checkWellFormed(volume);
    NullCheck.notNull(items, "Items");
    this.root.volumeContaining(volume, items);
  }

  @Override public void octTreeQueryVolumeOverlapping(
    final BoundingVolumeType volume,
    final SortedSet<T> items)
  {
    BoundingVolumeCheck.checkWellFormed(volume);
    NullCheck.notNull(items, "Items");
    this.root.volumeOverlapping(volume, items);
  }

  @Override public boolean octTreeRemove(
    final T item)
  {
    BoundingVolumeCheck.checkWellFormed(item);
    return this.root.remove(item);
  }

  @Override public void octTreeSDClearDynamic()
  {
    this.root.clearDynamic();
    this.objects_all_dynamic.clear();
  }

  @Override public <E extends Throwable> void octTreeTraverse(
    final OctTreeTraversalType<E> traversal)
    throws E
  {
    NullCheck.notNull(traversal, "Traversal");
    this.root.traverse(0, traversal);
  }

  @Override public String toString()
  {
    final StringBuilder b = new StringBuilder();
    b.append("[OctTree ");
    b.append(this.root.lower);
    b.append(" ");
    b.append(this.root.upper);
    b.append(" ");
    b.append(this.objects_all_dynamic);
    b.append(" ");
    b.append(this.objects_all_static);
    b.append("]");
    final String r = b.toString();
    assert r != null;
    return r;
  }
}
