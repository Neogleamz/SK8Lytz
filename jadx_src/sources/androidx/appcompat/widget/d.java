package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class d {

    /* renamed from: a  reason: collision with root package name */
    private final View f1442a;

    /* renamed from: d  reason: collision with root package name */
    private h0 f1445d;

    /* renamed from: e  reason: collision with root package name */
    private h0 f1446e;

    /* renamed from: f  reason: collision with root package name */
    private h0 f1447f;

    /* renamed from: c  reason: collision with root package name */
    private int f1444c = -1;

    /* renamed from: b  reason: collision with root package name */
    private final g f1443b = g.b();

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(View view) {
        this.f1442a = view;
    }

    private boolean a(Drawable drawable) {
        if (this.f1447f == null) {
            this.f1447f = new h0();
        }
        h0 h0Var = this.f1447f;
        h0Var.a();
        ColorStateList u8 = androidx.core.view.c0.u(this.f1442a);
        if (u8 != null) {
            h0Var.f1501d = true;
            h0Var.f1498a = u8;
        }
        PorterDuff.Mode v8 = androidx.core.view.c0.v(this.f1442a);
        if (v8 != null) {
            h0Var.f1500c = true;
            h0Var.f1499b = v8;
        }
        if (h0Var.f1501d || h0Var.f1500c) {
            g.i(drawable, h0Var, this.f1442a.getDrawableState());
            return true;
        }
        return false;
    }

    private boolean k() {
        int i8 = Build.VERSION.SDK_INT;
        return i8 > 21 ? this.f1445d != null : i8 == 21;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        Drawable background = this.f1442a.getBackground();
        if (background != null) {
            if (k() && a(background)) {
                return;
            }
            h0 h0Var = this.f1446e;
            if (h0Var != null) {
                g.i(background, h0Var, this.f1442a.getDrawableState());
                return;
            }
            h0 h0Var2 = this.f1445d;
            if (h0Var2 != null) {
                g.i(background, h0Var2, this.f1442a.getDrawableState());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList c() {
        h0 h0Var = this.f1446e;
        if (h0Var != null) {
            return h0Var.f1498a;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PorterDuff.Mode d() {
        h0 h0Var = this.f1446e;
        if (h0Var != null) {
            return h0Var.f1499b;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(AttributeSet attributeSet, int i8) {
        Context context = this.f1442a.getContext();
        int[] iArr = g.j.W3;
        j0 v8 = j0.v(context, attributeSet, iArr, i8, 0);
        View view = this.f1442a;
        androidx.core.view.c0.r0(view, view.getContext(), iArr, attributeSet, v8.r(), i8, 0);
        try {
            int i9 = g.j.X3;
            if (v8.s(i9)) {
                this.f1444c = v8.n(i9, -1);
                ColorStateList f5 = this.f1443b.f(this.f1442a.getContext(), this.f1444c);
                if (f5 != null) {
                    h(f5);
                }
            }
            int i10 = g.j.Y3;
            if (v8.s(i10)) {
                androidx.core.view.c0.y0(this.f1442a, v8.c(i10));
            }
            int i11 = g.j.Z3;
            if (v8.s(i11)) {
                androidx.core.view.c0.z0(this.f1442a, t.e(v8.k(i11, -1), null));
            }
        } finally {
            v8.w();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(Drawable drawable) {
        this.f1444c = -1;
        h(null);
        b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(int i8) {
        this.f1444c = i8;
        g gVar = this.f1443b;
        h(gVar != null ? gVar.f(this.f1442a.getContext(), i8) : null);
        b();
    }

    void h(ColorStateList colorStateList) {
        if (colorStateList != null) {
            if (this.f1445d == null) {
                this.f1445d = new h0();
            }
            h0 h0Var = this.f1445d;
            h0Var.f1498a = colorStateList;
            h0Var.f1501d = true;
        } else {
            this.f1445d = null;
        }
        b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i(ColorStateList colorStateList) {
        if (this.f1446e == null) {
            this.f1446e = new h0();
        }
        h0 h0Var = this.f1446e;
        h0Var.f1498a = colorStateList;
        h0Var.f1501d = true;
        b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j(PorterDuff.Mode mode) {
        if (this.f1446e == null) {
            this.f1446e = new h0();
        }
        h0 h0Var = this.f1446e;
        h0Var.f1499b = mode;
        h0Var.f1500c = true;
        b();
    }
}
