package com.chen4393c.vicinity;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public static String username = null;
    public static String address = null;

    public static final String POLICE = "Police";
    public static final String TRAFFIC = "Traffic";
    public static final String NO_ENTRY = "No Entry";
    public static final String NO_PARKING = "No Parking";
    public static final String SECURITY_CAMERA = "Security Camera";
    public static final String HEADLIGHT = "Headlight";
    public static final String SPEEDING = "Speeding";
    public static final String CONSTRUCTION = "Construction";
    public static final String SLIPPERY = "Slippery";

    public static final Map<String, Integer> trafficMap = new HashMap<String, Integer>() {};

    static {
        trafficMap.put(POLICE, R.drawable.policeman);
        trafficMap.put(TRAFFIC, R.drawable.traffic);
        trafficMap.put(NO_PARKING, R.drawable.no_parking);
        trafficMap.put(NO_ENTRY, R.drawable.no_entry);
        trafficMap.put(SECURITY_CAMERA, R.drawable.security_camera);
        trafficMap.put(HEADLIGHT, R.drawable.lights);
        trafficMap.put(SPEEDING, R.drawable.speeding);
        trafficMap.put(CONSTRUCTION, R.drawable.construction);
        trafficMap.put(SLIPPERY, R.drawable.slippery);
    }
}
