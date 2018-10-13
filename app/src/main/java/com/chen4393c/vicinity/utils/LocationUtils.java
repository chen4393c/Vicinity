package com.chen4393c.vicinity.utils;

import android.location.Location;

public class LocationUtils {
    /**
     * Get distance between two locations
     * @param currentLatitude current latitude
     * @param currentLongitude current longitude
     * @param destLatitude destination latitude
     * @param destLongitude destination longitude
     * @return the distance between two locations by miles
     */
    public static double getDistanceBetweenTwoLocations(double currentLatitude,
                                                        double currentLongitude,
                                                        double destLatitude,
                                                        double destLongitude) {

        Location currentLocation = new Location("CurrentLocation");
        currentLocation.setLatitude(currentLatitude);
        currentLocation.setLongitude(currentLongitude);
        Location destLocation = new Location("DestLocation");
        destLocation.setLatitude(destLatitude);
        destLocation.setLongitude(destLongitude);
        double distance = currentLocation.distanceTo(destLocation);

        double inches = (39.370078 * distance);
        double miles = inches / 63360;
//        double km = distance / 1000;
        return miles;
    }
}
