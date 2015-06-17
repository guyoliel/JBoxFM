package fm.jbox.jboxfm.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fm.jbox.jboxfm.models.Party;
import fm.jbox.jboxfm.R;

public class SimplePartyAdapter extends ArrayAdapter<Party> {

    private static final String TAG = "Adapter";
    private List<Party> itemList;

    public SimplePartyAdapter(Context ctx, List<Party> itemList) {
        super(ctx, R.layout.list_item, itemList);
        Log.i(TAG,"hi c");
        this.itemList = itemList;
        Log.i(TAG,"bye c");
    }

    public int getCount() {
        if (itemList != null)
        return itemList.size();
        return 0;
    }

    public Party getItem(int position) {
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
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.list_item,parent,false);
        Party p = getItem(position);

        TextView text = (TextView) v.findViewById(R.id.partyName);
        text.setText(p.getName());

        TextView user = (TextView) v.findViewById(R.id.partyUser);
        user.setText(p.getUser_id());

        TextView id = (TextView) v.findViewById(R.id.partyId);
        id.setText(p.getId());

        TextView partyCode = (TextView) v.findViewById(R.id.partyCodeDisplay);
        partyCode.setText("Party Code: "+p.getId());
        Log.i(TAG, id.getText().toString());
        return v;
    }

    public List<Party> getItemList() {
        return itemList;
    }

    public void setItemList(List<Party> itemList) {
        Log.i(TAG,"setItemList");
        this.itemList = itemList;
    }

}
