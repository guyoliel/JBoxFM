package fm.jbox.jboxfm.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import fm.jbox.jboxfm.R;
import fm.jbox.jboxfm.adapters.SimpleRequestAdapter;
import fm.jbox.jboxfm.models.Like;
import fm.jbox.jboxfm.models.Request;
import fm.jbox.jboxfm.models.User;

public class AsyncRequestListLoader extends AsyncTask<String, Void, List<Request>>
{
    private String tag = "AsyncParty";
    private Context myContext;
    private ListView lv;
   // private VideoView player;

    public AsyncRequestListLoader(Context ctx, ListView lv)
    {
        this.myContext = ctx;
        this.lv = lv;
       // this.player = player;
    }

    @Override
    protected void onPostExecute(List<Request> result) {
        super.onPostExecute(result);
        ListAdapter adpt = new SimpleRequestAdapter(this.myContext,result);
        Log.i(tag, "before Set Request");
        this.lv.setAdapter(adpt);
        Log.i(tag, "After Set");
        /*if(lv.getCount()>0) {
            Request item = (Request)lv.getItemAtPosition(0);
            String url = item.getUrl();
            Log.i("URL",url);
            //new GetVideoUrlTask(this.player,this.myContext).execute(url);
        }*/
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Request> doInBackground(String... params) {
        List<Request> result = new ArrayList<Request>();
        try {
            URL u = new URL(params[0]);

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

            Log.i(tag,jsonresult);

            JSONObject obj = new JSONObject(jsonresult);
            JSONObject party = obj.getJSONObject("party");
            JSONArray requestArr = party.getJSONArray("requests");

            for (int i=0; i < requestArr.length(); i++) {
                Log.i("ListLoader","hi "+i);
                result.add(convertRequest(requestArr.getJSONObject(i)));
            }

            return result;
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    private Request convertRequest(JSONObject obj) throws JSONException {

        String id = obj.getString("id");
        String author = obj.getString("author");
        String party_id = obj.getString("party_id");
        String created_at = obj.getString("created_at");
        String title = obj.getString("title");
        String thumbnail = obj.getString("thumbnail");
        String url = obj.getString("url");
        User user = convertUser(obj.getJSONObject("user"));
        Like[] likes = convertLikesArray(obj.getJSONArray("likes"));

        return new Request(id,author,party_id,thumbnail,url,created_at,title,user,likes);
    }

    private User convertUser(JSONObject obj) throws JSONException {

        String id = obj.getString("id");
        String name = obj.getString("name");
        String email = obj.getString("email");
        String thumbnail = obj.getString("thumbnail");

        return new User(id,name,email,thumbnail);
    }

    private Like[] convertLikesArray(JSONArray arr) throws JSONException {

        Like[] likes = new Like[arr.length()];
        for (int i = 0; i < arr.length(); i++) {
            JSONObject like = arr.getJSONObject(i);
            likes[i] = new Like(like.getString("id"),convertUser(like.getJSONObject("user")));
        }
        return likes;
    }
}
