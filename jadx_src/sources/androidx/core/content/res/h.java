package androidx.core.content.res;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import androidx.core.content.res.h;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.WeakHashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h {

    /* renamed from: a  reason: collision with root package name */
    private static final ThreadLocal<TypedValue> f4661a = new ThreadLocal<>();

    /* renamed from: b  reason: collision with root package name */
    private static final WeakHashMap<e, SparseArray<d>> f4662b = new WeakHashMap<>(0);

    /* renamed from: c  reason: collision with root package name */
    private static final Object f4663c = new Object();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static Drawable a(Resources resources, int i8, int i9) {
            return resources.getDrawableForDensity(i8, i9);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b {
        static Drawable a(Resources resources, int i8, Resources.Theme theme) {
            return resources.getDrawable(i8, theme);
        }

        static Drawable b(Resources resources, int i8, int i9, Resources.Theme theme) {
            return resources.getDrawableForDensity(i8, i9, theme);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {
        static int a(Resources resources, int i8, Resources.Theme theme) {
            return resources.getColor(i8, theme);
        }

        static ColorStateList b(Resources resources, int i8, Resources.Theme theme) {
            return resources.getColorStateList(i8, theme);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d {

        /* renamed from: a  reason: collision with root package name */
        final ColorStateList f4664a;

        /* renamed from: b  reason: collision with root package name */
        final Configuration f4665b;

        /* renamed from: c  reason: collision with root package name */
        final int f4666c;

        d(ColorStateList colorStateList, Configuration configuration, Resources.Theme theme) {
            this.f4664a = colorStateList;
            this.f4665b = configuration;
            this.f4666c = theme == null ? 0 : theme.hashCode();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: a  reason: collision with root package name */
        final Resources f4667a;

        /* renamed from: b  reason: collision with root package name */
        final Resources.Theme f4668b;

        e(Resources resources, Resources.Theme theme) {
            this.f4667a = resources;
            this.f4668b = theme;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || e.class != obj.getClass()) {
                return false;
            }
            e eVar = (e) obj;
            return this.f4667a.equals(eVar.f4667a) && androidx.core.util.c.a(this.f4668b, eVar.f4668b);
        }

        public int hashCode() {
            return androidx.core.util.c.b(this.f4667a, this.f4668b);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class f {
        public static Handler e(Handler handler) {
            return handler == null ? new Handler(Looper.getMainLooper()) : handler;
        }

        public final void c(final int i8, Handler handler) {
            e(handler).post(new Runnable() { // from class: androidx.core.content.res.i
                @Override // java.lang.Runnable
                public final void run() {
                    h.f.this.f(i8);
                }
            });
        }

        public final void d(final Typeface typeface, Handler handler) {
            e(handler).post(new Runnable() { // from class: androidx.core.content.res.j
                @Override // java.lang.Runnable
                public final void run() {
                    h.f.this.g(typeface);
                }
            });
        }

        /* renamed from: h */
        public abstract void f(int i8);

        /* renamed from: i */
        public abstract void g(Typeface typeface);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a {

            /* renamed from: a  reason: collision with root package name */
            private static final Object f4669a = new Object();

            /* renamed from: b  reason: collision with root package name */
            private static Method f4670b;

            /* renamed from: c  reason: collision with root package name */
            private static boolean f4671c;

            @SuppressLint({"BanUncheckedReflection"})
            static void a(Resources.Theme theme) {
                synchronized (f4669a) {
                    if (!f4671c) {
                        try {
                            Method declaredMethod = Resources.Theme.class.getDeclaredMethod("rebase", new Class[0]);
                            f4670b = declaredMethod;
                            declaredMethod.setAccessible(true);
                        } catch (NoSuchMethodException e8) {
                            Log.i("ResourcesCompat", "Failed to retrieve rebase() method", e8);
                        }
                        f4671c = true;
                    }
                    Method method = f4670b;
                    if (method != null) {
                        try {
                            method.invoke(theme, new Object[0]);
                        } catch (IllegalAccessException | InvocationTargetException e9) {
                            Log.i("ResourcesCompat", "Failed to invoke rebase() method via reflection", e9);
                            f4670b = null;
                        }
                    }
                }
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class b {
            static void a(Resources.Theme theme) {
                theme.rebase();
            }
        }

        public static void a(Resources.Theme theme) {
            int i8 = Build.VERSION.SDK_INT;
            if (i8 >= 29) {
                b.a(theme);
            } else if (i8 >= 23) {
                a.a(theme);
            }
        }
    }

    private static void a(e eVar, int i8, ColorStateList colorStateList, Resources.Theme theme) {
        synchronized (f4663c) {
            WeakHashMap<e, SparseArray<d>> weakHashMap = f4662b;
            SparseArray<d> sparseArray = weakHashMap.get(eVar);
            if (sparseArray == null) {
                sparseArray = new SparseArray<>();
                weakHashMap.put(eVar, sparseArray);
            }
            sparseArray.append(i8, new d(colorStateList, eVar.f4667a.getConfiguration(), theme));
        }
    }

    private static ColorStateList b(e eVar, int i8) {
        d dVar;
        Resources.Theme theme;
        synchronized (f4663c) {
            SparseArray<d> sparseArray = f4662b.get(eVar);
            if (sparseArray != null && sparseArray.size() > 0 && (dVar = sparseArray.get(i8)) != null) {
                if (dVar.f4665b.equals(eVar.f4667a.getConfiguration()) && (((theme = eVar.f4668b) == null && dVar.f4666c == 0) || (theme != null && dVar.f4666c == theme.hashCode()))) {
                    return dVar.f4664a;
                }
                sparseArray.remove(i8);
            }
            return null;
        }
    }

    public static Typeface c(Context context, int i8) {
        if (context.isRestricted()) {
            return null;
        }
        return m(context, i8, new TypedValue(), 0, null, null, false, true);
    }

    public static ColorStateList d(Resources resources, int i8, Resources.Theme theme) {
        e eVar = new e(resources, theme);
        ColorStateList b9 = b(eVar, i8);
        if (b9 != null) {
            return b9;
        }
        ColorStateList k8 = k(resources, i8, theme);
        if (k8 == null) {
            return Build.VERSION.SDK_INT >= 23 ? c.b(resources, i8, theme) : resources.getColorStateList(i8);
        }
        a(eVar, i8, k8, theme);
        return k8;
    }

    public static Drawable e(Resources resources, int i8, Resources.Theme theme) {
        return Build.VERSION.SDK_INT >= 21 ? b.a(resources, i8, theme) : resources.getDrawable(i8);
    }

    public static Drawable f(Resources resources, int i8, int i9, Resources.Theme theme) {
        int i10 = Build.VERSION.SDK_INT;
        return i10 >= 21 ? b.b(resources, i8, i9, theme) : i10 >= 15 ? a.a(resources, i8, i9) : resources.getDrawable(i8);
    }

    public static Typeface g(Context context, int i8) {
        if (context.isRestricted()) {
            return null;
        }
        return m(context, i8, new TypedValue(), 0, null, null, false, false);
    }

    public static Typeface h(Context context, int i8, TypedValue typedValue, int i9, f fVar) {
        if (context.isRestricted()) {
            return null;
        }
        return m(context, i8, typedValue, i9, fVar, null, true, false);
    }

    public static void i(Context context, int i8, f fVar, Handler handler) {
        androidx.core.util.h.h(fVar);
        if (context.isRestricted()) {
            fVar.c(-4, handler);
        } else {
            m(context, i8, new TypedValue(), 0, fVar, handler, false, false);
        }
    }

    private static TypedValue j() {
        ThreadLocal<TypedValue> threadLocal = f4661a;
        TypedValue typedValue = threadLocal.get();
        if (typedValue == null) {
            TypedValue typedValue2 = new TypedValue();
            threadLocal.set(typedValue2);
            return typedValue2;
        }
        return typedValue;
    }

    private static ColorStateList k(Resources resources, int i8, Resources.Theme theme) {
        if (l(resources, i8)) {
            return null;
        }
        try {
            return androidx.core.content.res.c.a(resources, resources.getXml(i8), theme);
        } catch (Exception e8) {
            Log.w("ResourcesCompat", "Failed to inflate ColorStateList, leaving it to the framework", e8);
            return null;
        }
    }

    private static boolean l(Resources resources, int i8) {
        TypedValue j8 = j();
        resources.getValue(i8, j8, true);
        int i9 = j8.type;
        return i9 >= 28 && i9 <= 31;
    }

    private static Typeface m(Context context, int i8, TypedValue typedValue, int i9, f fVar, Handler handler, boolean z4, boolean z8) {
        Resources resources = context.getResources();
        resources.getValue(i8, typedValue, true);
        Typeface n8 = n(context, resources, typedValue, i8, i9, fVar, handler, z4, z8);
        if (n8 == null && fVar == null && !z8) {
            throw new Resources.NotFoundException("Font resource ID #0x" + Integer.toHexString(i8) + " could not be retrieved.");
        }
        return n8;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x00aa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.graphics.Typeface n(android.content.Context r17, android.content.res.Resources r18, android.util.TypedValue r19, int r20, int r21, androidx.core.content.res.h.f r22, android.os.Handler r23, boolean r24, boolean r25) {
        /*
            r0 = r18
            r1 = r19
            r4 = r20
            r11 = r22
            r12 = r23
            java.lang.String r13 = "ResourcesCompat"
            java.lang.CharSequence r2 = r1.string
            if (r2 == 0) goto Lae
            java.lang.String r14 = r2.toString()
            java.lang.String r2 = "res/"
            boolean r2 = r14.startsWith(r2)
            r15 = -3
            r16 = 0
            if (r2 != 0) goto L25
            if (r11 == 0) goto L24
            r11.c(r15, r12)
        L24:
            return r16
        L25:
            int r2 = r1.assetCookie
            r7 = r21
            android.graphics.Typeface r2 = androidx.core.graphics.f.f(r0, r4, r14, r2, r7)
            if (r2 == 0) goto L35
            if (r11 == 0) goto L34
            r11.d(r2, r12)
        L34:
            return r2
        L35:
            if (r25 == 0) goto L38
            return r16
        L38:
            java.lang.String r2 = r14.toLowerCase()     // Catch: java.io.IOException -> L8a org.xmlpull.v1.XmlPullParserException -> L93
            java.lang.String r3 = ".xml"
            boolean r2 = r2.endsWith(r3)     // Catch: java.io.IOException -> L8a org.xmlpull.v1.XmlPullParserException -> L93
            if (r2 == 0) goto L6f
            android.content.res.XmlResourceParser r2 = r0.getXml(r4)     // Catch: java.io.IOException -> L8a org.xmlpull.v1.XmlPullParserException -> L93
            androidx.core.content.res.e$b r2 = androidx.core.content.res.e.b(r2, r0)     // Catch: java.io.IOException -> L8a org.xmlpull.v1.XmlPullParserException -> L93
            if (r2 != 0) goto L59
            java.lang.String r0 = "Failed to find font-family tag"
            android.util.Log.e(r13, r0)     // Catch: java.io.IOException -> L8a org.xmlpull.v1.XmlPullParserException -> L93
            if (r11 == 0) goto L58
            r11.c(r15, r12)     // Catch: java.io.IOException -> L8a org.xmlpull.v1.XmlPullParserException -> L93
        L58:
            return r16
        L59:
            int r6 = r1.assetCookie     // Catch: java.io.IOException -> L8a org.xmlpull.v1.XmlPullParserException -> L93
            r1 = r17
            r3 = r18
            r4 = r20
            r5 = r14
            r7 = r21
            r8 = r22
            r9 = r23
            r10 = r24
            android.graphics.Typeface r0 = androidx.core.graphics.f.c(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)     // Catch: java.io.IOException -> L8a org.xmlpull.v1.XmlPullParserException -> L93
            return r0
        L6f:
            int r5 = r1.assetCookie     // Catch: java.io.IOException -> L8a org.xmlpull.v1.XmlPullParserException -> L93
            r1 = r17
            r2 = r18
            r3 = r20
            r4 = r14
            r6 = r21
            android.graphics.Typeface r0 = androidx.core.graphics.f.d(r1, r2, r3, r4, r5, r6)     // Catch: java.io.IOException -> L8a org.xmlpull.v1.XmlPullParserException -> L93
            if (r11 == 0) goto L89
            if (r0 == 0) goto L86
            r11.d(r0, r12)     // Catch: java.io.IOException -> L8a org.xmlpull.v1.XmlPullParserException -> L93
            goto L89
        L86:
            r11.c(r15, r12)     // Catch: java.io.IOException -> L8a org.xmlpull.v1.XmlPullParserException -> L93
        L89:
            return r0
        L8a:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Failed to read xml resource "
            goto L9b
        L93:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Failed to parse xml resource "
        L9b:
            r1.append(r2)
            r1.append(r14)
            java.lang.String r1 = r1.toString()
            android.util.Log.e(r13, r1, r0)
            if (r11 == 0) goto Lad
            r11.c(r15, r12)
        Lad:
            return r16
        Lae:
            android.content.res.Resources$NotFoundException r2 = new android.content.res.Resources$NotFoundException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "Resource \""
            r3.append(r5)
            java.lang.String r0 = r0.getResourceName(r4)
            r3.append(r0)
            java.lang.String r0 = "\" ("
            r3.append(r0)
            java.lang.String r0 = java.lang.Integer.toHexString(r20)
            r3.append(r0)
            java.lang.String r0 = ") is not a Font: "
            r3.append(r0)
            r3.append(r1)
            java.lang.String r0 = r3.toString()
            r2.<init>(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.res.h.n(android.content.Context, android.content.res.Resources, android.util.TypedValue, int, int, androidx.core.content.res.h$f, android.os.Handler, boolean, boolean):android.graphics.Typeface");
    }
}
