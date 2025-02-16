import java.awt.Color;

import framework.AppBoard;

public class AppUtils {
    // conversion from texel/renderTarget (x,y) point to a complex coordinate plane (a, b), where the complex point is: a + bi
    protected static double texelCoordToComplexCoordinateX(int x) {
        return ((double)x / AppBoard.SUPERSAMPLE_TEXELS_PER_PIXEL_XY - AppBoard.get().getOrigin().x) / AppBoard.get().getPixelsPerUnit();
    }
    protected static double texelCoordToComplexCoordinateY(int y) {
        return -((double)y / AppBoard.SUPERSAMPLE_TEXELS_PER_PIXEL_XY - AppBoard.get().getOrigin().y) / AppBoard.get().getPixelsPerUnit();
    }

    // color palette support
    private static Color[] colorPalette = null;
    private static int[] rgba = new int[] { 0, 0, 0, 255 };
    private static void initColorPalette() {
        float hueMin = 0.0f;
        float hueMax = 0.4f;
        float brightnessMin = 0.5f;
        float brightnessMax = 5.0f;
        colorPalette = new Color[501];
        for (int i = 0; i < colorPalette.length - 1; i++) {
            float hue = (i / (float)colorPalette.length) * (hueMax - hueMin) + hueMin;
            float brightness = (float)(1.0f - Math.pow((double)(colorPalette.length - i) / colorPalette.length, 0.5)) * (brightnessMax - brightnessMin) + brightnessMin;
            colorPalette[i] = Color.getHSBColor(hue, 1.0f, brightness);
        }
        colorPalette[colorPalette.length - 1] = Color.BLACK;
    }
    public static int[] getColorPaletteValueRGBA(float zeroToOneValue) {
        if (colorPalette == null) {
            initColorPalette();
        }
        Color color = colorPalette[(int)(zeroToOneValue * (colorPalette.length - 1))];
        rgba[0] = (int)color.getRed();
        rgba[1] = (int)color.getGreen();
        rgba[2] = (int)color.getBlue();
        return rgba;
    }
}
