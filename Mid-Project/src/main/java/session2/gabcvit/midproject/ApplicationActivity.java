package session2.gabcvit.midproject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import session2.gabcvit.midproject.model.UserModel;
import session2.gabcvit.midproject.ui.Form;
import session2.gabcvit.midproject.ui.Notes;
import session2.gabcvit.midproject.util.Views;


public class ApplicationActivity extends Activity {
    public static final String NAME = ApplicationActivity.class.getSimpleName();

    public static final String FIRST_NAME_KEY = "firstName";
    public static final String LAST_NAME_KEY = "lastName";
    public static final String USER_KEY = "userKey";


    public void onFormEnterClick() {
        if (Views.isActivityNull(this)) {
            return;
        }

        // show newNote, then update it
        FragmentManager fragmentManager = getFragmentManager();
        Fragment oldNote = fragmentManager.findFragmentByTag(Notes.NAME);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (oldNote != null) {
            // remove previous Greeting instance (if such exists in some weird cases)
            fragmentTransaction.remove(oldNote);
        }

        // and then add a new instance of Greeting and commit at the end
        fragmentTransaction
                .add(R.id.rootView, Notes.newInstance(), Notes.NAME)
                .commit();

        // hide the pesky soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    public void passNames(String firstName, String lastName)
    {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStackImmediate(Notes.NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Notes notes = (Notes) fragmentManager.findFragmentByTag(Notes.NAME);
        notes.setNames(firstName, lastName);
    }

    public void onAddNoteClick()
    {
        FragmentManager fragmentManager = getFragmentManager();

        // remove EditFragment since it completed its work
        fragmentManager.popBackStackImmediate(Notes.NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // notify ContactFragment to update itself
        Notes notes = (Notes) fragmentManager.findFragmentByTag(Notes.NAME);
        if (notes != null) {
            // force to update from shared preferences by providing 'null'
            notes.update();
        }

        // hide the pesky soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.w(NAME, NAME + ".onCreate()");

        // add the layout
        setContentView(R.layout.applicationactivity);

        // add Form
        if (savedInstanceState == null) {
            // adding a fragment dynamically requires to ensure activity is not recreated from saved instance state
            // if 'savedInstanceState' is null - this menas activity is created for the first time, ie. app just started
            getFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.rootView, new Form(), Form.NAME)
                    .commit();
        }
    }

}