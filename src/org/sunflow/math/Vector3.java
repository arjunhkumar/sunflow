package org.sunflow.math;

public final class Vector3 {
    private static final float[] COS_THETA = new float[256];
    private static final float[] SIN_THETA = new float[256];
    private static final float[] COS_PHI = new float[256];
    private static final float[] SIN_PHI = new float[256];

    /** AR07 Modified to make this class primitive */
    public final float x, y, z;

    static {
        // precompute tables to compress unit vectors
        for (int i = 0; i < 256; i++) {
            double angle = (i * Math.PI) / 256.0;
            COS_THETA[i] = (float) Math.cos(angle);
            SIN_THETA[i] = (float) Math.sin(angle);
            COS_PHI[i] = (float) Math.cos(2 * angle);
            SIN_PHI[i] = (float) Math.sin(2 * angle);
        }
    }

    /** AR07 Modified to make this class primitive */
    public Vector3() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3 v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public static final Vector3 decode(short n, Vector3 dest) {
        int t = (n & 0xFF00) >>> 8;
        int p = n & 0xFF;
        /** AR07 Modified to make this class primitive */
//        dest.x = SIN_THETA[t] * COS_PHI[p];
//        dest.y = SIN_THETA[t] * SIN_PHI[p];
//        dest.z = COS_THETA[t];
        dest = new Vector3(SIN_THETA[t] * COS_PHI[p],SIN_THETA[t] * SIN_PHI[p],COS_THETA[t]);
        return dest;
    }

    public static final Vector3 decode(short n) {
        return decode(n, new Vector3());
    }

    public final short encode() {
        int theta = (int) (Math.acos(z) * (256.0 / Math.PI));
        if (theta > 255)
            theta = 255;
        int phi = (int) (Math.atan2(y, x) * (128.0 / Math.PI));
        if (phi < 0)
            phi += 256;
        else if (phi > 255)
            phi = 255;
        return (short) (((theta & 0xFF) << 8) | (phi & 0xFF));
    }

    public float get(int i) {
        switch (i) {
            case 0:
                return x;
            case 1:
                return y;
            default:
                return z;
        }
    }

    public final float length() {
        return (float) Math.sqrt((x * x) + (y * y) + (z * z));
    }

    public final float lengthSquared() {
        return (x * x) + (y * y) + (z * z);
    }

    public final Vector3 negate() {
        /** AR07 Modified to make this class primitive */
//        x = -x;
//        y = -y;
//        z = -z;
        return new Vector3(-x,-y,-z);
    }

    public final Vector3 negate(Vector3 dest) {
        /** AR07 Modified to make this class primitive */
//        dest.x = -x;
//        dest.y = -y;
//        dest.z = -z;
        dest = new Vector3(-x,-y,-z);
        return dest;
    }

    public final Vector3 mul(float s) {
        /** AR07 Modified to make this class primitive */
//        x *= s;
//        y *= s;
//        z *= s;
        return new Vector3(x*s,y*s,z*s);
    }

    public final Vector3 mul(float s, Vector3 dest) {
        /** AR07 Modified to make this class primitive */
//        dest.x = x * s;
//        dest.y = y * s;
//        dest.z = z * s;
        dest = new Vector3(x*s,y*s,z*s);
        return dest;
    }

    public final Vector3 div(float d) {
        /** AR07 Modified to make this class primitive */
//        x /= d;
//        y /= d;
//        z /= d;
//        return this;
        return new Vector3(x/d,y/d,z/d);
    }

    public final Vector3 div(float d, Vector3 dest) {
        /** AR07 Modified to make this class primitive */
//        dest.x = x / d;
//        dest.y = y / d;
//        dest.z = z / d;
        dest = new Vector3(x/d,y/d,z/d);
        return dest;
    }

    /** AR07 Modified to make this class primitive */
//    public final float normalizeLength() {
//        float n = (float) Math.sqrt(x * x + y * y + z * z);
//        float in = 1.0f / n;
//        /** AR07 Modified to make this class primitive */
//        x *= in;
//        y *= in;
//        z *= in;
//        return n;
//    }

    public final Vector3 normalize() {
        /** AR07 Modified to make this class primitive */
        float in = 1.0f / (float) Math.sqrt((x * x) + (y * y) + (z * z));
//        x *= in;
//        y *= in;
//        z *= in;
        return new Vector3(x*in,y*in,z*in);
//        return this;
    }

    public final Vector3 normalize(Vector3 dest) {
        /** AR07 Modified to make this class primitive */
        float in = 1.0f / (float) Math.sqrt((x * x) + (y * y) + (z * z));
//        dest.x = x * in;
//        dest.y = y * in;
//        dest.z = z * in;
        dest = new Vector3(x * in,y * in,z * in);
        return dest;
    }

    public final Vector3 set(float x, float y, float z) {
        /** AR07 Modified to make this class primitive */
//        this.x = x;
//        this.y = y;
//        this.z = z;
        return new Vector3(x,y,z);
    }

    public final Vector3 set(Vector3 v) {
        /** AR07 Modified to make this class primitive */
//        x = v.x;
//        y = v.y;
//        z = v.z;
//        return this;
        return new Vector3(v.x,v.y,v.z);
    }

    public final float dot(float vx, float vy, float vz) {
        return vx * x + vy * y + vz * z;
    }

    public static final float dot(Vector3 v1, Vector3 v2) {
        return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
    }

    public static final Vector3 cross(Vector3 v1, Vector3 v2, Vector3 dest) {
        /** AR07 Modified to make this class primitive */
        float x = (v1.y * v2.z) - (v1.z * v2.y);
        float y = (v1.z * v2.x) - (v1.x * v2.z);
        float z = (v1.x * v2.y) - (v1.y * v2.x);
        dest = new Vector3(x,y,z);
        return dest;
    }

    public static final Vector3 add(Vector3 v1, Vector3 v2, Vector3 dest) {
        /** AR07 Modified to make this class primitive */
        float x = v1.x + v2.x;
        float y = v1.y + v2.y;
        float z = v1.z + v2.z;
        dest = new Vector3(x,y,z);
        return dest;
    }

    public static final Vector3 sub(Vector3 v1, Vector3 v2, Vector3 dest) {
        /** AR07 Modified to make this class primitive */
        float x = v1.x - v2.x;
        float y = v1.y - v2.y;
        float z = v1.z - v2.z;
        dest = new Vector3(x,y,z);
        return dest;
    }

    @Override
    public final String toString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }
}