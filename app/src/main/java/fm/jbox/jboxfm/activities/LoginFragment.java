package fm.jbox.jboxfm.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.facebook.Session;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import fm.jbox.jboxfm.R;
import fm.jbox.jboxfm.tasks.AsyncServerLogin;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private UiLifecycleHelper uiHelper;
    private TextView failedText;
    private Intent mainIntent;
    private Activity context;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            failedText.setText(" ");
            AsyncServerLogin login = new AsyncServerLogin();
            try{
            String result = login.execute("http://music-hasalon-api.herokuapp.com/sessions").get();
            }catch(ExecutionException|InterruptedException ex)
            {ex.printStackTrace();}
            startActivity(mainIntent);
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
            failedText.setText("Failed To Login Through FaceBook Please Try Again");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_login,container, false);
        LoginButton authButton = (LoginButton) v.findViewById(R.id.authButton);
        failedText = (TextView) v.findViewById(R.id.failedText);
        authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("public_profile"));
        context = getActivity();
        mainIntent = new Intent(context,MainActivity.class);
        return v;
    }


}
