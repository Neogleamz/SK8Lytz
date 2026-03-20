package p7;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import java.lang.reflect.InvocationTargetException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {
    @TargetApi(21)
    public static void a(RippleDrawable rippleDrawable, int i8) {
        if (Build.VERSION.SDK_INT >= 23) {
            rippleDrawable.setRadius(i8);
            return;
        }
        try {
            RippleDrawable.class.getDeclaredMethod("setMaxRadius", Integer.TYPE).invoke(rippleDrawable, Integer.valueOf(i8));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e8) {
            throw new IllegalStateException("Couldn't set RippleDrawable radius", e8);
        }
    }

    public static PorterDuffColorFilter b(Drawable drawable, ColorStateList colorStateList, PorterDuff.Mode mode) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return new PorterDuffColorFilter(colorStateList.getColorForState(drawable.getState(), 0), mode);
    }
}
