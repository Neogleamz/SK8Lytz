package com.google.common.primitives;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class UnsignedLong extends Number implements Comparable<UnsignedLong> {

    /* renamed from: b  reason: collision with root package name */
    public static final UnsignedLong f19601b = new UnsignedLong(0);

    /* renamed from: c  reason: collision with root package name */
    public static final UnsignedLong f19602c = new UnsignedLong(1);

    /* renamed from: d  reason: collision with root package name */
    public static final UnsignedLong f19603d = new UnsignedLong(-1);

    /* renamed from: a  reason: collision with root package name */
    private final long f19604a;

    private UnsignedLong(long j8) {
        this.f19604a = j8;
    }

    @Override // java.lang.Comparable
    /* renamed from: c */
    public int compareTo(UnsignedLong unsignedLong) {
        com.google.common.base.l.n(unsignedLong);
        return m.a(this.f19604a, unsignedLong.f19604a);
    }

    @Override // java.lang.Number
    public double doubleValue() {
        long j8 = this.f19604a;
        if (j8 >= 0) {
            return j8;
        }
        return ((j8 & 1) | (j8 >>> 1)) * 2.0d;
    }

    public boolean equals(Object obj) {
        return (obj instanceof UnsignedLong) && this.f19604a == ((UnsignedLong) obj).f19604a;
    }

    @Override // java.lang.Number
    public float floatValue() {
        long j8 = this.f19604a;
        if (j8 >= 0) {
            return (float) j8;
        }
        return ((float) ((j8 & 1) | (j8 >>> 1))) * 2.0f;
    }

    public int hashCode() {
        return i.b(this.f19604a);
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) this.f19604a;
    }

    @Override // java.lang.Number
    public long longValue() {
        return this.f19604a;
    }

    public String toString() {
        return m.d(this.f19604a);
    }
}
