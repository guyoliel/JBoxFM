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

import fm.jbox.jboxfm.adapters.SimplePartyAdapter;
import fm.jbox.jboxfm.models.Party;

public class AsyncPartyListLoader extends AsyncTask<String, Void, List<Party>> {
    private String tag = "AsyncParty";
    private Context myContext;
    private ListView lv;

    public AsyncPartyListLoader(Context ctx,ListView lv)
    {
        this.myContext = ctx;
        this.lv = lv;
    }

    @Override
    protected void onPostExecute(List<Party> result) {
        super.onPostExecute(result);
        ListAdapter adpt = new SimplePartyAdapter(this.myContext,result);
        Log.i(tag, "before Set");
        this.lv.setAdapter(adpt);
        Log.i(tag, "After Set");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Party> doInBackground(String... params) {
        List<Party> result = new ArrayList<Party>();
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
            JSONObject user = obj.getJSONObject("user");
            JSONArray arr = user.getJSONArray("parties");
            for (int i=0; i < arr.length(); i++) {
                result.add(convertParty(arr.getJSONObject(i)));
            }

            return result;
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    private Party convertParty(JSONObject obj) throws JSONException {

        String id = obj.getString("id");
        String name = obj.getString("name");
        String user_id = obj.getString("user_id");
        String created_at = obj.getString("created_at");
        String updated_at = obj.getString("updated_at");

        return new Party(id,name,user_id,created_at,updated_at);
    }

}
