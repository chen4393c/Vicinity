package com.chen4393c.vicinity.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
    /**
     * Transform time unit to different time format
     * @param millis time stamp
     * @return formatted string of time stamp
     */
    public static String timeTransformer(long millis) {
        long currenttime = System.currentTimeMillis();
        long diff = currenttime - millis;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long days = TimeUnit.MILLISECONDS.toDays(diff);

        if (seconds < 60) {
            return seconds + " seconds ago";
        } else if (minutes < 60) {
            return minutes + " minutes ago";
        } else if (hours < 24) {
            return hours + " hours ago";
        } else {
            return days + " days ago";
        }
    }
}
