package com.google.android.gms.common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(int i8) {
        int[] iArr = {1, 2, 3};
        for (int i9 = 0; i9 < 3; i9++) {
            int i10 = iArr[i9];
            int i11 = i10 - 1;
            if (i10 == 0) {
                throw null;
            }
            if (i11 == i8) {
                return i10;
            }
        }
        return 1;
    }
}
