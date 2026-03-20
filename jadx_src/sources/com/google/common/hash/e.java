package com.google.common.hash;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Random;
import sun.misc.Unsafe;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class e extends Number {

    /* renamed from: d  reason: collision with root package name */
    static final ThreadLocal<int[]> f19559d = new ThreadLocal<>();

    /* renamed from: e  reason: collision with root package name */
    static final Random f19560e = new Random();

    /* renamed from: f  reason: collision with root package name */
    static final int f19561f = Runtime.getRuntime().availableProcessors();

    /* renamed from: g  reason: collision with root package name */
    private static final Unsafe f19562g;

    /* renamed from: h  reason: collision with root package name */
    private static final long f19563h;

    /* renamed from: j  reason: collision with root package name */
    private static final long f19564j;

    /* renamed from: a  reason: collision with root package name */
    volatile transient b[] f19565a;

    /* renamed from: b  reason: collision with root package name */
    volatile transient long f19566b;

    /* renamed from: c  reason: collision with root package name */
    volatile transient int f19567c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements PrivilegedExceptionAction<Unsafe> {
        a() {
        }

        @Override // java.security.PrivilegedExceptionAction
        /* renamed from: a */
        public Unsafe run() {
            Field[] declaredFields;
            for (Field field : Unsafe.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object obj = field.get(null);
                if (Unsafe.class.isInstance(obj)) {
                    return (Unsafe) Unsafe.class.cast(obj);
                }
            }
            throw new NoSuchFieldError("the Unsafe");
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b {

        /* renamed from: b  reason: collision with root package name */
        private static final Unsafe f19568b;

        /* renamed from: c  reason: collision with root package name */
        private static final long f19569c;

        /* renamed from: a  reason: collision with root package name */
        volatile long f19570a;

        static {
            try {
                Unsafe b9 = e.b();
                f19568b = b9;
                f19569c = b9.objectFieldOffset(b.class.getDeclaredField("a"));
            } catch (Exception e8) {
                throw new Error(e8);
            }
        }

        b(long j8) {
            this.f19570a = j8;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final boolean a(long j8, long j9) {
            return f19568b.compareAndSwapLong(this, f19569c, j8, j9);
        }
    }

    static {
        try {
            Unsafe f5 = f();
            f19562g = f5;
            f19563h = f5.objectFieldOffset(e.class.getDeclaredField("b"));
            f19564j = f5.objectFieldOffset(e.class.getDeclaredField("c"));
        } catch (Exception e8) {
            throw new Error(e8);
        }
    }

    static /* synthetic */ Unsafe b() {
        return f();
    }

    private static Unsafe f() {
        try {
            try {
                return Unsafe.getUnsafe();
            } catch (PrivilegedActionException e8) {
                throw new RuntimeException("Could not initialize intrinsics", e8.getCause());
            }
        } catch (SecurityException unused) {
            return (Unsafe) AccessController.doPrivileged(new a());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean c(long j8, long j9) {
        return f19562g.compareAndSwapLong(this, f19563h, j8, j9);
    }

    final boolean d() {
        return f19562g.compareAndSwapInt(this, f19564j, 0, 1);
    }

    abstract long e(long j8, long j9);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0023 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x00ee A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void g(long r17, int[] r19, boolean r20) {
        /*
            Method dump skipped, instructions count: 239
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.hash.e.g(long, int[], boolean):void");
    }
}
