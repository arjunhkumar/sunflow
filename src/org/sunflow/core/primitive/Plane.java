package org.sunflow.core.primitive;

import org.sunflow.SunflowAPI;
import org.sunflow.core.Instance;
import org.sunflow.core.IntersectionState;
import org.sunflow.core.ParameterList;
import org.sunflow.core.PrimitiveList;
import org.sunflow.core.Ray;
import org.sunflow.core.ShadingState;
import org.sunflow.math.BoundingBox;
import org.sunflow.math.Matrix4;
import org.sunflow.math.OrthoNormalBasis;
import org.sunflow.math.Point3;
import org.sunflow.math.Vector3;

public class Plane implements PrimitiveList {
    private Point3 center;
    private Vector3 normal;
    int k;
    private float bnu, bnv, bnd;
    private float cnu, cnv, cnd;

    public Plane() {
        center = new Point3(0, 0, 0);
        normal = new Vector3(0, 1, 0);
        k = 3;
        bnu = bnv = bnd = 0;
        cnu = cnv = cnd = 0;
    }

    public boolean update(ParameterList pl, SunflowAPI api) {
        center = pl.getPoint("center", center);
        Point3 b = pl.getPoint("point1", null);
        Point3 c = pl.getPoint("point2", null);
        if (b != null && c != null) {
            Point3 v0 = center;
            Point3 v1 = b;
            Point3 v2 = c;
            Vector3 ng = normal = Vector3.cross(Point3.sub(v1, v0, new Vector3()), Point3.sub(v2, v0, new Vector3()), new Vector3()).normalize();
            if (Math.abs(ng.x) > Math.abs(ng.y) && Math.abs(ng.x) > Math.abs(ng.z))
                k = 0;
            else if (Math.abs(ng.y) > Math.abs(ng.z))
                k = 1;
            else
                k = 2;
            float ax, ay, bx, by, cx, cy;
            switch (k) {
                case 0: {
                    ax = v0.getY();
                    ay = v0.getZ();
                    bx = v2.getY() - ax;
                    by = v2.getZ() - ay;
                    cx = v1.getY() - ax;
                    cy = v1.getZ() - ay;
                    break;
                }
                case 1: {
                    ax = v0.getZ();
                    ay = v0.getX();
                    bx = v2.getZ() - ax;
                    by = v2.getX() - ay;
                    cx = v1.getZ() - ax;
                    cy = v1.getX() - ay;
                    break;
                }
                case 2:
                default: {
                    ax = v0.getX();
                    ay = v0.getY();
                    bx = v2.getX() - ax;
                    by = v2.getY() - ay;
                    cx = v1.getX() - ax;
                    cy = v1.getY() - ay;
                }
            }
            float det = bx * cy - by * cx;
            bnu = -by / det;
            bnv = bx / det;
            bnd = (by * ax - bx * ay) / det;
            cnu = cy / det;
            cnv = -cx / det;
            cnd = (cx * ay - cy * ax) / det;
        } else {
            normal = pl.getVector("normal", normal);
            k = 3;
            bnu = bnv = bnd = 0;
            cnu = cnv = cnd = 0;
        }
        return true;
    }

    public void prepareShadingState(ShadingState state) {
        state.init();
        state.getRay().getPoint(state.getPoint());
        Instance parent = state.getInstance();
        Vector3 worldNormal = state.transformNormalObjectToWorld(normal);
        state.getNormal().set(worldNormal);
        state.getGeoNormal().set(worldNormal);
        state.setShader(parent.getShader(0));
        state.setModifier(parent.getModifier(0));
        Point3 p = state.transformWorldToObject(state.getPoint());
        float hu, hv;
        switch (k) {
            case 0: {
                hu = p.getY();
                hv = p.getZ();
                break;
            }
            case 1: {
                hu = p.getZ();
                hv = p.getX();
                break;
            }
            case 2: {
                hu = p.getX();
                hv = p.getY();
                break;
            }
            default:
                hu = hv = 0;
        }
        state.getUV().x = hu * bnu + hv * bnv + bnd;
        state.getUV().y = hu * cnu + hv * cnv + cnd;
        state.setBasis(OrthoNormalBasis.makeFromW(normal));
    }

    public void intersectPrimitive(Ray r, int primID, IntersectionState state) {
        float dn = normal.x * r.dx + normal.y * r.dy + normal.z * r.dz;
        if (dn == 0.0)
            return;
        float t = (((center.getX() - r.ox) * normal.x) + ((center.getY() - r.oy) * normal.y) + ((center.getZ() - r.oz) * normal.z)) / dn;
        if (r.isInside(t)) {
            r.setMax(t);
            state.setIntersection(0);
        }
    }

    public int getNumPrimitives() {
        return 1;
    }

    public float getPrimitiveBound(int primID, int i) {
        return 0;
    }

    public BoundingBox getWorldBounds(Matrix4 o2w) {
        return null;
    }

    public PrimitiveList getBakingPrimitives() {
        return null;
    }
}