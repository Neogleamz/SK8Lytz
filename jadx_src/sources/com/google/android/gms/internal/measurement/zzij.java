package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class zzij implements Serializable, Iterable<Byte> {

    /* renamed from: b  reason: collision with root package name */
    public static final zzij f12852b = new a8(a9.f12071d);

    /* renamed from: c  reason: collision with root package name */
    private static final t7 f12853c = new z7();

    /* renamed from: d  reason: collision with root package name */
    private static final Comparator<zzij> f12854d = new s7();

    /* renamed from: a  reason: collision with root package name */
    private int f12855a = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int h(byte b9) {
        return b9 & 255;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int i(int i8, int i9, int i10) {
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

    public static zzij n(String str) {
        return new a8(str.getBytes(a9.f12069b));
    }

    public static zzij p(byte[] bArr, int i8, int i9) {
        i(i8, i8 + i9, bArr.length);
        return new a8(f12853c.a(bArr, i8, i9));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static v7 y(int i8) {
        return new v7(i8);
    }

    public final String A() {
        return v() == 0 ? BuildConfig.FLAVOR : q(a9.f12069b);
    }

    public abstract boolean D();

    public abstract byte e(int i8);

    public abstract boolean equals(Object obj);

    /* JADX INFO: Access modifiers changed from: protected */
    public final int g() {
        return this.f12855a;
    }

    public final int hashCode() {
        int i8 = this.f12855a;
        if (i8 == 0) {
            int v8 = v();
            i8 = x(v8, 0, v8);
            if (i8 == 0) {
                i8 = 1;
            }
            this.f12855a = i8;
        }
        return i8;
    }

    @Override // java.lang.Iterable
    public /* synthetic */ Iterator<Byte> iterator() {
        return new q7(this);
    }

    public abstract zzij k(int i8, int i9);

    protected abstract String q(Charset charset);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void t(o7 o7Var);

    public final String toString() {
        String str;
        Locale locale = Locale.ROOT;
        Object[] objArr = new Object[3];
        objArr[0] = Integer.toHexString(System.identityHashCode(this));
        objArr[1] = Integer.valueOf(v());
        if (v() <= 50) {
            str = pb.a(this);
        } else {
            str = pb.a(k(0, 47)) + "...";
        }
        objArr[2] = str;
        return String.format(locale, "<ByteString@%s size=%d contents=\"%s\">", objArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract byte u(int i8);

    public abstract int v();

    protected abstract int x(int i8, int i9, int i10);
}
