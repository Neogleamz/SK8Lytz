package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y2 {

    /* renamed from: a  reason: collision with root package name */
    static final Charset f14885a = Charset.forName("US-ASCII");

    /* renamed from: b  reason: collision with root package name */
    static final Charset f14886b = Charset.forName("UTF-8");

    /* renamed from: c  reason: collision with root package name */
    static final Charset f14887c = Charset.forName("ISO-8859-1");

    /* renamed from: d  reason: collision with root package name */
    public static final byte[] f14888d;

    /* renamed from: e  reason: collision with root package name */
    public static final ByteBuffer f14889e;

    /* renamed from: f  reason: collision with root package name */
    public static final t1 f14890f;

    static {
        byte[] bArr = new byte[0];
        f14888d = bArr;
        f14889e = ByteBuffer.wrap(bArr);
        int i8 = t1.f14858a;
        r1 r1Var = new r1(bArr, 0, 0, false, null);
        try {
            r1Var.c(0);
            f14890f = r1Var;
        } catch (zzeo e8) {
            throw new IllegalArgumentException(e8);
        }
    }

    public static int a(boolean z4) {
        return z4 ? 1231 : 1237;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int b(int i8, byte[] bArr, int i9, int i10) {
        for (int i11 = i9; i11 < i9 + i10; i11++) {
            i8 = (i8 * 31) + bArr[i11];
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object c(Object obj, String str) {
        Objects.requireNonNull(obj, str);
        return obj;
    }

    public static String d(byte[] bArr) {
        return new String(bArr, f14886b);
    }
}
