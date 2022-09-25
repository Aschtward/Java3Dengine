package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Comparator;

import engine.Game;
import math.MatrizUtil;
import math.Mesh;
import math.Triangle;
import math.Vec3d;

public class Cube {
	
	private Mesh mesh;
	private MatrizUtil matrizUtil;
	public float theta = 0.5f;

	
	public Cube() {
		mesh = new Mesh(null);
		this.mesh = mesh.importMesh("obj/teapot.obj");
		matrizUtil = new MatrizUtil();
	}
	
	public void tick() {
		mesh.tick();
	}
	
	
	public void render(Graphics g) {
		mesh.render(g);
	}

}
