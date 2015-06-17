package fm.jbox.jboxfm.tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fm.jbox.jboxfm.adapters.SimplePartyAdapter;
import fm.jbox.jboxfm.models.Party;

public class GetPartyTask extends AsyncTask<String,Void,JSONObject> {

    private String url;

    public GetPartyTask()
    {
        this.url = "https://music-hasalon-api.herokuapp.com/parties/";
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        try {
            URL u = new URL(url+params[0]);

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

            Log.i("GetParty", jsonresult);

            return new JSONObject(jsonresult);

        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }



}
