package com.google.common.primitives;

import java.io.Serializable;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ImmutableLongArray implements Serializable {

    /* renamed from: d  reason: collision with root package name */
    private static final ImmutableLongArray f19591d = new ImmutableLongArray(new long[0]);

    /* renamed from: a  reason: collision with root package name */
    private final long[] f19592a;

    /* renamed from: b  reason: collision with root package name */
    private final transient int f19593b;

    /* renamed from: c  reason: collision with root package name */
    private final int f19594c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private long[] f19595a;

        /* renamed from: b  reason: collision with root package name */
        private int f19596b = 0;

        b(int i8) {
            this.f19595a = new long[i8];
        }

        private void c(int i8) {
            int i9 = this.f19596b + i8;
            long[] jArr = this.f19595a;
            if (i9 > jArr.length) {
                this.f19595a = Arrays.copyOf(jArr, d(jArr.length, i9));
            }
        }

        private static int d(int i8, int i9) {
            if (i9 >= 0) {
                int i10 = i8 + (i8 >> 1) + 1;
                if (i10 < i9) {
                    i10 = Integer.highestOneBit(i9 - 1) << 1;
                }
                if (i10 < 0) {
                    return Integer.MAX_VALUE;
                }
                return i10;
            }
            throw new AssertionError("cannot store more than MAX_VALUE elements");
        }

        public b a(long j8) {
            c(1);
            long[] jArr = this.f19595a;
            int i8 = this.f19596b;
            jArr[i8] = j8;
            this.f19596b = i8 + 1;
            return this;
        }

        public ImmutableLongArray b() {
            return this.f19596b == 0 ? ImmutableLongArray.f19591d : new ImmutableLongArray(this.f19595a, 0, this.f19596b);
        }
    }

    private ImmutableLongArray(long[] jArr) {
        this(jArr, 0, jArr.length);
    }

    private ImmutableLongArray(long[] jArr, int i8, int i9) {
        this.f19592a = jArr;
        this.f19593b = i8;
        this.f19594c = i9;
    }

    public static b b() {
        return new b(10);
    }

    private boolean e() {
        return this.f19593b > 0 || this.f19594c < this.f19592a.length;
    }

    public long c(int i8) {
        com.google.common.base.l.l(i8, f());
        return this.f19592a[this.f19593b + i8];
    }

    public boolean d() {
        return this.f19594c == this.f19593b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ImmutableLongArray) {
            ImmutableLongArray immutableLongArray = (ImmutableLongArray) obj;
            if (f() != immutableLongArray.f()) {
                return false;
            }
            for (int i8 = 0; i8 < f(); i8++) {
                if (c(i8) != immutableLongArray.c(i8)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public int f() {
        return this.f19594c - this.f19593b;
    }

    public long[] g() {
        return Arrays.copyOfRange(this.f19592a, this.f19593b, this.f19594c);
    }

    public ImmutableLongArray h() {
        return e() ? new ImmutableLongArray(g()) : this;
    }

    public int hashCode() {
        int i8 = 1;
        for (int i9 = this.f19593b; i9 < this.f19594c; i9++) {
            i8 = (i8 * 31) + i.b(this.f19592a[i9]);
        }
        return i8;
    }

    Object readResolve() {
        return d() ? f19591d : this;
    }

    public String toString() {
        if (d()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(f() * 5);
        sb.append('[');
        sb.append(this.f19592a[this.f19593b]);
        int i8 = this.f19593b;
        while (true) {
            i8++;
            if (i8 >= this.f19594c) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(", ");
            sb.append(this.f19592a[i8]);
        }
    }

    Object writeReplace() {
        return h();
    }
}
