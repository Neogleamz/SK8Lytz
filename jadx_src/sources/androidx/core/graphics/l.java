package androidx.core.graphics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.core.content.res.e;
import androidx.core.provider.g;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l {
    @SuppressLint({"BanConcurrentHashMap"})

    /* renamed from: a  reason: collision with root package name */
    private ConcurrentHashMap<Long, e.c> f4763a = new ConcurrentHashMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements c<g.b> {
        a() {
        }

        @Override // androidx.core.graphics.l.c
        /* renamed from: c */
        public int a(g.b bVar) {
            return bVar.e();
        }

        @Override // androidx.core.graphics.l.c
        /* renamed from: d */
        public boolean b(g.b bVar) {
            return bVar.f();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements c<e.d> {
        b() {
        }

        @Override // androidx.core.graphics.l.c
        /* renamed from: c */
        public int a(e.d dVar) {
            return dVar.e();
        }

        @Override // androidx.core.graphics.l.c
        /* renamed from: d */
        public boolean b(e.d dVar) {
            return dVar.f();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c<T> {
        int a(T t8);

        boolean b(T t8);
    }

    private void a(Typeface typeface, e.c cVar) {
        long j8 = j(typeface);
        if (j8 != 0) {
            this.f4763a.put(Long.valueOf(j8), cVar);
        }
    }

    private e.d f(e.c cVar, int i8) {
        return (e.d) g(cVar.a(), i8, new b());
    }

    private static <T> T g(T[] tArr, int i8, c<T> cVar) {
        int i9 = (i8 & 1) == 0 ? 400 : 700;
        boolean z4 = (i8 & 2) != 0;
        T t8 = null;
        int i10 = Integer.MAX_VALUE;
        for (T t9 : tArr) {
            int abs = (Math.abs(cVar.a(t9) - i9) * 2) + (cVar.b(t9) == z4 ? 0 : 1);
            if (t8 == null || i10 > abs) {
                t8 = t9;
                i10 = abs;
            }
        }
        return t8;
    }

    private static long j(Typeface typeface) {
        if (typeface == null) {
            return 0L;
        }
        try {
            Field declaredField = Typeface.class.getDeclaredField("native_instance");
            declaredField.setAccessible(true);
            return ((Number) declaredField.get(typeface)).longValue();
        } catch (IllegalAccessException e8) {
            Log.e("TypefaceCompatBaseImpl", "Could not retrieve font from family.", e8);
            return 0L;
        } catch (NoSuchFieldException e9) {
            Log.e("TypefaceCompatBaseImpl", "Could not retrieve font from family.", e9);
            return 0L;
        }
    }

    public Typeface b(Context context, e.c cVar, Resources resources, int i8) {
        e.d f5 = f(cVar, i8);
        if (f5 == null) {
            return null;
        }
        Typeface d8 = f.d(context, resources, f5.b(), f5.a(), 0, i8);
        a(d8, cVar);
        return d8;
    }

    public Typeface c(Context context, CancellationSignal cancellationSignal, g.b[] bVarArr, int i8) {
        InputStream inputStream;
        InputStream inputStream2 = null;
        if (bVarArr.length < 1) {
            return null;
        }
        try {
            inputStream = context.getContentResolver().openInputStream(h(bVarArr, i8).d());
        } catch (IOException unused) {
            inputStream = null;
        } catch (Throwable th) {
            th = th;
        }
        try {
            Typeface d8 = d(context, inputStream);
            m.a(inputStream);
            return d8;
        } catch (IOException unused2) {
            m.a(inputStream);
            return null;
        } catch (Throwable th2) {
            th = th2;
            inputStream2 = inputStream;
            m.a(inputStream2);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Typeface d(Context context, InputStream inputStream) {
        File e8 = m.e(context);
        if (e8 == null) {
            return null;
        }
        try {
            if (m.d(e8, inputStream)) {
                return Typeface.createFromFile(e8.getPath());
            }
            return null;
        } catch (RuntimeException unused) {
            return null;
        } finally {
            e8.delete();
        }
    }

    public Typeface e(Context context, Resources resources, int i8, String str, int i9) {
        File e8 = m.e(context);
        if (e8 == null) {
            return null;
        }
        try {
            if (m.c(e8, resources, i8)) {
                return Typeface.createFromFile(e8.getPath());
            }
            return null;
        } catch (RuntimeException unused) {
            return null;
        } finally {
            e8.delete();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public g.b h(g.b[] bVarArr, int i8) {
        return (g.b) g(bVarArr, i8, new a());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e.c i(Typeface typeface) {
        long j8 = j(typeface);
        if (j8 == 0) {
            return null;
        }
        return this.f4763a.get(Long.valueOf(j8));
    }
}
