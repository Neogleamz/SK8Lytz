package u7;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b {
    public static TypedValue a(Context context, int i8) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(i8, typedValue, true)) {
            return typedValue;
        }
        return null;
    }

    public static boolean b(Context context, int i8, boolean z4) {
        TypedValue a9 = a(context, i8);
        return (a9 == null || a9.type != 18) ? z4 : a9.data != 0;
    }

    public static int c(Context context, int i8, String str) {
        TypedValue a9 = a(context, i8);
        if (a9 != null) {
            return a9.data;
        }
        throw new IllegalArgumentException(String.format("%1$s requires a value for the %2$s attribute to be set in your app theme. You can either set the attribute in your theme or update your theme to inherit from Theme.MaterialComponents (or a descendant).", str, context.getResources().getResourceName(i8)));
    }

    public static int d(View view, int i8) {
        return c(view.getContext(), i8, view.getClass().getCanonicalName());
    }
}
