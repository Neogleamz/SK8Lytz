package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class a1 extends b1 {

    /* renamed from: a  reason: collision with root package name */
    Object[] f13242a = new Object[4];

    /* renamed from: b  reason: collision with root package name */
    int f13243b = 0;

    /* renamed from: c  reason: collision with root package name */
    boolean f13244c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a1(int i8) {
    }

    private final void d(int i8) {
        Object[] objArr = this.f13242a;
        int length = objArr.length;
        if (length < i8) {
            int i9 = length + (length >> 1) + 1;
            if (i9 < i8) {
                int highestOneBit = Integer.highestOneBit(i8 - 1);
                i9 = highestOneBit + highestOneBit;
            }
            if (i9 < 0) {
                i9 = Integer.MAX_VALUE;
            }
            this.f13242a = Arrays.copyOf(objArr, i9);
        } else if (!this.f13244c) {
            return;
        } else {
            this.f13242a = (Object[]) objArr.clone();
        }
        this.f13244c = false;
    }

    public final a1 b(Object obj) {
        Objects.requireNonNull(obj);
        d(this.f13243b + 1);
        Object[] objArr = this.f13242a;
        int i8 = this.f13243b;
        this.f13243b = i8 + 1;
        objArr[i8] = obj;
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final b1 c(Iterable iterable) {
        if (iterable instanceof Collection) {
            d(this.f13243b + iterable.size());
            if (iterable instanceof zzcq) {
                this.f13243b = ((zzcq) iterable).e(this.f13242a, this.f13243b);
                return this;
            }
        }
        for (Object obj : iterable) {
            a(obj);
        }
        return this;
    }
}
