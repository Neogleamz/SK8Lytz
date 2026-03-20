package androidx.transition;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.util.Property;
import android.view.View;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f0 {

    /* renamed from: a  reason: collision with root package name */
    private static final l0 f7552a;

    /* renamed from: b  reason: collision with root package name */
    static final Property<View, Float> f7553b;

    /* renamed from: c  reason: collision with root package name */
    static final Property<View, Rect> f7554c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a extends Property<View, Float> {
        a(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Float get(View view) {
            return Float.valueOf(f0.c(view));
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(View view, Float f5) {
            f0.h(view, f5.floatValue());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b extends Property<View, Rect> {
        b(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Rect get(View view) {
            return androidx.core.view.c0.w(view);
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(View view, Rect rect) {
            androidx.core.view.c0.A0(view, rect);
        }
    }

    static {
        int i8 = Build.VERSION.SDK_INT;
        f7552a = i8 >= 29 ? new k0() : i8 >= 23 ? new j0() : i8 >= 22 ? new i0() : i8 >= 21 ? new h0() : i8 >= 19 ? new g0() : new l0();
        f7553b = new a(Float.class, "translationAlpha");
        f7554c = new b(Rect.class, "clipBounds");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(View view) {
        f7552a.a(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static e0 b(View view) {
        return Build.VERSION.SDK_INT >= 18 ? new d0(view) : c0.e(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float c(View view) {
        return f7552a.c(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static o0 d(View view) {
        return Build.VERSION.SDK_INT >= 18 ? new n0(view) : new m0(view.getWindowToken());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void e(View view) {
        f7552a.d(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void f(View view, Matrix matrix) {
        f7552a.e(view, matrix);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void g(View view, int i8, int i9, int i10, int i11) {
        f7552a.f(view, i8, i9, i10, i11);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void h(View view, float f5) {
        f7552a.g(view, f5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void i(View view, int i8) {
        f7552a.h(view, i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void j(View view, Matrix matrix) {
        f7552a.i(view, matrix);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void k(View view, Matrix matrix) {
        f7552a.j(view, matrix);
    }
}
