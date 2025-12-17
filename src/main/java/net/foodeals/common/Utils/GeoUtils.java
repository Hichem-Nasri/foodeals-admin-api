package net.foodeals.common.Utils;

import net.foodeals.common.valueOjects.Coordinates;

public class GeoUtils {

	private static final double EARTH_RADIUS_KM = 6371.0;

	public static double haversine(Coordinates from, Coordinates to) {
		double lat1 = Math.toRadians(from.latitude());
		double lon1 = Math.toRadians(from.longitude());
		double lat2 = Math.toRadians(to.latitude());
		double lon2 = Math.toRadians(to.longitude());

		double dlat = lat2 - lat1;
		double dlon = lon2 - lon1;

		double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);

		return EARTH_RADIUS_KM * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
	}

	public static String estimateDuration(double distanceKm, String vehicleType) {
		double speedKmH = switch (vehicleType.toLowerCase()) {
		case "motorcycle" -> 30;
		case "bicycle" -> 15;
		default -> 20; // fallback
		};

		double durationMinutes = (distanceKm / speedKmH) * 60;
		return String.format("%.0f minutes", durationMinutes);
	}
}
