package com.google.android.gms.internal.mlkit_vision_common;

import android.os.SystemClock;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class cc {
    public static void a(rb rbVar, int i8, int i9, long j8, int i10, int i11, int i12, int i13) {
        rbVar.c(b(i8, i9, j8, i10, i11, i12, i13), zziv.INPUT_IMAGE_CONSTRUCTION);
    }

    private static bc b(int i8, int i9, long j8, int i10, int i11, int i12, int i13) {
        return new bc(i8, i9, i12, i10, i11, SystemClock.elapsedRealtime() - j8, i13);
    }
}
