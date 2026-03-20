package com.google.android.gms.internal.measurement;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.security.AccessController;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class yb {

    /* renamed from: a  reason: collision with root package name */
    private static final Unsafe f12694a;

    /* renamed from: b  reason: collision with root package name */
    private static final Class<?> f12695b;

    /* renamed from: c  reason: collision with root package name */
    private static final boolean f12696c;

    /* renamed from: d  reason: collision with root package name */
    private static final boolean f12697d;

    /* renamed from: e  reason: collision with root package name */
    private static final b f12698e;

    /* renamed from: f  reason: collision with root package name */
    private static final boolean f12699f;

    /* renamed from: g  reason: collision with root package name */
    private static final boolean f12700g;

    /* renamed from: h  reason: collision with root package name */
    private static final long f12701h;

    /* renamed from: i  reason: collision with root package name */
    private static final long f12702i;

    /* renamed from: j  reason: collision with root package name */
    private static final long f12703j;

    /* renamed from: k  reason: collision with root package name */
    private static final long f12704k;

    /* renamed from: l  reason: collision with root package name */
    private static final long f12705l;

    /* renamed from: m  reason: collision with root package name */
    private static final long f12706m;

    /* renamed from: n  reason: collision with root package name */
    private static final long f12707n;

    /* renamed from: o  reason: collision with root package name */
    private static final long f12708o;

    /* renamed from: p  reason: collision with root package name */
    private static final long f12709p;
    private static final long q;

    /* renamed from: r  reason: collision with root package name */
    private static final long f12710r;

    /* renamed from: s  reason: collision with root package name */
    private static final long f12711s;

    /* renamed from: t  reason: collision with root package name */
    private static final long f12712t;

    /* renamed from: u  reason: collision with root package name */
    private static final long f12713u;

    /* renamed from: v  reason: collision with root package name */
    private static final int f12714v;

    /* renamed from: w  reason: collision with root package name */
    static final boolean f12715w;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a extends b {
        a(Unsafe unsafe) {
            super(unsafe);
        }

        @Override // com.google.android.gms.internal.measurement.yb.b
        public final double a(Object obj, long j8) {
            return Double.longBitsToDouble(m(obj, j8));
        }

        @Override // com.google.android.gms.internal.measurement.yb.b
        public final void b(Object obj, long j8, byte b9) {
            if (yb.f12715w) {
                yb.u(obj, j8, b9);
            } else {
                yb.y(obj, j8, b9);
            }
        }

        @Override // com.google.android.gms.internal.measurement.yb.b
        public final void c(Object obj, long j8, double d8) {
            f(obj, j8, Double.doubleToLongBits(d8));
        }

        @Override // com.google.android.gms.internal.measurement.yb.b
        public final void d(Object obj, long j8, float f5) {
            e(obj, j8, Float.floatToIntBits(f5));
        }

        @Override // com.google.android.gms.internal.measurement.yb.b
        public final void g(Object obj, long j8, boolean z4) {
            if (yb.f12715w) {
                yb.k(obj, j8, z4);
            } else {
                yb.r(obj, j8, z4);
            }
        }

        @Override // com.google.android.gms.internal.measurement.yb.b
        public final float i(Object obj, long j8) {
            return Float.intBitsToFloat(l(obj, j8));
        }

        @Override // com.google.android.gms.internal.measurement.yb.b
        public final boolean k(Object obj, long j8) {
            return yb.f12715w ? yb.D(obj, j8) : yb.E(obj, j8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class b {

        /* renamed from: a  reason: collision with root package name */
        Unsafe f12716a;

        b(Unsafe unsafe) {
            this.f12716a = unsafe;
        }

        public abstract double a(Object obj, long j8);

        public abstract void b(Object obj, long j8, byte b9);

        public abstract void c(Object obj, long j8, double d8);

        public abstract void d(Object obj, long j8, float f5);

        public final void e(Object obj, long j8, int i8) {
            this.f12716a.putInt(obj, j8, i8);
        }

        public final void f(Object obj, long j8, long j9) {
            this.f12716a.putLong(obj, j8, j9);
        }

        public abstract void g(Object obj, long j8, boolean z4);

        public final boolean h() {
            Unsafe unsafe = this.f12716a;
            if (unsafe == null) {
                return false;
            }
            try {
                Class<?> cls = unsafe.getClass();
                cls.getMethod("objectFieldOffset", Field.class);
                cls.getMethod("arrayBaseOffset", Class.class);
                cls.getMethod("arrayIndexScale", Class.class);
                Class<?> cls2 = Long.TYPE;
                cls.getMethod("getInt", Object.class, cls2);
                cls.getMethod("putInt", Object.class, cls2, Integer.TYPE);
                cls.getMethod("getLong", Object.class, cls2);
                cls.getMethod("putLong", Object.class, cls2, cls2);
                cls.getMethod("getObject", Object.class, cls2);
                cls.getMethod("putObject", Object.class, cls2, Object.class);
                return true;
            } catch (Throwable th) {
                yb.l(th);
                return false;
            }
        }

        public abstract float i(Object obj, long j8);

        public final boolean j() {
            Unsafe unsafe = this.f12716a;
            if (unsafe == null) {
                return false;
            }
            try {
                Class<?> cls = unsafe.getClass();
                cls.getMethod("objectFieldOffset", Field.class);
                cls.getMethod("getLong", Object.class, Long.TYPE);
                return yb.c() != null;
            } catch (Throwable th) {
                yb.l(th);
                return false;
            }
        }

        public abstract boolean k(Object obj, long j8);

        public final int l(Object obj, long j8) {
            return this.f12716a.getInt(obj, j8);
        }

        public final long m(Object obj, long j8) {
            return this.f12716a.getLong(obj, j8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class c extends b {
        c(Unsafe unsafe) {
            super(unsafe);
        }

        @Override // com.google.android.gms.internal.measurement.yb.b
        public final double a(Object obj, long j8) {
            return Double.longBitsToDouble(m(obj, j8));
        }

        @Override // com.google.android.gms.internal.measurement.yb.b
        public final void b(Object obj, long j8, byte b9) {
            if (yb.f12715w) {
                yb.u(obj, j8, b9);
            } else {
                yb.y(obj, j8, b9);
            }
        }

        @Override // com.google.android.gms.internal.measurement.yb.b
        public final void c(Object obj, long j8, double d8) {
            f(obj, j8, Double.doubleToLongBits(d8));
        }

        @Override // com.google.android.gms.internal.measurement.yb.b
        public final void d(Object obj, long j8, float f5) {
            e(obj, j8, Float.floatToIntBits(f5));
        }

        @Override // com.google.android.gms.internal.measurement.yb.b
        public final void g(Object obj, long j8, boolean z4) {
            if (yb.f12715w) {
                yb.k(obj, j8, z4);
            } else {
                yb.r(obj, j8, z4);
            }
        }

        @Override // com.google.android.gms.internal.measurement.yb.b
        public final float i(Object obj, long j8) {
            return Float.intBitsToFloat(l(obj, j8));
        }

        @Override // com.google.android.gms.internal.measurement.yb.b
        public final boolean k(Object obj, long j8) {
            return yb.f12715w ? yb.D(obj, j8) : yb.E(obj, j8);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00d1  */
    static {
        /*
            java.lang.Class<java.lang.Object[]> r0 = java.lang.Object[].class
            java.lang.Class<double[]> r1 = double[].class
            java.lang.Class<float[]> r2 = float[].class
            java.lang.Class<long[]> r3 = long[].class
            java.lang.Class<int[]> r4 = int[].class
            java.lang.Class<boolean[]> r5 = boolean[].class
            sun.misc.Unsafe r6 = p()
            com.google.android.gms.internal.measurement.yb.f12694a = r6
            java.lang.Class r7 = com.google.android.gms.internal.measurement.k7.a()
            com.google.android.gms.internal.measurement.yb.f12695b = r7
            java.lang.Class r7 = java.lang.Long.TYPE
            boolean r7 = A(r7)
            com.google.android.gms.internal.measurement.yb.f12696c = r7
            java.lang.Class r8 = java.lang.Integer.TYPE
            boolean r8 = A(r8)
            com.google.android.gms.internal.measurement.yb.f12697d = r8
            if (r6 == 0) goto L3a
            if (r7 == 0) goto L32
            com.google.android.gms.internal.measurement.yb$c r7 = new com.google.android.gms.internal.measurement.yb$c
            r7.<init>(r6)
            goto L3b
        L32:
            if (r8 == 0) goto L3a
            com.google.android.gms.internal.measurement.yb$a r7 = new com.google.android.gms.internal.measurement.yb$a
            r7.<init>(r6)
            goto L3b
        L3a:
            r7 = 0
        L3b:
            com.google.android.gms.internal.measurement.yb.f12698e = r7
            r6 = 0
            if (r7 != 0) goto L42
            r8 = r6
            goto L46
        L42:
            boolean r8 = r7.j()
        L46:
            com.google.android.gms.internal.measurement.yb.f12699f = r8
            if (r7 != 0) goto L4c
            r8 = r6
            goto L50
        L4c:
            boolean r8 = r7.h()
        L50:
            com.google.android.gms.internal.measurement.yb.f12700g = r8
            java.lang.Class<byte[]> r8 = byte[].class
            int r8 = o(r8)
            long r8 = (long) r8
            com.google.android.gms.internal.measurement.yb.f12701h = r8
            int r10 = o(r5)
            long r10 = (long) r10
            com.google.android.gms.internal.measurement.yb.f12702i = r10
            int r5 = s(r5)
            long r10 = (long) r5
            com.google.android.gms.internal.measurement.yb.f12703j = r10
            int r5 = o(r4)
            long r10 = (long) r5
            com.google.android.gms.internal.measurement.yb.f12704k = r10
            int r4 = s(r4)
            long r4 = (long) r4
            com.google.android.gms.internal.measurement.yb.f12705l = r4
            int r4 = o(r3)
            long r4 = (long) r4
            com.google.android.gms.internal.measurement.yb.f12706m = r4
            int r3 = s(r3)
            long r3 = (long) r3
            com.google.android.gms.internal.measurement.yb.f12707n = r3
            int r3 = o(r2)
            long r3 = (long) r3
            com.google.android.gms.internal.measurement.yb.f12708o = r3
            int r2 = s(r2)
            long r2 = (long) r2
            com.google.android.gms.internal.measurement.yb.f12709p = r2
            int r2 = o(r1)
            long r2 = (long) r2
            com.google.android.gms.internal.measurement.yb.q = r2
            int r1 = s(r1)
            long r1 = (long) r1
            com.google.android.gms.internal.measurement.yb.f12710r = r1
            int r1 = o(r0)
            long r1 = (long) r1
            com.google.android.gms.internal.measurement.yb.f12711s = r1
            int r0 = s(r0)
            long r0 = (long) r0
            com.google.android.gms.internal.measurement.yb.f12712t = r0
            java.lang.reflect.Field r0 = C()
            if (r0 == 0) goto Lbf
            if (r7 != 0) goto Lb8
            goto Lbf
        Lb8:
            sun.misc.Unsafe r1 = r7.f12716a
            long r0 = r1.objectFieldOffset(r0)
            goto Lc1
        Lbf:
            r0 = -1
        Lc1:
            com.google.android.gms.internal.measurement.yb.f12713u = r0
            r0 = 7
            long r0 = r0 & r8
            int r0 = (int) r0
            com.google.android.gms.internal.measurement.yb.f12714v = r0
            java.nio.ByteOrder r0 = java.nio.ByteOrder.nativeOrder()
            java.nio.ByteOrder r1 = java.nio.ByteOrder.BIG_ENDIAN
            if (r0 != r1) goto Ld2
            r6 = 1
        Ld2:
            com.google.android.gms.internal.measurement.yb.f12715w = r6
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.yb.<clinit>():void");
    }

    private yb() {
    }

    private static boolean A(Class<?> cls) {
        try {
            Class<?> cls2 = f12695b;
            Class<?> cls3 = Boolean.TYPE;
            cls2.getMethod("peekLong", cls, cls3);
            cls2.getMethod("pokeLong", cls, Long.TYPE, cls3);
            Class<?> cls4 = Integer.TYPE;
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
    public static Object B(Object obj, long j8) {
        return f12698e.f12716a.getObject(obj, j8);
    }

    private static Field C() {
        Field d8 = d(Buffer.class, "effectiveDirectAddress");
        if (d8 != null) {
            return d8;
        }
        Field d9 = d(Buffer.class, "address");
        if (d9 == null || d9.getType() != Long.TYPE) {
            return null;
        }
        return d9;
    }

    static /* synthetic */ boolean D(Object obj, long j8) {
        return ((byte) (t(obj, (-4) & j8) >>> ((int) (((~j8) & 3) << 3)))) != 0;
    }

    static /* synthetic */ boolean E(Object obj, long j8) {
        return ((byte) (t(obj, (-4) & j8) >>> ((int) ((j8 & 3) << 3)))) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean F(Object obj, long j8) {
        return f12698e.k(obj, j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static double a(Object obj, long j8) {
        return f12698e.a(obj, j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T b(Class<T> cls) {
        try {
            return (T) f12694a.allocateInstance(cls);
        } catch (InstantiationException e8) {
            throw new IllegalStateException(e8);
        }
    }

    static /* synthetic */ Field c() {
        return C();
    }

    private static Field d(Class<?> cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (Throwable unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void f(Object obj, long j8, double d8) {
        f12698e.c(obj, j8, d8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void g(Object obj, long j8, float f5) {
        f12698e.d(obj, j8, f5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void h(Object obj, long j8, int i8) {
        f12698e.e(obj, j8, i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void i(Object obj, long j8, long j9) {
        f12698e.f(obj, j8, j9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void j(Object obj, long j8, Object obj2) {
        f12698e.f12716a.putObject(obj, j8, obj2);
    }

    static /* synthetic */ void k(Object obj, long j8, boolean z4) {
        u(obj, j8, z4 ? (byte) 1 : (byte) 0);
    }

    static /* synthetic */ void l(Throwable th) {
        Logger logger = Logger.getLogger(yb.class.getName());
        Level level = Level.WARNING;
        String valueOf = String.valueOf(th);
        logger.logp(level, "com.google.protobuf.UnsafeUtil", "logMissingMethod", "platform method missing - proto runtime falling back to safer methods: " + valueOf);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void m(byte[] bArr, long j8, byte b9) {
        f12698e.b(bArr, f12701h + j8, b9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float n(Object obj, long j8) {
        return f12698e.i(obj, j8);
    }

    private static int o(Class<?> cls) {
        if (f12700g) {
            return f12698e.f12716a.arrayBaseOffset(cls);
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Unsafe p() {
        try {
            return (Unsafe) AccessController.doPrivileged(new bc());
        } catch (Throwable unused) {
            return null;
        }
    }

    static /* synthetic */ void r(Object obj, long j8, boolean z4) {
        y(obj, j8, z4 ? (byte) 1 : (byte) 0);
    }

    private static int s(Class<?> cls) {
        if (f12700g) {
            return f12698e.f12716a.arrayIndexScale(cls);
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int t(Object obj, long j8) {
        return f12698e.l(obj, j8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void u(Object obj, long j8, byte b9) {
        long j9 = (-4) & j8;
        int t8 = t(obj, j9);
        int i8 = ((~((int) j8)) & 3) << 3;
        h(obj, j9, ((255 & b9) << i8) | (t8 & (~(255 << i8))));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void v(Object obj, long j8, boolean z4) {
        f12698e.g(obj, j8, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean w() {
        return f12700g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long x(Object obj, long j8) {
        return f12698e.m(obj, j8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void y(Object obj, long j8, byte b9) {
        long j9 = (-4) & j8;
        int i8 = (((int) j8) & 3) << 3;
        h(obj, j9, ((255 & b9) << i8) | (t(obj, j9) & (~(255 << i8))));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean z() {
        return f12699f;
    }
}
