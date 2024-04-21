package framework;

public class Mat4x4 {
    public double m[][] = new double[4][4];

    public Mat4x4(double m00, double m01, double m02, double m03,
                  double m10, double m11, double m12, double m13,
                  double m20, double m21, double m22, double m23,
                  double m30, double m31, double m32, double m33) {
        this.m[0][0] = m00; this.m[0][1] = m01; this.m[0][2] = m02; this.m[0][3] = m03;
        this.m[1][0] = m10; this.m[1][1] = m11; this.m[1][2] = m12; this.m[1][3] = m13;
        this.m[2][0] = m20; this.m[2][1] = m21; this.m[2][2] = m22; this.m[2][3] = m23;
        this.m[3][0] = m30; this.m[3][1] = m31; this.m[3][2] = m32; this.m[3][3] = m33;
    }
    public Mat4x4(Mat3x3 m) {
        this.set(m.m[0][0], m.m[0][1], m.m[0][2], 0.0,
                 m.m[1][0], m.m[1][1], m.m[1][2], 0.0,
                 m.m[2][0], m.m[2][1], m.m[2][2], 0.0,
                 0.0,     0.0,   0.0, 1.0);
    }

    public void set(double m00, double m01, double m02, double m03,
                    double m10, double m11, double m12, double m13,
                    double m20, double m21, double m22, double m23,
                    double m30, double m31, double m32, double m33) {
        this.m[0][0] = m00; this.m[0][1] = m01; this.m[0][2] = m02; this.m[0][3] = m03;
        this.m[1][0] = m10; this.m[1][1] = m11; this.m[1][2] = m12; this.m[1][3] = m13;
        this.m[2][0] = m20; this.m[2][1] = m21; this.m[2][2] = m22; this.m[2][3] = m23;
        this.m[3][0] = m30; this.m[3][1] = m31; this.m[3][2] = m32; this.m[3][3] = m33;
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

    public Mat4x4 add(Mat4x4 other) {
        this.m[0][0] += other.m[0][0];
        this.m[0][1] += other.m[0][1];
        this.m[0][2] += other.m[0][2];
        this.m[0][3] += other.m[0][3];
        this.m[1][0] += other.m[1][0];
        this.m[1][1] += other.m[1][1];
        this.m[1][2] += other.m[1][2];
        this.m[1][3] += other.m[1][3];
        this.m[2][0] += other.m[2][0];
        this.m[2][1] += other.m[2][1];
        this.m[2][2] += other.m[2][2];
        this.m[2][3] += other.m[2][3];
        this.m[3][0] += other.m[3][0];
        this.m[3][1] += other.m[3][1];
        this.m[3][2] += other.m[3][2];
        this.m[3][3] += other.m[3][3];
        return this;
    }

    public Mat4x4 subtract(Mat4x4 other) {
        this.m[0][0] -= other.m[0][0];
        this.m[0][1] -= other.m[0][1];
        this.m[0][2] -= other.m[0][2];
        this.m[0][3] -= other.m[0][3];
        this.m[1][0] -= other.m[1][0];
        this.m[1][1] -= other.m[1][1];
        this.m[1][2] -= other.m[1][2];
        this.m[1][3] -= other.m[1][3];
        this.m[2][0] -= other.m[2][0];
        this.m[2][1] -= other.m[2][1];
        this.m[2][2] -= other.m[2][2];
        this.m[2][3] -= other.m[2][3];
        this.m[3][0] -= other.m[3][0];
        this.m[3][1] -= other.m[3][1];
        this.m[3][2] -= other.m[3][2];
        this.m[3][3] -= other.m[3][3];
        return this;
    }
    
    public Mat4x4 multiply(Mat4x4 other) {         // other:mat4x4...
        this.set(
            this.m[0][0] * other.m[0][0] + this.m[0][1] * other.m[1][0] + this.m[0][2] * other.m[2][0] + this.m[0][3] * other.m[3][0], 
                this.m[0][0] * other.m[0][1] + this.m[0][1] * other.m[1][1] + this.m[0][2] * other.m[2][1] + this.m[0][3] * other.m[3][1],
                this.m[0][0] * other.m[0][2] + this.m[0][1] * other.m[1][2] + this.m[0][2] * other.m[2][2] + this.m[0][3] * other.m[3][2],
                this.m[0][0] * other.m[0][3] + this.m[0][1] * other.m[1][3] + this.m[0][2] * other.m[2][3] + this.m[0][3] * other.m[3][3],
            this.m[1][0] * other.m[0][0] + this.m[1][1] * other.m[1][0] + this.m[1][2] * other.m[2][0] + this.m[1][3] * other.m[3][0], 
                this.m[1][0] * other.m[0][1] + this.m[1][1] * other.m[1][1] + this.m[1][2] * other.m[2][1] + this.m[1][3] * other.m[3][1],
                this.m[1][0] * other.m[0][2] + this.m[1][1] * other.m[1][2] + this.m[1][2] * other.m[2][2] + this.m[1][3] * other.m[3][2],
                this.m[1][0] * other.m[0][3] + this.m[1][1] * other.m[1][3] + this.m[1][2] * other.m[2][3] + this.m[1][3] * other.m[3][3],
            this.m[2][0] * other.m[0][0] + this.m[2][1] * other.m[1][0] + this.m[2][2] * other.m[2][0] + this.m[2][3] * other.m[3][0], 
                this.m[2][0] * other.m[0][1] + this.m[2][1] * other.m[1][1] + this.m[2][2] * other.m[2][1] + this.m[2][3] * other.m[3][1],
                this.m[2][0] * other.m[0][2] + this.m[2][1] * other.m[1][2] + this.m[2][2] * other.m[2][2] + this.m[2][3] * other.m[3][2],
                this.m[2][0] * other.m[0][3] + this.m[2][1] * other.m[1][3] + this.m[2][2] * other.m[2][3] + this.m[2][3] * other.m[3][3],
            this.m[3][0] * other.m[0][0] + this.m[3][1] * other.m[1][0] + this.m[3][2] * other.m[2][0] + this.m[3][3] * other.m[3][0], 
                this.m[3][0] * other.m[0][1] + this.m[3][1] * other.m[1][1] + this.m[3][2] * other.m[2][1] + this.m[3][3] * other.m[3][1],
                this.m[3][0] * other.m[0][2] + this.m[3][1] * other.m[1][2] + this.m[3][2] * other.m[2][2] + this.m[3][3] * other.m[3][2],
                this.m[3][0] * other.m[0][3] + this.m[3][1] * other.m[1][3] + this.m[3][2] * other.m[2][3] + this.m[3][3] * other.m[3][3] );
        return this;
    }
    
    public Vec3 transform(Vec3 pt) {
        return new Vec3(
            this.m[0][0] * pt.x + this.m[0][1] * pt.y + this.m[0][2] * pt.z, 
            this.m[1][0] * pt.x + this.m[1][1] * pt.y + this.m[1][2] * pt.z, 
            this.m[2][0] * pt.x + this.m[2][1] * pt.y + this.m[2][2] * pt.z);
    }
    public Vec4 transform(Vec4 pt) {
        return new Vec4(
            this.m[0][0] * pt.x + this.m[0][1] * pt.y + this.m[0][2] * pt.z + this.m[0][3] * pt.w, 
            this.m[1][0] * pt.x + this.m[1][1] * pt.y + this.m[1][2] * pt.z + this.m[1][3] * pt.w, 
            this.m[2][0] * pt.x + this.m[2][1] * pt.y + this.m[2][2] * pt.z + this.m[2][3] * pt.w,
            this.m[3][0] * pt.x + this.m[3][1] * pt.y + this.m[3][2] * pt.z + this.m[3][3] * pt.w );
    }
    
    public Vec3 transformPoint(Vec3 pt) {
        Vec4 ret = this.transform(new Vec4(pt));
        ret.divide(ret.w);
        return new Vec3(ret.x, ret.y, ret.z);
    }
    public Vec3 transformPoint(Vec4 pt) {
        Vec4 ret = this.transform(pt);
        ret.divide(ret.w);
        return new Vec3(ret.x, ret.y, ret.z);
    }
    
    public Vec3 rotateVector(Vec3 v) {
        return new Vec3(
            this.m[0][0] * v.x + this.m[0][1] * v.y + this.m[0][2] * v.z, 
            this.m[1][0] * v.x + this.m[1][1] * v.y + this.m[1][2] * v.z, 
            this.m[2][0] * v.x + this.m[2][1] * v.y + this.m[2][2] * v.z );
    }
    
    public Mat4x4 scale(double scalar) {
        for (int r = 0; r < m.length; r++) {
            for (int c = 0; c < m[r].length; c++) {
                this.m[r][c] *= scalar;
            }
        }
        return this;
    }

    public double det() {
        Mat3x3 m3x3a = new Mat3x3(this.m[1][1], this.m[1][2], this.m[1][3],  this.m[2][1], this.m[2][2], this.m[2][3],  this.m[3][1], this.m[3][2], this.m[3][3]);
        Mat3x3 m3x3b = new Mat3x3(this.m[1][0], this.m[1][2], this.m[1][3],  this.m[2][0], this.m[2][2], this.m[2][3],  this.m[3][0], this.m[3][2], this.m[3][3]);
        Mat3x3 m3x3c = new Mat3x3(this.m[1][0], this.m[1][1], this.m[1][3],  this.m[2][0], this.m[2][1], this.m[2][3],  this.m[3][0], this.m[3][1], this.m[3][3]);
        Mat3x3 m3x3d = new Mat3x3(this.m[1][0], this.m[1][1], this.m[1][2],  this.m[2][0], this.m[2][1], this.m[2][2],  this.m[3][0], this.m[3][1], this.m[3][2]);
        return this.m[0][0] * m3x3a.det() - this.m[0][1] * m3x3b.det() + this.m[0][2] * m3x3c.det() - this.m[0][3] * m3x3d.det();
    }

    public Mat4x4 transpose() {
        this.set(this.m[0][0], this.m[1][0], this.m[2][0], this.m[3][0],
                 this.m[0][1], this.m[1][1], this.m[2][1], this.m[3][1],
                 this.m[0][2], this.m[1][2], this.m[2][2], this.m[3][2],
                 this.m[0][3], this.m[1][3], this.m[2][3], this.m[3][3] );
        return this;
    }
    
    // public Mat4x4 inverse() { // note: this has never been tested to be tested...
    //    double det = this.det();
    //    this.set((this.m[1][1] * this.m[2][2] - this.m[2][1] * this.m[1][2]), (this.m[0][2] * this.m[2][1] - this.m[2][2] * this.m[0][1]), (this.m[0][1] * this.m[1][2] - this.m[1][1] * this.m[0][2]),
    //             (this.m[1][2] * this.m[2][0] - this.m[2][2] * this.m[1][0]), (this.m[0][0] * this.m[2][2] - this.m[2][0] * this.m[0][2]), (this.m[0][2] * this.m[1][0] - this.m[1][2] * this.m[0][0]),
    //             (this.m[1][0] * this.m[2][1] - this.m[2][0] * this.m[1][1]), (this.m[0][1] * this.m[2][0] - this.m[2][1] * this.m[0][0]), (this.m[0][0] * this.m[1][1] - this.m[1][0] * this.m[0][1]));
    //    return this.scale(det);
    // }
    
    public String toString() {
        Mat4x4 m = Mat4x4.copy(this);
        m.m[0][0] = Math.round(m.m[0][0] * 1000) / 1000;
        m.m[0][1] = Math.round(m.m[0][1] * 1000) / 1000;
        m.m[0][2] = Math.round(m.m[0][2] * 1000) / 1000;
        m.m[0][3] = Math.round(m.m[0][3] * 1000) / 1000;
        m.m[1][0] = Math.round(m.m[1][0] * 1000) / 1000;
        m.m[1][1] = Math.round(m.m[1][1] * 1000) / 1000;
        m.m[1][2] = Math.round(m.m[1][2] * 1000) / 1000;
        m.m[1][3] = Math.round(m.m[1][3] * 1000) / 1000;
        m.m[2][0] = Math.round(m.m[2][0] * 1000) / 1000;
        m.m[2][1] = Math.round(m.m[2][1] * 1000) / 1000;
        m.m[2][2] = Math.round(m.m[2][2] * 1000) / 1000;
        m.m[2][3] = Math.round(m.m[2][3] * 1000) / 1000;
        m.m[3][0] = Math.round(m.m[3][0] * 1000) / 1000;
        m.m[3][1] = Math.round(m.m[3][1] * 1000) / 1000;
        m.m[3][2] = Math.round(m.m[3][2] * 1000) / 1000;
        m.m[3][3] = Math.round(m.m[3][3] * 1000) / 1000;
        return "(" + m.m[0][0] + " " + m.m[0][1] + " " + m.m[0][2] + " " + m.m[0][3] + ") (" + + m.m[1][0] + " " + m.m[1][1] + " " + m.m[1][2] + " " + m.m[1][3] + ") (" + m.m[2][0] + " " + m.m[2][1] + " " + m.m[2][2] + " " + m.m[2][3] + ") (" + m.m[3][0] + " " + m.m[3][1] + " " + m.m[3][2] + " " + m.m[3][3] + ")";
    }

    // Static functions...
    static Mat4x4 add(Mat4x4 m1, Mat4x4 m2) {
        Mat4x4 ret = Mat4x4.copy(m1);
        return ret.add(m2);
    }
    static Mat4x4 subtract(Mat4x4 m1, Mat4x4 m2) {
        Mat4x4 ret = Mat4x4.copy(m1);
        return ret.subtract(m2);
    }
    static Mat4x4 multiply(Mat4x4 m1, Mat4x4 m2) {
        Mat4x4 ret = Mat4x4.copy(m1);
        return ret.multiply(m2);
    }
    static Mat4x4 scale(Mat4x4 m1, double s) {
        Mat4x4 ret = Mat4x4.copy(m1);
        return ret.scale(s);
    }
    static double det(Mat4x4 m) {
        return m.det();
    }
    
    static Mat4x4 copy(Mat4x4 other) {
        Mat4x4 ret = new Mat4x4(other.m[0][0], other.m[0][1], other.m[0][2], other.m[0][3], other.m[1][0], other.m[1][1], other.m[1][2], other.m[1][3], other.m[2][0], other.m[2][1], other.m[2][2], other.m[2][3], other.m[3][0], other.m[3][1], other.m[3][2], other.m[3][3]);
        return ret;
    }
    static Mat4x4 zero() {
        return new Mat4x4(0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0);
    }
    static Mat4x4 identity() {
        return new Mat4x4(1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1);
    }

    static Mat4x4 transRotX(double angle) { // Angle is in degrees
        return new Mat4x4( Mat3x3.transRotX(angle) );
    }
    static Mat4x4 transRotY(double angle) { // Angle is in degrees
        return new Mat4x4( Mat3x3.transRotY(angle) );
    }
    static Mat4x4 transRotZ(double angle) { // Angle is in degrees
        return new Mat4x4( Mat3x3.transRotZ(angle) );
    }
    static Mat4x4 transFromBasis(Vec3 basX, Vec3 basY, Vec3 basZ) {
        return new Mat4x4(basX.x, basY.x, basZ.x, 0,
                          basX.y, basY.y, basZ.y, 0,
                          basX.z, basY.z, basZ.z, 0,
                           0,  0,  0, 1);
    }
    static Mat4x4 transRotAxis(Vec3 axis, double angle) { // Rotation about specified axis, angle is in degrees
        return new Mat4x4( Mat3x3.transRotAxis(axis, angle) );
    }
    static Mat4x4 transTrans(Vec3 offset) {
        return new Mat4x4(1, 0, 0, offset.x, 0, 1, 0, offset.y, 0, 0, 1, offset.z, 0, 0, 0, 1);
    }
    static Mat4x4 transProj() {
        return transProj(90);
    }
    static Mat4x4 transProj(double fovDeg) {
        return transProj(fovDeg, 0.1, 1000);
    }
    static Mat4x4 transProj(double fovDeg, double clipNear, double clipFar) { // FOV is full FOV in degrees
        double fovS = 1 / Util.tanDeg( fovDeg / 2 );
        return new Mat4x4(fovS,0,0,0, 0,fovS,0,0, 0,0,-clipFar/(clipFar-clipNear),-1, 0,0,-(clipFar*clipNear)/(clipFar-clipNear),0);
    }
}