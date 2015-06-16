package fm.jbox.jboxfm.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.Session;

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

public class AsyncServerLogin extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... params) {

        try {
            URL u = new URL(params[0]);

            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            JSONObject json = new JSONObject();
            json.put("access_token", Session.getActiveSession().getAccessToken());
            json.put("expires_in", Session.getActiveSession().getExpirationDate());
            Log.i("JSON",json.toString());
            StringEntity se = new StringEntity(json.toString(),"UTF-8");
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
                MyApp.userJson = new JSONObject(result);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
