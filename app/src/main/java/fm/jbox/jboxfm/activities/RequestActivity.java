package fm.jbox.jboxfm.activities;

import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.text.TextUtils.TruncateAt;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import fm.jbox.jboxfm.R;
import fm.jbox.jboxfm.models.Like;
import fm.jbox.jboxfm.models.User;
import fm.jbox.jboxfm.tasks.DownloadImageTask;
import fm.jbox.jboxfm.tasks.GetVideoUrlTask;
import fm.jbox.jboxfm.adapters.SimpleLikeAdapter;
import fm.jbox.jboxfm.tasks.LikeTask;
import fm.jbox.jboxfm.tasks.UnLikeTask;


public class RequestActivity extends ActionBarActivity {

    private ImageButton like;
    private boolean isLiked;
    private VideoView player;
    private Like[] likeArr;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        Bundle mainData = getIntent().getExtras();

        String createdAt = mainData.getString("requestCreated");
        String url = mainData.getString("requestUrl");
        String user = mainData.getString("requestUser");
        String likes = mainData.getString("requestLikes");
        final String id = mainData.getString("requestId");
        likeArr = new Like[0];
        Log.i("TryList",likes);
        setTitle(mainData.getString("requestTitle"));
        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.custom_title, null, false);
        //if you need to customize anything else about the text, do it here.
        //I'm using a custom TextView with a custom font in my layout xml so all I need to do is set title
        TextView title = ((TextView)v.findViewById(R.id.bar_title));
        title.setText(this.getTitle());
        title.setEllipsize(TruncateAt.MARQUEE);
        title.setMarqueeRepeatLimit(-1);
        title.setFocusable(true);
        title.setFocusableInTouchMode(true);
        title.requestFocus();
        //assign the view to the actionbar
        this.getSupportActionBar().setCustomView(v);

        try{
            JSONObject jUser = new JSONObject(user);
            User u = convertUser(jUser);
            TextView userName = (TextView) findViewById(R.id.userName);
            userName.setText(u.getName());

            Log.i("thumbnail",u.getThumbnail());
            new DownloadImageTask((ImageView) findViewById(R.id.userThumbnail)).execute(u.getThumbnail());


            TextView created = (TextView) findViewById(R.id.createdAt);
            SimpleDateFormat serverformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date past_time = serverformat.parse(createdAt);
            Date now = new Date();
            if(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past_time.getTime())>0 && TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past_time.getTime())<60 && TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past_time.getTime())!=1)
                created.setText(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past_time.getTime()) + " minutes ago");
            else if(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past_time.getTime())==1)
                created.setText("A minute ago");
            else if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past_time.getTime())==1)
                created.setText("A hour ago");
            else if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past_time.getTime())>1 && TimeUnit.MILLISECONDS.toHours(now.getTime() - past_time.getTime())<24)
                created.setText(TimeUnit.MILLISECONDS.toHours(now.getTime() - past_time.getTime()) + " hours ago");
            else if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past_time.getTime())==24)
                created.setText("A day ago");
            else if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past_time.getTime())>24)
                created.setText(TimeUnit.MILLISECONDS.toDays(now.getTime() - past_time.getTime()) + " days ago");
        }
        catch(Throwable t) {
            t.printStackTrace();
        }

        player = (VideoView) findViewById(R.id.requestPlayer);

        GetVideoUrlTask g = new GetVideoUrlTask(player,RequestActivity.this);
        g.execute(url);


        like = (ImageButton) findViewById(R.id.likeButton);
        try {
            JSONArray jR = new JSONArray(likes);
            likeArr = convertLikesArray(jR);
            isLiked = isLiked(likeArr);
            if(likeArr.length>0) {
                ListView userList = (ListView) findViewById(R.id.userLikesList);
                ListAdapter adpt = new SimpleLikeAdapter(this, likeArr);
                userList.setAdapter(adpt);
                if (isLiked)
                    like.setBackgroundColor(Color.parseColor("#207DE5"));
            }
            else
            {
                ListView userList = (ListView) findViewById(R.id.userLikesList);
                userList.setVisibility(View.GONE);
            }

        }catch (JSONException j)
        {
            j.printStackTrace();
        }
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    new UnLikeTask().execute("http://music-hasalon-api.herokuapp.com/requests/" + id + "/unlike");
                    like.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    isLiked = false;
                }
                else {
                    new LikeTask().execute("http://music-hasalon-api.herokuapp.com/requests/" + id + "/like");
                    like.setBackgroundColor(Color.parseColor("#207DE5"));
                    isLiked = true;
                }
            }
        });


    }

    private Like[] convertLikesArray(JSONArray arr) throws JSONException {

        Like[] likes = new Like[arr.length()];
        for (int i = 0; i < arr.length(); i++) {
            JSONObject like = arr.getJSONObject(i);
            likes[i] = new Like(like.getString("id"),convertUser(like.getJSONObject("user")));
        }
        return likes;
    }

    private User convertUser(JSONObject obj) throws JSONException {

        String id = obj.getString("id");
        String name = obj.getString("name");
        String email = obj.getString("email");
        String thumbnail = obj.getString("thumbnail");

        return new User(id,name,email,thumbnail);
    }

    private boolean isLiked(Like[] arr) throws JSONException{
        for (int i = 0; i < arr.length; i++) {
            Log.i("id1",""+MyApp.userJson.getInt("id"));
            Log.i("id2",arr[i].getUser().getId());
            if(arr[i].getUser().getId().equals(""+MyApp.userJson.getInt("id"))) {
                Log.i("isLiked","true");
                return true;
            }
        }
        Log.i("isLiked","false");
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request, menu);
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

        if(id == R.id.homeAsUp)
        {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
