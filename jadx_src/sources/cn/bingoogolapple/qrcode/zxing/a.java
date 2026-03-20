package cn.bingoogolapple.qrcode.zxing;

import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.b;
import com.google.zxing.e;
import com.google.zxing.h;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import w9.g;
import w9.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    static final Map<DecodeHintType, Object> f8634a;

    /* renamed from: b  reason: collision with root package name */
    static final Map<DecodeHintType, Object> f8635b;

    /* renamed from: c  reason: collision with root package name */
    static final Map<DecodeHintType, Object> f8636c;

    /* renamed from: d  reason: collision with root package name */
    static final Map<DecodeHintType, Object> f8637d;

    /* renamed from: e  reason: collision with root package name */
    static final Map<DecodeHintType, Object> f8638e;

    /* renamed from: f  reason: collision with root package name */
    static final Map<DecodeHintType, Object> f8639f;

    /* renamed from: g  reason: collision with root package name */
    static final Map<DecodeHintType, Object> f8640g;

    static {
        EnumMap enumMap = new EnumMap(DecodeHintType.class);
        f8634a = enumMap;
        ArrayList arrayList = new ArrayList();
        BarcodeFormat barcodeFormat = BarcodeFormat.a;
        arrayList.add(barcodeFormat);
        BarcodeFormat barcodeFormat2 = BarcodeFormat.b;
        arrayList.add(barcodeFormat2);
        BarcodeFormat barcodeFormat3 = BarcodeFormat.c;
        arrayList.add(barcodeFormat3);
        BarcodeFormat barcodeFormat4 = BarcodeFormat.d;
        arrayList.add(barcodeFormat4);
        BarcodeFormat barcodeFormat5 = BarcodeFormat.e;
        arrayList.add(barcodeFormat5);
        BarcodeFormat barcodeFormat6 = BarcodeFormat.f;
        arrayList.add(barcodeFormat6);
        BarcodeFormat barcodeFormat7 = BarcodeFormat.g;
        arrayList.add(barcodeFormat7);
        BarcodeFormat barcodeFormat8 = BarcodeFormat.h;
        arrayList.add(barcodeFormat8);
        BarcodeFormat barcodeFormat9 = BarcodeFormat.j;
        arrayList.add(barcodeFormat9);
        BarcodeFormat barcodeFormat10 = BarcodeFormat.k;
        arrayList.add(barcodeFormat10);
        BarcodeFormat barcodeFormat11 = BarcodeFormat.l;
        arrayList.add(barcodeFormat11);
        BarcodeFormat barcodeFormat12 = BarcodeFormat.m;
        arrayList.add(barcodeFormat12);
        BarcodeFormat barcodeFormat13 = BarcodeFormat.n;
        arrayList.add(barcodeFormat13);
        BarcodeFormat barcodeFormat14 = BarcodeFormat.p;
        arrayList.add(barcodeFormat14);
        BarcodeFormat barcodeFormat15 = BarcodeFormat.q;
        arrayList.add(barcodeFormat15);
        BarcodeFormat barcodeFormat16 = BarcodeFormat.t;
        arrayList.add(barcodeFormat16);
        BarcodeFormat barcodeFormat17 = BarcodeFormat.w;
        arrayList.add(barcodeFormat17);
        DecodeHintType decodeHintType = DecodeHintType.d;
        enumMap.put((EnumMap) decodeHintType, (DecodeHintType) arrayList);
        DecodeHintType decodeHintType2 = DecodeHintType.e;
        Boolean bool = Boolean.TRUE;
        enumMap.put((EnumMap) decodeHintType2, (DecodeHintType) bool);
        DecodeHintType decodeHintType3 = DecodeHintType.f;
        enumMap.put((EnumMap) decodeHintType3, (DecodeHintType) "utf-8");
        EnumMap enumMap2 = new EnumMap(DecodeHintType.class);
        f8635b = enumMap2;
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(barcodeFormat2);
        arrayList2.add(barcodeFormat3);
        arrayList2.add(barcodeFormat4);
        arrayList2.add(barcodeFormat5);
        arrayList2.add(barcodeFormat7);
        arrayList2.add(barcodeFormat8);
        arrayList2.add(barcodeFormat9);
        arrayList2.add(barcodeFormat11);
        arrayList2.add(barcodeFormat13);
        arrayList2.add(barcodeFormat14);
        arrayList2.add(barcodeFormat15);
        arrayList2.add(barcodeFormat16);
        arrayList2.add(barcodeFormat17);
        enumMap2.put((EnumMap) decodeHintType, (DecodeHintType) arrayList2);
        enumMap2.put((EnumMap) decodeHintType2, (DecodeHintType) bool);
        enumMap2.put((EnumMap) decodeHintType3, (DecodeHintType) "utf-8");
        EnumMap enumMap3 = new EnumMap(DecodeHintType.class);
        f8636c = enumMap3;
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(barcodeFormat);
        arrayList3.add(barcodeFormat6);
        arrayList3.add(barcodeFormat10);
        arrayList3.add(barcodeFormat12);
        enumMap3.put((EnumMap) decodeHintType, (DecodeHintType) arrayList3);
        enumMap3.put((EnumMap) decodeHintType2, (DecodeHintType) bool);
        enumMap3.put((EnumMap) decodeHintType3, (DecodeHintType) "utf-8");
        EnumMap enumMap4 = new EnumMap(DecodeHintType.class);
        f8637d = enumMap4;
        enumMap4.put((EnumMap) decodeHintType, (DecodeHintType) Collections.singletonList(barcodeFormat12));
        enumMap4.put((EnumMap) decodeHintType2, (DecodeHintType) bool);
        enumMap4.put((EnumMap) decodeHintType3, (DecodeHintType) "utf-8");
        EnumMap enumMap5 = new EnumMap(DecodeHintType.class);
        f8638e = enumMap5;
        enumMap5.put((EnumMap) decodeHintType, (DecodeHintType) Collections.singletonList(barcodeFormat5));
        enumMap5.put((EnumMap) decodeHintType2, (DecodeHintType) bool);
        enumMap5.put((EnumMap) decodeHintType3, (DecodeHintType) "utf-8");
        EnumMap enumMap6 = new EnumMap(DecodeHintType.class);
        f8639f = enumMap6;
        enumMap6.put((EnumMap) decodeHintType, (DecodeHintType) Collections.singletonList(barcodeFormat8));
        enumMap6.put((EnumMap) decodeHintType2, (DecodeHintType) bool);
        enumMap6.put((EnumMap) decodeHintType3, (DecodeHintType) "utf-8");
        EnumMap enumMap7 = new EnumMap(DecodeHintType.class);
        f8640g = enumMap7;
        ArrayList arrayList4 = new ArrayList();
        arrayList4.add(barcodeFormat12);
        arrayList4.add(barcodeFormat15);
        arrayList4.add(barcodeFormat8);
        arrayList4.add(barcodeFormat5);
        enumMap7.put((EnumMap) decodeHintType, (DecodeHintType) arrayList4);
        enumMap7.put((EnumMap) decodeHintType2, (DecodeHintType) bool);
        enumMap7.put((EnumMap) decodeHintType3, (DecodeHintType) "utf-8");
    }

    public static String a(Bitmap bitmap) {
        h hVar;
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] iArr = new int[width * height];
            bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
            hVar = new h(width, height, iArr);
        } catch (Exception e8) {
            e = e8;
            hVar = null;
        }
        try {
            return new e().a(new b(new i(hVar)), f8634a).f();
        } catch (Exception e9) {
            e = e9;
            e.printStackTrace();
            if (hVar != null) {
                try {
                    return new e().a(new b(new g(hVar)), f8634a).f();
                } catch (Throwable th) {
                    th.printStackTrace();
                    return null;
                }
            }
            return null;
        }
    }
}
