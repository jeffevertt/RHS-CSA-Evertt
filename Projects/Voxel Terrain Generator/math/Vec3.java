package math;

import java.awt.Color;

import framework.Util;

public class Vec3 {
    public double x = 0, y = 0, z = 0;

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3 add(Vec3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }
    
    public Vec3 plus(Vec3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }

    public Vec3 subtract(Vec3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }
    
    public Vec3 minus(Vec3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }

    public Vec3 multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }
    
    public Vec3 times(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }
    
    public Vec3 divide(double scalar) {
        this.x /= scalar;
        this.y /= scalar;
        this.z /= scalar;
        return this;
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double lengthSqr() {
        return (this.x * this.x + this.y * this.y + this.z * this.z);
    }
    
    public Vec3 normalize() {
        double length = this.length();
        double invLen = (length == 0) ? 1.0 : (1.0 / this.length());
        this.x *= invLen;
        this.y *= invLen;
        this.z *= invLen;
        return this;
    }

    public Vec3 unit() {
        Vec3 unitVec = new Vec3(this.x, this.y, this.z);
        unitVec.normalize();
        return unitVec;
    }

    public double distance(Vec3 other) {
        Vec3 me = new Vec3(this.x, this.y, this.z);
        return me.subtract(other).length();
    }

    public double distanceSqr(Vec3 other) {
        Vec3 me = new Vec3(this.x, this.y, this.z);
        return me.subtract(other).lengthSqr();
    }
    
    public double dot(Vec3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vec3 cross(Vec3 other) { // todo - need to test this
        return new Vec3(this.y * other.z - this.z * other.y, 
                        this.z * other.x - this.x * other.z, 
                        this.x * other.y - this.y * other.x);
    }

    public Vec3 lerp(Vec3 v1, Vec3 v2, double t) {
        Vec3 delta = subtract(v2, v1);
        Vec3 ret = new Vec3(v1.x, v1.y, v1.z);
        return ret.add(delta.multiply(t));
    }

    public Vec3 max(Vec3 other) {
        this.x = Math.max(this.x, other.x);
        this.y = Math.max(this.y, other.y);
        this.z = Math.max(this.z, other.z);
        return this;
    }

    public Vec3 min(Vec3 other) {
        this.x = Math.min(this.x, other.x);
        this.y = Math.min(this.y, other.y);
        this.z = Math.min(this.z, other.z);
        return this;
    }

    public Vec3 clamp(Vec3 min, Vec3 max) {
        this.x = Util.clamp(this.x, min.x, max.x);
        this.y = Util.clamp(this.y, min.y, max.y);
        this.z = Util.clamp(this.z, min.z, max.z);
        return this;
    }

    public String toStringWithCommaDelimiter() {
        Vec3 v = new Vec3(this.x, this.y, this.z);
        v.x = Math.round(v.x * 1000) / 1000;
        v.y = Math.round(v.y * 1000) / 1000;
        v.z = Math.round(v.z * 1000) / 1000;
        return "(" + v.x + "," + v.y + "," + v.z + ")";
    }

    // Static functions...
    public static Vec3 add(Vec3 v1, Vec3 v2) {
        Vec3 ret = new Vec3(v1.x, v1.y, v1.z);
        return ret.add(v2);
    }

    public static Vec3 subtract(Vec3 v1, Vec3 v2) {
        Vec3 ret = new Vec3(v1.x, v1.y, v1.z);
        return ret.subtract(v2);
    }

    public static Vec3 multiply(Vec3 v1, double s) {
        Vec3 ret = new Vec3(v1.x, v1.y, v1.z);
        return ret.multiply(s);
    }

    public static Vec3 multiply(double s, Vec3 v1) {
        Vec3 ret = new Vec3(v1.x, v1.y, v1.z);
        return ret.multiply(s);
    }

    public static Vec3 divide(Vec3 v1, double s) {
        Vec3 ret = new Vec3(v1.x, v1.y, v1.z);
        return ret.divide(s);
    }    

    public static Vec3 unit(Vec3 v1) {
        return v1.unit();
    }
    
    public static double length(Vec3 v) {
        return v.length();
    }

    public static double lengthSqr(Vec3 v) {
        return v.lengthSqr();
    }
    
    public static double distance(Vec3 v1, Vec3 v2) {
        return v1.distance(v2);
    }
    
    public static double distanceSqr(Vec3 v1, Vec3 v2) {
        return v1.distanceSqr(v2);
    }
    
    public static double dot(Vec3 v1, Vec3 v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static Vec3 cross(Vec3 v1, Vec3 v2) {
        return v1.cross(v2);
    }

    public static Vec3 max(Vec3 v1, Vec3 v2) {
        return new Vec3(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y), Math.max(v1.z, v2.z));
    }

    public static Vec3 min(Vec3 v1, Vec3 v2) {
        return new Vec3(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y), Math.min(v1.z, v2.z));
    }

    public static Vec3 clamp(Vec3 v, Vec3 min, Vec3 max) {
        return new Vec3(Util.clamp(v.x, min.x, max.x), Util.clamp(v.y, min.y, max.y), Util.clamp(v.z, min.z, max.z));
    }    
    
    public static Vec3 copy(Vec3 other) {
        return new Vec3(other.x, other.y, other.z);
    }
    public static Vec3 zero() {
        return new Vec3(0,0,0);
    }
    public static Vec3 right() {
        return new Vec3(1,0,0);
    }
    public static Vec3 left() { 
        return new Vec3(-1,0,0);
    }
    public static Vec3 up() { 
        return new Vec3(0,1,0);
    }
    public static Vec3 down() { 
        return new Vec3(0,-1,0);
    }
    public static Vec3 forward() {
        // OpenGl is right handed. We are y-up (so, negative z points forward)
        return new Vec3(0,0,-1);
    }
    public static Vec3 backward() {
        // OpenGl is right handed. We are y-up (so, positive z points backwards)
        return new Vec3(0,0,1);
    }

    public static Vec3 colorToVec3(Color color) {   // vec is normalized values (0 to 1)
        double invScalar = 1.0 / 255.0;
        return new Vec3(color.getRed() * invScalar, color.getGreen() * invScalar, color.getBlue() * invScalar);
    }
    public static Color vec3ToColor(Vec3 v) {       // vec is normalized values (0 to 1)
        return new Color(Math.min((int)(v.x * 255.0), 255), Math.min((int)(v.y * 255.0), 255), Math.min((int)(v.z * 255.0), 255));
    }

    public String toString() {
        return toStringWithCommaDelimiter();
    }
}
