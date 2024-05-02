package framework;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import framework.FluidSimulationBase;

// Base class (interface) for student simulation
public abstract class FluidSimulationBase {
    // Constants
    private final int LIN_SOLVE_STEPS = 20;     // Gauss-Seidel solver, number of steps to run
    private final int FIELD_SIZE_X = 128 + 2;
    private final int FIELD_SIZE_Y = 64 + 2;
    private final double SCALAR_FIELD_DIFFUSION_FACTOR = 0.0001; // So, like the dye or smoke particles, rate of diffusion
    private final double VECTOR_FIELD_DIFFUSION_FACTOR = 0.001; // This is the velocity field (wind or fluid flow), rate of diffusion

    // Navier-Stokes equations, implementation in referenced paper
    //  https://en.wikipedia.org/wiki/Navier%E2%80%93Stokes_equations
    // YouTube video: https://www.youtube.com/watch?v=qsYE1wMEMPA 
    // Paper: http://graphics.cs.cmu.edu/nsp/course/15-464/Fall09/papers/StamFluidforGames.pdf 

    // Private member variables
    private double[][] densityField = new double[FIELD_SIZE_X][FIELD_SIZE_Y];
    private double[][] densityFieldPrev = new double[FIELD_SIZE_X][FIELD_SIZE_Y];
    private Vec2[][] velocityField = new Vec2[FIELD_SIZE_X][FIELD_SIZE_Y];
    private Vec2[][] velocityFieldPrev = new Vec2[FIELD_SIZE_X][FIELD_SIZE_Y];
    private double[][] tmpBuffer1 = new double[FIELD_SIZE_X][FIELD_SIZE_Y];
    private double[][] tmpBuffer2 = new double[FIELD_SIZE_X][FIELD_SIZE_Y];

    // Abstract methods
    public abstract void update(double deltaTime);                          // updates the simulation (both velocities & densities)
    public abstract void setSourcesForDensityField(double[][] densField);   // set the source/forces field for the dye/smoke in the density field
    public abstract void setSourcesForVelocityField(Vec2[][] velField);     // set the source/forces field for the velocity field
    public abstract void onKeyPressed(char keyCode);
    public abstract void onKeyReleased(char keyCode);
    public abstract void onMouseButtonPressed(int buttonId);
    public abstract void onMouseButtonReleased(int buttonId);

    // Accessors
    public double[][] getDensityField() {
        // this is a 2D array containing a scalar density value of the dye/smoke at each position
        return densityField;
    }
    public Vec2[][] getVelocityField() {
        //  this is a 2D array containing a Vec2 velocity value of the motion field at each position
        return velocityField;
    }
    public Vec2 getMousePosition_simRelative() {
        // returns the position in the coordinates of the density/velocity fields
        //  the x,y value returned is 0,0 in the lower-left and (densityField.length-1),(densityField[0].length-1) at the upper right
        World world = World.get();
        if (world != null) {
            // rescale to simulation size
            Vec2 pos = world.getMousePositionDrawRegionRelative();
            if (pos == null) {
                return null;
            }
            pos.x = (pos.x / world.getCanvasSize().x) * (double)FIELD_SIZE_X;
            pos.y = (pos.y / world.getCanvasSize().y) * (double)FIELD_SIZE_Y;
            return pos;
        }
        return null;
    }

    // Constructor
    public FluidSimulationBase() {
        for (int r = 0; r < FIELD_SIZE_X; r++) {
            for (int c = 0; c < FIELD_SIZE_Y; c++) {
                densityField[r][c] = 0.0;
                densityFieldPrev[r][c] = 0.0;
                velocityField[r][c] = new Vec2(0, 0);
                velocityFieldPrev[r][c] = new Vec2(0, 0);
                tmpBuffer1[r][c] = 0.0;
                tmpBuffer2[r][c] = 0.0;
            }
        }
    }

    // Key & mouse pressed methods
    protected void keyPressed(KeyEvent e) {
        onKeyPressed( (char)e.getKeyCode() );
    }
    protected void keyReleased(KeyEvent e) {
        onKeyReleased( (char)e.getKeyCode() );
    }
    protected void mousePressed(MouseEvent e) {
        onMouseButtonPressed( e.getButton() );
    }
    protected void mouseReleased(MouseEvent e) {
        onMouseButtonReleased( e.getButton() );
    }

    // primary update method for updating the density field (does all the steps)
    public void updateDensityField(double deltaTime) {
        double[][] tmp;
        zeroDensityField(densityFieldPrev);
        setSourcesForDensityField(densityFieldPrev);
        applySourcesToDensityField(densityField, densityFieldPrev, deltaTime);
        /* swap */ tmp = densityField; densityField = densityFieldPrev; densityFieldPrev = tmp;
        updateDiffusionOfDensityField(densityField, densityFieldPrev, SCALAR_FIELD_DIFFUSION_FACTOR, deltaTime);
        /* swap */ tmp = densityField; densityField = densityFieldPrev; densityFieldPrev = tmp;
        updateApplyVelocityFieldToDensityField(densityField, densityFieldPrev, velocityField, deltaTime);
    }

    protected void zeroDensityField(double[][] densField) {
        // setup
        int rowCount = densField.length;
        int columnCount = densField[0].length;

        // zero it out
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                densField[r][c] = 0;
            }
        }
    }

    public void dampDensityField(double[][] densField, double scaleFactor) {
        // setup
        int rowCount = densField.length;
        int columnCount = densField[0].length;

        // go through multiplying
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                densField[r][c] *= scaleFactor;
            }
        }
    }

    // takes sources/forces and applies it to your density field using deltaTime
    protected void applySourcesToDensityField(double[][] densField, double[][] srcField, double deltaTime) {
        // setup
        int rowCount = densField.length;
        int rowCountNonBorder = rowCount - 2;
        int columnCount = densField[0].length;
        int columnCountNonBorder = columnCount - 2;
        
        // loop over the field applying the source density field
        for (int r = 1 ; r <= rowCountNonBorder; r++) {
            for (int c = 1 ; c <= columnCountNonBorder; c++) {
                densField[r][c] += srcField[r][c] * deltaTime;
            }
        }
    }

    // Diffuse of scalar density field (so dye/smoke density field), assumes a one element border around entire matrix
    protected void updateDiffusionOfDensityField(double[][] densPost, double[][] densPre, double diffFactor, double deltaTime) { 
        // setup
        int rowCount = densPre.length;
        int rowCountNonBorder = rowCount - 2;
        int columnCount = densPre[0].length;
        int columnCountNonBorder = columnCount - 2;
        double diffFactorCell = deltaTime * diffFactor * rowCountNonBorder * columnCountNonBorder;

        // Gauss-Seidel relaxation solution for linear equations (one equation per element)
        for (int k = 0 ; k < LIN_SOLVE_STEPS ; k++) { 
            for (int r = 1 ; r <= rowCountNonBorder; r++) {
                for (int c = 1 ; c <= columnCountNonBorder; c++) {
                    densPost[r][c] = (densPre[r][c] + diffFactorCell * (densPost[r-1][c] + densPost[r+1][c] + densPost[r][c-1] + densPost[r][c+1])) / (1.0 + 4.0 * diffFactorCell); 
                }
            }
            forceBoundryToScalarField(densPost);
        }  
    }

    // Advection step - Applies the velocity vector field to the scalar density field (so dye/smoke density field)
    protected void updateApplyVelocityFieldToDensityField(double[][] densPost, double[][] densPre, Vec2[][] vel, double deltaTime) { 
        // setup
        int rowCount = densPre.length;
        int rowCountNonBorder = rowCount - 2;
        int columnCount = densPre[0].length;
        int columnCountNonBorder = columnCount - 2;
        double deltaTimeR = deltaTime * rowCountNonBorder;
        double deltaTimeC = deltaTime * columnCountNonBorder;
         
        // At each cell use the velocity to determine where we are getting out next density particles
        //  note that the computed cell will be between four cells, so bilinear filter sample to get the density at that "cell"
        for (int r = 1; r <= rowCountNonBorder; r++) { 
            for (int c = 1; c <= columnCountNonBorder; c++) {
                // this is basically posBefore = posNow - vel * dt, where posBefore is a floating point coordinate
                double x = r - deltaTimeR * vel[r][c].x; 
                double y = c - deltaTimeC * vel[r][c].y; 

                // bilinear filter sampling
                if (x < 0.5) x = 0.5; 
                if (x > rowCountNonBorder + 0.5) x = rowCountNonBorder + 0.5; 
                int r0 = (int)x;
                int r1 = r0 + 1; 
                if (y < 0.5) y = 0.5; 
                if (y > columnCountNonBorder+0.5) y = columnCountNonBorder + 0.5; 
                int c0 = (int)y; 
                int c1 = c0 + 1; 
                double s1 = x - r0; 
                double s0 = 1 - s1; 
                double t1 = y - c0; 
                double t0 = 1 - t1;

                // and...apply it
                densPost[r][c] = s0 * (t0 * densPre[r0][c0] + t1 * densPre[r0][c1]) + s1 * (t0 * densPre[r1][c0] + t1 * densPre[r1][c1]);
            } 
        } 
        forceBoundryToScalarField(densPost); 
    }

    // deal with those edge cells
    private void forceBoundryToScalarField(double[][] dens) {
        int rowCount = dens.length;
        int rowCountNonBorder = rowCount - 2;
        int columnCount = dens[0].length;
        int columnCountNonBorder = columnCount - 2;

        // top & bottom rows
        for (int c = 1; c <= columnCountNonBorder; c++) {
            dens[0][c] = dens[1][c];
            dens[rowCountNonBorder + 1][c] = dens[rowCountNonBorder][c];
        }

        // left & right columns (force same value as neighbor)
        for (int r = 1; r <= rowCountNonBorder; r++) { 
            dens[r][0] = dens[r][1];
            dens[r][columnCountNonBorder + 1] = dens[r][columnCountNonBorder];
        }

        // corners
        dens[0][0] = (dens[1][0] + dens[0][1]) * 0.5;
        dens[0][columnCountNonBorder + 1] = (dens[1][columnCountNonBorder + 1] + dens[0][columnCountNonBorder]) * 0.5;
        dens[rowCountNonBorder + 1][0] = (dens[rowCountNonBorder][0] + dens[rowCountNonBorder + 1][1]) * 0.5;
        dens[rowCountNonBorder + 1][columnCountNonBorder + 1] = (dens[rowCountNonBorder][columnCountNonBorder + 1] + dens[rowCountNonBorder + 1][columnCountNonBorder]) * 0.5;
    }

    // main update method for updating the velocity field (does all the steps)
    public void updateVelocityField(double deltaTime) {
        Vec2[][] tmp;
        zeroVelocityField(velocityFieldPrev);
        setSourcesForVelocityField(velocityFieldPrev);
        applySourcesToVelocityField(velocityField, velocityFieldPrev, deltaTime);
        /* swap */ tmp = velocityField; velocityField = velocityFieldPrev; velocityFieldPrev = tmp;
        updateDiffusionOfVelocityField(velocityField, velocityFieldPrev, VECTOR_FIELD_DIFFUSION_FACTOR, deltaTime);
        removeCompressionInVelocityField(velocityField, tmpBuffer1, tmpBuffer2);
        /* swap */ tmp = velocityField; velocityField = velocityFieldPrev; velocityFieldPrev = tmp;
        updateAdvectionOfVelocityField(velocityField, velocityFieldPrev, deltaTime);
        removeCompressionInVelocityField(velocityField, tmpBuffer1, tmpBuffer2);
    }

    protected void zeroVelocityField(Vec2[][] velField) {
        // setup
        int rowCount = velField.length;
        int columnCount = velField[0].length;

        // zero it out
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                velField[r][c].x = 0;
                velField[r][c].y = 0;
            }
        }
    }

    // takes sources/forces and applies it to your velocity field using deltaTime
    protected void applySourcesToVelocityField(Vec2[][] velField, Vec2[][] srcField, double deltaTime) {
        // setup
        int rowCount = velField.length;
        int rowCountNonBorder = rowCount - 2;
        int columnCount = velField[0].length;
        int columnCountNonBorder = columnCount - 2;
        
        // loop over the field applying the source density field
        for (int r = 1 ; r <= rowCountNonBorder; r++) {
            for (int c = 1 ; c <= columnCountNonBorder; c++) {
                velField[r][c].x += srcField[r][c].x * deltaTime;
                velField[r][c].y += srcField[r][c].y * deltaTime;
            }
        }
    }

    // Diffuse of vector velocity field, assumes a one element border around entire matrix
    protected void updateDiffusionOfVelocityField(Vec2[][] velPost, Vec2[][] velPre, double diffFactor, double deltaTime) { 
        // setup
        int rowCount = velPre.length;
        int rowCountNonBorder = rowCount - 2;
        int columnCount = velPre[0].length;
        int columnCountNonBorder = columnCount - 2;
        double diffFactorCell = deltaTime * diffFactor * rowCountNonBorder * columnCountNonBorder;

        // Gauss-Seidel relaxation solution for linear equations (one equation per element)
        for (int k = 0 ; k < LIN_SOLVE_STEPS ; k++) { 
            for (int r = 1 ; r <= rowCountNonBorder; r++) {
                for (int c = 1 ; c <= columnCountNonBorder; c++) {
                    Vec2 neighborSum = Vec2.zero().add(velPost[r-1][c]).add(velPost[r+1][c]).add(velPost[r][c-1]).add(velPost[r][c+1]);
                    velPost[r][c] = neighborSum.multiply(diffFactorCell).add(velPre[r][c]).divide(1.0 + 4.0 * diffFactorCell); 
                }
            }
            forceBoundryToVelocityField(velPost);
        }  
    }

    // Applies the velocity vector field to the vector velocity field
    protected void updateAdvectionOfVelocityField(Vec2[][] velPost, Vec2[][] velPre, double deltaTime) { 
        // setup
        int rowCount = velPre.length;
        int rowCountNonBorder = rowCount - 2;
        int columnCount = velPre[0].length;
        int columnCountNonBorder = columnCount - 2;
        double deltaTimeN = deltaTime * rowCount;
         
        // At each cell use the velocity to determine where we are getting out next density particles
        //  note that the computed cell will be between four cells, so bilinear filter sample to get the density at that "cell"
        for (int r = 1; r <= rowCountNonBorder; r++) {
            for (int c = 1; c <= columnCountNonBorder; c++) {
                // This is basically posBefore = posNow - vel * dt, where posBefore is a floating point coordinate
                double x = r - deltaTimeN * velPre[r][c].x; 
                double y = c - deltaTimeN * velPre[r][c].y; 

                // Bilinear filter sampling
                if (x < 0.5) x = 0.5; 
                if (x > rowCountNonBorder + 0.5) x = rowCountNonBorder + 0.5; 
                int r0 = (int)x;
                int r1 = r0 + 1; 
                if (y < 0.5) y = 0.5; 
                if (y > columnCountNonBorder+0.5) y = columnCountNonBorder + 0.5; 
                int c0 = (int)y; 
                int c1 = c0 + 1; 
                double s1 = x - r0;
                double s0 = 1 - s1;
                double t1 = y - c0;
                double t0 = 1 - t1;

                // and...apply it
                velPost[r][c].x = s0 * (t0 * velPre[r0][c0].x + t1 * velPre[r0][c1].x) + s1 * (t0 * velPre[r1][c0].x + t1 * velPre[r1][c1].x);
                velPost[r][c].y = s0 * (t0 * velPre[r0][c0].y + t1 * velPre[r0][c1].y) + s1 * (t0 * velPre[r1][c0].y + t1 * velPre[r1][c1].y);
            } 
        } 
        forceBoundryToVelocityField(velPost); 
    }

    // This step enforces the medium to be incompressible (the field to be mass conserving)
    protected void removeCompressionInVelocityField(Vec2[][] vel, double[][] p, double[][] div) {
        // setup
        int rowCount = vel.length;
        int rowCountNonBorder = rowCount - 2;
        int columnCount = vel[0].length;
        int columnCountNonBorder = columnCount - 2;
        double h = 2.0 / (rowCountNonBorder + columnCountNonBorder);

        for (int r = 1; r <= rowCountNonBorder; r++) {
            for (int c = 1 ; c <= columnCountNonBorder; c++) {
                div[r][c] = -0.5 * h * (vel[r + 1][c].x - vel[r - 1][c].x + vel[r][c + 1].y - vel[r][c - 1].y);
                p[r][c] = 0; 
            } 
        } 
        forceBoundryToScalarField(div); 
        forceBoundryToScalarField(p);
        
        for (int k = 0 ; k < LIN_SOLVE_STEPS ; k++) {
            for (int r = 1 ; r <= rowCountNonBorder; r++) {
                for (int c = 1 ; c <= columnCountNonBorder; c++) {
                    p[r][c] = (div[r][c] + p[r - 1][c] + p[r + 1][c] + p[r][c - 1] + p[r][c + 1]) / 4;
                }
            }
            forceBoundryToScalarField(p);
        }
        
        for (int r = 1 ; r <= rowCountNonBorder; r++) {
            for (int c = 1 ; c <= columnCountNonBorder; c++) {
                vel[r][c].x -= 0.5 * (p[r + 1][c] - p[r - 1][c]) / h;
                vel[r][c].y -= 0.5 * (p[r][c + 1] - p[r][c - 1]) / h;
            }
        }
        forceBoundryToVelocityField(vel);
    } 

    // deal with those edge cells
    private void forceBoundryToVelocityField(Vec2[][] vel) {
        int rowCount = vel.length;
        int rowCountNonBorder = rowCount - 2;
        int columnCount = vel[0].length;
        int columnCountNonBorder = columnCount - 2;

        // top & bottom rows
        for (int c = 1; c <= columnCountNonBorder; c++) {
            vel[0][c].x = -vel[1][c].x; vel[0][c].y = vel[1][c].y;
            vel[rowCountNonBorder + 1][c].x = -vel[rowCountNonBorder][c].x; vel[rowCountNonBorder + 1][c].y = vel[rowCountNonBorder][c].y;
        }

        // left & right columns (force same value as neighbor)
        for (int r = 1; r <= rowCountNonBorder; r++) { 
            vel[r][0].x = vel[r][1].x; vel[r][0].y = -vel[r][1].y;
            vel[r][columnCountNonBorder + 1].x = vel[r][columnCountNonBorder].x; vel[r][columnCountNonBorder + 1].y = -vel[r][columnCountNonBorder].y;
        }

        // corners
        vel[0][0] = Vec2.add(vel[1][0], vel[0][1]).multiply(0.5);
        vel[0][columnCountNonBorder + 1] = Vec2.add(vel[1][columnCountNonBorder + 1], vel[0][columnCountNonBorder]).multiply(0.5);
        vel[rowCountNonBorder + 1][0] = Vec2.add(vel[rowCountNonBorder][0], vel[rowCountNonBorder + 1][1]).multiply(0.5);
        vel[rowCountNonBorder + 1][columnCountNonBorder + 1] = Vec2.add(vel[rowCountNonBorder][columnCountNonBorder + 1], vel[rowCountNonBorder + 1][columnCountNonBorder]).multiply(0.5);
    }
}
