package fm.jbox.jboxfm.activities;

import android.app.Application;

import org.json.JSONObject;

import java.net.URL;

public class MyApp extends Application {

    private static MyApp singleton;
    public static JSONObject userJson;
    public static String URL;

    public static MyApp getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }
}

