package com.google.common.primitives;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class UnsignedInteger extends Number implements Comparable<UnsignedInteger> {

    /* renamed from: b  reason: collision with root package name */
    public static final UnsignedInteger f19597b = f(0);

    /* renamed from: c  reason: collision with root package name */
    public static final UnsignedInteger f19598c = f(1);

    /* renamed from: d  reason: collision with root package name */
    public static final UnsignedInteger f19599d = f(-1);

    /* renamed from: a  reason: collision with root package name */
    private final int f19600a;

    private UnsignedInteger(int i8) {
        this.f19600a = i8 & (-1);
    }

    public static UnsignedInteger f(int i8) {
        return new UnsignedInteger(i8);
    }

    @Override // java.lang.Comparable
    /* renamed from: c */
    public int compareTo(UnsignedInteger unsignedInteger) {
        com.google.common.base.l.n(unsignedInteger);
        return l.a(this.f19600a, unsignedInteger.f19600a);
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return longValue();
    }

    public boolean equals(Object obj) {
        return (obj instanceof UnsignedInteger) && this.f19600a == ((UnsignedInteger) obj).f19600a;
    }

    @Override // java.lang.Number
    public float floatValue() {
        return (float) longValue();
    }

    public String h(int i8) {
        return l.d(this.f19600a, i8);
    }

    public int hashCode() {
        return this.f19600a;
    }

    @Override // java.lang.Number
    public int intValue() {
        return this.f19600a;
    }

    @Override // java.lang.Number
    public long longValue() {
        return l.c(this.f19600a);
    }

    public String toString() {
        return h(10);
    }
}
