package framework;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Draw {
    // static member variables
    private static AffineTransform baseTransform = new AffineTransform();

    // methods...
    public static AffineTransform getBaseTransform() {
        return new AffineTransform(baseTransform);
    }
    public static double getBaseTransformScale() {
        return baseTransform.getScaleX();
    }
    public static void setBaseTransform(AffineTransform transform) {
        baseTransform = transform;
    }
    public static double getUIScale() {
        return (double)Window.get().getWidth() / Window.DEFAULT_WINDOW_WIDTH;
    }
    public static void beginRender(Graphics2D g) {
    }
    public static void drawRect(Graphics2D g, Vec2 pos, Vec2 halfDims) {
        drawRect(g, pos, halfDims, Draw.getUIScale(), Color.BLUE, Color.BLACK, 0.1, 0.2);
    }
    public static void drawRect(Graphics2D g, Vec2 pos, Vec2 halfDims, double scale, Color colorFill, Color colorStroke, double strokeWidth, double roundedRadius) {
        // Setup transform...
        Vec2 drawPos = pos;
        Vec2 drawPosPixels = Util.toPixels(drawPos);
        AffineTransform transform = AffineTransform.getTranslateInstance(drawPosPixels.x * Draw.getBaseTransformScale(), drawPosPixels.y * Draw.getBaseTransformScale());
        transform.scale(Draw.getBaseTransformScale(), Draw.getBaseTransformScale());

        // Draw it..
        drawRect(g, transform, halfDims, scale, colorFill, colorStroke, strokeWidth, roundedRadius, Vec2.zero());
    }
    private static void drawRect(Graphics2D g, AffineTransform transform, Vec2 halfDims, double scale, Color colorFill, Color colorStroke, double strokeWidth, double roundedRadius, Vec2 geoOffset) {
        // Transform...
        g.setTransform(transform);

        // Fill...
        g.setPaint(colorFill);
        Vec2 halfDimsPixels = Util.toPixelDims(Vec2.multiply(halfDims, scale));
        Vec2 geoOffsetPixels = Util.toPixelDims(Vec2.multiply(geoOffset, scale));
        int roundedPixels = Util.toPixelsLengthInt(roundedRadius * scale);
        g.fillRoundRect((int)(geoOffsetPixels.x-halfDimsPixels.x), (int)(geoOffsetPixels.y-halfDimsPixels.y), (int)(halfDimsPixels.x * 2), (int)(halfDimsPixels.y * 2), roundedPixels, roundedPixels);

        // Stroke...
        if (strokeWidth > 0) {
            g.setPaint(colorStroke);
            g.setStroke(new BasicStroke((float)Math.max(Util.toPixelsLength(strokeWidth), 1)));
            g.drawRoundRect((int)(geoOffsetPixels.x-halfDimsPixels.x), (int)(geoOffsetPixels.y-halfDimsPixels.y), (int)(halfDimsPixels.x * 2), (int)(halfDimsPixels.y * 2), roundedPixels, roundedPixels);
        }
    }
}