package fm.jbox.jboxfm.activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import fm.jbox.jboxfm.R;


public class LoginActivity extends FragmentActivity {

    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            if (savedInstanceState == null) {
                loginFragment = new LoginFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(android.R.id.content, loginFragment)
                        .commit();
            } else {
                loginFragment = (LoginFragment) getSupportFragmentManager()
                        .findFragmentById(android.R.id.content);
            }

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
