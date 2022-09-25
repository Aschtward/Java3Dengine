package math;

public class MatrizUtil {
	
	public MatrizUtil() {
		
	}
	
	public static float[][] identityMatrix(){
		
		float[][] matrix = new float[4][4];

		matrix[0][0] = 1;
		matrix[1][1] = 1;
		matrix[2][2] = 1;
		matrix[3][3] = 1;
		
		return matrix;
	}
	
	public static float[][] matrixRotationY(float fAngleRad){
		
		float[][] matrix = new float[4][4];
		
		matrix[0][0] = (float) Math.cos(fAngleRad);
		matrix[0][2] = (float) Math.sin(fAngleRad);
		matrix[2][0] = (float) -Math.sin(fAngleRad);
		matrix[1][1] = 1;
		matrix[2][2] = (float) Math.cos(fAngleRad);
		matrix[3][3] = 1;
		
		return matrix;
	}
	
	public static float[][] matrixRotationX(float fAngleRad){
		
		float[][] matrix = new float[4][4];
		
		matrix[0][0] = 1;
		matrix[1][1] =  (float) Math.cos(fAngleRad);
		matrix[1][2] = (float) Math.sin(fAngleRad);
		matrix[2][1] =  (float) -Math.sin(fAngleRad);
		matrix[2][2] =  (float) Math.cos(fAngleRad);
		matrix[3][3] = 1;
		
		return matrix;
	}
	
	public static float[][] matrixRotationZ(float fAngleRad){
		
		float[][] matrix = new float[4][4];
		
		matrix[0][0] =  (float) Math.cos(fAngleRad); 
		matrix[0][1] = (float) Math.sin(fAngleRad);
		matrix[1][0] =  (float)- Math.sin(fAngleRad);
		matrix[1][1] = (float) Math.cos(fAngleRad);
		matrix[2][2] = 1;
		matrix[3][3] = 1;
		
		return matrix;
	}
	
	public static float[][] projectionMatrix(float fov, float fAspectRadio, float fNear, float fFar) {
		
		float[][] matrix = new float[4][4];
		float fFovRad = 1 / (float) Math.tan(fov * 0.5 / 180.0f *3.1459f);
		
		matrix[0][0] = fAspectRadio * fFovRad;
		matrix[1][1] = fFovRad;
		matrix[2][2] = fFar / (fFar - fNear);
		matrix[3][2] = (-fFar * fNear) / (fFar - fNear);
		matrix[2][3] = 1.0f;
		matrix[3][3] = 0.0f;
		
		return matrix;
	}
	
	public static float[][] translationMatrix(float x,float y,float z){
		
		float[][] matrix = new float[4][4];
		
		matrix[0][0] = 1;
		matrix[1][1] = 1;
		matrix[2][2] = 1;
		matrix[3][3] = 1;
		matrix[3][0] = x;
		matrix[3][1] = y;
		matrix[3][2] = z;
		
		return matrix;
	}
	
	public static float[][] matrixMatrixMultiply(float[][] m1, float[][] m2){
		
		float[][] matrix = new float[4][4];
		
		for (int c = 0; c < 4; c++)
			for (int r = 0; r < 4; r++)
				matrix[r][c] = m1[r][0] * m2[0][c] + m1[r][1] * m2[1][c] + m1[r][2] * m2[2][c] + m1[r][3] * m2[3][c];
		
		return matrix;
	}
	
	
	public static Vec3d multiplyMatrixVector(Vec3d vec1, float[][] matrix) {
		
		float x = vec1.x * matrix[0][0] + vec1.y *  matrix[1][0] + vec1.z *  matrix[2][0] +  vec1.w * matrix[3][0];
		float y = vec1.x * matrix[0][1] + vec1.y *  matrix[1][1] + vec1.z *  matrix[2][1] +  vec1.w * matrix[3][1];
		float z = vec1.x * matrix[0][2] + vec1.y *  matrix[1][2] + vec1.z *  matrix[2][2] +  vec1.w * matrix[3][2];
		float w = vec1.x * matrix[0][3] + vec1.y *  matrix[1][3] + vec1.z *  matrix[2][3] +  vec1.w * matrix[3][3];
		
		Vec3d vec2 = new Vec3d(x,y,z);
		vec2.w = w;
		return vec2;
	}
	
	public static float[][] matrixPointAt(Vec3d pos, Vec3d target, Vec3d up){
		
		float[][] matrix = new float[4][4];
		Vec3d newFoward = new Vec3d(0,0,0);
		Vec3d a = new Vec3d(0,0,0);
		Vec3d newUp = new Vec3d(0,0,0);
		Vec3d newRight = new Vec3d(0, 0, 0);
		
		//Calculate new foward direction
		newFoward = Vec3d.subVec(target, pos);
		newFoward = Vec3d.normalizeVector(newFoward);
		
		//Calculate new up direction
		a = Vec3d.multiplyVec(newFoward, Vec3d.vectorDotProduct(up, newFoward));
		newUp = Vec3d.subVec(up, a);
		newUp = Vec3d.normalizeVector(newUp);
		
		//Calculate new right direction
		newRight = Vec3d.crossProductVecto(newUp, newFoward);
		
		//Constructing matrix
		matrix[0][0] = newRight.x; 	matrix[0][1] = newRight.y; 	matrix[0][2] = newRight.z; 	matrix[0][3] = 0;
		matrix[1][0] = newUp.x; 	matrix[1][1] = newUp.y; 	matrix[1][2] = newUp.z; 	matrix[1][3] = 0;
		matrix[2][0] = newFoward.x; matrix[2][1] = newFoward.y; matrix[2][2] = newFoward.z; matrix[2][3] = 0;
		matrix[3][0] = pos.x; 		matrix[3][1] = pos.y; 		matrix[3][2] = pos.z; 		matrix[3][3] = 1;
		
		return matrix;
	}
	
	public static float[][] matrixQuickInvert(float[][] m){
		
		float[][] matrix = new float[4][4];
		
		matrix[0][0] = m[0][0]; matrix[0][1] = m[1][0]; matrix[0][2] = m[2][0]; matrix[0][3] = 0.0f;
		matrix[1][0] = m[0][1]; matrix[1][1] = m[1][1]; matrix[1][2] = m[2][1]; matrix[1][3] = 0.0f;
		matrix[2][0] = m[0][2]; matrix[2][1] = m[1][2]; matrix[2][2] = m[2][2]; matrix[2][3] = 0.0f;
		matrix[3][0] = -(m[3][0] * matrix[0][0] + m[3][1] * matrix[1][0] + m[3][2] * matrix[2][0]);
		matrix[3][1] = -(m[3][0] * matrix[0][1] + m[3][1] * matrix[1][1] + m[3][2] * matrix[2][1]);
		matrix[3][2] = -(m[3][0] * matrix[0][2] + m[3][1] * matrix[1][2] + m[3][2] * matrix[2][2]);
		matrix[3][3] = 1.0f;
		
		return matrix;
	}
	
	

}
