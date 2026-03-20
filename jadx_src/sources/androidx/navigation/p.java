package androidx.navigation;

import android.view.View;
import android.view.ViewParent;
import java.lang.ref.WeakReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p {
    public static NavController a(View view) {
        NavController b9 = b(view);
        if (b9 != null) {
            return b9;
        }
        throw new IllegalStateException("View " + view + " does not have a NavController set");
    }

    private static NavController b(View view) {
        while (view != null) {
            NavController c9 = c(view);
            if (c9 != null) {
                return c9;
            }
            ViewParent parent = view.getParent();
            view = parent instanceof View ? (View) parent : null;
        }
        return null;
    }

    private static NavController c(View view) {
        Object tag = view.getTag(s.f6444a);
        if (tag instanceof WeakReference) {
            tag = ((WeakReference) tag).get();
        } else if (!(tag instanceof NavController)) {
            return null;
        }
        return (NavController) tag;
    }

    public static void d(View view, NavController navController) {
        view.setTag(s.f6444a, navController);
    }
}
