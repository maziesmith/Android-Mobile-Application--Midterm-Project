package session2.gabcvit.midproject.model;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

import session2.gabcvit.midproject.ui.Notes;

/**
 * Created by gabrielchecchiavitali on 2015-11-02.
 */
public class UserModel {

    public static final UserModel EMPTY = new UserModel(null);
    public static final String NAME = UserModel.class.getSimpleName();

    public final JSONObject json;
    public final String firstName;
    public final String lastName;
    public final String text;
    private int textIndex = 0;
    public final boolean isEmpty;


    public UserModel(String jsonAsString) {
        JSONObject j = new JSONObject();
        try {
            jsonAsString = jsonAsString != null ? jsonAsString : "{}";
            j = new JSONObject(jsonAsString);
        }
        catch (JSONException e) {
            // not interested
            e.printStackTrace();
        }

        json = j;

        firstName = json.optString("firstName", "");
        lastName = json.optString("lastName", "");
        text = json.optString("text", "");

        isEmpty = json.length() == 0 || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(text);
    }

    public UserModel(final String firstName, final String lastName, final String text, final int index) {

        this.firstName = firstName != null ? firstName : "";
        this.lastName = lastName != null ? lastName : "";
        this.text = text != null ? text : "";

        json = new JSONObject();
        try {
            json.put("firstName", firstName);
            json.put("lastName", lastName);
            json.put("text", text);
        }
        catch(JSONException e) {
            // not interested
            e.printStackTrace();
        }

        isEmpty = json.length() == 0 || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(text);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (getClass() != object.getClass()) {
            return false;
        }

        UserModel other = (UserModel) object;

        if (isEmpty || other.isEmpty) {
            return false;
        }

        return Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName);
    }
}
