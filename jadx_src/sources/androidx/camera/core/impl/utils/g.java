package androidx.camera.core.impl.utils;

import com.daimajia.numberprogressbar.BuildConfig;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g {

    /* renamed from: e  reason: collision with root package name */
    static final Charset f2635e = StandardCharsets.US_ASCII;

    /* renamed from: f  reason: collision with root package name */
    static final String[] f2636f = {BuildConfig.FLAVOR, "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE", "IFD"};

    /* renamed from: g  reason: collision with root package name */
    static final int[] f2637g = {0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};

    /* renamed from: h  reason: collision with root package name */
    static final byte[] f2638h = {65, 83, 67, 73, 73, 0, 0, 0};

    /* renamed from: a  reason: collision with root package name */
    public final int f2639a;

    /* renamed from: b  reason: collision with root package name */
    public final int f2640b;

    /* renamed from: c  reason: collision with root package name */
    public final long f2641c;

    /* renamed from: d  reason: collision with root package name */
    public final byte[] f2642d;

    g(int i8, int i9, long j8, byte[] bArr) {
        this.f2639a = i8;
        this.f2640b = i9;
        this.f2641c = j8;
        this.f2642d = bArr;
    }

    g(int i8, int i9, byte[] bArr) {
        this(i8, i9, -1L, bArr);
    }

    public static g a(String str) {
        if (str.length() != 1 || str.charAt(0) < '0' || str.charAt(0) > '1') {
            byte[] bytes = str.getBytes(f2635e);
            return new g(1, bytes.length, bytes);
        }
        return new g(1, 1, new byte[]{(byte) (str.charAt(0) - '0')});
    }

    public static g b(double[] dArr, ByteOrder byteOrder) {
        ByteBuffer wrap = ByteBuffer.wrap(new byte[f2637g[12] * dArr.length]);
        wrap.order(byteOrder);
        for (double d8 : dArr) {
            wrap.putDouble(d8);
        }
        return new g(12, dArr.length, wrap.array());
    }

    public static g c(int[] iArr, ByteOrder byteOrder) {
        ByteBuffer wrap = ByteBuffer.wrap(new byte[f2637g[9] * iArr.length]);
        wrap.order(byteOrder);
        for (int i8 : iArr) {
            wrap.putInt(i8);
        }
        return new g(9, iArr.length, wrap.array());
    }

    public static g d(j[] jVarArr, ByteOrder byteOrder) {
        ByteBuffer wrap = ByteBuffer.wrap(new byte[f2637g[10] * jVarArr.length]);
        wrap.order(byteOrder);
        for (j jVar : jVarArr) {
            wrap.putInt((int) jVar.b());
            wrap.putInt((int) jVar.a());
        }
        return new g(10, jVarArr.length, wrap.array());
    }

    public static g e(String str) {
        byte[] bytes = (str + (char) 0).getBytes(f2635e);
        return new g(2, bytes.length, bytes);
    }

    public static g f(long j8, ByteOrder byteOrder) {
        return g(new long[]{j8}, byteOrder);
    }

    public static g g(long[] jArr, ByteOrder byteOrder) {
        ByteBuffer wrap = ByteBuffer.wrap(new byte[f2637g[4] * jArr.length]);
        wrap.order(byteOrder);
        for (long j8 : jArr) {
            wrap.putInt((int) j8);
        }
        return new g(4, jArr.length, wrap.array());
    }

    public static g h(j[] jVarArr, ByteOrder byteOrder) {
        ByteBuffer wrap = ByteBuffer.wrap(new byte[f2637g[5] * jVarArr.length]);
        wrap.order(byteOrder);
        for (j jVar : jVarArr) {
            wrap.putInt((int) jVar.b());
            wrap.putInt((int) jVar.a());
        }
        return new g(5, jVarArr.length, wrap.array());
    }

    public static g i(int[] iArr, ByteOrder byteOrder) {
        ByteBuffer wrap = ByteBuffer.wrap(new byte[f2637g[3] * iArr.length]);
        wrap.order(byteOrder);
        for (int i8 : iArr) {
            wrap.putShort((short) i8);
        }
        return new g(3, iArr.length, wrap.array());
    }

    public int j() {
        return f2637g[this.f2639a] * this.f2640b;
    }

    public String toString() {
        return "(" + f2636f[this.f2639a] + ", data length:" + this.f2642d.length + ")";
    }
}
