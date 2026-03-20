package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.core.content.res.e;
import androidx.core.provider.g;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class h extends l {

    /* renamed from: b  reason: collision with root package name */
    private static final Class<?> f4752b;

    /* renamed from: c  reason: collision with root package name */
    private static final Constructor<?> f4753c;

    /* renamed from: d  reason: collision with root package name */
    private static final Method f4754d;

    /* renamed from: e  reason: collision with root package name */
    private static final Method f4755e;

    static {
        Class<?> cls;
        Method method;
        Method method2;
        Constructor<?> constructor = null;
        try {
            cls = Class.forName("android.graphics.FontFamily");
            Constructor<?> constructor2 = cls.getConstructor(new Class[0]);
            Class<?> cls2 = Integer.TYPE;
            method2 = cls.getMethod("addFontWeightStyle", ByteBuffer.class, cls2, List.class, cls2, Boolean.TYPE);
            method = Typeface.class.getMethod("createFromFamiliesWithDefault", Array.newInstance(cls, 1).getClass());
            constructor = constructor2;
        } catch (ClassNotFoundException | NoSuchMethodException e8) {
            Log.e("TypefaceCompatApi24Impl", e8.getClass().getName(), e8);
            cls = null;
            method = null;
            method2 = null;
        }
        f4753c = constructor;
        f4752b = cls;
        f4754d = method2;
        f4755e = method;
    }

    private static boolean k(Object obj, ByteBuffer byteBuffer, int i8, int i9, boolean z4) {
        try {
            return ((Boolean) f4754d.invoke(obj, byteBuffer, Integer.valueOf(i8), null, Integer.valueOf(i9), Boolean.valueOf(z4))).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException unused) {
            return false;
        }
    }

    private static Typeface l(Object obj) {
        try {
            Object newInstance = Array.newInstance(f4752b, 1);
            Array.set(newInstance, 0, obj);
            return (Typeface) f4755e.invoke(null, newInstance);
        } catch (IllegalAccessException | InvocationTargetException unused) {
            return null;
        }
    }

    public static boolean m() {
        Method method = f4754d;
        if (method == null) {
            Log.w("TypefaceCompatApi24Impl", "Unable to collect necessary private methods.Fallback to legacy implementation.");
        }
        return method != null;
    }

    private static Object n() {
        try {
            return f4753c.newInstance(new Object[0]);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException unused) {
            return null;
        }
    }

    @Override // androidx.core.graphics.l
    public Typeface b(Context context, e.c cVar, Resources resources, int i8) {
        e.d[] a9;
        Object n8 = n();
        if (n8 == null) {
            return null;
        }
        for (e.d dVar : cVar.a()) {
            ByteBuffer b9 = m.b(context, resources, dVar.b());
            if (b9 == null || !k(n8, b9, dVar.c(), dVar.e(), dVar.f())) {
                return null;
            }
        }
        return l(n8);
    }

    @Override // androidx.core.graphics.l
    public Typeface c(Context context, CancellationSignal cancellationSignal, g.b[] bVarArr, int i8) {
        Object n8 = n();
        if (n8 == null) {
            return null;
        }
        k0.g gVar = new k0.g();
        for (g.b bVar : bVarArr) {
            Uri d8 = bVar.d();
            ByteBuffer byteBuffer = (ByteBuffer) gVar.get(d8);
            if (byteBuffer == null) {
                byteBuffer = m.f(context, cancellationSignal, d8);
                gVar.put(d8, byteBuffer);
            }
            if (byteBuffer == null || !k(n8, byteBuffer, bVar.c(), bVar.e(), bVar.f())) {
                return null;
            }
        }
        Typeface l8 = l(n8);
        if (l8 == null) {
            return null;
        }
        return Typeface.create(l8, i8);
    }
}
