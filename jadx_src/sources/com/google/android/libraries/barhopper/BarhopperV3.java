package com.google.android.libraries.barhopper;

import android.graphics.Bitmap;
import android.util.Log;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.b2;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzeo;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import r9.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class BarhopperV3 implements Closeable {
    private static final long NULL_NATIVE_CONTEXT = 0;
    private static final String TAG = BarhopperV3.class.getSimpleName();
    private long nativeContext;

    public BarhopperV3() {
        System.loadLibrary("barhopper_v3");
    }

    private native void closeNative(long j8);

    private native long createNative();

    private native long createNativeWithClientOptions(byte[] bArr);

    private native byte[] recognizeBitmapNative(long j8, Bitmap bitmap, RecognitionOptions recognitionOptions);

    private native byte[] recognizeBufferNative(long j8, int i8, int i9, ByteBuffer byteBuffer, RecognitionOptions recognitionOptions);

    private native byte[] recognizeNative(long j8, int i8, int i9, byte[] bArr, RecognitionOptions recognitionOptions);

    private native byte[] recognizeStridedBufferNative(long j8, int i8, int i9, int i10, ByteBuffer byteBuffer, RecognitionOptions recognitionOptions);

    private native byte[] recognizeStridedNative(long j8, int i8, int i9, int i10, byte[] bArr, RecognitionOptions recognitionOptions);

    private static a toProto(byte[] bArr) {
        Objects.requireNonNull(bArr);
        try {
            return a.J(bArr, b2.a());
        } catch (zzeo e8) {
            throw new IllegalStateException("Received unexpected BarhopperResponse buffer: {0}", e8);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        long j8 = this.nativeContext;
        if (j8 != 0) {
            closeNative(j8);
            this.nativeContext = 0L;
        }
    }

    public void create() {
        if (this.nativeContext != 0) {
            Log.w(TAG, "Native context already exists.");
            return;
        }
        long createNative = createNative();
        this.nativeContext = createNative;
        if (createNative == 0) {
            throw new IllegalStateException("Failed to create native context.");
        }
    }

    public void create(a8.a aVar) {
        if (this.nativeContext != 0) {
            Log.w(TAG, "Native context already exists.");
            return;
        }
        try {
            int b9 = aVar.b();
            byte[] bArr = new byte[b9];
            w1 a9 = w1.a(bArr, 0, b9);
            aVar.d(a9);
            a9.b();
            long createNativeWithClientOptions = createNativeWithClientOptions(bArr);
            this.nativeContext = createNativeWithClientOptions;
            if (createNativeWithClientOptions == 0) {
                throw new IllegalArgumentException("Failed to create native context with client options.");
            }
        } catch (IOException e8) {
            String name = aVar.getClass().getName();
            throw new RuntimeException("Serializing " + name + " to a byte array threw an IOException (should never happen).", e8);
        }
    }

    public a recognize(int i8, int i9, int i10, ByteBuffer byteBuffer, RecognitionOptions recognitionOptions) {
        long j8 = this.nativeContext;
        if (j8 != 0) {
            return toProto(recognizeStridedBufferNative(j8, i8, i9, i10, byteBuffer, recognitionOptions));
        }
        throw new IllegalStateException("Native context does not exist.");
    }

    public a recognize(int i8, int i9, int i10, byte[] bArr, RecognitionOptions recognitionOptions) {
        long j8 = this.nativeContext;
        if (j8 != 0) {
            return toProto(recognizeStridedNative(j8, i8, i9, i10, bArr, recognitionOptions));
        }
        throw new IllegalStateException("Native context does not exist.");
    }

    public a recognize(int i8, int i9, ByteBuffer byteBuffer, RecognitionOptions recognitionOptions) {
        long j8 = this.nativeContext;
        if (j8 != 0) {
            return toProto(recognizeBufferNative(j8, i8, i9, byteBuffer, recognitionOptions));
        }
        throw new IllegalStateException("Native context does not exist.");
    }

    public a recognize(int i8, int i9, byte[] bArr, RecognitionOptions recognitionOptions) {
        long j8 = this.nativeContext;
        if (j8 != 0) {
            return toProto(recognizeNative(j8, i8, i9, bArr, recognitionOptions));
        }
        throw new IllegalStateException("Native context does not exist.");
    }

    public a recognize(Bitmap bitmap, RecognitionOptions recognitionOptions) {
        if (this.nativeContext != 0) {
            if (bitmap.getConfig() != Bitmap.Config.ARGB_8888) {
                Log.d(TAG, "Input bitmap config is not ARGB_8888. Converting it to ARGB_8888 from ".concat(String.valueOf(bitmap.getConfig())));
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, bitmap.isMutable());
            }
            return toProto(recognizeBitmapNative(this.nativeContext, bitmap, recognitionOptions));
        }
        throw new IllegalStateException("Native context does not exist.");
    }
}
