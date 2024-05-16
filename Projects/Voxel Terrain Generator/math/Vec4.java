package math;

import framework.Util;

public class Vec4 {
    public double x = 0, y = 0, z = 0, w = 0;

    public Vec4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public Vec4(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1.0;
    }
    public Vec4(Vec3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = 1.0;
    }

    public Vec4 add(Vec4 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        this.w += other.w;
        return this;
    }
    
    public Vec4 plus(Vec4 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        this.w += other.w;
        return this;
    }

    public Vec4 subtract(Vec4 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        this.w -= other.w;
        return this;
    }
    
    public Vec4 minus(Vec4 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        this.w -= other.w;
        return this;
    }

    public Vec4 multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        this.w *= scalar;
        return this;
    }
    
    public Vec4 times(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        this.w *= scalar;
        return this;
    }
    
    public Vec4 divide(double scalar) {
        this.x /= scalar;
        this.y /= scalar;
        this.z /= scalar;
        this.w /= scalar;
        return this;
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
    }

    public double lengthSqr() {
        return (this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
    }
    
    public Vec4 normalize() {
        double length = this.length();
        double invLen = (length == 0) ? 1.0 : (1.0 / this.length());
        this.x *= invLen;
        this.y *= invLen;
        this.z *= invLen;
        this.w *= invLen;
        return this;
    }

    public Vec4 unit() {
        Vec4 unitVec = new Vec4(this.x, this.y, this.z, this.w);
        unitVec.normalize();
        return unitVec;
    }

    public double distance(Vec4 other) {
        Vec4 me = new Vec4(this.x, this.y, this.z, this.w);
        return me.subtract(other).length();
    }

    public double distanceSqr(Vec4 other) {
        Vec4 me = new Vec4(this.x, this.y, this.z, this.w);
        return me.subtract(other).lengthSqr();
    }
    
    public double dot(Vec4 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
    }

    public Vec4 lerp(Vec4 v1, Vec4 v2, double t) {
        Vec4 delta = subtract(v2, v1);
        Vec4 ret = new Vec4(v1.x, v1.y, v1.z, v1.w);
        return ret.add(delta.multiply(t));
    }

    public Vec4 max(Vec4 other) {
        this.x = Math.max(this.x, other.x);
        this.y = Math.max(this.y, other.y);
        this.z = Math.max(this.z, other.z);
        this.w = Math.max(this.w, other.w);
        return this;
    }

    public Vec4 min(Vec4 other) {
        this.x = Math.min(this.x, other.x);
        this.y = Math.min(this.y, other.y);
        this.z = Math.min(this.z, other.z);
        this.w = Math.min(this.w, other.w);
        return this;
    }

    public Vec4 clamp(Vec4 min, Vec4 max) {
        this.x = Util.clamp(this.x, min.x, max.x);
        this.y = Util.clamp(this.y, min.y, max.y);
        this.z = Util.clamp(this.z, min.z, max.z);
        this.w = Util.clamp(this.w, min.w, max.w);
        return this;
    }

    public String toStringWithCommaDelimiter() {
        Vec4 v = new Vec4(this.x, this.y, this.z, this.w);
        v.x = Math.round(v.x * 1000) / 1000;
        v.y = Math.round(v.y * 1000) / 1000;
        v.z = Math.round(v.z * 1000) / 1000;
        v.w = Math.round(v.w * 1000) / 1000;
        return "(" + v.x + "," + v.y + "," + v.z + "," + v.w + ")";
    }

    // Static functions...
    public static Vec4 add(Vec4 v1, Vec4 v2) {
        Vec4 ret = new Vec4(v1.x, v1.y, v1.z, v1.w);
        return ret.add(v2);
    }

    public static Vec4 subtract(Vec4 v1, Vec4 v2) {
        Vec4 ret = new Vec4(v1.x, v1.y, v1.z, v1.w);
        return ret.subtract(v2);
    }

    public static Vec4 multiply(Vec4 v1, double s) {
        Vec4 ret = new Vec4(v1.x, v1.y, v1.z, v1.w);
        return ret.multiply(s);
    }

    public static Vec4 multiply(double s, Vec4 v1) {
        Vec4 ret = new Vec4(v1.x, v1.y, v1.z, v1.w);
        return ret.multiply(s);
    }

    public static Vec4 divide(Vec4 v1, double s) {
        Vec4 ret = new Vec4(v1.x, v1.y, v1.z, v1.w);
        return ret.divide(s);
    }    

    public static Vec4 unit(Vec4 v1) {
        return v1.unit();
    }
    
    public static double length(Vec4 v) {
        return v.length();
    }

    public static double lengthSqr(Vec4 v) {
        return v.lengthSqr();
    }
    
    public static double distance(Vec4 v1, Vec4 v2) {
        return v1.distance(v2);
    }
    
    public static double distanceSqr(Vec4 v1, Vec4 v2) {
        return v1.distanceSqr(v2);
    }
    
    public static double dot(Vec4 v1, Vec4 v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z + v1.w * v2.w;
    }

    public static Vec4 max(Vec4 v1, Vec4 v2) {
        return new Vec4(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y), Math.max(v1.z, v2.z), Math.max(v1.w, v2.w));
    }

    public static Vec4 min(Vec4 v1, Vec4 v2) {
        return new Vec4(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y), Math.min(v1.z, v2.z), Math.min(v1.w, v2.w));
    }

    public static Vec4 clamp(Vec4 v, Vec4 min, Vec4 max) {
        return new Vec4(Util.clamp(v.x, min.x, max.x), Util.clamp(v.y, min.y, max.y), Util.clamp(v.z, min.z, max.z), Util.clamp(v.w, min.w, max.w));
    }
    
    public static Vec4 copy(Vec4 other) {
        return new Vec4(other.x, other.y, other.z, other.w);
    }
    public static Vec4 zero() {
        return new Vec4(Vec3.zero());
    }
    public static Vec4 right() {
        return new Vec4(Vec3.right());
    }
    public static Vec4 left() { 
        return new Vec4(Vec3.left());
    }
    public static Vec4 up() { 
        return new Vec4(Vec3.up());
    }
    public static Vec4 down() { 
        return new Vec4(Vec3.down());
    }
    public static Vec4 forward() {
        return new Vec4(Vec3.forward());
    }
    public static Vec4 backward() {
        return new Vec4(Vec3.backward());
    }

    public String toString() {
        return toStringWithCommaDelimiter();
    }
}
