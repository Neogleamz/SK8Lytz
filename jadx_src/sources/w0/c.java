package w0;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import androidx.core.view.c0;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {

    /* renamed from: x  reason: collision with root package name */
    private static final Interpolator f23392x = new a();

    /* renamed from: a  reason: collision with root package name */
    private int f23393a;

    /* renamed from: b  reason: collision with root package name */
    private int f23394b;

    /* renamed from: d  reason: collision with root package name */
    private float[] f23396d;

    /* renamed from: e  reason: collision with root package name */
    private float[] f23397e;

    /* renamed from: f  reason: collision with root package name */
    private float[] f23398f;

    /* renamed from: g  reason: collision with root package name */
    private float[] f23399g;

    /* renamed from: h  reason: collision with root package name */
    private int[] f23400h;

    /* renamed from: i  reason: collision with root package name */
    private int[] f23401i;

    /* renamed from: j  reason: collision with root package name */
    private int[] f23402j;

    /* renamed from: k  reason: collision with root package name */
    private int f23403k;

    /* renamed from: l  reason: collision with root package name */
    private VelocityTracker f23404l;

    /* renamed from: m  reason: collision with root package name */
    private float f23405m;

    /* renamed from: n  reason: collision with root package name */
    private float f23406n;

    /* renamed from: o  reason: collision with root package name */
    private int f23407o;

    /* renamed from: p  reason: collision with root package name */
    private final int f23408p;
    private int q;

    /* renamed from: r  reason: collision with root package name */
    private OverScroller f23409r;

    /* renamed from: s  reason: collision with root package name */
    private final AbstractC0221c f23410s;

    /* renamed from: t  reason: collision with root package name */
    private View f23411t;

    /* renamed from: u  reason: collision with root package name */
    private boolean f23412u;

    /* renamed from: v  reason: collision with root package name */
    private final ViewGroup f23413v;

    /* renamed from: c  reason: collision with root package name */
    private int f23395c = -1;

    /* renamed from: w  reason: collision with root package name */
    private final Runnable f23414w = new b();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Interpolator {
        a() {
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f5) {
            float f8 = f5 - 1.0f;
            return (f8 * f8 * f8 * f8 * f8) + 1.0f;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            c.this.L(0);
        }
    }

    /* renamed from: w0.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class AbstractC0221c {
        public abstract int a(View view, int i8, int i9);

        public abstract int b(View view, int i8, int i9);

        public int c(int i8) {
            return i8;
        }

        public int d(View view) {
            return 0;
        }

        public int e(View view) {
            return 0;
        }

        public void f(int i8, int i9) {
        }

        public boolean g(int i8) {
            return false;
        }

        public void h(int i8, int i9) {
        }

        public void i(View view, int i8) {
        }

        public abstract void j(int i8);

        public abstract void k(View view, int i8, int i9, int i10, int i11);

        public abstract void l(View view, float f5, float f8);

        public abstract boolean m(View view, int i8);
    }

    private c(Context context, ViewGroup viewGroup, AbstractC0221c abstractC0221c) {
        if (viewGroup == null) {
            throw new IllegalArgumentException("Parent view may not be null");
        }
        if (abstractC0221c == null) {
            throw new IllegalArgumentException("Callback may not be null");
        }
        this.f23413v = viewGroup;
        this.f23410s = abstractC0221c;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        int i8 = (int) ((context.getResources().getDisplayMetrics().density * 20.0f) + 0.5f);
        this.f23408p = i8;
        this.f23407o = i8;
        this.f23394b = viewConfiguration.getScaledTouchSlop();
        this.f23405m = viewConfiguration.getScaledMaximumFlingVelocity();
        this.f23406n = viewConfiguration.getScaledMinimumFlingVelocity();
        this.f23409r = new OverScroller(context, f23392x);
    }

    private boolean E(int i8) {
        if (D(i8)) {
            return true;
        }
        Log.e("ViewDragHelper", "Ignoring pointerId=" + i8 + " because ACTION_DOWN was not received for this pointer before ACTION_MOVE. It likely happened because  ViewDragHelper did not receive all the events in the event stream.");
        return false;
    }

    private void H() {
        this.f23404l.computeCurrentVelocity(1000, this.f23405m);
        q(h(this.f23404l.getXVelocity(this.f23395c), this.f23406n, this.f23405m), h(this.f23404l.getYVelocity(this.f23395c), this.f23406n, this.f23405m));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v14 */
    /* JADX WARN: Type inference failed for: r0v15 */
    /* JADX WARN: Type inference failed for: r0v4, types: [int] */
    /* JADX WARN: Type inference failed for: r3v3, types: [w0.c$c] */
    private void I(float f5, float f8, int i8) {
        boolean d8 = d(f5, f8, i8, 1);
        boolean z4 = d8;
        if (d(f8, f5, i8, 4)) {
            z4 = d8 | true;
        }
        boolean z8 = z4;
        if (d(f5, f8, i8, 2)) {
            z8 = (z4 ? 1 : 0) | true;
        }
        ?? r02 = z8;
        if (d(f8, f5, i8, 8)) {
            r02 = (z8 ? 1 : 0) | true;
        }
        if (r02 != 0) {
            int[] iArr = this.f23401i;
            iArr[i8] = iArr[i8] | r02;
            this.f23410s.f(r02, i8);
        }
    }

    private void J(float f5, float f8, int i8) {
        t(i8);
        float[] fArr = this.f23396d;
        this.f23398f[i8] = f5;
        fArr[i8] = f5;
        float[] fArr2 = this.f23397e;
        this.f23399g[i8] = f8;
        fArr2[i8] = f8;
        this.f23400h[i8] = z((int) f5, (int) f8);
        this.f23403k |= 1 << i8;
    }

    private void K(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        for (int i8 = 0; i8 < pointerCount; i8++) {
            int pointerId = motionEvent.getPointerId(i8);
            if (E(pointerId)) {
                float x8 = motionEvent.getX(i8);
                float y8 = motionEvent.getY(i8);
                this.f23398f[pointerId] = x8;
                this.f23399g[pointerId] = y8;
            }
        }
    }

    private boolean d(float f5, float f8, int i8, int i9) {
        float abs = Math.abs(f5);
        float abs2 = Math.abs(f8);
        if ((this.f23400h[i8] & i9) != i9 || (this.q & i9) == 0 || (this.f23402j[i8] & i9) == i9 || (this.f23401i[i8] & i9) == i9) {
            return false;
        }
        int i10 = this.f23394b;
        if (abs > i10 || abs2 > i10) {
            if (abs >= abs2 * 0.5f || !this.f23410s.g(i9)) {
                return (this.f23401i[i8] & i9) == 0 && abs > ((float) this.f23394b);
            }
            int[] iArr = this.f23402j;
            iArr[i8] = iArr[i8] | i9;
            return false;
        }
        return false;
    }

    private boolean g(View view, float f5, float f8) {
        if (view == null) {
            return false;
        }
        boolean z4 = this.f23410s.d(view) > 0;
        boolean z8 = this.f23410s.e(view) > 0;
        if (!z4 || !z8) {
            return z4 ? Math.abs(f5) > ((float) this.f23394b) : z8 && Math.abs(f8) > ((float) this.f23394b);
        }
        int i8 = this.f23394b;
        return (f5 * f5) + (f8 * f8) > ((float) (i8 * i8));
    }

    private float h(float f5, float f8, float f9) {
        float abs = Math.abs(f5);
        if (abs < f8) {
            return 0.0f;
        }
        return abs > f9 ? f5 > 0.0f ? f9 : -f9 : f5;
    }

    private int i(int i8, int i9, int i10) {
        int abs = Math.abs(i8);
        if (abs < i9) {
            return 0;
        }
        return abs > i10 ? i8 > 0 ? i10 : -i10 : i8;
    }

    private void j() {
        float[] fArr = this.f23396d;
        if (fArr == null) {
            return;
        }
        Arrays.fill(fArr, 0.0f);
        Arrays.fill(this.f23397e, 0.0f);
        Arrays.fill(this.f23398f, 0.0f);
        Arrays.fill(this.f23399g, 0.0f);
        Arrays.fill(this.f23400h, 0);
        Arrays.fill(this.f23401i, 0);
        Arrays.fill(this.f23402j, 0);
        this.f23403k = 0;
    }

    private void k(int i8) {
        if (this.f23396d == null || !D(i8)) {
            return;
        }
        this.f23396d[i8] = 0.0f;
        this.f23397e[i8] = 0.0f;
        this.f23398f[i8] = 0.0f;
        this.f23399g[i8] = 0.0f;
        this.f23400h[i8] = 0;
        this.f23401i[i8] = 0;
        this.f23402j[i8] = 0;
        this.f23403k = (~(1 << i8)) & this.f23403k;
    }

    private int l(int i8, int i9, int i10) {
        if (i8 == 0) {
            return 0;
        }
        int width = this.f23413v.getWidth();
        float f5 = width / 2;
        float r4 = f5 + (r(Math.min(1.0f, Math.abs(i8) / width)) * f5);
        int abs = Math.abs(i9);
        return Math.min(abs > 0 ? Math.round(Math.abs(r4 / abs) * 1000.0f) * 4 : (int) (((Math.abs(i8) / i10) + 1.0f) * 256.0f), 600);
    }

    private int m(View view, int i8, int i9, int i10, int i11) {
        float f5;
        float f8;
        float f9;
        float f10;
        int i12 = i(i10, (int) this.f23406n, (int) this.f23405m);
        int i13 = i(i11, (int) this.f23406n, (int) this.f23405m);
        int abs = Math.abs(i8);
        int abs2 = Math.abs(i9);
        int abs3 = Math.abs(i12);
        int abs4 = Math.abs(i13);
        int i14 = abs3 + abs4;
        int i15 = abs + abs2;
        if (i12 != 0) {
            f5 = abs3;
            f8 = i14;
        } else {
            f5 = abs;
            f8 = i15;
        }
        float f11 = f5 / f8;
        if (i13 != 0) {
            f9 = abs4;
            f10 = i14;
        } else {
            f9 = abs2;
            f10 = i15;
        }
        return (int) ((l(i8, i12, this.f23410s.d(view)) * f11) + (l(i9, i13, this.f23410s.e(view)) * (f9 / f10)));
    }

    public static c o(ViewGroup viewGroup, float f5, AbstractC0221c abstractC0221c) {
        c p8 = p(viewGroup, abstractC0221c);
        p8.f23394b = (int) (p8.f23394b * (1.0f / f5));
        return p8;
    }

    public static c p(ViewGroup viewGroup, AbstractC0221c abstractC0221c) {
        return new c(viewGroup.getContext(), viewGroup, abstractC0221c);
    }

    private void q(float f5, float f8) {
        this.f23412u = true;
        this.f23410s.l(this.f23411t, f5, f8);
        this.f23412u = false;
        if (this.f23393a == 1) {
            L(0);
        }
    }

    private float r(float f5) {
        return (float) Math.sin((f5 - 0.5f) * 0.47123894f);
    }

    private void s(int i8, int i9, int i10, int i11) {
        int left = this.f23411t.getLeft();
        int top = this.f23411t.getTop();
        if (i10 != 0) {
            i8 = this.f23410s.a(this.f23411t, i8, i10);
            c0.c0(this.f23411t, i8 - left);
        }
        int i12 = i8;
        if (i11 != 0) {
            i9 = this.f23410s.b(this.f23411t, i9, i11);
            c0.d0(this.f23411t, i9 - top);
        }
        int i13 = i9;
        if (i10 == 0 && i11 == 0) {
            return;
        }
        this.f23410s.k(this.f23411t, i12, i13, i12 - left, i13 - top);
    }

    private void t(int i8) {
        float[] fArr = this.f23396d;
        if (fArr == null || fArr.length <= i8) {
            int i9 = i8 + 1;
            float[] fArr2 = new float[i9];
            float[] fArr3 = new float[i9];
            float[] fArr4 = new float[i9];
            float[] fArr5 = new float[i9];
            int[] iArr = new int[i9];
            int[] iArr2 = new int[i9];
            int[] iArr3 = new int[i9];
            if (fArr != null) {
                System.arraycopy(fArr, 0, fArr2, 0, fArr.length);
                float[] fArr6 = this.f23397e;
                System.arraycopy(fArr6, 0, fArr3, 0, fArr6.length);
                float[] fArr7 = this.f23398f;
                System.arraycopy(fArr7, 0, fArr4, 0, fArr7.length);
                float[] fArr8 = this.f23399g;
                System.arraycopy(fArr8, 0, fArr5, 0, fArr8.length);
                int[] iArr4 = this.f23400h;
                System.arraycopy(iArr4, 0, iArr, 0, iArr4.length);
                int[] iArr5 = this.f23401i;
                System.arraycopy(iArr5, 0, iArr2, 0, iArr5.length);
                int[] iArr6 = this.f23402j;
                System.arraycopy(iArr6, 0, iArr3, 0, iArr6.length);
            }
            this.f23396d = fArr2;
            this.f23397e = fArr3;
            this.f23398f = fArr4;
            this.f23399g = fArr5;
            this.f23400h = iArr;
            this.f23401i = iArr2;
            this.f23402j = iArr3;
        }
    }

    private boolean v(int i8, int i9, int i10, int i11) {
        int left = this.f23411t.getLeft();
        int top = this.f23411t.getTop();
        int i12 = i8 - left;
        int i13 = i9 - top;
        if (i12 == 0 && i13 == 0) {
            this.f23409r.abortAnimation();
            L(0);
            return false;
        }
        this.f23409r.startScroll(left, top, i12, i13, m(this.f23411t, i12, i13, i10, i11));
        L(2);
        return true;
    }

    private int z(int i8, int i9) {
        int i10 = i8 < this.f23413v.getLeft() + this.f23407o ? 1 : 0;
        if (i9 < this.f23413v.getTop() + this.f23407o) {
            i10 |= 4;
        }
        if (i8 > this.f23413v.getRight() - this.f23407o) {
            i10 |= 2;
        }
        return i9 > this.f23413v.getBottom() - this.f23407o ? i10 | 8 : i10;
    }

    public int A() {
        return this.f23394b;
    }

    public int B() {
        return this.f23393a;
    }

    public boolean C(int i8, int i9) {
        return F(this.f23411t, i8, i9);
    }

    public boolean D(int i8) {
        return ((1 << i8) & this.f23403k) != 0;
    }

    public boolean F(View view, int i8, int i9) {
        return view != null && i8 >= view.getLeft() && i8 < view.getRight() && i9 >= view.getTop() && i9 < view.getBottom();
    }

    public void G(MotionEvent motionEvent) {
        int i8;
        int actionMasked = motionEvent.getActionMasked();
        int actionIndex = motionEvent.getActionIndex();
        if (actionMasked == 0) {
            b();
        }
        if (this.f23404l == null) {
            this.f23404l = VelocityTracker.obtain();
        }
        this.f23404l.addMovement(motionEvent);
        int i9 = 0;
        if (actionMasked == 0) {
            float x8 = motionEvent.getX();
            float y8 = motionEvent.getY();
            int pointerId = motionEvent.getPointerId(0);
            View u8 = u((int) x8, (int) y8);
            J(x8, y8, pointerId);
            S(u8, pointerId);
            int i10 = this.f23400h[pointerId];
            int i11 = this.q;
            if ((i10 & i11) != 0) {
                this.f23410s.h(i10 & i11, pointerId);
                return;
            }
            return;
        }
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                if (this.f23393a != 1) {
                    int pointerCount = motionEvent.getPointerCount();
                    while (i9 < pointerCount) {
                        int pointerId2 = motionEvent.getPointerId(i9);
                        if (E(pointerId2)) {
                            float x9 = motionEvent.getX(i9);
                            float y9 = motionEvent.getY(i9);
                            float f5 = x9 - this.f23396d[pointerId2];
                            float f8 = y9 - this.f23397e[pointerId2];
                            I(f5, f8, pointerId2);
                            if (this.f23393a != 1) {
                                View u9 = u((int) x9, (int) y9);
                                if (g(u9, f5, f8) && S(u9, pointerId2)) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        i9++;
                    }
                } else if (!E(this.f23395c)) {
                    return;
                } else {
                    int findPointerIndex = motionEvent.findPointerIndex(this.f23395c);
                    float x10 = motionEvent.getX(findPointerIndex);
                    float y10 = motionEvent.getY(findPointerIndex);
                    float[] fArr = this.f23398f;
                    int i12 = this.f23395c;
                    int i13 = (int) (x10 - fArr[i12]);
                    int i14 = (int) (y10 - this.f23399g[i12]);
                    s(this.f23411t.getLeft() + i13, this.f23411t.getTop() + i14, i13, i14);
                }
                K(motionEvent);
                return;
            } else if (actionMasked != 3) {
                if (actionMasked == 5) {
                    int pointerId3 = motionEvent.getPointerId(actionIndex);
                    float x11 = motionEvent.getX(actionIndex);
                    float y11 = motionEvent.getY(actionIndex);
                    J(x11, y11, pointerId3);
                    if (this.f23393a != 0) {
                        if (C((int) x11, (int) y11)) {
                            S(this.f23411t, pointerId3);
                            return;
                        }
                        return;
                    }
                    S(u((int) x11, (int) y11), pointerId3);
                    int i15 = this.f23400h[pointerId3];
                    int i16 = this.q;
                    if ((i15 & i16) != 0) {
                        this.f23410s.h(i15 & i16, pointerId3);
                        return;
                    }
                    return;
                } else if (actionMasked != 6) {
                    return;
                } else {
                    int pointerId4 = motionEvent.getPointerId(actionIndex);
                    if (this.f23393a == 1 && pointerId4 == this.f23395c) {
                        int pointerCount2 = motionEvent.getPointerCount();
                        while (true) {
                            if (i9 >= pointerCount2) {
                                i8 = -1;
                                break;
                            }
                            int pointerId5 = motionEvent.getPointerId(i9);
                            if (pointerId5 != this.f23395c) {
                                View u10 = u((int) motionEvent.getX(i9), (int) motionEvent.getY(i9));
                                View view = this.f23411t;
                                if (u10 == view && S(view, pointerId5)) {
                                    i8 = this.f23395c;
                                    break;
                                }
                            }
                            i9++;
                        }
                        if (i8 == -1) {
                            H();
                        }
                    }
                    k(pointerId4);
                    return;
                }
            } else if (this.f23393a == 1) {
                q(0.0f, 0.0f);
            }
        } else if (this.f23393a == 1) {
            H();
        }
        b();
    }

    void L(int i8) {
        this.f23413v.removeCallbacks(this.f23414w);
        if (this.f23393a != i8) {
            this.f23393a = i8;
            this.f23410s.j(i8);
            if (this.f23393a == 0) {
                this.f23411t = null;
            }
        }
    }

    public void M(int i8) {
        this.f23407o = i8;
    }

    public void N(int i8) {
        this.q = i8;
    }

    public void O(float f5) {
        this.f23406n = f5;
    }

    public boolean P(int i8, int i9) {
        if (this.f23412u) {
            return v(i8, i9, (int) this.f23404l.getXVelocity(this.f23395c), (int) this.f23404l.getYVelocity(this.f23395c));
        }
        throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
    }

    /* JADX WARN: Code restructure failed: missing block: B:49:0x00dd, code lost:
        if (r12 != r11) goto L58;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean Q(android.view.MotionEvent r17) {
        /*
            Method dump skipped, instructions count: 315
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: w0.c.Q(android.view.MotionEvent):boolean");
    }

    public boolean R(View view, int i8, int i9) {
        this.f23411t = view;
        this.f23395c = -1;
        boolean v8 = v(i8, i9, 0, 0);
        if (!v8 && this.f23393a == 0 && this.f23411t != null) {
            this.f23411t = null;
        }
        return v8;
    }

    boolean S(View view, int i8) {
        if (view == this.f23411t && this.f23395c == i8) {
            return true;
        }
        if (view == null || !this.f23410s.m(view, i8)) {
            return false;
        }
        this.f23395c = i8;
        c(view, i8);
        return true;
    }

    public void a() {
        b();
        if (this.f23393a == 2) {
            int currX = this.f23409r.getCurrX();
            int currY = this.f23409r.getCurrY();
            this.f23409r.abortAnimation();
            int currX2 = this.f23409r.getCurrX();
            int currY2 = this.f23409r.getCurrY();
            this.f23410s.k(this.f23411t, currX2, currY2, currX2 - currX, currY2 - currY);
        }
        L(0);
    }

    public void b() {
        this.f23395c = -1;
        j();
        VelocityTracker velocityTracker = this.f23404l;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.f23404l = null;
        }
    }

    public void c(View view, int i8) {
        if (view.getParent() == this.f23413v) {
            this.f23411t = view;
            this.f23395c = i8;
            this.f23410s.i(view, i8);
            L(1);
            return;
        }
        throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (" + this.f23413v + ")");
    }

    public boolean e(int i8) {
        int length = this.f23396d.length;
        for (int i9 = 0; i9 < length; i9++) {
            if (f(i8, i9)) {
                return true;
            }
        }
        return false;
    }

    public boolean f(int i8, int i9) {
        if (D(i9)) {
            boolean z4 = (i8 & 1) == 1;
            boolean z8 = (i8 & 2) == 2;
            float f5 = this.f23398f[i9] - this.f23396d[i9];
            float f8 = this.f23399g[i9] - this.f23397e[i9];
            if (!z4 || !z8) {
                return z4 ? Math.abs(f5) > ((float) this.f23394b) : z8 && Math.abs(f8) > ((float) this.f23394b);
            }
            int i10 = this.f23394b;
            return (f5 * f5) + (f8 * f8) > ((float) (i10 * i10));
        }
        return false;
    }

    public boolean n(boolean z4) {
        if (this.f23393a == 2) {
            boolean computeScrollOffset = this.f23409r.computeScrollOffset();
            int currX = this.f23409r.getCurrX();
            int currY = this.f23409r.getCurrY();
            int left = currX - this.f23411t.getLeft();
            int top = currY - this.f23411t.getTop();
            if (left != 0) {
                c0.c0(this.f23411t, left);
            }
            if (top != 0) {
                c0.d0(this.f23411t, top);
            }
            if (left != 0 || top != 0) {
                this.f23410s.k(this.f23411t, currX, currY, left, top);
            }
            if (computeScrollOffset && currX == this.f23409r.getFinalX() && currY == this.f23409r.getFinalY()) {
                this.f23409r.abortAnimation();
                computeScrollOffset = false;
            }
            if (!computeScrollOffset) {
                if (z4) {
                    this.f23413v.post(this.f23414w);
                } else {
                    L(0);
                }
            }
        }
        return this.f23393a == 2;
    }

    public View u(int i8, int i9) {
        for (int childCount = this.f23413v.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = this.f23413v.getChildAt(this.f23410s.c(childCount));
            if (i8 >= childAt.getLeft() && i8 < childAt.getRight() && i9 >= childAt.getTop() && i9 < childAt.getBottom()) {
                return childAt;
            }
        }
        return null;
    }

    public View w() {
        return this.f23411t;
    }

    public int x() {
        return this.f23408p;
    }

    public int y() {
        return this.f23407o;
    }
}
