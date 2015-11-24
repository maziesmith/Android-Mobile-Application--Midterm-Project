package session2.gabcvit.midproject.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utility class for network helper methods
 */
public class Networks {
    public static final String NAME = Networks.class.getSimpleName();

    /**
     * @return Available {@link android.net.NetworkInfo} or null if nothing
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        if (context != null) {
            try {
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (manager != null) {
                    final NetworkInfo info = manager.getActiveNetworkInfo();
                    return info != null && info.isAvailable() && info.isConnected() ? info : null;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}