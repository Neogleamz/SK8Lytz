package androidx.core.view;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n0 {

    /* renamed from: a  reason: collision with root package name */
    private final e f5069a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a extends e {

        /* renamed from: a  reason: collision with root package name */
        protected final Window f5070a;

        /* renamed from: b  reason: collision with root package name */
        private final View f5071b;

        a(Window window, View view) {
            this.f5070a = window;
            this.f5071b = view;
        }

        protected void c(int i8) {
            View decorView = this.f5070a.getDecorView();
            decorView.setSystemUiVisibility(i8 | decorView.getSystemUiVisibility());
        }

        protected void d(int i8) {
            this.f5070a.addFlags(i8);
        }

        protected void e(int i8) {
            View decorView = this.f5070a.getDecorView();
            decorView.setSystemUiVisibility((~i8) & decorView.getSystemUiVisibility());
        }

        protected void f(int i8) {
            this.f5070a.clearFlags(i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class b extends a {
        b(Window window, View view) {
            super(window, view);
        }

        @Override // androidx.core.view.n0.e
        public void b(boolean z4) {
            if (!z4) {
                e(8192);
                return;
            }
            f(67108864);
            d(Integer.MIN_VALUE);
            c(8192);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c extends b {
        c(Window window, View view) {
            super(window, view);
        }

        @Override // androidx.core.view.n0.e
        public void a(boolean z4) {
            if (!z4) {
                e(16);
                return;
            }
            f(134217728);
            d(Integer.MIN_VALUE);
            c(16);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class d extends e {

        /* renamed from: a  reason: collision with root package name */
        final n0 f5072a;

        /* renamed from: b  reason: collision with root package name */
        final WindowInsetsController f5073b;

        /* renamed from: c  reason: collision with root package name */
        private final k0.g<Object, WindowInsetsController.OnControllableInsetsChangedListener> f5074c;

        /* renamed from: d  reason: collision with root package name */
        protected Window f5075d;

        d(Window window, n0 n0Var) {
            this(window.getInsetsController(), n0Var);
            this.f5075d = window;
        }

        d(WindowInsetsController windowInsetsController, n0 n0Var) {
            this.f5074c = new k0.g<>();
            this.f5073b = windowInsetsController;
            this.f5072a = n0Var;
        }

        @Override // androidx.core.view.n0.e
        public void a(boolean z4) {
            if (z4) {
                if (this.f5075d != null) {
                    c(16);
                }
                this.f5073b.setSystemBarsAppearance(16, 16);
                return;
            }
            if (this.f5075d != null) {
                d(16);
            }
            this.f5073b.setSystemBarsAppearance(0, 16);
        }

        @Override // androidx.core.view.n0.e
        public void b(boolean z4) {
            if (z4) {
                if (this.f5075d != null) {
                    c(8192);
                }
                this.f5073b.setSystemBarsAppearance(8, 8);
                return;
            }
            if (this.f5075d != null) {
                d(8192);
            }
            this.f5073b.setSystemBarsAppearance(0, 8);
        }

        protected void c(int i8) {
            View decorView = this.f5075d.getDecorView();
            decorView.setSystemUiVisibility(i8 | decorView.getSystemUiVisibility());
        }

        protected void d(int i8) {
            View decorView = this.f5075d.getDecorView();
            decorView.setSystemUiVisibility((~i8) & decorView.getSystemUiVisibility());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class e {
        e() {
        }

        public void a(boolean z4) {
        }

        public void b(boolean z4) {
        }
    }

    public n0(Window window, View view) {
        e aVar;
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 30) {
            this.f5069a = new d(window, this);
            return;
        }
        if (i8 >= 26) {
            aVar = new c(window, view);
        } else if (i8 >= 23) {
            aVar = new b(window, view);
        } else if (i8 < 20) {
            this.f5069a = new e();
            return;
        } else {
            aVar = new a(window, view);
        }
        this.f5069a = aVar;
    }

    public void a(boolean z4) {
        this.f5069a.a(z4);
    }

    public void b(boolean z4) {
        this.f5069a.b(z4);
    }
}
