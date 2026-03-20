package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.CheckedTextView;
import java.lang.reflect.Field;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a {

        /* renamed from: a  reason: collision with root package name */
        private static Field f5145a;

        /* renamed from: b  reason: collision with root package name */
        private static boolean f5146b;

        static Drawable a(CheckedTextView checkedTextView) {
            if (!f5146b) {
                try {
                    Field declaredField = CheckedTextView.class.getDeclaredField("mCheckMarkDrawable");
                    f5145a = declaredField;
                    declaredField.setAccessible(true);
                } catch (NoSuchFieldException e8) {
                    Log.i("CheckedTextViewCompat", "Failed to retrieve mCheckMarkDrawable field", e8);
                }
                f5146b = true;
            }
            Field field = f5145a;
            if (field != null) {
                try {
                    return (Drawable) field.get(checkedTextView);
                } catch (IllegalAccessException e9) {
                    Log.i("CheckedTextViewCompat", "Failed to get check mark drawable via reflection", e9);
                    f5145a = null;
                }
            }
            return null;
        }
    }

    /* renamed from: androidx.core.widget.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class C0048b {
        static Drawable a(CheckedTextView checkedTextView) {
            return checkedTextView.getCheckMarkDrawable();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c {
        static void a(CheckedTextView checkedTextView, ColorStateList colorStateList) {
            checkedTextView.setCheckMarkTintList(colorStateList);
        }

        static void b(CheckedTextView checkedTextView, PorterDuff.Mode mode) {
            checkedTextView.setCheckMarkTintMode(mode);
        }
    }

    public static Drawable a(CheckedTextView checkedTextView) {
        return Build.VERSION.SDK_INT >= 16 ? C0048b.a(checkedTextView) : a.a(checkedTextView);
    }

    public static void b(CheckedTextView checkedTextView, ColorStateList colorStateList) {
        if (Build.VERSION.SDK_INT >= 21) {
            c.a(checkedTextView, colorStateList);
        } else if (checkedTextView instanceof m) {
            ((m) checkedTextView).setSupportCheckMarkTintList(colorStateList);
        }
    }

    public static void c(CheckedTextView checkedTextView, PorterDuff.Mode mode) {
        if (Build.VERSION.SDK_INT >= 21) {
            c.b(checkedTextView, mode);
        } else if (checkedTextView instanceof m) {
            ((m) checkedTextView).setSupportCheckMarkTintMode(mode);
        }
    }
}
