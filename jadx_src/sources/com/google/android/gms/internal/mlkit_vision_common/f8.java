package com.google.android.gms.internal.mlkit_vision_common;

import java.util.Arrays;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class f8 extends g9 {

    /* renamed from: a  reason: collision with root package name */
    Object[] f15462a = new Object[4];

    /* renamed from: b  reason: collision with root package name */
    int f15463b = 0;

    /* renamed from: c  reason: collision with root package name */
    boolean f15464c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f8(int i8) {
    }

    private final void b(int i8) {
        Object[] objArr = this.f15462a;
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
            this.f15462a = Arrays.copyOf(objArr, i9);
        } else if (!this.f15464c) {
            return;
        } else {
            this.f15462a = (Object[]) objArr.clone();
        }
        this.f15464c = false;
    }

    public final f8 a(Object obj) {
        Objects.requireNonNull(obj);
        b(this.f15463b + 1);
        Object[] objArr = this.f15462a;
        int i8 = this.f15463b;
        this.f15463b = i8 + 1;
        objArr[i8] = obj;
        return this;
    }
}
