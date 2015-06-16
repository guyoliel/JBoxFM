package fm.jbox.jboxfm.activities;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SearchView;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.widget.VideoView;

import org.json.JSONException;

import fm.jbox.jboxfm.R;
import fm.jbox.jboxfm.dialogs.DeletePartyDialog;
import fm.jbox.jboxfm.models.Request;
import fm.jbox.jboxfm.tasks.AsyncRequestListLoader;
import fm.jbox.jboxfm.tasks.DeleteTask;
import fm.jbox.jboxfm.tasks.GetVideoUrlTask;


public class PartyActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener,DeletePartyDialog.DeleteDialogListener {

    private ListView lv;
    private Context ctx;
    private String id;
    private SwipeRefreshLayout swipeContainer;
    private String userId;
    private VideoView player;
    private String url;
    private boolean isPlaying=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);

        ctx = this;

        Bundle mainData = getIntent().getExtras();
        id = mainData.getString("partyId");
        setTitle(mainData.getString("partyName"));
        userId = mainData.getString("partyUserId");
        lv = (ListView) findViewById(R.id.requestList);
        player = (VideoView)findViewById(R.id.mainPlayer);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    if(lv.getCount()>0) {
                        DeleteTask d = new DeleteTask();
                        String d_result = d.execute("http://music-hasalon-api.herokuapp.com/requests/" + ((Request) (lv.getItemAtPosition(0))).getId()).get();
                    }
                        AsyncRequestListLoader task = new AsyncRequestListLoader(ctx, lv);
                    String t_string = task.execute("http://music-hasalon-api.herokuapp.com/parties/" + id).get().toString();
                    if (lv.getCount() >1) {
                        Request item = (Request) lv.getItemAtPosition(1);
                        Log.i("ITEM", item.toString());
                        url = item.getUrl();
                        Log.i("URL", url);
                        new GetVideoUrlTask(player, PartyActivity.this).execute(url);
                    }
                    else
                    {isPlaying = false;}
                }catch (InterruptedException|ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        AsyncRequestListLoader task = new AsyncRequestListLoader(this,lv);
        task.execute("http://music-hasalon-api.herokuapp.com/parties/"+id);
        try {
            if (userId.equals(MyApp.userJson.getString("id"))) {
                player.setVisibility(View.VISIBLE);
            }
        }catch (JSONException j){
            j.printStackTrace();
        }

        // get action bar
        ActionBar actionBar = getActionBar();

        // Enabling Up / Back navigation
        try{
            actionBar.setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(ctx,RequestActivity.class);

                        TextView url = (TextView) view.findViewById(R.id.requestUrl);
                        i.putExtra("requestUrl",url.getText());

                        TextView created = (TextView) view.findViewById(R.id.requestCreated);
                        i.putExtra("requestCreated",created.getText());

                        TextView user = (TextView) view.findViewById(R.id.requestUser);
                        i.putExtra("requestUser",user.getText());

                        TextView title = (TextView) view.findViewById(R.id.requestTitle);
                        i.putExtra("requestTitle",title.getText());

                        TextView likes = (TextView) view.findViewById(R.id.requestLikes);
                        i.putExtra("requestLikes",likes.getText());

                        TextView requestId = (TextView) view.findViewById(R.id.requestId);
                        i.putExtra("requestId",requestId.getText());
                        startActivity(i);
                    }
                }

        );

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
               // android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (lv==null || lv.getChildCount()==0) ? 0 : lv.getChildAt(0).getTop();
                swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

    }

    @Override
    public void onRefresh(){
       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AsyncRequestListLoader task = new AsyncRequestListLoader(ctx,lv);
                task.execute("http://music-hasalon-api.herokuapp.com/parties/"+id);
                swipeContainer.setRefreshing(false);
            }
        },5000);
        if(lv.getCount()>0)
        {
            if(!isPlaying) {
                isPlaying = true;
                Request item = (Request) lv.getItemAtPosition(0);
                Log.i("ITEM", item.toString());
                this.url = item.getUrl();
                Log.i("URL", this.url);
                new GetVideoUrlTask(player, this).execute(this.url);
            }
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            intent.putExtra("partyId",id);
        }
        super.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_party, menu);
        MenuItem delete = (MenuItem) menu.findItem(R.id.action_delete_party);
        try {
            if (userId.equals(MyApp.userJson.getString("id"))) {
                delete.setVisible(true);
            }
        }catch (JSONException j){
            j.printStackTrace();
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        ComponentName cn = new ComponentName(this,SearchResultsActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        setSearchIcons(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    private void setSearchIcons(SearchView searchView){
        try{

            Field searchField = SearchView.class.getDeclaredField("mCloseButton");
            searchField.setAccessible(true);
            ImageView closeBtn = (ImageView) searchField.get(searchView);
            closeBtn.setImageResource(R.drawable.ic_action_cancel);

            int searchImgId = getResources().getIdentifier("android:id/search_button",null,null);
            ImageView searchButton = (ImageView) searchView.findViewById(searchImgId);
            searchButton.setImageResource(R.drawable.ic_action_search);

            int searchSrcTextId =getResources().getIdentifier("android:id/search_src_text",null,null);
            EditText searchEditText = (EditText) searchView.findViewById(searchSrcTextId);
            searchEditText.setHint("Request a song!");
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }catch (IllegalAccessException a){
            a.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        if (id == R.id.action_delete_party) {
            showDeleteDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDeleteDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new DeletePartyDialog();
        dialog.show(getFragmentManager(),"DeletePartyDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        Log.i("DialogClick","POSITIVE");
        new DeleteTask().execute("http://music-hasalon-api.herokuapp.com/parties/"+id);
        //Intent i = new Intent(ctx,MainActivity.class);
       // startActivity(i);
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        Log.i("DialogClick","NEGATIVE");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
