package session2.gabcvit.midproject.util;

import android.app.Activity;
/**
 * Utility class for any View related actions
 */
public class Views {
    /**
     * Test for activiy failure. Activity can be in process of destruction, finishing or plain not responding in
     * many edge-cases (e.g. quick start and close an app).
     *
     * @param activity activity
     * @since API 17
     * @return
     */
    public static boolean isActivityNull(Activity activity) {
        return activity == null || activity.isFinishing() || activity.isDestroyed();
    }
}