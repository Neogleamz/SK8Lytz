package w0;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import androidx.core.view.accessibility.d;
import androidx.core.view.accessibility.e;
import androidx.core.view.c0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayList;
import java.util.List;
import k0.h;
import w0.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a extends androidx.core.view.a {

    /* renamed from: n  reason: collision with root package name */
    private static final Rect f23374n = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

    /* renamed from: o  reason: collision with root package name */
    private static final b.a<androidx.core.view.accessibility.c> f23375o = new C0219a();

    /* renamed from: p  reason: collision with root package name */
    private static final b.InterfaceC0220b<h<androidx.core.view.accessibility.c>, androidx.core.view.accessibility.c> f23376p = new b();

    /* renamed from: h  reason: collision with root package name */
    private final AccessibilityManager f23381h;

    /* renamed from: i  reason: collision with root package name */
    private final View f23382i;

    /* renamed from: j  reason: collision with root package name */
    private c f23383j;

    /* renamed from: d  reason: collision with root package name */
    private final Rect f23377d = new Rect();

    /* renamed from: e  reason: collision with root package name */
    private final Rect f23378e = new Rect();

    /* renamed from: f  reason: collision with root package name */
    private final Rect f23379f = new Rect();

    /* renamed from: g  reason: collision with root package name */
    private final int[] f23380g = new int[2];

    /* renamed from: k  reason: collision with root package name */
    int f23384k = Integer.MIN_VALUE;

    /* renamed from: l  reason: collision with root package name */
    int f23385l = Integer.MIN_VALUE;

    /* renamed from: m  reason: collision with root package name */
    private int f23386m = Integer.MIN_VALUE;

    /* renamed from: w0.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class C0219a implements b.a<androidx.core.view.accessibility.c> {
        C0219a() {
        }

        @Override // w0.b.a
        /* renamed from: b */
        public void a(androidx.core.view.accessibility.c cVar, Rect rect) {
            cVar.m(rect);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements b.InterfaceC0220b<h<androidx.core.view.accessibility.c>, androidx.core.view.accessibility.c> {
        b() {
        }

        @Override // w0.b.InterfaceC0220b
        /* renamed from: c */
        public androidx.core.view.accessibility.c a(h<androidx.core.view.accessibility.c> hVar, int i8) {
            return hVar.q(i8);
        }

        @Override // w0.b.InterfaceC0220b
        /* renamed from: d */
        public int b(h<androidx.core.view.accessibility.c> hVar) {
            return hVar.o();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class c extends d {
        c() {
        }

        @Override // androidx.core.view.accessibility.d
        public androidx.core.view.accessibility.c b(int i8) {
            return androidx.core.view.accessibility.c.Q(a.this.J(i8));
        }

        @Override // androidx.core.view.accessibility.d
        public androidx.core.view.accessibility.c d(int i8) {
            int i9 = i8 == 2 ? a.this.f23384k : a.this.f23385l;
            if (i9 == Integer.MIN_VALUE) {
                return null;
            }
            return b(i9);
        }

        @Override // androidx.core.view.accessibility.d
        public boolean f(int i8, int i9, Bundle bundle) {
            return a.this.R(i8, i9, bundle);
        }
    }

    public a(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View may not be null");
        }
        this.f23382i = view;
        this.f23381h = (AccessibilityManager) view.getContext().getSystemService("accessibility");
        view.setFocusable(true);
        if (c0.C(view) == 0) {
            c0.E0(view, 1);
        }
    }

    private static Rect D(View view, int i8, Rect rect) {
        int width = view.getWidth();
        int height = view.getHeight();
        if (i8 == 17) {
            rect.set(width, 0, width, height);
        } else if (i8 == 33) {
            rect.set(0, height, width, height);
        } else if (i8 == 66) {
            rect.set(-1, 0, -1, height);
        } else if (i8 != 130) {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        } else {
            rect.set(0, -1, width, -1);
        }
        return rect;
    }

    private boolean G(Rect rect) {
        if (rect == null || rect.isEmpty() || this.f23382i.getWindowVisibility() != 0) {
            return false;
        }
        View view = this.f23382i;
        do {
            ViewParent parent = view.getParent();
            if (!(parent instanceof View)) {
                return parent != null;
            }
            view = (View) parent;
            if (view.getAlpha() <= 0.0f) {
                break;
            }
        } while (view.getVisibility() == 0);
        return false;
    }

    private static int H(int i8) {
        if (i8 != 19) {
            if (i8 != 21) {
                return i8 != 22 ? 130 : 66;
            }
            return 17;
        }
        return 33;
    }

    private boolean I(int i8, Rect rect) {
        Object d8;
        h<androidx.core.view.accessibility.c> y8 = y();
        int i9 = this.f23385l;
        androidx.core.view.accessibility.c f5 = i9 == Integer.MIN_VALUE ? null : y8.f(i9);
        if (i8 == 1 || i8 == 2) {
            d8 = w0.b.d(y8, f23376p, f23375o, f5, i8, c0.E(this.f23382i) == 1, false);
        } else if (i8 != 17 && i8 != 33 && i8 != 66 && i8 != 130) {
            throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD, FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        } else {
            Rect rect2 = new Rect();
            int i10 = this.f23385l;
            if (i10 != Integer.MIN_VALUE) {
                z(i10, rect2);
            } else if (rect != null) {
                rect2.set(rect);
            } else {
                D(this.f23382i, i8, rect2);
            }
            d8 = w0.b.c(y8, f23376p, f23375o, f5, rect2, i8);
        }
        androidx.core.view.accessibility.c cVar = (androidx.core.view.accessibility.c) d8;
        return V(cVar != null ? y8.k(y8.j(cVar)) : Integer.MIN_VALUE);
    }

    private boolean S(int i8, int i9, Bundle bundle) {
        return i9 != 1 ? i9 != 2 ? i9 != 64 ? i9 != 128 ? L(i8, i9, bundle) : n(i8) : U(i8) : o(i8) : V(i8);
    }

    private boolean T(int i8, Bundle bundle) {
        return c0.h0(this.f23382i, i8, bundle);
    }

    private boolean U(int i8) {
        int i9;
        if (this.f23381h.isEnabled() && this.f23381h.isTouchExplorationEnabled() && (i9 = this.f23384k) != i8) {
            if (i9 != Integer.MIN_VALUE) {
                n(i9);
            }
            this.f23384k = i8;
            this.f23382i.invalidate();
            W(i8, RecognitionOptions.TEZ_CODE);
            return true;
        }
        return false;
    }

    private void X(int i8) {
        int i9 = this.f23386m;
        if (i9 == i8) {
            return;
        }
        this.f23386m = i8;
        W(i8, RecognitionOptions.ITF);
        W(i9, RecognitionOptions.QR_CODE);
    }

    private boolean n(int i8) {
        if (this.f23384k == i8) {
            this.f23384k = Integer.MIN_VALUE;
            this.f23382i.invalidate();
            W(i8, 65536);
            return true;
        }
        return false;
    }

    private boolean p() {
        int i8 = this.f23385l;
        return i8 != Integer.MIN_VALUE && L(i8, 16, null);
    }

    private AccessibilityEvent q(int i8, int i9) {
        return i8 != -1 ? r(i8, i9) : s(i9);
    }

    private AccessibilityEvent r(int i8, int i9) {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i9);
        androidx.core.view.accessibility.c J = J(i8);
        obtain.getText().add(J.x());
        obtain.setContentDescription(J.r());
        obtain.setScrollable(J.K());
        obtain.setPassword(J.J());
        obtain.setEnabled(J.F());
        obtain.setChecked(J.D());
        N(i8, obtain);
        if (obtain.getText().isEmpty() && obtain.getContentDescription() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
        }
        obtain.setClassName(J.p());
        e.c(obtain, this.f23382i, i8);
        obtain.setPackageName(this.f23382i.getContext().getPackageName());
        return obtain;
    }

    private AccessibilityEvent s(int i8) {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i8);
        this.f23382i.onInitializeAccessibilityEvent(obtain);
        return obtain;
    }

    private androidx.core.view.accessibility.c t(int i8) {
        androidx.core.view.accessibility.c O = androidx.core.view.accessibility.c.O();
        O.i0(true);
        O.k0(true);
        O.c0("android.view.View");
        Rect rect = f23374n;
        O.X(rect);
        O.Y(rect);
        O.t0(this.f23382i);
        P(i8, O);
        if (O.x() == null && O.r() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
        }
        O.m(this.f23378e);
        if (this.f23378e.equals(rect)) {
            throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
        }
        int k8 = O.k();
        if ((k8 & 64) == 0) {
            if ((k8 & RecognitionOptions.ITF) == 0) {
                O.r0(this.f23382i.getContext().getPackageName());
                O.C0(this.f23382i, i8);
                if (this.f23384k == i8) {
                    O.V(true);
                    O.a(RecognitionOptions.ITF);
                } else {
                    O.V(false);
                    O.a(64);
                }
                boolean z4 = this.f23385l == i8;
                if (z4) {
                    O.a(2);
                } else if (O.G()) {
                    O.a(1);
                }
                O.l0(z4);
                this.f23382i.getLocationOnScreen(this.f23380g);
                O.n(this.f23377d);
                if (this.f23377d.equals(rect)) {
                    O.m(this.f23377d);
                    if (O.f4905b != -1) {
                        androidx.core.view.accessibility.c O2 = androidx.core.view.accessibility.c.O();
                        for (int i9 = O.f4905b; i9 != -1; i9 = O2.f4905b) {
                            O2.u0(this.f23382i, -1);
                            O2.X(f23374n);
                            P(i9, O2);
                            O2.m(this.f23378e);
                            Rect rect2 = this.f23377d;
                            Rect rect3 = this.f23378e;
                            rect2.offset(rect3.left, rect3.top);
                        }
                        O2.S();
                    }
                    this.f23377d.offset(this.f23380g[0] - this.f23382i.getScrollX(), this.f23380g[1] - this.f23382i.getScrollY());
                }
                if (this.f23382i.getLocalVisibleRect(this.f23379f)) {
                    this.f23379f.offset(this.f23380g[0] - this.f23382i.getScrollX(), this.f23380g[1] - this.f23382i.getScrollY());
                    if (this.f23377d.intersect(this.f23379f)) {
                        O.Y(this.f23377d);
                        if (G(this.f23377d)) {
                            O.G0(true);
                        }
                    }
                }
                return O;
            }
            throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
        }
        throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
    }

    private androidx.core.view.accessibility.c u() {
        androidx.core.view.accessibility.c P = androidx.core.view.accessibility.c.P(this.f23382i);
        c0.f0(this.f23382i, P);
        ArrayList arrayList = new ArrayList();
        C(arrayList);
        if (P.o() <= 0 || arrayList.size() <= 0) {
            int size = arrayList.size();
            for (int i8 = 0; i8 < size; i8++) {
                P.d(this.f23382i, ((Integer) arrayList.get(i8)).intValue());
            }
            return P;
        }
        throw new RuntimeException("Views cannot have both real and virtual children");
    }

    private h<androidx.core.view.accessibility.c> y() {
        ArrayList arrayList = new ArrayList();
        C(arrayList);
        h<androidx.core.view.accessibility.c> hVar = new h<>();
        for (int i8 = 0; i8 < arrayList.size(); i8++) {
            hVar.l(arrayList.get(i8).intValue(), t(arrayList.get(i8).intValue()));
        }
        return hVar;
    }

    private void z(int i8, Rect rect) {
        J(i8).m(rect);
    }

    public final int A() {
        return this.f23385l;
    }

    protected abstract int B(float f5, float f8);

    protected abstract void C(List<Integer> list);

    public final void E(int i8) {
        F(i8, 0);
    }

    public final void F(int i8, int i9) {
        ViewParent parent;
        if (i8 == Integer.MIN_VALUE || !this.f23381h.isEnabled() || (parent = this.f23382i.getParent()) == null) {
            return;
        }
        AccessibilityEvent q = q(i8, RecognitionOptions.PDF417);
        androidx.core.view.accessibility.b.b(q, i9);
        parent.requestSendAccessibilityEvent(this.f23382i, q);
    }

    androidx.core.view.accessibility.c J(int i8) {
        return i8 == -1 ? u() : t(i8);
    }

    public final void K(boolean z4, int i8, Rect rect) {
        int i9 = this.f23385l;
        if (i9 != Integer.MIN_VALUE) {
            o(i9);
        }
        if (z4) {
            I(i8, rect);
        }
    }

    protected abstract boolean L(int i8, int i9, Bundle bundle);

    protected void M(AccessibilityEvent accessibilityEvent) {
    }

    protected void N(int i8, AccessibilityEvent accessibilityEvent) {
    }

    protected void O(androidx.core.view.accessibility.c cVar) {
    }

    protected abstract void P(int i8, androidx.core.view.accessibility.c cVar);

    protected void Q(int i8, boolean z4) {
    }

    boolean R(int i8, int i9, Bundle bundle) {
        return i8 != -1 ? S(i8, i9, bundle) : T(i9, bundle);
    }

    public final boolean V(int i8) {
        int i9;
        if ((this.f23382i.isFocused() || this.f23382i.requestFocus()) && (i9 = this.f23385l) != i8) {
            if (i9 != Integer.MIN_VALUE) {
                o(i9);
            }
            if (i8 == Integer.MIN_VALUE) {
                return false;
            }
            this.f23385l = i8;
            Q(i8, true);
            W(i8, 8);
            return true;
        }
        return false;
    }

    public final boolean W(int i8, int i9) {
        ViewParent parent;
        if (i8 == Integer.MIN_VALUE || !this.f23381h.isEnabled() || (parent = this.f23382i.getParent()) == null) {
            return false;
        }
        return parent.requestSendAccessibilityEvent(this.f23382i, q(i8, i9));
    }

    @Override // androidx.core.view.a
    public d b(View view) {
        if (this.f23383j == null) {
            this.f23383j = new c();
        }
        return this.f23383j;
    }

    @Override // androidx.core.view.a
    public void f(View view, AccessibilityEvent accessibilityEvent) {
        super.f(view, accessibilityEvent);
        M(accessibilityEvent);
    }

    @Override // androidx.core.view.a
    public void g(View view, androidx.core.view.accessibility.c cVar) {
        super.g(view, cVar);
        O(cVar);
    }

    public final boolean o(int i8) {
        if (this.f23385l != i8) {
            return false;
        }
        this.f23385l = Integer.MIN_VALUE;
        Q(i8, false);
        W(i8, 8);
        return true;
    }

    public final boolean v(MotionEvent motionEvent) {
        if (this.f23381h.isEnabled() && this.f23381h.isTouchExplorationEnabled()) {
            int action = motionEvent.getAction();
            if (action == 7 || action == 9) {
                int B = B(motionEvent.getX(), motionEvent.getY());
                X(B);
                return B != Integer.MIN_VALUE;
            } else if (action == 10 && this.f23386m != Integer.MIN_VALUE) {
                X(Integer.MIN_VALUE);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public final boolean w(KeyEvent keyEvent) {
        int i8 = 0;
        if (keyEvent.getAction() != 1) {
            int keyCode = keyEvent.getKeyCode();
            if (keyCode == 61) {
                if (keyEvent.hasNoModifiers()) {
                    return I(2, null);
                }
                if (keyEvent.hasModifiers(1)) {
                    return I(1, null);
                }
                return false;
            }
            if (keyCode != 66) {
                switch (keyCode) {
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                        if (keyEvent.hasNoModifiers()) {
                            int H = H(keyCode);
                            int repeatCount = keyEvent.getRepeatCount() + 1;
                            boolean z4 = false;
                            while (i8 < repeatCount && I(H, null)) {
                                i8++;
                                z4 = true;
                            }
                            return z4;
                        }
                        return false;
                    case 23:
                        break;
                    default:
                        return false;
                }
            }
            if (keyEvent.hasNoModifiers() && keyEvent.getRepeatCount() == 0) {
                p();
                return true;
            }
            return false;
        }
        return false;
    }

    public final int x() {
        return this.f23384k;
    }
}
