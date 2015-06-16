package fm.jbox.jboxfm.tasks;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.renderscript.Sampler;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import fm.jbox.jboxfm.activities.MyApp;
import fm.jbox.jboxfm.models.Video;
import com.facebook.Session;

public class CreateRequestTask extends AsyncTask<String, Void, Void> {

    String partyId;
    Video v;

    public CreateRequestTask(Video video,String partyId){
        this.v = video;
        this.partyId = partyId;
    }

    @Override
    protected Void doInBackground(String... params) {

        try {
            AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
            HttpResponse response;
            JSONObject json = new JSONObject();
            JSONObject request = new JSONObject();
            request.put("title", v.getTitle());
            request.put("author",v.getAuthor());
            request.put("url",v.getUrl());
            request.put("party_id",Integer.parseInt(partyId));
            request.put("thumbnail", v.getThumbnail());
            json.put("request", request);
            json.put("user_access_token", MyApp.userJson.getString("access_token"));
            Log.i("HttpResponse",json.toString());

            StringEntity se = new StringEntity(json.toString());
            HttpPost post = new HttpPost(params[0]);
            se.setContentType("application/json");
            post.setHeader(HTTP.CONTENT_TYPE, "application/json");
            post.setHeader("Accept", "application/json");
            post.setEntity(se);
            response = client.execute(post);

            if(response != null) {
                InputStream is = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                String result = sb.toString();
                Log.i("HttpResponse",result);
            }
            client.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
