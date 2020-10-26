package hr.fer.zemris.java.raytracer.model;

/**
 * Reprezentacija 3D sfere.
 * Odredena je središtem tipa {@link Point3D} i radiusom tipa double.
 * 
 * @author Luka Dragutin
 *
 */
public class Sphere extends GraphicalObject {

	/**
	 * Srediste sfere
	 */
	private Point3D center;
	
	/** Polumjer sfere */
	private double radius;
	
	/**
	 * Koeficijent difuzivne komponente svjetlosti crvene boje
	 */
	private double kdr;
	
	/**
	 * Koeficijent difuzivne komponente svjetlosti zelene boje
	 */
	private double kdg;
	
	/**
	 * Koeficijent difuzivne komponente svjetlosti plave boje
	 */
	private double kdb;
	
	/**
	 * Koeficijent rekleksivne komponente svjetlosti crvene boje
	 */
	private double krr;

	/**
	 * Koeficijent rekleksivne komponente svjetlosti zelene boje
	 */
	private double krg;
	
	/**
	 * Koeficijent rekleksivne komponente svjetlosti plave boje
	 */
	private double krb;
	
	/** Indeks hrapavosti povrsine */
	private double krn;

	
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D toCenter = ray.start.sub(center);
		
		double a = 1;
		double b = ray.direction.scalarProduct(toCenter) * 2;
		double c = toCenter.norm() * toCenter.norm() - radius * radius;
		
		double D = b*b - 4*a*c;
		if(D < 0) {
			return null;
		}
		
		double cross1 = (-b - Math.sqrt(D)) / 2*a;
		double cross2 = (-b + Math.sqrt(D)) / 2*a;
		double closer;
		boolean outer;
		
		if(cross1 < 0 && cross2 < 0) {
			return null;
		}
		else if(cross1 < 0) {
			closer = cross2;
			outer = false;
		}
		else {
			closer = cross1 <= cross2 ? cross1 : cross2;
			outer = true;
		}
		
		Point3D point = ray.start.add(ray.direction.scalarMultiply(closer));
		return new RayIntersectionSphere(point, closer, outer);
	}

	/**
	 * Implementacija presjecista izmedu zrake svjetla i sfere.
	 * @see RayIntersection
	 * @author Luka Dragutin
	 *
	 */
	private class RayIntersectionSphere extends RayIntersection {

		protected RayIntersectionSphere(Point3D point, double distance, boolean outer) {
			super(point, distance, outer);
		}


		@Override
		public Point3D getNormal() {
			return this.getPoint().sub(center).normalize();
			
		}

		@Override
		public double getKdr() {
			return kdr;
		}

		@Override
		public double getKdg() {
			return kdg;
		}

		@Override
		public double getKdb() {
			return kdb;
		}

		@Override
		public double getKrr() {
			return krr;
		}

		@Override
		public double getKrg() {
			return krg;
		}

		@Override
		public double getKrb() {
			return krb;
		}

		@Override
		public double getKrn() {
			return krn;
		}

	}

}
