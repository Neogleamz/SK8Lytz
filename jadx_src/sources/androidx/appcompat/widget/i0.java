package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import java.lang.ref.WeakReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class i0 extends a0 {

    /* renamed from: b  reason: collision with root package name */
    private final WeakReference<Context> f1504b;

    public i0(Context context, Resources resources) {
        super(resources);
        this.f1504b = new WeakReference<>(context);
    }

    @Override // android.content.res.Resources
    public Drawable getDrawable(int i8) {
        Drawable a9 = a(i8);
        Context context = this.f1504b.get();
        if (a9 != null && context != null) {
            z.h().x(context, i8, a9);
        }
        return a9;
    }
}
