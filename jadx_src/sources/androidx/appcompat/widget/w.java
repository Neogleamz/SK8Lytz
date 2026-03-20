package androidx.appcompat.widget;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class w implements View.OnTouchListener, View.OnAttachStateChangeListener {

    /* renamed from: a  reason: collision with root package name */
    private final float f1637a;

    /* renamed from: b  reason: collision with root package name */
    private final int f1638b;

    /* renamed from: c  reason: collision with root package name */
    private final int f1639c;

    /* renamed from: d  reason: collision with root package name */
    final View f1640d;

    /* renamed from: e  reason: collision with root package name */
    private Runnable f1641e;

    /* renamed from: f  reason: collision with root package name */
    private Runnable f1642f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f1643g;

    /* renamed from: h  reason: collision with root package name */
    private int f1644h;

    /* renamed from: j  reason: collision with root package name */
    private final int[] f1645j = new int[2];

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewParent parent = w.this.f1640d.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            w.this.e();
        }
    }

    public w(View view) {
        this.f1640d = view;
        view.setLongClickable(true);
        view.addOnAttachStateChangeListener(this);
        this.f1637a = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
        int tapTimeout = ViewConfiguration.getTapTimeout();
        this.f1638b = tapTimeout;
        this.f1639c = (tapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
    }

    private void a() {
        Runnable runnable = this.f1642f;
        if (runnable != null) {
            this.f1640d.removeCallbacks(runnable);
        }
        Runnable runnable2 = this.f1641e;
        if (runnable2 != null) {
            this.f1640d.removeCallbacks(runnable2);
        }
    }

    private boolean f(MotionEvent motionEvent) {
        u uVar;
        View view = this.f1640d;
        androidx.appcompat.view.menu.p b9 = b();
        if (b9 == null || !b9.b() || (uVar = (u) b9.m()) == null || !uVar.isShown()) {
            return false;
        }
        MotionEvent obtainNoHistory = MotionEvent.obtainNoHistory(motionEvent);
        i(view, obtainNoHistory);
        j(uVar, obtainNoHistory);
        boolean e8 = uVar.e(obtainNoHistory, this.f1644h);
        obtainNoHistory.recycle();
        int actionMasked = motionEvent.getActionMasked();
        return e8 && (actionMasked != 1 && actionMasked != 3);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0017, code lost:
        if (r1 != 3) goto L13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean g(android.view.MotionEvent r6) {
        /*
            r5 = this;
            android.view.View r0 = r5.f1640d
            boolean r1 = r0.isEnabled()
            r2 = 0
            if (r1 != 0) goto La
            return r2
        La:
            int r1 = r6.getActionMasked()
            if (r1 == 0) goto L41
            r3 = 1
            if (r1 == r3) goto L3d
            r4 = 2
            if (r1 == r4) goto L1a
            r6 = 3
            if (r1 == r6) goto L3d
            goto L6d
        L1a:
            int r1 = r5.f1644h
            int r1 = r6.findPointerIndex(r1)
            if (r1 < 0) goto L6d
            float r4 = r6.getX(r1)
            float r6 = r6.getY(r1)
            float r1 = r5.f1637a
            boolean r6 = h(r0, r4, r6, r1)
            if (r6 != 0) goto L6d
            r5.a()
            android.view.ViewParent r6 = r0.getParent()
            r6.requestDisallowInterceptTouchEvent(r3)
            return r3
        L3d:
            r5.a()
            goto L6d
        L41:
            int r6 = r6.getPointerId(r2)
            r5.f1644h = r6
            java.lang.Runnable r6 = r5.f1641e
            if (r6 != 0) goto L52
            androidx.appcompat.widget.w$a r6 = new androidx.appcompat.widget.w$a
            r6.<init>()
            r5.f1641e = r6
        L52:
            java.lang.Runnable r6 = r5.f1641e
            int r1 = r5.f1638b
            long r3 = (long) r1
            r0.postDelayed(r6, r3)
            java.lang.Runnable r6 = r5.f1642f
            if (r6 != 0) goto L65
            androidx.appcompat.widget.w$b r6 = new androidx.appcompat.widget.w$b
            r6.<init>()
            r5.f1642f = r6
        L65:
            java.lang.Runnable r6 = r5.f1642f
            int r1 = r5.f1639c
            long r3 = (long) r1
            r0.postDelayed(r6, r3)
        L6d:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.w.g(android.view.MotionEvent):boolean");
    }

    private static boolean h(View view, float f5, float f8, float f9) {
        float f10 = -f9;
        return f5 >= f10 && f8 >= f10 && f5 < ((float) (view.getRight() - view.getLeft())) + f9 && f8 < ((float) (view.getBottom() - view.getTop())) + f9;
    }

    private boolean i(View view, MotionEvent motionEvent) {
        int[] iArr = this.f1645j;
        view.getLocationOnScreen(iArr);
        motionEvent.offsetLocation(iArr[0], iArr[1]);
        return true;
    }

    private boolean j(View view, MotionEvent motionEvent) {
        int[] iArr = this.f1645j;
        view.getLocationOnScreen(iArr);
        motionEvent.offsetLocation(-iArr[0], -iArr[1]);
        return true;
    }

    public abstract androidx.appcompat.view.menu.p b();

    protected abstract boolean c();

    protected boolean d() {
        androidx.appcompat.view.menu.p b9 = b();
        if (b9 == null || !b9.b()) {
            return true;
        }
        b9.dismiss();
        return true;
    }

    void e() {
        a();
        View view = this.f1640d;
        if (view.isEnabled() && !view.isLongClickable() && c()) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            long uptimeMillis = SystemClock.uptimeMillis();
            MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
            view.onTouchEvent(obtain);
            obtain.recycle();
            this.f1643g = true;
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean z4;
        boolean z8 = this.f1643g;
        if (z8) {
            z4 = f(motionEvent) || !d();
        } else {
            z4 = g(motionEvent) && c();
            if (z4) {
                long uptimeMillis = SystemClock.uptimeMillis();
                MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                this.f1640d.onTouchEvent(obtain);
                obtain.recycle();
            }
        }
        this.f1643g = z4;
        return z4 || z8;
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View view) {
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View view) {
        this.f1643g = false;
        this.f1644h = -1;
        Runnable runnable = this.f1641e;
        if (runnable != null) {
            this.f1640d.removeCallbacks(runnable);
        }
    }
}
