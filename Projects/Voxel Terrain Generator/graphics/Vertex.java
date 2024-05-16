package graphics;

import math.Vec2;
import math.Vec3;

public class Vertex {
    public Vec3 pos;
    public Color color;
    public Vec2 uv;

    public Vertex(Vec3 pos, Color color, Vec2 uv) {
        this.pos = pos;
        this.color = color;
        this.uv = uv;
    }
    public Vertex(Vec3 pos, Color color) {
        this(pos, color, Vec2.zero());
    }
    public Vertex(Vec3 pos) {
        this(pos, Color.WHITE, Vec2.zero());
    }
}
