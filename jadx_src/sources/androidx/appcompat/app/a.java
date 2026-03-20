package androidx.appcompat.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.b;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements DrawerLayout.e {

    /* renamed from: a  reason: collision with root package name */
    private final b f634a;

    /* renamed from: b  reason: collision with root package name */
    private final DrawerLayout f635b;

    /* renamed from: c  reason: collision with root package name */
    private i.d f636c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f637d;

    /* renamed from: e  reason: collision with root package name */
    private Drawable f638e;

    /* renamed from: f  reason: collision with root package name */
    boolean f639f;

    /* renamed from: g  reason: collision with root package name */
    private final int f640g;

    /* renamed from: h  reason: collision with root package name */
    private final int f641h;

    /* renamed from: i  reason: collision with root package name */
    View.OnClickListener f642i;

    /* renamed from: j  reason: collision with root package name */
    private boolean f643j;

    /* renamed from: androidx.appcompat.app.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class View$OnClickListenerC0010a implements View.OnClickListener {
        View$OnClickListenerC0010a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            a aVar = a.this;
            if (aVar.f639f) {
                aVar.j();
                return;
            }
            View.OnClickListener onClickListener = aVar.f642i;
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        boolean a();

        Context b();

        void c(Drawable drawable, int i8);

        Drawable d();

        void e(int i8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        b getDrawerToggleDelegate();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class d implements b {

        /* renamed from: a  reason: collision with root package name */
        private final Activity f645a;

        /* renamed from: b  reason: collision with root package name */
        private b.a f646b;

        /* renamed from: androidx.appcompat.app.a$d$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class C0011a {
            static void a(android.app.ActionBar actionBar, int i8) {
                actionBar.setHomeActionContentDescription(i8);
            }

            static void b(android.app.ActionBar actionBar, Drawable drawable) {
                actionBar.setHomeAsUpIndicator(drawable);
            }
        }

        d(Activity activity) {
            this.f645a = activity;
        }

        @Override // androidx.appcompat.app.a.b
        public boolean a() {
            android.app.ActionBar actionBar = this.f645a.getActionBar();
            return (actionBar == null || (actionBar.getDisplayOptions() & 4) == 0) ? false : true;
        }

        @Override // androidx.appcompat.app.a.b
        public Context b() {
            android.app.ActionBar actionBar = this.f645a.getActionBar();
            return actionBar != null ? actionBar.getThemedContext() : this.f645a;
        }

        @Override // androidx.appcompat.app.a.b
        public void c(Drawable drawable, int i8) {
            android.app.ActionBar actionBar = this.f645a.getActionBar();
            if (actionBar != null) {
                if (Build.VERSION.SDK_INT >= 18) {
                    C0011a.b(actionBar, drawable);
                    C0011a.a(actionBar, i8);
                    return;
                }
                actionBar.setDisplayShowHomeEnabled(true);
                this.f646b = androidx.appcompat.app.b.c(this.f645a, drawable, i8);
                actionBar.setDisplayShowHomeEnabled(false);
            }
        }

        @Override // androidx.appcompat.app.a.b
        public Drawable d() {
            if (Build.VERSION.SDK_INT >= 18) {
                TypedArray obtainStyledAttributes = b().obtainStyledAttributes(null, new int[]{16843531}, 16843470, 0);
                Drawable drawable = obtainStyledAttributes.getDrawable(0);
                obtainStyledAttributes.recycle();
                return drawable;
            }
            return androidx.appcompat.app.b.a(this.f645a);
        }

        @Override // androidx.appcompat.app.a.b
        public void e(int i8) {
            if (Build.VERSION.SDK_INT < 18) {
                this.f646b = androidx.appcompat.app.b.b(this.f646b, this.f645a, i8);
                return;
            }
            android.app.ActionBar actionBar = this.f645a.getActionBar();
            if (actionBar != null) {
                C0011a.a(actionBar, i8);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class e implements b {

        /* renamed from: a  reason: collision with root package name */
        final Toolbar f647a;

        /* renamed from: b  reason: collision with root package name */
        final Drawable f648b;

        /* renamed from: c  reason: collision with root package name */
        final CharSequence f649c;

        e(Toolbar toolbar) {
            this.f647a = toolbar;
            this.f648b = toolbar.getNavigationIcon();
            this.f649c = toolbar.getNavigationContentDescription();
        }

        @Override // androidx.appcompat.app.a.b
        public boolean a() {
            return true;
        }

        @Override // androidx.appcompat.app.a.b
        public Context b() {
            return this.f647a.getContext();
        }

        @Override // androidx.appcompat.app.a.b
        public void c(Drawable drawable, int i8) {
            this.f647a.setNavigationIcon(drawable);
            e(i8);
        }

        @Override // androidx.appcompat.app.a.b
        public Drawable d() {
            return this.f648b;
        }

        @Override // androidx.appcompat.app.a.b
        public void e(int i8) {
            if (i8 == 0) {
                this.f647a.setNavigationContentDescription(this.f649c);
            } else {
                this.f647a.setNavigationContentDescription(i8);
            }
        }
    }

    a(Activity activity, Toolbar toolbar, DrawerLayout drawerLayout, i.d dVar, int i8, int i9) {
        this.f637d = true;
        this.f639f = true;
        this.f643j = false;
        if (toolbar != null) {
            this.f634a = new e(toolbar);
            toolbar.setNavigationOnClickListener(new View$OnClickListenerC0010a());
        } else if (activity instanceof c) {
            this.f634a = ((c) activity).getDrawerToggleDelegate();
        } else {
            this.f634a = new d(activity);
        }
        this.f635b = drawerLayout;
        this.f640g = i8;
        this.f641h = i9;
        if (dVar == null) {
            this.f636c = new i.d(this.f634a.b());
        } else {
            this.f636c = dVar;
        }
        this.f638e = e();
    }

    public a(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int i8, int i9) {
        this(activity, toolbar, drawerLayout, null, i8, i9);
    }

    private void h(float f5) {
        i.d dVar;
        boolean z4;
        if (f5 != 1.0f) {
            if (f5 == 0.0f) {
                dVar = this.f636c;
                z4 = false;
            }
            this.f636c.setProgress(f5);
        }
        dVar = this.f636c;
        z4 = true;
        dVar.f(z4);
        this.f636c.setProgress(f5);
    }

    @Override // androidx.drawerlayout.widget.DrawerLayout.e
    public void a(View view) {
        h(1.0f);
        if (this.f639f) {
            f(this.f641h);
        }
    }

    @Override // androidx.drawerlayout.widget.DrawerLayout.e
    public void b(View view) {
        h(0.0f);
        if (this.f639f) {
            f(this.f640g);
        }
    }

    @Override // androidx.drawerlayout.widget.DrawerLayout.e
    public void c(int i8) {
    }

    @Override // androidx.drawerlayout.widget.DrawerLayout.e
    public void d(View view, float f5) {
        if (this.f637d) {
            h(Math.min(1.0f, Math.max(0.0f, f5)));
        } else {
            h(0.0f);
        }
    }

    Drawable e() {
        return this.f634a.d();
    }

    void f(int i8) {
        this.f634a.e(i8);
    }

    void g(Drawable drawable, int i8) {
        if (!this.f643j && !this.f634a.a()) {
            Log.w("ActionBarDrawerToggle", "DrawerToggle may not show up because NavigationIcon is not visible. You may need to call actionbar.setDisplayHomeAsUpEnabled(true);");
            this.f643j = true;
        }
        this.f634a.c(drawable, i8);
    }

    public void i() {
        h(this.f635b.C(8388611) ? 1.0f : 0.0f);
        if (this.f639f) {
            g(this.f636c, this.f635b.C(8388611) ? this.f641h : this.f640g);
        }
    }

    void j() {
        int q = this.f635b.q(8388611);
        if (this.f635b.F(8388611) && q != 2) {
            this.f635b.d(8388611);
        } else if (q != 1) {
            this.f635b.K(8388611);
        }
    }
}
