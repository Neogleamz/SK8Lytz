package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import androidx.core.view.c0;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l extends RecyclerView.n implements RecyclerView.p {
    private g A;
    private Rect C;
    private long D;

    /* renamed from: d  reason: collision with root package name */
    float f6953d;

    /* renamed from: e  reason: collision with root package name */
    float f6954e;

    /* renamed from: f  reason: collision with root package name */
    private float f6955f;

    /* renamed from: g  reason: collision with root package name */
    private float f6956g;

    /* renamed from: h  reason: collision with root package name */
    float f6957h;

    /* renamed from: i  reason: collision with root package name */
    float f6958i;

    /* renamed from: j  reason: collision with root package name */
    private float f6959j;

    /* renamed from: k  reason: collision with root package name */
    private float f6960k;

    /* renamed from: m  reason: collision with root package name */
    f f6962m;

    /* renamed from: o  reason: collision with root package name */
    int f6964o;
    private int q;

    /* renamed from: r  reason: collision with root package name */
    RecyclerView f6966r;

    /* renamed from: t  reason: collision with root package name */
    VelocityTracker f6968t;

    /* renamed from: u  reason: collision with root package name */
    private List<RecyclerView.b0> f6969u;

    /* renamed from: v  reason: collision with root package name */
    private List<Integer> f6970v;

    /* renamed from: z  reason: collision with root package name */
    androidx.core.view.e f6974z;

    /* renamed from: a  reason: collision with root package name */
    final List<View> f6950a = new ArrayList();

    /* renamed from: b  reason: collision with root package name */
    private final float[] f6951b = new float[2];

    /* renamed from: c  reason: collision with root package name */
    RecyclerView.b0 f6952c = null;

    /* renamed from: l  reason: collision with root package name */
    int f6961l = -1;

    /* renamed from: n  reason: collision with root package name */
    private int f6963n = 0;

    /* renamed from: p  reason: collision with root package name */
    List<h> f6965p = new ArrayList();

    /* renamed from: s  reason: collision with root package name */
    final Runnable f6967s = new a();

    /* renamed from: w  reason: collision with root package name */
    private RecyclerView.j f6971w = null;

    /* renamed from: x  reason: collision with root package name */
    View f6972x = null;

    /* renamed from: y  reason: collision with root package name */
    int f6973y = -1;
    private final RecyclerView.r B = new b();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            l lVar = l.this;
            if (lVar.f6952c == null || !lVar.E()) {
                return;
            }
            l lVar2 = l.this;
            RecyclerView.b0 b0Var = lVar2.f6952c;
            if (b0Var != null) {
                lVar2.z(b0Var);
            }
            l lVar3 = l.this;
            lVar3.f6966r.removeCallbacks(lVar3.f6967s);
            c0.l0(l.this.f6966r, this);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements RecyclerView.r {
        b() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.r
        public void a(RecyclerView recyclerView, MotionEvent motionEvent) {
            l.this.f6974z.a(motionEvent);
            VelocityTracker velocityTracker = l.this.f6968t;
            if (velocityTracker != null) {
                velocityTracker.addMovement(motionEvent);
            }
            if (l.this.f6961l == -1) {
                return;
            }
            int actionMasked = motionEvent.getActionMasked();
            int findPointerIndex = motionEvent.findPointerIndex(l.this.f6961l);
            if (findPointerIndex >= 0) {
                l.this.o(actionMasked, motionEvent, findPointerIndex);
            }
            l lVar = l.this;
            RecyclerView.b0 b0Var = lVar.f6952c;
            if (b0Var == null) {
                return;
            }
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    if (findPointerIndex >= 0) {
                        lVar.L(motionEvent, lVar.f6964o, findPointerIndex);
                        l.this.z(b0Var);
                        l lVar2 = l.this;
                        lVar2.f6966r.removeCallbacks(lVar2.f6967s);
                        l.this.f6967s.run();
                        l.this.f6966r.invalidate();
                        return;
                    }
                    return;
                } else if (actionMasked != 3) {
                    if (actionMasked != 6) {
                        return;
                    }
                    int actionIndex = motionEvent.getActionIndex();
                    int pointerId = motionEvent.getPointerId(actionIndex);
                    l lVar3 = l.this;
                    if (pointerId == lVar3.f6961l) {
                        lVar3.f6961l = motionEvent.getPointerId(actionIndex == 0 ? 1 : 0);
                        l lVar4 = l.this;
                        lVar4.L(motionEvent, lVar4.f6964o, actionIndex);
                        return;
                    }
                    return;
                } else {
                    VelocityTracker velocityTracker2 = lVar.f6968t;
                    if (velocityTracker2 != null) {
                        velocityTracker2.clear();
                    }
                }
            }
            l.this.F(null, 0);
            l.this.f6961l = -1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.r
        public boolean c(RecyclerView recyclerView, MotionEvent motionEvent) {
            int findPointerIndex;
            h s8;
            l.this.f6974z.a(motionEvent);
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                l.this.f6961l = motionEvent.getPointerId(0);
                l.this.f6953d = motionEvent.getX();
                l.this.f6954e = motionEvent.getY();
                l.this.A();
                l lVar = l.this;
                if (lVar.f6952c == null && (s8 = lVar.s(motionEvent)) != null) {
                    l lVar2 = l.this;
                    lVar2.f6953d -= s8.f6997j;
                    lVar2.f6954e -= s8.f6998k;
                    lVar2.r(s8.f6992e, true);
                    if (l.this.f6950a.remove(s8.f6992e.f6628a)) {
                        l lVar3 = l.this;
                        lVar3.f6962m.c(lVar3.f6966r, s8.f6992e);
                    }
                    l.this.F(s8.f6992e, s8.f6993f);
                    l lVar4 = l.this;
                    lVar4.L(motionEvent, lVar4.f6964o, 0);
                }
            } else if (actionMasked == 3 || actionMasked == 1) {
                l lVar5 = l.this;
                lVar5.f6961l = -1;
                lVar5.F(null, 0);
            } else {
                int i8 = l.this.f6961l;
                if (i8 != -1 && (findPointerIndex = motionEvent.findPointerIndex(i8)) >= 0) {
                    l.this.o(actionMasked, motionEvent, findPointerIndex);
                }
            }
            VelocityTracker velocityTracker = l.this.f6968t;
            if (velocityTracker != null) {
                velocityTracker.addMovement(motionEvent);
            }
            return l.this.f6952c != null;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.r
        public void e(boolean z4) {
            if (z4) {
                l.this.F(null, 0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends h {

        /* renamed from: o  reason: collision with root package name */
        final /* synthetic */ int f6977o;

        /* renamed from: p  reason: collision with root package name */
        final /* synthetic */ RecyclerView.b0 f6978p;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(RecyclerView.b0 b0Var, int i8, int i9, float f5, float f8, float f9, float f10, int i10, RecyclerView.b0 b0Var2) {
            super(b0Var, i8, i9, f5, f8, f9, f10);
            this.f6977o = i10;
            this.f6978p = b0Var2;
        }

        @Override // androidx.recyclerview.widget.l.h, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            if (this.f6999l) {
                return;
            }
            if (this.f6977o <= 0) {
                l lVar = l.this;
                lVar.f6962m.c(lVar.f6966r, this.f6978p);
            } else {
                l.this.f6950a.add(this.f6978p.f6628a);
                this.f6996i = true;
                int i8 = this.f6977o;
                if (i8 > 0) {
                    l.this.B(this, i8);
                }
            }
            l lVar2 = l.this;
            View view = lVar2.f6972x;
            View view2 = this.f6978p.f6628a;
            if (view == view2) {
                lVar2.D(view2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ h f6979a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ int f6980b;

        d(h hVar, int i8) {
            this.f6979a = hVar;
            this.f6980b = i8;
        }

        @Override // java.lang.Runnable
        public void run() {
            RecyclerView recyclerView = l.this.f6966r;
            if (recyclerView == null || !recyclerView.isAttachedToWindow()) {
                return;
            }
            h hVar = this.f6979a;
            if (hVar.f6999l || hVar.f6992e.j() == -1) {
                return;
            }
            RecyclerView.l itemAnimator = l.this.f6966r.getItemAnimator();
            if ((itemAnimator == null || !itemAnimator.q(null)) && !l.this.x()) {
                l.this.f6962m.B(this.f6979a.f6992e, this.f6980b);
            } else {
                l.this.f6966r.post(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e implements RecyclerView.j {
        e() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public int a(int i8, int i9) {
            l lVar = l.this;
            View view = lVar.f6972x;
            if (view == null) {
                return i9;
            }
            int i10 = lVar.f6973y;
            if (i10 == -1) {
                i10 = lVar.f6966r.indexOfChild(view);
                l.this.f6973y = i10;
            }
            return i9 == i8 + (-1) ? i10 : i9 < i10 ? i9 : i9 + 1;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class f {

        /* renamed from: b  reason: collision with root package name */
        private static final Interpolator f6983b = new a();

        /* renamed from: c  reason: collision with root package name */
        private static final Interpolator f6984c = new b();

        /* renamed from: a  reason: collision with root package name */
        private int f6985a = -1;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements Interpolator {
            a() {
            }

            @Override // android.animation.TimeInterpolator
            public float getInterpolation(float f5) {
                return f5 * f5 * f5 * f5 * f5;
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class b implements Interpolator {
            b() {
            }

            @Override // android.animation.TimeInterpolator
            public float getInterpolation(float f5) {
                float f8 = f5 - 1.0f;
                return (f8 * f8 * f8 * f8 * f8) + 1.0f;
            }
        }

        public static int e(int i8, int i9) {
            int i10;
            int i11 = i8 & 789516;
            if (i11 == 0) {
                return i8;
            }
            int i12 = i8 & (~i11);
            if (i9 == 0) {
                i10 = i11 << 2;
            } else {
                int i13 = i11 << 1;
                i12 |= (-789517) & i13;
                i10 = (i13 & 789516) << 2;
            }
            return i12 | i10;
        }

        private int i(RecyclerView recyclerView) {
            if (this.f6985a == -1) {
                this.f6985a = recyclerView.getResources().getDimensionPixelSize(p1.b.f22268d);
            }
            return this.f6985a;
        }

        public static int s(int i8, int i9) {
            return i9 << (i8 * 8);
        }

        public static int t(int i8, int i9) {
            int s8 = s(0, i9 | i8);
            return s(2, i8) | s(1, i9) | s8;
        }

        public void A(RecyclerView.b0 b0Var, int i8) {
            if (b0Var != null) {
                n.f7003a.b(b0Var.f6628a);
            }
        }

        public abstract void B(RecyclerView.b0 b0Var, int i8);

        public boolean a(RecyclerView recyclerView, RecyclerView.b0 b0Var, RecyclerView.b0 b0Var2) {
            return true;
        }

        public RecyclerView.b0 b(RecyclerView.b0 b0Var, List<RecyclerView.b0> list, int i8, int i9) {
            int bottom;
            int abs;
            int top;
            int abs2;
            int left;
            int abs3;
            int right;
            int abs4;
            int width = i8 + b0Var.f6628a.getWidth();
            int height = i9 + b0Var.f6628a.getHeight();
            int left2 = i8 - b0Var.f6628a.getLeft();
            int top2 = i9 - b0Var.f6628a.getTop();
            int size = list.size();
            RecyclerView.b0 b0Var2 = null;
            int i10 = -1;
            for (int i11 = 0; i11 < size; i11++) {
                RecyclerView.b0 b0Var3 = list.get(i11);
                if (left2 > 0 && (right = b0Var3.f6628a.getRight() - width) < 0 && b0Var3.f6628a.getRight() > b0Var.f6628a.getRight() && (abs4 = Math.abs(right)) > i10) {
                    b0Var2 = b0Var3;
                    i10 = abs4;
                }
                if (left2 < 0 && (left = b0Var3.f6628a.getLeft() - i8) > 0 && b0Var3.f6628a.getLeft() < b0Var.f6628a.getLeft() && (abs3 = Math.abs(left)) > i10) {
                    b0Var2 = b0Var3;
                    i10 = abs3;
                }
                if (top2 < 0 && (top = b0Var3.f6628a.getTop() - i9) > 0 && b0Var3.f6628a.getTop() < b0Var.f6628a.getTop() && (abs2 = Math.abs(top)) > i10) {
                    b0Var2 = b0Var3;
                    i10 = abs2;
                }
                if (top2 > 0 && (bottom = b0Var3.f6628a.getBottom() - height) < 0 && b0Var3.f6628a.getBottom() > b0Var.f6628a.getBottom() && (abs = Math.abs(bottom)) > i10) {
                    b0Var2 = b0Var3;
                    i10 = abs;
                }
            }
            return b0Var2;
        }

        public void c(RecyclerView recyclerView, RecyclerView.b0 b0Var) {
            n.f7003a.a(b0Var.f6628a);
        }

        public int d(int i8, int i9) {
            int i10;
            int i11 = i8 & 3158064;
            if (i11 == 0) {
                return i8;
            }
            int i12 = i8 & (~i11);
            if (i9 == 0) {
                i10 = i11 >> 2;
            } else {
                int i13 = i11 >> 1;
                i12 |= (-3158065) & i13;
                i10 = (i13 & 3158064) >> 2;
            }
            return i12 | i10;
        }

        final int f(RecyclerView recyclerView, RecyclerView.b0 b0Var) {
            return d(k(recyclerView, b0Var), c0.E(recyclerView));
        }

        public long g(RecyclerView recyclerView, int i8, float f5, float f8) {
            RecyclerView.l itemAnimator = recyclerView.getItemAnimator();
            return itemAnimator == null ? i8 == 8 ? 200L : 250L : i8 == 8 ? itemAnimator.n() : itemAnimator.o();
        }

        public int h() {
            return 0;
        }

        public float j(RecyclerView.b0 b0Var) {
            return 0.5f;
        }

        public abstract int k(RecyclerView recyclerView, RecyclerView.b0 b0Var);

        public float l(float f5) {
            return f5;
        }

        public float m(RecyclerView.b0 b0Var) {
            return 0.5f;
        }

        public float n(float f5) {
            return f5;
        }

        boolean o(RecyclerView recyclerView, RecyclerView.b0 b0Var) {
            return (f(recyclerView, b0Var) & 16711680) != 0;
        }

        public int p(RecyclerView recyclerView, int i8, int i9, int i10, long j8) {
            int signum = (int) (((int) (((int) Math.signum(i9)) * i(recyclerView) * f6984c.getInterpolation(Math.min(1.0f, (Math.abs(i9) * 1.0f) / i8)))) * f6983b.getInterpolation(j8 <= 2000 ? ((float) j8) / 2000.0f : 1.0f));
            return signum == 0 ? i9 > 0 ? 1 : -1 : signum;
        }

        public boolean q() {
            return true;
        }

        public abstract boolean r();

        public void u(Canvas canvas, RecyclerView recyclerView, RecyclerView.b0 b0Var, float f5, float f8, int i8, boolean z4) {
            n.f7003a.d(canvas, recyclerView, b0Var.f6628a, f5, f8, i8, z4);
        }

        public void v(Canvas canvas, RecyclerView recyclerView, RecyclerView.b0 b0Var, float f5, float f8, int i8, boolean z4) {
            n.f7003a.c(canvas, recyclerView, b0Var.f6628a, f5, f8, i8, z4);
        }

        void w(Canvas canvas, RecyclerView recyclerView, RecyclerView.b0 b0Var, List<h> list, int i8, float f5, float f8) {
            int size = list.size();
            for (int i9 = 0; i9 < size; i9++) {
                h hVar = list.get(i9);
                hVar.e();
                int save = canvas.save();
                u(canvas, recyclerView, hVar.f6992e, hVar.f6997j, hVar.f6998k, hVar.f6993f, false);
                canvas.restoreToCount(save);
            }
            if (b0Var != null) {
                int save2 = canvas.save();
                u(canvas, recyclerView, b0Var, f5, f8, i8, true);
                canvas.restoreToCount(save2);
            }
        }

        void x(Canvas canvas, RecyclerView recyclerView, RecyclerView.b0 b0Var, List<h> list, int i8, float f5, float f8) {
            int size = list.size();
            boolean z4 = false;
            for (int i9 = 0; i9 < size; i9++) {
                h hVar = list.get(i9);
                int save = canvas.save();
                v(canvas, recyclerView, hVar.f6992e, hVar.f6997j, hVar.f6998k, hVar.f6993f, false);
                canvas.restoreToCount(save);
            }
            if (b0Var != null) {
                int save2 = canvas.save();
                v(canvas, recyclerView, b0Var, f5, f8, i8, true);
                canvas.restoreToCount(save2);
            }
            for (int i10 = size - 1; i10 >= 0; i10--) {
                h hVar2 = list.get(i10);
                boolean z8 = hVar2.f7000m;
                if (z8 && !hVar2.f6996i) {
                    list.remove(i10);
                } else if (!z8) {
                    z4 = true;
                }
            }
            if (z4) {
                recyclerView.invalidate();
            }
        }

        public abstract boolean y(RecyclerView recyclerView, RecyclerView.b0 b0Var, RecyclerView.b0 b0Var2);

        public void z(RecyclerView recyclerView, RecyclerView.b0 b0Var, int i8, RecyclerView.b0 b0Var2, int i9, int i10, int i11) {
            RecyclerView.o layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof i) {
                ((i) layoutManager).b(b0Var.f6628a, b0Var2.f6628a, i10, i11);
                return;
            }
            if (layoutManager.l()) {
                if (layoutManager.R(b0Var2.f6628a) <= recyclerView.getPaddingLeft()) {
                    recyclerView.l1(i9);
                }
                if (layoutManager.U(b0Var2.f6628a) >= recyclerView.getWidth() - recyclerView.getPaddingRight()) {
                    recyclerView.l1(i9);
                }
            }
            if (layoutManager.m()) {
                if (layoutManager.V(b0Var2.f6628a) <= recyclerView.getPaddingTop()) {
                    recyclerView.l1(i9);
                }
                if (layoutManager.P(b0Var2.f6628a) >= recyclerView.getHeight() - recyclerView.getPaddingBottom()) {
                    recyclerView.l1(i9);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class g extends GestureDetector.SimpleOnGestureListener {

        /* renamed from: a  reason: collision with root package name */
        private boolean f6986a = true;

        g() {
        }

        void a() {
            this.f6986a = false;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            View t8;
            RecyclerView.b0 h02;
            if (!this.f6986a || (t8 = l.this.t(motionEvent)) == null || (h02 = l.this.f6966r.h0(t8)) == null) {
                return;
            }
            l lVar = l.this;
            if (lVar.f6962m.o(lVar.f6966r, h02)) {
                int pointerId = motionEvent.getPointerId(0);
                int i8 = l.this.f6961l;
                if (pointerId == i8) {
                    int findPointerIndex = motionEvent.findPointerIndex(i8);
                    float x8 = motionEvent.getX(findPointerIndex);
                    float y8 = motionEvent.getY(findPointerIndex);
                    l lVar2 = l.this;
                    lVar2.f6953d = x8;
                    lVar2.f6954e = y8;
                    lVar2.f6958i = 0.0f;
                    lVar2.f6957h = 0.0f;
                    if (lVar2.f6962m.r()) {
                        l.this.F(h02, 2);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h implements Animator.AnimatorListener {

        /* renamed from: a  reason: collision with root package name */
        final float f6988a;

        /* renamed from: b  reason: collision with root package name */
        final float f6989b;

        /* renamed from: c  reason: collision with root package name */
        final float f6990c;

        /* renamed from: d  reason: collision with root package name */
        final float f6991d;

        /* renamed from: e  reason: collision with root package name */
        final RecyclerView.b0 f6992e;

        /* renamed from: f  reason: collision with root package name */
        final int f6993f;

        /* renamed from: g  reason: collision with root package name */
        private final ValueAnimator f6994g;

        /* renamed from: h  reason: collision with root package name */
        final int f6995h;

        /* renamed from: i  reason: collision with root package name */
        boolean f6996i;

        /* renamed from: j  reason: collision with root package name */
        float f6997j;

        /* renamed from: k  reason: collision with root package name */
        float f6998k;

        /* renamed from: l  reason: collision with root package name */
        boolean f6999l = false;

        /* renamed from: m  reason: collision with root package name */
        boolean f7000m = false;

        /* renamed from: n  reason: collision with root package name */
        private float f7001n;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements ValueAnimator.AnimatorUpdateListener {
            a() {
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                h.this.c(valueAnimator.getAnimatedFraction());
            }
        }

        h(RecyclerView.b0 b0Var, int i8, int i9, float f5, float f8, float f9, float f10) {
            this.f6993f = i9;
            this.f6995h = i8;
            this.f6992e = b0Var;
            this.f6988a = f5;
            this.f6989b = f8;
            this.f6990c = f9;
            this.f6991d = f10;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.f6994g = ofFloat;
            ofFloat.addUpdateListener(new a());
            ofFloat.setTarget(b0Var.f6628a);
            ofFloat.addListener(this);
            c(0.0f);
        }

        public void a() {
            this.f6994g.cancel();
        }

        public void b(long j8) {
            this.f6994g.setDuration(j8);
        }

        public void c(float f5) {
            this.f7001n = f5;
        }

        public void d() {
            this.f6992e.G(false);
            this.f6994g.start();
        }

        public void e() {
            float f5 = this.f6988a;
            float f8 = this.f6990c;
            this.f6997j = f5 == f8 ? this.f6992e.f6628a.getTranslationX() : f5 + (this.f7001n * (f8 - f5));
            float f9 = this.f6989b;
            float f10 = this.f6991d;
            this.f6998k = f9 == f10 ? this.f6992e.f6628a.getTranslationY() : f9 + (this.f7001n * (f10 - f9));
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            c(1.0f);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (!this.f7000m) {
                this.f6992e.G(true);
            }
            this.f7000m = true;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface i {
        void b(View view, View view2, int i8, int i9);
    }

    public l(f fVar) {
        this.f6962m = fVar;
    }

    private void C() {
        VelocityTracker velocityTracker = this.f6968t;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.f6968t = null;
        }
    }

    private void G() {
        this.q = ViewConfiguration.get(this.f6966r.getContext()).getScaledTouchSlop();
        this.f6966r.h(this);
        this.f6966r.k(this.B);
        this.f6966r.j(this);
        I();
    }

    private void I() {
        this.A = new g();
        this.f6974z = new androidx.core.view.e(this.f6966r.getContext(), this.A);
    }

    private void J() {
        g gVar = this.A;
        if (gVar != null) {
            gVar.a();
            this.A = null;
        }
        if (this.f6974z != null) {
            this.f6974z = null;
        }
    }

    private int K(RecyclerView.b0 b0Var) {
        if (this.f6963n == 2) {
            return 0;
        }
        int k8 = this.f6962m.k(this.f6966r, b0Var);
        int d8 = (this.f6962m.d(k8, c0.E(this.f6966r)) & 65280) >> 8;
        if (d8 == 0) {
            return 0;
        }
        int i8 = (k8 & 65280) >> 8;
        if (Math.abs(this.f6957h) > Math.abs(this.f6958i)) {
            int n8 = n(b0Var, d8);
            if (n8 > 0) {
                return (i8 & n8) == 0 ? f.e(n8, c0.E(this.f6966r)) : n8;
            }
            int p8 = p(b0Var, d8);
            if (p8 > 0) {
                return p8;
            }
        } else {
            int p9 = p(b0Var, d8);
            if (p9 > 0) {
                return p9;
            }
            int n9 = n(b0Var, d8);
            if (n9 > 0) {
                return (i8 & n9) == 0 ? f.e(n9, c0.E(this.f6966r)) : n9;
            }
        }
        return 0;
    }

    private void l() {
        if (Build.VERSION.SDK_INT >= 21) {
            return;
        }
        if (this.f6971w == null) {
            this.f6971w = new e();
        }
        this.f6966r.setChildDrawingOrderCallback(this.f6971w);
    }

    private int n(RecyclerView.b0 b0Var, int i8) {
        if ((i8 & 12) != 0) {
            int i9 = this.f6957h > 0.0f ? 8 : 4;
            VelocityTracker velocityTracker = this.f6968t;
            if (velocityTracker != null && this.f6961l > -1) {
                velocityTracker.computeCurrentVelocity(1000, this.f6962m.n(this.f6956g));
                float xVelocity = this.f6968t.getXVelocity(this.f6961l);
                float yVelocity = this.f6968t.getYVelocity(this.f6961l);
                int i10 = xVelocity <= 0.0f ? 4 : 8;
                float abs = Math.abs(xVelocity);
                if ((i10 & i8) != 0 && i9 == i10 && abs >= this.f6962m.l(this.f6955f) && abs > Math.abs(yVelocity)) {
                    return i10;
                }
            }
            float width = this.f6966r.getWidth() * this.f6962m.m(b0Var);
            if ((i8 & i9) == 0 || Math.abs(this.f6957h) <= width) {
                return 0;
            }
            return i9;
        }
        return 0;
    }

    private int p(RecyclerView.b0 b0Var, int i8) {
        if ((i8 & 3) != 0) {
            int i9 = this.f6958i > 0.0f ? 2 : 1;
            VelocityTracker velocityTracker = this.f6968t;
            if (velocityTracker != null && this.f6961l > -1) {
                velocityTracker.computeCurrentVelocity(1000, this.f6962m.n(this.f6956g));
                float xVelocity = this.f6968t.getXVelocity(this.f6961l);
                float yVelocity = this.f6968t.getYVelocity(this.f6961l);
                int i10 = yVelocity <= 0.0f ? 1 : 2;
                float abs = Math.abs(yVelocity);
                if ((i10 & i8) != 0 && i10 == i9 && abs >= this.f6962m.l(this.f6955f) && abs > Math.abs(xVelocity)) {
                    return i10;
                }
            }
            float height = this.f6966r.getHeight() * this.f6962m.m(b0Var);
            if ((i8 & i9) == 0 || Math.abs(this.f6958i) <= height) {
                return 0;
            }
            return i9;
        }
        return 0;
    }

    private void q() {
        this.f6966r.Z0(this);
        this.f6966r.b1(this.B);
        this.f6966r.a1(this);
        for (int size = this.f6965p.size() - 1; size >= 0; size--) {
            this.f6962m.c(this.f6966r, this.f6965p.get(0).f6992e);
        }
        this.f6965p.clear();
        this.f6972x = null;
        this.f6973y = -1;
        C();
        J();
    }

    private List<RecyclerView.b0> u(RecyclerView.b0 b0Var) {
        RecyclerView.b0 b0Var2 = b0Var;
        List<RecyclerView.b0> list = this.f6969u;
        if (list == null) {
            this.f6969u = new ArrayList();
            this.f6970v = new ArrayList();
        } else {
            list.clear();
            this.f6970v.clear();
        }
        int h8 = this.f6962m.h();
        int round = Math.round(this.f6959j + this.f6957h) - h8;
        int round2 = Math.round(this.f6960k + this.f6958i) - h8;
        int i8 = h8 * 2;
        int width = b0Var2.f6628a.getWidth() + round + i8;
        int height = b0Var2.f6628a.getHeight() + round2 + i8;
        int i9 = (round + width) / 2;
        int i10 = (round2 + height) / 2;
        RecyclerView.o layoutManager = this.f6966r.getLayoutManager();
        int K = layoutManager.K();
        int i11 = 0;
        while (i11 < K) {
            View J = layoutManager.J(i11);
            if (J != b0Var2.f6628a && J.getBottom() >= round2 && J.getTop() <= height && J.getRight() >= round && J.getLeft() <= width) {
                RecyclerView.b0 h02 = this.f6966r.h0(J);
                if (this.f6962m.a(this.f6966r, this.f6952c, h02)) {
                    int abs = Math.abs(i9 - ((J.getLeft() + J.getRight()) / 2));
                    int abs2 = Math.abs(i10 - ((J.getTop() + J.getBottom()) / 2));
                    int i12 = (abs * abs) + (abs2 * abs2);
                    int size = this.f6969u.size();
                    int i13 = 0;
                    for (int i14 = 0; i14 < size && i12 > this.f6970v.get(i14).intValue(); i14++) {
                        i13++;
                    }
                    this.f6969u.add(i13, h02);
                    this.f6970v.add(i13, Integer.valueOf(i12));
                }
            }
            i11++;
            b0Var2 = b0Var;
        }
        return this.f6969u;
    }

    private RecyclerView.b0 v(MotionEvent motionEvent) {
        View t8;
        RecyclerView.o layoutManager = this.f6966r.getLayoutManager();
        int i8 = this.f6961l;
        if (i8 == -1) {
            return null;
        }
        int findPointerIndex = motionEvent.findPointerIndex(i8);
        float abs = Math.abs(motionEvent.getX(findPointerIndex) - this.f6953d);
        float abs2 = Math.abs(motionEvent.getY(findPointerIndex) - this.f6954e);
        int i9 = this.q;
        if (abs >= i9 || abs2 >= i9) {
            if (abs <= abs2 || !layoutManager.l()) {
                if ((abs2 <= abs || !layoutManager.m()) && (t8 = t(motionEvent)) != null) {
                    return this.f6966r.h0(t8);
                }
                return null;
            }
            return null;
        }
        return null;
    }

    private void w(float[] fArr) {
        if ((this.f6964o & 12) != 0) {
            fArr[0] = (this.f6959j + this.f6957h) - this.f6952c.f6628a.getLeft();
        } else {
            fArr[0] = this.f6952c.f6628a.getTranslationX();
        }
        if ((this.f6964o & 3) != 0) {
            fArr[1] = (this.f6960k + this.f6958i) - this.f6952c.f6628a.getTop();
        } else {
            fArr[1] = this.f6952c.f6628a.getTranslationY();
        }
    }

    private static boolean y(View view, float f5, float f8, float f9, float f10) {
        return f5 >= f9 && f5 <= f9 + ((float) view.getWidth()) && f8 >= f10 && f8 <= f10 + ((float) view.getHeight());
    }

    void A() {
        VelocityTracker velocityTracker = this.f6968t;
        if (velocityTracker != null) {
            velocityTracker.recycle();
        }
        this.f6968t = VelocityTracker.obtain();
    }

    void B(h hVar, int i8) {
        this.f6966r.post(new d(hVar, i8));
    }

    void D(View view) {
        if (view == this.f6972x) {
            this.f6972x = null;
            if (this.f6971w != null) {
                this.f6966r.setChildDrawingOrderCallback(null);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x00c1, code lost:
        if (r1 > 0) goto L24;
     */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00fd  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0100 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x010c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean E() {
        /*
            Method dump skipped, instructions count: 277
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.l.E():boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0136  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void F(androidx.recyclerview.widget.RecyclerView.b0 r24, int r25) {
        /*
            Method dump skipped, instructions count: 334
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.l.F(androidx.recyclerview.widget.RecyclerView$b0, int):void");
    }

    public void H(RecyclerView.b0 b0Var) {
        if (!this.f6962m.o(this.f6966r, b0Var)) {
            Log.e("ItemTouchHelper", "Start drag has been called but dragging is not enabled");
        } else if (b0Var.f6628a.getParent() != this.f6966r) {
            Log.e("ItemTouchHelper", "Start drag has been called with a view holder which is not a child of the RecyclerView which is controlled by this ItemTouchHelper.");
        } else {
            A();
            this.f6958i = 0.0f;
            this.f6957h = 0.0f;
            F(b0Var, 2);
        }
    }

    void L(MotionEvent motionEvent, int i8, int i9) {
        float x8 = motionEvent.getX(i9);
        float y8 = motionEvent.getY(i9);
        float f5 = x8 - this.f6953d;
        this.f6957h = f5;
        this.f6958i = y8 - this.f6954e;
        if ((i8 & 4) == 0) {
            this.f6957h = Math.max(0.0f, f5);
        }
        if ((i8 & 8) == 0) {
            this.f6957h = Math.min(0.0f, this.f6957h);
        }
        if ((i8 & 1) == 0) {
            this.f6958i = Math.max(0.0f, this.f6958i);
        }
        if ((i8 & 2) == 0) {
            this.f6958i = Math.min(0.0f, this.f6958i);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void b(View view) {
        D(view);
        RecyclerView.b0 h02 = this.f6966r.h0(view);
        if (h02 == null) {
            return;
        }
        RecyclerView.b0 b0Var = this.f6952c;
        if (b0Var != null && h02 == b0Var) {
            F(null, 0);
            return;
        }
        r(h02, false);
        if (this.f6950a.remove(h02.f6628a)) {
            this.f6962m.c(this.f6966r, h02);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void d(View view) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.n
    public void g(Rect rect, View view, RecyclerView recyclerView, RecyclerView.y yVar) {
        rect.setEmpty();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.n
    public void i(Canvas canvas, RecyclerView recyclerView, RecyclerView.y yVar) {
        float f5;
        float f8;
        this.f6973y = -1;
        if (this.f6952c != null) {
            w(this.f6951b);
            float[] fArr = this.f6951b;
            float f9 = fArr[0];
            f8 = fArr[1];
            f5 = f9;
        } else {
            f5 = 0.0f;
            f8 = 0.0f;
        }
        this.f6962m.w(canvas, recyclerView, this.f6952c, this.f6965p, this.f6963n, f5, f8);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.n
    public void k(Canvas canvas, RecyclerView recyclerView, RecyclerView.y yVar) {
        float f5;
        float f8;
        if (this.f6952c != null) {
            w(this.f6951b);
            float[] fArr = this.f6951b;
            float f9 = fArr[0];
            f8 = fArr[1];
            f5 = f9;
        } else {
            f5 = 0.0f;
            f8 = 0.0f;
        }
        this.f6962m.x(canvas, recyclerView, this.f6952c, this.f6965p, this.f6963n, f5, f8);
    }

    public void m(RecyclerView recyclerView) {
        RecyclerView recyclerView2 = this.f6966r;
        if (recyclerView2 == recyclerView) {
            return;
        }
        if (recyclerView2 != null) {
            q();
        }
        this.f6966r = recyclerView;
        if (recyclerView != null) {
            Resources resources = recyclerView.getResources();
            this.f6955f = resources.getDimension(p1.b.f22270f);
            this.f6956g = resources.getDimension(p1.b.f22269e);
            G();
        }
    }

    void o(int i8, MotionEvent motionEvent, int i9) {
        RecyclerView.b0 v8;
        int f5;
        if (this.f6952c != null || i8 != 2 || this.f6963n == 2 || !this.f6962m.q() || this.f6966r.getScrollState() == 1 || (v8 = v(motionEvent)) == null || (f5 = (this.f6962m.f(this.f6966r, v8) & 65280) >> 8) == 0) {
            return;
        }
        float x8 = motionEvent.getX(i9);
        float y8 = motionEvent.getY(i9);
        float f8 = x8 - this.f6953d;
        float f9 = y8 - this.f6954e;
        float abs = Math.abs(f8);
        float abs2 = Math.abs(f9);
        int i10 = this.q;
        if (abs >= i10 || abs2 >= i10) {
            if (abs > abs2) {
                if (f8 < 0.0f && (f5 & 4) == 0) {
                    return;
                }
                if (f8 > 0.0f && (f5 & 8) == 0) {
                    return;
                }
            } else if (f9 < 0.0f && (f5 & 1) == 0) {
                return;
            } else {
                if (f9 > 0.0f && (f5 & 2) == 0) {
                    return;
                }
            }
            this.f6958i = 0.0f;
            this.f6957h = 0.0f;
            this.f6961l = motionEvent.getPointerId(0);
            F(v8, 1);
        }
    }

    void r(RecyclerView.b0 b0Var, boolean z4) {
        for (int size = this.f6965p.size() - 1; size >= 0; size--) {
            h hVar = this.f6965p.get(size);
            if (hVar.f6992e == b0Var) {
                hVar.f6999l |= z4;
                if (!hVar.f7000m) {
                    hVar.a();
                }
                this.f6965p.remove(size);
                return;
            }
        }
    }

    h s(MotionEvent motionEvent) {
        if (this.f6965p.isEmpty()) {
            return null;
        }
        View t8 = t(motionEvent);
        for (int size = this.f6965p.size() - 1; size >= 0; size--) {
            h hVar = this.f6965p.get(size);
            if (hVar.f6992e.f6628a == t8) {
                return hVar;
            }
        }
        return null;
    }

    View t(MotionEvent motionEvent) {
        float x8 = motionEvent.getX();
        float y8 = motionEvent.getY();
        RecyclerView.b0 b0Var = this.f6952c;
        if (b0Var != null) {
            View view = b0Var.f6628a;
            if (y(view, x8, y8, this.f6959j + this.f6957h, this.f6960k + this.f6958i)) {
                return view;
            }
        }
        for (int size = this.f6965p.size() - 1; size >= 0; size--) {
            h hVar = this.f6965p.get(size);
            View view2 = hVar.f6992e.f6628a;
            if (y(view2, x8, y8, hVar.f6997j, hVar.f6998k)) {
                return view2;
            }
        }
        return this.f6966r.S(x8, y8);
    }

    boolean x() {
        int size = this.f6965p.size();
        for (int i8 = 0; i8 < size; i8++) {
            if (!this.f6965p.get(i8).f7000m) {
                return true;
            }
        }
        return false;
    }

    void z(RecyclerView.b0 b0Var) {
        if (!this.f6966r.isLayoutRequested() && this.f6963n == 2) {
            float j8 = this.f6962m.j(b0Var);
            int i8 = (int) (this.f6959j + this.f6957h);
            int i9 = (int) (this.f6960k + this.f6958i);
            if (Math.abs(i9 - b0Var.f6628a.getTop()) >= b0Var.f6628a.getHeight() * j8 || Math.abs(i8 - b0Var.f6628a.getLeft()) >= b0Var.f6628a.getWidth() * j8) {
                List<RecyclerView.b0> u8 = u(b0Var);
                if (u8.size() == 0) {
                    return;
                }
                RecyclerView.b0 b9 = this.f6962m.b(b0Var, u8, i8, i9);
                if (b9 == null) {
                    this.f6969u.clear();
                    this.f6970v.clear();
                    return;
                }
                int j9 = b9.j();
                int j10 = b0Var.j();
                if (this.f6962m.y(this.f6966r, b0Var, b9)) {
                    this.f6962m.z(this.f6966r, b0Var, j10, b9, j9, i8, i9);
                }
            }
        }
    }
}
