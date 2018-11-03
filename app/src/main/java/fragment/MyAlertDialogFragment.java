package fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MyAlertDialogFragment extends DialogFragment{

    public static MyAlertDialogFragment newInstance(String title, String message, String dialogType, String id) {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putString("dialogType", dialogType);
        args.putString("id", id);
        frag.setArguments(args);
        return frag;
    }

    public interface PositiveNegativeOptionsListener {
        void doPositiveClick(String type, String id);
        void doNegativeClick(String type, String id, DialogInterface dialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        final String dialogType = getArguments().getString("dialogType"); //This is to determine which is the dialog to load!
        final String id = getArguments().getString("id");
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((PositiveNegativeOptionsListener) getActivity()).doPositiveClick(dialogType, id);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((PositiveNegativeOptionsListener) getActivity()).doNegativeClick(dialogType, id, dialog);
                    }
                })
                .create();
    }
}


