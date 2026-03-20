package com.example.blelibrary.protocol.layer;

import android.util.Log;
import c3.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class LowerTransportLayerDecoder {
    private static final String TAG = "com.example.blelibrary.protocol.layer.LowerTransportLayerDecoder";
    private int receiveSeq = -1;
    private int totalLength = -1;
    private int prevSegmentNum = -1;
    private int cmdId = -1;
    private byte[] buffer = null;
    private int prevIndex = -1;

    private UpperTransportLayer getData(byte b9) {
        UpperTransportLayer upperTransportLayer = new UpperTransportLayer(false, false, (byte) this.receiveSeq, (byte) this.cmdId, this.buffer);
        resetAssemblyFlag();
        return upperTransportLayer;
    }

    private void resetAssemblyFlag() {
        this.receiveSeq = -1;
        this.totalLength = -1;
        this.prevSegmentNum = -1;
        this.cmdId = -1;
        this.prevIndex = -1;
        this.buffer = null;
    }

    public UpperTransportLayer getTransport(byte[] bArr) {
        String str = TAG;
        Log.i(str, "HEX : " + b.a(bArr, bArr.length) + " length " + bArr.length);
        if (bArr.length <= 0) {
            Log.e(str, "Empty data...");
            return null;
        }
        byte b9 = bArr[0];
        if ((b9 & 64) != 64) {
            if (bArr.length < 8) {
                Log.i(str, "byte length error : " + bArr.length);
                return null;
            } else if (((((bArr[2] << 8) & 65280) | (bArr[3] & 255)) & 65535) != 32768) {
                Log.i(str, "byte frag ctrl error : " + (((bArr[3] & 255) | ((bArr[2] << 8) & 65280)) & 65535));
                return null;
            } else {
                this.cmdId = bArr[7];
                this.receiveSeq = bArr[1];
                System.out.println();
                int i8 = (((bArr[4] << 8) & 65280) | (bArr[5] & 255)) & 65535;
                this.totalLength = i8;
                byte[] bArr2 = new byte[i8];
                this.buffer = bArr2;
                System.arraycopy(bArr, 8, bArr2, 0, bArr2.length);
                return getData(b9);
            }
        } else if (bArr.length < 4) {
            Log.i(str, "byte length error : " + bArr.length);
            resetAssemblyFlag();
            return null;
        } else {
            int i9 = (((bArr[2] << 8) & 32512) | (bArr[3] & 255)) & 65535;
            if (((((bArr[2] << 8) & 65280) | (bArr[3] & 255)) & 65535) == 0) {
                resetAssemblyFlag();
                if (bArr.length < 8) {
                    Log.i(str, "byte length error : " + bArr.length);
                    return null;
                }
                this.receiveSeq = bArr[1];
                int i10 = (((bArr[4] << 8) & 65280) | (bArr[5] & 255)) & 65535;
                this.totalLength = i10;
                byte[] bArr3 = new byte[i10];
                this.buffer = bArr3;
                this.prevSegmentNum = i9;
                this.cmdId = bArr[7];
                System.arraycopy(bArr, 8, bArr3, 0, (bArr[6] & 255) - 1);
                this.prevIndex = bArr.length - 8;
            } else if (this.receiveSeq == bArr[1] && this.prevSegmentNum == i9 - 1) {
                int i11 = bArr[4] & 255;
                System.arraycopy(bArr, 5, this.buffer, this.prevIndex, i11);
                this.receiveSeq = bArr[1];
                this.prevSegmentNum = i9;
                this.prevIndex += i11;
                if ((bArr[2] & 128) == 128) {
                    return getData(b9);
                }
            } else {
                resetAssemblyFlag();
                this.buffer = null;
            }
            return null;
        }
    }
}
