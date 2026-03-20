package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import androidx.core.content.res.e;
import androidx.core.provider.g;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class g extends l {

    /* renamed from: b  reason: collision with root package name */
    private static Class<?> f4747b = null;

    /* renamed from: c  reason: collision with root package name */
    private static Constructor<?> f4748c = null;

    /* renamed from: d  reason: collision with root package name */
    private static Method f4749d = null;

    /* renamed from: e  reason: collision with root package name */
    private static Method f4750e = null;

    /* renamed from: f  reason: collision with root package name */
    private static boolean f4751f = false;

    private static boolean k(Object obj, String str, int i8, boolean z4) {
        n();
        try {
            return ((Boolean) f4749d.invoke(obj, str, Integer.valueOf(i8), Boolean.valueOf(z4))).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException e8) {
            throw new RuntimeException(e8);
        }
    }

    private static Typeface l(Object obj) {
        n();
        try {
            Object newInstance = Array.newInstance(f4747b, 1);
            Array.set(newInstance, 0, obj);
            return (Typeface) f4750e.invoke(null, newInstance);
        } catch (IllegalAccessException | InvocationTargetException e8) {
            throw new RuntimeException(e8);
        }
    }

    private File m(ParcelFileDescriptor parcelFileDescriptor) {
        try {
            String readlink = Os.readlink("/proc/self/fd/" + parcelFileDescriptor.getFd());
            if (OsConstants.S_ISREG(Os.stat(readlink).st_mode)) {
                return new File(readlink);
            }
        } catch (ErrnoException unused) {
        }
        return null;
    }

    private static void n() {
        Method method;
        Class<?> cls;
        Method method2;
        if (f4751f) {
            return;
        }
        f4751f = true;
        Constructor<?> constructor = null;
        try {
            cls = Class.forName("android.graphics.FontFamily");
            Constructor<?> constructor2 = cls.getConstructor(new Class[0]);
            method2 = cls.getMethod("addFontWeightStyle", String.class, Integer.TYPE, Boolean.TYPE);
            method = Typeface.class.getMethod("createFromFamiliesWithDefault", Array.newInstance(cls, 1).getClass());
            constructor = constructor2;
        } catch (ClassNotFoundException | NoSuchMethodException e8) {
            Log.e("TypefaceCompatApi21Impl", e8.getClass().getName(), e8);
            method = null;
            cls = null;
            method2 = null;
        }
        f4748c = constructor;
        f4747b = cls;
        f4749d = method2;
        f4750e = method;
    }

    private static Object o() {
        n();
        try {
            return f4748c.newInstance(new Object[0]);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e8) {
            throw new RuntimeException(e8);
        }
    }

    @Override // androidx.core.graphics.l
    public Typeface b(Context context, e.c cVar, Resources resources, int i8) {
        e.d[] a9;
        Object o5 = o();
        for (e.d dVar : cVar.a()) {
            File e8 = m.e(context);
            if (e8 == null) {
                return null;
            }
            try {
                if (!m.c(e8, resources, dVar.b())) {
                    return null;
                }
                if (!k(o5, e8.getPath(), dVar.e(), dVar.f())) {
                    return null;
                }
            } catch (RuntimeException unused) {
                return null;
            } finally {
                e8.delete();
            }
        }
        return l(o5);
    }

    @Override // androidx.core.graphics.l
    public Typeface c(Context context, CancellationSignal cancellationSignal, g.b[] bVarArr, int i8) {
        if (bVarArr.length < 1) {
            return null;
        }
        g.b h8 = h(bVarArr, i8);
        try {
            ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(h8.d(), "r", cancellationSignal);
            if (openFileDescriptor == null) {
                if (openFileDescriptor != null) {
                    openFileDescriptor.close();
                }
                return null;
            }
            File m8 = m(openFileDescriptor);
            if (m8 != null && m8.canRead()) {
                Typeface createFromFile = Typeface.createFromFile(m8);
                openFileDescriptor.close();
                return createFromFile;
            }
            FileInputStream fileInputStream = new FileInputStream(openFileDescriptor.getFileDescriptor());
            Typeface d8 = super.d(context, fileInputStream);
            fileInputStream.close();
            openFileDescriptor.close();
            return d8;
        } catch (IOException unused) {
            return null;
        }
    }
}
