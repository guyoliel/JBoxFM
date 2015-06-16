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
import fm.jbox.jboxfm.models.Like;
import fm.jbox.jboxfm.models.Party;
import fm.jbox.jboxfm.tasks.DownloadImageTask;

public class SimpleLikeAdapter extends ArrayAdapter<Like> {

    private Like[] likeArr;

    public SimpleLikeAdapter(Context ctx, Like[] likeArr) {
        super(ctx, R.layout.list_item, likeArr);
        this.likeArr = likeArr;
    }

    public int getCount() {
        if (likeArr != null)
            return likeArr.length;
        return 0;
    }

    public Like getItem(int position) {
        if (likeArr != null)
            return likeArr[position];
        return null;
    }

    public long getItemId(int position) {
        if (likeArr != null)
            return likeArr[position].hashCode();
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.user_list_item,parent,false);
        Like l = getItem(position);

        TextView userName = (TextView) v.findViewById(R.id.userName);
        userName.setText(l.getUser().getName());

        TextView id = (TextView) v.findViewById(R.id.userId);
        id.setText(l.getUser().getId());

        new DownloadImageTask((ImageView) v.findViewById(R.id.userThumbnail)).execute(l.getUser().getThumbnail());
        return v;
    }

    public Like[] getItemList() {
        return likeArr;
    }

    public void setItemList(Like[] likeArr) {
        this.likeArr = likeArr;
    }
}
