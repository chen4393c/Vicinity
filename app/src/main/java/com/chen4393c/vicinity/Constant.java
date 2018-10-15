package com.chen4393c.vicinity;

public class Constant {

    public static final String EMAIL = "mailto:chen4393c@gmail.com";
    public static final String GIT_HUB = "https://github.com/chen4393c/Vicinity";
    private static final String APP_URL = "https://github.com/chen4393c/Vicinity";
    private static final String DESIGNED_BY = "Designed by Chaoran Chen";
    public static final String SHARE_CONTENT =
            "An user-generated content Android social media app:\n" + APP_URL + "\n- " + DESIGNED_BY;

    public static int[] mapThemes = {
            R.raw.theme_standard,
            R.raw.theme_silver,
            R.raw.theme_retro,
            R.raw.theme_dark,
            R.raw.theme_night,
            R.raw.theme_aubergine
    };

    public static int[] reportEventDrawableIds = {
            R.drawable.event_work,
            R.drawable.event_music,
            R.drawable.event_weather,
            R.drawable.event_shopping,
            R.drawable.event_movie,
            R.drawable.event_travel,
            R.drawable.event_working_out,
            R.drawable.event_party,
            R.drawable.event_note
    };

    public static final double LOC_SHAKE = 0.002;
}
