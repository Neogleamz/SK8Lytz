package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class zzdb implements Iterable, Serializable {

    /* renamed from: b  reason: collision with root package name */
    public static final zzdb f14977b = new m1(y2.f14888d);

    /* renamed from: c  reason: collision with root package name */
    private static final Comparator f14978c;

    /* renamed from: d  reason: collision with root package name */
    private static final p1 f14979d;

    /* renamed from: a  reason: collision with root package name */
    private int f14980a = 0;

    static {
        int i8 = b1.f14727a;
        f14979d = new p1(null);
        f14978c = new h1();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int A(int i8, int i9, int i10) {
        int i11 = i9 - i8;
        if ((i8 | i9 | i11 | (i10 - i9)) < 0) {
            if (i8 < 0) {
                throw new IndexOutOfBoundsException("Beginning index: " + i8 + " < 0");
            } else if (i9 < i8) {
                throw new IndexOutOfBoundsException("Beginning index larger than ending index: " + i8 + ", " + i9);
            } else {
                throw new IndexOutOfBoundsException("End index: " + i9 + " >= " + i10);
            }
        }
        return i11;
    }

    public static zzdb F(byte[] bArr, int i8, int i9) {
        A(i8, i8 + i9, bArr.length);
        byte[] bArr2 = new byte[i9];
        System.arraycopy(bArr, i8, bArr2, 0, i9);
        return new m1(bArr2);
    }

    public static zzdb G(InputStream inputStream) {
        ArrayList arrayList = new ArrayList();
        int i8 = RecognitionOptions.QR_CODE;
        while (true) {
            byte[] bArr = new byte[i8];
            int i9 = 0;
            while (i9 < i8) {
                int read = inputStream.read(bArr, i9, i8 - i9);
                if (read == -1) {
                    break;
                }
                i9 += read;
            }
            zzdb F = i9 == 0 ? null : F(bArr, 0, i9);
            if (F == null) {
                break;
            }
            arrayList.add(F);
            i8 = Math.min(i8 + i8, 8192);
        }
        int size = arrayList.size();
        return size == 0 ? f14977b : h(arrayList.iterator(), size);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void K(int i8, int i9) {
        if (((i9 - (i8 + 1)) | i8) < 0) {
            if (i8 < 0) {
                throw new ArrayIndexOutOfBoundsException("Index < 0: " + i8);
            }
            throw new ArrayIndexOutOfBoundsException("Index > length: " + i8 + ", " + i9);
        }
    }

    private static zzdb h(Iterator it, int i8) {
        if (i8 > 0) {
            if (i8 == 1) {
                return (zzdb) it.next();
            }
            int i9 = i8 >>> 1;
            zzdb h8 = h(it, i9);
            zzdb h9 = h(it, i8 - i9);
            if (Integer.MAX_VALUE - h8.i() >= h9.i()) {
                return q4.S(h8, h9);
            }
            int i10 = h8.i();
            int i11 = h9.i();
            throw new IllegalArgumentException("ByteString would be too long: " + i10 + "+" + i11);
        }
        throw new IllegalArgumentException(String.format("length (%s) must be >= 1", Integer.valueOf(i8)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int D() {
        return this.f14980a;
    }

    @Override // java.lang.Iterable
    /* renamed from: E */
    public k1 iterator() {
        return new g1(this);
    }

    public final String H(Charset charset) {
        return i() == 0 ? BuildConfig.FLAVOR : v(charset);
    }

    public final String I() {
        return H(y2.f14886b);
    }

    @Deprecated
    public final void L(byte[] bArr, int i8, int i9, int i10) {
        A(0, i10, i());
        A(i9, i9 + i10, bArr.length);
        if (i10 > 0) {
            k(bArr, 0, i9, i10);
        }
    }

    public final byte[] M() {
        int i8 = i();
        if (i8 == 0) {
            return y2.f14888d;
        }
        byte[] bArr = new byte[i8];
        k(bArr, 0, 0, i8);
        return bArr;
    }

    public abstract byte e(int i8);

    public abstract boolean equals(Object obj);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract byte g(int i8);

    public final int hashCode() {
        int i8 = this.f14980a;
        if (i8 == 0) {
            int i9 = i();
            i8 = q(i9, 0, i9);
            if (i8 == 0) {
                i8 = 1;
            }
            this.f14980a = i8;
        }
        return i8;
    }

    public abstract int i();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void k(byte[] bArr, int i8, int i9, int i10);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int n();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract boolean p();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int q(int i8, int i9, int i10);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int t(int i8, int i9, int i10);

    public final String toString() {
        Locale locale = Locale.ROOT;
        Object[] objArr = new Object[3];
        objArr[0] = Integer.toHexString(System.identityHashCode(this));
        objArr[1] = Integer.valueOf(i());
        objArr[2] = i() <= 50 ? g5.a(this) : g5.a(u(0, 47)).concat("...");
        return String.format(locale, "<ByteString@%s size=%d contents=\"%s\">", objArr);
    }

    public abstract zzdb u(int i8, int i9);

    protected abstract String v(Charset charset);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void x(f1 f1Var);

    public abstract boolean y();
}
