/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

package com.io7m.jspatial;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jaux.functional.Function;
import com.io7m.jtensors.VectorI2D;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;

/**
 * <p>
 * An extremely simple quadtree implementation. This implementation emphasizes
 * simplicity and correctness over performance, to serve as a base for an
 * understanding of the other implementations. This implementation uses
 * single-precision floating point coordinates for the position of the tree
 * and the positions of objects within the tree.
 * </p>
 * <p>
 * The tree has the following properties (or lack thereof):
 * </p>
 * <ul>
 * <li>No attempt is made to distinguish between static and movable objects.
 * In most games and other simulations, objects are inserted into an empty
 * quadtree once per frame. If most of those objects do not move (such as
 * static geometry in the game/simulation world), then they will always be
 * inserted into the same point in the tree and re-inserting them every frame
 * is wasteful and/or redundant. An obvious optimization, therefore, is to
 * distinguish between static and movable objects in the tree and to provide
 * support for removing just the movable objects in one efficient operation.</li>
 * <li>
 * As various operations can potentially traverse the entire tree, it is
 * desirable to keep the number of nodes (quadrants) in the tree to a minimum.
 * This implementation does not allow the programmer to specify a minimum size
 * for quadrants and does not attempt to remove redundant nodes when an object
 * is removed from the tree.</li>
 * <li>
 * Each child quadrant of a given quadrant is exactly half the width and
 * height of the parent.</li>
 * <li>
 * Values of type <code>QuadTreeBasic</code> cannot be safely accessed from
 * multiple threads without explicit synchronization.</li>
 * </ul>
 * 
 * @since 2.1.0
 * @param <T>
 *          The type of objects contained within the tree
 */

@NotThreadSafe public final class QuadTreeBasicF<T extends QuadTreeMemberF<T>> implements
  QuadTreeInterfaceF<T>
{
  final class Quadrant implements BoundingAreaF
  {
    final @Nonnull VectorI2F            lower;
    final @Nonnull VectorI2F            upper;

    private @CheckForNull Quadrant      x0y0;
    private @CheckForNull Quadrant      x1y0;
    private @CheckForNull Quadrant      x0y1;
    private @CheckForNull Quadrant      x1y1;
    private boolean                     leaf;

    private final @Nonnull SortedSet<T> quadrant_objects;
    protected final float               quadrant_size_x;
    protected final float               quadrant_size_y;

    /**
     * Construct a quadrant defined by the inclusive ranges given by
     * <code>lower</code> and <code>upper</code>.
     */

    Quadrant(
      final @Nonnull VectorI2F lower,
      final @Nonnull VectorI2F upper)
    {
      this.upper = upper;
      this.lower = lower;
      this.x0y0 = null;
      this.x1y0 = null;
      this.x0y1 = null;
      this.x1y1 = null;
      this.leaf = true;
      this.quadrant_objects = new TreeSet<T>();
      this.quadrant_size_x = Dimensions.getSpanSizeXF(this.lower, this.upper);
      this.quadrant_size_y = Dimensions.getSpanSizeYF(this.lower, this.upper);
    }

    void areaContaining(
      final @Nonnull BoundingAreaF area,
      final @Nonnull SortedSet<T> items)
    {
      /**
       * If <code>area</code> completely contains this quadrant, collect
       * everything in this quadrant and all children of this quadrant.
       */

      if (BoundingAreaCheck.containedWithinF(area, this)) {
        this.collectRecursive(items);
        return;
      }

      /**
       * Otherwise, <code>area</code> may be overlapping this quadrant and
       * therefore some items may still be contained within <code>area</code>.
       */

      for (final T object : this.quadrant_objects) {
        if (BoundingAreaCheck.containedWithinF(area, object)) {
          items.add(object);
        }
      }

      if (this.leaf == false) {
        this.x0y0.areaContaining(area, items);
        this.x0y1.areaContaining(area, items);
        this.x1y0.areaContaining(area, items);
        this.x1y1.areaContaining(area, items);
      }
    }

    void areaOverlapping(
      final @Nonnull BoundingAreaF area,
      final @Nonnull SortedSet<T> items)
    {
      /**
       * If <code>area</code> overlaps this quadrant, test each object against
       * <code>area</code>.
       */

      if (BoundingAreaCheck.overlapsAreaF(area, this)) {
        for (final T object : this.quadrant_objects) {
          if (BoundingAreaCheck.overlapsAreaF(area, object)) {
            items.add(object);
          }
        }

        if (this.leaf == false) {
          this.x0y0.areaOverlapping(area, items);
          this.x1y0.areaOverlapping(area, items);
          this.x0y1.areaOverlapping(area, items);
          this.x1y1.areaOverlapping(area, items);
        }
      }
    }

    @Override public @Nonnull VectorReadable2F boundingAreaLowerF()
    {
      return this.lower;
    }

    @Override public @Nonnull VectorReadable2F boundingAreaUpperF()
    {
      return this.upper;
    }

    private boolean canSplit()
    {
      return (this.quadrant_size_x >= 2) && (this.quadrant_size_y >= 2);
    }

    void clear()
    {
      this.quadrant_objects.clear();
      if (this.leaf == false) {
        this.x0y0.clear();
        this.x1y0.clear();
        this.x0y1.clear();
        this.x1y1.clear();
      }
    }

    private void collectRecursive(
      final @Nonnull SortedSet<T> items)
    {
      items.addAll(this.quadrant_objects);
      if (this.leaf == false) {
        this.x0y0.collectRecursive(items);
        this.x0y1.collectRecursive(items);
        this.x1y0.collectRecursive(items);
        this.x1y1.collectRecursive(items);
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
      final @Nonnull T item)
    {
      return this.insertBase(item);
    }

    /**
     * Insertion base case: item may or may not fit within node.
     */

    private boolean insertBase(
      final @Nonnull T item)
    {
      if (QuadTreeBasicF.this.objects_all.contains(item)) {
        return false;
      }
      if (BoundingAreaCheck.containedWithinF(this, item)) {
        return this.insertStep(item);
      }
      return false;
    }

    /**
     * Insert the given object into the current quadrant's object list, and
     * also inserted into the "global" object list.
     */

    private boolean insertObject(
      final @Nonnull T item)
    {
      QuadTreeBasicF.this.objects_all.add(item);
      this.quadrant_objects.add(item);
      return true;
    }

    /**
     * Insertion inductive case: item fits within node, but may fit more
     * precisely within a child node.
     */

    private boolean insertStep(
      final @Nonnull T item)
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

          return this.insertObject(item);
        }
      }

      /**
       * See if the object will fit in any of the child nodes.
       */

      assert this.leaf == false;

      if (BoundingAreaCheck.containedWithinF(this.x0y0, item)) {
        return this.x0y0.insertStep(item);
      }
      if (BoundingAreaCheck.containedWithinF(this.x1y0, item)) {
        return this.x1y0.insertStep(item);
      }
      if (BoundingAreaCheck.containedWithinF(this.x0y1, item)) {
        return this.x0y1.insertStep(item);
      }
      if (BoundingAreaCheck.containedWithinF(this.x1y1, item)) {
        return this.x1y1.insertStep(item);
      }

      /**
       * Otherwise, insert the object into this node.
       */
      return this.insertObject(item);
    }

    void raycast(
      final @Nonnull RayI2D ray,
      final @Nonnull SortedSet<QuadTreeRaycastResultF<T>> items)
    {
      if (BoundingAreaCheck.rayBoxIntersects(
        ray,
        this.lower.x,
        this.lower.y,
        this.upper.x,
        this.upper.y)) {

        for (final T object : this.quadrant_objects) {
          final VectorReadable2F object_lower = object.boundingAreaLowerF();
          final VectorReadable2F object_upper = object.boundingAreaUpperF();

          if (BoundingAreaCheck.rayBoxIntersects(
            ray,
            object_lower.getXF(),
            object_lower.getYF(),
            object_upper.getXF(),
            object_upper.getYF())) {

            final QuadTreeRaycastResultF<T> r =
              new QuadTreeRaycastResultF<T>(object, VectorI2D.distance(
                new VectorI2D(object_lower.getXF(), object_lower.getYF()),
                ray.origin));
            items.add(r);
          }
        }

        if (this.leaf == false) {
          this.x0y0.raycast(ray, items);
          this.x0y1.raycast(ray, items);
          this.x1y0.raycast(ray, items);
          this.x1y1.raycast(ray, items);
        }
      }
    }

    void raycastQuadrants(
      final @Nonnull RayI2D ray,
      final @Nonnull SortedSet<QuadTreeRaycastResultF<Quadrant>> quadrants)
    {
      if (BoundingAreaCheck.rayBoxIntersects(
        ray,
        this.lower.x,
        this.lower.y,
        this.upper.x,
        this.upper.y)) {

        if (this.leaf) {
          final QuadTreeRaycastResultF<Quadrant> r =
            new QuadTreeRaycastResultF<Quadrant>(this, VectorI2D.distance(
              new VectorI2D(this.lower.x, this.lower.y),
              ray.origin));
          quadrants.add(r);
          return;
        }

        this.x0y0.raycastQuadrants(ray, quadrants);
        this.x0y1.raycastQuadrants(ray, quadrants);
        this.x1y0.raycastQuadrants(ray, quadrants);
        this.x1y1.raycastQuadrants(ray, quadrants);
      }
    }

    boolean remove(
      final @Nonnull T item)
    {
      if (QuadTreeBasicF.this.objects_all.contains(item) == false) {
        return false;
      }

      /**
       * If an object is in objects_all, then it must be within the bounds of
       * the tree, according to insert().
       */
      assert BoundingAreaCheck.containedWithinF(this, item);
      return this.removeStep(item);
    }

    private boolean removeStep(
      final @Nonnull T item)
    {
      if (this.quadrant_objects.contains(item)) {
        this.quadrant_objects.remove(item);
        QuadTreeBasicF.this.objects_all.remove(item);
        return true;
      }

      if (this.leaf == false) {
        if (BoundingAreaCheck.containedWithinF(this.x0y0, item)) {
          return this.x0y0.removeStep(item);
        }
        if (BoundingAreaCheck.containedWithinF(this.x1y0, item)) {
          return this.x1y0.removeStep(item);
        }
        if (BoundingAreaCheck.containedWithinF(this.x0y1, item)) {
          return this.x0y1.removeStep(item);
        }
        if (BoundingAreaCheck.containedWithinF(this.x1y1, item)) {
          return this.x1y1.removeStep(item);
        }
      }

      /**
       * The object must be in the tree, according to remove(). Therefore it
       * must be in this node, or one of the child quadrants.
       */
      throw new UnreachableCodeException();
    }

    /**
     * Split this node into four quadrants.
     */

    private void split()
    {
      assert this.canSplit();

      final Quadrants q = new Quadrants(this.lower, this.upper);
      this.x0y0 = new Quadrant(q.x0y0_lower, q.x0y0_upper);
      this.x0y1 = new Quadrant(q.x0y1_lower, q.x0y1_upper);
      this.x1y0 = new Quadrant(q.x1y0_lower, q.x1y0_upper);
      this.x1y1 = new Quadrant(q.x1y1_lower, q.x1y1_upper);
      this.leaf = false;
    }

    void traverse(
      final int depth,
      final @Nonnull QuadTreeTraversalF traversal)
      throws Exception
    {
      traversal.visitF(depth, this.lower, this.upper);
      if (this.leaf == false) {
        this.x0y0.traverse(depth + 1, traversal);
        this.x1y0.traverse(depth + 1, traversal);
        this.x0y1.traverse(depth + 1, traversal);
        this.x1y1.traverse(depth + 1, traversal);
      }
    }
  }

  static final class Quadrants
  {
    final @Nonnull VectorI2F x0y0_lower;
    final @Nonnull VectorI2F x0y0_upper;
    final @Nonnull VectorI2F x1y0_lower;
    final @Nonnull VectorI2F x1y0_upper;
    final @Nonnull VectorI2F x0y1_lower;
    final @Nonnull VectorI2F x0y1_upper;
    final @Nonnull VectorI2F x1y1_lower;
    final @Nonnull VectorI2F x1y1_upper;
    float                    new_size_x;
    float                    new_size_y;

    /**
     * Split a quadrant defined by the two points <code>lower</code> and
     * <code>upper</code> into four quadrants.
     */

    Quadrants(
      final @Nonnull VectorI2F lower,
      final @Nonnull VectorI2F upper)
    {
      final float size_x = Dimensions.getSpanSizeXF(lower, upper);
      final float size_y = Dimensions.getSpanSizeYF(lower, upper);

      assert size_x >= 2;
      assert size_y >= 2;

      this.new_size_x = size_x / 2;
      this.new_size_y = size_y / 2;

      final float[] x_spans = new float[4];
      final float[] y_spans = new float[4];

      Dimensions.split1DF(lower.getXF(), upper.getXF(), x_spans);
      Dimensions.split1DF(lower.getYF(), upper.getYF(), y_spans);

      this.x0y0_lower = new VectorI2F(x_spans[0], y_spans[0]);
      this.x0y1_lower = new VectorI2F(x_spans[0], y_spans[2]);
      this.x1y0_lower = new VectorI2F(x_spans[2], y_spans[0]);
      this.x1y1_lower = new VectorI2F(x_spans[2], y_spans[2]);

      this.x0y0_upper = new VectorI2F(x_spans[1], y_spans[1]);
      this.x0y1_upper = new VectorI2F(x_spans[1], y_spans[3]);
      this.x1y0_upper = new VectorI2F(x_spans[3], y_spans[1]);
      this.x1y1_upper = new VectorI2F(x_spans[3], y_spans[3]);
    }
  }

  private final @Nonnull Quadrant       root;
  protected final @Nonnull SortedSet<T> objects_all;

  /**
   * Construct a quadtree using the provided configuration parameters.
   * 
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>config == null</code></li>
   *           <li>
   *           <code>(config.getSizeX() >= 2 && config.getSizeX() <= Integer.MAX_VALUE) == false</code>
   *           </li>
   *           <li>
   *           <code>(config.getSizeY() >= 2 && config.getSizeY() <= Integer.MAX_VALUE) == false</code>
   *           </li>
   *           <li><code>config.getSizeX()</code> is not divisible by
   *           <code>2</code></li>
   *           <li><code>config.getSizeY()</code> is not divisible by
   *           <code>2</code></li>
   *           </ul>
   */

  public QuadTreeBasicF(
    final @Nonnull QuadTreeConfigF config)
    throws ConstraintError
  {
    this(config.getPosition(), config.getSize());
  }

  private QuadTreeBasicF(
    final @Nonnull VectorReadable2F position,
    final @Nonnull VectorReadable2I size)
    throws ConstraintError
  {
    Constraints.constrainNotNull(position, "Position");
    Constraints.constrainNotNull(size, "Size");

    Constraints.constrainRange(size.getXI(), 2, Integer.MAX_VALUE, "X size");
    Constraints.constrainRange(size.getYI(), 2, Integer.MAX_VALUE, "Y size");
    Constraints.constrainArbitrary((size.getXI() % 2) == 0, "X size is even");
    Constraints.constrainArbitrary((size.getYI() % 2) == 0, "Y size is even");

    this.objects_all = new TreeSet<T>();

    final VectorI2F lower = new VectorI2F(position);
    final VectorI2F upper =
      new VectorI2F(
        (position.getXF() + size.getXI()) - 1,
        (position.getYF() + size.getYI()) - 1);
    this.root = new Quadrant(lower, upper);
  }

  @Override public void quadTreeClear()
  {
    this.root.clear();
    this.objects_all.clear();
  }

  @Override public float quadTreeGetPositionX()
  {
    return this.root.lower.x;
  }

  @Override public float quadTreeGetPositionY()
  {
    return this.root.lower.y;
  }

  @Override public int quadTreeGetSizeX()
  {
    return (int) this.root.quadrant_size_x;
  }

  @Override public int quadTreeGetSizeY()
  {
    return (int) this.root.quadrant_size_y;
  }

  @Override public boolean quadTreeInsert(
    final @Nonnull T item)
    throws ConstraintError
  {
    Constraints.constrainNotNull(item, "Item");
    Constraints.constrainArbitrary(
      BoundingAreaCheck.wellFormedF(item),
      "Bounding area is well-formed");

    return this.root.insert(item);
  }

  @Override public void quadTreeIterateObjects(
    final @Nonnull Function<T, Boolean> f)
    throws Exception,
      ConstraintError
  {
    Constraints.constrainNotNull(f, "Function");

    for (final T item : this.objects_all) {
      final Boolean next = f.call(item);
      if (next.booleanValue() == false) {
        break;
      }
    }
  }

  @Override public void quadTreeQueryAreaContaining(
    final @Nonnull BoundingAreaF area,
    final @Nonnull SortedSet<T> items)
    throws ConstraintError
  {
    Constraints.constrainNotNull(area, "Area");
    Constraints.constrainArbitrary(
      BoundingAreaCheck.wellFormedF(area),
      "Bounding area is well-formed");
    Constraints.constrainNotNull(items, "Items");

    this.root.areaContaining(area, items);
  }

  @Override public void quadTreeQueryAreaOverlapping(
    final @Nonnull BoundingAreaF area,
    final @Nonnull SortedSet<T> items)
    throws ConstraintError
  {
    Constraints.constrainNotNull(area, "Area");
    Constraints.constrainArbitrary(
      BoundingAreaCheck.wellFormedF(area),
      "Bounding area is well-formed");
    Constraints.constrainNotNull(items, "Items");

    this.root.areaOverlapping(area, items);
  }

  @Override public void quadTreeQueryRaycast(
    final @Nonnull RayI2D ray,
    final @Nonnull SortedSet<QuadTreeRaycastResultF<T>> items)
    throws ConstraintError
  {
    Constraints.constrainNotNull(ray, "Ray");
    Constraints.constrainNotNull(items, "Items");

    this.root.raycast(ray, items);
  }

  /**
   * Return the set of quadrants intersected by <code>ray</code>.
   * 
   * @param ray
   *          The ray
   * @param items
   *          The resulting set of quadrants
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>ray == null</code></li>
   *           <li><code>items == null</code></li>
   *           </ul>
   */

  void quadTreeQueryRaycastQuadrants(
    final @Nonnull RayI2D ray,
    final @Nonnull SortedSet<QuadTreeRaycastResultF<Quadrant>> items)
    throws ConstraintError
  {
    Constraints.constrainNotNull(ray, "Ray");
    Constraints.constrainNotNull(items, "Items");

    this.root.raycastQuadrants(ray, items);
  }

  @Override public boolean quadTreeRemove(
    final @Nonnull T item)
    throws ConstraintError
  {
    Constraints.constrainNotNull(item, "Item");
    Constraints.constrainArbitrary(
      BoundingAreaCheck.wellFormedF(item),
      "Bounding area is well-formed");

    return this.root.remove(item);
  }

  @Override public void quadTreeTraverse(
    final @Nonnull QuadTreeTraversalF traversal)
    throws Exception,
      ConstraintError
  {
    Constraints.constrainNotNull(traversal, "Traversal");
    this.root.traverse(0, traversal);
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[QuadTree ");
    builder.append(this.root.lower);
    builder.append(" ");
    builder.append(this.root.upper);
    builder.append(" ");
    builder.append(this.objects_all);
    builder.append("]");
    return builder.toString();
  }
}