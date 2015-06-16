package fm.jbox.jboxfm.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class LogOutDialog extends DialogFragment {
    public interface LogOutDialogListener {
        public void onLogOutDialogPositiveClick(DialogFragment dialog);
        public void onLogOutDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    LogOutDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (LogOutDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Log Out").setMessage("Are you sure that you want to log out?")
                .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onLogOutDialogPositiveClick(LogOutDialog.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Log.i("DialogClick", "NEGATIVE");
                        mListener.onLogOutDialogNegativeClick(LogOutDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
