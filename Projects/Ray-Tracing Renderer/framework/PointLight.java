package framework;

import java.awt.Color;

public class PointLight {
    private Vec3 pos;
    private Color color;
    private double brightness;
    private double attenConstLin;
    private double attenConstQuad;

    public PointLight(Vec3 pos, Color color, double brightness) {
        this(pos, color, brightness, 1, 0.2);
    }
    public PointLight(Vec3 pos, Color color, double brightness, double attenConstLin, double attenConstQuad) {
        this.pos = pos;
        this.color = color;
        this.brightness = brightness;
        this.attenConstLin = attenConstLin;
        this.attenConstQuad = attenConstQuad;
    }

    public Vec3 getPos() {
        return this.pos;
    }
    public Color getColor() {
        return this.color;
    }
    public double calcAttenScalar(double dst) {
        return brightness / (1.0 + attenConstLin * dst + attenConstQuad * dst * dst);
    }
}
