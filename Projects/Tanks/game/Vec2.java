package game;

public class Vec2 {
    public double x = 0, y = 0;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 other) {
		this.x += other.x;
		this.y += other.y;
        return this;
    }
	
	public Vec2 plus(Vec2 other) {
		this.x += other.x;
		this.y += other.y;
        return this;
    }

    public Vec2 subtract(Vec2 other) {
		this.x -= other.x;
		this.y -= other.y;
        return this;
    }
	
	public Vec2 minus(Vec2 other) {
		this.x -= other.x;
		this.y -= other.y;
        return this;
    }

    public Vec2 multiply(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
        return this;
    }
	
	public Vec2 times(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
        return this;
    }
	
    public Vec2 divide(double scalar) {
		this.x /= scalar;
		this.y /= scalar;
        return this;
    }

    public double length() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double lengthSqr() {
		return (this.x * this.x + this.y * this.y);
    }
	
    public Vec2 normalize() {
		double length = this.length();
		double invLen = (length == 0) ? 1.0 : (1.0 / length);
		this.x *= invLen;
		this.y *= invLen;
		return this;
    }

    public Vec2 unit() {
		Vec2 unitVec = new Vec2(this.x, this.y);
		unitVec.normalize();
		return unitVec;
    }

    public double angle() {
		return Math.toDegrees( Math.atan2(this.y, this.x) );
    }

    public double distance(Vec2 other) {
		Vec2 me = new Vec2(this.x, this.y);
		return me.subtract(other).length();
    }

    public double distanceSqr(Vec2 other) {
		Vec2 me = new Vec2(this.x, this.y);
		return me.subtract(other).lengthSqr();
    }
	
    public double dot(Vec2 other) {
		return this.x * other.x + this.y * other.y;
    }
	
	public Vec2 rotate(double angle) {
		angle = angle * (Math.PI/180);
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double x = this.x;
		double y = this.y;
		this.x = x * cos - y * sin;
		this.y = x * sin + y * cos;
		return this;
	}

	public Vec2 max(Vec2 other) {
		this.x = Math.max(this.x, other.x);
		this.y = Math.max(this.y, other.y);
		return this;
	}

	public Vec2 min(Vec2 other) {
		this.x = Math.min(this.x, other.x);
		this.y = Math.min(this.y, other.y);
		return this;
	}

	public Vec2 clamp(Vec2 min, Vec2 max) {
		this.x = Util.clamp(this.x, min.x, max.x);
		this.y = Util.clamp(this.y, min.y, max.y);
		return this;
	}

	public String toStringWithCommaDelimiter() {
		Vec2 v = new Vec2(this.x, this.y);
		v.x = Math.round(v.x * 1000) / 1000.0;
		v.y = Math.round(v.y * 1000) / 1000.0;
		return String.format("(%.2f, %.2f)", v.x, v.y);
	}

	// Static functions...
	public static Vec2 add(Vec2 v1, Vec2 v2) {
		Vec2 ret = new Vec2(v1.x, v1.y);
		return ret.add(v2);
	}

	public static Vec2 subtract(Vec2 v1, Vec2 v2) {
		Vec2 ret = new Vec2(v1.x, v1.y);
		return ret.subtract(v2);
	}

	public static Vec2 multiply(Vec2 v1, double s) {
		Vec2 ret = new Vec2(v1.x, v1.y);
		return ret.multiply(s);
	}

	public static Vec2 multiply(double s, Vec2 v1) {
		Vec2 ret = new Vec2(v1.x, v1.y);
		return ret.multiply(s);
	}

	public static Vec2 divide(Vec2 v1, double s) {
		Vec2 ret = new Vec2(v1.x, v1.y);
		return ret.divide(s);
	}	

	public static Vec2 unit(Vec2 v1) {
		return v1.unit();
	}
	
	public static double length(Vec2 v) {
		return v.length();
	}

	public static double lengthSqr(Vec2 v) {
		return v.lengthSqr();
	}
	
	public static double distance(Vec2 v1, Vec2 v2) {
		return v1.distance(v2);
	}
	
	public static double distanceSqr(Vec2 v1, Vec2 v2) {
		return v1.distanceSqr(v2);
	}
	
	public static double dot(Vec2 v1, Vec2 v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}
	
	public static Vec2 rotate(Vec2 v1, double angle) {
		Vec2 ret = new Vec2(v1.x, v1.y);
		return ret.rotate(angle);
	}

	public static Vec2 max(Vec2 v1, Vec2 v2) {
		return new Vec2(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y));
	}

	public static Vec2 min(Vec2 v1, Vec2 v2) {
		return new Vec2(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y));
	}

	public static Vec2 clamp(Vec2 v, Vec2 min, Vec2 max) {
		return new Vec2(Util.clamp(v.x, min.x, max.x), Util.clamp(v.y, min.y, max.y));
	}	
	
	public static Vec2 copy(Vec2 other) {
		return new Vec2(other.x, other.y);
	}
	public static Vec2 zero() {
		return new Vec2(0,0);
	}
	public static Vec2 right() {
		return new Vec2(1,0);
	}
	public static Vec2 left() { 
		return new Vec2(-1,0);
	}
	public static Vec2 up() { 
		return new Vec2(0,1);
	}
	public static Vec2 down() { 
		return new Vec2(0,-1);
	}

	public String toString() {
		return toStringWithCommaDelimiter();
	}
}
