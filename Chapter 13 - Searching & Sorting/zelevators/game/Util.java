package game;

import java.awt.Color;

public class Util {
    // Math utility functions...
    public static double clamp(double x, double min, double max) {
	    return Math.max(min, Math.min(x, max));
    }
    public static double lerp(double a, double b, double t) {
        return (a + (b - a)* t);
    }
    public static double sinDeg(double x) {
	    return Math.sin(Math.toRadians(x));
    }
    public static double cosDeg(double x) {
        return Math.cos(Math.toRadians(x));
    }
    public static double tanDeg(double x) {
        return Math.tan(Math.toRadians(x));
    }
    public static double asinDeg(double x) {
        return Math.toDegrees(Math.asin(x));
    }
    public static double acosDeg(double x) {
        return Math.toDegrees(Math.acos(x));
    }
    public static double atanDeg(double x) {
        return Math.toDegrees(Math.atan(x));
    }
    public static double atan2Deg(double y, double x) {
        double angle = Math.toDegrees(Math.atan2(y, x));
        return (angle < 0) ? angle + 360 : angle;
    }
    public static double minAngleToAngleDelta(double a1, double a2) {
        double d1 = a2 - a1;
        double d2 = a2 - (a1 + 360);
        double d3 = a2 - (a1 - 360);
        if (Math.abs(d1) < Math.abs(d2)) {
            return (Math.abs(d1) < Math.abs(d3)) ? d1 : d3;
        }
        return (Math.abs(d2) < Math.abs(d3)) ? d2 : d3;
    }

    // Misc utils...
    public static double randRange(double minInclusive, double maxExclusive) {
        return minInclusive + Math.random() * (maxExclusive - minInclusive);
    }
    public static int randRangeInt(int minInclusive, int maxInclusive) {
        double randDouble = randRange(minInclusive, maxInclusive + 1);
        return (int)randDouble;
    }
    public static String toIntStringCeil(double value) {
        int valueInt = (int)Math.ceil(value);
        return String.valueOf(valueInt);
    }
    public static String toIntStringFloor(double value) {
        int valueInt = (int)Math.floor(value);
        return String.valueOf(valueInt);
    }

    // Coordinate frame helpers...
    public static double toCoordFrameX(int playerIdx, double posX) {
        posX = (posX - World.get(playerIdx).getOrigin().x) / World.get(playerIdx).getPixelsPerUnit();
        return posX;
    }
    public static double toCoordFrameY(int playerIdx, double posY) {
        posY = -(posY - World.get(playerIdx).getOrigin().y) / World.get(playerIdx).getPixelsPerUnit();
        return posY;
    }
    public static Vec2 toCoordFrame(int playerIdx, Vec2 pos) {
        Vec2 origin = World.get(playerIdx).getOrigin();
        return new Vec2((pos.x-origin.x) / World.get(playerIdx).getPixelsPerUnit(), -(pos.y-origin.y) / World.get(playerIdx).getPixelsPerUnit());
    }
    public static double toCoordFrameLength(int playerIdx, double len) {
        return len / World.get(playerIdx).getPixelsPerUnit();
    }
    public static double toPixelsX(int playerIdx, double posX) {
        Vec2 origin = World.get(playerIdx).getOrigin();
        posX = posX * World.get(playerIdx).getPixelsPerUnit() + origin.x;
        return posX;
    }
    public static double toPixelsY(int playerIdx, double posY) {
        Vec2 origin = World.get(playerIdx).getOrigin();
        posY = -posY * World.get(playerIdx).getPixelsPerUnit() + origin.y;
        return posY;
    }
    public static int toPixelsXInt(int playerIdx, double posX) {
        Vec2 origin = World.get(playerIdx).getOrigin();
        posX = posX * World.get(playerIdx).getPixelsPerUnit() + origin.x;
        return (int)Math.round(posX);
    }
    public static int toPixelsYInt(int playerIdx, double posY) {
        Vec2 origin = World.get(playerIdx).getOrigin();
        posY = -posY * World.get(playerIdx).getPixelsPerUnit() + origin.y;
        return (int)Math.round(posY);
    }    
    public static Vec2 toPixels(int playerIdx, Vec2 pos) {
        Vec2 origin = World.get(playerIdx).getOrigin();
        return new Vec2(pos.x * World.get(playerIdx).getPixelsPerUnit() + origin.x, -pos.y * World.get(playerIdx).getPixelsPerUnit() + origin.y);
    }
    public static Vec2 toPixelDims(int playerIdx, Vec2 dims) {
        return new Vec2(dims.x * World.get(playerIdx).getPixelsPerUnit(), dims.y * World.get(playerIdx).getPixelsPerUnit());
    }
    public static double toPixelsLength(int playerIdx, double len) {
        return len * World.get(playerIdx).getPixelsPerUnit();
    }
    public static int toPixelsLengthInt(int playerIdx, double len) {
        return (int)Math.round(len * World.get(playerIdx).getPixelsPerUnit());
    }
    public static Vec2 clampToClosestHalfUnit(int playerIdx, Vec2 point) {
        return new Vec2(Math.round(point.x - 0.5) + 0.5, Math.round(point.y - 0.5) + 0.5);
    }
    public static Vec2 maxCoordFrameUnits(int playerIdx) {
        return new Vec2(World.get(playerIdx).getCanvasSize().x / World.get(playerIdx).getPixelsPerUnit(), World.get(playerIdx).getCanvasSize().y / World.get(playerIdx).getPixelsPerUnit());
    }
    public static Vec2 maxHalfUnitsInField(int playerIdx) {
        Vec2 maxHalfUnits = maxCoordFrameUnits(playerIdx);
        maxHalfUnits = clampToClosestHalfUnit(playerIdx, maxHalfUnits);
        if (!isInsideField(playerIdx, maxHalfUnits, 0.2)) {
            maxHalfUnits.subtract(new Vec2(1, 1));
        }
        return maxHalfUnits;
    }

    // Color utils...
    public static Color colorLerp(Color colorA, Color colorB, double t) {
        return colorLerp(colorA, colorB, (float)t);
    }
    public static Color colorLerp(Color colorA, Color colorB, float t) {
        if (t <= 0) {
            return colorA;
        }
        else if (t >= 1) {
            return colorB;
        }
        return new Color(
            (int)lerp((float)colorA.getRed(), (float)colorB.getRed(), t),
            (int)lerp((float)colorA.getGreen(), (float)colorB.getGreen(), t),
            (int)lerp((float)colorA.getBlue(), (float)colorB.getBlue(), t));
    }

    // Override the log...
    public static void log(String str) {
        System.out.println(str);
    }

    // Intersection utils...
    public static boolean circlesIntersect(Vec2 p1, double r1, Vec2 p2, double r2) {
        double dstSqr = Vec2.distanceSqr(p1, p2);
        double radSum = r1 + r2;
        return (dstSqr < radSum * radSum);
    }
    public static boolean circleContainsPoint(Vec2 circlePos, double circleRad, Vec2 point) {
        double dstSqr = Vec2.distanceSqr(circlePos, point);
        return (dstSqr < circleRad * circleRad);
    }
    public static boolean isInsideField(int playerIdx, Vec2 point) {
        return isInsideField(playerIdx, point, 0);
    }
    public static boolean isInsideField(int playerIdx, Vec2 point, double buffer) {
        if ((point.x <= buffer) || (point.y <= buffer)) {
            return false;
        }
        if ((point.x >= toCoordFrameLength(playerIdx, World.get(playerIdx).getCanvasSize().x) - buffer) || (point.y >= toCoordFrameLength(playerIdx, World.get(playerIdx).getCanvasSize().y) - buffer)) {
            return false;
        }
        return true;
    }
    public static Vec2 intersectSegments(Vec2 seg0start, Vec2 seg0dir, Vec2 seg1start, Vec2 seg1dir) {
        double s = (-seg0dir.y * (seg0start.x - seg1start.x) + seg0dir.x * (seg0start.y - seg1start.y)) / (-seg1dir.x * seg0dir.y + seg0dir.x * seg1dir.y);
        double t = ( seg1dir.x * (seg0start.y - seg1start.y) - seg1dir.y * (seg0start.x - seg1start.x)) / (-seg1dir.x * seg0dir.y + seg0dir.x * seg1dir.y);
        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            // Collision detected, return the point of intersection
            return new Vec2(seg0start.x + (t * seg0dir.x), seg0start.y + (t * seg0dir.y));
        }
        return null;
    }
    public static boolean intersectCircleSegment(Vec2 circleCenter, double circleRadius, Vec2 segStart, Vec2 segDir) {
        double dst = distancePointToSegment(circleCenter, segStart, segDir);
        return (dst < circleRadius);
    }
    public static boolean intersectCircleCapsule(Vec2 circleCenter, double circleRadius, Vec2 capSegStart, Vec2 capSegDir, double capRadius) {
        double dst = distancePointToSegment(circleCenter, capSegStart, capSegDir);
        return (dst < circleRadius + capRadius);
    }

    // Distances (returns null if no intersection, vec2 with intersection otherwise)...
    public static double distancePointToLine(Vec2 point, Vec2 linePoint, Vec2 lineDirUnit) {
        Vec2 v = Vec2.subtract(point, linePoint);
        Vec2 u = Vec2.subtract(v, Vec2.multiply(lineDirUnit, Vec2.dot(v, lineDirUnit)));
        return u.length();
    }
    public static double distancePointToSegment(Vec2 point, Vec2 segStart, Vec2 segDir) {
        Vec2 v = Vec2.subtract(point, segStart);
        double segDirLen = segDir.length();
        Vec2 segDirUnit = Vec2.divide(segDir, segDirLen);
        Vec2 closestPt = Vec2.add(segStart, Vec2.multiply(segDirUnit, Util.clamp(Vec2.dot(v, segDirUnit), 0, segDirLen)));
        return Vec2.distance(point, closestPt);
    }
    public static double distancePointToPlane(Vec2 point, Vec2 linePoint, Vec2 lineNormalUnit) {
        Vec2 v = Vec2.subtract(point, linePoint);
        return Vec2.dot(v, lineNormalUnit);
    }
}