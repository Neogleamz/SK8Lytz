package u7;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.Log;
import androidx.core.content.res.h;
import k7.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d {

    /* renamed from: a  reason: collision with root package name */
    public final ColorStateList f23093a;

    /* renamed from: b  reason: collision with root package name */
    public final ColorStateList f23094b;

    /* renamed from: c  reason: collision with root package name */
    public final ColorStateList f23095c;

    /* renamed from: d  reason: collision with root package name */
    public final ColorStateList f23096d;

    /* renamed from: e  reason: collision with root package name */
    public final String f23097e;

    /* renamed from: f  reason: collision with root package name */
    public final int f23098f;

    /* renamed from: g  reason: collision with root package name */
    public final int f23099g;

    /* renamed from: h  reason: collision with root package name */
    public final boolean f23100h;

    /* renamed from: i  reason: collision with root package name */
    public final float f23101i;

    /* renamed from: j  reason: collision with root package name */
    public final float f23102j;

    /* renamed from: k  reason: collision with root package name */
    public final float f23103k;

    /* renamed from: l  reason: collision with root package name */
    public final boolean f23104l;

    /* renamed from: m  reason: collision with root package name */
    public final float f23105m;

    /* renamed from: n  reason: collision with root package name */
    public float f23106n;

    /* renamed from: o  reason: collision with root package name */
    private final int f23107o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f23108p = false;
    private Typeface q;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends h.f {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ f f23109a;

        a(f fVar) {
            this.f23109a = fVar;
        }

        @Override // androidx.core.content.res.h.f
        public void h(int i8) {
            d.this.f23108p = true;
            this.f23109a.a(i8);
        }

        @Override // androidx.core.content.res.h.f
        public void i(Typeface typeface) {
            d dVar = d.this;
            dVar.q = Typeface.create(typeface, dVar.f23098f);
            d.this.f23108p = true;
            this.f23109a.b(d.this.q, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends f {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ TextPaint f23111a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ f f23112b;

        b(TextPaint textPaint, f fVar) {
            this.f23111a = textPaint;
            this.f23112b = fVar;
        }

        @Override // u7.f
        public void a(int i8) {
            this.f23112b.a(i8);
        }

        @Override // u7.f
        public void b(Typeface typeface, boolean z4) {
            d.this.l(this.f23111a, typeface);
            this.f23112b.b(typeface, z4);
        }
    }

    public d(Context context, int i8) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(i8, l.X7);
        this.f23106n = obtainStyledAttributes.getDimension(l.Y7, 0.0f);
        this.f23093a = c.a(context, obtainStyledAttributes, l.f21274b8);
        this.f23094b = c.a(context, obtainStyledAttributes, l.f21284c8);
        this.f23095c = c.a(context, obtainStyledAttributes, l.d8);
        this.f23098f = obtainStyledAttributes.getInt(l.f21264a8, 0);
        this.f23099g = obtainStyledAttributes.getInt(l.Z7, 1);
        int e8 = c.e(obtainStyledAttributes, l.j8, l.i8);
        this.f23107o = obtainStyledAttributes.getResourceId(e8, 0);
        this.f23097e = obtainStyledAttributes.getString(e8);
        this.f23100h = obtainStyledAttributes.getBoolean(l.k8, false);
        this.f23096d = c.a(context, obtainStyledAttributes, l.e8);
        this.f23101i = obtainStyledAttributes.getFloat(l.f8, 0.0f);
        this.f23102j = obtainStyledAttributes.getFloat(l.g8, 0.0f);
        this.f23103k = obtainStyledAttributes.getFloat(l.h8, 0.0f);
        obtainStyledAttributes.recycle();
        if (Build.VERSION.SDK_INT < 21) {
            this.f23104l = false;
            this.f23105m = 0.0f;
            return;
        }
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(i8, l.L4);
        int i9 = l.M4;
        this.f23104l = obtainStyledAttributes2.hasValue(i9);
        this.f23105m = obtainStyledAttributes2.getFloat(i9, 0.0f);
        obtainStyledAttributes2.recycle();
    }

    private void d() {
        String str;
        if (this.q == null && (str = this.f23097e) != null) {
            this.q = Typeface.create(str, this.f23098f);
        }
        if (this.q == null) {
            int i8 = this.f23099g;
            this.q = i8 != 1 ? i8 != 2 ? i8 != 3 ? Typeface.DEFAULT : Typeface.MONOSPACE : Typeface.SERIF : Typeface.SANS_SERIF;
            this.q = Typeface.create(this.q, this.f23098f);
        }
    }

    private boolean i(Context context) {
        if (e.a()) {
            return true;
        }
        int i8 = this.f23107o;
        return (i8 != 0 ? h.c(context, i8) : null) != null;
    }

    public Typeface e() {
        d();
        return this.q;
    }

    public Typeface f(Context context) {
        if (this.f23108p) {
            return this.q;
        }
        if (!context.isRestricted()) {
            try {
                Typeface g8 = h.g(context, this.f23107o);
                this.q = g8;
                if (g8 != null) {
                    this.q = Typeface.create(g8, this.f23098f);
                }
            } catch (Resources.NotFoundException | UnsupportedOperationException unused) {
            } catch (Exception e8) {
                Log.d("TextAppearance", "Error loading font " + this.f23097e, e8);
            }
        }
        d();
        this.f23108p = true;
        return this.q;
    }

    public void g(Context context, TextPaint textPaint, f fVar) {
        l(textPaint, e());
        h(context, new b(textPaint, fVar));
    }

    public void h(Context context, f fVar) {
        if (i(context)) {
            f(context);
        } else {
            d();
        }
        int i8 = this.f23107o;
        if (i8 == 0) {
            this.f23108p = true;
        }
        if (this.f23108p) {
            fVar.b(this.q, true);
            return;
        }
        try {
            h.i(context, i8, new a(fVar), null);
        } catch (Resources.NotFoundException unused) {
            this.f23108p = true;
            fVar.a(1);
        } catch (Exception e8) {
            Log.d("TextAppearance", "Error loading font " + this.f23097e, e8);
            this.f23108p = true;
            fVar.a(-3);
        }
    }

    public void j(Context context, TextPaint textPaint, f fVar) {
        k(context, textPaint, fVar);
        ColorStateList colorStateList = this.f23093a;
        textPaint.setColor(colorStateList != null ? colorStateList.getColorForState(textPaint.drawableState, colorStateList.getDefaultColor()) : -16777216);
        float f5 = this.f23103k;
        float f8 = this.f23101i;
        float f9 = this.f23102j;
        ColorStateList colorStateList2 = this.f23096d;
        textPaint.setShadowLayer(f5, f8, f9, colorStateList2 != null ? colorStateList2.getColorForState(textPaint.drawableState, colorStateList2.getDefaultColor()) : 0);
    }

    public void k(Context context, TextPaint textPaint, f fVar) {
        if (i(context)) {
            l(textPaint, f(context));
        } else {
            g(context, textPaint, fVar);
        }
    }

    public void l(TextPaint textPaint, Typeface typeface) {
        textPaint.setTypeface(typeface);
        int i8 = (~typeface.getStyle()) & this.f23098f;
        textPaint.setFakeBoldText((i8 & 1) != 0);
        textPaint.setTextSkewX((i8 & 2) != 0 ? -0.25f : 0.0f);
        textPaint.setTextSize(this.f23106n);
        if (Build.VERSION.SDK_INT < 21 || !this.f23104l) {
            return;
        }
        textPaint.setLetterSpacing(this.f23105m);
    }
}
