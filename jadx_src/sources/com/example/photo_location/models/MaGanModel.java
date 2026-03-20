package com.example.photo_location.models;

import android.graphics.Bitmap;
import cm.b;
import em.a;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.tensorflow.lite.support.image.e;
import org.tensorflow.lite.support.image.ops.ResizeOp;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MaGanModel {
    private static final String TAG = "com.example.photo_location.models.MaGanModel";
    private final a boxes;
    private final ByteBuffer classes;
    private final b imageProcessor;
    private final ImgProcessor imgProcessor;
    private ProgressListening listening;
    private final TFLiteModel model;
    private final ByteBuffer numbers;
    private final a scores;
    private final e tensorImage;
    public int imageWidth = 0;
    public int imageHeiht = 0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class BitmapResult {
        public Bitmap bitmap;
        public Bitmap originBitmap;
        public ResultBox resultBox;

        public BitmapResult(Bitmap bitmap, Bitmap bitmap2, ResultBox resultBox) {
            this.bitmap = bitmap;
            this.originBitmap = bitmap2;
            this.resultBox = resultBox;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface ProgressListening {
        void onSendProgress(int i8);
    }

    public MaGanModel(TFLiteModel tFLiteModel, ImgProcessor imgProcessor) {
        this.model = tFLiteModel;
        this.tensorImage = new e(tFLiteModel.inputDataTypes.get(0));
        this.imgProcessor = imgProcessor;
        int[] iArr = tFLiteModel.inputShapes.get(0);
        this.imageProcessor = new b.b().e(new ResizeOp(iArr[1], iArr[2], ResizeOp.ResizeMethod.a)).d(new bm.a(0.0f, 0.003921569f)).f();
        this.boxes = a.e(new int[]{1, 2535, 4}, tFLiteModel.outputDataTypes.get(0));
        this.scores = a.e(new int[]{1, 2535, 1}, tFLiteModel.outputDataTypes.get(1));
        this.classes = ByteBuffer.allocate(20280);
        this.numbers = ByteBuffer.allocate(4);
    }

    private float[] toArrayFloat(List<Float> list) {
        int size = list.size();
        float[] fArr = new float[size];
        for (int i8 = 0; i8 < size; i8++) {
            fArr[i8] = list.get(i8).floatValue();
        }
        return fArr;
    }

    private int[] toArrayInt(List<Integer> list) {
        int size = list.size();
        int[] iArr = new int[size];
        for (int i8 = 0; i8 < size; i8++) {
            iArr[i8] = list.get(i8).intValue();
        }
        return iArr;
    }

    public List<int[]> getSplitIndex(int i8, int i9, int i10, int i11) {
        int i12;
        int i13;
        int i14;
        this.imageWidth = i8;
        this.imageHeiht = i9;
        ArrayList arrayList = new ArrayList();
        int i15 = 0;
        int i16 = 0;
        while (true) {
            int i17 = i10 - i11;
            int i18 = i15 * i17;
            int i19 = i18 + i10;
            if (i19 > i8) {
                i12 = i8 - i18;
                i19 = i8;
            } else {
                i12 = i10;
            }
            int i20 = i17 * i16;
            int i21 = i20 + i10;
            if (i21 > i9) {
                i14 = i9 - i20;
                i13 = i9;
            } else {
                i13 = i21;
                i14 = i10;
            }
            arrayList.add(new int[]{i18, i20, i12, i14});
            if (i19 == i8 && i13 == i9) {
                return arrayList;
            }
            if (i19 == i8) {
                i16++;
                i15 = 0;
            } else {
                i15++;
            }
        }
    }

    public ResultBox merge(ResultBox resultBox, ResultBox resultBox2, int i8) {
        return this.imgProcessor.merge(resultBox, resultBox2, i8, this.imageWidth, this.imageHeiht);
    }

    public BitmapResult predict(List<Bitmap> list, String str, String str2, long j8, int i8) {
        List<Bitmap> list2 = list;
        float[][] fArr = new float[list.size()];
        float[][] fArr2 = new float[list.size()];
        int[][] iArr = new int[list.size()];
        int[] iArr2 = new int[list.size()];
        int i9 = 0;
        int i10 = 0;
        while (i10 < list.size()) {
            Bitmap bitmap = list2.get(i10);
            List<int[]> splitIndex = getSplitIndex(bitmap.getWidth(), bitmap.getHeight(), 416, 20);
            ArrayList arrayList = new ArrayList(12288);
            ArrayList arrayList2 = new ArrayList(3072);
            ArrayList arrayList3 = new ArrayList(3072);
            int i11 = i9;
            int i12 = i11;
            while (i11 < splitIndex.size()) {
                int[] iArr3 = splitIndex.get(i11);
                List<int[]> list3 = splitIndex;
                int[] iArr4 = iArr2;
                Bitmap createBitmap = Bitmap.createBitmap(bitmap, iArr3[i9], iArr3[1], iArr3[2], iArr3[3]);
                this.tensorImage.d(createBitmap);
                HashMap hashMap = new HashMap();
                Bitmap bitmap2 = bitmap;
                hashMap.put(0, this.boxes.g().rewind());
                hashMap.put(1, this.scores.g().rewind());
                hashMap.put(2, this.classes.rewind());
                int[][] iArr5 = iArr;
                this.model.interpreter.h(new Object[]{((e) this.imageProcessor.a(this.tensorImage)).b()}, hashMap);
                BestBoxes nms = this.imgProcessor.nms(this.boxes.i(), this.scores.i(), toClass());
                float[][] fArr3 = nms.boxes;
                float[] fArr4 = nms.scores;
                int[] iArr6 = nms.classes;
                int width = createBitmap.getWidth();
                int height = createBitmap.getHeight();
                int length = i12 + fArr3.length;
                int i13 = 0;
                while (i13 < fArr3.length) {
                    float[] fArr5 = fArr3[i13];
                    float f5 = fArr5[0];
                    float f8 = fArr5[1];
                    float f9 = fArr5[2];
                    float f10 = fArr5[3];
                    float[][] fArr6 = fArr2;
                    float f11 = width;
                    arrayList.add(Float.valueOf(iArr3[0] + (f5 * f11)));
                    int i14 = width;
                    float f12 = height;
                    arrayList.add(Float.valueOf(iArr3[1] + (f8 * f12)));
                    arrayList.add(Float.valueOf(iArr3[0] + (f9 * f11)));
                    arrayList.add(Float.valueOf(iArr3[1] + (f10 * f12)));
                    arrayList2.add(Float.valueOf(fArr4[i13]));
                    arrayList3.add(Integer.valueOf(iArr6[i13]));
                    i13++;
                    width = i14;
                    height = height;
                    fArr3 = fArr3;
                    fArr2 = fArr6;
                }
                i11++;
                splitIndex = list3;
                iArr2 = iArr4;
                bitmap = bitmap2;
                iArr = iArr5;
                i12 = length;
                i9 = 0;
            }
            float[][] fArr7 = fArr2;
            int[][] iArr7 = iArr;
            int[] iArr8 = iArr2;
            fArr[i10] = toArrayFloat(arrayList);
            fArr7[i10] = toArrayFloat(arrayList2);
            iArr7[i10] = toArrayInt(arrayList3);
            iArr8[i10] = i12;
            ProgressListening progressListening = this.listening;
            if (progressListening != null) {
                progressListening.onSendProgress(i10 + 1);
            }
            i10++;
            list2 = list;
            iArr2 = iArr8;
            iArr = iArr7;
            fArr2 = fArr7;
            i9 = 0;
        }
        int i15 = i9;
        float[][] fArr8 = fArr2;
        List<Bitmap> list4 = list2;
        Bitmap bitmap3 = list4.get(i15);
        ResultBox run = this.imgProcessor.run(fArr, fArr8, iArr, iArr2, 600, bitmap3.getWidth(), bitmap3.getHeight(), i8, str, str2, j8);
        if (run == null) {
            return null;
        }
        Bitmap bitmap4 = list4.get(0);
        return new BitmapResult(bitmap4.copy(Bitmap.Config.ARGB_8888, true), bitmap4, run);
    }

    public BitmapResult predict2(List<Bitmap> list, int i8, String str, String str2, long j8, int i9) {
        List<Bitmap> list2 = list;
        float[][] fArr = new float[list.size()];
        float[][] fArr2 = new float[list.size()];
        int[][] iArr = new int[list.size()];
        int[] iArr2 = new int[list.size()];
        int i10 = 0;
        int i11 = 0;
        while (i11 < list.size()) {
            Bitmap captureImage = ImageUtil.captureImage(list2.get(i11));
            List<int[]> splitIndex = getSplitIndex(captureImage.getWidth(), captureImage.getHeight(), 416, 20);
            ArrayList arrayList = new ArrayList(12288);
            ArrayList arrayList2 = new ArrayList(3072);
            ArrayList arrayList3 = new ArrayList(3072);
            int i12 = i10;
            int i13 = i12;
            while (i12 < splitIndex.size()) {
                int[] iArr3 = splitIndex.get(i12);
                List<int[]> list3 = splitIndex;
                int[] iArr4 = iArr2;
                Bitmap createBitmap = Bitmap.createBitmap(captureImage, iArr3[i10], iArr3[1], iArr3[2], iArr3[3]);
                this.tensorImage.d(createBitmap);
                HashMap hashMap = new HashMap();
                Bitmap bitmap = captureImage;
                hashMap.put(0, this.boxes.g().rewind());
                hashMap.put(1, this.scores.g().rewind());
                hashMap.put(2, this.classes.rewind());
                int[][] iArr5 = iArr;
                this.model.interpreter.h(new Object[]{((e) this.imageProcessor.a(this.tensorImage)).b()}, hashMap);
                BestBoxes nms = this.imgProcessor.nms(this.boxes.i(), this.scores.i(), toClass());
                float[][] fArr3 = nms.boxes;
                float[] fArr4 = nms.scores;
                int[] iArr6 = nms.classes;
                int width = createBitmap.getWidth();
                int height = createBitmap.getHeight();
                int length = i13 + fArr3.length;
                int i14 = 0;
                while (i14 < fArr3.length) {
                    float[] fArr5 = fArr3[i14];
                    float f5 = fArr5[0];
                    float f8 = fArr5[1];
                    float f9 = fArr5[2];
                    float f10 = fArr5[3];
                    float[][] fArr6 = fArr2;
                    float f11 = width;
                    arrayList.add(Float.valueOf(iArr3[0] + (f5 * f11)));
                    int i15 = width;
                    float f12 = height;
                    arrayList.add(Float.valueOf(iArr3[1] + (f8 * f12)));
                    arrayList.add(Float.valueOf(iArr3[0] + (f9 * f11)));
                    arrayList.add(Float.valueOf(iArr3[1] + (f10 * f12)));
                    arrayList2.add(Float.valueOf(fArr4[i14]));
                    arrayList3.add(Integer.valueOf(iArr6[i14]));
                    i14++;
                    width = i15;
                    height = height;
                    fArr3 = fArr3;
                    fArr2 = fArr6;
                }
                i12++;
                splitIndex = list3;
                iArr2 = iArr4;
                captureImage = bitmap;
                iArr = iArr5;
                i13 = length;
                i10 = 0;
            }
            float[][] fArr7 = fArr2;
            int[][] iArr7 = iArr;
            int[] iArr8 = iArr2;
            fArr[i11] = toArrayFloat(arrayList);
            fArr7[i11] = toArrayFloat(arrayList2);
            iArr7[i11] = toArrayInt(arrayList3);
            iArr8[i11] = i13;
            ProgressListening progressListening = this.listening;
            if (progressListening != null) {
                progressListening.onSendProgress(i11 + 1);
            }
            i11++;
            list2 = list;
            iArr2 = iArr8;
            iArr = iArr7;
            fArr2 = fArr7;
            i10 = 0;
        }
        int i16 = i10;
        float[][] fArr8 = fArr2;
        List<Bitmap> list4 = list2;
        Bitmap bitmap2 = list4.get(i16);
        ResultBox run = this.imgProcessor.run(fArr, fArr8, iArr, iArr2, i8, bitmap2.getWidth(), bitmap2.getHeight(), i9, str, str2, j8);
        if (run == null) {
            return null;
        }
        Bitmap bitmap3 = list4.get(0);
        return new BitmapResult(bitmap3.copy(Bitmap.Config.ARGB_8888, true), bitmap3, run);
    }

    public void startListeningPropress(ProgressListening progressListening) {
        this.listening = progressListening;
    }

    public int[] toClass() {
        byte[] array = this.classes.array();
        int[] iArr = new int[array.length / 8];
        int i8 = 0;
        int i9 = 0;
        while (i8 < array.length) {
            iArr[i9] = ((array[i8 + 3] & 255) << 24) | ((array[i8 + 2] & 255) << 16) | ((array[i8 + 1] & 255) << 8) | (array[i8] & 255);
            i8 += 8;
            i9++;
        }
        return iArr;
    }

    public int toNum() {
        byte[] array = this.numbers.array();
        return (array[0] & 255) | ((array[3] & 255) << 24) | ((array[2] & 255) << 16) | ((array[1] & 255) << 8);
    }
}
