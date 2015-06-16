package fm.jbox.jboxfm.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

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

import fm.jbox.jboxfm.adapters.SimpleSearchAdapter;
import fm.jbox.jboxfm.models.Video;

public class AsyncSearchRequestListLoader extends AsyncTask<String, Void, List<Video>> {

    private String tag = "AsyncSearch";
    private Context myContext;
    private ListView lv;

    public AsyncSearchRequestListLoader(Context ctx,ListView lv)
    {
        this.myContext = ctx;
        this.lv = lv;
    }

    @Override
    protected void onPostExecute(List<Video> result) {
        super.onPostExecute(result);
        ListAdapter adpt = new SimpleSearchAdapter(this.myContext,result);
        Log.i(tag, "before Set Request");
        this.lv.setAdapter(adpt);
        Log.i(tag, "After Set");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Video> doInBackground(String... params) {
        List<Video> result = new ArrayList<>();
        try {
            URL u = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
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

            Log.i(tag,jsonresult);

            JSONObject obj = new JSONObject(jsonresult);
            JSONArray videos = obj.getJSONArray("items");

            for (int i=0; i < videos.length(); i++) {
                result.add(convertVideo(videos.getJSONObject(i)));
            }

            return result;
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    private Video convertVideo(JSONObject obj) throws JSONException {
        if (obj.getJSONObject("id").has("videoId")) {
            JSONObject snippet = obj.getJSONObject("snippet");
            String author = snippet.getString("channelTitle");
            String title = snippet.getString("title");
            String thumbnail = snippet.getJSONObject("thumbnails").getJSONObject("default").getString("url");
            String url = obj.getJSONObject("id").getString("videoId");
            url = "http://www.youtube.com/watch?v=" + url;
            return new Video(title, author, url, thumbnail);
        }
        else
            return null;
    }

}
