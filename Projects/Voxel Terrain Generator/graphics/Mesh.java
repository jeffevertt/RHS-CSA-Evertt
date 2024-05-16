package graphics;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import org.lwjgl.system.MemoryUtil;

import framework.CameraBase;
import framework.Util;
import framework.WorldBase;
import math.Mat4x4;

public class Mesh {
    // Consts
    private final int DEFAULT_FLOAT_CAPACITY = 4096;    // number of floats it will store, by default (can grow)
    private final int FLOATS_PER_VERTEX = 3 + 4 + 2;    // pos (3 floats), colorRGBA (4 floats), uv (2 floats)

    // Instance data
    private FloatBuffer vertices = null;
    private VertexArrayObject vao = null;
    private VertexBufferObject vbo = null;
    private ShaderProgram shaderProgram = null;
    private boolean vertDataNeedsUpdate = true;
    private int vertexCount = 0;

    // Static data
    private static float[] singleVertexTempBuffer = null;

    // Public methods
    public Mesh() {
        // VAO and VBO create
        vao = new VertexArrayObject();
        vbo = new VertexBufferObject();

        // Default up our memory buffer for our vertices
        vertices = MemoryUtil.memAllocFloat(DEFAULT_FLOAT_CAPACITY);

        // Make sure we've alloced our temp buffer(s)
        if (singleVertexTempBuffer == null) {
            singleVertexTempBuffer = new float[FLOATS_PER_VERTEX];
        }
    }

    // Add a single vertex
    public boolean addVert(Vertex vert) {
        if (!maybeAllocMoreVertexSpace(1)) {
            return false;
        }

        // Copy to temp buffer
        singleVertexTempBuffer[0] = (float)vert.pos.x;
        singleVertexTempBuffer[1] = (float)vert.pos.y;
        singleVertexTempBuffer[2] = (float)vert.pos.z;
        singleVertexTempBuffer[3] = vert.color.getRed();
        singleVertexTempBuffer[4] = vert.color.getGreen();
        singleVertexTempBuffer[5] = vert.color.getBlue();
        singleVertexTempBuffer[6] = vert.color.getAlpha();
        singleVertexTempBuffer[7] = (float)vert.uv.x;
        singleVertexTempBuffer[8] = (float)vert.uv.y;

        // Copy in the new data
        vertices.position(vertexCount * FLOATS_PER_VERTEX);
        vertices.put(singleVertexTempBuffer, 0, FLOATS_PER_VERTEX);
        vertexCount += 1;

        // Needs update
        vertDataNeedsUpdate = true;

        return true;
    }

    // setVerts - Note that vertData must match the standard format we're using 
    //  in this app: pos (3 floats), colorRGBA (4 floats), uv (2 floats)
    public boolean addVerts(float[] vertData) {
        // setup
        if (vertData.length % FLOATS_PER_VERTEX != 0) {
            // incorrect stride (check the vertex format specified above)
            return false;
        }
        int vertexCountToAdd = vertData.length / FLOATS_PER_VERTEX;
        if (!maybeAllocMoreVertexSpace(vertexCountToAdd)) {
            return false;
        }

        // Copy in the new data
        vertices.position(vertexCount * FLOATS_PER_VERTEX);
        vertices.put(vertData, 0, vertData.length);
        vertexCount += vertexCountToAdd;

        // Needs update
        vertDataNeedsUpdate = true;

        return true;
    }

    private boolean maybeAllocMoreVertexSpace(int vertexCountToAdd) {
        // setup
        int floatsToAdd = vertexCountToAdd * FLOATS_PER_VERTEX;

        // Check for space in vertices & reserve it if not already there.
        int remainingSpaceFloatCount = vertices.capacity() - vertexCount * FLOATS_PER_VERTEX;
        if (remainingSpaceFloatCount < floatsToAdd) {
            int newBufferMinSize = vertices.capacity() + floatsToAdd;
            int blockAllocSize = DEFAULT_FLOAT_CAPACITY;
            FloatBuffer newVertices = MemoryUtil.memAllocFloat((newBufferMinSize / blockAllocSize + 1) * blockAllocSize);
            vertices.rewind();
            newVertices.put(vertices);
            MemoryUtil.memFree(vertices);
            vertices = newVertices;
        }
        
        return true;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    private boolean setupShaderProgram() {
        // Check if its already done
        if (shaderProgram != null) {
            return true;
        }

        // VAO and VBO bind
        vao.bind();
        vbo.bind(GL_ARRAY_BUFFER);        

        // Shaders
        Shader vertexShader, fragmentShader;
        if (Util.isDefaultContext()) {
            vertexShader = Shader.loadShader(GL_VERTEX_SHADER, "res/worldDefault.vert");
            fragmentShader = Shader.loadShader(GL_FRAGMENT_SHADER, "res/worldDefault.frag");
        } else {
            // no backup (we we need a legacy version?)
            return false;
        }

        // Create the shader program
        shaderProgram = new ShaderProgram();
        shaderProgram.attachShader(vertexShader);
        shaderProgram.attachShader(fragmentShader);
        if (Util.isDefaultContext()) {
            shaderProgram.bindFragmentDataLocation(0, "fragColor");
        }
        shaderProgram.link();
        if (glGetError() != GL_NO_ERROR) System.out.println("glError: " + glGetError());
        shaderProgram.use();

        // clean up (they are referenced by the shader program, so drop our references)
        vertexShader.delete();
        fragmentShader.delete();

        return true;
    }

    public void cleanUp() {
        // VAO and VBO free up
        if (vao != null) {
            vao.delete();
            vao = null;
        }
        if (vbo != null) {
            vbo.delete();
            vbo = null;
        }
        if (shaderProgram != null) {
            shaderProgram.delete();
            shaderProgram = null;
        }

        // Memory buffer
        if (vertices != null) {
            MemoryUtil.memFree(vertices);
            vertices = null;
        }

        // Reset
        vertexCount = 0;
        vertDataNeedsUpdate = true;
    }

    public void syncVertData() {
        if (!vertDataNeedsUpdate) {
            return;
        }

        // Set current location in float buffer back to start (prep for upload)
        vertices.rewind();

        // Upload vertex data
        vbo.bind(GL_ARRAY_BUFFER);
        if (glGetError() != GL_NO_ERROR) System.out.println("glError: " + glGetError());
        vbo.uploadData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        if (glGetError() != GL_NO_ERROR) System.out.println("glError: " + glGetError());

        // Mark as sync'ed
        vertDataNeedsUpdate = false;
    }

    private void updateVertexAttributes() {
        // pos
        int posAttrib = shaderProgram.getAttributeLocation("position");
        shaderProgram.enableVertexAttribute(posAttrib);
        shaderProgram.pointVertexAttribute(posAttrib, 3, FLOATS_PER_VERTEX * Float.BYTES, 0);
        if (glGetError() != GL_NO_ERROR) System.out.println("glError: " + glGetError());

        // color
        int colAttrib = shaderProgram.getAttributeLocation("color");
        shaderProgram.enableVertexAttribute(colAttrib);
        shaderProgram.pointVertexAttribute(colAttrib, 4, FLOATS_PER_VERTEX * Float.BYTES, 3 * Float.BYTES);
        if (glGetError() != GL_NO_ERROR) System.out.println("glError: " + glGetError());

        // uv
        int texAttrib = shaderProgram.getAttributeLocation("texcoord");
        shaderProgram.enableVertexAttribute(texAttrib);
        shaderProgram.pointVertexAttribute(texAttrib, 2, FLOATS_PER_VERTEX * Float.BYTES, 7 * Float.BYTES);
        if (glGetError() != GL_NO_ERROR) System.out.println("glError: " + glGetError());
    }
    
    private void updateShaderProgramParams() {
        // setup
        CameraBase camera = WorldBase.get().getCamera();
        if (camera == null) {
            return;
        }

        // texture
        int uniTex = shaderProgram.getUniformLocation("texImage");
        shaderProgram.setUniform(uniTex, 0);
        if (glGetError() != GL_NO_ERROR) System.out.println("glError: " + glGetError());

        // trans: model
        Mat4x4 model = Mat4x4.identity();
        int uniModel = shaderProgram.getUniformLocation("model");
        shaderProgram.setUniform(uniModel, model);

        // trans: view/camera
        Mat4x4 view = camera.getWorldToCamera();
        int uniView = shaderProgram.getUniformLocation("view");
        shaderProgram.setUniform(uniView, view);

        // trans: projection
        Mat4x4 projection = camera.getProjectionTransform();
        int uniProjection = shaderProgram.getUniformLocation("projection");
        shaderProgram.setUniform(uniProjection, projection);
        if (glGetError() != GL_NO_ERROR) System.out.println("glError: " + glGetError());
    }

    public void render() {
        // Shader program
        setupShaderProgram();

        // VAO and VBO bind
        vao.bind();
        vbo.bind(GL_ARRAY_BUFFER);
        shaderProgram.use();

        // Sync vert data
        syncVertData();

        // Setup shader program
        updateVertexAttributes();
        updateShaderProgramParams();

        // Draw
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        if (glGetError() != GL_NO_ERROR) System.out.println("glError: " + glGetError());
    }
}
