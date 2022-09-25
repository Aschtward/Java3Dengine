package engine;

import math.MatrizUtil;
import math.Vec3d;
import objects.Cube;

public class Camera {
	
	public  Vec3d vCamera; 
	public  Vec3d vUp; 
	public  Vec3d vTarget; 
	public  Vec3d vLookDir;
	public  Vec3d vFoward;
	public  float[][] camMatrix;
	public  float[][] viewMatrix;
	public  float[][] camRot;
	float[][] matrixRotZ;
	float[][] matrixRotX;
	public  float fYaw;
	private int time;
	public boolean turnLeft,turnRight,goFoward,goBackwards;
	public float fXaw;
	
	public Camera() {
		
		vCamera = new Vec3d(0,15,0);
		vUp = new Vec3d(0,-1,0);
		vTarget = new Vec3d(0,0,-1);
		vFoward = new Vec3d(0,0,0);
		camMatrix = MatrizUtil.matrixPointAt(vCamera, vTarget, vUp);
		viewMatrix = MatrizUtil.matrixQuickInvert(camMatrix);
		matrixRotZ = MatrizUtil.matrixRotationY(fYaw);
		matrixRotX = MatrizUtil.matrixRotationX(fXaw);
	}
	
	public void tick() {
		
		if(turnLeft) {
			Vec3d vLeft = new Vec3d(2,0,0);
			vLeft = MatrizUtil.multiplyMatrixVector(vLeft, camRot);
			vCamera = Vec3d.sumVec( vCamera,vLeft);
		}
		if(turnRight) {
			Vec3d vRight = new Vec3d(-2,0,0);
			vRight = MatrizUtil.multiplyMatrixVector(vRight, camRot);
			vCamera = Vec3d.sumVec( vCamera,vRight);
		}
		if(goFoward) {
			vCamera = Vec3d.sumVec( vCamera,vFoward);
		}
		if(goBackwards) {
			vCamera = Vec3d.subVec( vCamera,vFoward);
		}
		float[][] matriTrans = MatrizUtil.translationMatrix(.0f, .0f, 0.0f);
		matrixRotZ = MatrizUtil.matrixRotationY(fYaw);
		matrixRotX = MatrizUtil.matrixRotationX(fXaw);
		camRot = MatrizUtil.matrixMatrixMultiply(matrixRotX, matrixRotZ);
		camRot = MatrizUtil.matrixMatrixMultiply(camRot, matriTrans);

		vTarget = new Vec3d(0,0,1);
		vLookDir = MatrizUtil.multiplyMatrixVector(vTarget, camRot);
		vFoward = Vec3d.multiplyVec(vLookDir, 2.0f);
		vTarget = Vec3d.sumVec(vCamera, vLookDir);
		
		camMatrix = MatrizUtil.matrixPointAt(vCamera, vTarget, vUp);
		viewMatrix = MatrizUtil.matrixQuickInvert(camMatrix);
	}
	
	

}
