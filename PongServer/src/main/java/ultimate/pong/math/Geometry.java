package ultimate.pong.math;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public abstract class Geometry
{
	private Geometry()
	{

	}
	
	public static double getSectorSize(int numberOfCorners)
	{
		return Math.PI * 2 / numberOfCorners;
	}

	public static Vector getStartCornerPoint(int numberOfCorners, int sectorIndex)
	{
		double cornerPosition = (sectorIndex - 0.5) * getSectorSize(numberOfCorners);
		return direction(cornerPosition);
	}

	public static Vector getEndCornerPoint(int numberOfCorners, int sectorIndex)
	{
		double cornerPosition = (sectorIndex + 0.5) * getSectorSize(numberOfCorners);
		return direction(cornerPosition);
	}


	public static Vector getCenterPoint(int numberOfCorners, int sectorIndex)
	{
		Vector start = getStartCornerPoint(numberOfCorners, sectorIndex);
		Vector end = getEndCornerPoint(numberOfCorners, sectorIndex);
		return center(start, end);
	}
	
	public static int getSectorIndex(int numberOfCorners, Vector v)
	{
		double sectorSize = getSectorSize(numberOfCorners);
		double pointAngle = angle(v);
		if(pointAngle < 0)
			pointAngle += (Math.PI*2);
		return (int) Math.floor((pointAngle  + sectorSize/2.0) / sectorSize);
	}
	
	public static int getPlayerSectorIndex(int numberOfCorners, Vector v)
	{
		double sectorSize = getSectorSize(numberOfCorners);
		double pointAngle = angle(v) + sectorSize;
		if(pointAngle < 0)
			pointAngle += Math.PI*2;
		return (int) Math.floor(pointAngle / (2* sectorSize));
	}
	
	public static double angle(Vector v)
	{
		return Math.atan2(v.y, v.x);
	}
	
	public static Vector direction(double angle)
	{
		return new Vector(Math.cos(angle), Math.sin(angle));
	}

	public static Vector center(Vector v1, Vector v2)
	{
		return new Vector((v1.x + v2.x) / 2.0, (v1.y + v2.y) / 2.0);
	}

	public static double distance(Vector v1, Vector v2)
	{
		return Math.sqrt((v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y) * (v1.y - v2.y));
	}
	
	public static double distanceFromCenter(Vector v)
	{
		return Math.sqrt(v.x * v.x + v.y * v.y);
	}
	
	public static Vector intersect(Vector v11, Vector v12, Vector v21, Vector v22)
	{
		// line 1 --> (x(s),y(s)) = (x11 + s*(x12-x11),y11 + s*(y12-y11))
		// line 2 --> (x(t),y(t)) = (x21 + t*(x22-x21),y21 + t*(y22-y21))
		// ==>
		// equation 1 --> s*(x12-x11) - t*(x22-x21) = x21-x11
		// equation 2 --> s*(y12-y11) - t*(y22-y21) = y21-y11
		// ==> Cramer's rule
		double a1 = v12.x - v11.x;
		double a2 = v12.y - v11.y;
		double b1 = -(v22.x - v21.x);
		double b2 = -(v22.y - v21.y);
		double c1 = v21.x - v11.x;
		double c2 = v21.y - v11.y;
		double quotient = a1*b2 - a2*b1;
		
//		System.out.println("a1=" + a1);
//		System.out.println("a2=" + a2);
//		System.out.println("b1=" + b1);
//		System.out.println("b2=" + b2);
//		System.out.println("c1=" + c1);
//		System.out.println("c2=" + c2);
//		System.out.println("quotient=" + quotient);
		
		if(quotient == 0.0) // lines are parallel
			return null; // no intersection 

		double s =  (c1*b2 - c2*b1) / quotient;
		double t =  (a1*c2 - a2*c1) / quotient;

		if(s < 0.0 || s > 1.0) 
			return null; // no intersection
		if(t < 0.0 || t > 1.0) 
			return null; // no intersection
		
		double xs1 = v11.x + s*(v12.x - v11.x);
		double ys1 = v11.y + s*(v12.y - v11.y);
//		System.out.println("intersection 1: " + xs1 + " , " + ys1);
//		double xs2 = v21.x + t*(v22.x - v21.x);
//		double ys2 = v21.y + t*(v22.y - v21.y);
//		System.out.println("intersection 2: " + xs2 + " , " + ys2);
		
		return new Vector(xs1, ys1);
	}
	
	public static Set<Vector> intersect(Polygon p1, Polygon p2)
	{
		// use set to prevent duplicates
		Set<Vector> intersections = new TreeSet<Vector>();
		
		List<Vector> points1 = p1.getPoints();
		List<Vector> points2 = p2.getPoints();
		
		Vector p11, p12, p21, p22;
		Vector intersection;
		for(int i1 = 0; i1 < points1.size(); i1++)
		{
			p11 = points1.get(i1);
			p12 = points1.get((i1+1) % points1.size()); // closing edge from last to first point
			
			for(int i2 = 0; i2 < points2.size(); i2++)
			{
				p21 = points2.get(i2);
				p22 = points2.get((i2+1) % points2.size()); // closing edge from last to first point
				
				intersection = intersect(p11, p12, p21, p22);
				System.out.println(intersection);
				if(intersection != null)
					intersections.add(intersection);
			}
		}
		
		return intersections;
	}
}
