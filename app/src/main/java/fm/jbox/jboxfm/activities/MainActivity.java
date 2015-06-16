package fm.jbox.jboxfm.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.facebook.Session;

import org.json.JSONException;

import fm.jbox.jboxfm.R;
import fm.jbox.jboxfm.dialogs.CreatePartyDialog;
import fm.jbox.jboxfm.dialogs.LogOutDialog;
import fm.jbox.jboxfm.tasks.AsyncCreateParty;
import fm.jbox.jboxfm.tasks.AsyncPartyListLoader;


public class MainActivity extends ActionBarActivity implements CreatePartyDialog.CreatePartyDialogListener, LogOutDialog.LogOutDialogListener {

    private ListView lv;
    private Context ctx;
    private final String tag = "Main";
    private EditText partyCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("parties");
        tabSpec.setContent(R.id.tabParties);
        tabSpec.setIndicator("Parties");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("search");
        tabSpec.setContent(R.id.tabSearch);
        tabSpec.setIndicator("Search");
        tabHost.addTab(tabSpec);

        partyCode = (EditText) findViewById(R.id.partyCode);
        partyCode.setHint("Enter Party Code");

        for(int i=0; i<tabHost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
        }

        lv = (ListView) findViewById(R.id.partyList);

        loadParties();

        lv.setOnItemClickListener(
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(ctx,PartyActivity.class);
                        TextView partyId = (TextView) view.findViewById(R.id.partyId);
                        Log.i(tag,partyId.getText().toString());
                        i.putExtra("partyId",partyId.getText());
                        TextView partyName = (TextView) view.findViewById(R.id.partyName);
                        i.putExtra("partyName",partyName.getText());

                        TextView partyUser = (TextView) view.findViewById(R.id.partyUser);
                        i.putExtra("partyUserId",partyUser.getText());
                        startActivity(i);
                    }
                }

        );
        SetTitle();
    }



   public void SetTitle(){
           try {
               setTitle("Welcome " + MyApp.userJson.getString("name")+"!");
           }catch (JSONException j){
               j.printStackTrace();
           }
   }

   private void loadParties(){
       try {
           AsyncPartyListLoader task = new AsyncPartyListLoader(this, lv);
           task.execute("http://music-hasalon-api.herokuapp.com/users/" + MyApp.userJson.getString("id"));
       }catch(JSONException ex){ex.printStackTrace();}
   }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Resume","REFRESH ME PLEASE I BEG YOU!");
        loadParties();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            showLogOutDialog();
            return true;
        }

        if (id == R.id.action_add) {
            showNoticeDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CreatePartyDialog();
        dialog.show(getFragmentManager(), "CreatePartyDialog");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        loadParties();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    public void showLogOutDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new LogOutDialog();
        dialog.show(getFragmentManager(), "LogOutDialog");
    }

    @Override
    public void onLogOutDialogPositiveClick(DialogFragment dialog){
        Session s = Session.getActiveSession();
        s.closeAndClearTokenInformation();
        finish();
    }

    @Override
    public void onLogOutDialogNegativeClick(DialogFragment dialog){

    }

    @Override
    public void onBackPressed() {
    }
}
