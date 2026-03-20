package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class q2 extends a1 implements RandomAccess, v2, f4 {

    /* renamed from: d  reason: collision with root package name */
    private static final q2 f14833d = new q2(new int[0], 0, false);

    /* renamed from: b  reason: collision with root package name */
    private int[] f14834b;

    /* renamed from: c  reason: collision with root package name */
    private int f14835c;

    q2() {
        this(new int[10], 0, true);
    }

    private q2(int[] iArr, int i8, boolean z4) {
        super(z4);
        this.f14834b = iArr;
        this.f14835c = i8;
    }

    public static q2 h() {
        return f14833d;
    }

    private final String k(int i8) {
        int i9 = this.f14835c;
        return "Index:" + i8 + ", Size:" + i9;
    }

    private final void n(int i8) {
        if (i8 < 0 || i8 >= this.f14835c) {
            throw new IndexOutOfBoundsException(k(i8));
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ void add(int i8, Object obj) {
        int i9;
        int intValue = ((Integer) obj).intValue();
        e();
        if (i8 < 0 || i8 > (i9 = this.f14835c)) {
            throw new IndexOutOfBoundsException(k(i8));
        }
        int[] iArr = this.f14834b;
        if (i9 < iArr.length) {
            System.arraycopy(iArr, i8, iArr, i8 + 1, i9 - i8);
        } else {
            int[] iArr2 = new int[((i9 * 3) / 2) + 1];
            System.arraycopy(iArr, 0, iArr2, 0, i8);
            System.arraycopy(this.f14834b, i8, iArr2, i8 + 1, this.f14835c - i8);
            this.f14834b = iArr2;
        }
        this.f14834b[i8] = intValue;
        this.f14835c++;
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ /* synthetic */ boolean add(Object obj) {
        i(((Integer) obj).intValue());
        return true;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection collection) {
        e();
        byte[] bArr = y2.f14888d;
        Objects.requireNonNull(collection);
        if (collection instanceof q2) {
            q2 q2Var = (q2) collection;
            int i8 = q2Var.f14835c;
            if (i8 == 0) {
                return false;
            }
            int i9 = this.f14835c;
            if (Integer.MAX_VALUE - i9 >= i8) {
                int i10 = i9 + i8;
                int[] iArr = this.f14834b;
                if (i10 > iArr.length) {
                    this.f14834b = Arrays.copyOf(iArr, i10);
                }
                System.arraycopy(q2Var.f14834b, 0, this.f14834b, this.f14835c, q2Var.f14835c);
                this.f14835c = i10;
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
        if (obj instanceof q2) {
            q2 q2Var = (q2) obj;
            if (this.f14835c != q2Var.f14835c) {
                return false;
            }
            int[] iArr = q2Var.f14834b;
            for (int i8 = 0; i8 < this.f14835c; i8++) {
                if (this.f14834b[i8] != iArr[i8]) {
                    return false;
                }
            }
            return true;
        }
        return super.equals(obj);
    }

    public final int g(int i8) {
        n(i8);
        return this.f14834b[i8];
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object get(int i8) {
        n(i8);
        return Integer.valueOf(this.f14834b[i8]);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.Collection, java.util.List
    public final int hashCode() {
        int i8 = 1;
        for (int i9 = 0; i9 < this.f14835c; i9++) {
            i8 = (i8 * 31) + this.f14834b[i9];
        }
        return i8;
    }

    public final void i(int i8) {
        e();
        int i9 = this.f14835c;
        int[] iArr = this.f14834b;
        if (i9 == iArr.length) {
            int[] iArr2 = new int[((i9 * 3) / 2) + 1];
            System.arraycopy(iArr, 0, iArr2, 0, i9);
            this.f14834b = iArr2;
        }
        int[] iArr3 = this.f14834b;
        int i10 = this.f14835c;
        this.f14835c = i10 + 1;
        iArr3[i10] = i8;
    }

    @Override // java.util.AbstractList, java.util.List
    public final int indexOf(Object obj) {
        if (obj instanceof Integer) {
            int intValue = ((Integer) obj).intValue();
            int i8 = this.f14835c;
            for (int i9 = 0; i9 < i8; i9++) {
                if (this.f14834b[i9] == intValue) {
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
        n(i8);
        int[] iArr = this.f14834b;
        int i10 = iArr[i8];
        if (i8 < this.f14835c - 1) {
            System.arraycopy(iArr, i8 + 1, iArr, i8, (i9 - i8) - 1);
        }
        this.f14835c--;
        ((AbstractList) this).modCount++;
        return Integer.valueOf(i10);
    }

    @Override // java.util.AbstractList
    protected final void removeRange(int i8, int i9) {
        e();
        if (i9 < i8) {
            throw new IndexOutOfBoundsException("toIndex < fromIndex");
        }
        int[] iArr = this.f14834b;
        System.arraycopy(iArr, i9, iArr, i8, this.f14835c - i9);
        this.f14835c -= i9 - i8;
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object set(int i8, Object obj) {
        int intValue = ((Integer) obj).intValue();
        e();
        n(i8);
        int[] iArr = this.f14834b;
        int i9 = iArr[i8];
        iArr[i8] = intValue;
        return Integer.valueOf(i9);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f14835c;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2
    public final /* bridge */ /* synthetic */ x2 z(int i8) {
        if (i8 >= this.f14835c) {
            return new q2(Arrays.copyOf(this.f14834b, i8), this.f14835c, true);
        }
        throw new IllegalArgumentException();
    }
}
