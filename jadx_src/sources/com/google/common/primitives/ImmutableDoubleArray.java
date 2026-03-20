package com.google.common.primitives;

import java.io.Serializable;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ImmutableDoubleArray implements Serializable {

    /* renamed from: d  reason: collision with root package name */
    private static final ImmutableDoubleArray f19583d = new ImmutableDoubleArray(new double[0]);

    /* renamed from: a  reason: collision with root package name */
    private final double[] f19584a;

    /* renamed from: b  reason: collision with root package name */
    private final transient int f19585b;

    /* renamed from: c  reason: collision with root package name */
    private final int f19586c;

    private ImmutableDoubleArray(double[] dArr) {
        this(dArr, 0, dArr.length);
    }

    private ImmutableDoubleArray(double[] dArr, int i8, int i9) {
        this.f19584a = dArr;
        this.f19585b = i8;
        this.f19586c = i9;
    }

    private static boolean a(double d8, double d9) {
        return Double.doubleToLongBits(d8) == Double.doubleToLongBits(d9);
    }

    private boolean d() {
        return this.f19585b > 0 || this.f19586c < this.f19584a.length;
    }

    public double b(int i8) {
        com.google.common.base.l.l(i8, e());
        return this.f19584a[this.f19585b + i8];
    }

    public boolean c() {
        return this.f19586c == this.f19585b;
    }

    public int e() {
        return this.f19586c - this.f19585b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ImmutableDoubleArray) {
            ImmutableDoubleArray immutableDoubleArray = (ImmutableDoubleArray) obj;
            if (e() != immutableDoubleArray.e()) {
                return false;
            }
            for (int i8 = 0; i8 < e(); i8++) {
                if (!a(b(i8), immutableDoubleArray.b(i8))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public double[] f() {
        return Arrays.copyOfRange(this.f19584a, this.f19585b, this.f19586c);
    }

    public ImmutableDoubleArray g() {
        return d() ? new ImmutableDoubleArray(f()) : this;
    }

    public int hashCode() {
        int i8 = 1;
        for (int i9 = this.f19585b; i9 < this.f19586c; i9++) {
            i8 = (i8 * 31) + c.b(this.f19584a[i9]);
        }
        return i8;
    }

    Object readResolve() {
        return c() ? f19583d : this;
    }

    public String toString() {
        if (c()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(e() * 5);
        sb.append('[');
        sb.append(this.f19584a[this.f19585b]);
        int i8 = this.f19585b;
        while (true) {
            i8++;
            if (i8 >= this.f19586c) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(", ");
            sb.append(this.f19584a[i8]);
        }
    }

    Object writeReplace() {
        return g();
    }
}
