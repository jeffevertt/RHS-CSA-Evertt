package framework;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.Timer;

public class World extends JPanel implements ActionListener, MouseListener {
    // Singleton...
    private static World instance = null;
    public static synchronized World get()
    {
        if (instance == null)
            instance = new World();
  
        return instance;
    }

    // Constants...
    public static final int FIELD_BORDER = 10;
    public static final int FIELD_BORDER_TOP = FIELD_BORDER;
    public static final Color COLOR_BACKGROUND = new Color(229, 195, 172);
    public static final int RENDER_TIMER_PERIOD = 16;   // In milliseconds
    public static final double AMBIENT_LIGHT = 0.0;     // 0 to 1 scale
    public static final double REFLECTION_FACTOR = 0.075;

    // Member variables...
    private Vec2 origin = new Vec2(0, 0);
    private Vec2 canvasSize = new Vec2(0, 0);
    private double pixelsPerUnit = 1;
    private Timer renderTimer = null;
    private BufferedImage backBufferRealtime = null;
    private BufferedImage backBufferFinal = null;
    private RendererBase renderer = null;
    private int imageDownScaleFactor = 1;
    private Camera camera = null;
    private ArrayList<PointLight> lights = new ArrayList<PointLight>();
    private ArrayList<RenderObject> renderObjects = new ArrayList<RenderObject>();
    private ArrayList<RenderObject> occluderObjects = new ArrayList<RenderObject>(); // subset of renderObjects

    // Get/Set accessors/mutators...
    public Vec2 getOrigin() {
        return origin;
    }
    public Vec2 getCanvasSize() {
        return canvasSize;
    }
    public double getPixelsPerUnit() {
        return pixelsPerUnit;
    }
    public RendererBase getRenderer() {
        return renderer;
    }
    public void setRenderer(RendererBase renderer, int imageDownScaleFactor) {
        this.renderer = renderer;
        this.imageDownScaleFactor = Math.max(imageDownScaleFactor, 1);
    }

    // Member functions (methods)...
    protected World() {
        // Resize event...
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                // Set the origin and sizes...
                canvasSize = new Vec2(getWidth() - FIELD_BORDER * 2, getHeight() - FIELD_BORDER - FIELD_BORDER_TOP);
                pixelsPerUnit = Math.floor(Math.min(canvasSize.x, canvasSize.y) / 12);
                origin = new Vec2(FIELD_BORDER, getHeight() - FIELD_BORDER);

                // Back buffer(s)...
                if (backBufferRealtime != null) {
                    backBufferRealtime = null; // no dispose required?
                }
                if (backBufferFinal != null) {
                    backBufferFinal = null;
                }
                backBufferRealtime = new BufferedImage((int)canvasSize.x / imageDownScaleFactor, 
                                                       (int)canvasSize.y / imageDownScaleFactor, 
                                                       BufferedImage.TYPE_INT_ARGB);
                if (imageDownScaleFactor == 1) {
                    backBufferFinal = backBufferRealtime;
                }
                else {
                    backBufferFinal = new BufferedImage((int)canvasSize.x, 
                                                        (int)canvasSize.y, 
                                                        BufferedImage.TYPE_INT_ARGB);
                }
            }
        });

        // Defaults...
        setBackground(COLOR_BACKGROUND);

        // Kick off render timer...
        if (renderTimer != null && renderTimer.isRunning()) {
            renderTimer.stop();
        }
        renderTimer = new Timer(RENDER_TIMER_PERIOD, this);
        renderTimer.start();

        // Mouse listener...
        this.addMouseListener(this);
    }

    // Render timer...
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == renderTimer) {
            repaint();
        }
    }

    // Mouse events...
    public void mousePressed(MouseEvent event) {
    }
    public void mouseReleased(MouseEvent event) {
    }
    public void mouseEntered(MouseEvent event) {
    }
    public void mouseExited(MouseEvent event) {
    }
    public void mouseClicked(MouseEvent event) {
        // Double click restarts it...
        if (event.getClickCount() == 1) {
            // Pause/unpause the App...
            App.get().setPause(!App.get().isPaused());
        }
    }

    // Scene support methods...
    public void resetScene() {
        camera = null;
        lights.clear();
        renderObjects.clear();
        occluderObjects.clear();
    }
    public Camera getCamera() {
        return camera;
    }
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    public ArrayList<PointLight> getLights() {
        return this.lights;
    }
    public void addLight(PointLight light) {
        if (!lights.contains(light)) {
            lights.add(light);
        }
    }
    public ArrayList<RenderObject> getRenderObjects() {
        return this.renderObjects;
    }
    public ArrayList<RenderObject> getOccluders() {
        return this.occluderObjects;
    }
    public void addRenderObject(RenderObject rendObj) {
        addRenderObject(rendObj, false);
    }
    public void addRenderObject(RenderObject rendObj, boolean isLightOccluder) {
        if (!renderObjects.contains(rendObj)) {
            renderObjects.add(rendObj);
        }
        if (isLightOccluder && !occluderObjects.contains(rendObj)) {
            occluderObjects.add(rendObj);
        }
    }
    protected void createDefaultScene() {
        // Reset, just in case...
        resetScene();

        // Setup...
        //  World dims: 10x10x10 from points (0,0,0) to (10,10,10)
        double roomSize = 10.0;

        // Add the camera...
        Vec3 camInitPos = new Vec3(roomSize * 0.25, roomSize * 0.5, roomSize * 0.1);
        Vec3 camLookAtPt = new Vec3(roomSize * 0.5, roomSize * 0.35, roomSize * 0.5);
        setCamera( new Camera(camInitPos, camLookAtPt, Vec3.up(), 110.0, 0.1, 1000.0) );

        // Light(s)...
        addLight(new PointLight(new Vec3(roomSize * 0.8, roomSize * 0.7, roomSize * 0.5),
                                new Color(0xFF, 0xFF, 0xFF), 6.0));
        addLight(new PointLight(new Vec3(roomSize * 0.2, roomSize * 0.7, roomSize * 0.5),
                                new Color(0xFF, 0xFF, 0xFF), 6.0));

        // Scene walls...
        Color colorWalls = new Color(0xAA, 0xAA, 0xAA);
        addRenderObject(new Plane(new Vec3(0,0,0), Vec3.forward(), colorWalls));
        addRenderObject(new Plane(new Vec3(0,0,roomSize), Vec3.backward(), colorWalls));
        addRenderObject(new Plane(new Vec3(0,0,0), Vec3.right(), colorWalls));
        addRenderObject(new Plane(new Vec3(roomSize,0,0), Vec3.left(), colorWalls));
        addRenderObject(new Plane(new Vec3(0,0,0), Vec3.up(), colorWalls, Plane.ColorPattern.BLACK_AND_WHITE_GRID));
        addRenderObject(new Plane(new Vec3(0,roomSize,0), Vec3.down(), colorWalls));

        // RenderObject(s)...
        addRenderObject(new Sphere(new Vec3(roomSize * 0.5, roomSize * 0.35, roomSize * 0.5), 
                                   1.25, new Color(10, 255, 10)), true );
    }
    protected Color calcShadingAtSurfPt(Vec3 surfPt, Vec3 surfNorm, Color diffuseColor, double specFactor, int maxReflections) {
        // setup
        Vec3 diffuseColorVec3 = Vec3.colorToVec3( diffuseColor );
        ArrayList<PointLight> lights = World.get().getLights();
        ArrayList<RenderObject> occluders = World.get().getOccluders();
        Vec3 cameraLookUnit = World.get().getCamera().forward();
        double surfPosEpsilon = 0.0001;

        // final color (start off with ambient color)
        Vec3 finalColor = Vec3.multiply( diffuseColorVec3, World.AMBIENT_LIGHT );

        // find the direct illumination from lights
        for (PointLight light : lights) {
            Vec3 lightPos = light.getPos();
            Vec3 lightToSurfPt = Vec3.subtract(surfPt, lightPos);
            double lightToSurfPtLen = lightToSurfPt.length();
            Vec3 lightToSurfPtUnit = Vec3.multiply(lightToSurfPt, 1.0 / lightToSurfPtLen);
    
            // see if the light is occluded
            boolean isLightOccluded = false;
            for (RenderObject occluder : occluders) {
                double intDst = occluder.rayIntersectDst(lightPos, lightToSurfPtUnit);
                if ((intDst >= 0) && (intDst < lightToSurfPtLen - surfPosEpsilon)) {
                    isLightOccluded = true;
                    break;
                }
            }
            
            // it not, factor it in (sum up in finalColor)
            if (!isLightOccluded) {
                // calc light attenuation
                double lightAttenScalar = light.calcAttenScalar(lightToSurfPtLen);
                double lightNormDot = Math.max(-lightToSurfPtUnit.dot(surfNorm), 0.0);

                // diffuse component
                finalColor.add( Vec3.multiply(diffuseColorVec3, lightNormDot * lightAttenScalar) );

                // specular component (blinn-phong half angle model)
                Vec3 halfAngleVec = Vec3.add(cameraLookUnit, lightToSurfPtUnit).unit();
                double specComp = Math.max(Vec3.dot(surfNorm, halfAngleVec) * -1.0, 0);
                double diffuseClamp = Math.min(lightNormDot * 5, 1.0);
                specComp = (Math.pow(specComp, 16.0) * specFactor * diffuseClamp); // square it (could tweak this, depending on taste)
                finalColor.add( Vec3.multiply(Vec3.colorToVec3(light.getColor()), specComp) );
            }

            // reflection
            if (maxReflections > 0) {
                // calc the reflection vector
                Vec3 reflectVec = Vec3.add(cameraLookUnit, Vec3.multiply(surfNorm, 2.0)).unit();
                Vec3 reflectPos = Vec3.add(surfPt, Vec3.multiply(reflectVec, surfPosEpsilon));
                RayCastResult reflectRes = rayCast(reflectPos, reflectVec, 1000.0, maxReflections - 1);
                finalColor.add( Vec3.multiply(Vec3.colorToVec3(reflectRes.color), REFLECTION_FACTOR) );
            }
        }

        return Vec3.vec3ToColor(finalColor);
    }
    protected RayCastResult rayCast(Vec3 rayOrigin, Vec3 rayDirUnit, double maxRayLen) {
        return rayCast(rayOrigin, rayDirUnit, maxRayLen, 1);
    }
    protected RayCastResult rayCast(Vec3 rayOrigin, Vec3 rayDirUnit, double maxRayLen, int maxReflections) {
        RayCastResult res = null;

        // Go through checking for intersection with all objects. Save off first intersection
        ArrayList<RenderObject> renderObjects = World.get().getRenderObjects();
        for (RenderObject obj : renderObjects) {
            RayCastResult objRes = obj.rayCast(rayOrigin, rayDirUnit, maxReflections);
            if (objRes != null) {
                double dstToIntersect = objRes.pos.distance(rayOrigin);
                if (dstToIntersect <= maxRayLen) {
                    // pick the closest
                    if ((res == null) || (objRes.calcDstFromCamera() < res.calcDstFromCamera())) {
                        res = objRes;
                    }
                }
            }
        } 
        return res;
    }
    
    // Draw functions...
    private void drawWorld(Graphics2D g) {
        // Setup...
        Draw.beginRender(g);

        // Set the base transform (this deals with HPI screen scaling properly)...
        Draw.setBaseTransform(g.getTransform());

        // Rendering hints...
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Map<Object, Object> hints = new LinkedHashMap<Object, Object>();
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.addRenderingHints(hints);

        // Full window clear...
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Background...
        g.setColor(COLOR_BACKGROUND);
        g.fillRect(FIELD_BORDER, FIELD_BORDER_TOP, getWidth() - FIELD_BORDER * 2, getHeight() - FIELD_BORDER - FIELD_BORDER_TOP);
        g.setColor(!App.get().isPaused() ? Color.DARK_GRAY : Color.RED);
        g.setStroke(new BasicStroke(2));
        g.drawRect(FIELD_BORDER, FIELD_BORDER_TOP, getWidth() - FIELD_BORDER * 2, getHeight() - FIELD_BORDER - FIELD_BORDER_TOP);

        // Select a back buffer depending on motion (this is a performance optimization)...
        BufferedImage backBuffer = camera.isMoving() ? backBufferRealtime : backBufferFinal;

        // Let the renderer do its drawing...
        if ((camera != null) && (renderer != null) && (backBuffer != null)) {
            // 
            WritableRaster renderTarget = backBuffer.getRaster();
            renderer.render(renderTarget, this, camera);

            // Draw the back buffer onto the screen/panel...
            g.drawImage(backBuffer, 
                        (int)getOrigin().x, 
                        (int)getOrigin().y - (int)getCanvasSize().y, 
                        (int)getCanvasSize().x,
                        (int)getCanvasSize().y,
                        null);
        }
    }
    public void paint(Graphics g) {
        drawWorld((Graphics2D)g);
    }
}
