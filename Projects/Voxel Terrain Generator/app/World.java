package app;

import framework.NoiseMapComposite;
import framework.VoxelVolume;
import framework.WorldBase;
import framework.NoiseMapComposite.CompositeMethod;
import framework.NoiseMapPerlin;
import math.Vec3;

/* The World object is responsible for creating the VoxelVolume. 
 *  Together with the Renderer, the world is created and rendered.
 *  Note that to create the voxelVolume, it uses other classes
 *  provided in framework (namely 2D NoiseMaps).
 */
public class World extends WorldBase {
    // Singleton...
    public static synchronized World get() {
        return (World)WorldBase.get();
    }

    // consts
    public final static int VOXEL_CELL_COUNT_XZ = 128;
    public final static int VOXEL_CELL_COUNT_Y  = 32;

    // data
    private VoxelVolume voxelVolume;

    // accessorworldMeshs
    public VoxelVolume getVoxelVolume() {
        return voxelVolume;
    }

    // constructor
    public World() {
        super();
    }

    public double findHeightAtPosXZ(int x, int z) {
        return (voxelVolume != null) ? voxelVolume.findHeightAtPosXZ(x, z) : 0;
    }

    public boolean createDefaultScene() {
        super.createDefaultScene();

        // create up a camera
        Camera camera = new Camera(new Vec3( VOXEL_CELL_COUNT_XZ / 2, 
                                             VOXEL_CELL_COUNT_Y / 2, 
                                             VOXEL_CELL_COUNT_XZ / 2 ), Vec3.forward());
        setCamera(camera);

        // create terrain from set of noise maps
        NoiseMapComposite terrainHeights = new NoiseMapComposite(CompositeMethod.Add);
        terrainHeights.addNoiseMapLayer(new NoiseMapPerlin(VOXEL_CELL_COUNT_XZ, VOXEL_CELL_COUNT_XZ, 0.1), 
                                        8.0);
        terrainHeights.addNoiseMapLayer(new NoiseMapPerlin(VOXEL_CELL_COUNT_XZ, VOXEL_CELL_COUNT_XZ, 0.5), 
                                        2.0);
        
        // create voxel volume from the noise maps
        voxelVolume = new VoxelVolume(VOXEL_CELL_COUNT_XZ, VOXEL_CELL_COUNT_Y, VOXEL_CELL_COUNT_XZ);
        voxelVolume.initFromNoiseMap(terrainHeights, VOXEL_CELL_COUNT_XZ);

        // renderer
        Renderer.get().createMeshFromVoxelVolume(World.get().getVoxelVolume());

        return true;
    }
}
