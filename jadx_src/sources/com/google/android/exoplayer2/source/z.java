package com.google.android.exoplayer2.source;

import android.util.SparseArray;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class z<V> {

    /* renamed from: c  reason: collision with root package name */
    private final b6.h<V> f10851c;

    /* renamed from: b  reason: collision with root package name */
    private final SparseArray<V> f10850b = new SparseArray<>();

    /* renamed from: a  reason: collision with root package name */
    private int f10849a = -1;

    public z(b6.h<V> hVar) {
        this.f10851c = hVar;
    }

    public void a(int i8, V v8) {
        if (this.f10849a == -1) {
            b6.a.f(this.f10850b.size() == 0);
            this.f10849a = 0;
        }
        if (this.f10850b.size() > 0) {
            SparseArray<V> sparseArray = this.f10850b;
            int keyAt = sparseArray.keyAt(sparseArray.size() - 1);
            b6.a.a(i8 >= keyAt);
            if (keyAt == i8) {
                SparseArray<V> sparseArray2 = this.f10850b;
                this.f10851c.accept(sparseArray2.valueAt(sparseArray2.size() - 1));
            }
        }
        this.f10850b.append(i8, v8);
    }

    public void b() {
        for (int i8 = 0; i8 < this.f10850b.size(); i8++) {
            this.f10851c.accept(this.f10850b.valueAt(i8));
        }
        this.f10849a = -1;
        this.f10850b.clear();
    }

    public void c(int i8) {
        for (int size = this.f10850b.size() - 1; size >= 0 && i8 < this.f10850b.keyAt(size); size--) {
            this.f10851c.accept(this.f10850b.valueAt(size));
            this.f10850b.removeAt(size);
        }
        this.f10849a = this.f10850b.size() > 0 ? Math.min(this.f10849a, this.f10850b.size() - 1) : -1;
    }

    public void d(int i8) {
        int i9 = 0;
        while (i9 < this.f10850b.size() - 1) {
            int i10 = i9 + 1;
            if (i8 < this.f10850b.keyAt(i10)) {
                return;
            }
            this.f10851c.accept(this.f10850b.valueAt(i9));
            this.f10850b.removeAt(i9);
            int i11 = this.f10849a;
            if (i11 > 0) {
                this.f10849a = i11 - 1;
            }
            i9 = i10;
        }
    }

    public V e(int i8) {
        if (this.f10849a == -1) {
            this.f10849a = 0;
        }
        while (true) {
            int i9 = this.f10849a;
            if (i9 <= 0 || i8 >= this.f10850b.keyAt(i9)) {
                break;
            }
            this.f10849a--;
        }
        while (this.f10849a < this.f10850b.size() - 1 && i8 >= this.f10850b.keyAt(this.f10849a + 1)) {
            this.f10849a++;
        }
        return this.f10850b.valueAt(this.f10849a);
    }

    public V f() {
        SparseArray<V> sparseArray = this.f10850b;
        return sparseArray.valueAt(sparseArray.size() - 1);
    }

    public boolean g() {
        return this.f10850b.size() == 0;
    }
}
