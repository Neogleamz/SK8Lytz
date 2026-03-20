package com.example.photo_location.models;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.a;
import org.tensorflow.lite.nnapi.NnApiDelegate;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class TFLiteModel {
    private static final int NUM_THREADS = 10;
    private static final String TAG = "com.example.photo_location.models.TFLiteModel";
    public final Context context;
    public List<DataType> inputDataTypes;
    public List<int[]> inputShapes;
    public int inputTensorCount;
    public a interpreter;
    public List<DataType> outputDataTypes;
    public List<int[]> outputShapes;
    public int outputTensorCount;

    public TFLiteModel(Context context, String str, boolean z4) {
        this.inputTensorCount = 0;
        this.outputTensorCount = 0;
        this.context = context;
        MappedByteBuffer loadModel = loadModel(str);
        if (loadModel == null) {
            Log.e(TAG, "model path is null : " + str);
            return;
        }
        a.a aVar = new a.a();
        if (Build.VERSION.SDK_INT >= 28) {
            aVar.a(new NnApiDelegate());
        } else {
            aVar.b(10);
        }
        a aVar2 = new a(loadModel, aVar);
        this.interpreter = aVar2;
        this.inputTensorCount = aVar2.c();
        this.outputTensorCount = this.interpreter.f();
        this.inputShapes = new ArrayList(this.inputTensorCount);
        this.outputShapes = new ArrayList(this.outputTensorCount);
        this.inputDataTypes = new ArrayList(this.inputTensorCount);
        this.outputDataTypes = new ArrayList(this.outputTensorCount);
        for (int i8 = 0; i8 < this.inputTensorCount; i8++) {
            this.inputShapes.add(this.interpreter.b(i8).s());
        }
        for (int i9 = 0; i9 < this.outputTensorCount; i9++) {
            this.outputShapes.add(this.interpreter.d(i9).s());
        }
        for (int i10 = 0; i10 < this.inputTensorCount; i10++) {
            this.inputDataTypes.add(this.interpreter.b(i10).g());
        }
        for (int i11 = 0; i11 < this.outputTensorCount; i11++) {
            this.outputDataTypes.add(this.interpreter.d(i11).g());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0059 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.nio.MappedByteBuffer loadModel(java.lang.String r10) {
        /*
            r9 = this;
            r0 = 0
            android.content.Context r1 = r9.context     // Catch: java.lang.Throwable -> L37 java.io.IOException -> L39
            android.content.res.AssetManager r1 = r1.getAssets()     // Catch: java.lang.Throwable -> L37 java.io.IOException -> L39
            android.content.res.AssetFileDescriptor r10 = r1.openFd(r10)     // Catch: java.lang.Throwable -> L37 java.io.IOException -> L39
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L37 java.io.IOException -> L39
            java.io.FileDescriptor r2 = r10.getFileDescriptor()     // Catch: java.lang.Throwable -> L37 java.io.IOException -> L39
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L37 java.io.IOException -> L39
            java.nio.channels.FileChannel r3 = r1.getChannel()     // Catch: java.io.IOException -> L35 java.lang.Throwable -> L55
            java.nio.channels.FileChannel$MapMode r4 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch: java.io.IOException -> L35 java.lang.Throwable -> L55
            long r5 = r10.getStartOffset()     // Catch: java.io.IOException -> L35 java.lang.Throwable -> L55
            long r7 = r10.getDeclaredLength()     // Catch: java.io.IOException -> L35 java.lang.Throwable -> L55
            java.nio.MappedByteBuffer r10 = r3.map(r4, r5, r7)     // Catch: java.io.IOException -> L35 java.lang.Throwable -> L55
            r1.close()     // Catch: java.io.IOException -> L2a
            goto L34
        L2a:
            r0 = move-exception
            java.lang.String r1 = com.example.photo_location.models.TFLiteModel.TAG
            java.lang.String r0 = r0.getMessage()
            android.util.Log.e(r1, r0)
        L34:
            return r10
        L35:
            r10 = move-exception
            goto L3b
        L37:
            r10 = move-exception
            goto L57
        L39:
            r10 = move-exception
            r1 = r0
        L3b:
            java.lang.String r2 = com.example.photo_location.models.TFLiteModel.TAG     // Catch: java.lang.Throwable -> L55
            java.lang.String r10 = r10.getMessage()     // Catch: java.lang.Throwable -> L55
            android.util.Log.e(r2, r10)     // Catch: java.lang.Throwable -> L55
            if (r1 == 0) goto L54
            r1.close()     // Catch: java.io.IOException -> L4a
            goto L54
        L4a:
            r10 = move-exception
            java.lang.String r1 = com.example.photo_location.models.TFLiteModel.TAG
            java.lang.String r10 = r10.getMessage()
            android.util.Log.e(r1, r10)
        L54:
            return r0
        L55:
            r10 = move-exception
            r0 = r1
        L57:
            if (r0 == 0) goto L67
            r0.close()     // Catch: java.io.IOException -> L5d
            goto L67
        L5d:
            r0 = move-exception
            java.lang.String r1 = com.example.photo_location.models.TFLiteModel.TAG
            java.lang.String r0 = r0.getMessage()
            android.util.Log.e(r1, r0)
        L67:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.photo_location.models.TFLiteModel.loadModel(java.lang.String):java.nio.MappedByteBuffer");
    }

    public static MaGanModel maGanModel(Context context, String str) {
        TFLiteModel tFLiteModel = new TFLiteModel(context, str, false);
        ImgProcessor imgProcessor = new ImgProcessor();
        imgProcessor.initConfig(LoadConfig.small(context), LoadConfig.medium(context), LoadConfig.large(context));
        return new MaGanModel(tFLiteModel, imgProcessor);
    }
}
