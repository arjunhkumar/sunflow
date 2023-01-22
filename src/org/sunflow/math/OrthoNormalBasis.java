package org.sunflow.math;

public final class OrthoNormalBasis {
    private Vector3 u, v, w;

    private OrthoNormalBasis() {
        u = new Vector3();
        v = new Vector3();
        w = new Vector3();
    }

    public void flipU() {
        u= u.negate();
    }

    public void flipV() {
        v = v.negate();
    }

    public void flipW() {
        w = w.negate();
    }

    public void swapUV() {
        Vector3 t = u;
        u = v;
        v = t;
    }

    public void swapVW() {
        Vector3 t = v;
        v = w;
        w = t;
    }

    public void swapWU() {
        Vector3 t = w;
        w = u;
        u = t;
    }

    public Vector3 transform(Vector3 a, Vector3 dest) {
        /** AR07 Modified to make Vector3 class primitive */
        float x = (a.x * u.x) + (a.y * v.x) + (a.z * w.x);
        float y = (a.x * u.y) + (a.y * v.y) + (a.z * w.y);
        float z = (a.x * u.z) + (a.y * v.z) + (a.z * w.z);
        dest = new Vector3(x, y, z);
        return dest;
    }

    public Vector3 transform(Vector3 a) {
        /** AR07 Modified to make Vector3 class primitive */
        float x = (a.x * u.x) + (a.y * v.x) + (a.z * w.x);
        float y = (a.x * u.y) + (a.y * v.y) + (a.z * w.y);
        float z = (a.x * u.z) + (a.y * v.z) + (a.z * w.z);
        a = new Vector3(x, y, z);
        return a;
    }

    public Vector3 untransform(Vector3 a, Vector3 dest) {
        /** AR07 Modified to make Vector3 class primitive */
        float x = Vector3.dot(a, u);
        float y = Vector3.dot(a, v);
        float z = Vector3.dot(a, w);
        dest = new Vector3(x, y, z);
        return dest;
    }

    public Vector3 untransform(Vector3 a) {
        float x = Vector3.dot(a, u);
        float y = Vector3.dot(a, v);
        float z = Vector3.dot(a, w);
        a = a.set(x, y, z);
        return a;
    }

    public float untransformX(Vector3 a) {
        return Vector3.dot(a, u);
    }

    public float untransformY(Vector3 a) {
        return Vector3.dot(a, v);
    }

    public float untransformZ(Vector3 a) {
        return Vector3.dot(a, w);
    }

    public static final OrthoNormalBasis makeFromW(Vector3 w) {
        OrthoNormalBasis onb = new OrthoNormalBasis();
        onb.w = w.normalize(onb.w);
        /** AR07 Modified to make Vector3 class primitive */
        if ((Math.abs(onb.w.x) < Math.abs(onb.w.y)) && (Math.abs(onb.w.x) < Math.abs(onb.w.z))) {
            onb.v = new Vector3(0, onb.w.z, -onb.w.y);
//            onb.v.x = 0;
//            onb.v.y = onb.w.z;
//            onb.v.z = -onb.w.y;
        } else if (Math.abs(onb.w.y) < Math.abs(onb.w.z)) {
            onb.v = new Vector3(onb.w.z,0, -onb.w.x);
//            onb.v.x = onb.w.z;
//            onb.v.y = 0;
//            onb.v.z = -onb.w.x;
        } else {
            onb.v = new Vector3(onb.w.y,onb.w.x,0);
//            onb.v.x = onb.w.y;
//            onb.v.y = -onb.w.x;
//            onb.v.z = 0;
        }
        onb.v = onb.v.normalize();
        onb.u = Vector3.cross(onb.v, onb.w, onb.u);
        return onb;
    }

    public static final OrthoNormalBasis makeFromWV(Vector3 w, Vector3 v) {
        /** AR07 Modified to make Vector3 class primitive */
        OrthoNormalBasis onb = new OrthoNormalBasis();
        onb.w = w.normalize(onb.w);
        onb.u = Vector3.cross(v, onb.w, onb.u).normalize();
        onb.v = Vector3.cross(onb.w, onb.u, onb.v);
        return onb;
    }
}