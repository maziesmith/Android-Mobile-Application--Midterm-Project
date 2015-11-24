package session2.gabcvit.midproject.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import session2.gabcvit.midproject.ApplicationActivity;
import session2.gabcvit.midproject.R;

/**
 * Created by gabrielchecchiavitali on 2015-11-02.
 */
public class Form extends Fragment implements View.OnClickListener {
    public static final String NAME = Form.class.getSimpleName();

    private AlertDialog dialog;

    private TextView firstName;
    private TextView lastName;
    private Button button;


    @Override
    public void onClick(View view) {
        if (button.equals(view)) {
            // button was clicked
            final String first = firstName.getText() != null ? firstName.getText().toString() : "";
            final String last = lastName.getText() != null ? lastName.getText().toString() : "";

            if (!TextUtils.isEmpty(first) && !TextUtils.isEmpty(last)) {
                // remove caret
                firstName.setCursorVisible(false);
                lastName.setCursorVisible(false);
                changeButtonLabel();

                ((ApplicationActivity) getActivity()).onFormEnterClick();
                ((ApplicationActivity) getActivity()).passNames(first, last);

            }
            else {
                // either first or last name are empty - raise an error
                if (TextUtils.isEmpty(first)) {
                    // first name needed, put caret in first name text view
                    firstName.requestFocus();
                }
                else {
                    // last name is empty, caret on last name text view
                    lastName.requestFocus();
                }

                // create an error dialog
                // close any previous alert dialog
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                // read values from /res/values/strings.xml
                final String close = getString(R.string.form_error_close);
                final String title = getString(R.string.form_error_title);
                final String description = getString(R.string.form_error_description);

                // create the dialog by using its Builder
                dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(title)
                        .setMessage(description)
                        .setCancelable(true)
                        .setNegativeButton(close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.cancel();
                                    dialog = null;
                                }
                            }
                        })
                        .create();
                dialog.show();
            }
        }
    }

    public void changeButtonLabel()
    {
        button.setText(R.string.button_pressed);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w(NAME, NAME + ".onCreateView()");
        return inflater.inflate(R.layout.form, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.w(NAME, NAME + ".onActivityCreated()");

        // set references to the needed views
        firstName = (TextView) view.findViewById(R.id.firstName);
        lastName = (TextView) view.findViewById(R.id.lastName);
        button = (Button) view.findViewById(R.id.addButton);
        button.setOnClickListener(this);

        if (savedInstanceState != null) {
            // restore from saved instance state
            firstName.setText(savedInstanceState.getString(ApplicationActivity.FIRST_NAME_KEY));
            lastName.setText(savedInstanceState.getString(ApplicationActivity.LAST_NAME_KEY));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.w(NAME, NAME + ".onSaveInstanceState()");

        // pass the value for instance state (e.g. orientation changed, went in background, etc.)
        final String first = firstName.getText() != null ? firstName.getText().toString() : null;
        final String last = lastName.getText() != null ? lastName.getText().toString() : null;
        outState.putString(ApplicationActivity.FIRST_NAME_KEY, first);
        outState.putString(ApplicationActivity.LAST_NAME_KEY, last);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.w(NAME, NAME + ".onDestroyView()");

        // always clear the dialog
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }

    public String getFirstName()
    {
        return this.firstName.getText().toString();

    }

    public String getLastName()
    {
        return this.lastName.getText().toString();
    }

}
