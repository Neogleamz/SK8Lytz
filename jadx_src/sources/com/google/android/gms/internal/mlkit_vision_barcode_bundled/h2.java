package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h2 extends a1 implements RandomAccess, u2, f4 {

    /* renamed from: d  reason: collision with root package name */
    private static final h2 f14773d = new h2(new float[0], 0, false);

    /* renamed from: b  reason: collision with root package name */
    private float[] f14774b;

    /* renamed from: c  reason: collision with root package name */
    private int f14775c;

    h2() {
        this(new float[10], 0, true);
    }

    private h2(float[] fArr, int i8, boolean z4) {
        super(z4);
        this.f14774b = fArr;
        this.f14775c = i8;
    }

    public static h2 g() {
        return f14773d;
    }

    private final String h(int i8) {
        int i9 = this.f14775c;
        return "Index:" + i8 + ", Size:" + i9;
    }

    private final void i(int i8) {
        if (i8 < 0 || i8 >= this.f14775c) {
            throw new IndexOutOfBoundsException(h(i8));
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.u2
    public final void K0(float f5) {
        e();
        int i8 = this.f14775c;
        float[] fArr = this.f14774b;
        if (i8 == fArr.length) {
            float[] fArr2 = new float[((i8 * 3) / 2) + 1];
            System.arraycopy(fArr, 0, fArr2, 0, i8);
            this.f14774b = fArr2;
        }
        float[] fArr3 = this.f14774b;
        int i9 = this.f14775c;
        this.f14775c = i9 + 1;
        fArr3[i9] = f5;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ void add(int i8, Object obj) {
        int i9;
        float floatValue = ((Float) obj).floatValue();
        e();
        if (i8 < 0 || i8 > (i9 = this.f14775c)) {
            throw new IndexOutOfBoundsException(h(i8));
        }
        float[] fArr = this.f14774b;
        if (i9 < fArr.length) {
            System.arraycopy(fArr, i8, fArr, i8 + 1, i9 - i8);
        } else {
            float[] fArr2 = new float[((i9 * 3) / 2) + 1];
            System.arraycopy(fArr, 0, fArr2, 0, i8);
            System.arraycopy(this.f14774b, i8, fArr2, i8 + 1, this.f14775c - i8);
            this.f14774b = fArr2;
        }
        this.f14774b[i8] = floatValue;
        this.f14775c++;
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ /* synthetic */ boolean add(Object obj) {
        K0(((Float) obj).floatValue());
        return true;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection collection) {
        e();
        byte[] bArr = y2.f14888d;
        Objects.requireNonNull(collection);
        if (collection instanceof h2) {
            h2 h2Var = (h2) collection;
            int i8 = h2Var.f14775c;
            if (i8 == 0) {
                return false;
            }
            int i9 = this.f14775c;
            if (Integer.MAX_VALUE - i9 >= i8) {
                int i10 = i9 + i8;
                float[] fArr = this.f14774b;
                if (i10 > fArr.length) {
                    this.f14774b = Arrays.copyOf(fArr, i10);
                }
                System.arraycopy(h2Var.f14774b, 0, this.f14774b, this.f14775c, h2Var.f14775c);
                this.f14775c = i10;
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
        if (obj instanceof h2) {
            h2 h2Var = (h2) obj;
            if (this.f14775c != h2Var.f14775c) {
                return false;
            }
            float[] fArr = h2Var.f14774b;
            for (int i8 = 0; i8 < this.f14775c; i8++) {
                if (Float.floatToIntBits(this.f14774b[i8]) != Float.floatToIntBits(fArr[i8])) {
                    return false;
                }
            }
            return true;
        }
        return super.equals(obj);
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object get(int i8) {
        i(i8);
        return Float.valueOf(this.f14774b[i8]);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.Collection, java.util.List
    public final int hashCode() {
        int i8 = 1;
        for (int i9 = 0; i9 < this.f14775c; i9++) {
            i8 = (i8 * 31) + Float.floatToIntBits(this.f14774b[i9]);
        }
        return i8;
    }

    @Override // java.util.AbstractList, java.util.List
    public final int indexOf(Object obj) {
        if (obj instanceof Float) {
            float floatValue = ((Float) obj).floatValue();
            int i8 = this.f14775c;
            for (int i9 = 0; i9 < i8; i9++) {
                if (this.f14774b[i9] == floatValue) {
                    return i9;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2
    /* renamed from: m */
    public final u2 z(int i8) {
        if (i8 >= this.f14775c) {
            return new h2(Arrays.copyOf(this.f14774b, i8), this.f14775c, true);
        }
        throw new IllegalArgumentException();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object remove(int i8) {
        int i9;
        e();
        i(i8);
        float[] fArr = this.f14774b;
        float f5 = fArr[i8];
        if (i8 < this.f14775c - 1) {
            System.arraycopy(fArr, i8 + 1, fArr, i8, (i9 - i8) - 1);
        }
        this.f14775c--;
        ((AbstractList) this).modCount++;
        return Float.valueOf(f5);
    }

    @Override // java.util.AbstractList
    protected final void removeRange(int i8, int i9) {
        e();
        if (i9 < i8) {
            throw new IndexOutOfBoundsException("toIndex < fromIndex");
        }
        float[] fArr = this.f14774b;
        System.arraycopy(fArr, i9, fArr, i8, this.f14775c - i9);
        this.f14775c -= i9 - i8;
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object set(int i8, Object obj) {
        float floatValue = ((Float) obj).floatValue();
        e();
        i(i8);
        float[] fArr = this.f14774b;
        float f5 = fArr[i8];
        fArr[i8] = floatValue;
        return Float.valueOf(f5);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f14775c;
    }
}
