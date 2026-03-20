package q7;

import android.content.Context;
import android.graphics.Color;
import u7.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    private final boolean f22635a;

    /* renamed from: b  reason: collision with root package name */
    private final int f22636b;

    /* renamed from: c  reason: collision with root package name */
    private final int f22637c;

    /* renamed from: d  reason: collision with root package name */
    private final float f22638d;

    public a(Context context) {
        this.f22635a = b.b(context, k7.b.f21069v, false);
        this.f22636b = n7.a.b(context, k7.b.f21068u, 0);
        this.f22637c = n7.a.b(context, k7.b.f21066s, 0);
        this.f22638d = context.getResources().getDisplayMetrics().density;
    }

    private boolean f(int i8) {
        return androidx.core.graphics.b.p(i8, 255) == this.f22637c;
    }

    public float a(float f5) {
        float f8 = this.f22638d;
        if (f8 <= 0.0f || f5 <= 0.0f) {
            return 0.0f;
        }
        return Math.min(((((float) Math.log1p(f5 / f8)) * 4.5f) + 2.0f) / 100.0f, 1.0f);
    }

    public int b(int i8, float f5) {
        float a9 = a(f5);
        return androidx.core.graphics.b.p(n7.a.h(androidx.core.graphics.b.p(i8, 255), this.f22636b, a9), Color.alpha(i8));
    }

    public int c(int i8, float f5) {
        return (this.f22635a && f(i8)) ? b(i8, f5) : i8;
    }

    public int d(float f5) {
        return c(this.f22637c, f5);
    }

    public boolean e() {
        return this.f22635a;
    }
}
