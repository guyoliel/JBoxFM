package fm.jbox.jboxfm.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import fm.jbox.jboxfm.activities.MyApp;

/**
 * Created by GuyOliel on 2/14/15.
 */
public class LikeTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        try{
            URL u = new URL(params[0]);

            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            JSONObject json = new JSONObject();
            json.put("user_access_token", MyApp.userJson.getString("access_token"));
            Log.i("LikeJson", json.toString());
            StringEntity se = new StringEntity(json.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
            HttpPost post = new HttpPost(u.toURI());
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
                Log.i("HttpResponse", result);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}
