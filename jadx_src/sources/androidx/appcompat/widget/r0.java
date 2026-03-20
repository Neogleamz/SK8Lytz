package androidx.appcompat.widget;

import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityManager;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class r0 implements View.OnLongClickListener, View.OnHoverListener, View.OnAttachStateChangeListener {

    /* renamed from: l  reason: collision with root package name */
    private static r0 f1585l;

    /* renamed from: m  reason: collision with root package name */
    private static r0 f1586m;

    /* renamed from: a  reason: collision with root package name */
    private final View f1587a;

    /* renamed from: b  reason: collision with root package name */
    private final CharSequence f1588b;

    /* renamed from: c  reason: collision with root package name */
    private final int f1589c;

    /* renamed from: d  reason: collision with root package name */
    private final Runnable f1590d = new Runnable() { // from class: androidx.appcompat.widget.q0
        @Override // java.lang.Runnable
        public final void run() {
            r0.this.e();
        }
    };

    /* renamed from: e  reason: collision with root package name */
    private final Runnable f1591e = new Runnable() { // from class: androidx.appcompat.widget.p0
        @Override // java.lang.Runnable
        public final void run() {
            r0.this.d();
        }
    };

    /* renamed from: f  reason: collision with root package name */
    private int f1592f;

    /* renamed from: g  reason: collision with root package name */
    private int f1593g;

    /* renamed from: h  reason: collision with root package name */
    private s0 f1594h;

    /* renamed from: j  reason: collision with root package name */
    private boolean f1595j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f1596k;

    private r0(View view, CharSequence charSequence) {
        this.f1587a = view;
        this.f1588b = charSequence;
        this.f1589c = androidx.core.view.e0.c(ViewConfiguration.get(view.getContext()));
        c();
        view.setOnLongClickListener(this);
        view.setOnHoverListener(this);
    }

    private void b() {
        this.f1587a.removeCallbacks(this.f1590d);
    }

    private void c() {
        this.f1596k = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void e() {
        i(false);
    }

    private void f() {
        this.f1587a.postDelayed(this.f1590d, ViewConfiguration.getLongPressTimeout());
    }

    private static void g(r0 r0Var) {
        r0 r0Var2 = f1585l;
        if (r0Var2 != null) {
            r0Var2.b();
        }
        f1585l = r0Var;
        if (r0Var != null) {
            r0Var.f();
        }
    }

    public static void h(View view, CharSequence charSequence) {
        r0 r0Var = f1585l;
        if (r0Var != null && r0Var.f1587a == view) {
            g(null);
        }
        if (!TextUtils.isEmpty(charSequence)) {
            new r0(view, charSequence);
            return;
        }
        r0 r0Var2 = f1586m;
        if (r0Var2 != null && r0Var2.f1587a == view) {
            r0Var2.d();
        }
        view.setOnLongClickListener(null);
        view.setLongClickable(false);
        view.setOnHoverListener(null);
    }

    private boolean j(MotionEvent motionEvent) {
        int x8 = (int) motionEvent.getX();
        int y8 = (int) motionEvent.getY();
        if (this.f1596k || Math.abs(x8 - this.f1592f) > this.f1589c || Math.abs(y8 - this.f1593g) > this.f1589c) {
            this.f1592f = x8;
            this.f1593g = y8;
            this.f1596k = false;
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d() {
        if (f1586m == this) {
            f1586m = null;
            s0 s0Var = this.f1594h;
            if (s0Var != null) {
                s0Var.c();
                this.f1594h = null;
                c();
                this.f1587a.removeOnAttachStateChangeListener(this);
            } else {
                Log.e("TooltipCompatHandler", "sActiveHandler.mPopup == null");
            }
        }
        if (f1585l == this) {
            g(null);
        }
        this.f1587a.removeCallbacks(this.f1591e);
    }

    void i(boolean z4) {
        long longPressTimeout;
        if (androidx.core.view.c0.V(this.f1587a)) {
            g(null);
            r0 r0Var = f1586m;
            if (r0Var != null) {
                r0Var.d();
            }
            f1586m = this;
            this.f1595j = z4;
            s0 s0Var = new s0(this.f1587a.getContext());
            this.f1594h = s0Var;
            s0Var.e(this.f1587a, this.f1592f, this.f1593g, this.f1595j, this.f1588b);
            this.f1587a.addOnAttachStateChangeListener(this);
            if (this.f1595j) {
                longPressTimeout = 2500;
            } else {
                longPressTimeout = ((androidx.core.view.c0.P(this.f1587a) & 1) == 1 ? 3000L : 15000L) - ViewConfiguration.getLongPressTimeout();
            }
            this.f1587a.removeCallbacks(this.f1591e);
            this.f1587a.postDelayed(this.f1591e, longPressTimeout);
        }
    }

    @Override // android.view.View.OnHoverListener
    public boolean onHover(View view, MotionEvent motionEvent) {
        if (this.f1594h == null || !this.f1595j) {
            AccessibilityManager accessibilityManager = (AccessibilityManager) this.f1587a.getContext().getSystemService("accessibility");
            if (accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled()) {
                return false;
            }
            int action = motionEvent.getAction();
            if (action != 7) {
                if (action == 10) {
                    c();
                    d();
                }
            } else if (this.f1587a.isEnabled() && this.f1594h == null && j(motionEvent)) {
                g(this);
            }
            return false;
        }
        return false;
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        this.f1592f = view.getWidth() / 2;
        this.f1593g = view.getHeight() / 2;
        i(true);
        return true;
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View view) {
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View view) {
        d();
    }
}
