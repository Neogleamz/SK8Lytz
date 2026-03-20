package androidx.appcompat.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d extends ContextWrapper {

    /* renamed from: f  reason: collision with root package name */
    private static Configuration f759f;

    /* renamed from: a  reason: collision with root package name */
    private int f760a;

    /* renamed from: b  reason: collision with root package name */
    private Resources.Theme f761b;

    /* renamed from: c  reason: collision with root package name */
    private LayoutInflater f762c;

    /* renamed from: d  reason: collision with root package name */
    private Configuration f763d;

    /* renamed from: e  reason: collision with root package name */
    private Resources f764e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        static Context a(d dVar, Configuration configuration) {
            return dVar.createConfigurationContext(configuration);
        }
    }

    public d() {
        super(null);
    }

    public d(Context context, int i8) {
        super(context);
        this.f760a = i8;
    }

    public d(Context context, Resources.Theme theme) {
        super(context);
        this.f761b = theme;
    }

    private Resources b() {
        Resources resources;
        int i8;
        if (this.f764e == null) {
            Configuration configuration = this.f763d;
            if (configuration == null || ((i8 = Build.VERSION.SDK_INT) >= 26 && e(configuration))) {
                resources = super.getResources();
            } else if (i8 >= 17) {
                resources = a.a(this, this.f763d).getResources();
            } else {
                Resources resources2 = super.getResources();
                Configuration configuration2 = new Configuration(resources2.getConfiguration());
                configuration2.updateFrom(this.f763d);
                this.f764e = new Resources(resources2.getAssets(), resources2.getDisplayMetrics(), configuration2);
            }
            this.f764e = resources;
        }
        return this.f764e;
    }

    private void d() {
        boolean z4 = this.f761b == null;
        if (z4) {
            this.f761b = getResources().newTheme();
            Resources.Theme theme = getBaseContext().getTheme();
            if (theme != null) {
                this.f761b.setTo(theme);
            }
        }
        f(this.f761b, this.f760a, z4);
    }

    private static boolean e(Configuration configuration) {
        if (configuration == null) {
            return true;
        }
        if (f759f == null) {
            Configuration configuration2 = new Configuration();
            configuration2.fontScale = 0.0f;
            f759f = configuration2;
        }
        return configuration.equals(f759f);
    }

    public void a(Configuration configuration) {
        if (this.f764e != null) {
            throw new IllegalStateException("getResources() or getAssets() has already been called");
        }
        if (this.f763d != null) {
            throw new IllegalStateException("Override configuration has already been set");
        }
        this.f763d = new Configuration(configuration);
    }

    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    public int c() {
        return this.f760a;
    }

    protected void f(Resources.Theme theme, int i8, boolean z4) {
        theme.applyStyle(i8, true);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources getResources() {
        return b();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Object getSystemService(String str) {
        if ("layout_inflater".equals(str)) {
            if (this.f762c == null) {
                this.f762c = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return this.f762c;
        }
        return getBaseContext().getSystemService(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources.Theme getTheme() {
        Resources.Theme theme = this.f761b;
        if (theme != null) {
            return theme;
        }
        if (this.f760a == 0) {
            this.f760a = g.i.f20002f;
        }
        d();
        return this.f761b;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void setTheme(int i8) {
        if (this.f760a != i8) {
            this.f760a = i8;
            d();
        }
    }
}
