package com.google.common.primitives;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g extends h {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a extends AbstractList<Integer> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        final int[] f19606a;

        /* renamed from: b  reason: collision with root package name */
        final int f19607b;

        /* renamed from: c  reason: collision with root package name */
        final int f19608c;

        a(int[] iArr) {
            this(iArr, 0, iArr.length);
        }

        a(int[] iArr, int i8, int i9) {
            this.f19606a = iArr;
            this.f19607b = i8;
            this.f19608c = i9;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean contains(Object obj) {
            return (obj instanceof Integer) && g.i(this.f19606a, ((Integer) obj).intValue(), this.f19607b, this.f19608c) != -1;
        }

        @Override // java.util.AbstractList, java.util.List
        /* renamed from: e */
        public Integer get(int i8) {
            com.google.common.base.l.l(i8, size());
            return Integer.valueOf(this.f19606a[this.f19607b + i8]);
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof a) {
                a aVar = (a) obj;
                int size = size();
                if (aVar.size() != size) {
                    return false;
                }
                for (int i8 = 0; i8 < size; i8++) {
                    if (this.f19606a[this.f19607b + i8] != aVar.f19606a[aVar.f19607b + i8]) {
                        return false;
                    }
                }
                return true;
            }
            return super.equals(obj);
        }

        @Override // java.util.AbstractList, java.util.List
        /* renamed from: g */
        public Integer set(int i8, Integer num) {
            com.google.common.base.l.l(i8, size());
            int[] iArr = this.f19606a;
            int i9 = this.f19607b;
            int i10 = iArr[i9 + i8];
            iArr[i9 + i8] = ((Integer) com.google.common.base.l.n(num)).intValue();
            return Integer.valueOf(i10);
        }

        int[] h() {
            return Arrays.copyOfRange(this.f19606a, this.f19607b, this.f19608c);
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public int hashCode() {
            int i8 = 1;
            for (int i9 = this.f19607b; i9 < this.f19608c; i9++) {
                i8 = (i8 * 31) + g.g(this.f19606a[i9]);
            }
            return i8;
        }

        @Override // java.util.AbstractList, java.util.List
        public int indexOf(Object obj) {
            int i8;
            if (!(obj instanceof Integer) || (i8 = g.i(this.f19606a, ((Integer) obj).intValue(), this.f19607b, this.f19608c)) < 0) {
                return -1;
            }
            return i8 - this.f19607b;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.AbstractList, java.util.List
        public int lastIndexOf(Object obj) {
            int j8;
            if (!(obj instanceof Integer) || (j8 = g.j(this.f19606a, ((Integer) obj).intValue(), this.f19607b, this.f19608c)) < 0) {
                return -1;
            }
            return j8 - this.f19607b;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.f19608c - this.f19607b;
        }

        @Override // java.util.AbstractList, java.util.List
        public List<Integer> subList(int i8, int i9) {
            com.google.common.base.l.r(i8, i9, size());
            if (i8 == i9) {
                return Collections.emptyList();
            }
            int[] iArr = this.f19606a;
            int i10 = this.f19607b;
            return new a(iArr, i8 + i10, i10 + i9);
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            StringBuilder sb = new StringBuilder(size() * 5);
            sb.append('[');
            sb.append(this.f19606a[this.f19607b]);
            int i8 = this.f19607b;
            while (true) {
                i8++;
                if (i8 >= this.f19608c) {
                    sb.append(']');
                    return sb.toString();
                }
                sb.append(", ");
                sb.append(this.f19606a[i8]);
            }
        }
    }

    public static List<Integer> c(int... iArr) {
        return iArr.length == 0 ? Collections.emptyList() : new a(iArr);
    }

    public static int d(long j8) {
        int i8 = (int) j8;
        com.google.common.base.l.h(((long) i8) == j8, "Out of range: %s", j8);
        return i8;
    }

    public static int e(int i8, int i9) {
        if (i8 < i9) {
            return -1;
        }
        return i8 > i9 ? 1 : 0;
    }

    public static int f(int i8, int i9, int i10) {
        com.google.common.base.l.g(i9 <= i10, "min (%s) must be less than or equal to max (%s)", i9, i10);
        return Math.min(Math.max(i8, i9), i10);
    }

    public static int g(int i8) {
        return i8;
    }

    public static int h(int[] iArr, int i8) {
        return i(iArr, i8, 0, iArr.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int i(int[] iArr, int i8, int i9, int i10) {
        while (i9 < i10) {
            if (iArr[i9] == i8) {
                return i9;
            }
            i9++;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int j(int[] iArr, int i8, int i9, int i10) {
        for (int i11 = i10 - 1; i11 >= i9; i11--) {
            if (iArr[i11] == i8) {
                return i11;
            }
        }
        return -1;
    }

    public static int k(long j8) {
        if (j8 > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        if (j8 < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        return (int) j8;
    }

    public static int[] l(Collection<? extends Number> collection) {
        if (collection instanceof a) {
            return ((a) collection).h();
        }
        Object[] array = collection.toArray();
        int length = array.length;
        int[] iArr = new int[length];
        for (int i8 = 0; i8 < length; i8++) {
            iArr[i8] = ((Number) com.google.common.base.l.n(array[i8])).intValue();
        }
        return iArr;
    }
}
