package androidx.core.view;

import android.view.View;
import android.view.ViewParent;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class q {

    /* renamed from: a  reason: collision with root package name */
    private ViewParent f5076a;

    /* renamed from: b  reason: collision with root package name */
    private ViewParent f5077b;

    /* renamed from: c  reason: collision with root package name */
    private final View f5078c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f5079d;

    /* renamed from: e  reason: collision with root package name */
    private int[] f5080e;

    public q(View view) {
        this.f5078c = view;
    }

    private boolean g(int i8, int i9, int i10, int i11, int[] iArr, int i12, int[] iArr2) {
        ViewParent h8;
        int i13;
        int i14;
        int[] iArr3;
        if (!l() || (h8 = h(i12)) == null) {
            return false;
        }
        if (i8 == 0 && i9 == 0 && i10 == 0 && i11 == 0) {
            if (iArr != null) {
                iArr[0] = 0;
                iArr[1] = 0;
            }
            return false;
        }
        if (iArr != null) {
            this.f5078c.getLocationInWindow(iArr);
            i13 = iArr[0];
            i14 = iArr[1];
        } else {
            i13 = 0;
            i14 = 0;
        }
        if (iArr2 == null) {
            int[] i15 = i();
            i15[0] = 0;
            i15[1] = 0;
            iArr3 = i15;
        } else {
            iArr3 = iArr2;
        }
        g0.d(h8, this.f5078c, i8, i9, i10, i11, i12, iArr3);
        if (iArr != null) {
            this.f5078c.getLocationInWindow(iArr);
            iArr[0] = iArr[0] - i13;
            iArr[1] = iArr[1] - i14;
        }
        return true;
    }

    private ViewParent h(int i8) {
        if (i8 != 0) {
            if (i8 != 1) {
                return null;
            }
            return this.f5077b;
        }
        return this.f5076a;
    }

    private int[] i() {
        if (this.f5080e == null) {
            this.f5080e = new int[2];
        }
        return this.f5080e;
    }

    private void n(int i8, ViewParent viewParent) {
        if (i8 == 0) {
            this.f5076a = viewParent;
        } else if (i8 != 1) {
        } else {
            this.f5077b = viewParent;
        }
    }

    public boolean a(float f5, float f8, boolean z4) {
        ViewParent h8;
        if (!l() || (h8 = h(0)) == null) {
            return false;
        }
        return g0.a(h8, this.f5078c, f5, f8, z4);
    }

    public boolean b(float f5, float f8) {
        ViewParent h8;
        if (!l() || (h8 = h(0)) == null) {
            return false;
        }
        return g0.b(h8, this.f5078c, f5, f8);
    }

    public boolean c(int i8, int i9, int[] iArr, int[] iArr2) {
        return d(i8, i9, iArr, iArr2, 0);
    }

    public boolean d(int i8, int i9, int[] iArr, int[] iArr2, int i10) {
        ViewParent h8;
        int i11;
        int i12;
        if (!l() || (h8 = h(i10)) == null) {
            return false;
        }
        if (i8 == 0 && i9 == 0) {
            if (iArr2 != null) {
                iArr2[0] = 0;
                iArr2[1] = 0;
                return false;
            }
            return false;
        }
        if (iArr2 != null) {
            this.f5078c.getLocationInWindow(iArr2);
            i11 = iArr2[0];
            i12 = iArr2[1];
        } else {
            i11 = 0;
            i12 = 0;
        }
        if (iArr == null) {
            iArr = i();
        }
        iArr[0] = 0;
        iArr[1] = 0;
        g0.c(h8, this.f5078c, i8, i9, iArr, i10);
        if (iArr2 != null) {
            this.f5078c.getLocationInWindow(iArr2);
            iArr2[0] = iArr2[0] - i11;
            iArr2[1] = iArr2[1] - i12;
        }
        return (iArr[0] == 0 && iArr[1] == 0) ? false : true;
    }

    public void e(int i8, int i9, int i10, int i11, int[] iArr, int i12, int[] iArr2) {
        g(i8, i9, i10, i11, iArr, i12, iArr2);
    }

    public boolean f(int i8, int i9, int i10, int i11, int[] iArr) {
        return g(i8, i9, i10, i11, iArr, 0, null);
    }

    public boolean j() {
        return k(0);
    }

    public boolean k(int i8) {
        return h(i8) != null;
    }

    public boolean l() {
        return this.f5079d;
    }

    public void m(boolean z4) {
        if (this.f5079d) {
            c0.T0(this.f5078c);
        }
        this.f5079d = z4;
    }

    public boolean o(int i8) {
        return p(i8, 0);
    }

    public boolean p(int i8, int i9) {
        if (k(i9)) {
            return true;
        }
        if (l()) {
            View view = this.f5078c;
            for (ViewParent parent = this.f5078c.getParent(); parent != null; parent = parent.getParent()) {
                if (g0.f(parent, view, this.f5078c, i8, i9)) {
                    n(i9, parent);
                    g0.e(parent, view, this.f5078c, i8, i9);
                    return true;
                }
                if (parent instanceof View) {
                    view = (View) parent;
                }
            }
            return false;
        }
        return false;
    }

    public void q() {
        r(0);
    }

    public void r(int i8) {
        ViewParent h8 = h(i8);
        if (h8 != null) {
            g0.g(h8, this.f5078c, i8);
            n(i8, null);
        }
    }
}
