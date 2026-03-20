package com.google.android.gms.internal.measurement;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a9 {

    /* renamed from: a  reason: collision with root package name */
    private static final Charset f12068a = Charset.forName("US-ASCII");

    /* renamed from: b  reason: collision with root package name */
    static final Charset f12069b = Charset.forName("UTF-8");

    /* renamed from: c  reason: collision with root package name */
    private static final Charset f12070c = Charset.forName("ISO-8859-1");

    /* renamed from: d  reason: collision with root package name */
    public static final byte[] f12071d;

    /* renamed from: e  reason: collision with root package name */
    private static final ByteBuffer f12072e;

    /* renamed from: f  reason: collision with root package name */
    private static final b8 f12073f;

    static {
        byte[] bArr = new byte[0];
        f12071d = bArr;
        f12072e = ByteBuffer.wrap(bArr);
        f12073f = b8.c(bArr, 0, bArr.length, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(int i8, byte[] bArr, int i9, int i10) {
        for (int i11 = i9; i11 < i9 + i10; i11++) {
            i8 = (i8 * 31) + bArr[i11];
        }
        return i8;
    }

    public static int b(long j8) {
        return (int) (j8 ^ (j8 >>> 32));
    }

    public static int c(boolean z4) {
        return z4 ? 1231 : 1237;
    }

    public static int d(byte[] bArr) {
        int length = bArr.length;
        int a9 = a(length, bArr, 0, length);
        if (a9 == 0) {
            return 1;
        }
        return a9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T e(T t8) {
        Objects.requireNonNull(t8);
        return t8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T f(T t8, String str) {
        Objects.requireNonNull(t8, str);
        return t8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean g(ia iaVar) {
        if (iaVar instanceof j7) {
            j7 j7Var = (j7) iaVar;
            return false;
        }
        return false;
    }

    public static String h(byte[] bArr) {
        return new String(bArr, f12069b);
    }

    public static boolean i(byte[] bArr) {
        return dc.d(bArr);
    }
}
