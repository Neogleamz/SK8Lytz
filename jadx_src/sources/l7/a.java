package l7;

import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    public static final TimeInterpolator f21786a = new LinearInterpolator();

    /* renamed from: b  reason: collision with root package name */
    public static final TimeInterpolator f21787b = new d1.b();

    /* renamed from: c  reason: collision with root package name */
    public static final TimeInterpolator f21788c = new d1.a();

    /* renamed from: d  reason: collision with root package name */
    public static final TimeInterpolator f21789d = new d1.c();

    /* renamed from: e  reason: collision with root package name */
    public static final TimeInterpolator f21790e = new DecelerateInterpolator();

    public static float a(float f5, float f8, float f9) {
        return f5 + (f9 * (f8 - f5));
    }

    public static float b(float f5, float f8, float f9, float f10, float f11) {
        return f11 < f9 ? f5 : f11 > f10 ? f8 : a(f5, f8, (f11 - f9) / (f10 - f9));
    }

    public static int c(int i8, int i9, float f5) {
        return i8 + Math.round(f5 * (i9 - i8));
    }
}
