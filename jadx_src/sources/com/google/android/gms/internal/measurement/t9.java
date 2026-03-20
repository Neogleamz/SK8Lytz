package com.google.android.gms.internal.measurement;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t9 extends l7<Long> implements d9, sa, RandomAccess {

    /* renamed from: d  reason: collision with root package name */
    private static final t9 f12526d = new t9(new long[0], 0, false);

    /* renamed from: b  reason: collision with root package name */
    private long[] f12527b;

    /* renamed from: c  reason: collision with root package name */
    private int f12528c;

    t9() {
        this(new long[10], 0, true);
    }

    private t9(long[] jArr, int i8, boolean z4) {
        super(z4);
        this.f12527b = jArr;
        this.f12528c = i8;
    }

    public static t9 h() {
        return f12526d;
    }

    private final String i(int i8) {
        int i9 = this.f12528c;
        return "Index:" + i8 + ", Size:" + i9;
    }

    private final void k(int i8) {
        if (i8 < 0 || i8 >= this.f12528c) {
            throw new IndexOutOfBoundsException(i(i8));
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ void add(int i8, Object obj) {
        int i9;
        long longValue = ((Long) obj).longValue();
        e();
        if (i8 < 0 || i8 > (i9 = this.f12528c)) {
            throw new IndexOutOfBoundsException(i(i8));
        }
        long[] jArr = this.f12527b;
        if (i9 < jArr.length) {
            System.arraycopy(jArr, i8, jArr, i8 + 1, i9 - i8);
        } else {
            long[] jArr2 = new long[((i9 * 3) / 2) + 1];
            System.arraycopy(jArr, 0, jArr2, 0, i8);
            System.arraycopy(this.f12527b, i8, jArr2, i8 + 1, this.f12528c - i8);
            this.f12527b = jArr2;
        }
        this.f12527b[i8] = longValue;
        this.f12528c++;
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* synthetic */ boolean add(Object obj) {
        g(((Long) obj).longValue());
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection<? extends Long> collection) {
        e();
        a9.e(collection);
        if (collection instanceof t9) {
            t9 t9Var = (t9) collection;
            int i8 = t9Var.f12528c;
            if (i8 == 0) {
                return false;
            }
            int i9 = this.f12528c;
            if (Integer.MAX_VALUE - i9 >= i8) {
                int i10 = i9 + i8;
                long[] jArr = this.f12527b;
                if (i10 > jArr.length) {
                    this.f12527b = Arrays.copyOf(jArr, i10);
                }
                System.arraycopy(t9Var.f12527b, 0, this.f12527b, this.f12528c, t9Var.f12528c);
                this.f12528c = i10;
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

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.Collection, java.util.List
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof t9) {
            t9 t9Var = (t9) obj;
            if (this.f12528c != t9Var.f12528c) {
                return false;
            }
            long[] jArr = t9Var.f12527b;
            for (int i8 = 0; i8 < this.f12528c; i8++) {
                if (this.f12527b[i8] != jArr[i8]) {
                    return false;
                }
            }
            return true;
        }
        return super.equals(obj);
    }

    public final void g(long j8) {
        e();
        int i8 = this.f12528c;
        long[] jArr = this.f12527b;
        if (i8 == jArr.length) {
            long[] jArr2 = new long[((i8 * 3) / 2) + 1];
            System.arraycopy(jArr, 0, jArr2, 0, i8);
            this.f12527b = jArr2;
        }
        long[] jArr3 = this.f12527b;
        int i9 = this.f12528c;
        this.f12528c = i9 + 1;
        jArr3[i9] = j8;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object get(int i8) {
        return Long.valueOf(j(i8));
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.Collection, java.util.List
    public final int hashCode() {
        int i8 = 1;
        for (int i9 = 0; i9 < this.f12528c; i9++) {
            i8 = (i8 * 31) + a9.b(this.f12527b[i9]);
        }
        return i8;
    }

    @Override // java.util.AbstractList, java.util.List
    public final int indexOf(Object obj) {
        if (obj instanceof Long) {
            long longValue = ((Long) obj).longValue();
            int size = size();
            for (int i8 = 0; i8 < size; i8++) {
                if (this.f12527b[i8] == longValue) {
                    return i8;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override // com.google.android.gms.internal.measurement.d9
    public final long j(int i8) {
        k(i8);
        return this.f12527b[i8];
    }

    @Override // com.google.android.gms.internal.measurement.g9
    /* renamed from: o */
    public final d9 c(int i8) {
        if (i8 >= this.f12528c) {
            return new t9(Arrays.copyOf(this.f12527b, i8), this.f12528c, true);
        }
        throw new IllegalArgumentException();
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.List
    public final /* synthetic */ Object remove(int i8) {
        int i9;
        e();
        k(i8);
        long[] jArr = this.f12527b;
        long j8 = jArr[i8];
        if (i8 < this.f12528c - 1) {
            System.arraycopy(jArr, i8 + 1, jArr, i8, (i9 - i8) - 1);
        }
        this.f12528c--;
        ((AbstractList) this).modCount++;
        return Long.valueOf(j8);
    }

    @Override // java.util.AbstractList
    protected final void removeRange(int i8, int i9) {
        e();
        if (i9 < i8) {
            throw new IndexOutOfBoundsException("toIndex < fromIndex");
        }
        long[] jArr = this.f12527b;
        System.arraycopy(jArr, i9, jArr, i8, this.f12528c - i9);
        this.f12528c -= i9 - i8;
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object set(int i8, Object obj) {
        long longValue = ((Long) obj).longValue();
        e();
        k(i8);
        long[] jArr = this.f12527b;
        long j8 = jArr[i8];
        jArr[i8] = longValue;
        return Long.valueOf(j8);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f12528c;
    }
}
