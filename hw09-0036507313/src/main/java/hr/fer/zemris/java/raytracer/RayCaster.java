package hr.fer.zemris.java.raytracer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Program koji simulira 3D scenu sastavljenu od izvora svjetlosti
 * i 3D objekata na osnovu pracenja presjecista zraka svjetlosti
 * tipa {@link Ray} i 3D predmeta tipa {@link GraphicalObject}.
 * @author Luka Dragutin
 *
 */
public class RayCaster {

	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	protected final static double CONSTANT = 0.001;

	/**
	 * Stvara i vraca primjerak objekta koji implementira sucelje {@link IRayTracerProducer}
	 * @see IRayTracerProducer
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {

			@SuppressWarnings("unused")
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer, AtomicBoolean cancel) {
				System.out.println("Započinjem izračune...");
				
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				Point3D zAxis = view.sub(eye).normalize();
				Point3D yAxis = viewUp.normalize().sub(zAxis.scalarMultiply(zAxis.scalarProduct(viewUp.normalize())));
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();
				Point3D screenCorner = view.normalize().sub(xAxis.scalarMultiply(horizontal / 2.0))
						.add(yAxis.scalarMultiply(vertical / 2.0));
				Scene scene = RayTracerViewer.createPredefinedScene();
				
				short[] rgb = new short[3];
				int offset = 0;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner
								.add(xAxis.scalarMultiply(x / (double) (width - 1) * horizontal))
								.sub(yAxis.scalarMultiply(y / (double) (height - 1) * vertical));
						Ray ray = Ray.fromPoints(eye, screenPoint);
						
						tracer(scene, ray, rgb);
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						offset++;
					}
				}
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}

			/**
			 * Gleda sjece li se zraka svjetlosti sa nekim od objekata
			 * u sceni te ovisno o tome ga boja(ako ima presjeka) ili ne boja(ako nema presjeka ili
			 * je u sjeni drugog objekta) tako da u polje predano argumentom zapise int vrijednosti
			 * boja objekta koji je zraka pogodila
			 * @param scene Model scene
			 * @param ray Zraka svjetlosti koju pratimo
			 * @param rgb Polje u koje biljezimo boje
			 */
			private void tracer(Scene scene, Ray ray, short[] rgb) {
				RayIntersection closest = findClosestIntersection(scene, ray);
				
				if (closest == null) {
					Arrays.fill(rgb, (short) 0);
				} else {
					determineColorFor(scene, rgb, closest, ray);
				}
			}

			
			/**
			 * Pronalazi prvo presjeciste zrake sa objektima u sceni te 
			 * vraca opis presjeka ili null ako nema presjeka
			 * @param scene Model scene
			 * @param ray Zraka svjetlosti
			 * @return RayIntersection objekt koji opisuje tocku presjeka
			 */
			private RayIntersection findClosestIntersection(Scene scene, Ray ray) {
				RayIntersection closest = null;
				
				for (GraphicalObject g : scene.getObjects()) {
					RayIntersection i = g.findClosestRayIntersection(ray);
					if (i == null) {
						continue;
					} else {
						closest = closest != null && closest.getDistance() < i.getDistance() ? closest : i;
					}
				}
				return closest;
			}

			/**
			 * Ovisno o okruzenju presjeka zrake i objekta, boja sliku objekta u sceni
			 * Ako izmedu presjeka i izvora svjetlosti se nalazi neki drugi objekt onda
			 * ga oboja samo ambijentnim osvjetljenjem, a inace racuna rgb komponente osvjetljenje na 
			 * osnovu difuznih i reflektivnih komponenata svjetlosti
			 * @param scene Model scene
			 * @param rgb Polje za spremanje vrijednosti boja piksela na slici
			 * @param intersection Presjek zrake i objekta cije okruzenje gledamo(bojamo)
			 * @param eyeRay Zraka koja se sjekla sa objektom
			 */
			private void determineColorFor(Scene scene, short[] rgb, RayIntersection intersection, Ray eyeRay) {
				Arrays.fill(rgb, (short) 15);

				for (var light : scene.getLights()) {
					Ray ray = Ray.fromPoints(light.getPoint(), intersection.getPoint());

					RayIntersection close = findClosestIntersection(scene, ray);

					double distance = intersection.getPoint().sub(light.getPoint()).norm();
					if (close == null || close.getDistance() + 0.01 < distance) {
						continue;
					} else {

						Point3D v = eyeRay.start.sub(intersection.getPoint()).normalize();
						Point3D ri = light.getPoint().sub(intersection.getPoint()).normalize();
						Point3D rn = intersection.getNormal();
						Point3D r = rn.scalarMultiply(ri.scalarProduct(rn) * 2).sub(ri).normalize();

						double cosDifusive = Math.max(ri.scalarProduct(rn), 0);
						double cosReflective = Math.pow(Math.max(r.scalarProduct(v), 0), intersection.getKrn());

						rgb[0] += light.getR()
								* (intersection.getKdr() * cosDifusive + intersection.getKrr() * cosReflective);
						rgb[1] += light.getG()
								* (intersection.getKdg() * cosDifusive + intersection.getKrg() * cosReflective);
						rgb[2] += light.getB()
								* (intersection.getKdb() * cosDifusive + intersection.getKrb() * cosReflective);
					}
				}

			}
		};
	}

}
