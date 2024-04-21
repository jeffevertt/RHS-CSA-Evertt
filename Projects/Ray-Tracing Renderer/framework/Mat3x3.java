package framework;

public class Mat3x3 {
    public double m[][] = new double[3][3];

    public Mat3x3(double m00, double m01, double m02,
                  double m10, double m11, double m12,
                  double m20, double m21, double m22) {
        this.m[0][0] = m00; this.m[0][1] = m01; this.m[0][2] = m02;
        this.m[1][0] = m10; this.m[1][1] = m11; this.m[1][2] = m12;
        this.m[2][0] = m20; this.m[2][1] = m21; this.m[2][2] = m22;
    }

    public void set(double m00, double m01, double m02,
                    double m10, double m11, double m12,
                    double m20, double m21, double m22) {
        this.m[0][0] = m00; this.m[0][1] = m01; this.m[0][2] = m02;
        this.m[1][0] = m10; this.m[1][1] = m11; this.m[1][2] = m12;
        this.m[2][0] = m20; this.m[2][1] = m21; this.m[2][2] = m22;
    }
    
    public Vec3 basisX() {
        return new Vec3(this.m[0][0], this.m[1][0], this.m[2][0]);
    }
    
    public Vec3 basisY() {
        return new Vec3(this.m[0][1], this.m[1][1], this.m[2][1]);
    }
    
    public Vec3 basisZ() {
        return new Vec3(this.m[0][2], this.m[1][2], this.m[2][2]);
    }

    public Mat3x3 add(Mat3x3 other) {
        this.m[0][0] += other.m[0][0];
        this.m[0][1] += other.m[0][1];
        this.m[0][2] += other.m[0][2];
        this.m[1][0] += other.m[1][0];
        this.m[1][1] += other.m[1][1];
        this.m[1][2] += other.m[1][2];
        this.m[2][0] += other.m[2][0];
        this.m[2][1] += other.m[2][1];
        this.m[2][2] += other.m[2][2];
        return this;
    }

    public Mat3x3 subtract(Mat3x3 other) {
        this.m[0][0] -= other.m[0][0];
        this.m[0][1] -= other.m[0][1];
        this.m[0][2] -= other.m[0][2];
        this.m[1][0] -= other.m[1][0];
        this.m[1][1] -= other.m[1][1];
        this.m[1][2] -= other.m[1][2];
        this.m[2][0] -= other.m[2][0];
        this.m[2][1] -= other.m[2][1];
        this.m[2][2] -= other.m[2][2];
        return this;
    }
    
    public Mat3x3 multiply(Mat3x3 other) {
        this.set(
            this.m[0][0] * other.m[0][0] + this.m[0][1] * other.m[1][0] + this.m[0][2] * other.m[2][0], 
                this.m[0][0] * other.m[0][1] + this.m[0][1] * other.m[1][1] + this.m[0][2] * other.m[2][1],
                this.m[0][0] * other.m[0][2] + this.m[0][1] * other.m[1][2] + this.m[0][2] * other.m[2][2],
            this.m[1][0] * other.m[0][0] + this.m[1][1] * other.m[1][0] + this.m[1][2] * other.m[2][0], 
                this.m[1][0] * other.m[0][1] + this.m[1][1] * other.m[1][1] + this.m[1][2] * other.m[2][1],
                this.m[1][0] * other.m[0][2] + this.m[1][1] * other.m[1][2] + this.m[1][2] * other.m[2][2],
            this.m[2][0] * other.m[0][0] + this.m[2][1] * other.m[1][0] + this.m[2][2] * other.m[2][0], 
                this.m[2][0] * other.m[0][1] + this.m[2][1] * other.m[1][1] + this.m[2][2] * other.m[2][1],
                this.m[2][0] * other.m[0][2] + this.m[2][1] * other.m[1][2] + this.m[2][2] * other.m[2][2]);
        return this;
    }
    
    public Vec3 transform(Vec3 pt) {
        return new Vec3(
            this.m[0][0] * pt.x + this.m[0][1] * pt.y + this.m[0][2] * pt.z, 
            this.m[1][0] * pt.x + this.m[1][1] * pt.y + this.m[1][2] * pt.z, 
            this.m[2][0] * pt.x + this.m[2][1] * pt.y + this.m[2][2] * pt.z);
    }
    
    public Mat3x3 scale(double scalar) {
        this.m[0][0] *= scalar;
        this.m[0][1] *= scalar;
        this.m[0][2] *= scalar;
        this.m[1][0] *= scalar;
        this.m[1][1] *= scalar;
        this.m[1][2] *= scalar;
        this.m[2][0] *= scalar;
        this.m[2][1] *= scalar;
        this.m[2][2] *= scalar;
        return this;
    }

    public double det() {
        double l = this.m[0][0] * (this.m[1][1] * this.m[2][2] - this.m[2][1] * this.m[1][2]);
        double m = this.m[0][1] * (this.m[1][0] * this.m[2][2] - this.m[2][0] * this.m[1][2]);
        double r = this.m[0][2] * (this.m[1][0] * this.m[2][1] - this.m[2][0] * this.m[1][1]);
        return (l - m + r);
    }

    public Mat3x3 transpose() {
        this.set(this.m[0][0], this.m[1][0], this.m[2][0],
                 this.m[0][1], this.m[1][1], this.m[2][1],
                 this.m[0][2], this.m[1][2], this.m[2][2]);
        return this;
    }
    
    public Mat3x3 inverse() {
        double det = this.det();
        this.set((this.m[1][1] * this.m[2][2] - this.m[2][1] * this.m[1][2]), (this.m[0][2] * this.m[2][1] - this.m[2][2] * this.m[0][1]), (this.m[0][1] * this.m[1][2] - this.m[1][1] * this.m[0][2]),
                 (this.m[1][2] * this.m[2][0] - this.m[2][2] * this.m[1][0]), (this.m[0][0] * this.m[2][2] - this.m[2][0] * this.m[0][2]), (this.m[0][2] * this.m[1][0] - this.m[1][2] * this.m[0][0]),
                 (this.m[1][0] * this.m[2][1] - this.m[2][0] * this.m[1][1]), (this.m[0][1] * this.m[2][0] - this.m[2][1] * this.m[0][0]), (this.m[0][0] * this.m[1][1] - this.m[1][0] * this.m[0][1]));
        return this.scale(det);
    }
    
    public String toString() {
        Mat3x3 m = Mat3x3.copy(this);
        m.m[0][0] = Math.round(m.m[0][0] * 1000) / 1000;
        m.m[0][1] = Math.round(m.m[0][1] * 1000) / 1000;
        m.m[0][2] = Math.round(m.m[0][2] * 1000) / 1000;
        m.m[1][0] = Math.round(m.m[1][0] * 1000) / 1000;
        m.m[1][1] = Math.round(m.m[1][1] * 1000) / 1000;
        m.m[1][2] = Math.round(m.m[1][2] * 1000) / 1000;
        m.m[2][0] = Math.round(m.m[2][0] * 1000) / 1000;
        m.m[2][1] = Math.round(m.m[2][1] * 1000) / 1000;
        m.m[2][2] = Math.round(m.m[2][2] * 1000) / 1000;
        return "(" + m.m[0][0] + " " + m.m[0][1] + " " + m.m[0][2] + ") (" + + m.m[1][0] + " " + m.m[1][1] + " " + m.m[1][2] + ") (" + m.m[2][0] + " " + m.m[2][1] + " " + m.m[2][2] + ")";
    }

    // Static functions...
    static Mat3x3 add(Mat3x3 m1, Mat3x3 m2) {
        Mat3x3 ret = Mat3x3.copy(m1);
        return ret.add(m2);
    }
    static Mat3x3 subtract(Mat3x3 m1, Mat3x3 m2) {
        Mat3x3 ret = Mat3x3.copy(m1);
        return ret.subtract(m2);
    }
    static Mat3x3 multiply(Mat3x3 m1, Mat3x3 m2) {
        Mat3x3 ret = Mat3x3.copy(m1);
        return ret.multiply(m2);
    }
    static Mat3x3 scale(Mat3x3 m1, double s) {
        Mat3x3 ret = Mat3x3.copy(m1);
        return ret.scale(s);
    }
    static double det(Mat3x3 m) {
        return m.det();
    }
    
    static Mat3x3 copy(Mat3x3 other) {
        return new Mat3x3(other.m[0][0], other.m[0][1], other.m[0][2], other.m[1][0], other.m[1][1], other.m[1][2], other.m[2][0], other.m[2][1], other.m[2][2]);
    }
    static Mat3x3 zero() {
        return new Mat3x3(0,0,0,0,0,0,0,0,0);
    }
    static Mat3x3 identity() {
        return new Mat3x3(1,0,0,0,1,0,0,0,1);
    }

    static Mat3x3 transRotX(double angle) { // Angle is in degrees
        return new Mat3x3(1, 0, 0, 0, Util.cosDeg(angle), -Util.sinDeg(angle), 0, Util.sinDeg(angle), Util.cosDeg(angle));
    }
    static Mat3x3 transRotY(double angle) { // Angle is in degrees
        return new Mat3x3(Util.cosDeg(angle), 0, Util.sinDeg(angle), 0, 1, 0, -Util.sinDeg(angle), 0, Util.cosDeg(angle));
    }
    static Mat3x3 transRotZ(double angle) { // Angle is in degrees
        return new Mat3x3(Util.cosDeg(angle), -Util.sinDeg(angle), 0, Util.sinDeg(angle), Util.cosDeg(angle), 0, 0, 0, 1);
    }
    static Mat3x3 transRotAxis(Vec3 axis, double angle) { // Rotation about specified axis, angle is in degrees
        axis.normalize();
        return new Mat3x3(Util.cosDeg(angle) + axis.x * axis.x * (1 - Util.cosDeg(angle)), axis.x * axis.y * (1 - Util.cosDeg(angle)) - axis.z * Util.sinDeg(angle), axis.x * axis.z * (1 - Util.cosDeg(angle)) + axis.y * Util.sinDeg(angle),
                          axis.y * axis.z * (1 - Util.cosDeg(angle)) + axis.z * Util.sinDeg(angle), Util.cosDeg(angle) + axis.y * axis.y * (1 - Util.cosDeg(angle)), axis.y * axis.z * (1 - Util.cosDeg(angle)) - axis.x * Util.sinDeg(angle),
                          axis.z * axis.x * (1 - Util.cosDeg(angle)) - axis.y * Util.sinDeg(angle), axis.z * axis.y * (1 - Util.cosDeg(angle)) + axis.x * Util.sinDeg(angle), Util.cosDeg(angle) + axis.z * axis.z * (1 - Util.cosDeg(angle)));
    }
}
