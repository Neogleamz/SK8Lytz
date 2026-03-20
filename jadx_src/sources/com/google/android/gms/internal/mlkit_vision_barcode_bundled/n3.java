package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class n3 extends a1 implements RandomAccess, f4 {

    /* renamed from: d  reason: collision with root package name */
    private static final n3 f14818d = new n3(new long[0], 0, false);

    /* renamed from: b  reason: collision with root package name */
    private long[] f14819b;

    /* renamed from: c  reason: collision with root package name */
    private int f14820c;

    n3() {
        this(new long[10], 0, true);
    }

    private n3(long[] jArr, int i8, boolean z4) {
        super(z4);
        this.f14819b = jArr;
        this.f14820c = i8;
    }

    private final String i(int i8) {
        int i9 = this.f14820c;
        return "Index:" + i8 + ", Size:" + i9;
    }

    private final void k(int i8) {
        if (i8 < 0 || i8 >= this.f14820c) {
            throw new IndexOutOfBoundsException(i(i8));
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ void add(int i8, Object obj) {
        int i9;
        long longValue = ((Long) obj).longValue();
        e();
        if (i8 < 0 || i8 > (i9 = this.f14820c)) {
            throw new IndexOutOfBoundsException(i(i8));
        }
        long[] jArr = this.f14819b;
        if (i9 < jArr.length) {
            System.arraycopy(jArr, i8, jArr, i8 + 1, i9 - i8);
        } else {
            long[] jArr2 = new long[((i9 * 3) / 2) + 1];
            System.arraycopy(jArr, 0, jArr2, 0, i8);
            System.arraycopy(this.f14819b, i8, jArr2, i8 + 1, this.f14820c - i8);
            this.f14819b = jArr2;
        }
        this.f14819b[i8] = longValue;
        this.f14820c++;
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ /* synthetic */ boolean add(Object obj) {
        h(((Long) obj).longValue());
        return true;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection collection) {
        e();
        byte[] bArr = y2.f14888d;
        Objects.requireNonNull(collection);
        if (collection instanceof n3) {
            n3 n3Var = (n3) collection;
            int i8 = n3Var.f14820c;
            if (i8 == 0) {
                return false;
            }
            int i9 = this.f14820c;
            if (Integer.MAX_VALUE - i9 >= i8) {
                int i10 = i9 + i8;
                long[] jArr = this.f14819b;
                if (i10 > jArr.length) {
                    this.f14819b = Arrays.copyOf(jArr, i10);
                }
                System.arraycopy(n3Var.f14819b, 0, this.f14819b, this.f14820c, n3Var.f14820c);
                this.f14820c = i10;
                ((AbstractList) this).modCount++;
                return true;
            }
            throw new OutOfMemoryError();
        }
        return super.addAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.Collection, java.util.List
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof n3) {
            n3 n3Var = (n3) obj;
            if (this.f14820c != n3Var.f14820c) {
                return false;
            }
            long[] jArr = n3Var.f14819b;
            for (int i8 = 0; i8 < this.f14820c; i8++) {
                if (this.f14819b[i8] != jArr[i8]) {
                    return false;
                }
            }
            return true;
        }
        return super.equals(obj);
    }

    public final long g(int i8) {
        k(i8);
        return this.f14819b[i8];
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object get(int i8) {
        k(i8);
        return Long.valueOf(this.f14819b[i8]);
    }

    public final void h(long j8) {
        e();
        int i8 = this.f14820c;
        long[] jArr = this.f14819b;
        if (i8 == jArr.length) {
            long[] jArr2 = new long[((i8 * 3) / 2) + 1];
            System.arraycopy(jArr, 0, jArr2, 0, i8);
            this.f14819b = jArr2;
        }
        long[] jArr3 = this.f14819b;
        int i9 = this.f14820c;
        this.f14820c = i9 + 1;
        jArr3[i9] = j8;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.Collection, java.util.List
    public final int hashCode() {
        int i8 = 1;
        for (int i9 = 0; i9 < this.f14820c; i9++) {
            long j8 = this.f14819b[i9];
            byte[] bArr = y2.f14888d;
            i8 = (i8 * 31) + ((int) (j8 ^ (j8 >>> 32)));
        }
        return i8;
    }

    @Override // java.util.AbstractList, java.util.List
    public final int indexOf(Object obj) {
        if (obj instanceof Long) {
            long longValue = ((Long) obj).longValue();
            int i8 = this.f14820c;
            for (int i9 = 0; i9 < i8; i9++) {
                if (this.f14819b[i9] == longValue) {
                    return i9;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object remove(int i8) {
        int i9;
        e();
        k(i8);
        long[] jArr = this.f14819b;
        long j8 = jArr[i8];
        if (i8 < this.f14820c - 1) {
            System.arraycopy(jArr, i8 + 1, jArr, i8, (i9 - i8) - 1);
        }
        this.f14820c--;
        ((AbstractList) this).modCount++;
        return Long.valueOf(j8);
    }

    @Override // java.util.AbstractList
    protected final void removeRange(int i8, int i9) {
        e();
        if (i9 < i8) {
            throw new IndexOutOfBoundsException("toIndex < fromIndex");
        }
        long[] jArr = this.f14819b;
        System.arraycopy(jArr, i9, jArr, i8, this.f14820c - i9);
        this.f14820c -= i9 - i8;
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object set(int i8, Object obj) {
        long longValue = ((Long) obj).longValue();
        e();
        k(i8);
        long[] jArr = this.f14819b;
        long j8 = jArr[i8];
        jArr[i8] = longValue;
        return Long.valueOf(j8);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f14820c;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2
    public final /* bridge */ /* synthetic */ x2 z(int i8) {
        if (i8 >= this.f14820c) {
            return new n3(Arrays.copyOf(this.f14819b, i8), this.f14820c, true);
        }
        throw new IllegalArgumentException();
    }
}
