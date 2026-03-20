package com.google.android.gms.internal.measurement;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class t8 extends l7<Float> implements sa, RandomAccess {

    /* renamed from: d  reason: collision with root package name */
    private static final t8 f12523d = new t8(new float[0], 0, false);

    /* renamed from: b  reason: collision with root package name */
    private float[] f12524b;

    /* renamed from: c  reason: collision with root package name */
    private int f12525c;

    t8() {
        this(new float[10], 0, true);
    }

    private t8(float[] fArr, int i8, boolean z4) {
        super(z4);
        this.f12524b = fArr;
        this.f12525c = i8;
    }

    private final String h(int i8) {
        int i9 = this.f12525c;
        return "Index:" + i8 + ", Size:" + i9;
    }

    private final void i(int i8) {
        if (i8 < 0 || i8 >= this.f12525c) {
            throw new IndexOutOfBoundsException(h(i8));
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ void add(int i8, Object obj) {
        int i9;
        float floatValue = ((Float) obj).floatValue();
        e();
        if (i8 < 0 || i8 > (i9 = this.f12525c)) {
            throw new IndexOutOfBoundsException(h(i8));
        }
        float[] fArr = this.f12524b;
        if (i9 < fArr.length) {
            System.arraycopy(fArr, i8, fArr, i8 + 1, i9 - i8);
        } else {
            float[] fArr2 = new float[((i9 * 3) / 2) + 1];
            System.arraycopy(fArr, 0, fArr2, 0, i8);
            System.arraycopy(this.f12524b, i8, fArr2, i8 + 1, this.f12525c - i8);
            this.f12524b = fArr2;
        }
        this.f12524b[i8] = floatValue;
        this.f12525c++;
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* synthetic */ boolean add(Object obj) {
        g(((Float) obj).floatValue());
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection<? extends Float> collection) {
        e();
        a9.e(collection);
        if (collection instanceof t8) {
            t8 t8Var = (t8) collection;
            int i8 = t8Var.f12525c;
            if (i8 == 0) {
                return false;
            }
            int i9 = this.f12525c;
            if (Integer.MAX_VALUE - i9 >= i8) {
                int i10 = i9 + i8;
                float[] fArr = this.f12524b;
                if (i10 > fArr.length) {
                    this.f12524b = Arrays.copyOf(fArr, i10);
                }
                System.arraycopy(t8Var.f12524b, 0, this.f12524b, this.f12525c, t8Var.f12525c);
                this.f12525c = i10;
                ((AbstractList) this).modCount++;
                return true;
            }
            throw new OutOfMemoryError();
        }
        return super.addAll(collection);
    }

    @Override // com.google.android.gms.internal.measurement.g9
    public final /* synthetic */ g9 c(int i8) {
        if (i8 >= this.f12525c) {
            return new t8(Arrays.copyOf(this.f12524b, i8), this.f12525c, true);
        }
        throw new IllegalArgumentException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.Collection, java.util.List
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof t8) {
            t8 t8Var = (t8) obj;
            if (this.f12525c != t8Var.f12525c) {
                return false;
            }
            float[] fArr = t8Var.f12524b;
            for (int i8 = 0; i8 < this.f12525c; i8++) {
                if (Float.floatToIntBits(this.f12524b[i8]) != Float.floatToIntBits(fArr[i8])) {
                    return false;
                }
            }
            return true;
        }
        return super.equals(obj);
    }

    public final void g(float f5) {
        e();
        int i8 = this.f12525c;
        float[] fArr = this.f12524b;
        if (i8 == fArr.length) {
            float[] fArr2 = new float[((i8 * 3) / 2) + 1];
            System.arraycopy(fArr, 0, fArr2, 0, i8);
            this.f12524b = fArr2;
        }
        float[] fArr3 = this.f12524b;
        int i9 = this.f12525c;
        this.f12525c = i9 + 1;
        fArr3[i9] = f5;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object get(int i8) {
        i(i8);
        return Float.valueOf(this.f12524b[i8]);
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.Collection, java.util.List
    public final int hashCode() {
        int i8 = 1;
        for (int i9 = 0; i9 < this.f12525c; i9++) {
            i8 = (i8 * 31) + Float.floatToIntBits(this.f12524b[i9]);
        }
        return i8;
    }

    @Override // java.util.AbstractList, java.util.List
    public final int indexOf(Object obj) {
        if (obj instanceof Float) {
            float floatValue = ((Float) obj).floatValue();
            int size = size();
            for (int i8 = 0; i8 < size; i8++) {
                if (this.f12524b[i8] == floatValue) {
                    return i8;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.List
    public final /* synthetic */ Object remove(int i8) {
        int i9;
        e();
        i(i8);
        float[] fArr = this.f12524b;
        float f5 = fArr[i8];
        if (i8 < this.f12525c - 1) {
            System.arraycopy(fArr, i8 + 1, fArr, i8, (i9 - i8) - 1);
        }
        this.f12525c--;
        ((AbstractList) this).modCount++;
        return Float.valueOf(f5);
    }

    @Override // java.util.AbstractList
    protected final void removeRange(int i8, int i9) {
        e();
        if (i9 < i8) {
            throw new IndexOutOfBoundsException("toIndex < fromIndex");
        }
        float[] fArr = this.f12524b;
        System.arraycopy(fArr, i9, fArr, i8, this.f12525c - i9);
        this.f12525c -= i9 - i8;
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object set(int i8, Object obj) {
        float floatValue = ((Float) obj).floatValue();
        e();
        i(i8);
        float[] fArr = this.f12524b;
        float f5 = fArr[i8];
        fArr[i8] = floatValue;
        return Float.valueOf(f5);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f12525c;
    }
}
