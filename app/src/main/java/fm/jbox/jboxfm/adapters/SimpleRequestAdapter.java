package fm.jbox.jboxfm.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.ExecutionException;

import fm.jbox.jboxfm.R;
import fm.jbox.jboxfm.activities.MyApp;
import fm.jbox.jboxfm.models.Request;
import fm.jbox.jboxfm.tasks.DeleteTask;
import fm.jbox.jboxfm.tasks.DownloadImageTask;

public class SimpleRequestAdapter extends ArrayAdapter<Request> {

    private static final String TAG = "RequestAdapter";
    private List<Request> itemList;
    private Context ctx;
    private String partyOwnerId;

    public SimpleRequestAdapter(Context ctx, List<Request> itemList,String partyOwnerId) {
        super(ctx, R.layout.request_list_item_owner, itemList);
        this.ctx = ctx;
        Log.i(TAG,"hi c");
        this.itemList = itemList;
        Log.i(TAG,"bye c");
        this.partyOwnerId = partyOwnerId;
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    public Request getItem(int position) {
        if (itemList != null)
            return itemList.get(position);
        return null;
    }

    public long getItemId(int position) {
        if (itemList != null)
            return itemList.get(position).hashCode();
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        try {
            final ViewGroup list = parent;
            Log.i(TAG, getItem(position).toString());
            final Request r = getItem(position);
            final View view;
            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (MyApp.userJson.getString("id").equals(r.getUser().getId())) {
                view = inflater.inflate(R.layout.request_list_item_owner, parent, false);
                ImageButton deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    DeleteTask delete = new DeleteTask();
                                    String done = delete.execute("http://music-hasalon-api.herokuapp.com/requests/" + r.getId()).get();
                                    //list.removeViewInLayout(view);
                                    itemList.remove(position);
                                    notifyDataSetChanged();
                                } catch (InterruptedException | ExecutionException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                );
            }
            else
                view = inflater.inflate(R.layout.request_list_item, parent, false);


            new DownloadImageTask((ImageView) view.findViewById(R.id.requestThumbnail)).execute(r.getThumbnail());

            TextView id = (TextView) view.findViewById(R.id.requestId);
            id.setText(r.getId());

            TextView title = (TextView) view.findViewById(R.id.requestTitle);
            title.setText(r.getTitle());

            TextView url = (TextView) view.findViewById(R.id.requestUrl);
            url.setText(r.getUrl());

            TextView created = (TextView) view.findViewById(R.id.requestCreated);
            created.setText(r.getCreated_at());

            TextView user = (TextView) view.findViewById(R.id.requestUser);
            user.setText(r.getUser().toString());

            TextView userName = (TextView) view.findViewById(R.id.requestAuthor);
            userName.setText(r.getUser().getName());

            TextView likes = (TextView) view.findViewById(R.id.requestLikes);
            likes.setText(r.getLikesString());

            TextView likesCount = (TextView) view.findViewById(R.id.likeCount);
            if (r.getLikes().length != 0)
                likesCount.setText("Ups:" + r.getLikes().length);
            else
                likesCount.setVisibility(View.GONE);

            return view;
        }catch (JSONException ex){ex.printStackTrace();}
            return null;
        }

        public List<Request> getItemList () {
            return itemList;
        }

    public void setItemList(List<Request> itemList) {
        Log.i(TAG, "setItemList");
        this.itemList = itemList;
    }
}
