package androidx.appcompat.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.LocaleManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.window.OnBackInvokedDispatcher;
import androidx.appcompat.app.a;
import androidx.appcompat.app.m;
import androidx.appcompat.view.b;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.t0;
import java.lang.ref.WeakReference;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class f {

    /* renamed from: a  reason: collision with root package name */
    static m.a f658a = new m.a(new m.b());

    /* renamed from: b  reason: collision with root package name */
    private static int f659b = -100;

    /* renamed from: c  reason: collision with root package name */
    private static androidx.core.os.j f660c = null;

    /* renamed from: d  reason: collision with root package name */
    private static androidx.core.os.j f661d = null;

    /* renamed from: e  reason: collision with root package name */
    private static Boolean f662e = null;

    /* renamed from: f  reason: collision with root package name */
    private static boolean f663f = false;

    /* renamed from: g  reason: collision with root package name */
    private static final k0.b<WeakReference<f>> f664g = new k0.b<>();

    /* renamed from: h  reason: collision with root package name */
    private static final Object f665h = new Object();

    /* renamed from: j  reason: collision with root package name */
    private static final Object f666j = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        /* JADX INFO: Access modifiers changed from: package-private */
        public static LocaleList a(String str) {
            return LocaleList.forLanguageTags(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {
        static LocaleList a(Object obj) {
            return ((LocaleManager) obj).getApplicationLocales();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static void b(Object obj, LocaleList localeList) {
            ((LocaleManager) obj).setApplicationLocales(localeList);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void F(f fVar) {
        synchronized (f665h) {
            G(fVar);
        }
    }

    private static void G(f fVar) {
        synchronized (f665h) {
            Iterator<WeakReference<f>> it = f664g.iterator();
            while (it.hasNext()) {
                f fVar2 = it.next().get();
                if (fVar2 == fVar || fVar2 == null) {
                    it.remove();
                }
            }
        }
    }

    public static void I(boolean z4) {
        t0.c(z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void R(final Context context) {
        if (v(context)) {
            if (androidx.core.os.a.d()) {
                if (f663f) {
                    return;
                }
                f658a.execute(new Runnable() { // from class: androidx.appcompat.app.e
                    @Override // java.lang.Runnable
                    public final void run() {
                        f.w(context);
                    }
                });
                return;
            }
            synchronized (f666j) {
                androidx.core.os.j jVar = f660c;
                if (jVar == null) {
                    if (f661d == null) {
                        f661d = androidx.core.os.j.c(m.b(context));
                    }
                    if (f661d.f()) {
                        return;
                    }
                    f660c = f661d;
                } else if (!jVar.equals(f661d)) {
                    androidx.core.os.j jVar2 = f660c;
                    f661d = jVar2;
                    m.a(context, jVar2.h());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void d(f fVar) {
        synchronized (f665h) {
            G(fVar);
            f664g.add(new WeakReference<>(fVar));
        }
    }

    public static f h(Activity activity, d dVar) {
        return new AppCompatDelegateImpl(activity, dVar);
    }

    public static f i(Dialog dialog, d dVar) {
        return new AppCompatDelegateImpl(dialog, dVar);
    }

    public static androidx.core.os.j k() {
        if (androidx.core.os.a.d()) {
            Object p8 = p();
            if (p8 != null) {
                return androidx.core.os.j.i(b.a(p8));
            }
        } else {
            androidx.core.os.j jVar = f660c;
            if (jVar != null) {
                return jVar;
            }
        }
        return androidx.core.os.j.e();
    }

    public static int m() {
        return f659b;
    }

    static Object p() {
        Context l8;
        Iterator<WeakReference<f>> it = f664g.iterator();
        while (it.hasNext()) {
            f fVar = it.next().get();
            if (fVar != null && (l8 = fVar.l()) != null) {
                return l8.getSystemService("locale");
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static androidx.core.os.j r() {
        return f660c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean v(Context context) {
        if (f662e == null) {
            try {
                Bundle bundle = AppLocalesMetadataHolderService.a(context).metaData;
                if (bundle != null) {
                    f662e = Boolean.valueOf(bundle.getBoolean("autoStoreLocales"));
                }
            } catch (PackageManager.NameNotFoundException unused) {
                Log.d("AppCompatDelegate", "Checking for metadata for AppLocalesMetadataHolderService : Service not found");
                f662e = Boolean.FALSE;
            }
        }
        return f662e.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void w(Context context) {
        m.c(context);
        f663f = true;
    }

    public abstract void A(Bundle bundle);

    public abstract void B();

    public abstract void C(Bundle bundle);

    public abstract void D();

    public abstract void E();

    public abstract boolean H(int i8);

    public abstract void J(int i8);

    public abstract void K(View view);

    public abstract void L(View view, ViewGroup.LayoutParams layoutParams);

    public void M(OnBackInvokedDispatcher onBackInvokedDispatcher) {
    }

    public abstract void N(Toolbar toolbar);

    public void O(int i8) {
    }

    public abstract void P(CharSequence charSequence);

    public abstract androidx.appcompat.view.b Q(b.a aVar);

    public abstract void e(View view, ViewGroup.LayoutParams layoutParams);

    @Deprecated
    public void f(Context context) {
    }

    public Context g(Context context) {
        f(context);
        return context;
    }

    public abstract <T extends View> T j(int i8);

    public Context l() {
        return null;
    }

    public abstract a.b n();

    public int o() {
        return -100;
    }

    public abstract MenuInflater q();

    public abstract ActionBar s();

    public abstract void t();

    public abstract void u();

    public abstract void x(Configuration configuration);

    public abstract void y(Bundle bundle);

    public abstract void z();
}
