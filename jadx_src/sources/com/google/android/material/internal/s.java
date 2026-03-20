package com.google.android.material.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import androidx.core.view.c0;
import androidx.core.view.m0;
import androidx.core.view.v;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class s {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f18159a;

        a(View view) {
            this.f18159a = view;
        }

        @Override // java.lang.Runnable
        public void run() {
            ((InputMethodManager) this.f18159a.getContext().getSystemService("input_method")).showSoftInput(this.f18159a, 1);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b implements e {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ boolean f18160a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ boolean f18161b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ boolean f18162c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ e f18163d;

        b(boolean z4, boolean z8, boolean z9, e eVar) {
            this.f18160a = z4;
            this.f18161b = z8;
            this.f18162c = z9;
            this.f18163d = eVar;
        }

        @Override // com.google.android.material.internal.s.e
        public m0 a(View view, m0 m0Var, f fVar) {
            if (this.f18160a) {
                fVar.f18169d += m0Var.j();
            }
            boolean h8 = s.h(view);
            if (this.f18161b) {
                if (h8) {
                    fVar.f18168c += m0Var.k();
                } else {
                    fVar.f18166a += m0Var.k();
                }
            }
            if (this.f18162c) {
                if (h8) {
                    fVar.f18166a += m0Var.l();
                } else {
                    fVar.f18168c += m0Var.l();
                }
            }
            fVar.a(view);
            e eVar = this.f18163d;
            return eVar != null ? eVar.a(view, m0Var, fVar) : m0Var;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c implements v {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ e f18164a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ f f18165b;

        c(e eVar, f fVar) {
            this.f18164a = eVar;
            this.f18165b = fVar;
        }

        @Override // androidx.core.view.v
        public m0 a(View view, m0 m0Var) {
            return this.f18164a.a(view, m0Var, new f(this.f18165b));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d implements View.OnAttachStateChangeListener {
        d() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            view.removeOnAttachStateChangeListener(this);
            c0.q0(view);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface e {
        m0 a(View view, m0 m0Var, f fVar);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f {

        /* renamed from: a  reason: collision with root package name */
        public int f18166a;

        /* renamed from: b  reason: collision with root package name */
        public int f18167b;

        /* renamed from: c  reason: collision with root package name */
        public int f18168c;

        /* renamed from: d  reason: collision with root package name */
        public int f18169d;

        public f(int i8, int i9, int i10, int i11) {
            this.f18166a = i8;
            this.f18167b = i9;
            this.f18168c = i10;
            this.f18169d = i11;
        }

        public f(f fVar) {
            this.f18166a = fVar.f18166a;
            this.f18167b = fVar.f18167b;
            this.f18168c = fVar.f18168c;
            this.f18169d = fVar.f18169d;
        }

        public void a(View view) {
            c0.J0(view, this.f18166a, this.f18167b, this.f18168c, this.f18169d);
        }
    }

    public static void a(View view, AttributeSet attributeSet, int i8, int i9, e eVar) {
        TypedArray obtainStyledAttributes = view.getContext().obtainStyledAttributes(attributeSet, k7.l.f21315g3, i8, i9);
        boolean z4 = obtainStyledAttributes.getBoolean(k7.l.f21324h3, false);
        boolean z8 = obtainStyledAttributes.getBoolean(k7.l.f21333i3, false);
        boolean z9 = obtainStyledAttributes.getBoolean(k7.l.f21342j3, false);
        obtainStyledAttributes.recycle();
        b(view, new b(z4, z8, z9, eVar));
    }

    public static void b(View view, e eVar) {
        c0.I0(view, new c(eVar, new f(c0.J(view), view.getPaddingTop(), c0.I(view), view.getPaddingBottom())));
        j(view);
    }

    public static float c(Context context, int i8) {
        return TypedValue.applyDimension(1, i8, context.getResources().getDisplayMetrics());
    }

    public static ViewGroup d(View view) {
        if (view == null) {
            return null;
        }
        View rootView = view.getRootView();
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(16908290);
        if (viewGroup != null) {
            return viewGroup;
        }
        if (rootView == view || !(rootView instanceof ViewGroup)) {
            return null;
        }
        return (ViewGroup) rootView;
    }

    public static r e(View view) {
        return f(d(view));
    }

    public static r f(View view) {
        if (view == null) {
            return null;
        }
        return Build.VERSION.SDK_INT >= 18 ? new q(view) : p.c(view);
    }

    public static float g(View view) {
        float f5 = 0.0f;
        for (ViewParent parent = view.getParent(); parent instanceof View; parent = parent.getParent()) {
            f5 += c0.y((View) parent);
        }
        return f5;
    }

    public static boolean h(View view) {
        return c0.E(view) == 1;
    }

    public static PorterDuff.Mode i(int i8, PorterDuff.Mode mode) {
        if (i8 != 3) {
            if (i8 != 5) {
                if (i8 != 9) {
                    switch (i8) {
                        case 14:
                            return PorterDuff.Mode.MULTIPLY;
                        case 15:
                            return PorterDuff.Mode.SCREEN;
                        case 16:
                            return PorterDuff.Mode.ADD;
                        default:
                            return mode;
                    }
                }
                return PorterDuff.Mode.SRC_ATOP;
            }
            return PorterDuff.Mode.SRC_IN;
        }
        return PorterDuff.Mode.SRC_OVER;
    }

    public static void j(View view) {
        if (c0.V(view)) {
            c0.q0(view);
        } else {
            view.addOnAttachStateChangeListener(new d());
        }
    }

    public static void k(View view) {
        view.requestFocus();
        view.post(new a(view));
    }
}
