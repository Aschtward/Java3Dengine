package math;

public class Triangle {
	
	public Vec3d[] p;
	public float lightDotTriangle;

	public Triangle() {
		p = new Vec3d[3];
		p[0] = new Vec3d(0,0,0);
		p[1] = new Vec3d(0,0,0);
		p[2] = new Vec3d(0,0,0);
		
	}
	
	public void addPoints(Vec3d p , int pos) {
		this.p[pos] = p;
	}
	
	public static int compareTo(Triangle t1,Triangle t2) {
		float z1 = (t1.p[0].z + t1.p[1].z + t1.p[2].z) / 3.0f;
		float z2 = (t2.p[0].z + t2.p[1].z + t2.p[2].z) / 3.0f;
		if(z1 < z2) {
			return - 1;
		}else {
			return 1;
		}
	}
	
}
