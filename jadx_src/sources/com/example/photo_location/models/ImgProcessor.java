package com.example.photo_location.models;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ImgProcessor {
    static {
        System.loadLibrary("img_processor");
        System.loadLibrary("crypto");
        System.loadLibrary("ssl");
    }

    public native int initConfig(int[][] iArr, int[][] iArr2, int[][] iArr3);

    public native ResultBox merge(ResultBox resultBox, ResultBox resultBox2, int i8, int i9, int i10);

    public native BestBoxes nms(float[] fArr, float[] fArr2, int[] iArr);

    public native ResultBox run(float[][] fArr, float[][] fArr2, int[][] iArr, int[] iArr2, int i8, int i9, int i10, int i11, String str, String str2, long j8);
}
