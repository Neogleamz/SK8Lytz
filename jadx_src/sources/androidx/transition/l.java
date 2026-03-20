package androidx.transition;

import android.animation.ObjectAnimator;
import android.animation.TypeConverter;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.util.Property;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class l {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> ObjectAnimator a(T t8, Property<T, PointF> property, Path path) {
        return Build.VERSION.SDK_INT >= 21 ? ObjectAnimator.ofObject(t8, property, (TypeConverter) null, path) : ObjectAnimator.ofFloat(t8, new m(property, path), 0.0f, 1.0f);
    }
}
