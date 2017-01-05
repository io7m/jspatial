package com.io7m.jspatial.api.quadtrees;

import com.io7m.jspatial.api.BoundingAreaD;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;

/**
 * Immutable implementation of {@link QuadTreeConfigurationDType}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code QuadTreeConfigurationD.builder()}.
 * Use the static factory method to create immutable instances:
 * {@code QuadTreeConfigurationD.of()}.
 */
@SuppressWarnings("all")
@Generated({"Immutables.generator", "QuadTreeConfigurationDType"})
public final class QuadTreeConfigurationD
    implements QuadTreeConfigurationDType {
  private final BoundingAreaD area;
  private final double minimumQuadrantWidth;
  private final double minimumQuadrantHeight;
  private final boolean trimOnRemove;

  private QuadTreeConfigurationD(
      BoundingAreaD area,
      double minimumQuadrantWidth,
      double minimumQuadrantHeight,
      boolean trimOnRemove) {
    this.area = Objects.requireNonNull(area, "area");
    this.minimumQuadrantWidth = minimumQuadrantWidth;
    this.minimumQuadrantHeight = minimumQuadrantHeight;
    this.trimOnRemove = trimOnRemove;
    initShim.setMinimumQuadrantWidth(this.minimumQuadrantWidth);
    initShim.setMinimumQuadrantHeight(this.minimumQuadrantHeight);
    initShim.setTrimOnRemove(this.trimOnRemove);
    this.initShim = null;
  }

  private QuadTreeConfigurationD(QuadTreeConfigurationD.Builder builder) {
    this.area = builder.area;
    if (builder.minimumQuadrantWidthIsSet()) {
      initShim.setMinimumQuadrantWidth(builder.minimumQuadrantWidth);
    }
    if (builder.minimumQuadrantHeightIsSet()) {
      initShim.setMinimumQuadrantHeight(builder.minimumQuadrantHeight);
    }
    if (builder.trimOnRemoveIsSet()) {
      initShim.setTrimOnRemove(builder.trimOnRemove);
    }
    this.minimumQuadrantWidth = initShim.minimumQuadrantWidth();
    this.minimumQuadrantHeight = initShim.minimumQuadrantHeight();
    this.trimOnRemove = initShim.trimOnRemove();
    this.initShim = null;
  }

  private QuadTreeConfigurationD(
      QuadTreeConfigurationD original,
      BoundingAreaD area,
      double minimumQuadrantWidth,
      double minimumQuadrantHeight,
      boolean trimOnRemove) {
    this.area = area;
    this.minimumQuadrantWidth = minimumQuadrantWidth;
    this.minimumQuadrantHeight = minimumQuadrantHeight;
    this.trimOnRemove = trimOnRemove;
    this.initShim = null;
  }

  private static final int STAGE_INITIALIZING = -1;
  private static final int STAGE_UNINITIALIZED = 0;
  private static final int STAGE_INITIALIZED = 1;
  private transient volatile InitShim initShim = new InitShim();

  private final class InitShim {
    private double minimumQuadrantWidth;
    private int minimumQuadrantWidthStage;

    double minimumQuadrantWidth() {
      if (minimumQuadrantWidthStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (minimumQuadrantWidthStage == STAGE_UNINITIALIZED) {
        minimumQuadrantWidthStage = STAGE_INITIALIZING;
        this.minimumQuadrantWidth = minimumQuadrantWidthInitialize();
        minimumQuadrantWidthStage = STAGE_INITIALIZED;
      }
      return this.minimumQuadrantWidth;
    }

    void setMinimumQuadrantWidth(double minimumQuadrantWidth) {
      this.minimumQuadrantWidth = minimumQuadrantWidth;
      minimumQuadrantWidthStage = STAGE_INITIALIZED;
    }
    private double minimumQuadrantHeight;
    private int minimumQuadrantHeightStage;

    double minimumQuadrantHeight() {
      if (minimumQuadrantHeightStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (minimumQuadrantHeightStage == STAGE_UNINITIALIZED) {
        minimumQuadrantHeightStage = STAGE_INITIALIZING;
        this.minimumQuadrantHeight = minimumQuadrantHeightInitialize();
        minimumQuadrantHeightStage = STAGE_INITIALIZED;
      }
      return this.minimumQuadrantHeight;
    }

    void setMinimumQuadrantHeight(double minimumQuadrantHeight) {
      this.minimumQuadrantHeight = minimumQuadrantHeight;
      minimumQuadrantHeightStage = STAGE_INITIALIZED;
    }
    private boolean trimOnRemove;
    private int trimOnRemoveStage;

    boolean trimOnRemove() {
      if (trimOnRemoveStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
      if (trimOnRemoveStage == STAGE_UNINITIALIZED) {
        trimOnRemoveStage = STAGE_INITIALIZING;
        this.trimOnRemove = trimOnRemoveInitialize();
        trimOnRemoveStage = STAGE_INITIALIZED;
      }
      return this.trimOnRemove;
    }

    void setTrimOnRemove(boolean trimOnRemove) {
      this.trimOnRemove = trimOnRemove;
      trimOnRemoveStage = STAGE_INITIALIZED;
    }

    private String formatInitCycleMessage() {
      ArrayList<String> attributes = new ArrayList<String>();
      if (minimumQuadrantWidthStage == STAGE_INITIALIZING) attributes.add("minimumQuadrantWidth");
      if (minimumQuadrantHeightStage == STAGE_INITIALIZING) attributes.add("minimumQuadrantHeight");
      if (trimOnRemoveStage == STAGE_INITIALIZING) attributes.add("trimOnRemove");
      return "Cannot build QuadTreeConfigurationD, attribute initializers form cycle" + attributes;
    }
  }

  private double minimumQuadrantWidthInitialize() {
    return QuadTreeConfigurationDType.super.minimumQuadrantWidth();
  }

  private double minimumQuadrantHeightInitialize() {
    return QuadTreeConfigurationDType.super.minimumQuadrantHeight();
  }

  private boolean trimOnRemoveInitialize() {
    return QuadTreeConfigurationDType.super.trimOnRemove();
  }

  /**
   * @return The maximum bounding area of the tree
   */
  @Override
  public BoundingAreaD area() {
    return area;
  }

  /**
   * @return The minimum width of quadrants (must be {@code >= 0.0001})
   */
  @Override
  public double minimumQuadrantWidth() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.minimumQuadrantWidth()
        : this.minimumQuadrantWidth;
  }

  /**
   * @return The minimum height of quadrants (must be {@code >= 0.0001})
   */
  @Override
  public double minimumQuadrantHeight() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.minimumQuadrantHeight()
        : this.minimumQuadrantHeight;
  }

  /**
   * @return {@code true} iff the implementation should attempt to trim empty
   * leaf nodes when an item is removed
   */
  @Override
  public boolean trimOnRemove() {
    InitShim shim = this.initShim;
    return shim != null
        ? shim.trimOnRemove()
        : this.trimOnRemove;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link QuadTreeConfigurationDType#area() area} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param area A new value for area
   * @return A modified copy of the {@code this} object
   */
  public final QuadTreeConfigurationD withArea(BoundingAreaD area) {
    if (this.area == area) return this;
    BoundingAreaD newValue = Objects.requireNonNull(area, "area");
    return new QuadTreeConfigurationD(this, newValue, this.minimumQuadrantWidth, this.minimumQuadrantHeight, this.trimOnRemove);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link QuadTreeConfigurationDType#minimumQuadrantWidth() minimumQuadrantWidth} attribute.
   * A value strict bits equality used to prevent copying of the same value by returning {@code this}.
   * @param minimumQuadrantWidth A new value for minimumQuadrantWidth
   * @return A modified copy of the {@code this} object
   */
  public final QuadTreeConfigurationD withMinimumQuadrantWidth(double minimumQuadrantWidth) {
    if (Double.doubleToLongBits(this.minimumQuadrantWidth) == Double.doubleToLongBits(minimumQuadrantWidth)) return this;
    return new QuadTreeConfigurationD(this, this.area, minimumQuadrantWidth, this.minimumQuadrantHeight, this.trimOnRemove);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link QuadTreeConfigurationDType#minimumQuadrantHeight() minimumQuadrantHeight} attribute.
   * A value strict bits equality used to prevent copying of the same value by returning {@code this}.
   * @param minimumQuadrantHeight A new value for minimumQuadrantHeight
   * @return A modified copy of the {@code this} object
   */
  public final QuadTreeConfigurationD withMinimumQuadrantHeight(double minimumQuadrantHeight) {
    if (Double.doubleToLongBits(this.minimumQuadrantHeight) == Double.doubleToLongBits(minimumQuadrantHeight)) return this;
    return new QuadTreeConfigurationD(this, this.area, this.minimumQuadrantWidth, minimumQuadrantHeight, this.trimOnRemove);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link QuadTreeConfigurationDType#trimOnRemove() trimOnRemove} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param trimOnRemove A new value for trimOnRemove
   * @return A modified copy of the {@code this} object
   */
  public final QuadTreeConfigurationD withTrimOnRemove(boolean trimOnRemove) {
    if (this.trimOnRemove == trimOnRemove) return this;
    return new QuadTreeConfigurationD(this, this.area, this.minimumQuadrantWidth, this.minimumQuadrantHeight, trimOnRemove);
  }

  /**
   * This instance is equal to all instances of {@code QuadTreeConfigurationD} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof QuadTreeConfigurationD
        && equalTo((QuadTreeConfigurationD) another);
  }

  private boolean equalTo(QuadTreeConfigurationD another) {
    return area.equals(another.area)
        && Double.doubleToLongBits(minimumQuadrantWidth) == Double.doubleToLongBits(another.minimumQuadrantWidth)
        && Double.doubleToLongBits(minimumQuadrantHeight) == Double.doubleToLongBits(another.minimumQuadrantHeight)
        && trimOnRemove == another.trimOnRemove;
  }

  /**
   * Computes a hash code from attributes: {@code area}, {@code minimumQuadrantWidth}, {@code minimumQuadrantHeight}, {@code trimOnRemove}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 31;
    h = h * 17 + area.hashCode();
    h = h * 17 + Double.hashCode(minimumQuadrantWidth);
    h = h * 17 + Double.hashCode(minimumQuadrantHeight);
    h = h * 17 + Boolean.hashCode(trimOnRemove);
    return h;
  }

  /**
   * Prints the immutable value {@code QuadTreeConfigurationD} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "QuadTreeConfigurationD{"
        + "area=" + area
        + ", minimumQuadrantWidth=" + minimumQuadrantWidth
        + ", minimumQuadrantHeight=" + minimumQuadrantHeight
        + ", trimOnRemove=" + trimOnRemove
        + "}";
  }

  /**
   * Construct a new immutable {@code QuadTreeConfigurationD} instance.
   * @param area The value for the {@code area} attribute
   * @param minimumQuadrantWidth The value for the {@code minimumQuadrantWidth} attribute
   * @param minimumQuadrantHeight The value for the {@code minimumQuadrantHeight} attribute
   * @param trimOnRemove The value for the {@code trimOnRemove} attribute
   * @return An immutable QuadTreeConfigurationD instance
   */
  public static QuadTreeConfigurationD of(BoundingAreaD area, double minimumQuadrantWidth, double minimumQuadrantHeight, boolean trimOnRemove) {
    return new QuadTreeConfigurationD(area, minimumQuadrantWidth, minimumQuadrantHeight, trimOnRemove);
  }

  /**
   * Creates an immutable copy of a {@link QuadTreeConfigurationDType} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable QuadTreeConfigurationD instance
   */
  public static QuadTreeConfigurationD copyOf(QuadTreeConfigurationDType instance) {
    if (instance instanceof QuadTreeConfigurationD) {
      return (QuadTreeConfigurationD) instance;
    }
    return QuadTreeConfigurationD.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link QuadTreeConfigurationD QuadTreeConfigurationD}.
   * @return A new QuadTreeConfigurationD builder
   */
  public static QuadTreeConfigurationD.Builder builder() {
    return new QuadTreeConfigurationD.Builder();
  }

  /**
   * Builds instances of type {@link QuadTreeConfigurationD QuadTreeConfigurationD}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  public static final class Builder {
    private static final long INIT_BIT_AREA = 0x1L;
    private static final long OPT_BIT_MINIMUM_QUADRANT_WIDTH = 0x1L;
    private static final long OPT_BIT_MINIMUM_QUADRANT_HEIGHT = 0x2L;
    private static final long OPT_BIT_TRIM_ON_REMOVE = 0x4L;
    private long initBits = 0x1L;
    private long optBits;

    private BoundingAreaD area;
    private double minimumQuadrantWidth;
    private double minimumQuadrantHeight;
    private boolean trimOnRemove;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code QuadTreeConfigurationDType} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(QuadTreeConfigurationDType instance) {
      Objects.requireNonNull(instance, "instance");
      setArea(instance.area());
      setMinimumQuadrantWidth(instance.minimumQuadrantWidth());
      setMinimumQuadrantHeight(instance.minimumQuadrantHeight());
      setTrimOnRemove(instance.trimOnRemove());
      return this;
    }

    /**
     * Initializes the value for the {@link QuadTreeConfigurationDType#area() area} attribute.
     * @param area The value for area 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder setArea(BoundingAreaD area) {
      this.area = Objects.requireNonNull(area, "area");
      initBits &= ~INIT_BIT_AREA;
      return this;
    }

    /**
     * Initializes the value for the {@link QuadTreeConfigurationDType#minimumQuadrantWidth() minimumQuadrantWidth} attribute.
     * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link QuadTreeConfigurationDType#minimumQuadrantWidth() minimumQuadrantWidth}.</em>
     * @param minimumQuadrantWidth The value for minimumQuadrantWidth 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder setMinimumQuadrantWidth(double minimumQuadrantWidth) {
      this.minimumQuadrantWidth = minimumQuadrantWidth;
      optBits |= OPT_BIT_MINIMUM_QUADRANT_WIDTH;
      return this;
    }

    /**
     * Initializes the value for the {@link QuadTreeConfigurationDType#minimumQuadrantHeight() minimumQuadrantHeight} attribute.
     * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link QuadTreeConfigurationDType#minimumQuadrantHeight() minimumQuadrantHeight}.</em>
     * @param minimumQuadrantHeight The value for minimumQuadrantHeight 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder setMinimumQuadrantHeight(double minimumQuadrantHeight) {
      this.minimumQuadrantHeight = minimumQuadrantHeight;
      optBits |= OPT_BIT_MINIMUM_QUADRANT_HEIGHT;
      return this;
    }

    /**
     * Initializes the value for the {@link QuadTreeConfigurationDType#trimOnRemove() trimOnRemove} attribute.
     * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link QuadTreeConfigurationDType#trimOnRemove() trimOnRemove}.</em>
     * @param trimOnRemove The value for trimOnRemove 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder setTrimOnRemove(boolean trimOnRemove) {
      this.trimOnRemove = trimOnRemove;
      optBits |= OPT_BIT_TRIM_ON_REMOVE;
      return this;
    }

    /**
     * Builds a new {@link QuadTreeConfigurationD QuadTreeConfigurationD}.
     * @return An immutable instance of QuadTreeConfigurationD
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public QuadTreeConfigurationD build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new QuadTreeConfigurationD(this);
    }

    private boolean minimumQuadrantWidthIsSet() {
      return (optBits & OPT_BIT_MINIMUM_QUADRANT_WIDTH) != 0;
    }

    private boolean minimumQuadrantHeightIsSet() {
      return (optBits & OPT_BIT_MINIMUM_QUADRANT_HEIGHT) != 0;
    }

    private boolean trimOnRemoveIsSet() {
      return (optBits & OPT_BIT_TRIM_ON_REMOVE) != 0;
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<String>();
      if ((initBits & INIT_BIT_AREA) != 0) attributes.add("area");
      return "Cannot build QuadTreeConfigurationD, some of required attributes are not set " + attributes;
    }
  }
}