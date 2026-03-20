package androidx.core.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import androidx.core.provider.g;
import com.google.android.gms.dynamite.descriptors.com.google.mlkit.dynamite.barcode.ModuleDescriptor;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f {

    /* renamed from: a  reason: collision with root package name */
    static final k0.e<String, Typeface> f4804a = new k0.e<>(16);

    /* renamed from: b  reason: collision with root package name */
    private static final ExecutorService f4805b = h.a("fonts-androidx", 10, ModuleDescriptor.MODULE_VERSION);

    /* renamed from: c  reason: collision with root package name */
    static final Object f4806c = new Object();

    /* renamed from: d  reason: collision with root package name */
    static final k0.g<String, ArrayList<androidx.core.util.a<e>>> f4807d = new k0.g<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Callable<e> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ String f4808a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Context f4809b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ androidx.core.provider.e f4810c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ int f4811d;

        a(String str, Context context, androidx.core.provider.e eVar, int i8) {
            this.f4808a = str;
            this.f4809b = context;
            this.f4810c = eVar;
            this.f4811d = i8;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a */
        public e call() {
            return f.c(this.f4808a, this.f4809b, this.f4810c, this.f4811d);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements androidx.core.util.a<e> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ androidx.core.provider.a f4812a;

        b(androidx.core.provider.a aVar) {
            this.f4812a = aVar;
        }

        @Override // androidx.core.util.a
        /* renamed from: a */
        public void accept(e eVar) {
            if (eVar == null) {
                eVar = new e(-3);
            }
            this.f4812a.b(eVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements Callable<e> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ String f4813a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Context f4814b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ androidx.core.provider.e f4815c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ int f4816d;

        c(String str, Context context, androidx.core.provider.e eVar, int i8) {
            this.f4813a = str;
            this.f4814b = context;
            this.f4815c = eVar;
            this.f4816d = i8;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a */
        public e call() {
            try {
                return f.c(this.f4813a, this.f4814b, this.f4815c, this.f4816d);
            } catch (Throwable unused) {
                return new e(-3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements androidx.core.util.a<e> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ String f4817a;

        d(String str) {
            this.f4817a = str;
        }

        @Override // androidx.core.util.a
        /* renamed from: a */
        public void accept(e eVar) {
            synchronized (f.f4806c) {
                k0.g<String, ArrayList<androidx.core.util.a<e>>> gVar = f.f4807d;
                ArrayList<androidx.core.util.a<e>> arrayList = gVar.get(this.f4817a);
                if (arrayList == null) {
                    return;
                }
                gVar.remove(this.f4817a);
                for (int i8 = 0; i8 < arrayList.size(); i8++) {
                    arrayList.get(i8).accept(eVar);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: a  reason: collision with root package name */
        final Typeface f4818a;

        /* renamed from: b  reason: collision with root package name */
        final int f4819b;

        e(int i8) {
            this.f4818a = null;
            this.f4819b = i8;
        }

        @SuppressLint({"WrongConstant"})
        e(Typeface typeface) {
            this.f4818a = typeface;
            this.f4819b = 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @SuppressLint({"WrongConstant"})
        public boolean a() {
            return this.f4819b == 0;
        }
    }

    private static String a(androidx.core.provider.e eVar, int i8) {
        return eVar.d() + "-" + i8;
    }

    @SuppressLint({"WrongConstant"})
    private static int b(g.a aVar) {
        int i8 = 1;
        if (aVar.c() != 0) {
            return aVar.c() != 1 ? -3 : -2;
        }
        g.b[] b9 = aVar.b();
        if (b9 != null && b9.length != 0) {
            i8 = 0;
            for (g.b bVar : b9) {
                int b10 = bVar.b();
                if (b10 != 0) {
                    if (b10 < 0) {
                        return -3;
                    }
                    return b10;
                }
            }
        }
        return i8;
    }

    static e c(String str, Context context, androidx.core.provider.e eVar, int i8) {
        k0.e<String, Typeface> eVar2 = f4804a;
        Typeface c9 = eVar2.c(str);
        if (c9 != null) {
            return new e(c9);
        }
        try {
            g.a e8 = androidx.core.provider.d.e(context, eVar, null);
            int b9 = b(e8);
            if (b9 != 0) {
                return new e(b9);
            }
            Typeface b10 = androidx.core.graphics.f.b(context, null, e8.b(), i8);
            if (b10 != null) {
                eVar2.d(str, b10);
                return new e(b10);
            }
            return new e(-3);
        } catch (PackageManager.NameNotFoundException unused) {
            return new e(-1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Typeface d(Context context, androidx.core.provider.e eVar, int i8, Executor executor, androidx.core.provider.a aVar) {
        String a9 = a(eVar, i8);
        Typeface c9 = f4804a.c(a9);
        if (c9 != null) {
            aVar.b(new e(c9));
            return c9;
        }
        b bVar = new b(aVar);
        synchronized (f4806c) {
            k0.g<String, ArrayList<androidx.core.util.a<e>>> gVar = f4807d;
            ArrayList<androidx.core.util.a<e>> arrayList = gVar.get(a9);
            if (arrayList != null) {
                arrayList.add(bVar);
                return null;
            }
            ArrayList<androidx.core.util.a<e>> arrayList2 = new ArrayList<>();
            arrayList2.add(bVar);
            gVar.put(a9, arrayList2);
            c cVar = new c(a9, context, eVar, i8);
            if (executor == null) {
                executor = f4805b;
            }
            h.b(executor, cVar, new d(a9));
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Typeface e(Context context, androidx.core.provider.e eVar, androidx.core.provider.a aVar, int i8, int i9) {
        String a9 = a(eVar, i8);
        Typeface c9 = f4804a.c(a9);
        if (c9 != null) {
            aVar.b(new e(c9));
            return c9;
        } else if (i9 == -1) {
            e c10 = c(a9, context, eVar, i8);
            aVar.b(c10);
            return c10.f4818a;
        } else {
            try {
                e eVar2 = (e) h.c(f4805b, new a(a9, context, eVar, i8), i9);
                aVar.b(eVar2);
                return eVar2.f4818a;
            } catch (InterruptedException unused) {
                aVar.b(new e(-3));
                return null;
            }
        }
    }
}
