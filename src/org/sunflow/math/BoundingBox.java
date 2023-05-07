package org.sunflow.math;

/**
 * 3D axis-aligned bounding box. Stores only the minimum and maximum corner
 * points.
 */
public class BoundingBox {
    private Point3 minimum;
    private Point3 maximum;

    /**
     * Creates an empty box. The minimum point will have all components set to
     * positive infinity, and the maximum will have all components set to
     * negative infinity.
     */
    public BoundingBox() {
        minimum = new Point3(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        maximum = new Point3(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
    }

    /**
     * Creates a copy of the given box.
     * 
     * @param b bounding box to copy
     */
    public BoundingBox(BoundingBox b) {
        minimum = new Point3(b.minimum);
        maximum = new Point3(b.maximum);
    }

    /**
     * Creates a bounding box containing only the specified point.
     * 
     * @param p point to include
     */
    public BoundingBox(Point3 p) {
        this(p.getX(), p.getY(), p.getZ());
    }

    /**
     * Creates a bounding box containing only the specified point.
     * 
     * @param x x coordinate of the point to include
     * @param y y coordinate of the point to include
     * @param z z coordinate of the point to include
     */
    public BoundingBox(float x, float y, float z) {
        minimum = new Point3(x, y, z);
        maximum = new Point3(x, y, z);
    }

    /**
     * Creates a bounding box centered around the origin.
     * 
     * @param size half edge length of the bounding box
     */
    public BoundingBox(float size) {
        minimum = new Point3(-size, -size, -size);
        maximum = new Point3(size, size, size);
    }

    /**
     * Gets the minimum corner of the box. That is the corner of smallest
     * coordinates on each axis. Note that the returned reference is not cloned
     * for efficiency purposes so care must be taken not to change the
     * coordinates of the point.
     * 
     * @return a reference to the minimum corner
     */
    public final Point3 getMinimum() {
        return minimum;
    }

    /**
     * Gets the maximum corner of the box. That is the corner of largest
     * coordinates on each axis. Note that the returned reference is not cloned
     * for efficiency purposes so care must be taken not to change the
     * coordinates of the point.
     * 
     * @return a reference to the maximum corner
     */
    public final Point3 getMaximum() {
        return maximum;
    }

    /**
     * Gets the center of the box, computed as (min + max) / 2.
     * 
     * @return a reference to the center of the box
     */
    public final Point3 getCenter() {
        return Point3.mid(minimum, maximum, new Point3());
    }

    /**
     * Gets a corner of the bounding box. The index scheme uses the binary
     * representation of the index to decide which corner to return. Corner 0 is
     * equivalent to the minimum and corner 7 is equivalent to the maximum.
     * 
     * @param i a corner index, from 0 to 7
     * @return the corresponding corner
     */
    public final Point3 getCorner(int i) {
        float x = (i & 1) == 0 ? minimum.getX() : maximum.getX();
        float y = (i & 2) == 0 ? minimum.getY() : maximum.getY();
        float z = (i & 4) == 0 ? minimum.getZ() : maximum.getZ();
        return new Point3(x, y, z);
    }

    /**
     * Gets a specific coordinate of the surface's bounding box.
     * 
     * @param i index of a side from 0 to 5
     * @return value of the request bounding box side
     */
    public final float getBound(int i) {
        switch (i) {
            case 0:
                return minimum.getX();
            case 1:
                return maximum.getX();
            case 2:
                return minimum.getY();
            case 3:
                return maximum.getY();
            case 4:
                return minimum.getZ();
            case 5:
                return maximum.getZ();
            default:
                return 0;
        }
    }

    /**
     * Gets the extents vector for the box. This vector is computed as (max -
     * min). Its coordinates are always positive and represent the dimensions of
     * the box along the three axes.
     * 
     * @return a refreence to the extent vector
     * @see org.sunflow.math.Vector3#length()
     */
    public final Vector3 getExtents() {
        return Point3.sub(maximum, minimum, new Vector3());
    }

    /**
     * Gets the surface area of the box.
     * 
     * @return surface area
     */
    public final float getArea() {
        Vector3 w = getExtents();
        float ax = Math.max(w.x, 0);
        float ay = Math.max(w.y, 0);
        float az = Math.max(w.z, 0);
        return 2 * (ax * ay + ay * az + az * ax);
    }

    /**
     * Gets the box's volume
     * 
     * @return volume
     */
    public final float getVolume() {
        Vector3 w = getExtents();
        float ax = Math.max(w.x, 0);
        float ay = Math.max(w.y, 0);
        float az = Math.max(w.z, 0);
        return ax * ay * az;
    }

    /**
     * Enlarge the bounding box by the minimum possible amount to avoid numeric
     * precision related problems.
     */
    public final void enlargeUlps() {
        final float eps = 0.0001f;
        minimum.x -= Math.max(eps, Math.ulp(minimum.getX()));
        minimum.y -= Math.max(eps, Math.ulp(minimum.getY()));
        minimum.z -= Math.max(eps, Math.ulp(minimum.getZ()));
        maximum.x += Math.max(eps, Math.ulp(maximum.getX()));
        maximum.y += Math.max(eps, Math.ulp(maximum.getY()));
        maximum.z += Math.max(eps, Math.ulp(maximum.getZ()));
    }

    /**
     * Returns <code>true</code> when the box has just been initialized, and
     * is still empty. This method might also return true if the state of the
     * box becomes inconsistent and some component of the minimum corner is
     * larger than the corresponding coordinate of the maximum corner.
     * 
     * @return <code>true</code> if the box is empty, <code>false</code>
     *         otherwise
     */
    public final boolean isEmpty() {
        return (maximum.getX() < minimum.getX()) || (maximum.getY() < minimum.getY()) || (maximum.getZ() < minimum.getZ());
    }

    /**
     * Returns <code>true</code> if the specified bounding box intersects this
     * one. The boxes are treated as volumes, so a box inside another will
     * return true. Returns <code>false</code> if the parameter is
     * <code>null</code>.
     * 
     * @param b box to be tested for intersection
     * @return <code>true</code> if the boxes overlap, <code>false</code>
     *         otherwise
     */
    public final boolean intersects(BoundingBox b) {
        return ((b != null) && (minimum.getX() <= b.maximum.getX()) && (maximum.getX() >= b.minimum.getX()) && (minimum.getY() <= b.maximum.getY()) && (maximum.getY() >= b.minimum.getY()) && (minimum.getZ() <= b.maximum.getZ()) && (maximum.getZ() >= b.minimum.getZ()));
    }

    /**
     * Checks to see if the specified {@link org.sunflow.math.Point3 point}is
     * inside the volume defined by this box. Returns <code>false</code> if
     * the parameter is <code>null</code>.
     * 
     * @param p point to be tested for containment
     * @return <code>true</code> if the point is inside the box,
     *         <code>false</code> otherwise
     */
    public final boolean contains(Point3 p) {
        return ((p != null) && (p.getX() >= minimum.getX()) && (p.getX() <= maximum.getX()) && (p.getY() >= minimum.getY()) && (p.getY() <= maximum.getY()) && (p.getZ() >= minimum.getZ()) && (p.getZ() <= maximum.getZ()));
    }

    /**
     * Check to see if the specified point is inside the volume defined by this
     * box.
     * 
     * @param x x coordinate of the point to be tested
     * @param y y coordinate of the point to be tested
     * @param z z coordinate of the point to be tested
     * @return <code>true</code> if the point is inside the box,
     *         <code>false</code> otherwise
     */
    public final boolean contains(float x, float y, float z) {
        return ((x >= minimum.getX()) && (x <= maximum.getX()) && (y >= minimum.getY()) && (y <= maximum.getY()) && (z >= minimum.getZ()) && (z <= maximum.getZ()));
    }

    /**
     * Changes the extents of the box as needed to include the given
     * {@link org.sunflow.math.Point3 point}into this box. Does nothing if the
     * parameter is <code>null</code>.
     * 
     * @param p point to be included
     */
    public final void include(Point3 p) {
        if (p != null) {
            if (p.getX() < minimum.getX())
                minimum.x = p.getX();
            if (p.getX() > maximum.getX())
                maximum.x = p.getX();
            if (p.getY() < minimum.getY())
                minimum.y = p.getY();
            if (p.getY() > maximum.getY())
                maximum.y = p.getY();
            if (p.getZ() < minimum.getZ())
                minimum.z = p.getZ();
            if (p.getZ() > maximum.getZ())
                maximum.z = p.getZ();
        }
    }

    /**
     * Changes the extents of the box as needed to include the given point into
     * this box.
     * 
     * @param x x coordinate of the point
     * @param y y coordinate of the point
     * @param z z coordinate of the point
     */
    public final void include(float x, float y, float z) {
        if (x < minimum.getX())
            minimum.x = x;
        if (x > maximum.getX())
            maximum.x = x;
        if (y < minimum.getY())
            minimum.y = y;
        if (y > maximum.getY())
            maximum.y = y;
        if (z < minimum.getZ())
            minimum.z = z;
        if (z > maximum.getZ())
            maximum.z = z;
    }

    /**
     * Changes the extents of the box as needed to include the given box into
     * this box. Does nothing if the parameter is <code>null</code>.
     * 
     * @param b box to be included
     */
    public final void include(BoundingBox b) {
        if (b != null) {
            if (b.minimum.getX() < minimum.getX())
                minimum.x = b.minimum.getX();
            if (b.maximum.getX() > maximum.getX())
                maximum.x = b.maximum.getX();
            if (b.minimum.getY() < minimum.getY())
                minimum.y = b.minimum.getY();
            if (b.maximum.getY() > maximum.getY())
                maximum.y = b.maximum.getY();
            if (b.minimum.getZ() < minimum.getZ())
                minimum.z = b.minimum.getZ();
            if (b.maximum.getZ() > maximum.getZ())
                maximum.z = b.maximum.getZ();
        }
    }

    @Override
    public final String toString() {
        return String.format("(%.2f, %.2f, %.2f) to (%.2f, %.2f, %.2f)", minimum.getX(), minimum.getY(), minimum.getZ(), maximum.getX(), maximum.getY(), maximum.getZ());
    }
}