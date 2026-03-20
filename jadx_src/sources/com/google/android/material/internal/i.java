package com.google.android.material.internal;

import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import com.daimajia.numberprogressbar.BuildConfig;
import java.lang.reflect.Constructor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class i {

    /* renamed from: n  reason: collision with root package name */
    static final int f18124n;

    /* renamed from: o  reason: collision with root package name */
    private static boolean f18125o;

    /* renamed from: p  reason: collision with root package name */
    private static Constructor<StaticLayout> f18126p;
    private static Object q;

    /* renamed from: a  reason: collision with root package name */
    private CharSequence f18127a;

    /* renamed from: b  reason: collision with root package name */
    private final TextPaint f18128b;

    /* renamed from: c  reason: collision with root package name */
    private final int f18129c;

    /* renamed from: e  reason: collision with root package name */
    private int f18131e;

    /* renamed from: l  reason: collision with root package name */
    private boolean f18138l;

    /* renamed from: d  reason: collision with root package name */
    private int f18130d = 0;

    /* renamed from: f  reason: collision with root package name */
    private Layout.Alignment f18132f = Layout.Alignment.ALIGN_NORMAL;

    /* renamed from: g  reason: collision with root package name */
    private int f18133g = Integer.MAX_VALUE;

    /* renamed from: h  reason: collision with root package name */
    private float f18134h = 0.0f;

    /* renamed from: i  reason: collision with root package name */
    private float f18135i = 1.0f;

    /* renamed from: j  reason: collision with root package name */
    private int f18136j = f18124n;

    /* renamed from: k  reason: collision with root package name */
    private boolean f18137k = true;

    /* renamed from: m  reason: collision with root package name */
    private TextUtils.TruncateAt f18139m = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends Exception {
        a(Throwable th) {
            super("Error thrown initializing StaticLayout " + th.getMessage(), th);
        }
    }

    static {
        f18124n = Build.VERSION.SDK_INT >= 23 ? 1 : 0;
    }

    private i(CharSequence charSequence, TextPaint textPaint, int i8) {
        this.f18127a = charSequence;
        this.f18128b = textPaint;
        this.f18129c = i8;
        this.f18131e = charSequence.length();
    }

    private void b() {
        Class<?> cls;
        if (f18125o) {
            return;
        }
        try {
            boolean z4 = this.f18138l && Build.VERSION.SDK_INT >= 23;
            if (Build.VERSION.SDK_INT >= 18) {
                cls = TextDirectionHeuristic.class;
                q = z4 ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR;
            } else {
                ClassLoader classLoader = i.class.getClassLoader();
                String str = this.f18138l ? "RTL" : "LTR";
                Class<?> loadClass = classLoader.loadClass("android.text.TextDirectionHeuristic");
                Class<?> loadClass2 = classLoader.loadClass("android.text.TextDirectionHeuristics");
                q = loadClass2.getField(str).get(loadClass2);
                cls = loadClass;
            }
            Class cls2 = Integer.TYPE;
            Class cls3 = Float.TYPE;
            Constructor<StaticLayout> declaredConstructor = StaticLayout.class.getDeclaredConstructor(CharSequence.class, cls2, cls2, TextPaint.class, cls2, Layout.Alignment.class, cls, cls3, cls3, Boolean.TYPE, TextUtils.TruncateAt.class, cls2, cls2);
            f18126p = declaredConstructor;
            declaredConstructor.setAccessible(true);
            f18125o = true;
        } catch (Exception e8) {
            throw new a(e8);
        }
    }

    public static i c(CharSequence charSequence, TextPaint textPaint, int i8) {
        return new i(charSequence, textPaint, i8);
    }

    public StaticLayout a() {
        if (this.f18127a == null) {
            this.f18127a = BuildConfig.FLAVOR;
        }
        int max = Math.max(0, this.f18129c);
        CharSequence charSequence = this.f18127a;
        if (this.f18133g == 1) {
            charSequence = TextUtils.ellipsize(charSequence, this.f18128b, max, this.f18139m);
        }
        int min = Math.min(charSequence.length(), this.f18131e);
        this.f18131e = min;
        if (Build.VERSION.SDK_INT < 23) {
            b();
            try {
                return (StaticLayout) ((Constructor) androidx.core.util.h.h(f18126p)).newInstance(charSequence, Integer.valueOf(this.f18130d), Integer.valueOf(this.f18131e), this.f18128b, Integer.valueOf(max), this.f18132f, androidx.core.util.h.h(q), Float.valueOf(1.0f), Float.valueOf(0.0f), Boolean.valueOf(this.f18137k), null, Integer.valueOf(max), Integer.valueOf(this.f18133g));
            } catch (Exception e8) {
                throw new a(e8);
            }
        }
        if (this.f18138l && this.f18133g == 1) {
            this.f18132f = Layout.Alignment.ALIGN_OPPOSITE;
        }
        StaticLayout.Builder obtain = StaticLayout.Builder.obtain(charSequence, this.f18130d, min, this.f18128b, max);
        obtain.setAlignment(this.f18132f);
        obtain.setIncludePad(this.f18137k);
        obtain.setTextDirection(this.f18138l ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR);
        TextUtils.TruncateAt truncateAt = this.f18139m;
        if (truncateAt != null) {
            obtain.setEllipsize(truncateAt);
        }
        obtain.setMaxLines(this.f18133g);
        float f5 = this.f18134h;
        if (f5 != 0.0f || this.f18135i != 1.0f) {
            obtain.setLineSpacing(f5, this.f18135i);
        }
        if (this.f18133g > 1) {
            obtain.setHyphenationFrequency(this.f18136j);
        }
        return obtain.build();
    }

    public i d(Layout.Alignment alignment) {
        this.f18132f = alignment;
        return this;
    }

    public i e(TextUtils.TruncateAt truncateAt) {
        this.f18139m = truncateAt;
        return this;
    }

    public i f(int i8) {
        this.f18136j = i8;
        return this;
    }

    public i g(boolean z4) {
        this.f18137k = z4;
        return this;
    }

    public i h(boolean z4) {
        this.f18138l = z4;
        return this;
    }

    public i i(float f5, float f8) {
        this.f18134h = f5;
        this.f18135i = f8;
        return this;
    }

    public i j(int i8) {
        this.f18133g = i8;
        return this;
    }
}
