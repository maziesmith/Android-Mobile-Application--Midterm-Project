package session2.gabcvit.midproject.util;

import java.util.Arrays;

/**
 * Utility class for object helper methods
 */
public class Objects {
    public static final String NAME = Objects.class.getSimpleName();

    /**
     * Direct copy from API 19
     * <p/>
     * Null-safe equivalent of {@code a.equals(b)}.
     */
    public static boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    /**
     * Direct copy from API 19
     * <p/>
     * Convenience wrapper for {@link java.util.Arrays#hashCode}, adding varargs.
     * This can be used to compute a hash code for an object's fields as follows:
     * {@code Objects.hash(a, b, c)}.
     */
    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }
}

