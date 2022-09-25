package math;

import java.util.ArrayList;

import engine.Game;

public class Vec3d {
	
	public float x,y,z,w;

	public Vec3d(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1;
	}
	
	public static Vec3d sumVec(Vec3d v1,Vec3d v2) {
		
		float x = v1.x + v2.x;
		float y = v1.y + v2.y;
		float z = v1.z + v2.z;
		Vec3d vec = new Vec3d(x,y,z);
		
		return vec;
	}
	
	public static Vec3d subVec(Vec3d v1,Vec3d v2) {
		
		float x = v1.x - v2.x;
		float y = v1.y - v2.y;
		float z = v1.z - v2.z;
		Vec3d vec = new Vec3d(x,y,z);
		
		return vec;
	}
	
	public static Vec3d multiplyVec(Vec3d v1, float k) {
		
		float x = v1.x * k;
		float y = v1.y * k;
		float z = v1.z * k;
		Vec3d vec = new Vec3d(x,y,z);
		
		return vec;
	}
	
	public static Vec3d divideVec(Vec3d v1, float k) {
		
		float x = v1.x / k;
		float y = v1.y / k;
		float z = v1.z / k;
		Vec3d vec = new Vec3d(x,y,z);
		
		return vec;
	}
	
	public static float vectorDotProduct(Vec3d v1, Vec3d v2) {
		
		
		return v1.x*v2.x + v1.y*v2.y + v1.z * v2.z;
	}
	
	public static float vectorLenght(Vec3d v) {
		
		return (float) Math.sqrt(vectorDotProduct(v,v));	
	}
	
	public static Vec3d normalizeVector(Vec3d v) {
		
		float l = vectorLenght(v);
		
		return new Vec3d(v.x/l,v.y/l,v.z/l);
	}
	
	public static Vec3d crossProductVecto(Vec3d v, Vec3d v1) {
		
		Vec3d crossVector = new Vec3d(0,0,0);

		crossVector.x = v.y * v1.z - v.z * v1.y;
		crossVector.y = v.z * v1.x - v.x * v1.z;
		crossVector.z = v.x * v1.y - v.y * v1.x;

		return crossVector;
	}
	
	public static Vec3d vecIntersectsPlane(Vec3d plane, Vec3d planeN, Vec3d lineStart, Vec3d lineEnd) {
		 
		planeN = Vec3d.normalizeVector(planeN);
		
		float planeD = -Vec3d.vectorDotProduct(planeN, plane);
		float ad = Vec3d.vectorDotProduct(lineStart, planeN);
		float bd = Vec3d.vectorDotProduct(lineEnd, planeN);
		float t = (-planeD - ad)/(bd - ad);
		
		Vec3d lineStartToEnd = Vec3d.subVec(lineEnd, lineStart);
		Vec3d lineToIntersect = Vec3d.multiplyVec(lineStartToEnd, t);
		
		return Vec3d.sumVec(lineStart, lineToIntersect);
	}
	
	public static int triangleIntClipAgainstPlane(Vec3d planeP,Vec3d planeN, Triangle inTri,Triangle outTri1, Triangle outTri2) {
		
		planeN = Vec3d.normalizeVector(planeN);
		
		Vec3d[] insidePoints = new Vec3d[3];  int nInsidePointCount = 0;
		Vec3d[] outsidePoints = new Vec3d[3]; int nOutsidePointCount = 0;
		Vec3d v1 = Vec3d.normalizeVector(inTri.p[0]);
		Vec3d v2 = Vec3d.normalizeVector(inTri.p[1]);
		Vec3d v3 = Vec3d.normalizeVector(inTri.p[2]);
		float d0 = (planeN.x * v1.x + planeN.y * v1.y + planeN.z * v1.z - Vec3d.vectorDotProduct(planeN, planeP));
		float d1 = (planeN.x * v2.x + planeN.y * v2.y + planeN.z * v2.z - Vec3d.vectorDotProduct(planeN, planeP));
		float d2 = (planeN.x *  v3.x + planeN.y *  v3.y + planeN.z *  v3.z - Vec3d.vectorDotProduct(planeN, planeP));
		

		if (d0 >= 0) { 
			insidePoints[nInsidePointCount++] = inTri.p[0]; 
		}else { 
			outsidePoints[nOutsidePointCount++] = inTri.p[0];
		}
		if (d1 >= 0) { 
			insidePoints[nInsidePointCount++] = inTri.p[1];
		} else { 
			outsidePoints[nOutsidePointCount++] = inTri.p[1]; 
		}
		if (d2 >= 0) { 
			insidePoints[nInsidePointCount++] = inTri.p[2]; 
		} else { 
			outsidePoints[nOutsidePointCount++] = inTri.p[2];
		}
		
		if (nInsidePointCount == 0)
		{
			return 0;
		}
		
		if (nInsidePointCount == 3)
		{
			outTri1 = inTri;

			return 1;
		}
		
		if (nInsidePointCount == 1 && nOutsidePointCount == 2)
		{

			outTri1.lightDotTriangle =  inTri.lightDotTriangle;

			outTri1.p[0] = insidePoints[0];

			outTri1.p[1] = vecIntersectsPlane(planeP, planeN, insidePoints[0], outsidePoints[0]);
			outTri1.p[2] = vecIntersectsPlane(planeP, planeN, insidePoints[0], outsidePoints[1]);

			return 1; 
		}
		if (nInsidePointCount == 2 && nOutsidePointCount == 1)
		{

			outTri1.lightDotTriangle =  inTri.lightDotTriangle;
			outTri2.lightDotTriangle =  inTri.lightDotTriangle;


			outTri1.p[0] = insidePoints[0];
			outTri1.p[1] = insidePoints[1];
			outTri1.p[2] = vecIntersectsPlane(planeP, planeN, insidePoints[0], outsidePoints[0]);


			outTri2.p[0] = insidePoints[1];
			outTri2.p[1] = outTri1.p[2];
			outTri2.p[2] = vecIntersectsPlane(planeP, planeN, insidePoints[1], outsidePoints[0]);

			return 2; 
		}
		return 0;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	

}
