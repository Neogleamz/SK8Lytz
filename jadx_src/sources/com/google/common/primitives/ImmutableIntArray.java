package com.google.common.primitives;

import java.io.Serializable;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ImmutableIntArray implements Serializable {

    /* renamed from: d  reason: collision with root package name */
    private static final ImmutableIntArray f19587d = new ImmutableIntArray(new int[0]);

    /* renamed from: a  reason: collision with root package name */
    private final int[] f19588a;

    /* renamed from: b  reason: collision with root package name */
    private final transient int f19589b;

    /* renamed from: c  reason: collision with root package name */
    private final int f19590c;

    private ImmutableIntArray(int[] iArr) {
        this(iArr, 0, iArr.length);
    }

    private ImmutableIntArray(int[] iArr, int i8, int i9) {
        this.f19588a = iArr;
        this.f19589b = i8;
        this.f19590c = i9;
    }

    private boolean c() {
        return this.f19589b > 0 || this.f19590c < this.f19588a.length;
    }

    public int a(int i8) {
        com.google.common.base.l.l(i8, d());
        return this.f19588a[this.f19589b + i8];
    }

    public boolean b() {
        return this.f19590c == this.f19589b;
    }

    public int d() {
        return this.f19590c - this.f19589b;
    }

    public int[] e() {
        return Arrays.copyOfRange(this.f19588a, this.f19589b, this.f19590c);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ImmutableIntArray) {
            ImmutableIntArray immutableIntArray = (ImmutableIntArray) obj;
            if (d() != immutableIntArray.d()) {
                return false;
            }
            for (int i8 = 0; i8 < d(); i8++) {
                if (a(i8) != immutableIntArray.a(i8)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public ImmutableIntArray f() {
        return c() ? new ImmutableIntArray(e()) : this;
    }

    public int hashCode() {
        int i8 = 1;
        for (int i9 = this.f19589b; i9 < this.f19590c; i9++) {
            i8 = (i8 * 31) + g.g(this.f19588a[i9]);
        }
        return i8;
    }

    Object readResolve() {
        return b() ? f19587d : this;
    }

    public String toString() {
        if (b()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(d() * 5);
        sb.append('[');
        sb.append(this.f19588a[this.f19589b]);
        int i8 = this.f19589b;
        while (true) {
            i8++;
            if (i8 >= this.f19590c) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(", ");
            sb.append(this.f19588a[i8]);
        }
    }

    Object writeReplace() {
        return f();
    }
}
