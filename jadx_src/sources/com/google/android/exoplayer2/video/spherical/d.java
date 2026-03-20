package com.google.android.exoplayer2.video.spherical;

import b6.l0;
import b6.y;
import b6.z;
import com.google.android.exoplayer2.video.spherical.c;
import java.util.ArrayList;
import java.util.zip.Inflater;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d {
    public static c a(byte[] bArr, int i8) {
        ArrayList<c.a> arrayList;
        z zVar = new z(bArr);
        try {
            arrayList = c(zVar) ? f(zVar) : e(zVar);
        } catch (ArrayIndexOutOfBoundsException unused) {
            arrayList = null;
        }
        if (arrayList == null) {
            return null;
        }
        int size = arrayList.size();
        if (size != 1) {
            if (size != 2) {
                return null;
            }
            return new c(arrayList.get(0), arrayList.get(1), i8);
        }
        return new c(arrayList.get(0), i8);
    }

    private static int b(int i8) {
        return (-(i8 & 1)) ^ (i8 >> 1);
    }

    private static boolean c(z zVar) {
        zVar.V(4);
        int q = zVar.q();
        zVar.U(0);
        return q == 1886547818;
    }

    private static c.a d(z zVar) {
        int q = zVar.q();
        if (q > 10000) {
            return null;
        }
        float[] fArr = new float[q];
        for (int i8 = 0; i8 < q; i8++) {
            fArr[i8] = zVar.p();
        }
        int q8 = zVar.q();
        if (q8 > 32000) {
            return null;
        }
        double d8 = 2.0d;
        double log = Math.log(2.0d);
        int ceil = (int) Math.ceil(Math.log(q * 2.0d) / log);
        y yVar = new y(zVar.e());
        int i9 = 8;
        yVar.p(zVar.f() * 8);
        float[] fArr2 = new float[q8 * 5];
        int i10 = 5;
        int[] iArr = new int[5];
        int i11 = 0;
        int i12 = 0;
        while (i11 < q8) {
            int i13 = 0;
            while (i13 < i10) {
                int b9 = iArr[i13] + b(yVar.h(ceil));
                if (b9 >= q || b9 < 0) {
                    return null;
                }
                fArr2[i12] = fArr[b9];
                iArr[i13] = b9;
                i13++;
                i12++;
                i10 = 5;
            }
            i11++;
            i10 = 5;
        }
        yVar.p((yVar.e() + 7) & (-8));
        int i14 = 32;
        int h8 = yVar.h(32);
        c.b[] bVarArr = new c.b[h8];
        int i15 = 0;
        while (i15 < h8) {
            int h9 = yVar.h(i9);
            int h10 = yVar.h(i9);
            int h11 = yVar.h(i14);
            if (h11 > 128000) {
                return null;
            }
            int ceil2 = (int) Math.ceil(Math.log(q8 * d8) / log);
            float[] fArr3 = new float[h11 * 3];
            float[] fArr4 = new float[h11 * 2];
            int i16 = 0;
            for (int i17 = 0; i17 < h11; i17++) {
                i16 += b(yVar.h(ceil2));
                if (i16 < 0 || i16 >= q8) {
                    return null;
                }
                int i18 = i17 * 3;
                int i19 = i16 * 5;
                fArr3[i18] = fArr2[i19];
                fArr3[i18 + 1] = fArr2[i19 + 1];
                fArr3[i18 + 2] = fArr2[i19 + 2];
                int i20 = i17 * 2;
                fArr4[i20] = fArr2[i19 + 3];
                fArr4[i20 + 1] = fArr2[i19 + 4];
            }
            bVarArr[i15] = new c.b(h9, fArr3, fArr4, h10);
            i15++;
            i14 = 32;
            d8 = 2.0d;
            i9 = 8;
        }
        return new c.a(bVarArr);
    }

    private static ArrayList<c.a> e(z zVar) {
        if (zVar.H() != 0) {
            return null;
        }
        zVar.V(7);
        int q = zVar.q();
        if (q == 1684433976) {
            z zVar2 = new z();
            Inflater inflater = new Inflater(true);
            try {
                if (!l0.q0(zVar, zVar2, inflater)) {
                    return null;
                }
                zVar = zVar2;
            } finally {
                inflater.end();
            }
        } else if (q != 1918990112) {
            return null;
        }
        return g(zVar);
    }

    private static ArrayList<c.a> f(z zVar) {
        int q;
        zVar.V(8);
        int f5 = zVar.f();
        int g8 = zVar.g();
        while (f5 < g8 && (q = zVar.q() + f5) > f5 && q <= g8) {
            int q8 = zVar.q();
            if (q8 == 2037673328 || q8 == 1836279920) {
                zVar.T(q);
                return e(zVar);
            }
            zVar.U(q);
            f5 = q;
        }
        return null;
    }

    private static ArrayList<c.a> g(z zVar) {
        ArrayList<c.a> arrayList = new ArrayList<>();
        int f5 = zVar.f();
        int g8 = zVar.g();
        while (f5 < g8) {
            int q = zVar.q() + f5;
            if (q <= f5 || q > g8) {
                return null;
            }
            if (zVar.q() == 1835365224) {
                c.a d8 = d(zVar);
                if (d8 == null) {
                    return null;
                }
                arrayList.add(d8);
            }
            zVar.U(q);
            f5 = q;
        }
        return arrayList;
    }
}
