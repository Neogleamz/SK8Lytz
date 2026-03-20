package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b8 {

    /* renamed from: d  reason: collision with root package name */
    private static volatile int f12094d = 100;

    /* renamed from: a  reason: collision with root package name */
    int f12095a;

    /* renamed from: b  reason: collision with root package name */
    private int f12096b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f12097c;

    private b8() {
        this.f12095a = f12094d;
        this.f12096b = Integer.MAX_VALUE;
        this.f12097c = false;
    }

    public static int a(int i8) {
        return (-(i8 & 1)) ^ (i8 >>> 1);
    }

    public static long b(long j8) {
        return (-(j8 & 1)) ^ (j8 >>> 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static b8 c(byte[] bArr, int i8, int i9, boolean z4) {
        e8 e8Var = new e8(bArr, i9);
        try {
            e8Var.d(i9);
            return e8Var;
        } catch (zzkb e8) {
            throw new IllegalArgumentException(e8);
        }
    }

    public abstract int d(int i8);

    public abstract int e();
}
