package math;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import engine.Camera;
import engine.Game;

public class Mesh {
	
	public ArrayList<Triangle> triangles;
	ArrayList<Triangle> triangleToDrawn = new ArrayList<Triangle>();
	float theta = 0;

	public Mesh(ArrayList<Triangle> t) {
		triangles = t ;
	}
	
	public void tick() {
		//theta += 0.01f ;
		
		float[][] matrix = new float[4][4];
		float[][] matriTrans = MatrizUtil.translationMatrix(.0f, .0f, 0.0f);
		float[][] matrixRotZ = MatrizUtil.matrixRotationZ(theta);
		float[][] matrixRotX = MatrizUtil.matrixRotationX(theta);
		float[][] matrixWorld = MatrizUtil.identityMatrix();
		matrixWorld = MatrizUtil.matrixMatrixMultiply(matrixRotX, matrixRotZ);
		matrixWorld = MatrizUtil.matrixMatrixMultiply(matrixWorld, matriTrans);
		triangleToDrawn.clear();
		matrix = MatrizUtil.projectionMatrix(90.0f, (float)Game.HEIGHT/Game.WIDTH, 0.1f, 1000.0f);
		
		
		for(Triangle t : this.triangles) {
			
			Triangle tProjected = new Triangle();
			Triangle tTranslated = new Triangle();
			Triangle tView = new Triangle();

			tTranslated.p[0] = MatrizUtil.multiplyMatrixVector(t.p[0],matrixWorld);
			tTranslated.p[1] = MatrizUtil.multiplyMatrixVector(t.p[1],matrixWorld);
			tTranslated.p[2] = MatrizUtil.multiplyMatrixVector(t.p[2],matrixWorld);
			
			tTranslated.p[0].z += .2f; 
			tTranslated.p[1].z += .2f; 
			tTranslated.p[2].z += .2f; 
			
			Vec3d normal = new Vec3d(0,0,0), line1 = new Vec3d(0,0,0), line2 = new Vec3d(0,0,0);
			
			line1 = Vec3d.subVec(tTranslated.p[1], tTranslated.p[0]);
			line2 = Vec3d.subVec(tTranslated.p[2], tTranslated.p[0]);
			
			normal = Vec3d.crossProductVecto(line1, line2);
			normal = Vec3d.normalizeVector(normal);
			
			Vec3d vCameraRay = Vec3d.subVec(tTranslated.p[0], Game.cam.vCamera);

			if(Vec3d.vectorDotProduct(vCameraRay,normal) < 0.0f) {
				
				Vec3d lightDirection = new Vec3d(1.0f,1.0f,-1.0f);
				lightDirection = Vec3d.normalizeVector(lightDirection);
				
				float lightDotPoint = Vec3d.vectorDotProduct(lightDirection, normal);
				
				tView.p[0] = MatrizUtil.multiplyMatrixVector(tTranslated.p[0],Game.cam.viewMatrix);
				tView.p[1] = MatrizUtil.multiplyMatrixVector(tTranslated.p[1],Game.cam.viewMatrix);
				tView.p[2] = MatrizUtil.multiplyMatrixVector(tTranslated.p[2],Game.cam.viewMatrix);
				
				int nClippedTriangles = 0;
				Triangle[] clipped = new Triangle[2];
				clipped[0] = new Triangle();
				clipped[1] = new Triangle();
				Triangle aux = tView;
				nClippedTriangles = Vec3d.triangleIntClipAgainstPlane(new Vec3d(0,0,0.1f), new Vec3d(0,0,1), aux, clipped[0], clipped[1]);
				
				//for(int n = 0; n < nClippedTriangles; n++){
					
					tProjected.p[0] = MatrizUtil.multiplyMatrixVector(aux.p[0],matrix);
					tProjected.p[1] = MatrizUtil.multiplyMatrixVector(aux.p[1],matrix);
					tProjected.p[2] = MatrizUtil.multiplyMatrixVector(aux.p[2],matrix);
					
					tProjected.p[0] = Vec3d.divideVec(tProjected.p[0], tProjected.p[0].w);
					tProjected.p[1] = Vec3d.divideVec(tProjected.p[1], tProjected.p[1].w);
					tProjected.p[2] = Vec3d.divideVec(tProjected.p[2], tProjected.p[2].w);
					
					
					Vec3d offSet = new Vec3d(1,1,0);
					tProjected.p[0] = Vec3d.sumVec(tProjected.p[0], offSet);
					tProjected.p[1] = Vec3d.sumVec(tProjected.p[1], offSet);
					tProjected.p[2] = Vec3d.sumVec(tProjected.p[2], offSet);;
					
		
					tProjected.p[0].x *= 0.5f *  Game.WIDTH; tProjected.p[0].y *= 0.5f *  Game.HEIGHT;
					tProjected.p[1].x *= 0.5f *  Game.WIDTH; tProjected.p[1].y *= 0.5f *  Game.HEIGHT;
					tProjected.p[2].x *= 0.5f *  Game.WIDTH; tProjected.p[2].y *= 0.5f *  Game.HEIGHT;
					tProjected.lightDotTriangle = lightDotPoint;
					triangleToDrawn.add(tProjected);
				//}
			}
				
		}
		
		triangleToDrawn.sort(new Comparator<Triangle>() {
    		public int compare(Triangle t1, Triangle t2) {
    			float z1 = (t1.p[0].z + t1.p[1].z + t1.p[2].z) / 3.0f;
    			float z2 = (t2.p[0].z + t2.p[1].z + t2.p[2].z) / 3.0f;
    			if(z1 > z2) {
    				return -1;
    			}if(z1 < z2) {
    				return +1;
    			}
    			return 0;
    		}
    	});
	}
	
	public void drawTriangle(int x1,int y1,int x2,int y2,int x3,int y3,float luminense,Graphics g) {
		
		
		if(luminense < 0) {
			luminense = 0;
		}
		Color c = new Color(1.0f*luminense,1.0f*luminense,0.9f*luminense);
		g.setColor(c);	
		
		Polygon p = new Polygon();
		p.addPoint(x3, y3);
		p.addPoint(x2, y2);
		p.addPoint(x1, y1);
		
		g.fillPolygon(p);
		
	}
	
	public void render(Graphics g) {
		
		for(Triangle tProjected : triangleToDrawn) {
			drawTriangle((int)tProjected.p[0].x,	(int)tProjected.p[0].y,
					(int) tProjected.p[1].x, 	(int)tProjected.p[1].y,
					(int)tProjected.p[2].x, 	(int)tProjected.p[2].y,
					tProjected.lightDotTriangle, g);
		}
	}
	
	public Triangle createNewTriangle(float x, float y, float z, float x1, float y1, float z1, float x2, float y2, float z2) {
		Triangle t = new Triangle();
		Vec3d point1 = new Vec3d(x,y,z);
		Vec3d point2 = new Vec3d(x1,y1,z1);
		Vec3d point3 = new Vec3d(x2,y2,z2);
		t.p[0] = point1;
		t.p[1] = point2;
		t.p[2] = point3;
		return t;
	}
	
	public Mesh importMesh(String path) {
		
		File importedMesh = new File(path);
		try {
			@SuppressWarnings("resource")
			Scanner reader = new Scanner(importedMesh);
			ArrayList<Vec3d> vector = new ArrayList<Vec3d>();
			ArrayList<Triangle> triangle = new ArrayList<Triangle>();
			
			while(reader.hasNextLine()) {
				String line;
				line = reader.nextLine();
				
				
				List<String> values = Stream.of(line.split(" ")).map(elem -> new String(elem)).collect(Collectors.toList());
				
				if(values.get(0).equals("v")) {
					Vec3d v = new Vec3d(0,0,0);
					v.x = Float.parseFloat(values.get(1));
					v.y = Float.parseFloat(values.get(2));
					v.z = Float.parseFloat(values.get(3));

					vector.add(v );
				}
				
				
				if(values.get(0).equals("f")) {
					
					int[] f = new int[3];
					
					f[0] = Integer.parseInt(values.get(1));
					f[1] = Integer.parseInt(values.get(2));
					f[2] = Integer.parseInt(values.get(3));

					Triangle t = new Triangle();
					t.p[0] = vector.get(f[0]-1);
					t.p[1] = vector.get(f[1]-1);
					t.p[2] = vector.get(f[2]-1);
					triangle.add(t);
				}

			}
			Mesh mesh = new Mesh(triangle);
			return mesh;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	

}
