package framework;

/*
 * The MIT License (MIT)
 *
 * Copyright © 2014-2015, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import math.Vec2;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

/**
 * This class represents a GLFW window.
 *
 * @author Heiko Brumme
 */
public class Window {
    // Singleton...
    private static Window instance = null;
    public static synchronized Window get() {
        return instance;
    }

    // Enums...
    protected enum ArrowKey {
        Left, Right, Down, Up
    }

    // Constants...
    public static final String WINDOW_TITLE = "CS-A: Voxel Terrain Generator";
    public static final int DEFAULT_WINDOW_WIDTH = 1024;
    public static final int DEFAULT_WINDOW_HEIGHT = 600;
    public static final boolean USE_VSYNC = true;

    /**
     * Stores the window handle.
     */
    private long id;

    /**
     * Key callback for the window.
     */
    private GLFWKeyCallback keyCallback;

    /**
     * Shows if vsync is enabled.
     */
    private boolean vsync;

    // keeps track of pressed state of arrow keys
    private boolean[] arrowKeyPressed = new boolean[4]; // order matches ArrowKey 

    /**
     * Creates a GLFW window and its OpenGL context with the specified width,
     * height and title.
     */
    public Window() {
        // Instance...
        if (instance != null) {
            System.out.println("WARNING: Two windows created. Don't do that.");
        }
        instance = this;
    }

    public boolean init() {
        // save off locals
        this.vsync = USE_VSYNC;

        /* Creating a temporary window for getting the available OpenGL version */
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        long temp = glfwCreateWindow(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, WINDOW_TITLE, NULL, NULL);
        glfwMakeContextCurrent(temp);
        GL.createCapabilities();
        GLCapabilities caps = GL.getCapabilities();
        glfwDestroyWindow(temp);

        /* Reset and set window hints */
        glfwDefaultWindowHints();
        if (caps.OpenGL32) {
            /* Hints for OpenGL 3.2 core profile */
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        } else if (caps.OpenGL21) {
            /* Hints for legacy OpenGL 2.1 */
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        } else {
            throw new RuntimeException("Neither OpenGL 3.2 nor OpenGL 2.1 is supported, you may need to update the graphics driver");
        }
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        /* Create window with specified OpenGL context */
        id = glfwCreateWindow(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, WINDOW_TITLE, NULL, NULL);
        if (id == NULL) {
            glfwTerminate();
            return false;
        }

        /* Center window on screen */
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(id,
                         (vidmode.width() - DEFAULT_WINDOW_WIDTH) / 2,
                         (vidmode.height() - DEFAULT_WINDOW_HEIGHT) / 2 );

        // Hide cursor (mouse look)
        glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        /* Create OpenGL context */
        glfwMakeContextCurrent(id);
        GL.createCapabilities();

        /* Enable v-sync */
        if (vsync) {
            glfwSwapInterval(1);
        }

        /* Set key callback */
        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                    glfwSetWindowShouldClose(window, true);
                }

                // arrow keys
                if ((key == GLFW_KEY_LEFT) || (key == GLFW_KEY_A)) {
                    arrowKeyPressed[0] = (action == GLFW_PRESS) ? true : (action == GLFW_RELEASE) ? false : arrowKeyPressed[0]; // ArrowKey.Left
                }
                else if ((key == GLFW_KEY_RIGHT) || (key == GLFW_KEY_D)) {
                    arrowKeyPressed[1] = (action == GLFW_PRESS) ? true : (action == GLFW_RELEASE) ? false : arrowKeyPressed[1]; // ArrowKey.Right
                }
                else if ((key == GLFW_KEY_DOWN) || (key == GLFW_KEY_S)) {
                    arrowKeyPressed[2] = (action == GLFW_PRESS) ? true : (action == GLFW_RELEASE) ? false : arrowKeyPressed[2]; // ArrowKey.Down
                }
                else if ((key == GLFW_KEY_UP) || (key == GLFW_KEY_W)) {
                    arrowKeyPressed[3] = (action == GLFW_PRESS) ? true : (action == GLFW_RELEASE) ? false : arrowKeyPressed[3]; // ArrowKey.Up
                }
            }
        };
        glfwSetKeyCallback(id, keyCallback);

        return true;
    }

    /**
     * Returns if the window is closing.
     *
     * @return true if the window should close, else false
     */
    public boolean isClosing() {
        return glfwWindowShouldClose(id);
    }

    public boolean isArrowKeyPressed(ArrowKey arrowKey) {
        switch (arrowKey) {
            case Left  : return arrowKeyPressed[0];
            case Right : return arrowKeyPressed[1];
            case Down  : return arrowKeyPressed[2];
            case Up    : return arrowKeyPressed[3];
        }
        return false;
    }

    public Vec2 getMouseCursorPos() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(id, x , y);
        return new Vec2(x.get(), y.get());
    }

    public int getWidth() {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(id, w, h);
        return w.get(0);
    }

    public int getHeight() {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(id, w, h);
        return h.get(0);
    }

    public double getAspectWoverH() {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(id, w, h);
        int width = w.get(0);
        int height = h.get(0);
        return (double)width / (double)Math.max(height, 1);
    }

    /**
     * Sets the window title
     *
     * @param title New window title
     */
    public void setTitle(CharSequence title) {
        glfwSetWindowTitle(id, title);
    }

    /**
     * Updates the screen.
     */
    public void update() {
        glfwSwapBuffers(id);
        glfwPollEvents();
    }

    /**
     * Destroys the window an releases its callbacks.
     */
    public void destroy() {
        glfwDestroyWindow(id);
        keyCallback.free();
    }

    /**
     * Setter for v-sync.
     *
     * @param vsync Set to true to enable v-sync
     */
    public void setVSync(boolean vsync) {
        this.vsync = vsync;
        if (vsync) {
            glfwSwapInterval(1);
        } else {
            glfwSwapInterval(0);
        }
    }

    /**
     * Check if v-sync is enabled.
     *
     * @return true if v-sync is enabled
     */
    public boolean isVSyncEnabled() {
        return this.vsync;
    }

}
