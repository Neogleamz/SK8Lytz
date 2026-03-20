package com.google.android.libraries.barhopper;

import com.google.android.apps.common.proguard.UsedByNative;
@UsedByNative("jni_common.cc")
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class RecognitionOptions {
    public static final int AZTEC = 4096;
    public static final int CODABAR = 8;
    public static final int CODE_128 = 1;
    public static final int CODE_39 = 2;
    public static final int CODE_93 = 4;
    public static final int DATA_MATRIX = 16;
    public static final int EAN_13 = 32;
    public static final int EAN_8 = 64;
    public static final int ITF = 128;
    public static final int LOCAL_BLOCK = 1;
    public static final int NIBLACK_W16_K00 = 32;
    public static final int NIBLACK_W32_K00 = 16;
    public static final int NIBLACK_W64_K02 = 8;
    public static final int NIBLACK_W75_K02 = 4;
    public static final int ONED = 2;
    public static final int PDF417 = 2048;
    public static final int QR_CODE = 256;
    public static final int TEZ_CODE = 32768;
    public static final int UNRECOGNIZED = 0;
    public static final int UPC_A = 512;
    public static final int UPC_E = 1024;
    @UsedByNative("jni_common.cc")
    private int barcodeFormats = 0;
    @UsedByNative("jni_common.cc")
    private boolean outputUnrecognizedBarcodes = false;
    @UsedByNative("jni_common.cc")
    private boolean useQrMobilenetV3 = false;
    @UsedByNative("jni_common.cc")
    private boolean enableQrAlignmentGrid = false;
    @UsedByNative("jni_common.cc")
    private boolean enableUseKeypointAsFinderPattern = false;
    @UsedByNative("jni_common.cc")
    private int binarizerOptions = 3;
    @UsedByNative("jni_common.cc")
    private OnedRecognitionOptions onedRecognitionOptions = new OnedRecognitionOptions();

    public int getBarcodeFormats() {
        return this.barcodeFormats;
    }

    public int getBinarizerOptions() {
        return this.binarizerOptions;
    }

    public boolean getEnableQrAlignmentGrid() {
        return this.enableQrAlignmentGrid;
    }

    public boolean getEnableUseKeypointAsFinderPattern() {
        return this.enableUseKeypointAsFinderPattern;
    }

    public OnedRecognitionOptions getOnedRecognitionOptions() {
        return this.onedRecognitionOptions;
    }

    public boolean getOutputUnrecognizedBarcodes() {
        return this.outputUnrecognizedBarcodes;
    }

    public boolean getUseQrMobilenetV3() {
        return this.useQrMobilenetV3;
    }

    public void setBarcodeFormats(int i8) {
        this.barcodeFormats = i8;
    }

    public void setBinarizerOptions(int i8) {
        this.binarizerOptions = i8;
    }

    public void setEnableQrAlignmentGrid(boolean z4) {
        this.enableQrAlignmentGrid = z4;
    }

    public void setEnableUseKeypointAsFinderPattern(boolean z4) {
        this.enableUseKeypointAsFinderPattern = z4;
    }

    public void setOnedRecognitionOptions(OnedRecognitionOptions onedRecognitionOptions) {
        this.onedRecognitionOptions = onedRecognitionOptions;
    }

    public void setOutputUnrecognizedBarcodes(boolean z4) {
        this.outputUnrecognizedBarcodes = z4;
    }

    public void setUseQrMobilenetV3(boolean z4) {
        this.useQrMobilenetV3 = z4;
    }
}
