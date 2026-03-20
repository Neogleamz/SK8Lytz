package androidx.core.view;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m0 {

    /* renamed from: b  reason: collision with root package name */
    public static final m0 f5037b;

    /* renamed from: a  reason: collision with root package name */
    private final l f5038a;

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"SoonBlockedPrivateApi"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        private static Field f5039a;

        /* renamed from: b  reason: collision with root package name */
        private static Field f5040b;

        /* renamed from: c  reason: collision with root package name */
        private static Field f5041c;

        /* renamed from: d  reason: collision with root package name */
        private static boolean f5042d;

        static {
            try {
                Field declaredField = View.class.getDeclaredField("mAttachInfo");
                f5039a = declaredField;
                declaredField.setAccessible(true);
                Class<?> cls = Class.forName("android.view.View$AttachInfo");
                Field declaredField2 = cls.getDeclaredField("mStableInsets");
                f5040b = declaredField2;
                declaredField2.setAccessible(true);
                Field declaredField3 = cls.getDeclaredField("mContentInsets");
                f5041c = declaredField3;
                declaredField3.setAccessible(true);
                f5042d = true;
            } catch (ReflectiveOperationException e8) {
                Log.w("WindowInsetsCompat", "Failed to get visible insets from AttachInfo " + e8.getMessage(), e8);
            }
        }

        public static m0 a(View view) {
            if (f5042d && view.isAttachedToWindow()) {
                try {
                    Object obj = f5039a.get(view.getRootView());
                    if (obj != null) {
                        Rect rect = (Rect) f5040b.get(obj);
                        Rect rect2 = (Rect) f5041c.get(obj);
                        if (rect != null && rect2 != null) {
                            m0 a9 = new b().b(androidx.core.graphics.c.c(rect)).c(androidx.core.graphics.c.c(rect2)).a();
                            a9.v(a9);
                            a9.d(view.getRootView());
                            return a9;
                        }
                    }
                } catch (IllegalAccessException e8) {
                    Log.w("WindowInsetsCompat", "Failed to get insets from AttachInfo. " + e8.getMessage(), e8);
                }
            }
            return null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private final f f5043a;

        public b() {
            int i8 = Build.VERSION.SDK_INT;
            this.f5043a = i8 >= 30 ? new e() : i8 >= 29 ? new d() : i8 >= 20 ? new c() : new f();
        }

        public b(m0 m0Var) {
            int i8 = Build.VERSION.SDK_INT;
            this.f5043a = i8 >= 30 ? new e(m0Var) : i8 >= 29 ? new d(m0Var) : i8 >= 20 ? new c(m0Var) : new f(m0Var);
        }

        public m0 a() {
            return this.f5043a.b();
        }

        @Deprecated
        public b b(androidx.core.graphics.c cVar) {
            this.f5043a.d(cVar);
            return this;
        }

        @Deprecated
        public b c(androidx.core.graphics.c cVar) {
            this.f5043a.f(cVar);
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c extends f {

        /* renamed from: e  reason: collision with root package name */
        private static Field f5044e = null;

        /* renamed from: f  reason: collision with root package name */
        private static boolean f5045f = false;

        /* renamed from: g  reason: collision with root package name */
        private static Constructor<WindowInsets> f5046g = null;

        /* renamed from: h  reason: collision with root package name */
        private static boolean f5047h = false;

        /* renamed from: c  reason: collision with root package name */
        private WindowInsets f5048c;

        /* renamed from: d  reason: collision with root package name */
        private androidx.core.graphics.c f5049d;

        c() {
            this.f5048c = h();
        }

        c(m0 m0Var) {
            super(m0Var);
            this.f5048c = m0Var.x();
        }

        private static WindowInsets h() {
            if (!f5045f) {
                try {
                    f5044e = WindowInsets.class.getDeclaredField("CONSUMED");
                } catch (ReflectiveOperationException e8) {
                    Log.i("WindowInsetsCompat", "Could not retrieve WindowInsets.CONSUMED field", e8);
                }
                f5045f = true;
            }
            Field field = f5044e;
            if (field != null) {
                try {
                    WindowInsets windowInsets = (WindowInsets) field.get(null);
                    if (windowInsets != null) {
                        return new WindowInsets(windowInsets);
                    }
                } catch (ReflectiveOperationException e9) {
                    Log.i("WindowInsetsCompat", "Could not get value from WindowInsets.CONSUMED field", e9);
                }
            }
            if (!f5047h) {
                try {
                    f5046g = WindowInsets.class.getConstructor(Rect.class);
                } catch (ReflectiveOperationException e10) {
                    Log.i("WindowInsetsCompat", "Could not retrieve WindowInsets(Rect) constructor", e10);
                }
                f5047h = true;
            }
            Constructor<WindowInsets> constructor = f5046g;
            if (constructor != null) {
                try {
                    return constructor.newInstance(new Rect());
                } catch (ReflectiveOperationException e11) {
                    Log.i("WindowInsetsCompat", "Could not invoke WindowInsets(Rect) constructor", e11);
                }
            }
            return null;
        }

        @Override // androidx.core.view.m0.f
        m0 b() {
            a();
            m0 y8 = m0.y(this.f5048c);
            y8.t(this.f5052b);
            y8.w(this.f5049d);
            return y8;
        }

        @Override // androidx.core.view.m0.f
        void d(androidx.core.graphics.c cVar) {
            this.f5049d = cVar;
        }

        @Override // androidx.core.view.m0.f
        void f(androidx.core.graphics.c cVar) {
            WindowInsets windowInsets = this.f5048c;
            if (windowInsets != null) {
                this.f5048c = windowInsets.replaceSystemWindowInsets(cVar.f4708a, cVar.f4709b, cVar.f4710c, cVar.f4711d);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class d extends f {

        /* renamed from: c  reason: collision with root package name */
        final WindowInsets.Builder f5050c;

        d() {
            this.f5050c = new WindowInsets.Builder();
        }

        d(m0 m0Var) {
            super(m0Var);
            WindowInsets x8 = m0Var.x();
            this.f5050c = x8 != null ? new WindowInsets.Builder(x8) : new WindowInsets.Builder();
        }

        @Override // androidx.core.view.m0.f
        m0 b() {
            a();
            m0 y8 = m0.y(this.f5050c.build());
            y8.t(this.f5052b);
            return y8;
        }

        @Override // androidx.core.view.m0.f
        void c(androidx.core.graphics.c cVar) {
            this.f5050c.setMandatorySystemGestureInsets(cVar.e());
        }

        @Override // androidx.core.view.m0.f
        void d(androidx.core.graphics.c cVar) {
            this.f5050c.setStableInsets(cVar.e());
        }

        @Override // androidx.core.view.m0.f
        void e(androidx.core.graphics.c cVar) {
            this.f5050c.setSystemGestureInsets(cVar.e());
        }

        @Override // androidx.core.view.m0.f
        void f(androidx.core.graphics.c cVar) {
            this.f5050c.setSystemWindowInsets(cVar.e());
        }

        @Override // androidx.core.view.m0.f
        void g(androidx.core.graphics.c cVar) {
            this.f5050c.setTappableElementInsets(cVar.e());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class e extends d {
        e() {
        }

        e(m0 m0Var) {
            super(m0Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f {

        /* renamed from: a  reason: collision with root package name */
        private final m0 f5051a;

        /* renamed from: b  reason: collision with root package name */
        androidx.core.graphics.c[] f5052b;

        f() {
            this(new m0((m0) null));
        }

        f(m0 m0Var) {
            this.f5051a = m0Var;
        }

        protected final void a() {
            androidx.core.graphics.c[] cVarArr = this.f5052b;
            if (cVarArr != null) {
                androidx.core.graphics.c cVar = cVarArr[m.b(1)];
                androidx.core.graphics.c cVar2 = this.f5052b[m.b(2)];
                if (cVar2 == null) {
                    cVar2 = this.f5051a.f(2);
                }
                if (cVar == null) {
                    cVar = this.f5051a.f(1);
                }
                f(androidx.core.graphics.c.a(cVar, cVar2));
                androidx.core.graphics.c cVar3 = this.f5052b[m.b(16)];
                if (cVar3 != null) {
                    e(cVar3);
                }
                androidx.core.graphics.c cVar4 = this.f5052b[m.b(32)];
                if (cVar4 != null) {
                    c(cVar4);
                }
                androidx.core.graphics.c cVar5 = this.f5052b[m.b(64)];
                if (cVar5 != null) {
                    g(cVar5);
                }
            }
        }

        m0 b() {
            a();
            return this.f5051a;
        }

        void c(androidx.core.graphics.c cVar) {
        }

        void d(androidx.core.graphics.c cVar) {
        }

        void e(androidx.core.graphics.c cVar) {
        }

        void f(androidx.core.graphics.c cVar) {
        }

        void g(androidx.core.graphics.c cVar) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class g extends l {

        /* renamed from: h  reason: collision with root package name */
        private static boolean f5053h = false;

        /* renamed from: i  reason: collision with root package name */
        private static Method f5054i;

        /* renamed from: j  reason: collision with root package name */
        private static Class<?> f5055j;

        /* renamed from: k  reason: collision with root package name */
        private static Field f5056k;

        /* renamed from: l  reason: collision with root package name */
        private static Field f5057l;

        /* renamed from: c  reason: collision with root package name */
        final WindowInsets f5058c;

        /* renamed from: d  reason: collision with root package name */
        private androidx.core.graphics.c[] f5059d;

        /* renamed from: e  reason: collision with root package name */
        private androidx.core.graphics.c f5060e;

        /* renamed from: f  reason: collision with root package name */
        private m0 f5061f;

        /* renamed from: g  reason: collision with root package name */
        androidx.core.graphics.c f5062g;

        g(m0 m0Var, WindowInsets windowInsets) {
            super(m0Var);
            this.f5060e = null;
            this.f5058c = windowInsets;
        }

        g(m0 m0Var, g gVar) {
            this(m0Var, new WindowInsets(gVar.f5058c));
        }

        @SuppressLint({"WrongConstant"})
        private androidx.core.graphics.c u(int i8, boolean z4) {
            androidx.core.graphics.c cVar = androidx.core.graphics.c.f4707e;
            for (int i9 = 1; i9 <= 256; i9 <<= 1) {
                if ((i8 & i9) != 0) {
                    cVar = androidx.core.graphics.c.a(cVar, v(i9, z4));
                }
            }
            return cVar;
        }

        private androidx.core.graphics.c w() {
            m0 m0Var = this.f5061f;
            return m0Var != null ? m0Var.h() : androidx.core.graphics.c.f4707e;
        }

        private androidx.core.graphics.c x(View view) {
            if (Build.VERSION.SDK_INT < 30) {
                if (!f5053h) {
                    z();
                }
                Method method = f5054i;
                if (method != null && f5055j != null && f5056k != null) {
                    try {
                        Object invoke = method.invoke(view, new Object[0]);
                        if (invoke == null) {
                            Log.w("WindowInsetsCompat", "Failed to get visible insets. getViewRootImpl() returned null from the provided view. This means that the view is either not attached or the method has been overridden", new NullPointerException());
                            return null;
                        }
                        Rect rect = (Rect) f5056k.get(f5057l.get(invoke));
                        if (rect != null) {
                            return androidx.core.graphics.c.c(rect);
                        }
                        return null;
                    } catch (ReflectiveOperationException e8) {
                        Log.e("WindowInsetsCompat", "Failed to get visible insets. (Reflection error). " + e8.getMessage(), e8);
                    }
                }
                return null;
            }
            throw new UnsupportedOperationException("getVisibleInsets() should not be called on API >= 30. Use WindowInsets.isVisible() instead.");
        }

        @SuppressLint({"PrivateApi"})
        private static void z() {
            try {
                f5054i = View.class.getDeclaredMethod("getViewRootImpl", new Class[0]);
                Class<?> cls = Class.forName("android.view.View$AttachInfo");
                f5055j = cls;
                f5056k = cls.getDeclaredField("mVisibleInsets");
                f5057l = Class.forName("android.view.ViewRootImpl").getDeclaredField("mAttachInfo");
                f5056k.setAccessible(true);
                f5057l.setAccessible(true);
            } catch (ReflectiveOperationException e8) {
                Log.e("WindowInsetsCompat", "Failed to get visible insets. (Reflection error). " + e8.getMessage(), e8);
            }
            f5053h = true;
        }

        @Override // androidx.core.view.m0.l
        void d(View view) {
            androidx.core.graphics.c x8 = x(view);
            if (x8 == null) {
                x8 = androidx.core.graphics.c.f4707e;
            }
            r(x8);
        }

        @Override // androidx.core.view.m0.l
        void e(m0 m0Var) {
            m0Var.v(this.f5061f);
            m0Var.u(this.f5062g);
        }

        @Override // androidx.core.view.m0.l
        public boolean equals(Object obj) {
            if (super.equals(obj)) {
                return Objects.equals(this.f5062g, ((g) obj).f5062g);
            }
            return false;
        }

        @Override // androidx.core.view.m0.l
        public androidx.core.graphics.c g(int i8) {
            return u(i8, false);
        }

        @Override // androidx.core.view.m0.l
        final androidx.core.graphics.c k() {
            if (this.f5060e == null) {
                this.f5060e = androidx.core.graphics.c.b(this.f5058c.getSystemWindowInsetLeft(), this.f5058c.getSystemWindowInsetTop(), this.f5058c.getSystemWindowInsetRight(), this.f5058c.getSystemWindowInsetBottom());
            }
            return this.f5060e;
        }

        @Override // androidx.core.view.m0.l
        m0 m(int i8, int i9, int i10, int i11) {
            b bVar = new b(m0.y(this.f5058c));
            bVar.c(m0.p(k(), i8, i9, i10, i11));
            bVar.b(m0.p(i(), i8, i9, i10, i11));
            return bVar.a();
        }

        @Override // androidx.core.view.m0.l
        boolean o() {
            return this.f5058c.isRound();
        }

        @Override // androidx.core.view.m0.l
        @SuppressLint({"WrongConstant"})
        boolean p(int i8) {
            for (int i9 = 1; i9 <= 256; i9 <<= 1) {
                if ((i8 & i9) != 0 && !y(i9)) {
                    return false;
                }
            }
            return true;
        }

        @Override // androidx.core.view.m0.l
        public void q(androidx.core.graphics.c[] cVarArr) {
            this.f5059d = cVarArr;
        }

        @Override // androidx.core.view.m0.l
        void r(androidx.core.graphics.c cVar) {
            this.f5062g = cVar;
        }

        @Override // androidx.core.view.m0.l
        void s(m0 m0Var) {
            this.f5061f = m0Var;
        }

        protected androidx.core.graphics.c v(int i8, boolean z4) {
            androidx.core.graphics.c h8;
            int i9;
            if (i8 == 1) {
                return z4 ? androidx.core.graphics.c.b(0, Math.max(w().f4709b, k().f4709b), 0, 0) : androidx.core.graphics.c.b(0, k().f4709b, 0, 0);
            }
            if (i8 == 2) {
                if (z4) {
                    androidx.core.graphics.c w8 = w();
                    androidx.core.graphics.c i10 = i();
                    return androidx.core.graphics.c.b(Math.max(w8.f4708a, i10.f4708a), 0, Math.max(w8.f4710c, i10.f4710c), Math.max(w8.f4711d, i10.f4711d));
                }
                androidx.core.graphics.c k8 = k();
                m0 m0Var = this.f5061f;
                h8 = m0Var != null ? m0Var.h() : null;
                int i11 = k8.f4711d;
                if (h8 != null) {
                    i11 = Math.min(i11, h8.f4711d);
                }
                return androidx.core.graphics.c.b(k8.f4708a, 0, k8.f4710c, i11);
            } else if (i8 != 8) {
                if (i8 != 16) {
                    if (i8 != 32) {
                        if (i8 != 64) {
                            if (i8 != 128) {
                                return androidx.core.graphics.c.f4707e;
                            }
                            m0 m0Var2 = this.f5061f;
                            androidx.core.view.d e8 = m0Var2 != null ? m0Var2.e() : f();
                            return e8 != null ? androidx.core.graphics.c.b(e8.b(), e8.d(), e8.c(), e8.a()) : androidx.core.graphics.c.f4707e;
                        }
                        return l();
                    }
                    return h();
                }
                return j();
            } else {
                androidx.core.graphics.c[] cVarArr = this.f5059d;
                h8 = cVarArr != null ? cVarArr[m.b(8)] : null;
                if (h8 != null) {
                    return h8;
                }
                androidx.core.graphics.c k9 = k();
                androidx.core.graphics.c w9 = w();
                int i12 = k9.f4711d;
                if (i12 > w9.f4711d) {
                    return androidx.core.graphics.c.b(0, 0, 0, i12);
                }
                androidx.core.graphics.c cVar = this.f5062g;
                return (cVar == null || cVar.equals(androidx.core.graphics.c.f4707e) || (i9 = this.f5062g.f4711d) <= w9.f4711d) ? androidx.core.graphics.c.f4707e : androidx.core.graphics.c.b(0, 0, 0, i9);
            }
        }

        protected boolean y(int i8) {
            if (i8 != 1 && i8 != 2) {
                if (i8 == 4) {
                    return false;
                }
                if (i8 != 8 && i8 != 128) {
                    return true;
                }
            }
            return !v(i8, false).equals(androidx.core.graphics.c.f4707e);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class h extends g {

        /* renamed from: m  reason: collision with root package name */
        private androidx.core.graphics.c f5063m;

        h(m0 m0Var, WindowInsets windowInsets) {
            super(m0Var, windowInsets);
            this.f5063m = null;
        }

        h(m0 m0Var, h hVar) {
            super(m0Var, hVar);
            this.f5063m = null;
            this.f5063m = hVar.f5063m;
        }

        @Override // androidx.core.view.m0.l
        m0 b() {
            return m0.y(this.f5058c.consumeStableInsets());
        }

        @Override // androidx.core.view.m0.l
        m0 c() {
            return m0.y(this.f5058c.consumeSystemWindowInsets());
        }

        @Override // androidx.core.view.m0.l
        final androidx.core.graphics.c i() {
            if (this.f5063m == null) {
                this.f5063m = androidx.core.graphics.c.b(this.f5058c.getStableInsetLeft(), this.f5058c.getStableInsetTop(), this.f5058c.getStableInsetRight(), this.f5058c.getStableInsetBottom());
            }
            return this.f5063m;
        }

        @Override // androidx.core.view.m0.l
        boolean n() {
            return this.f5058c.isConsumed();
        }

        @Override // androidx.core.view.m0.l
        public void t(androidx.core.graphics.c cVar) {
            this.f5063m = cVar;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class i extends h {
        i(m0 m0Var, WindowInsets windowInsets) {
            super(m0Var, windowInsets);
        }

        i(m0 m0Var, i iVar) {
            super(m0Var, iVar);
        }

        @Override // androidx.core.view.m0.l
        m0 a() {
            return m0.y(this.f5058c.consumeDisplayCutout());
        }

        @Override // androidx.core.view.m0.g, androidx.core.view.m0.l
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof i) {
                i iVar = (i) obj;
                return Objects.equals(this.f5058c, iVar.f5058c) && Objects.equals(this.f5062g, iVar.f5062g);
            }
            return false;
        }

        @Override // androidx.core.view.m0.l
        androidx.core.view.d f() {
            return androidx.core.view.d.e(this.f5058c.getDisplayCutout());
        }

        @Override // androidx.core.view.m0.l
        public int hashCode() {
            return this.f5058c.hashCode();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class j extends i {

        /* renamed from: n  reason: collision with root package name */
        private androidx.core.graphics.c f5064n;

        /* renamed from: o  reason: collision with root package name */
        private androidx.core.graphics.c f5065o;

        /* renamed from: p  reason: collision with root package name */
        private androidx.core.graphics.c f5066p;

        j(m0 m0Var, WindowInsets windowInsets) {
            super(m0Var, windowInsets);
            this.f5064n = null;
            this.f5065o = null;
            this.f5066p = null;
        }

        j(m0 m0Var, j jVar) {
            super(m0Var, jVar);
            this.f5064n = null;
            this.f5065o = null;
            this.f5066p = null;
        }

        @Override // androidx.core.view.m0.l
        androidx.core.graphics.c h() {
            if (this.f5065o == null) {
                this.f5065o = androidx.core.graphics.c.d(this.f5058c.getMandatorySystemGestureInsets());
            }
            return this.f5065o;
        }

        @Override // androidx.core.view.m0.l
        androidx.core.graphics.c j() {
            if (this.f5064n == null) {
                this.f5064n = androidx.core.graphics.c.d(this.f5058c.getSystemGestureInsets());
            }
            return this.f5064n;
        }

        @Override // androidx.core.view.m0.l
        androidx.core.graphics.c l() {
            if (this.f5066p == null) {
                this.f5066p = androidx.core.graphics.c.d(this.f5058c.getTappableElementInsets());
            }
            return this.f5066p;
        }

        @Override // androidx.core.view.m0.g, androidx.core.view.m0.l
        m0 m(int i8, int i9, int i10, int i11) {
            return m0.y(this.f5058c.inset(i8, i9, i10, i11));
        }

        @Override // androidx.core.view.m0.h, androidx.core.view.m0.l
        public void t(androidx.core.graphics.c cVar) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class k extends j {
        static final m0 q = m0.y(WindowInsets.CONSUMED);

        k(m0 m0Var, WindowInsets windowInsets) {
            super(m0Var, windowInsets);
        }

        k(m0 m0Var, k kVar) {
            super(m0Var, kVar);
        }

        @Override // androidx.core.view.m0.g, androidx.core.view.m0.l
        final void d(View view) {
        }

        @Override // androidx.core.view.m0.g, androidx.core.view.m0.l
        public androidx.core.graphics.c g(int i8) {
            return androidx.core.graphics.c.d(this.f5058c.getInsets(n.a(i8)));
        }

        @Override // androidx.core.view.m0.g, androidx.core.view.m0.l
        public boolean p(int i8) {
            return this.f5058c.isVisible(n.a(i8));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class l {

        /* renamed from: b  reason: collision with root package name */
        static final m0 f5067b = new b().a().a().b().c();

        /* renamed from: a  reason: collision with root package name */
        final m0 f5068a;

        l(m0 m0Var) {
            this.f5068a = m0Var;
        }

        m0 a() {
            return this.f5068a;
        }

        m0 b() {
            return this.f5068a;
        }

        m0 c() {
            return this.f5068a;
        }

        void d(View view) {
        }

        void e(m0 m0Var) {
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof l) {
                l lVar = (l) obj;
                return o() == lVar.o() && n() == lVar.n() && androidx.core.util.c.a(k(), lVar.k()) && androidx.core.util.c.a(i(), lVar.i()) && androidx.core.util.c.a(f(), lVar.f());
            }
            return false;
        }

        androidx.core.view.d f() {
            return null;
        }

        androidx.core.graphics.c g(int i8) {
            return androidx.core.graphics.c.f4707e;
        }

        androidx.core.graphics.c h() {
            return k();
        }

        public int hashCode() {
            return androidx.core.util.c.b(Boolean.valueOf(o()), Boolean.valueOf(n()), k(), i(), f());
        }

        androidx.core.graphics.c i() {
            return androidx.core.graphics.c.f4707e;
        }

        androidx.core.graphics.c j() {
            return k();
        }

        androidx.core.graphics.c k() {
            return androidx.core.graphics.c.f4707e;
        }

        androidx.core.graphics.c l() {
            return k();
        }

        m0 m(int i8, int i9, int i10, int i11) {
            return f5067b;
        }

        boolean n() {
            return false;
        }

        boolean o() {
            return false;
        }

        boolean p(int i8) {
            return true;
        }

        public void q(androidx.core.graphics.c[] cVarArr) {
        }

        void r(androidx.core.graphics.c cVar) {
        }

        void s(m0 m0Var) {
        }

        public void t(androidx.core.graphics.c cVar) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class m {
        public static int a() {
            return 8;
        }

        static int b(int i8) {
            if (i8 != 1) {
                if (i8 != 2) {
                    if (i8 != 4) {
                        if (i8 != 8) {
                            if (i8 != 16) {
                                if (i8 != 32) {
                                    if (i8 != 64) {
                                        if (i8 != 128) {
                                            if (i8 == 256) {
                                                return 8;
                                            }
                                            throw new IllegalArgumentException("type needs to be >= FIRST and <= LAST, type=" + i8);
                                        }
                                        return 7;
                                    }
                                    return 6;
                                }
                                return 5;
                            }
                            return 4;
                        }
                        return 3;
                    }
                    return 2;
                }
                return 1;
            }
            return 0;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class n {
        static int a(int i8) {
            int statusBars;
            int i9 = 0;
            for (int i10 = 1; i10 <= 256; i10 <<= 1) {
                if ((i8 & i10) != 0) {
                    if (i10 == 1) {
                        statusBars = WindowInsets.Type.statusBars();
                    } else if (i10 == 2) {
                        statusBars = WindowInsets.Type.navigationBars();
                    } else if (i10 == 4) {
                        statusBars = WindowInsets.Type.captionBar();
                    } else if (i10 == 8) {
                        statusBars = WindowInsets.Type.ime();
                    } else if (i10 == 16) {
                        statusBars = WindowInsets.Type.systemGestures();
                    } else if (i10 == 32) {
                        statusBars = WindowInsets.Type.mandatorySystemGestures();
                    } else if (i10 == 64) {
                        statusBars = WindowInsets.Type.tappableElement();
                    } else if (i10 == 128) {
                        statusBars = WindowInsets.Type.displayCutout();
                    }
                    i9 |= statusBars;
                }
            }
            return i9;
        }
    }

    static {
        f5037b = Build.VERSION.SDK_INT >= 30 ? k.q : l.f5067b;
    }

    private m0(WindowInsets windowInsets) {
        l gVar;
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 30) {
            gVar = new k(this, windowInsets);
        } else if (i8 >= 29) {
            gVar = new j(this, windowInsets);
        } else if (i8 >= 28) {
            gVar = new i(this, windowInsets);
        } else if (i8 >= 21) {
            gVar = new h(this, windowInsets);
        } else if (i8 < 20) {
            this.f5038a = new l(this);
            return;
        } else {
            gVar = new g(this, windowInsets);
        }
        this.f5038a = gVar;
    }

    public m0(m0 m0Var) {
        if (m0Var == null) {
            this.f5038a = new l(this);
            return;
        }
        l lVar = m0Var.f5038a;
        int i8 = Build.VERSION.SDK_INT;
        this.f5038a = (i8 < 30 || !(lVar instanceof k)) ? (i8 < 29 || !(lVar instanceof j)) ? (i8 < 28 || !(lVar instanceof i)) ? (i8 < 21 || !(lVar instanceof h)) ? (i8 < 20 || !(lVar instanceof g)) ? new l(this) : new g(this, (g) lVar) : new h(this, (h) lVar) : new i(this, (i) lVar) : new j(this, (j) lVar) : new k(this, (k) lVar);
        lVar.e(this);
    }

    static androidx.core.graphics.c p(androidx.core.graphics.c cVar, int i8, int i9, int i10, int i11) {
        int max = Math.max(0, cVar.f4708a - i8);
        int max2 = Math.max(0, cVar.f4709b - i9);
        int max3 = Math.max(0, cVar.f4710c - i10);
        int max4 = Math.max(0, cVar.f4711d - i11);
        return (max == i8 && max2 == i9 && max3 == i10 && max4 == i11) ? cVar : androidx.core.graphics.c.b(max, max2, max3, max4);
    }

    public static m0 y(WindowInsets windowInsets) {
        return z(windowInsets, null);
    }

    public static m0 z(WindowInsets windowInsets, View view) {
        m0 m0Var = new m0((WindowInsets) androidx.core.util.h.h(windowInsets));
        if (view != null && c0.V(view)) {
            m0Var.v(c0.L(view));
            m0Var.d(view.getRootView());
        }
        return m0Var;
    }

    @Deprecated
    public m0 a() {
        return this.f5038a.a();
    }

    @Deprecated
    public m0 b() {
        return this.f5038a.b();
    }

    @Deprecated
    public m0 c() {
        return this.f5038a.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(View view) {
        this.f5038a.d(view);
    }

    public androidx.core.view.d e() {
        return this.f5038a.f();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof m0) {
            return androidx.core.util.c.a(this.f5038a, ((m0) obj).f5038a);
        }
        return false;
    }

    public androidx.core.graphics.c f(int i8) {
        return this.f5038a.g(i8);
    }

    @Deprecated
    public androidx.core.graphics.c g() {
        return this.f5038a.h();
    }

    @Deprecated
    public androidx.core.graphics.c h() {
        return this.f5038a.i();
    }

    public int hashCode() {
        l lVar = this.f5038a;
        if (lVar == null) {
            return 0;
        }
        return lVar.hashCode();
    }

    @Deprecated
    public androidx.core.graphics.c i() {
        return this.f5038a.j();
    }

    @Deprecated
    public int j() {
        return this.f5038a.k().f4711d;
    }

    @Deprecated
    public int k() {
        return this.f5038a.k().f4708a;
    }

    @Deprecated
    public int l() {
        return this.f5038a.k().f4710c;
    }

    @Deprecated
    public int m() {
        return this.f5038a.k().f4709b;
    }

    @Deprecated
    public boolean n() {
        return !this.f5038a.k().equals(androidx.core.graphics.c.f4707e);
    }

    public m0 o(int i8, int i9, int i10, int i11) {
        return this.f5038a.m(i8, i9, i10, i11);
    }

    public boolean q() {
        return this.f5038a.n();
    }

    public boolean r(int i8) {
        return this.f5038a.p(i8);
    }

    @Deprecated
    public m0 s(int i8, int i9, int i10, int i11) {
        return new b(this).c(androidx.core.graphics.c.b(i8, i9, i10, i11)).a();
    }

    void t(androidx.core.graphics.c[] cVarArr) {
        this.f5038a.q(cVarArr);
    }

    void u(androidx.core.graphics.c cVar) {
        this.f5038a.r(cVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v(m0 m0Var) {
        this.f5038a.s(m0Var);
    }

    void w(androidx.core.graphics.c cVar) {
        this.f5038a.t(cVar);
    }

    public WindowInsets x() {
        l lVar = this.f5038a;
        if (lVar instanceof g) {
            return ((g) lVar).f5058c;
        }
        return null;
    }
}
