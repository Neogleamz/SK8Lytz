package androidx.appcompat.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g0 extends ContextWrapper {

    /* renamed from: c  reason: collision with root package name */
    private static final Object f1492c = new Object();

    /* renamed from: d  reason: collision with root package name */
    private static ArrayList<WeakReference<g0>> f1493d;

    /* renamed from: a  reason: collision with root package name */
    private final Resources f1494a;

    /* renamed from: b  reason: collision with root package name */
    private final Resources.Theme f1495b;

    private g0(Context context) {
        super(context);
        if (!t0.d()) {
            this.f1494a = new i0(this, context.getResources());
            this.f1495b = null;
            return;
        }
        t0 t0Var = new t0(this, context.getResources());
        this.f1494a = t0Var;
        Resources.Theme newTheme = t0Var.newTheme();
        this.f1495b = newTheme;
        newTheme.setTo(context.getTheme());
    }

    private static boolean a(Context context) {
        if ((context instanceof g0) || (context.getResources() instanceof i0) || (context.getResources() instanceof t0)) {
            return false;
        }
        return Build.VERSION.SDK_INT < 21 || t0.d();
    }

    public static Context b(Context context) {
        if (a(context)) {
            synchronized (f1492c) {
                ArrayList<WeakReference<g0>> arrayList = f1493d;
                if (arrayList == null) {
                    f1493d = new ArrayList<>();
                } else {
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        WeakReference<g0> weakReference = f1493d.get(size);
                        if (weakReference == null || weakReference.get() == null) {
                            f1493d.remove(size);
                        }
                    }
                    for (int size2 = f1493d.size() - 1; size2 >= 0; size2--) {
                        WeakReference<g0> weakReference2 = f1493d.get(size2);
                        g0 g0Var = weakReference2 != null ? weakReference2.get() : null;
                        if (g0Var != null && g0Var.getBaseContext() == context) {
                            return g0Var;
                        }
                    }
                }
                g0 g0Var2 = new g0(context);
                f1493d.add(new WeakReference<>(g0Var2));
                return g0Var2;
            }
        }
        return context;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public AssetManager getAssets() {
        return this.f1494a.getAssets();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources getResources() {
        return this.f1494a;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources.Theme getTheme() {
        Resources.Theme theme = this.f1495b;
        return theme == null ? super.getTheme() : theme;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void setTheme(int i8) {
        Resources.Theme theme = this.f1495b;
        if (theme == null) {
            super.setTheme(i8);
        } else {
            theme.applyStyle(i8, true);
        }
    }
}
