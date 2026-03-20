package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.c0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class HeaderBehavior<V extends View> extends ViewOffsetBehavior<V> {

    /* renamed from: d  reason: collision with root package name */
    private Runnable f17393d;

    /* renamed from: e  reason: collision with root package name */
    OverScroller f17394e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f17395f;

    /* renamed from: g  reason: collision with root package name */
    private int f17396g;

    /* renamed from: h  reason: collision with root package name */
    private int f17397h;

    /* renamed from: i  reason: collision with root package name */
    private int f17398i;

    /* renamed from: j  reason: collision with root package name */
    private VelocityTracker f17399j;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        private final CoordinatorLayout f17400a;

        /* renamed from: b  reason: collision with root package name */
        private final V f17401b;

        a(CoordinatorLayout coordinatorLayout, V v8) {
            this.f17400a = coordinatorLayout;
            this.f17401b = v8;
        }

        @Override // java.lang.Runnable
        public void run() {
            OverScroller overScroller;
            if (this.f17401b == null || (overScroller = HeaderBehavior.this.f17394e) == null) {
                return;
            }
            if (!overScroller.computeScrollOffset()) {
                HeaderBehavior.this.N(this.f17400a, this.f17401b);
                return;
            }
            HeaderBehavior headerBehavior = HeaderBehavior.this;
            headerBehavior.P(this.f17400a, this.f17401b, headerBehavior.f17394e.getCurrY());
            c0.l0(this.f17401b, this);
        }
    }

    public HeaderBehavior() {
        this.f17396g = -1;
        this.f17398i = -1;
    }

    public HeaderBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f17396g = -1;
        this.f17398i = -1;
    }

    private void I() {
        if (this.f17399j == null) {
            this.f17399j = VelocityTracker.obtain();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x008c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:37:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean D(androidx.coordinatorlayout.widget.CoordinatorLayout r12, V r13, android.view.MotionEvent r14) {
        /*
            r11 = this;
            int r0 = r14.getActionMasked()
            r1 = -1
            r2 = 1
            r3 = 0
            if (r0 == r2) goto L4e
            r4 = 2
            if (r0 == r4) goto L2d
            r12 = 3
            if (r0 == r12) goto L72
            r12 = 6
            if (r0 == r12) goto L13
            goto L4c
        L13:
            int r12 = r14.getActionIndex()
            if (r12 != 0) goto L1b
            r12 = r2
            goto L1c
        L1b:
            r12 = r3
        L1c:
            int r13 = r14.getPointerId(r12)
            r11.f17396g = r13
            float r12 = r14.getY(r12)
            r13 = 1056964608(0x3f000000, float:0.5)
            float r12 = r12 + r13
            int r12 = (int) r12
            r11.f17397h = r12
            goto L4c
        L2d:
            int r0 = r11.f17396g
            int r0 = r14.findPointerIndex(r0)
            if (r0 != r1) goto L36
            return r3
        L36:
            float r0 = r14.getY(r0)
            int r0 = (int) r0
            int r1 = r11.f17397h
            int r7 = r1 - r0
            r11.f17397h = r0
            int r8 = r11.K(r13)
            r9 = 0
            r4 = r11
            r5 = r12
            r6 = r13
            r4.O(r5, r6, r7, r8, r9)
        L4c:
            r12 = r3
            goto L81
        L4e:
            android.view.VelocityTracker r0 = r11.f17399j
            if (r0 == 0) goto L72
            r0.addMovement(r14)
            android.view.VelocityTracker r0 = r11.f17399j
            r4 = 1000(0x3e8, float:1.401E-42)
            r0.computeCurrentVelocity(r4)
            android.view.VelocityTracker r0 = r11.f17399j
            int r4 = r11.f17396g
            float r10 = r0.getYVelocity(r4)
            int r0 = r11.L(r13)
            int r8 = -r0
            r9 = 0
            r5 = r11
            r6 = r12
            r7 = r13
            r5.J(r6, r7, r8, r9, r10)
            r12 = r2
            goto L73
        L72:
            r12 = r3
        L73:
            r11.f17395f = r3
            r11.f17396g = r1
            android.view.VelocityTracker r13 = r11.f17399j
            if (r13 == 0) goto L81
            r13.recycle()
            r13 = 0
            r11.f17399j = r13
        L81:
            android.view.VelocityTracker r13 = r11.f17399j
            if (r13 == 0) goto L88
            r13.addMovement(r14)
        L88:
            boolean r13 = r11.f17395f
            if (r13 != 0) goto L90
            if (r12 == 0) goto L8f
            goto L90
        L8f:
            r2 = r3
        L90:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.appbar.HeaderBehavior.D(androidx.coordinatorlayout.widget.CoordinatorLayout, android.view.View, android.view.MotionEvent):boolean");
    }

    boolean H(V v8) {
        return false;
    }

    final boolean J(CoordinatorLayout coordinatorLayout, V v8, int i8, int i9, float f5) {
        Runnable runnable = this.f17393d;
        if (runnable != null) {
            v8.removeCallbacks(runnable);
            this.f17393d = null;
        }
        if (this.f17394e == null) {
            this.f17394e = new OverScroller(v8.getContext());
        }
        this.f17394e.fling(0, E(), 0, Math.round(f5), 0, 0, i8, i9);
        if (!this.f17394e.computeScrollOffset()) {
            N(coordinatorLayout, v8);
            return false;
        }
        a aVar = new a(coordinatorLayout, v8);
        this.f17393d = aVar;
        c0.l0(v8, aVar);
        return true;
    }

    int K(V v8) {
        return -v8.getHeight();
    }

    int L(V v8) {
        return v8.getHeight();
    }

    int M() {
        return E();
    }

    void N(CoordinatorLayout coordinatorLayout, V v8) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int O(CoordinatorLayout coordinatorLayout, V v8, int i8, int i9, int i10) {
        return Q(coordinatorLayout, v8, M() - i8, i9, i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int P(CoordinatorLayout coordinatorLayout, V v8, int i8) {
        return Q(coordinatorLayout, v8, i8, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    int Q(CoordinatorLayout coordinatorLayout, V v8, int i8, int i9, int i10) {
        int c9;
        int E = E();
        if (i9 == 0 || E < i9 || E > i10 || E == (c9 = t0.a.c(i8, i9, i10))) {
            return 0;
        }
        G(c9);
        return E - c9;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean k(CoordinatorLayout coordinatorLayout, V v8, MotionEvent motionEvent) {
        int findPointerIndex;
        if (this.f17398i < 0) {
            this.f17398i = ViewConfiguration.get(coordinatorLayout.getContext()).getScaledTouchSlop();
        }
        if (motionEvent.getActionMasked() == 2 && this.f17395f) {
            int i8 = this.f17396g;
            if (i8 == -1 || (findPointerIndex = motionEvent.findPointerIndex(i8)) == -1) {
                return false;
            }
            int y8 = (int) motionEvent.getY(findPointerIndex);
            if (Math.abs(y8 - this.f17397h) > this.f17398i) {
                this.f17397h = y8;
                return true;
            }
        }
        if (motionEvent.getActionMasked() == 0) {
            this.f17396g = -1;
            int x8 = (int) motionEvent.getX();
            int y9 = (int) motionEvent.getY();
            boolean z4 = H(v8) && coordinatorLayout.F(v8, x8, y9);
            this.f17395f = z4;
            if (z4) {
                this.f17397h = y9;
                this.f17396g = motionEvent.getPointerId(0);
                I();
                OverScroller overScroller = this.f17394e;
                if (overScroller != null && !overScroller.isFinished()) {
                    this.f17394e.abortAnimation();
                    return true;
                }
            }
        }
        VelocityTracker velocityTracker = this.f17399j;
        if (velocityTracker != null) {
            velocityTracker.addMovement(motionEvent);
        }
        return false;
    }
}
