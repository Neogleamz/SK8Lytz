package com.example.blelibrary.protocol.layer;

import c3.a;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class LowerTransportLayerEncoder {
    private static final String TAG = "com.example.blelibrary.protocol.layer.LowerTransportLayerEncoder";

    private static int calculateSegNum(byte[] bArr, int i8) {
        int length = (bArr.length - i8) + 8;
        int i9 = i8 - 5;
        if (length <= i9) {
            return 2;
        }
        int i10 = length % i9;
        int i11 = (length / i9) + 1;
        return i10 == 0 ? i11 : i11 + 1;
    }

    private static byte createCtrl(boolean z4, boolean z8, boolean z9) {
        byte a9 = a.a((byte) 0, 7);
        byte d8 = z9 ? a.d(a9, 6) : a.a(a9, 6);
        byte d9 = z4 ? a.d(d8, 5) : a.a(d8, 5);
        return a.a(a.a(a.a(a.a(z8 ? a.d(d9, 4) : a.a(d9, 4), 3), 2), 1), 0);
    }

    public static List<byte[]> generator(UpperTransportLayer upperTransportLayer, int i8) {
        ArrayList arrayList;
        int i9;
        int i10;
        int i11;
        byte[] bArr;
        byte[] payload = upperTransportLayer.getPayload();
        boolean isProtect = upperTransportLayer.isProtect();
        boolean isAck = upperTransportLayer.isAck();
        int i12 = i8 - 8;
        boolean z4 = false;
        if (payload.length <= i12) {
            byte[] bArr2 = new byte[payload.length + 8];
            set8Bytes(bArr2, isProtect, isAck, upperTransportLayer.getSeq(), false, Short.MIN_VALUE, upperTransportLayer.getCmdId(), payload.length, payload.length + 1);
            System.arraycopy(upperTransportLayer.getPayload(), 0, bArr2, 8, payload.length);
            return Collections.singletonList(bArr2);
        }
        int calculateSegNum = calculateSegNum(payload, i8);
        ArrayList arrayList2 = new ArrayList(calculateSegNum);
        int i13 = 0;
        int i14 = 0;
        while (i14 < calculateSegNum) {
            if (i14 == 0) {
                byte[] bArr3 = new byte[i8];
                arrayList = arrayList2;
                i9 = calculateSegNum;
                set8Bytes(bArr3, isProtect, isAck, upperTransportLayer.getSeq(), true, (short) 0, upperTransportLayer.getCmdId(), payload.length, i8 - 7);
                System.arraycopy(upperTransportLayer.getPayload(), 0, bArr3, 8, i12);
                i11 = i12;
                bArr = bArr3;
                i10 = i14;
            } else {
                arrayList = arrayList2;
                i9 = calculateSegNum;
                i10 = i14;
                short s8 = i10 == i9 + (-1) ? (short) (32768 | i10) : (short) i10;
                int min = Math.min(upperTransportLayer.getPayload().length - i13, i8 - 5);
                byte[] bArr4 = new byte[min + 5];
                int i15 = i13;
                set6Bytes(bArr4, isProtect, isAck, upperTransportLayer.getSeq(), s8, min);
                System.arraycopy(upperTransportLayer.getPayload(), i15, bArr4, 5, min);
                i11 = i15 + min;
                bArr = bArr4;
            }
            ArrayList arrayList3 = arrayList;
            arrayList3.add(bArr);
            i14 = i10 + 1;
            i13 = i11;
            arrayList2 = arrayList3;
            calculateSegNum = i9;
            z4 = false;
        }
        return arrayList2;
    }

    private static void set6Bytes(byte[] bArr, boolean z4, boolean z8, byte b9, short s8, int i8) {
        bArr[0] = createCtrl(z4, z8, true);
        bArr[1] = b9;
        bArr[2] = (byte) ((65280 & s8) >> 8);
        bArr[3] = (byte) (s8 & 255);
        bArr[4] = (byte) i8;
    }

    private static void set8Bytes(byte[] bArr, boolean z4, boolean z8, byte b9, boolean z9, short s8, byte b10, int i8, int i9) {
        bArr[0] = createCtrl(z4, z8, z9);
        bArr[1] = b9;
        bArr[2] = (byte) ((s8 & 65280) >> 8);
        bArr[3] = (byte) (s8 & 255);
        bArr[4] = (byte) ((65280 & i8) >> 8);
        bArr[5] = (byte) (i8 & 255);
        bArr[6] = (byte) i9;
        bArr[7] = b10;
    }
}
