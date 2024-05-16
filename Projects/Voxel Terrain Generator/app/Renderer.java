package app;

import framework.RendererBase;
import framework.VoxelVolume;
import graphics.Color;
import graphics.Mesh;
import graphics.Texture;
import graphics.Vertex;
import math.Vec3;

/* The renderer is responsible for building the render data (Mesh(s))
 *   from the provided VoxelVolume object. */
public class Renderer extends RendererBase {
    // Consts
    public static final double BLOCK_COLOR_RAND_FACTOR = 0.5;
    public final static int VOXEL_CELL_COUNT_XZ = 128;
    public final static int VOXEL_CELL_COUNT_Y  = 32;
    public final static Vec3 LIGHTING_SUN_DIRECTION = (new Vec3(-1.0, -2.0, -0.5)).unit();
    public final static Vec3 LIGHTING_SUN_COLOR = new Vec3(1.0, 0.9, 0.8);
    public final static Vec3 LIGHTING_AMBIENT_RGB = new Vec3(0.2, 0.22, 0.3);

    // Singleton
    public static synchronized Renderer get() {
        return (Renderer)RendererBase.get();
    }

    // data
    private Texture textureWhite;
    private Mesh terrainMesh;

    // methods
    public boolean init() {
        if (!super.init()) {
            return false;
        }

        // create any resources we will need
        textureWhite = Texture.loadTexture("res/white.png");

        return true;
    }
    public void dispose() {
        // release any resources we have created
        textureWhite.delete();

        // parent
        super.dispose();
    }

    public void createMeshFromVoxelVolume(VoxelVolume voxelVolume) {
        // free up any existing mesh that already exists (otherwise create one up)
        if (terrainMesh != null) {
            terrainMesh.cleanUp();
        }
        else {
            terrainMesh = new Mesh();
        }

        // Go through the voxel volume, looking for border cells & create up quads for them
        for (int z = 1; z < voxelVolume.getSizeZ() - 1; z++) {
            for (int x = 1; x < voxelVolume.getSizeX() - 1; x++) {
                for (int y = 1; y < voxelVolume.getSizeY() - 1; y++) {
                    // check each of the eight directions/cell-faces and maybe create quads
                    if (voxelVolume.isCellFilled(x, y, z) && !voxelVolume.isCellFilled(x - 1, y, z)) {
                        addQuadToMesh( voxelVolume, voxelVolume.getCellFaceCenter(x, y, z, VoxelVolume.CellEdge.NegX),
                                       Vec3.left(), Vec3.up(), 1);
                    }
                    if (voxelVolume.isCellFilled(x, y, z) && !voxelVolume.isCellFilled(x + 1, y, z)) {
                        addQuadToMesh( voxelVolume, voxelVolume.getCellFaceCenter(x, y, z, VoxelVolume.CellEdge.PosX),
                                       Vec3.right(), Vec3.up(), 1.0);
                    }
                    if (voxelVolume.isCellFilled(x, y, z) && !voxelVolume.isCellFilled(x, y - 1, z)) {
                        addQuadToMesh( voxelVolume, voxelVolume.getCellFaceCenter(x, y, z, VoxelVolume.CellEdge.NegY),
                                       Vec3.down(), Vec3.right(), 1.0);
                    }
                    if (voxelVolume.isCellFilled(x, y, z) && !voxelVolume.isCellFilled(x, y + 1, z)) {
                        addQuadToMesh( voxelVolume, voxelVolume.getCellFaceCenter(x, y, z, VoxelVolume.CellEdge.PosY),
                                       Vec3.up(), Vec3.right(), 1.0 );
                    }
                    if (voxelVolume.isCellFilled(x, y, z) && !voxelVolume.isCellFilled(x, y, z - 1)) {
                        addQuadToMesh( voxelVolume, voxelVolume.getCellFaceCenter(x, y, z, VoxelVolume.CellEdge.NegZ),
                                       Vec3.backward(), Vec3.up(), -1.0 );
                    }
                    if (voxelVolume.isCellFilled(x, y, z) && !voxelVolume.isCellFilled(x, y, z + 1)) {
                        addQuadToMesh( voxelVolume, voxelVolume.getCellFaceCenter(x, y, z, VoxelVolume.CellEdge.PosZ),
                                       Vec3.forward(), Vec3.up(), -1.0 );
                    }
                }
            }
        }
    }

    private double calcAmbientOcclusionForVertex(VoxelVolume voxelVolume, Vec3 vertPos, Vec3 quadCenter, Vec3 quadNormal, Vec3[] cardinalDirectionVectors) {
        double clearCells = 0;
        for (int i = 0; i < cardinalDirectionVectors.length; i++) {
            // only consider front facing cells
            if (cardinalDirectionVectors[i].dot(quadNormal) <= 0.0) {
                continue;
            }
            Vec3 testPos = Vec3.add(vertPos, cardinalDirectionVectors[i]);
            clearCells += voxelVolume.isCellFilled( (int)Math.round(testPos.x), (int)Math.round(testPos.y), (int)Math.round(testPos.z) ) ? 0.0 : 1.0;
        }
        double ambOcc = clearCells / 4;     // there will always be 4 of the 8 in the front face
        return ambOcc * ambOcc;             // curve it a bit
    }

    private void addQuadToMesh(VoxelVolume voxelVolume, Vec3 center, Vec3 normal, Vec3 up, double rightSign) {
        // length from a unit cell's center to vertex, sqrt( 0.5^2 * 3 )...used for ambient occlusion tests
        final double offsetLength = 0.866;

        // lighting (note that we'll want to add some ambient occlusion, just basic for now)
        double dotSun = Math.max( -normal.dot(LIGHTING_SUN_DIRECTION), 0.0 );
        Vec3 ambientLight = Vec3.multiply(LIGHTING_AMBIENT_RGB, (1 - BLOCK_COLOR_RAND_FACTOR) + BLOCK_COLOR_RAND_FACTOR * Math.random());
        Vec3 lightColor = Vec3.add( ambientLight, Vec3.multiply(LIGHTING_SUN_COLOR, dotSun) );

        // Color the up facing one green...also add a bit of randomness just for variety
        Vec3 colorDiffuse = (normal.y > 0.9) ? new Vec3(98 / 255.0, 200 / 255.0, 25 / 255.0) : new Vec3(210 / 255.0, 180 / 255.0, 130 / 255.0);

        // Calc the four corners
        Vec3 right = Vec3.multiply(up.cross(normal), rightSign);
        Vec3 UL = Vec3.add( Vec3.add(Vec3.multiply(up, 0.5), Vec3.multiply(right, -0.5)),  center);
        Vec3 UR = Vec3.add( Vec3.add(Vec3.multiply(up, 0.5), Vec3.multiply(right, 0.5)), center);
        Vec3 LR = Vec3.add( Vec3.add(Vec3.multiply(up,  -0.5), Vec3.multiply(right, 0.5)), center);
        Vec3 LL = Vec3.add( Vec3.add(Vec3.multiply(up,  -0.5), Vec3.multiply(right, -0.5)),  center);

        // calc ambient occlusion at corners/verts & use that to light the verts (test the eight cardinal diag directions for occlusion)
        Vec3[] cardinalDirectionVectors = { 
            Vec3.multiply( Vec3.add(      Vec3.subtract(UL, center).unit(), normal ).unit(), offsetLength ), 
            Vec3.multiply( Vec3.add(      Vec3.subtract(UR, center).unit(), normal ).unit(), offsetLength ), 
            Vec3.multiply( Vec3.add(      Vec3.subtract(LR, center).unit(), normal ).unit(), offsetLength ), 
            Vec3.multiply( Vec3.add(      Vec3.subtract(LL, center).unit(), normal ).unit(), offsetLength ),
            Vec3.multiply( Vec3.subtract( Vec3.subtract(UL, center).unit(), normal ).unit(), offsetLength ), 
            Vec3.multiply( Vec3.subtract( Vec3.subtract(UR, center).unit(), normal ).unit(), offsetLength ), 
            Vec3.multiply( Vec3.subtract( Vec3.subtract(LR, center).unit(), normal ).unit(), offsetLength ), 
            Vec3.multiply( Vec3.subtract( Vec3.subtract(LL, center).unit(), normal ).unit(), offsetLength ) };
        Color colorUL = new Color( (new Vec3(lightColor.x * colorDiffuse.x, lightColor.y * colorDiffuse.y, lightColor.z * colorDiffuse.z)).multiply( calcAmbientOcclusionForVertex(voxelVolume, UL, center, normal, cardinalDirectionVectors) ) );
        Color colorUR = new Color( (new Vec3(lightColor.x * colorDiffuse.x, lightColor.y * colorDiffuse.y, lightColor.z * colorDiffuse.z)).multiply( calcAmbientOcclusionForVertex(voxelVolume, UR, center, normal, cardinalDirectionVectors) ) );
        Color colorLR = new Color( (new Vec3(lightColor.x * colorDiffuse.x, lightColor.y * colorDiffuse.y, lightColor.z * colorDiffuse.z)).multiply( calcAmbientOcclusionForVertex(voxelVolume, LR, center, normal, cardinalDirectionVectors) ) );
        Color colorLL = new Color( (new Vec3(lightColor.x * colorDiffuse.x, lightColor.y * colorDiffuse.y, lightColor.z * colorDiffuse.z)).multiply( calcAmbientOcclusionForVertex(voxelVolume, LL, center, normal, cardinalDirectionVectors) ) );

        // verts
        Vertex v0 = new Vertex( UL, colorUL );
        Vertex v1 = new Vertex( UR, colorUR );
        Vertex v2 = new Vertex( LR, colorLR );
        Vertex v3 = new Vertex( LL, colorLL );
        
        // Add the two tris for the quad
        terrainMesh.addVert(v0); terrainMesh.addVert(v1); terrainMesh.addVert(v2); 
        terrainMesh.addVert(v0); terrainMesh.addVert(v2); terrainMesh.addVert(v3); 
    }

    public void renderWorld() {
        // Terrain mesh
        textureWhite.bind();
        if (terrainMesh != null) {
            terrainMesh.render();
        }

        // Super
        super.renderWorld();
    }
}
