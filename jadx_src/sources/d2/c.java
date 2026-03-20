package d2;

import android.util.TypedValue;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* renamed from: a  reason: collision with root package name */
    private static final TypedValue f19700a = new TypedValue();

    public static <T> T a(View view, int i8, String str, Class<T> cls) {
        try {
            return cls.cast(view);
        } catch (ClassCastException e8) {
            String e9 = e(view, i8);
            throw new IllegalStateException("View '" + e9 + "' with ID " + i8 + " for " + str + " was of the wrong type. See cause for more info.", e8);
        }
    }

    public static <T> T b(View view, int i8, String str, Class<T> cls) {
        return (T) a(view.findViewById(i8), i8, str, cls);
    }

    public static View c(View view, int i8, String str) {
        View findViewById = view.findViewById(i8);
        if (findViewById != null) {
            return findViewById;
        }
        String e8 = e(view, i8);
        throw new IllegalStateException("Required view '" + e8 + "' with ID " + i8 + " for " + str + " was not found. If this view is optional add '@Nullable' (fields) or '@Optional' (methods) annotation.");
    }

    public static <T> T d(View view, int i8, String str, Class<T> cls) {
        return (T) a(c(view, i8, str), i8, str, cls);
    }

    private static String e(View view, int i8) {
        return view.isInEditMode() ? "<unavailable while editing>" : view.getContext().getResources().getResourceEntryName(i8);
    }
}
