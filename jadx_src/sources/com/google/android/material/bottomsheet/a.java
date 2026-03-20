package com.google.android.material.bottomsheet;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import androidx.appcompat.app.i;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.c0;
import androidx.core.view.m0;
import androidx.core.view.v;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import k7.k;
import x7.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends i {

    /* renamed from: f  reason: collision with root package name */
    private BottomSheetBehavior<FrameLayout> f17571f;

    /* renamed from: g  reason: collision with root package name */
    private FrameLayout f17572g;

    /* renamed from: h  reason: collision with root package name */
    private CoordinatorLayout f17573h;

    /* renamed from: j  reason: collision with root package name */
    private FrameLayout f17574j;

    /* renamed from: k  reason: collision with root package name */
    boolean f17575k;

    /* renamed from: l  reason: collision with root package name */
    boolean f17576l;

    /* renamed from: m  reason: collision with root package name */
    private boolean f17577m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f17578n;

    /* renamed from: p  reason: collision with root package name */
    private BottomSheetBehavior.g f17579p;
    private boolean q;

    /* renamed from: t  reason: collision with root package name */
    private BottomSheetBehavior.g f17580t;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.android.material.bottomsheet.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0127a implements v {
        C0127a() {
        }

        @Override // androidx.core.view.v
        public m0 a(View view, m0 m0Var) {
            if (a.this.f17579p != null) {
                a.this.f17571f.j0(a.this.f17579p);
            }
            if (m0Var != null) {
                a aVar = a.this;
                aVar.f17579p = new f(aVar.f17574j, m0Var, null);
                a.this.f17571f.S(a.this.f17579p);
            }
            return m0Var;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements View.OnClickListener {
        b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            a aVar = a.this;
            if (aVar.f17576l && aVar.isShowing() && a.this.q()) {
                a.this.cancel();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends androidx.core.view.a {
        c() {
        }

        @Override // androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            boolean z4;
            super.g(view, cVar);
            if (a.this.f17576l) {
                cVar.a(1048576);
                z4 = true;
            } else {
                z4 = false;
            }
            cVar.h0(z4);
        }

        @Override // androidx.core.view.a
        public boolean j(View view, int i8, Bundle bundle) {
            if (i8 == 1048576) {
                a aVar = a.this;
                if (aVar.f17576l) {
                    aVar.cancel();
                    return true;
                }
            }
            return super.j(view, i8, bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements View.OnTouchListener {
        d() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class e extends BottomSheetBehavior.g {
        e() {
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.g
        public void a(View view, float f5) {
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.g
        public void b(View view, int i8) {
            if (i8 == 5) {
                a.this.cancel();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class f extends BottomSheetBehavior.g {

        /* renamed from: a  reason: collision with root package name */
        private final boolean f17586a;

        /* renamed from: b  reason: collision with root package name */
        private final boolean f17587b;

        /* renamed from: c  reason: collision with root package name */
        private final m0 f17588c;

        private f(View view, m0 m0Var) {
            int color;
            this.f17588c = m0Var;
            boolean z4 = Build.VERSION.SDK_INT >= 23 && (view.getSystemUiVisibility() & 8192) != 0;
            this.f17587b = z4;
            h e02 = BottomSheetBehavior.c0(view).e0();
            ColorStateList x8 = e02 != null ? e02.x() : c0.u(view);
            if (x8 != null) {
                color = x8.getDefaultColor();
            } else if (!(view.getBackground() instanceof ColorDrawable)) {
                this.f17586a = z4;
                return;
            } else {
                color = ((ColorDrawable) view.getBackground()).getColor();
            }
            this.f17586a = n7.a.f(color);
        }

        /* synthetic */ f(View view, m0 m0Var, C0127a c0127a) {
            this(view, m0Var);
        }

        private void c(View view) {
            int paddingLeft;
            int i8;
            if (view.getTop() < this.f17588c.m()) {
                a.p(view, this.f17586a);
                paddingLeft = view.getPaddingLeft();
                i8 = this.f17588c.m() - view.getTop();
            } else if (view.getTop() == 0) {
                return;
            } else {
                a.p(view, this.f17587b);
                paddingLeft = view.getPaddingLeft();
                i8 = 0;
            }
            view.setPadding(paddingLeft, i8, view.getPaddingRight(), view.getPaddingBottom());
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.g
        public void a(View view, float f5) {
            c(view);
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.g
        public void b(View view, int i8) {
            c(view);
        }
    }

    public a(Context context, int i8) {
        super(context, e(context, i8));
        this.f17576l = true;
        this.f17577m = true;
        this.f17580t = new e();
        g(1);
        this.q = getContext().getTheme().obtainStyledAttributes(new int[]{k7.b.f21070w}).getBoolean(0, false);
    }

    private static int e(Context context, int i8) {
        if (i8 == 0) {
            TypedValue typedValue = new TypedValue();
            return context.getTheme().resolveAttribute(k7.b.f21054f, typedValue, true) ? typedValue.resourceId : k.f21235f;
        }
        return i8;
    }

    private FrameLayout l() {
        if (this.f17572g == null) {
            FrameLayout frameLayout = (FrameLayout) View.inflate(getContext(), k7.h.f21180b, null);
            this.f17572g = frameLayout;
            this.f17573h = (CoordinatorLayout) frameLayout.findViewById(k7.f.f21155d);
            FrameLayout frameLayout2 = (FrameLayout) this.f17572g.findViewById(k7.f.f21156e);
            this.f17574j = frameLayout2;
            BottomSheetBehavior<FrameLayout> c02 = BottomSheetBehavior.c0(frameLayout2);
            this.f17571f = c02;
            c02.S(this.f17580t);
            this.f17571f.s0(this.f17576l);
        }
        return this.f17572g;
    }

    public static void p(View view, boolean z4) {
        if (Build.VERSION.SDK_INT >= 23) {
            int systemUiVisibility = view.getSystemUiVisibility();
            view.setSystemUiVisibility(z4 ? systemUiVisibility | 8192 : systemUiVisibility & (-8193));
        }
    }

    private View r(int i8, View view, ViewGroup.LayoutParams layoutParams) {
        l();
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) this.f17572g.findViewById(k7.f.f21155d);
        if (i8 != 0 && view == null) {
            view = getLayoutInflater().inflate(i8, (ViewGroup) coordinatorLayout, false);
        }
        if (this.q) {
            c0.I0(this.f17574j, new C0127a());
        }
        this.f17574j.removeAllViews();
        FrameLayout frameLayout = this.f17574j;
        if (layoutParams == null) {
            frameLayout.addView(view);
        } else {
            frameLayout.addView(view, layoutParams);
        }
        coordinatorLayout.findViewById(k7.f.f21152b0).setOnClickListener(new b());
        c0.t0(this.f17574j, new c());
        this.f17574j.setOnTouchListener(new d());
        return this.f17572g;
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void cancel() {
        BottomSheetBehavior<FrameLayout> m8 = m();
        if (!this.f17575k || m8.f0() == 5) {
            super.cancel();
        } else {
            m8.y0(5);
        }
    }

    public BottomSheetBehavior<FrameLayout> m() {
        if (this.f17571f == null) {
            l();
        }
        return this.f17571f;
    }

    public boolean n() {
        return this.f17575k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o() {
        this.f17571f.j0(this.f17580t);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        if (window == null || Build.VERSION.SDK_INT < 21) {
            return;
        }
        boolean z4 = this.q && Color.alpha(window.getNavigationBarColor()) < 255;
        FrameLayout frameLayout = this.f17572g;
        if (frameLayout != null) {
            frameLayout.setFitsSystemWindows(!z4);
        }
        CoordinatorLayout coordinatorLayout = this.f17573h;
        if (coordinatorLayout != null) {
            coordinatorLayout.setFitsSystemWindows(!z4);
        }
        if (z4) {
            window.getDecorView().setSystemUiVisibility(768);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.i, androidx.activity.h, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        if (window != null) {
            int i8 = Build.VERSION.SDK_INT;
            if (i8 >= 21) {
                window.setStatusBarColor(0);
                window.addFlags(Integer.MIN_VALUE);
                if (i8 < 23) {
                    window.addFlags(67108864);
                }
            }
            window.setLayout(-1, -1);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.h, android.app.Dialog
    public void onStart() {
        super.onStart();
        BottomSheetBehavior<FrameLayout> bottomSheetBehavior = this.f17571f;
        if (bottomSheetBehavior == null || bottomSheetBehavior.f0() != 5) {
            return;
        }
        this.f17571f.y0(4);
    }

    boolean q() {
        if (!this.f17578n) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{16843611});
            this.f17577m = obtainStyledAttributes.getBoolean(0, true);
            obtainStyledAttributes.recycle();
            this.f17578n = true;
        }
        return this.f17577m;
    }

    @Override // android.app.Dialog
    public void setCancelable(boolean z4) {
        super.setCancelable(z4);
        if (this.f17576l != z4) {
            this.f17576l = z4;
            BottomSheetBehavior<FrameLayout> bottomSheetBehavior = this.f17571f;
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.s0(z4);
            }
        }
    }

    @Override // android.app.Dialog
    public void setCanceledOnTouchOutside(boolean z4) {
        super.setCanceledOnTouchOutside(z4);
        if (z4 && !this.f17576l) {
            this.f17576l = true;
        }
        this.f17577m = z4;
        this.f17578n = true;
    }

    @Override // androidx.appcompat.app.i, android.app.Dialog
    public void setContentView(int i8) {
        super.setContentView(r(i8, null, null));
    }

    @Override // androidx.appcompat.app.i, android.app.Dialog
    public void setContentView(View view) {
        super.setContentView(r(0, view, null));
    }

    @Override // androidx.appcompat.app.i, android.app.Dialog
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        super.setContentView(r(0, view, layoutParams));
    }
}
