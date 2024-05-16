package framework;

import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import graphics.Color;
import graphics.Font;
import graphics.Shader;
import graphics.ShaderProgram;
import graphics.Texture;
import graphics.VertexArrayObject;
import graphics.VertexBufferObject;
import math.Mat4x4;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

/* RenderBase
 *  We are right handed, y-up (negative z points forward) */
public class RendererBase {
    // Singletons...
    private static RendererBase instance = null;
    public static synchronized RendererBase get() {
        return instance;
    }

    private VertexArrayObject vao;
    private VertexBufferObject vbo;
    private ShaderProgram programUI;

    private FloatBuffer vertices;
    private int numVertices;
    private boolean drawing;

    private Font font;
    private Font debugFont;

    // Member functions (methods)...
    protected RendererBase() {
        // Instance...
        if (instance != null) {
            System.out.println("WARNING: Two renderers created. Don't do that.");
        }
        instance = this;
    }

    public boolean init() {
        // Text shader
        setupShaderProgram();

        // Blending
        glDisable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Z-buffer & cull mode
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDepthFunc(GL_LEQUAL);

        // fonts
        try {
            font = new Font(new FileInputStream("res/Inconsolata.ttf"), 16);
        } catch (FontFormatException | IOException ex) {
            font = new Font();
        }
        debugFont = new Font(12, false);

        return true;
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void begin() {
        if (drawing) {
            throw new IllegalStateException("Renderer is already drawing!");
        }
        drawing = true;
        numVertices = 0;
    }

    public void end() {
        if (!drawing) {
            throw new IllegalStateException("Renderer isn't drawing!");
        }
        drawing = false;
        flush();
    }

    public void flush() {
        if (numVertices > 0) {
            vertices.flip();

            if (vao != null) {
                vao.bind();
            } else {
                vbo.bind(GL_ARRAY_BUFFER);
                specifyVertexAttributes();
            }
            programUI.use();

            /* Upload the new vertex data */
            vbo.bind(GL_ARRAY_BUFFER);
            vbo.uploadSubData(GL_ARRAY_BUFFER, 0, vertices);

            /* Draw batch */
            glDrawArrays(GL_TRIANGLES, 0, numVertices);

            /* Clear vertex data for next batch */
            vertices.clear();
            numVertices = 0;
        }
    }
    
    public void renderWorld() {
        // ...

        // fps
        RendererBase renderer = RendererBase.get();
        renderer.drawDebugText("FPS: " + App.get().getFPS(), 5, 5);
        flush();
    }

    public int getTextWidth(CharSequence text) {
        return font.getWidth(text);
    }

    public int getTextHeight(CharSequence text) {
        return font.getHeight(text);
    }

    /**
     * Calculates total width of a debug text.
     *
     * @param text The text
     *
     * @return Total width of the text
     */
    public int getDebugTextWidth(CharSequence text) {
        return debugFont.getWidth(text);
    }

    /**
     * Calculates total height of a debug text.
     *
     * @param text The text
     *
     * @return Total width of the text
     */
    public int getDebugTextHeight(CharSequence text) {
        return debugFont.getHeight(text);
    }

    /**
     * Draw text at the specified position.
     *
     * @param text Text to draw
     * @param x    X coordinate of the text position
     * @param y    Y coordinate of the text position
     */
    public void drawText(CharSequence text, float x, float y) {
        font.drawText(this, text, x, y);
    }

    /**
     * Draw debug text at the specified position.
     *
     * @param text Text to draw
     * @param x    X coordinate of the text position
     * @param y    Y coordinate of the text position
     */
    public void drawDebugText(CharSequence text, float x, float y) {
        debugFont.drawText(this, text, x, y);
    }

    /**
     * Draw text at the specified position and color.
     *
     * @param text Text to draw
     * @param x    X coordinate of the text position
     * @param y    Y coordinate of the text position
     * @param c    Color to use
     */
    public void drawText(CharSequence text, float x, float y, Color c) {
        font.drawText(this, text, x, y, c);
    }

    /**
     * Draw debug text at the specified position and color.
     *
     * @param text Text to draw
     * @param x    X coordinate of the text position
     * @param y    Y coordinate of the text position
     * @param c    Color to use
     */
    public void drawDebugText(CharSequence text, float x, float y, Color c) {
        debugFont.drawText(this, text, x, y, c);
    }

    /**
     * Draws the currently bound texture on specified coordinates.
     *
     * @param texture Used for getting width and height of the texture
     * @param x       X position of the texture
     * @param y       Y position of the texture
     */
    public void drawTexture(Texture texture, float x, float y) {
        drawTexture(texture, x, y, Color.WHITE);
    }

    /**
     * Draws the currently bound texture on specified coordinates and with
     * specified color.
     *
     * @param texture Used for getting width and height of the texture
     * @param x       X position of the texture
     * @param y       Y position of the texture
     * @param c       The color to use
     */
    public void drawTexture(Texture texture, float x, float y, Color c) {
        /* Vertex positions */
        float x1 = x;
        float y1 = y;
        float x2 = x1 + texture.getWidth();
        float y2 = y1 + texture.getHeight();

        /* Texture coordinates */
        float u1 = 0f;
        float v1 = 0f;
        float u2 = 1f;
        float v2 = 1f;

        drawTextureRegion(x1, y1, x2, y2, u1, v1, u2, v2, c);
    }

    /**
     * Draws a texture region with the currently bound texture on specified
     * coordinates.
     *
     * @param texture   Used for getting width and height of the texture
     * @param x         X position of the texture
     * @param y         Y position of the texture
     * @param regX      X position of the texture region
     * @param regY      Y position of the texture region
     * @param regWidth  Width of the texture region
     * @param regHeight Height of the texture region
     */
    public void drawTextureRegion(Texture texture, float x, float y, float regX, float regY, float regWidth, float regHeight) {
        drawTextureRegion(texture, x, y, regX, regY, regWidth, regHeight, Color.WHITE);
    }

    /**
     * Draws a texture region with the currently bound texture on specified
     * coordinates.
     *
     * @param texture   Used for getting width and height of the texture
     * @param x         X position of the texture
     * @param y         Y position of the texture
     * @param regX      X position of the texture region
     * @param regY      Y position of the texture region
     * @param regWidth  Width of the texture region
     * @param regHeight Height of the texture region
     * @param c         The color to use
     */
    public void drawTextureRegion(Texture texture, float x, float y, float regX, float regY, float regWidth, float regHeight, Color c) {
        /* Vertex positions */
        float x1 = x;
        float y1 = y;
        float x2 = x + regWidth;
        float y2 = y + regHeight;

        /* Texture coordinates */
        float u1 = regX / texture.getWidth();
        float v1 = regY / texture.getHeight();
        float u2 = (regX + regWidth) / texture.getWidth();
        float v2 = (regY + regHeight) / texture.getHeight();

        drawTextureRegion(x1, y1, x2, y2, u1, v1, u2, v2, c);
    }

    /**
     * Draws a texture region with the currently bound texture on specified
     * coordinates.
     *
     * @param x1 Bottom left x position
     * @param y1 Bottom left y position
     * @param x2 Top right x position
     * @param y2 Top right y position
     * @param u1 Bottom left s coordinate
     * @param v1 Bottom left t coordinate
     * @param u2 Top right s coordinate
     * @param v2 Top right t coordinate
     */
    public void drawTextureRegion(float x1, float y1, float x2, float y2, float u1, float v1, float u2, float v2) {
        drawTextureRegion(x1, y1, x2, y2, u1, v1, u2, v2, Color.WHITE);
    }

    /**
     * Draws a texture region with the currently bound texture on specified
     * coordinates.
     *
     * @param x1 Bottom left x position
     * @param y1 Bottom left y position
     * @param x2 Top right x position
     * @param y2 Top right y position
     * @param u1 Bottom left s coordinate
     * @param v1 Bottom left t coordinate
     * @param u2 Top right s coordinate
     * @param v2 Top right t coordinate
     * @param c  The color to use
     */
    public void drawTextureRegion(float x1, float y1, float x2, float y2, float u1, float v1, float u2, float v2, Color c) {
        if (vertices.remaining() < 8 * 6) {
            /* We need more space in the buffer, so flush it */
            flush();
        }

        float r = c.getRed();
        float g = c.getGreen();
        float b = c.getBlue();
        float a = c.getAlpha();

        vertices.put(x1).put(y1).put(r).put(g).put(b).put(a).put(u1).put(v1);
        vertices.put(x2).put(y2).put(r).put(g).put(b).put(a).put(u2).put(v2);
        vertices.put(x1).put(y2).put(r).put(g).put(b).put(a).put(u1).put(v2);

        vertices.put(x1).put(y1).put(r).put(g).put(b).put(a).put(u1).put(v1);
        vertices.put(x2).put(y1).put(r).put(g).put(b).put(a).put(u2).put(v1);
        vertices.put(x2).put(y2).put(r).put(g).put(b).put(a).put(u2).put(v2);

        numVertices += 6;
    }

    /**
     * Dispose renderer and clean up its used data.
     */
    public void dispose() {
        MemoryUtil.memFree(vertices);

        if (vao != null) {
            vao.delete();
        }
        vbo.delete();
        programUI.delete();

        font.dispose();
        debugFont.dispose();
    }

    /** Setups the default shader program. */
    private void setupShaderProgram() {
        if (Util.isDefaultContext()) {
            /* Generate Vertex Array Object */
            vao = new VertexArrayObject();
            vao.bind();
        } else {
            vao = null;
        }

        /* Generate Vertex Buffer Object */
        vbo = new VertexBufferObject();
        vbo.bind(GL_ARRAY_BUFFER);

        /* Create FloatBuffer */
        vertices = MemoryUtil.memAllocFloat(4096);

        /* Upload null data to allocate storage for the VBO */
        long size = vertices.capacity() * Float.BYTES;
        vbo.uploadData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW);

        /* Initialize variables */
        numVertices = 0;
        drawing = false;

        /* Load shaders */
        Shader vertexShaderUI, fragmentShaderUI;
        if (Util.isDefaultContext()) {
            vertexShaderUI = Shader.loadShader(GL_VERTEX_SHADER, "res/ui.vert");
            fragmentShaderUI = Shader.loadShader(GL_FRAGMENT_SHADER, "res/ui.frag");
        } else {
            vertexShaderUI = Shader.loadShader(GL_VERTEX_SHADER, "res/uiLegacy.vert");
            fragmentShaderUI = Shader.loadShader(GL_FRAGMENT_SHADER, "res/uiLegacy.frag");
        }

        /* Create shader program */
        programUI = new ShaderProgram();
        programUI.attachShader(vertexShaderUI);
        programUI.attachShader(fragmentShaderUI);
        if (Util.isDefaultContext()) {
            programUI.bindFragmentDataLocation(0, "fragColor");
        }
        programUI.link();
        programUI.use();

        /* Delete linked shaders */
        vertexShaderUI.delete();
        fragmentShaderUI.delete();

        /* Get width and height of framebuffer */
        long window = GLFW.glfwGetCurrentContext();
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
            width = widthBuffer.get();
            height = heightBuffer.get();
        }

        /* Specify Vertex Pointers */
        specifyVertexAttributes();

        /* Set texture uniform */
        int uniTex = programUI.getUniformLocation("texImage");
        programUI.setUniform(uniTex, 0);

        /* Set model matrix to identity matrix */
        Mat4x4 model = Mat4x4.identity();
        int uniModel = programUI.getUniformLocation("model");
        programUI.setUniform(uniModel, model);

        /* Set view matrix to identity matrix */
        Mat4x4 view = Mat4x4.identity();
        int uniView = programUI.getUniformLocation("view");
        programUI.setUniform(uniView, view);

        /* Set projection matrix to an orthographic projection */
        Mat4x4 projection = Mat4x4.transOrthographic(0f, width, 0f, height, -1f, 1f);
        int uniProjection = programUI.getUniformLocation("projection");
        programUI.setUniform(uniProjection, projection);
    }

    /**
     * Specifies the vertex pointers.
     */
    private void specifyVertexAttributes() {
        /* Specify Vertex Pointer */
        int posAttrib = programUI.getAttributeLocation("position");
        programUI.enableVertexAttribute(posAttrib);
        programUI.pointVertexAttribute(posAttrib, 2, 8 * Float.BYTES, 0);

        /* Specify Color Pointer */
        int colAttrib = programUI.getAttributeLocation("color");
        programUI.enableVertexAttribute(colAttrib);
        programUI.pointVertexAttribute(colAttrib, 4, 8 * Float.BYTES, 2 * Float.BYTES);

        /* Specify Texture Pointer */
        int texAttrib = programUI.getAttributeLocation("texcoord");
        programUI.enableVertexAttribute(texAttrib);
        programUI.pointVertexAttribute(texAttrib, 2, 8 * Float.BYTES, 6 * Float.BYTES);
    }
}
