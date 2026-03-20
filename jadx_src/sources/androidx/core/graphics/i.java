package androidx.core.graphics;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.FontVariationAxis;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import androidx.core.content.res.e;
import androidx.core.provider.g;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i extends g {

    /* renamed from: g  reason: collision with root package name */
    protected final Class<?> f4756g;

    /* renamed from: h  reason: collision with root package name */
    protected final Constructor<?> f4757h;

    /* renamed from: i  reason: collision with root package name */
    protected final Method f4758i;

    /* renamed from: j  reason: collision with root package name */
    protected final Method f4759j;

    /* renamed from: k  reason: collision with root package name */
    protected final Method f4760k;

    /* renamed from: l  reason: collision with root package name */
    protected final Method f4761l;

    /* renamed from: m  reason: collision with root package name */
    protected final Method f4762m;

    public i() {
        Method method;
        Constructor<?> constructor;
        Method method2;
        Method method3;
        Method method4;
        Method method5;
        Class<?> cls = null;
        try {
            Class<?> y8 = y();
            constructor = z(y8);
            method2 = v(y8);
            method3 = w(y8);
            method4 = A(y8);
            method5 = u(y8);
            method = x(y8);
            cls = y8;
        } catch (ClassNotFoundException | NoSuchMethodException e8) {
            Log.e("TypefaceCompatApi26Impl", "Unable to collect necessary methods for class " + e8.getClass().getName(), e8);
            method = null;
            constructor = null;
            method2 = null;
            method3 = null;
            method4 = null;
            method5 = null;
        }
        this.f4756g = cls;
        this.f4757h = constructor;
        this.f4758i = method2;
        this.f4759j = method3;
        this.f4760k = method4;
        this.f4761l = method5;
        this.f4762m = method;
    }

    private Object o() {
        try {
            return this.f4757h.newInstance(new Object[0]);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException unused) {
            return null;
        }
    }

    private void p(Object obj) {
        try {
            this.f4761l.invoke(obj, new Object[0]);
        } catch (IllegalAccessException | InvocationTargetException unused) {
        }
    }

    private boolean q(Context context, Object obj, String str, int i8, int i9, int i10, FontVariationAxis[] fontVariationAxisArr) {
        try {
            return ((Boolean) this.f4758i.invoke(obj, context.getAssets(), str, 0, Boolean.FALSE, Integer.valueOf(i8), Integer.valueOf(i9), Integer.valueOf(i10), fontVariationAxisArr)).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException unused) {
            return false;
        }
    }

    private boolean r(Object obj, ByteBuffer byteBuffer, int i8, int i9, int i10) {
        try {
            return ((Boolean) this.f4759j.invoke(obj, byteBuffer, Integer.valueOf(i8), null, Integer.valueOf(i9), Integer.valueOf(i10))).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException unused) {
            return false;
        }
    }

    private boolean s(Object obj) {
        try {
            return ((Boolean) this.f4760k.invoke(obj, new Object[0])).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException unused) {
            return false;
        }
    }

    private boolean t() {
        if (this.f4758i == null) {
            Log.w("TypefaceCompatApi26Impl", "Unable to collect necessary private methods. Fallback to legacy implementation.");
        }
        return this.f4758i != null;
    }

    protected Method A(Class<?> cls) {
        return cls.getMethod("freeze", new Class[0]);
    }

    @Override // androidx.core.graphics.g, androidx.core.graphics.l
    public Typeface b(Context context, e.c cVar, Resources resources, int i8) {
        e.d[] a9;
        if (t()) {
            Object o5 = o();
            if (o5 == null) {
                return null;
            }
            for (e.d dVar : cVar.a()) {
                if (!q(context, o5, dVar.a(), dVar.c(), dVar.e(), dVar.f() ? 1 : 0, FontVariationAxis.fromFontVariationSettings(dVar.d()))) {
                    p(o5);
                    return null;
                }
            }
            if (s(o5)) {
                return l(o5);
            }
            return null;
        }
        return super.b(context, cVar, resources, i8);
    }

    @Override // androidx.core.graphics.g, androidx.core.graphics.l
    public Typeface c(Context context, CancellationSignal cancellationSignal, g.b[] bVarArr, int i8) {
        Typeface l8;
        if (bVarArr.length < 1) {
            return null;
        }
        if (!t()) {
            g.b h8 = h(bVarArr, i8);
            try {
                ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(h8.d(), "r", cancellationSignal);
                if (openFileDescriptor == null) {
                    if (openFileDescriptor != null) {
                        openFileDescriptor.close();
                    }
                    return null;
                }
                Typeface build = new Typeface.Builder(openFileDescriptor.getFileDescriptor()).setWeight(h8.e()).setItalic(h8.f()).build();
                openFileDescriptor.close();
                return build;
            } catch (IOException unused) {
                return null;
            }
        }
        Map<Uri, ByteBuffer> h9 = m.h(context, bVarArr, cancellationSignal);
        Object o5 = o();
        if (o5 == null) {
            return null;
        }
        boolean z4 = false;
        for (g.b bVar : bVarArr) {
            ByteBuffer byteBuffer = h9.get(bVar.d());
            if (byteBuffer != null) {
                if (!r(o5, byteBuffer, bVar.c(), bVar.e(), bVar.f() ? 1 : 0)) {
                    p(o5);
                    return null;
                }
                z4 = true;
            }
        }
        if (!z4) {
            p(o5);
            return null;
        } else if (s(o5) && (l8 = l(o5)) != null) {
            return Typeface.create(l8, i8);
        } else {
            return null;
        }
    }

    @Override // androidx.core.graphics.l
    public Typeface e(Context context, Resources resources, int i8, String str, int i9) {
        if (t()) {
            Object o5 = o();
            if (o5 == null) {
                return null;
            }
            if (!q(context, o5, str, 0, -1, -1, null)) {
                p(o5);
                return null;
            } else if (s(o5)) {
                return l(o5);
            } else {
                return null;
            }
        }
        return super.e(context, resources, i8, str, i9);
    }

    protected Typeface l(Object obj) {
        try {
            Object newInstance = Array.newInstance(this.f4756g, 1);
            Array.set(newInstance, 0, obj);
            return (Typeface) this.f4762m.invoke(null, newInstance, -1, -1);
        } catch (IllegalAccessException | InvocationTargetException unused) {
            return null;
        }
    }

    protected Method u(Class<?> cls) {
        return cls.getMethod("abortCreation", new Class[0]);
    }

    protected Method v(Class<?> cls) {
        Class<?> cls2 = Integer.TYPE;
        return cls.getMethod("addFontFromAssetManager", AssetManager.class, String.class, cls2, Boolean.TYPE, cls2, cls2, cls2, FontVariationAxis[].class);
    }

    protected Method w(Class<?> cls) {
        Class<?> cls2 = Integer.TYPE;
        return cls.getMethod("addFontFromBuffer", ByteBuffer.class, cls2, FontVariationAxis[].class, cls2, cls2);
    }

    protected Method x(Class<?> cls) {
        Class cls2 = Integer.TYPE;
        Method declaredMethod = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", Array.newInstance(cls, 1).getClass(), cls2, cls2);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }

    protected Class<?> y() {
        return Class.forName("android.graphics.FontFamily");
    }

    protected Constructor<?> z(Class<?> cls) {
        return cls.getConstructor(new Class[0]);
    }
}
