package org.sunflow.core.shader;

import org.sunflow.SunflowAPI;
import org.sunflow.core.ParameterList;
import org.sunflow.core.Shader;
import org.sunflow.core.ShadingState;
import org.sunflow.image.Color;
import org.sunflow.math.Matrix4;
import org.sunflow.math.Point3;

public class WireframeShader implements Shader {
    private Color lineColor;
    private Color fillColor;
    private float width;
    private float cosWidth;

    public WireframeShader() {
        lineColor = Color.BLACK;
        fillColor = Color.WHITE;
        // pick a very small angle - should be roughly the half the angular
        // width of a
        // pixel
        width = (float) (Math.PI * 0.5 / 4096);
        cosWidth = (float) Math.cos(width);
    }

    public boolean update(ParameterList pl, SunflowAPI api) {
        lineColor = pl.getColor("line", lineColor);
        fillColor = pl.getColor("fill", fillColor);
        width = pl.getFloat("width", width);
        cosWidth = (float) Math.cos(width);
        return true;
    }

    public Color getFillColor(ShadingState state) {
        return fillColor;
    }

    public Color getLineColor(ShadingState state) {
        return lineColor;
    }

    public Color getRadiance(ShadingState state) {
        Point3[] p = new Point3[3];
        if (!state.getTrianglePoints(p))
            return getFillColor(state);
        // transform points into camera space
        Point3 center = state.getPoint();
        Matrix4 w2c = state.getWorldToCamera();
        center = w2c.transformP(center);
        for (int i = 0; i < 3; i++)
            p[i] = w2c.transformP(state.transformObjectToWorld(p[i]));
        float cn = 1.0f / (float) Math.sqrt(center.getX() * center.getX() + center.getY() * center.getY() + center.getZ() * center.getZ());
        for (int i = 0, i2 = 2; i < 3; i2 = i, i++) {
            // compute orthogonal projection of the shading point onto each
            // triangle edge as in:
            // http://mathworld.wolfram.com/Point-LineDistance3-Dimensional.html
            float t = (center.getX() - p[i].getX()) * (p[i2].getX() - p[i].getX());
            t += (center.getY() - p[i].getY()) * (p[i2].getY() - p[i].getY());
            t += (center.getZ() - p[i].getZ()) * (p[i2].getZ() - p[i].getZ());
            t /= p[i].distanceToSquared(p[i2]);
            float projx = (1 - t) * p[i].getX() + t * p[i2].getX();
            float projy = (1 - t) * p[i].getY() + t * p[i2].getY();
            float projz = (1 - t) * p[i].getZ() + t * p[i2].getZ();
            float n = 1.0f / (float) Math.sqrt(projx * projx + projy * projy + projz * projz);
            // check angular width
            float dot = projx * center.getX() + projy * center.getY() + projz * center.getZ();
            if (dot * n * cn >= cosWidth)
                return getLineColor(state);
        }
        return getFillColor(state);
    }

    public void scatterPhoton(ShadingState state, Color power) {
    }
}