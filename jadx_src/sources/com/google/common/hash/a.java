package com.google.common.hash;

import com.google.common.base.l;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLongArray;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    final AtomicLongArray f19556a;

    /* renamed from: b  reason: collision with root package name */
    private final b f19557b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(long[] jArr) {
        l.e(jArr.length > 0, "data length is zero!");
        this.f19556a = new AtomicLongArray(jArr);
        this.f19557b = c.a();
        long j8 = 0;
        for (long j9 : jArr) {
            j8 += Long.bitCount(j9);
        }
        this.f19557b.a(j8);
    }

    public static long[] a(AtomicLongArray atomicLongArray) {
        int length = atomicLongArray.length();
        long[] jArr = new long[length];
        for (int i8 = 0; i8 < length; i8++) {
            jArr[i8] = atomicLongArray.get(i8);
        }
        return jArr;
    }

    public boolean equals(Object obj) {
        if (obj instanceof a) {
            return Arrays.equals(a(this.f19556a), a(((a) obj).f19556a));
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(a(this.f19556a));
    }
}
