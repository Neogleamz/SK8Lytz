package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.security.AccessController;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class s5 {

    /* renamed from: a  reason: collision with root package name */
    private static final Unsafe f14850a;

    /* renamed from: b  reason: collision with root package name */
    private static final Class f14851b;

    /* renamed from: c  reason: collision with root package name */
    private static final boolean f14852c;

    /* renamed from: d  reason: collision with root package name */
    private static final r5 f14853d;

    /* renamed from: e  reason: collision with root package name */
    private static final boolean f14854e;

    /* renamed from: f  reason: collision with root package name */
    private static final boolean f14855f;

    /* renamed from: g  reason: collision with root package name */
    static final long f14856g;

    /* renamed from: h  reason: collision with root package name */
    static final boolean f14857h;

    /* JADX WARN: Removed duplicated region for block: B:22:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0123  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0135  */
    static {
        /*
            Method dump skipped, instructions count: 313
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_vision_barcode_bundled.s5.<clinit>():void");
    }

    private s5() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    static boolean A(Class cls) {
        int i8 = b1.f14727a;
        try {
            Class cls2 = f14851b;
            Class cls3 = Boolean.TYPE;
            cls2.getMethod("peekLong", cls, cls3);
            cls2.getMethod("pokeLong", cls, Long.TYPE, cls3);
            Class cls4 = Integer.TYPE;
            cls2.getMethod("pokeInt", cls, cls4, cls3);
            cls2.getMethod("peekInt", cls, cls3);
            cls2.getMethod("pokeByte", cls, Byte.TYPE);
            cls2.getMethod("peekByte", cls);
            cls2.getMethod("pokeByteArray", cls, byte[].class, cls4, cls4);
            cls2.getMethod("peekByteArray", cls, byte[].class, cls4, cls4);
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean B(Object obj, long j8) {
        return f14853d.g(obj, j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean C() {
        return f14855f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean D() {
        return f14854e;
    }

    private static int E(Class cls) {
        if (f14855f) {
            return f14853d.f14848a.arrayBaseOffset(cls);
        }
        return -1;
    }

    private static int a(Class cls) {
        if (f14855f) {
            return f14853d.f14848a.arrayIndexScale(cls);
        }
        return -1;
    }

    private static Field b() {
        int i8 = b1.f14727a;
        Field c9 = c(Buffer.class, "effectiveDirectAddress");
        if (c9 == null) {
            Field c10 = c(Buffer.class, "address");
            if (c10 == null || c10.getType() != Long.TYPE) {
                return null;
            }
            return c10;
        }
        return c9;
    }

    private static Field c(Class cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (Throwable unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void d(Object obj, long j8, byte b9) {
        r5 r5Var = f14853d;
        long j9 = (-4) & j8;
        int i8 = r5Var.f14848a.getInt(obj, j9);
        int i9 = ((~((int) j8)) & 3) << 3;
        r5Var.f14848a.putInt(obj, j9, ((255 & b9) << i9) | (i8 & (~(255 << i9))));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void e(Object obj, long j8, byte b9) {
        r5 r5Var = f14853d;
        long j9 = (-4) & j8;
        int i8 = (((int) j8) & 3) << 3;
        r5Var.f14848a.putInt(obj, j9, ((255 & b9) << i8) | (r5Var.f14848a.getInt(obj, j9) & (~(255 << i8))));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static double f(Object obj, long j8) {
        return f14853d.a(obj, j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float g(Object obj, long j8) {
        return f14853d.b(obj, j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int h(Object obj, long j8) {
        return f14853d.f14848a.getInt(obj, j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long i(Object obj, long j8) {
        return f14853d.f14848a.getLong(obj, j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object j(Class cls) {
        try {
            return f14850a.allocateInstance(cls);
        } catch (InstantiationException e8) {
            throw new IllegalStateException(e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object k(Object obj, long j8) {
        return f14853d.f14848a.getObject(obj, j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Unsafe l() {
        try {
            return (Unsafe) AccessController.doPrivileged(new o5());
        } catch (Throwable unused) {
            return null;
        }
    }

    static /* bridge */ /* synthetic */ void m(Throwable th) {
        Logger.getLogger(s5.class.getName()).logp(Level.WARNING, "com.google.protobuf.UnsafeUtil", "logMissingMethod", "platform method missing - proto runtime falling back to safer methods: ".concat(th.toString()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void r(Object obj, long j8, boolean z4) {
        f14853d.c(obj, j8, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void s(byte[] bArr, long j8, byte b9) {
        f14853d.d(bArr, f14856g + j8, b9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void t(Object obj, long j8, double d8) {
        f14853d.e(obj, j8, d8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void u(Object obj, long j8, float f5) {
        f14853d.f(obj, j8, f5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void v(Object obj, long j8, int i8) {
        f14853d.f14848a.putInt(obj, j8, i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void w(Object obj, long j8, long j9) {
        f14853d.f14848a.putLong(obj, j8, j9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void x(Object obj, long j8, Object obj2) {
        f14853d.f14848a.putObject(obj, j8, obj2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ boolean y(Object obj, long j8) {
        return ((byte) ((f14853d.f14848a.getInt(obj, (-4) & j8) >>> ((int) (((~j8) & 3) << 3))) & 255)) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ boolean z(Object obj, long j8) {
        return ((byte) ((f14853d.f14848a.getInt(obj, (-4) & j8) >>> ((int) ((j8 & 3) << 3))) & 255)) != 0;
    }
}
