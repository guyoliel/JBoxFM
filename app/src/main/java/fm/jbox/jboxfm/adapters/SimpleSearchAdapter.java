package fm.jbox.jboxfm.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fm.jbox.jboxfm.R;
import fm.jbox.jboxfm.activities.MyApp;
import fm.jbox.jboxfm.models.Request;
import fm.jbox.jboxfm.models.Video;
import fm.jbox.jboxfm.tasks.DownloadImageTask;

public class SimpleSearchAdapter extends ArrayAdapter<Video> {
    private static final String TAG = "RequestAdapter";
    private List<Video> itemList;

    public SimpleSearchAdapter(Context ctx, List<Video> itemList) {
        super(ctx, R.layout.search_list_item, itemList);
        Log.i(TAG, "hi c");
        this.itemList = itemList;
        Log.i(TAG,"bye c");
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    public Video getItem(int position) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(getItem(position)!=null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View v = inflater.inflate(R.layout.search_list_item, parent, false);

            Log.i(TAG, getItem(position).toString());
            Video vid = getItem(position);

            TextView thumbnail = (TextView) v.findViewById(R.id.requestThumbnailText);
            thumbnail.setText(vid.getThumbnail());
            new DownloadImageTask((ImageView) v.findViewById(R.id.requestThumbnail)).execute(vid.getThumbnail());

            TextView title = (TextView) v.findViewById(R.id.requestTitle);
            title.setText(vid.getTitle());

            TextView url = (TextView) v.findViewById(R.id.requestUrl);
            url.setText(vid.getUrl());

            TextView userName = (TextView) v.findViewById(R.id.requestAuthor);
            userName.setText(vid.getAuthor());


            return v;
        }
        return null;
    }

    public List<Video> getItemList() {
        return itemList;
    }

    public void setItemList(List<Video> itemList) {
        Log.i(TAG,"setItemList");
        this.itemList = itemList;
    }

}
