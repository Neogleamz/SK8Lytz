package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e1 extends a1 implements RandomAccess, f4 {

    /* renamed from: d  reason: collision with root package name */
    private static final e1 f14746d = new e1(new boolean[0], 0, false);

    /* renamed from: b  reason: collision with root package name */
    private boolean[] f14747b;

    /* renamed from: c  reason: collision with root package name */
    private int f14748c;

    e1() {
        this(new boolean[10], 0, true);
    }

    private e1(boolean[] zArr, int i8, boolean z4) {
        super(z4);
        this.f14747b = zArr;
        this.f14748c = i8;
    }

    private final String h(int i8) {
        int i9 = this.f14748c;
        return "Index:" + i8 + ", Size:" + i9;
    }

    private final void i(int i8) {
        if (i8 < 0 || i8 >= this.f14748c) {
            throw new IndexOutOfBoundsException(h(i8));
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ void add(int i8, Object obj) {
        int i9;
        boolean booleanValue = ((Boolean) obj).booleanValue();
        e();
        if (i8 < 0 || i8 > (i9 = this.f14748c)) {
            throw new IndexOutOfBoundsException(h(i8));
        }
        boolean[] zArr = this.f14747b;
        if (i9 < zArr.length) {
            System.arraycopy(zArr, i8, zArr, i8 + 1, i9 - i8);
        } else {
            boolean[] zArr2 = new boolean[((i9 * 3) / 2) + 1];
            System.arraycopy(zArr, 0, zArr2, 0, i8);
            System.arraycopy(this.f14747b, i8, zArr2, i8 + 1, this.f14748c - i8);
            this.f14747b = zArr2;
        }
        this.f14747b[i8] = booleanValue;
        this.f14748c++;
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ /* synthetic */ boolean add(Object obj) {
        g(((Boolean) obj).booleanValue());
        return true;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection collection) {
        e();
        byte[] bArr = y2.f14888d;
        Objects.requireNonNull(collection);
        if (collection instanceof e1) {
            e1 e1Var = (e1) collection;
            int i8 = e1Var.f14748c;
            if (i8 == 0) {
                return false;
            }
            int i9 = this.f14748c;
            if (Integer.MAX_VALUE - i9 >= i8) {
                int i10 = i9 + i8;
                boolean[] zArr = this.f14747b;
                if (i10 > zArr.length) {
                    this.f14747b = Arrays.copyOf(zArr, i10);
                }
                System.arraycopy(e1Var.f14747b, 0, this.f14747b, this.f14748c, e1Var.f14748c);
                this.f14748c = i10;
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
        if (obj instanceof e1) {
            e1 e1Var = (e1) obj;
            if (this.f14748c != e1Var.f14748c) {
                return false;
            }
            boolean[] zArr = e1Var.f14747b;
            for (int i8 = 0; i8 < this.f14748c; i8++) {
                if (this.f14747b[i8] != zArr[i8]) {
                    return false;
                }
            }
            return true;
        }
        return super.equals(obj);
    }

    public final void g(boolean z4) {
        e();
        int i8 = this.f14748c;
        boolean[] zArr = this.f14747b;
        if (i8 == zArr.length) {
            boolean[] zArr2 = new boolean[((i8 * 3) / 2) + 1];
            System.arraycopy(zArr, 0, zArr2, 0, i8);
            this.f14747b = zArr2;
        }
        boolean[] zArr3 = this.f14747b;
        int i9 = this.f14748c;
        this.f14748c = i9 + 1;
        zArr3[i9] = z4;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object get(int i8) {
        i(i8);
        return Boolean.valueOf(this.f14747b[i8]);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.Collection, java.util.List
    public final int hashCode() {
        int i8 = 1;
        for (int i9 = 0; i9 < this.f14748c; i9++) {
            i8 = (i8 * 31) + y2.a(this.f14747b[i9]);
        }
        return i8;
    }

    @Override // java.util.AbstractList, java.util.List
    public final int indexOf(Object obj) {
        if (obj instanceof Boolean) {
            boolean booleanValue = ((Boolean) obj).booleanValue();
            int i8 = this.f14748c;
            for (int i9 = 0; i9 < i8; i9++) {
                if (this.f14747b[i9] == booleanValue) {
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
        i(i8);
        boolean[] zArr = this.f14747b;
        boolean z4 = zArr[i8];
        if (i8 < this.f14748c - 1) {
            System.arraycopy(zArr, i8 + 1, zArr, i8, (i9 - i8) - 1);
        }
        this.f14748c--;
        ((AbstractList) this).modCount++;
        return Boolean.valueOf(z4);
    }

    @Override // java.util.AbstractList
    protected final void removeRange(int i8, int i9) {
        e();
        if (i9 < i8) {
            throw new IndexOutOfBoundsException("toIndex < fromIndex");
        }
        boolean[] zArr = this.f14747b;
        System.arraycopy(zArr, i9, zArr, i8, this.f14748c - i9);
        this.f14748c -= i9 - i8;
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object set(int i8, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        e();
        i(i8);
        boolean[] zArr = this.f14747b;
        boolean z4 = zArr[i8];
        zArr[i8] = booleanValue;
        return Boolean.valueOf(z4);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f14748c;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2
    public final /* bridge */ /* synthetic */ x2 z(int i8) {
        if (i8 >= this.f14748c) {
            return new e1(Arrays.copyOf(this.f14747b, i8), this.f14748c, true);
        }
        throw new IllegalArgumentException();
    }
}
