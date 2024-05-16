package framework;

import math.Vec2;

/* This class represents a 2D image map with Perlin noise inside of it.
 *  For an explanation of what Perlin Noise is and how it works, check
 *  out this page: https://rtouti.github.io/graphics/perlin-noise-algorithm 
 *  Alternatively, google or youtube video search or check out the original papers.
 * 
 * The map's size is width x length (world space), which is passed to the constructor. 
 *  The parameter heightScale scales the values stored in the map. The primary interface
 *  method is calcValueAtPoint which takes a 2D position in the map (world space).
 */
public class NoiseMap2D {
    private double width = -1;
    private double length = -1;
    private double heightScale = -1;
    private static int perm[] = null;

    public NoiseMap2D(double width, double length, double heightScale) {
        this.width = width;
        this.length = length;
        this.heightScale = heightScale;

        // Setup permutations (used in random calculations), done once for the class
        if (perm == null) {
            int[] perm256 = { 151, 160, 137, 91, 90, 15,
                131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23,
                190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33,
                88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166,
                77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244,
                102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196,
                135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123,
                5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
                223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9,
                129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228,
                251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107,
                49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254,
                138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180 };
            perm = new int[512];
            for (int i = 0; i < 256; i++) {
                perm[i] = perm256[i];
                perm[256 + i] = perm[i];
            }
        }
    }

    public double calcValueAtPoint(Vec2 pt) {
        // normalize the pt
        double ptx = pt.x / width;
        double pty = pt.y / length;

        int xi = (int) Math.floor(ptx) & 255;
        int yi = (int) Math.floor(pty) & 255;
        int g1 = perm[perm[xi] + yi];
        int g2 = perm[perm[xi + 1] + yi];
        int g3 = perm[perm[xi] + yi + 1];
        int g4 = perm[perm[xi + 1] + yi + 1];

        double xf = ptx - Math.floor(ptx);
        double yf = pty - Math.floor(pty);

        double d1 = grad(g1, xf, yf);
        double d2 = grad(g2, xf - 1, yf);
        double d3 = grad(g3, xf, yf - 1);
        double d4 = grad(g4, xf - 1, yf - 1);

        double u = easeZeroToOne(xf);
        double v = easeZeroToOne(yf);

        double x1Inter = Util.lerp(d1, d2, u);
        double x2Inter = Util.lerp(d3, d4, u);
        return Util.lerp(x1Inter, x2Inter, v) * heightScale;
    }

    private static double easeZeroToOne(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static double grad(int hash, double x, double y) {
        switch (hash & 3) {
            case 0:
                return x + y;
            case 1:
                return -x + y;
            case 2:
                return x - y;
            case 3:
                return -x - y;
            default:
                return 0;
        }
    }
}