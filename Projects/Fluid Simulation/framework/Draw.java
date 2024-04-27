package framework;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.AffineTransform;

public class Draw {
    // Enums...
    public enum FontSize {
        XXSMALL,
        XSMALL,
        SMALL,
        MEDIUM,
        LARGE,
        XLARGE
    }

    // Static member variables...
    private static Font fontDefault;
    private static Font fonts[] = { null, null, null, null, null, null };
    private static AffineTransform baseTransform = new AffineTransform();

    // Functions...
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
        if (fontDefault != null) {
            return;
        }
        fontDefault = g.getFont();
        fonts[FontSize.XXSMALL.ordinal()] = fontDefault.deriveFont(12f);
        fonts[FontSize.XSMALL.ordinal()] = fontDefault.deriveFont(18f);
        fonts[FontSize.SMALL.ordinal()] = fontDefault.deriveFont(24f);
        fonts[FontSize.MEDIUM.ordinal()] = fontDefault.deriveFont(28f);
        fonts[FontSize.LARGE.ordinal()] = fontDefault.deriveFont(34f);
        fonts[FontSize.XLARGE.ordinal()] = fontDefault.deriveFont(42f);
    }
    public static FontSize fontSizeFromScale(double scale) {
        scale *= World.get().getPixelsPerUnit() / 42;
        if (scale <= 0.5) {
            return FontSize.XXSMALL;
        }
        else if (scale <= 0.7) {
            return FontSize.XSMALL;
        }
        else if (scale <= 0.85) {
            return FontSize.SMALL;
        }
        else if (scale <= 1.15) {
            return FontSize.MEDIUM;
        }
        else if (scale <= 1.5) {
            return FontSize.LARGE;
        }
        return FontSize.XLARGE;
    }
    public static void drawRect(Graphics2D g, Vec2 pos, Vec2 halfDims, double scale, Color colorFill, Color colorStroke, double strokeWidth, double roundedRadius) {
        // Setup transform...
        Vec2 drawPosPixels = Util.toPixels(pos);
        AffineTransform transform = AffineTransform.getTranslateInstance(drawPosPixels.x * Draw.getBaseTransformScale(), drawPosPixels.y * Draw.getBaseTransformScale());
        transform.scale(Draw.getBaseTransformScale(), Draw.getBaseTransformScale());

        // Draw it..
        drawRect(g, transform, halfDims, scale, colorFill, colorStroke, strokeWidth, roundedRadius, Vec2.zero());
    }
    public static void drawRect(Graphics2D g, AffineTransform transform, Vec2 halfDims, double scale, Color colorFill, Color colorStroke, double strokeWidth, double roundedRadius, Vec2 geoOffset) {
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
    public static void drawTextCentered(Graphics2D g, String text, Vec2 pos, double scale, FontSize fontSize, Color color) {
        drawTextCentered(g, text, pos, scale, fontSize, color, null);
    }
    public static void drawTextCentered(Graphics2D g, String text, Vec2 pos, double scale, FontSize fontSize, Color color, Color dropShadowColor) {
        // Basics...
        Vec2 drawPosPixels = Util.toPixels(pos);
        AffineTransform transform = AffineTransform.getTranslateInstance(drawPosPixels.x * Draw.getBaseTransformScale(), drawPosPixels.y * Draw.getBaseTransformScale());
        transform.scale(Draw.getBaseTransformScale() * scale, Draw.getBaseTransformScale() * scale);
        g.setTransform(transform);
        g.setFont(fonts[fontSize.ordinal()]);

        // Width...
        FontMetrics fontMetrics = g.getFontMetrics(fonts[fontSize.ordinal()]);
		int textWidth = fontMetrics.stringWidth(text);

        // Maybe a shadow...
        if (dropShadowColor != null) {
            g.setColor(dropShadowColor);
		    g.drawString(text, -textWidth/2 + 1, (fontMetrics.getAscent() - fontMetrics.getDescent()) / 2 + 1);
        }

        // And draw it...
        g.setColor(color);
		g.drawString(text, -textWidth/2, (fontMetrics.getAscent() - fontMetrics.getDescent()) / 2);
    }
}