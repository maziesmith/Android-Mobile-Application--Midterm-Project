package session2.gabcvit.midproject.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import session2.gabcvit.midproject.ApplicationActivity;
import session2.gabcvit.midproject.R;
import session2.gabcvit.midproject.model.UserModel;
import session2.gabcvit.midproject.util.Views;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gabrielchecchiavitali on 2015-11-02.
 */
public class Notes extends Fragment implements View.OnClickListener
{
    public static final String NAME = Notes.class.getSimpleName();
    private static final String MODEL_KEY = "modelKey";

    private String firstName;
    private String lastName;
    private TextView noteText;
    private Button addNote;
    private RecyclerView list;
    private SharedPreferences sharedPreferences;
    private JSONArray contacts;
    public int savedModelIndex;

    private static final class CellViewHolder extends RecyclerView.ViewHolder{
        private final TextView note;
        private UserModel model;

        private CellViewHolder(View view) {
            super(view);
            note = (TextView) view.findViewById(R.id.title);
        }

        private void update(UserModel model) {
            Log.w(NAME, NAME + ".update()");
            if (model == null || model.isEmpty) {
                return;
            }
            this.model = model;

            note.setText(" " + model.text);
        }
    }

    private static final class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private final LayoutInflater layoutInflater;
        private final List<UserModel> items;

        private final int itemsSize;

        private Adapter(@NonNull Context context, List<UserModel> items) {
            layoutInflater = LayoutInflater.from(context);

            setHasStableIds(true);

            this.items = items != null ? items : Collections.<UserModel>emptyList();
            itemsSize = this.items.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup recyclerView, final int viewType) {
            View view = layoutInflater.inflate(viewType, recyclerView, false);
            // for empty cell use ViewHolder, otherwise CellViewHolder
            return R.layout.notes_empty_cell == viewType ? new ViewHolder(view) {
            } : new CellViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            if (viewHolder instanceof CellViewHolder) {
                ((CellViewHolder) viewHolder).update(items.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return itemsSize;
        }

        @Override
        public int getItemViewType(final int position) {
            // if model is null and only one item, we treat this one as no data - will be used
            // to load R.layout.contacts_empty_cell. Otherwise regular layout
            UserModel model = items.get(position);
            return itemsSize == 1 && model.isEmpty ? R.layout.notes_empty_cell : R.layout.notes_cell;
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w(NAME, NAME + ".onCreateView()");
        return inflater.inflate(R.layout.notes, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.w(NAME, NAME + ".onCreateView()");
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if (Views.isActivityNull(activity)) {
            return;
        }

        // get a reference to 'ApplicationActivity.xml' shared preference file
        sharedPreferences = activity.getSharedPreferences(ApplicationActivity.NAME, Context.MODE_PRIVATE);

        // get model as string by first reading saved instance state. If saved instance state is not valid (i.e. first time
        // created, will use getArguments() to retrieve passed bundle
        String modelAsString = savedInstanceState != null && savedInstanceState.containsKey(MODEL_KEY) ? savedInstanceState
                .getString(MODEL_KEY, "") : getArguments().getString(MODEL_KEY, "");
        UserModel model = new UserModel(modelAsString);

        // retrieve all stored notes
        try {
            String contactsAsString = sharedPreferences.getString(ApplicationActivity.USER_KEY, "");
            contacts = new JSONArray(contactsAsString);
        }
        catch (JSONException e) {
            // not interested
            e.printStackTrace();
            contacts = new JSONArray();
        }

        // and get index for the model to be changed (if such exists already)
        savedModelIndex = -1;
        int length = contacts.length();
//        Log.w(NAME, NAME + " - " + length);
//        if (length > 0 ) { //&& !model.isEmpty
//            for (int i = 0; i < length; i++) {
//                //UserModel m = new UserModel(contacts.optString(i));
//                //if (model.equals(m)) {
//                    Log.w(NAME, NAME + "ENTERED!" +i);
//                    savedModelIndex = i;
//                    //break;
//                //}
//            }
//        }

        noteText = (TextView) view.findViewById(R.id.new_note);
        addNote = (Button) view.findViewById(R.id.button);
        addNote.setOnClickListener(this);

        /*String modelAsString = savedInstanceState != null && savedInstanceState.containsKey(MODEL_KEY) ? savedInstanceState
                .getString(MODEL_KEY, "") : getArguments().getString(MODEL_KEY, "");
        UserModel model = new UserModel(modelAsString);

        Bundle arguments = getArguments();*/

        list = (RecyclerView) view.findViewById(R.id.notes_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list.setItemAnimator(new DefaultItemAnimator());

        // enable options menu
        setHasOptionsMenu(true);

        update();
    }

    public static Notes newInstance() {
        // use this bundle as setArguments() so we can get it from within onActivityCreated()
        final Bundle bundle = new Bundle();

        final Notes fragment = new Notes();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Notes newInstance(String text) {
        text = text != null ? text : "";
        // use this bundle as setArguments() so we can get it from within onActivityCreated()

        final Bundle bundle = new Bundle();
        //bundle.putString(ApplicationActivity.USER_KEY, text);

        final Notes fragment = new Notes();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void update() {
        Log.w(NAME, NAME + ".update()");
        // read from shared preferences directly
        Activity activity = getActivity();

        if (!Views.isActivityNull(activity)) {
            List<UserModel> models = new ArrayList<>();
            String notesAsString = activity.getPreferences(Context.MODE_PRIVATE).getString(ApplicationActivity.USER_KEY,
                    "[]");

            JSONArray notes = new JSONArray();
            try {
                notes = new JSONArray(notesAsString);

                int notesLength = notes.length();

                for (int i = 0; i < notesLength; i++) {
                    models.add(new UserModel(notes.optString(i)));
                }
            }
            catch (JSONException e) {
                // not interested
                e.printStackTrace();
            }

            if (models.size() == 0) {
                // no data - add 'null' to indicate the need of R.layout.contacts_empty_cell
                models.add(UserModel.EMPTY);
            }

            list.setAdapter(new Adapter(activity, models));
        }
    }

    public void onClick(View view) {
        Log.w(NAME, NAME + ".onClick()");
        boolean isUpdated = false;
        ApplicationActivity activity = (ApplicationActivity) getActivity();

        if (addNote.equals(view)) {

            UserModel currentModel = toUserModel();

            try {
                if (savedModelIndex > -1) {
                    // replace index with the updated model
                    contacts.put(savedModelIndex, currentModel.json);
                }
                else {
                    // add a new model
                    contacts.put(currentModel.json);
                }

                // store back in shared preferences
                sharedPreferences.edit().putString(ApplicationActivity.USER_KEY, contacts.toString()).apply();
                isUpdated = true;
                this.noteText.setText("");
            }
            catch (JSONException e) {
                // not interested
                e.printStackTrace();
            }

            if (isUpdated) {
                activity.onAddNoteClick();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.w(NAME, NAME + ".onSaveInstanceState()");

        // pass the value for instance state (e.g. orientation changed, went in background, etc.)
        outState.putString(MODEL_KEY, toUserModel().json.toString());
    }

    private UserModel toUserModel()
    {
        Log.w(NAME, NAME + "toUserModel()" + this.firstName + this.lastName + this.savedModelIndex);
        return new UserModel(this.firstName, this.lastName, this.noteText.getText().toString(), this.savedModelIndex);
    }

    public void setNames(String first, String last)
    {
        this.firstName = first;
        this.lastName = last;
    }


}
