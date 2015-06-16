package fm.jbox.jboxfm.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fm.jbox.jboxfm.R;
import fm.jbox.jboxfm.models.Video;
import fm.jbox.jboxfm.tasks.AsyncSearchRequestListLoader;
import fm.jbox.jboxfm.tasks.CreateRequestTask;

public class SearchResultsActivity extends ActionBarActivity {

    TextView partyId;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        partyId = (TextView) findViewById(R.id.partyId);

        lv = (ListView) findViewById(R.id.searchRequestList);

        handleIntent(getIntent());

        ActionBar actionBar = getActionBar();

        try{
            actionBar.setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView title = (TextView) view.findViewById(R.id.requestTitle);
                TextView author = (TextView) view.findViewById(R.id.requestAuthor);
                TextView url = (TextView) view.findViewById(R.id.requestUrl);
                TextView thumbnail = (TextView) view.findViewById(R.id.requestThumbnailText);

                Video vid = new Video(title.getText().toString(),author.getText().toString(),
                        url.getText().toString(),thumbnail.getText().toString());
                CreateRequestTask c = new CreateRequestTask(vid,partyId.getText().toString());
                c.execute("https://music-hasalon-api.herokuapp.com//requests");
                finish();
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent){
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            setTitle("Results For:\"" + query + "\"");
            partyId.setText(intent.getStringExtra("partyId"));

            query = query.replaceAll(" ", "-");
            Log.i("query",query);
            try {
                query = URLEncoder.encode(query, "UTF-8");
            }catch(UnsupportedEncodingException ex){ex.printStackTrace();}

            new AsyncSearchRequestListLoader(this,lv).execute("http://music-hasalon-api.herokuapp.com/parties/"
                    +intent.getStringExtra("partyId")+"/search?songpull="+query);
            Log.i("URL","http://music-hasalon-api.herokuapp.com/parties/"
                    +intent.getStringExtra("partyId")+"/search?songpull="+query);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
