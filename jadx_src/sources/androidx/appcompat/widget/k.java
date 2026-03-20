package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k {

    /* renamed from: a  reason: collision with root package name */
    private final ImageView f1508a;

    /* renamed from: b  reason: collision with root package name */
    private h0 f1509b;

    /* renamed from: c  reason: collision with root package name */
    private h0 f1510c;

    /* renamed from: d  reason: collision with root package name */
    private h0 f1511d;

    /* renamed from: e  reason: collision with root package name */
    private int f1512e = 0;

    public k(ImageView imageView) {
        this.f1508a = imageView;
    }

    private boolean a(Drawable drawable) {
        if (this.f1511d == null) {
            this.f1511d = new h0();
        }
        h0 h0Var = this.f1511d;
        h0Var.a();
        ColorStateList a9 = androidx.core.widget.g.a(this.f1508a);
        if (a9 != null) {
            h0Var.f1501d = true;
            h0Var.f1498a = a9;
        }
        PorterDuff.Mode b9 = androidx.core.widget.g.b(this.f1508a);
        if (b9 != null) {
            h0Var.f1500c = true;
            h0Var.f1499b = b9;
        }
        if (h0Var.f1501d || h0Var.f1500c) {
            g.i(drawable, h0Var, this.f1508a.getDrawableState());
            return true;
        }
        return false;
    }

    private boolean l() {
        int i8 = Build.VERSION.SDK_INT;
        return i8 > 21 ? this.f1509b != null : i8 == 21;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        if (this.f1508a.getDrawable() != null) {
            this.f1508a.getDrawable().setLevel(this.f1512e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c() {
        Drawable drawable = this.f1508a.getDrawable();
        if (drawable != null) {
            t.b(drawable);
        }
        if (drawable != null) {
            if (l() && a(drawable)) {
                return;
            }
            h0 h0Var = this.f1510c;
            if (h0Var != null) {
                g.i(drawable, h0Var, this.f1508a.getDrawableState());
                return;
            }
            h0 h0Var2 = this.f1509b;
            if (h0Var2 != null) {
                g.i(drawable, h0Var2, this.f1508a.getDrawableState());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList d() {
        h0 h0Var = this.f1510c;
        if (h0Var != null) {
            return h0Var.f1498a;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PorterDuff.Mode e() {
        h0 h0Var = this.f1510c;
        if (h0Var != null) {
            return h0Var.f1499b;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean f() {
        return Build.VERSION.SDK_INT < 21 || !(this.f1508a.getBackground() instanceof RippleDrawable);
    }

    public void g(AttributeSet attributeSet, int i8) {
        int n8;
        Context context = this.f1508a.getContext();
        int[] iArr = g.j.R;
        j0 v8 = j0.v(context, attributeSet, iArr, i8, 0);
        ImageView imageView = this.f1508a;
        androidx.core.view.c0.r0(imageView, imageView.getContext(), iArr, attributeSet, v8.r(), i8, 0);
        try {
            Drawable drawable = this.f1508a.getDrawable();
            if (drawable == null && (n8 = v8.n(g.j.S, -1)) != -1 && (drawable = h.a.b(this.f1508a.getContext(), n8)) != null) {
                this.f1508a.setImageDrawable(drawable);
            }
            if (drawable != null) {
                t.b(drawable);
            }
            int i9 = g.j.T;
            if (v8.s(i9)) {
                androidx.core.widget.g.c(this.f1508a, v8.c(i9));
            }
            int i10 = g.j.U;
            if (v8.s(i10)) {
                androidx.core.widget.g.d(this.f1508a, t.e(v8.k(i10, -1), null));
            }
        } finally {
            v8.w();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(Drawable drawable) {
        this.f1512e = drawable.getLevel();
    }

    public void i(int i8) {
        if (i8 != 0) {
            Drawable b9 = h.a.b(this.f1508a.getContext(), i8);
            if (b9 != null) {
                t.b(b9);
            }
            this.f1508a.setImageDrawable(b9);
        } else {
            this.f1508a.setImageDrawable(null);
        }
        c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j(ColorStateList colorStateList) {
        if (this.f1510c == null) {
            this.f1510c = new h0();
        }
        h0 h0Var = this.f1510c;
        h0Var.f1498a = colorStateList;
        h0Var.f1501d = true;
        c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(PorterDuff.Mode mode) {
        if (this.f1510c == null) {
            this.f1510c = new h0();
        }
        h0 h0Var = this.f1510c;
        h0Var.f1499b = mode;
        h0Var.f1500c = true;
        c();
    }
}
