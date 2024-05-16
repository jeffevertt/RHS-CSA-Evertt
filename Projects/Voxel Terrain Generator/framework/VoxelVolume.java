package framework;

import math.Vec2;
import math.Vec3;

public class VoxelVolume {
    // enums
    public enum CellEdge {
        NegX, PosX,
        NegY, PosY,
        NegZ, PosZ
    }
    // private member data
    private boolean[][][] cells = null;

    /* VoxelVolume - Construct a volume with the specified cell count
     *  - XZ are the horizonal dimensions and Y is the vertical 
     *  - Each voxel indicates a cube with one unit sizes in all three
     *      dimensions. The x,y,z location indicates the center of the
     *      cube. */
    public VoxelVolume(int sizeX, int sizeY, int sizeZ) {
        cells = new boolean[sizeX][sizeY][sizeZ];
    }

    public int getSizeX() {
        return cells.length;
    }
    public int getSizeY() {
        return cells[0].length;
    }
    public int getSizeZ() {
        return cells[0][0].length;
    }

    public boolean isCellFilled(int x, int y, int z) {
        return cells[x][y][z];
    }

    public Vec3 getCellCenter(int x, int y, int z) {
        return new Vec3(x + 0.5, y + 0.5, z + 0.5);
    }
    public Vec3 getCellFaceCenter(int x, int y, int z, CellEdge cellEdge) {
        switch (cellEdge) {
            case NegX : return new Vec3(x - 0.5, y, z);
            case PosX : return new Vec3(x + 0.5, y, z);
            case NegY : return new Vec3(x, y - 0.5, z);
            case PosY : return new Vec3(x, y + 0.5, z);
            case NegZ : return new Vec3(x, y, z - 0.5);
            case PosZ : return new Vec3(x, y, z + 0.5);
        }
        // shouldn't ever get here
        return getCellCenter(x, y, z);
    }

    public void initFromNoiseMap(NoiseMap map, double scaleXZ) {
        initFromNoiseMap(map, scaleXZ, 1.0);
    }
    public void initFromNoiseMap(NoiseMap map, double scaleXZ, double scaleY) {
        for (int z = 0; z < cells[0][0].length; ++z) {
            for (int x = 0; x < cells.length; ++x) {
                // Calc the height at the cell
                Vec2 mapPt = new Vec2(((double)x / (cells.length - 1)) * scaleXZ,
                                      ((double)z / (cells[0][0].length - 1)) * scaleXZ);
                int height = (int)(map.calcValueAtPoint(mapPt) * cells[0].length * scaleY);
                for (int y = 0; y < cells[0].length; ++y) {
                    cells[x][y][z] = (y <= height) ? true : false;
                }
            }
        }
    }

    /* findHeightAtPosXZ returns the height of the first cell's top in the 
     *  vertical (i.e. Y) dimension. Return 0 if no cells in column are filled. */
    public double findHeightAtPosXZ(int x, int z) {
        // Bounds check
        if ((x < 0 || x >= cells.length) || 
            (z < 0 || z >= cells[0][0].length)) {
            return 0;
        }

        // Start at the top cell and march down till we hit something
        int y = getSizeY() - 1;
        while (y >= 0) {
            if (cells[x][y][z]) {
                return y + 0.5; // Offset up half a cell dim because we want the top surface
            }
            y--;
        }
        return 0;
    }
}
