package v2;

import android.graphics.Typeface;
import android.os.Build;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {
    public static final Typeface a(Typeface typeface, boolean z4) {
        Typeface create;
        p.e(typeface, "<this>");
        if (Build.VERSION.SDK_INT >= 28) {
            create = Typeface.create(typeface, typeface.getWeight() == 0 ? 400 : typeface.getWeight(), z4);
        } else {
            create = Typeface.create(typeface, z4 ? typeface.isBold() ? 3 : 2 : typeface.isBold() ? 1 : 0);
        }
        p.b(create);
        return create;
    }

    public static final Typeface b(Typeface typeface, int i8) {
        Typeface create;
        p.e(typeface, "<this>");
        if (Build.VERSION.SDK_INT >= 28) {
            create = Typeface.create(typeface, i8, typeface.isItalic());
        } else {
            create = Typeface.create(typeface, i8 >= 700 ? 1 : 0);
        }
        p.b(create);
        return create;
    }
}
