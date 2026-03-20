package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class ec extends fc {
    @Override // com.google.android.gms.internal.measurement.fc
    final int a(int i8, byte[] bArr, int i9, int i10) {
        while (i9 < i10 && bArr[i9] >= 0) {
            i9++;
        }
        if (i9 >= i10) {
            return 0;
        }
        while (i9 < i10) {
            int i11 = i9 + 1;
            byte b9 = bArr[i9];
            if (b9 < 0) {
                if (b9 < -32) {
                    if (i11 >= i10) {
                        return b9;
                    }
                    if (b9 >= -62) {
                        i9 = i11 + 1;
                        if (bArr[i11] > -65) {
                        }
                    }
                    return -1;
                } else if (b9 >= -16) {
                    if (i11 >= i10 - 2) {
                        return dc.c(bArr, i11, i10);
                    }
                    int i12 = i11 + 1;
                    byte b10 = bArr[i11];
                    if (b10 <= -65 && (((b9 << 28) + (b10 + 112)) >> 30) == 0) {
                        int i13 = i12 + 1;
                        if (bArr[i12] <= -65) {
                            i11 = i13 + 1;
                            if (bArr[i13] > -65) {
                            }
                        }
                    }
                    return -1;
                } else if (i11 >= i10 - 1) {
                    return dc.c(bArr, i11, i10);
                } else {
                    int i14 = i11 + 1;
                    byte b11 = bArr[i11];
                    if (b11 <= -65 && ((b9 != -32 || b11 >= -96) && (b9 != -19 || b11 < -96))) {
                        i9 = i14 + 1;
                        if (bArr[i14] > -65) {
                        }
                    }
                    return -1;
                }
            }
            i9 = i11;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x001d, code lost:
        return r10 + r0;
     */
    @Override // com.google.android.gms.internal.measurement.fc
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int b(java.lang.String r8, byte[] r9, int r10, int r11) {
        /*
            Method dump skipped, instructions count: 251
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.ec.b(java.lang.String, byte[], int, int):int");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.fc
    public final String c(byte[] bArr, int i8, int i9) {
        if ((i8 | i9 | ((bArr.length - i8) - i9)) >= 0) {
            int i10 = i8 + i9;
            char[] cArr = new char[i9];
            int i11 = 0;
            while (i8 < i10) {
                byte b9 = bArr[i8];
                if (!(b9 >= 0)) {
                    break;
                }
                i8++;
                cc.d(b9, cArr, i11);
                i11++;
            }
            int i12 = i11;
            while (i8 < i10) {
                int i13 = i8 + 1;
                byte b10 = bArr[i8];
                if (b10 >= 0) {
                    int i14 = i12 + 1;
                    cc.d(b10, cArr, i12);
                    while (i13 < i10) {
                        byte b11 = bArr[i13];
                        if (!(b11 >= 0)) {
                            break;
                        }
                        i13++;
                        cc.d(b11, cArr, i14);
                        i14++;
                    }
                    i8 = i13;
                    i12 = i14;
                } else if (b10 < -32) {
                    if (i13 >= i10) {
                        throw zzkb.c();
                    }
                    cc.c(b10, bArr[i13], cArr, i12);
                    i8 = i13 + 1;
                    i12++;
                } else if (b10 < -16) {
                    if (i13 >= i10 - 1) {
                        throw zzkb.c();
                    }
                    int i15 = i13 + 1;
                    cc.b(b10, bArr[i13], bArr[i15], cArr, i12);
                    i8 = i15 + 1;
                    i12++;
                } else if (i13 >= i10 - 2) {
                    throw zzkb.c();
                } else {
                    int i16 = i13 + 1;
                    byte b12 = bArr[i13];
                    int i17 = i16 + 1;
                    cc.a(b10, b12, bArr[i16], bArr[i17], cArr, i12);
                    i8 = i17 + 1;
                    i12 = i12 + 1 + 1;
                }
            }
            return new String(cArr, 0, i12);
        }
        throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", Integer.valueOf(bArr.length), Integer.valueOf(i8), Integer.valueOf(i9)));
    }
}
