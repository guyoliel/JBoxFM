package fm.jbox.jboxfm.tasks;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetVideoUrlTask extends AsyncTask<String, Void, JSONObject> {

    VideoView player;
    Context ctx;
    public GetVideoUrlTask(VideoView player,Context ctx){
        this.player = player;
        this.ctx = ctx;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            String videoParser = "http://52.1.103.114:8989/api/info?url=";
            String videoUrl = params[0].split("&feature")[0];
            Log.i("PARAMS",videoUrl);

            URL u = new URL(videoParser+videoUrl);

            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream is = conn.getInputStream();
            // Read the stream
            //  byte[] b = new byte[1024];
            //  ByteArrayOutputStream baos = new ByteArrayOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            String jsonresult = sb.toString();
            /*
            while ( is.read(b) != -1)
            baos.write(b);*/

            return new JSONObject(jsonresult);
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject){
        super.onPostExecute(jsonObject);
        try {
            String videoUrl = jsonObject.getString("direct_url");
            Log.i("URL",videoUrl);
            Log.i("Context",ctx.toString());
            Log.i("PLayer",player.toString());
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    ctx);
            mediacontroller.setAnchorView(player);
            Uri video = Uri.parse(videoUrl);
            player.setMediaController(mediacontroller);
            player.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                Log.i("Prepared","ready to start");
                player.start();
            }
        });
    }
}
