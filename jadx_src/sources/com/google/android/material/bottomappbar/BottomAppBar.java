package com.google.android.material.bottomappbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.c0;
import androidx.core.view.m0;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.s;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import k7.k;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class BottomAppBar extends Toolbar implements CoordinatorLayout.b {
    private static final int G0 = k.f21247s;
    private Behavior A0;
    private int B0;
    private int C0;
    private int D0;
    AnimatorListenerAdapter E0;
    l7.k<FloatingActionButton> F0;

    /* renamed from: l0  reason: collision with root package name */
    private final int f17479l0;

    /* renamed from: m0  reason: collision with root package name */
    private final x7.h f17480m0;

    /* renamed from: n0  reason: collision with root package name */
    private Animator f17481n0;

    /* renamed from: o0  reason: collision with root package name */
    private Animator f17482o0;

    /* renamed from: p0  reason: collision with root package name */
    private int f17483p0;

    /* renamed from: q0  reason: collision with root package name */
    private int f17484q0;

    /* renamed from: r0  reason: collision with root package name */
    private boolean f17485r0;

    /* renamed from: s0  reason: collision with root package name */
    private final boolean f17486s0;

    /* renamed from: t0  reason: collision with root package name */
    private final boolean f17487t0;

    /* renamed from: u0  reason: collision with root package name */
    private final boolean f17488u0;

    /* renamed from: v0  reason: collision with root package name */
    private int f17489v0;

    /* renamed from: w0  reason: collision with root package name */
    private ArrayList<j> f17490w0;

    /* renamed from: x0  reason: collision with root package name */
    private int f17491x0;

    /* renamed from: y0  reason: collision with root package name */
    private boolean f17492y0;

    /* renamed from: z0  reason: collision with root package name */
    private boolean f17493z0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class Behavior extends HideBottomViewOnScrollBehavior<BottomAppBar> {

        /* renamed from: e  reason: collision with root package name */
        private final Rect f17494e;

        /* renamed from: f  reason: collision with root package name */
        private WeakReference<BottomAppBar> f17495f;

        /* renamed from: g  reason: collision with root package name */
        private int f17496g;

        /* renamed from: h  reason: collision with root package name */
        private final View.OnLayoutChangeListener f17497h;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements View.OnLayoutChangeListener {
            a() {
            }

            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15) {
                BottomAppBar bottomAppBar = (BottomAppBar) Behavior.this.f17495f.get();
                if (bottomAppBar == null || !(view instanceof FloatingActionButton)) {
                    view.removeOnLayoutChangeListener(this);
                    return;
                }
                FloatingActionButton floatingActionButton = (FloatingActionButton) view;
                floatingActionButton.j(Behavior.this.f17494e);
                int height = Behavior.this.f17494e.height();
                bottomAppBar.R0(height);
                bottomAppBar.setFabCornerSize(floatingActionButton.getShapeAppearanceModel().r().a(new RectF(Behavior.this.f17494e)));
                CoordinatorLayout.e eVar = (CoordinatorLayout.e) view.getLayoutParams();
                if (Behavior.this.f17496g == 0) {
                    ((ViewGroup.MarginLayoutParams) eVar).bottomMargin = bottomAppBar.getBottomInset() + (bottomAppBar.getResources().getDimensionPixelOffset(k7.d.O) - ((floatingActionButton.getMeasuredHeight() - height) / 2));
                    ((ViewGroup.MarginLayoutParams) eVar).leftMargin = bottomAppBar.getLeftInset();
                    ((ViewGroup.MarginLayoutParams) eVar).rightMargin = bottomAppBar.getRightInset();
                    if (s.h(floatingActionButton)) {
                        ((ViewGroup.MarginLayoutParams) eVar).leftMargin += bottomAppBar.f17479l0;
                    } else {
                        ((ViewGroup.MarginLayoutParams) eVar).rightMargin += bottomAppBar.f17479l0;
                    }
                }
            }
        }

        public Behavior() {
            this.f17497h = new a();
            this.f17494e = new Rect();
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f17497h = new a();
            this.f17494e = new Rect();
        }

        @Override // com.google.android.material.behavior.HideBottomViewOnScrollBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: M */
        public boolean l(CoordinatorLayout coordinatorLayout, BottomAppBar bottomAppBar, int i8) {
            this.f17495f = new WeakReference<>(bottomAppBar);
            View H0 = bottomAppBar.H0();
            if (H0 != null && !c0.W(H0)) {
                CoordinatorLayout.e eVar = (CoordinatorLayout.e) H0.getLayoutParams();
                eVar.f4387d = 49;
                this.f17496g = ((ViewGroup.MarginLayoutParams) eVar).bottomMargin;
                if (H0 instanceof FloatingActionButton) {
                    FloatingActionButton floatingActionButton = (FloatingActionButton) H0;
                    floatingActionButton.addOnLayoutChangeListener(this.f17497h);
                    bottomAppBar.z0(floatingActionButton);
                }
                bottomAppBar.P0();
            }
            coordinatorLayout.M(bottomAppBar, i8);
            return super.l(coordinatorLayout, bottomAppBar, i8);
        }

        @Override // com.google.android.material.behavior.HideBottomViewOnScrollBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: N */
        public boolean A(CoordinatorLayout coordinatorLayout, BottomAppBar bottomAppBar, View view, View view2, int i8, int i9) {
            return bottomAppBar.getHideOnScroll() && super.A(coordinatorLayout, bottomAppBar, view, view2, i8, i9);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: c  reason: collision with root package name */
        int f17499c;

        /* renamed from: d  reason: collision with root package name */
        boolean f17500d;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f17499c = parcel.readInt();
            this.f17500d = parcel.readInt() != 0;
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeInt(this.f17499c);
            parcel.writeInt(this.f17500d ? 1 : 0);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (BottomAppBar.this.f17492y0) {
                return;
            }
            BottomAppBar bottomAppBar = BottomAppBar.this;
            bottomAppBar.L0(bottomAppBar.f17483p0, BottomAppBar.this.f17493z0);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements l7.k<FloatingActionButton> {
        b() {
        }

        @Override // l7.k
        /* renamed from: c */
        public void a(FloatingActionButton floatingActionButton) {
            BottomAppBar.this.f17480m0.b0(floatingActionButton.getVisibility() == 0 ? floatingActionButton.getScaleY() : 0.0f);
        }

        @Override // l7.k
        /* renamed from: d */
        public void b(FloatingActionButton floatingActionButton) {
            float translationX = floatingActionButton.getTranslationX();
            if (BottomAppBar.this.getTopEdgeTreatment().k() != translationX) {
                BottomAppBar.this.getTopEdgeTreatment().s(translationX);
                BottomAppBar.this.f17480m0.invalidateSelf();
            }
            float max = Math.max(0.0f, -floatingActionButton.getTranslationY());
            if (BottomAppBar.this.getTopEdgeTreatment().d() != max) {
                BottomAppBar.this.getTopEdgeTreatment().l(max);
                BottomAppBar.this.f17480m0.invalidateSelf();
            }
            BottomAppBar.this.f17480m0.b0(floatingActionButton.getVisibility() == 0 ? floatingActionButton.getScaleY() : 0.0f);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements s.e {
        c() {
        }

        @Override // com.google.android.material.internal.s.e
        public m0 a(View view, m0 m0Var, s.f fVar) {
            boolean z4;
            if (BottomAppBar.this.f17486s0) {
                BottomAppBar.this.B0 = m0Var.j();
            }
            boolean z8 = false;
            if (BottomAppBar.this.f17487t0) {
                z4 = BottomAppBar.this.D0 != m0Var.k();
                BottomAppBar.this.D0 = m0Var.k();
            } else {
                z4 = false;
            }
            if (BottomAppBar.this.f17488u0) {
                boolean z9 = BottomAppBar.this.C0 != m0Var.l();
                BottomAppBar.this.C0 = m0Var.l();
                z8 = z9;
            }
            if (z4 || z8) {
                BottomAppBar.this.A0();
                BottomAppBar.this.P0();
                BottomAppBar.this.O0();
            }
            return m0Var;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends AnimatorListenerAdapter {
        d() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BottomAppBar.this.E0();
            BottomAppBar.this.f17481n0 = null;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            BottomAppBar.this.F0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e extends FloatingActionButton.b {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f17505a;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a extends FloatingActionButton.b {
            a() {
            }

            @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.b
            public void b(FloatingActionButton floatingActionButton) {
                BottomAppBar.this.E0();
            }
        }

        e(int i8) {
            this.f17505a = i8;
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.b
        public void a(FloatingActionButton floatingActionButton) {
            floatingActionButton.setTranslationX(BottomAppBar.this.J0(this.f17505a));
            floatingActionButton.s(new a());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f extends AnimatorListenerAdapter {
        f() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BottomAppBar.this.E0();
            BottomAppBar.this.f17492y0 = false;
            BottomAppBar.this.f17482o0 = null;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            BottomAppBar.this.F0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class g extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        public boolean f17509a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ ActionMenuView f17510b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ int f17511c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ boolean f17512d;

        g(ActionMenuView actionMenuView, int i8, boolean z4) {
            this.f17510b = actionMenuView;
            this.f17511c = i8;
            this.f17512d = z4;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f17509a = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.f17509a) {
                return;
            }
            boolean z4 = BottomAppBar.this.f17491x0 != 0;
            BottomAppBar bottomAppBar = BottomAppBar.this;
            bottomAppBar.N0(bottomAppBar.f17491x0);
            BottomAppBar.this.T0(this.f17510b, this.f17511c, this.f17512d, z4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ActionMenuView f17514a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ int f17515b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ boolean f17516c;

        h(ActionMenuView actionMenuView, int i8, boolean z4) {
            this.f17514a = actionMenuView;
            this.f17515b = i8;
            this.f17516c = z4;
        }

        @Override // java.lang.Runnable
        public void run() {
            ActionMenuView actionMenuView = this.f17514a;
            actionMenuView.setTranslationX(BottomAppBar.this.I0(actionMenuView, this.f17515b, this.f17516c));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class i extends AnimatorListenerAdapter {
        i() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            BottomAppBar.this.E0.onAnimationStart(animator);
            FloatingActionButton G0 = BottomAppBar.this.G0();
            if (G0 != null) {
                G0.setTranslationX(BottomAppBar.this.getFabTranslationX());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface j {
        void a(BottomAppBar bottomAppBar);

        void b(BottomAppBar bottomAppBar);
    }

    public BottomAppBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.f21052d);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public BottomAppBar(android.content.Context r11, android.util.AttributeSet r12, int r13) {
        /*
            r10 = this;
            int r6 = com.google.android.material.bottomappbar.BottomAppBar.G0
            android.content.Context r11 = y7.a.c(r11, r12, r13, r6)
            r10.<init>(r11, r12, r13)
            x7.h r11 = new x7.h
            r11.<init>()
            r10.f17480m0 = r11
            r7 = 0
            r10.f17489v0 = r7
            r10.f17491x0 = r7
            r10.f17492y0 = r7
            r0 = 1
            r10.f17493z0 = r0
            com.google.android.material.bottomappbar.BottomAppBar$a r0 = new com.google.android.material.bottomappbar.BottomAppBar$a
            r0.<init>()
            r10.E0 = r0
            com.google.android.material.bottomappbar.BottomAppBar$b r0 = new com.google.android.material.bottomappbar.BottomAppBar$b
            r0.<init>()
            r10.F0 = r0
            android.content.Context r8 = r10.getContext()
            int[] r2 = k7.l.T
            int[] r5 = new int[r7]
            r0 = r8
            r1 = r12
            r3 = r13
            r4 = r6
            android.content.res.TypedArray r0 = com.google.android.material.internal.m.h(r0, r1, r2, r3, r4, r5)
            int r1 = k7.l.U
            android.content.res.ColorStateList r1 = u7.c.a(r8, r0, r1)
            int r2 = k7.l.V
            int r2 = r0.getDimensionPixelSize(r2, r7)
            int r3 = k7.l.Y
            int r3 = r0.getDimensionPixelOffset(r3, r7)
            float r3 = (float) r3
            int r4 = k7.l.Z
            int r4 = r0.getDimensionPixelOffset(r4, r7)
            float r4 = (float) r4
            int r5 = k7.l.f21256a0
            int r5 = r0.getDimensionPixelOffset(r5, r7)
            float r5 = (float) r5
            int r9 = k7.l.W
            int r9 = r0.getInt(r9, r7)
            r10.f17483p0 = r9
            int r9 = k7.l.X
            int r9 = r0.getInt(r9, r7)
            r10.f17484q0 = r9
            int r9 = k7.l.f21266b0
            boolean r9 = r0.getBoolean(r9, r7)
            r10.f17485r0 = r9
            int r9 = k7.l.f21276c0
            boolean r9 = r0.getBoolean(r9, r7)
            r10.f17486s0 = r9
            int r9 = k7.l.f21286d0
            boolean r9 = r0.getBoolean(r9, r7)
            r10.f17487t0 = r9
            int r9 = k7.l.f21295e0
            boolean r7 = r0.getBoolean(r9, r7)
            r10.f17488u0 = r7
            r0.recycle()
            android.content.res.Resources r0 = r10.getResources()
            int r7 = k7.d.N
            int r0 = r0.getDimensionPixelOffset(r7)
            r10.f17479l0 = r0
            com.google.android.material.bottomappbar.a r0 = new com.google.android.material.bottomappbar.a
            r0.<init>(r3, r4, r5)
            x7.m$b r3 = x7.m.a()
            x7.m$b r0 = r3.B(r0)
            x7.m r0 = r0.m()
            r11.setShapeAppearanceModel(r0)
            r0 = 2
            r11.i0(r0)
            android.graphics.Paint$Style r0 = android.graphics.Paint.Style.FILL
            r11.d0(r0)
            r11.P(r8)
            float r0 = (float) r2
            r10.setElevation(r0)
            androidx.core.graphics.drawable.a.o(r11, r1)
            androidx.core.view.c0.x0(r10, r11)
            com.google.android.material.bottomappbar.BottomAppBar$c r11 = new com.google.android.material.bottomappbar.BottomAppBar$c
            r11.<init>()
            com.google.android.material.internal.s.a(r10, r12, r13, r6, r11)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.bottomappbar.BottomAppBar.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void A0() {
        Animator animator = this.f17482o0;
        if (animator != null) {
            animator.cancel();
        }
        Animator animator2 = this.f17481n0;
        if (animator2 != null) {
            animator2.cancel();
        }
    }

    private void C0(int i8, List<Animator> list) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(G0(), "translationX", J0(i8));
        ofFloat.setDuration(300L);
        list.add(ofFloat);
    }

    private void D0(int i8, boolean z4, List<Animator> list) {
        ActionMenuView actionMenuView = getActionMenuView();
        if (actionMenuView == null) {
            return;
        }
        Animator ofFloat = ObjectAnimator.ofFloat(actionMenuView, "alpha", 1.0f);
        if (Math.abs(actionMenuView.getTranslationX() - I0(actionMenuView, i8, z4)) <= 1.0f) {
            if (actionMenuView.getAlpha() < 1.0f) {
                list.add(ofFloat);
                return;
            }
            return;
        }
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(actionMenuView, "alpha", 0.0f);
        ofFloat2.addListener(new g(actionMenuView, i8, z4));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(150L);
        animatorSet.playSequentially(ofFloat2, ofFloat);
        list.add(animatorSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void E0() {
        ArrayList<j> arrayList;
        int i8 = this.f17489v0 - 1;
        this.f17489v0 = i8;
        if (i8 != 0 || (arrayList = this.f17490w0) == null) {
            return;
        }
        Iterator<j> it = arrayList.iterator();
        while (it.hasNext()) {
            it.next().b(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F0() {
        ArrayList<j> arrayList;
        int i8 = this.f17489v0;
        this.f17489v0 = i8 + 1;
        if (i8 != 0 || (arrayList = this.f17490w0) == null) {
            return;
        }
        Iterator<j> it = arrayList.iterator();
        while (it.hasNext()) {
            it.next().a(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FloatingActionButton G0() {
        View H0 = H0();
        if (H0 instanceof FloatingActionButton) {
            return (FloatingActionButton) H0;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:8:0x001e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.view.View H0() {
        /*
            r4 = this;
            android.view.ViewParent r0 = r4.getParent()
            boolean r0 = r0 instanceof androidx.coordinatorlayout.widget.CoordinatorLayout
            r1 = 0
            if (r0 != 0) goto La
            return r1
        La:
            android.view.ViewParent r0 = r4.getParent()
            androidx.coordinatorlayout.widget.CoordinatorLayout r0 = (androidx.coordinatorlayout.widget.CoordinatorLayout) r0
            java.util.List r0 = r0.w(r4)
            java.util.Iterator r0 = r0.iterator()
        L18:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L2d
            java.lang.Object r2 = r0.next()
            android.view.View r2 = (android.view.View) r2
            boolean r3 = r2 instanceof com.google.android.material.floatingactionbutton.FloatingActionButton
            if (r3 != 0) goto L2c
            boolean r3 = r2 instanceof com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
            if (r3 == 0) goto L18
        L2c:
            return r2
        L2d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.bottomappbar.BottomAppBar.H0():android.view.View");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float J0(int i8) {
        boolean h8 = s.h(this);
        if (i8 == 1) {
            return ((getMeasuredWidth() / 2) - (this.f17479l0 + (h8 ? this.D0 : this.C0))) * (h8 ? -1 : 1);
        }
        return 0.0f;
    }

    private boolean K0() {
        FloatingActionButton G02 = G0();
        return G02 != null && G02.o();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void L0(int i8, boolean z4) {
        if (!c0.W(this)) {
            this.f17492y0 = false;
            N0(this.f17491x0);
            return;
        }
        Animator animator = this.f17482o0;
        if (animator != null) {
            animator.cancel();
        }
        ArrayList arrayList = new ArrayList();
        if (!K0()) {
            i8 = 0;
            z4 = false;
        }
        D0(i8, z4, arrayList);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        this.f17482o0 = animatorSet;
        animatorSet.addListener(new f());
        this.f17482o0.start();
    }

    private void M0(int i8) {
        if (this.f17483p0 == i8 || !c0.W(this)) {
            return;
        }
        Animator animator = this.f17481n0;
        if (animator != null) {
            animator.cancel();
        }
        ArrayList arrayList = new ArrayList();
        if (this.f17484q0 == 1) {
            C0(i8, arrayList);
        } else {
            B0(i8, arrayList);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        this.f17481n0 = animatorSet;
        animatorSet.addListener(new d());
        this.f17481n0.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void O0() {
        ActionMenuView actionMenuView = getActionMenuView();
        if (actionMenuView == null || this.f17482o0 != null) {
            return;
        }
        actionMenuView.setAlpha(1.0f);
        if (K0()) {
            S0(actionMenuView, this.f17483p0, this.f17493z0);
        } else {
            S0(actionMenuView, 0, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void P0() {
        getTopEdgeTreatment().s(getFabTranslationX());
        View H0 = H0();
        this.f17480m0.b0((this.f17493z0 && K0()) ? 1.0f : 0.0f);
        if (H0 != null) {
            H0.setTranslationY(getFabTranslationY());
            H0.setTranslationX(getFabTranslationX());
        }
    }

    private void S0(ActionMenuView actionMenuView, int i8, boolean z4) {
        T0(actionMenuView, i8, z4, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void T0(ActionMenuView actionMenuView, int i8, boolean z4, boolean z8) {
        h hVar = new h(actionMenuView, i8, z4);
        if (z8) {
            actionMenuView.post(hVar);
        } else {
            hVar.run();
        }
    }

    private ActionMenuView getActionMenuView() {
        for (int i8 = 0; i8 < getChildCount(); i8++) {
            View childAt = getChildAt(i8);
            if (childAt instanceof ActionMenuView) {
                return (ActionMenuView) childAt;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getBottomInset() {
        return this.B0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getFabTranslationX() {
        return J0(this.f17483p0);
    }

    private float getFabTranslationY() {
        return -getTopEdgeTreatment().d();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getLeftInset() {
        return this.D0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRightInset() {
        return this.C0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.google.android.material.bottomappbar.a getTopEdgeTreatment() {
        return (com.google.android.material.bottomappbar.a) this.f17480m0.D().p();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void z0(FloatingActionButton floatingActionButton) {
        floatingActionButton.e(this.E0);
        floatingActionButton.f(new i());
        floatingActionButton.g(this.F0);
    }

    protected void B0(int i8, List<Animator> list) {
        FloatingActionButton G02 = G0();
        if (G02 == null || G02.n()) {
            return;
        }
        F0();
        G02.l(new e(i8));
    }

    protected int I0(ActionMenuView actionMenuView, int i8, boolean z4) {
        if (i8 == 1 && z4) {
            boolean h8 = s.h(this);
            int measuredWidth = h8 ? getMeasuredWidth() : 0;
            for (int i9 = 0; i9 < getChildCount(); i9++) {
                View childAt = getChildAt(i9);
                if ((childAt.getLayoutParams() instanceof Toolbar.LayoutParams) && (((Toolbar.LayoutParams) childAt.getLayoutParams()).f469a & 8388615) == 8388611) {
                    measuredWidth = h8 ? Math.min(measuredWidth, childAt.getLeft()) : Math.max(measuredWidth, childAt.getRight());
                }
            }
            return measuredWidth - ((h8 ? actionMenuView.getRight() : actionMenuView.getLeft()) + (h8 ? this.C0 : -this.D0));
        }
        return 0;
    }

    public void N0(int i8) {
        if (i8 != 0) {
            this.f17491x0 = 0;
            getMenu().clear();
            x(i8);
        }
    }

    public void Q0(int i8, int i9) {
        this.f17491x0 = i9;
        this.f17492y0 = true;
        L0(i8, this.f17493z0);
        M0(i8);
        this.f17483p0 = i8;
    }

    boolean R0(int i8) {
        float f5 = i8;
        if (f5 != getTopEdgeTreatment().j()) {
            getTopEdgeTreatment().r(f5);
            this.f17480m0.invalidateSelf();
            return true;
        }
        return false;
    }

    public ColorStateList getBackgroundTint() {
        return this.f17480m0.H();
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.b
    public Behavior getBehavior() {
        if (this.A0 == null) {
            this.A0 = new Behavior();
        }
        return this.A0;
    }

    public float getCradleVerticalOffset() {
        return getTopEdgeTreatment().d();
    }

    public int getFabAlignmentMode() {
        return this.f17483p0;
    }

    public int getFabAnimationMode() {
        return this.f17484q0;
    }

    public float getFabCradleMargin() {
        return getTopEdgeTreatment().f();
    }

    public float getFabCradleRoundedCornerRadius() {
        return getTopEdgeTreatment().g();
    }

    public boolean getHideOnScroll() {
        return this.f17485r0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        x7.i.f(this, this.f17480m0);
        if (getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).setClipChildren(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        if (z4) {
            A0();
            P0();
        }
        O0();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.a());
        this.f17483p0 = savedState.f17499c;
        this.f17493z0 = savedState.f17500d;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f17499c = this.f17483p0;
        savedState.f17500d = this.f17493z0;
        return savedState;
    }

    public void setBackgroundTint(ColorStateList colorStateList) {
        androidx.core.graphics.drawable.a.o(this.f17480m0, colorStateList);
    }

    public void setCradleVerticalOffset(float f5) {
        if (f5 != getCradleVerticalOffset()) {
            getTopEdgeTreatment().l(f5);
            this.f17480m0.invalidateSelf();
            P0();
        }
    }

    @Override // android.view.View
    public void setElevation(float f5) {
        this.f17480m0.Z(f5);
        getBehavior().G(this, this.f17480m0.C() - this.f17480m0.B());
    }

    public void setFabAlignmentMode(int i8) {
        Q0(i8, 0);
    }

    public void setFabAnimationMode(int i8) {
        this.f17484q0 = i8;
    }

    void setFabCornerSize(float f5) {
        if (f5 != getTopEdgeTreatment().e()) {
            getTopEdgeTreatment().m(f5);
            this.f17480m0.invalidateSelf();
        }
    }

    public void setFabCradleMargin(float f5) {
        if (f5 != getFabCradleMargin()) {
            getTopEdgeTreatment().o(f5);
            this.f17480m0.invalidateSelf();
        }
    }

    public void setFabCradleRoundedCornerRadius(float f5) {
        if (f5 != getFabCradleRoundedCornerRadius()) {
            getTopEdgeTreatment().q(f5);
            this.f17480m0.invalidateSelf();
        }
    }

    public void setHideOnScroll(boolean z4) {
        this.f17485r0 = z4;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setSubtitle(CharSequence charSequence) {
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitle(CharSequence charSequence) {
    }
}
