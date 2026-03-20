package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.transition.Transition;
import androidx.transition.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class Visibility extends Transition {
    private static final String[] Z = {"android:visibility:visibility", "android:visibility:parent"};
    private int Y;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends r {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ViewGroup f7496a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ View f7497b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ View f7498c;

        a(ViewGroup viewGroup, View view, View view2) {
            this.f7496a = viewGroup;
            this.f7497b = view;
            this.f7498c = view2;
        }

        @Override // androidx.transition.r, androidx.transition.Transition.f
        public void b(Transition transition) {
            a0.b(this.f7496a).d(this.f7497b);
        }

        @Override // androidx.transition.Transition.f
        public void c(Transition transition) {
            this.f7498c.setTag(x1.b.f23764e, null);
            a0.b(this.f7496a).d(this.f7497b);
            transition.a0(this);
        }

        @Override // androidx.transition.r, androidx.transition.Transition.f
        public void e(Transition transition) {
            if (this.f7497b.getParent() == null) {
                a0.b(this.f7496a).c(this.f7497b);
            } else {
                Visibility.this.cancel();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends AnimatorListenerAdapter implements Transition.f, a.InterfaceC0082a {

        /* renamed from: a  reason: collision with root package name */
        private final View f7500a;

        /* renamed from: b  reason: collision with root package name */
        private final int f7501b;

        /* renamed from: c  reason: collision with root package name */
        private final ViewGroup f7502c;

        /* renamed from: d  reason: collision with root package name */
        private final boolean f7503d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f7504e;

        /* renamed from: f  reason: collision with root package name */
        boolean f7505f = false;

        b(View view, int i8, boolean z4) {
            this.f7500a = view;
            this.f7501b = i8;
            this.f7502c = (ViewGroup) view.getParent();
            this.f7503d = z4;
            g(true);
        }

        private void f() {
            if (!this.f7505f) {
                f0.i(this.f7500a, this.f7501b);
                ViewGroup viewGroup = this.f7502c;
                if (viewGroup != null) {
                    viewGroup.invalidate();
                }
            }
            g(false);
        }

        private void g(boolean z4) {
            ViewGroup viewGroup;
            if (!this.f7503d || this.f7504e == z4 || (viewGroup = this.f7502c) == null) {
                return;
            }
            this.f7504e = z4;
            a0.d(viewGroup, z4);
        }

        @Override // androidx.transition.Transition.f
        public void a(Transition transition) {
        }

        @Override // androidx.transition.Transition.f
        public void b(Transition transition) {
            g(false);
        }

        @Override // androidx.transition.Transition.f
        public void c(Transition transition) {
            f();
            transition.a0(this);
        }

        @Override // androidx.transition.Transition.f
        public void d(Transition transition) {
        }

        @Override // androidx.transition.Transition.f
        public void e(Transition transition) {
            g(true);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f7505f = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            f();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener, androidx.transition.a.InterfaceC0082a
        public void onAnimationPause(Animator animator) {
            if (this.f7505f) {
                return;
            }
            f0.i(this.f7500a, this.f7501b);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener, androidx.transition.a.InterfaceC0082a
        public void onAnimationResume(Animator animator) {
            if (this.f7505f) {
                return;
            }
            f0.i(this.f7500a, 0);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: a  reason: collision with root package name */
        boolean f7506a;

        /* renamed from: b  reason: collision with root package name */
        boolean f7507b;

        /* renamed from: c  reason: collision with root package name */
        int f7508c;

        /* renamed from: d  reason: collision with root package name */
        int f7509d;

        /* renamed from: e  reason: collision with root package name */
        ViewGroup f7510e;

        /* renamed from: f  reason: collision with root package name */
        ViewGroup f7511f;

        c() {
        }
    }

    public Visibility() {
        this.Y = 3;
    }

    @SuppressLint({"RestrictedApi"})
    public Visibility(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.Y = 3;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, q.f7599e);
        int g8 = androidx.core.content.res.k.g(obtainStyledAttributes, (XmlResourceParser) attributeSet, "transitionVisibilityMode", 0, 0);
        obtainStyledAttributes.recycle();
        if (g8 != 0) {
            v0(g8);
        }
    }

    private void o0(u uVar) {
        uVar.f7619a.put("android:visibility:visibility", Integer.valueOf(uVar.f7620b.getVisibility()));
        uVar.f7619a.put("android:visibility:parent", uVar.f7620b.getParent());
        int[] iArr = new int[2];
        uVar.f7620b.getLocationOnScreen(iArr);
        uVar.f7619a.put("android:visibility:screenLocation", iArr);
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0075, code lost:
        if (r9 == 0) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x007f, code lost:
        if (r0.f7510e == null) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0091, code lost:
        if (r0.f7508c == 0) goto L25;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private androidx.transition.Visibility.c q0(androidx.transition.u r8, androidx.transition.u r9) {
        /*
            r7 = this;
            androidx.transition.Visibility$c r0 = new androidx.transition.Visibility$c
            r0.<init>()
            r1 = 0
            r0.f7506a = r1
            r0.f7507b = r1
            java.lang.String r2 = "android:visibility:parent"
            r3 = 0
            r4 = -1
            java.lang.String r5 = "android:visibility:visibility"
            if (r8 == 0) goto L33
            java.util.Map<java.lang.String, java.lang.Object> r6 = r8.f7619a
            boolean r6 = r6.containsKey(r5)
            if (r6 == 0) goto L33
            java.util.Map<java.lang.String, java.lang.Object> r6 = r8.f7619a
            java.lang.Object r6 = r6.get(r5)
            java.lang.Integer r6 = (java.lang.Integer) r6
            int r6 = r6.intValue()
            r0.f7508c = r6
            java.util.Map<java.lang.String, java.lang.Object> r6 = r8.f7619a
            java.lang.Object r6 = r6.get(r2)
            android.view.ViewGroup r6 = (android.view.ViewGroup) r6
            r0.f7510e = r6
            goto L37
        L33:
            r0.f7508c = r4
            r0.f7510e = r3
        L37:
            if (r9 == 0) goto L5a
            java.util.Map<java.lang.String, java.lang.Object> r6 = r9.f7619a
            boolean r6 = r6.containsKey(r5)
            if (r6 == 0) goto L5a
            java.util.Map<java.lang.String, java.lang.Object> r3 = r9.f7619a
            java.lang.Object r3 = r3.get(r5)
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r3 = r3.intValue()
            r0.f7509d = r3
            java.util.Map<java.lang.String, java.lang.Object> r3 = r9.f7619a
            java.lang.Object r2 = r3.get(r2)
            android.view.ViewGroup r2 = (android.view.ViewGroup) r2
            r0.f7511f = r2
            goto L5e
        L5a:
            r0.f7509d = r4
            r0.f7511f = r3
        L5e:
            r2 = 1
            if (r8 == 0) goto L82
            if (r9 == 0) goto L82
            int r8 = r0.f7508c
            int r9 = r0.f7509d
            if (r8 != r9) goto L70
            android.view.ViewGroup r3 = r0.f7510e
            android.view.ViewGroup r4 = r0.f7511f
            if (r3 != r4) goto L70
            return r0
        L70:
            if (r8 == r9) goto L78
            if (r8 != 0) goto L75
            goto L93
        L75:
            if (r9 != 0) goto L96
            goto L88
        L78:
            android.view.ViewGroup r8 = r0.f7511f
            if (r8 != 0) goto L7d
            goto L93
        L7d:
            android.view.ViewGroup r8 = r0.f7510e
            if (r8 != 0) goto L96
            goto L88
        L82:
            if (r8 != 0) goto L8d
            int r8 = r0.f7509d
            if (r8 != 0) goto L8d
        L88:
            r0.f7507b = r2
        L8a:
            r0.f7506a = r2
            goto L96
        L8d:
            if (r9 != 0) goto L96
            int r8 = r0.f7508c
            if (r8 != 0) goto L96
        L93:
            r0.f7507b = r1
            goto L8a
        L96:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.transition.Visibility.q0(androidx.transition.u, androidx.transition.u):androidx.transition.Visibility$c");
    }

    @Override // androidx.transition.Transition
    public String[] L() {
        return Z;
    }

    @Override // androidx.transition.Transition
    public boolean N(u uVar, u uVar2) {
        if (uVar == null && uVar2 == null) {
            return false;
        }
        if (uVar == null || uVar2 == null || uVar2.f7619a.containsKey("android:visibility:visibility") == uVar.f7619a.containsKey("android:visibility:visibility")) {
            c q02 = q0(uVar, uVar2);
            if (q02.f7506a) {
                return q02.f7508c == 0 || q02.f7509d == 0;
            }
            return false;
        }
        return false;
    }

    @Override // androidx.transition.Transition
    public void j(u uVar) {
        o0(uVar);
    }

    @Override // androidx.transition.Transition
    public void m(u uVar) {
        o0(uVar);
    }

    public int p0() {
        return this.Y;
    }

    public Animator r0(ViewGroup viewGroup, View view, u uVar, u uVar2) {
        return null;
    }

    @Override // androidx.transition.Transition
    public Animator s(ViewGroup viewGroup, u uVar, u uVar2) {
        c q02 = q0(uVar, uVar2);
        if (q02.f7506a) {
            if (q02.f7510e == null && q02.f7511f == null) {
                return null;
            }
            return q02.f7507b ? s0(viewGroup, uVar, q02.f7508c, uVar2, q02.f7509d) : u0(viewGroup, uVar, q02.f7508c, uVar2, q02.f7509d);
        }
        return null;
    }

    public Animator s0(ViewGroup viewGroup, u uVar, int i8, u uVar2, int i9) {
        if ((this.Y & 1) != 1 || uVar2 == null) {
            return null;
        }
        if (uVar == null) {
            View view = (View) uVar2.f7620b.getParent();
            if (q0(B(view, false), M(view, false)).f7506a) {
                return null;
            }
        }
        return r0(viewGroup, uVar2.f7620b, uVar, uVar2);
    }

    public Animator t0(ViewGroup viewGroup, View view, u uVar, u uVar2) {
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x0089, code lost:
        if (r17.C != false) goto L52;
     */
    /* JADX WARN: Removed duplicated region for block: B:27:0x004a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.animation.Animator u0(android.view.ViewGroup r18, androidx.transition.u r19, int r20, androidx.transition.u r21, int r22) {
        /*
            Method dump skipped, instructions count: 264
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.transition.Visibility.u0(android.view.ViewGroup, androidx.transition.u, int, androidx.transition.u, int):android.animation.Animator");
    }

    public void v0(int i8) {
        if ((i8 & (-4)) != 0) {
            throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
        }
        this.Y = i8;
    }
}
