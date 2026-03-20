package androidx.profileinstaller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;
import java.util.TreeMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class n {

    /* renamed from: a  reason: collision with root package name */
    static final byte[] f6506a = {112, 114, 111, 0};

    /* renamed from: b  reason: collision with root package name */
    static final byte[] f6507b = {112, 114, 109, 0};

    private static void A(InputStream inputStream) {
        e.h(inputStream);
        int j8 = e.j(inputStream);
        if (j8 == 6 || j8 == 7) {
            return;
        }
        while (j8 > 0) {
            e.j(inputStream);
            for (int j9 = e.j(inputStream); j9 > 0; j9--) {
                e.h(inputStream);
            }
            j8--;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean B(OutputStream outputStream, byte[] bArr, d[] dVarArr) {
        if (Arrays.equals(bArr, p.f6518a)) {
            N(outputStream, dVarArr);
            return true;
        } else if (Arrays.equals(bArr, p.f6519b)) {
            M(outputStream, dVarArr);
            return true;
        } else if (Arrays.equals(bArr, p.f6521d)) {
            K(outputStream, dVarArr);
            return true;
        } else if (Arrays.equals(bArr, p.f6520c)) {
            L(outputStream, dVarArr);
            return true;
        } else if (Arrays.equals(bArr, p.f6522e)) {
            J(outputStream, dVarArr);
            return true;
        } else {
            return false;
        }
    }

    private static void C(OutputStream outputStream, d dVar) {
        int i8 = 0;
        for (int i9 : dVar.f6486h) {
            Integer valueOf = Integer.valueOf(i9);
            e.p(outputStream, valueOf.intValue() - i8);
            i8 = valueOf.intValue();
        }
    }

    private static q D(d[] dVarArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            e.p(byteArrayOutputStream, dVarArr.length);
            int i8 = 2;
            for (d dVar : dVarArr) {
                e.q(byteArrayOutputStream, dVar.f6481c);
                e.q(byteArrayOutputStream, dVar.f6482d);
                e.q(byteArrayOutputStream, dVar.f6485g);
                String j8 = j(dVar.f6479a, dVar.f6480b, p.f6518a);
                int k8 = e.k(j8);
                e.p(byteArrayOutputStream, k8);
                i8 = i8 + 4 + 4 + 4 + 2 + (k8 * 1);
                e.n(byteArrayOutputStream, j8);
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if (i8 == byteArray.length) {
                q qVar = new q(f.DEX_FILES, i8, byteArray, false);
                byteArrayOutputStream.close();
                return qVar;
            }
            throw e.c("Expected size " + i8 + ", does not match actual size " + byteArray.length);
        } catch (Throwable th) {
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void E(OutputStream outputStream, byte[] bArr) {
        outputStream.write(f6506a);
        outputStream.write(bArr);
    }

    private static void F(OutputStream outputStream, d dVar) {
        I(outputStream, dVar);
        C(outputStream, dVar);
        H(outputStream, dVar);
    }

    private static void G(OutputStream outputStream, d dVar, String str) {
        e.p(outputStream, e.k(str));
        e.p(outputStream, dVar.f6483e);
        e.q(outputStream, dVar.f6484f);
        e.q(outputStream, dVar.f6481c);
        e.q(outputStream, dVar.f6485g);
        e.n(outputStream, str);
    }

    private static void H(OutputStream outputStream, d dVar) {
        byte[] bArr = new byte[k(dVar.f6485g)];
        for (Map.Entry<Integer, Integer> entry : dVar.f6487i.entrySet()) {
            int intValue = entry.getKey().intValue();
            int intValue2 = entry.getValue().intValue();
            if ((intValue2 & 2) != 0) {
                z(bArr, 2, intValue, dVar);
            }
            if ((intValue2 & 4) != 0) {
                z(bArr, 4, intValue, dVar);
            }
        }
        outputStream.write(bArr);
    }

    private static void I(OutputStream outputStream, d dVar) {
        int i8 = 0;
        for (Map.Entry<Integer, Integer> entry : dVar.f6487i.entrySet()) {
            int intValue = entry.getKey().intValue();
            if ((entry.getValue().intValue() & 1) != 0) {
                e.p(outputStream, intValue - i8);
                e.p(outputStream, 0);
                i8 = intValue;
            }
        }
    }

    private static void J(OutputStream outputStream, d[] dVarArr) {
        e.p(outputStream, dVarArr.length);
        for (d dVar : dVarArr) {
            String j8 = j(dVar.f6479a, dVar.f6480b, p.f6522e);
            e.p(outputStream, e.k(j8));
            e.p(outputStream, dVar.f6487i.size());
            e.p(outputStream, dVar.f6486h.length);
            e.q(outputStream, dVar.f6481c);
            e.n(outputStream, j8);
            for (Integer num : dVar.f6487i.keySet()) {
                e.p(outputStream, num.intValue());
            }
            for (int i8 : dVar.f6486h) {
                e.p(outputStream, i8);
            }
        }
    }

    private static void K(OutputStream outputStream, d[] dVarArr) {
        e.r(outputStream, dVarArr.length);
        for (d dVar : dVarArr) {
            String j8 = j(dVar.f6479a, dVar.f6480b, p.f6521d);
            e.p(outputStream, e.k(j8));
            e.p(outputStream, dVar.f6486h.length);
            e.q(outputStream, dVar.f6487i.size() * 4);
            e.q(outputStream, dVar.f6481c);
            e.n(outputStream, j8);
            for (Integer num : dVar.f6487i.keySet()) {
                e.p(outputStream, num.intValue());
                e.p(outputStream, 0);
            }
            for (int i8 : dVar.f6486h) {
                e.p(outputStream, i8);
            }
        }
    }

    private static void L(OutputStream outputStream, d[] dVarArr) {
        byte[] b9 = b(dVarArr, p.f6520c);
        e.r(outputStream, dVarArr.length);
        e.m(outputStream, b9);
    }

    private static void M(OutputStream outputStream, d[] dVarArr) {
        byte[] b9 = b(dVarArr, p.f6519b);
        e.r(outputStream, dVarArr.length);
        e.m(outputStream, b9);
    }

    private static void N(OutputStream outputStream, d[] dVarArr) {
        O(outputStream, dVarArr);
    }

    private static void O(OutputStream outputStream, d[] dVarArr) {
        int length;
        byte[] bArr;
        ArrayList arrayList = new ArrayList(3);
        ArrayList arrayList2 = new ArrayList(3);
        arrayList.add(D(dVarArr));
        arrayList.add(c(dVarArr));
        arrayList.add(d(dVarArr));
        long length2 = p.f6518a.length + f6506a.length + 4 + (arrayList.size() * 16);
        e.q(outputStream, arrayList.size());
        for (int i8 = 0; i8 < arrayList.size(); i8++) {
            q qVar = (q) arrayList.get(i8);
            e.q(outputStream, qVar.f6525a.f());
            e.q(outputStream, length2);
            if (qVar.f6528d) {
                byte[] b9 = e.b(qVar.f6527c);
                arrayList2.add(b9);
                e.q(outputStream, b9.length);
                e.q(outputStream, bArr.length);
                length = b9.length;
            } else {
                arrayList2.add(qVar.f6527c);
                e.q(outputStream, qVar.f6527c.length);
                e.q(outputStream, 0L);
                length = qVar.f6527c.length;
            }
            length2 += length;
        }
        for (int i9 = 0; i9 < arrayList2.size(); i9++) {
            outputStream.write((byte[]) arrayList2.get(i9));
        }
    }

    private static int a(d dVar) {
        int i8 = 0;
        for (Map.Entry<Integer, Integer> entry : dVar.f6487i.entrySet()) {
            i8 |= entry.getValue().intValue();
        }
        return i8;
    }

    private static byte[] b(d[] dVarArr, byte[] bArr) {
        int i8 = 0;
        int i9 = 0;
        for (d dVar : dVarArr) {
            i9 += e.k(j(dVar.f6479a, dVar.f6480b, bArr)) + 16 + (dVar.f6483e * 2) + dVar.f6484f + k(dVar.f6485g);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(i9);
        if (Arrays.equals(bArr, p.f6520c)) {
            int length = dVarArr.length;
            while (i8 < length) {
                d dVar2 = dVarArr[i8];
                G(byteArrayOutputStream, dVar2, j(dVar2.f6479a, dVar2.f6480b, bArr));
                F(byteArrayOutputStream, dVar2);
                i8++;
            }
        } else {
            for (d dVar3 : dVarArr) {
                G(byteArrayOutputStream, dVar3, j(dVar3.f6479a, dVar3.f6480b, bArr));
            }
            int length2 = dVarArr.length;
            while (i8 < length2) {
                F(byteArrayOutputStream, dVarArr[i8]);
                i8++;
            }
        }
        if (byteArrayOutputStream.size() == i9) {
            return byteArrayOutputStream.toByteArray();
        }
        throw e.c("The bytes saved do not match expectation. actual=" + byteArrayOutputStream.size() + " expected=" + i9);
    }

    private static q c(d[] dVarArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i8 = 0;
        for (int i9 = 0; i9 < dVarArr.length; i9++) {
            try {
                d dVar = dVarArr[i9];
                e.p(byteArrayOutputStream, i9);
                e.p(byteArrayOutputStream, dVar.f6483e);
                i8 = i8 + 2 + 2 + (dVar.f6483e * 2);
                C(byteArrayOutputStream, dVar);
            } catch (Throwable th) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        if (i8 == byteArray.length) {
            q qVar = new q(f.CLASSES, i8, byteArray, true);
            byteArrayOutputStream.close();
            return qVar;
        }
        throw e.c("Expected size " + i8 + ", does not match actual size " + byteArray.length);
    }

    private static q d(d[] dVarArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i8 = 0;
        for (int i9 = 0; i9 < dVarArr.length; i9++) {
            try {
                d dVar = dVarArr[i9];
                int a9 = a(dVar);
                byte[] e8 = e(dVar);
                byte[] f5 = f(dVar);
                e.p(byteArrayOutputStream, i9);
                int length = e8.length + 2 + f5.length;
                e.q(byteArrayOutputStream, length);
                e.p(byteArrayOutputStream, a9);
                byteArrayOutputStream.write(e8);
                byteArrayOutputStream.write(f5);
                i8 = i8 + 2 + 4 + length;
            } catch (Throwable th) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        if (i8 == byteArray.length) {
            q qVar = new q(f.METHODS, i8, byteArray, true);
            byteArrayOutputStream.close();
            return qVar;
        }
        throw e.c("Expected size " + i8 + ", does not match actual size " + byteArray.length);
    }

    private static byte[] e(d dVar) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            H(byteArrayOutputStream, dVar);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return byteArray;
        } catch (Throwable th) {
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static byte[] f(d dVar) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            I(byteArrayOutputStream, dVar);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return byteArray;
        } catch (Throwable th) {
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static String g(String str, String str2) {
        return "!".equals(str2) ? str.replace(":", "!") : ":".equals(str2) ? str.replace("!", ":") : str;
    }

    private static String h(String str) {
        int indexOf = str.indexOf("!");
        if (indexOf < 0) {
            indexOf = str.indexOf(":");
        }
        return indexOf > 0 ? str.substring(indexOf + 1) : str;
    }

    private static d i(d[] dVarArr, String str) {
        if (dVarArr.length <= 0) {
            return null;
        }
        String h8 = h(str);
        for (int i8 = 0; i8 < dVarArr.length; i8++) {
            if (dVarArr[i8].f6480b.equals(h8)) {
                return dVarArr[i8];
            }
        }
        return null;
    }

    private static String j(String str, String str2, byte[] bArr) {
        String a9 = p.a(bArr);
        if (str.length() <= 0) {
            return g(str2, a9);
        }
        if (str2.equals("classes.dex")) {
            return str;
        }
        if (str2.contains("!") || str2.contains(":")) {
            return g(str2, a9);
        }
        if (str2.endsWith(".apk")) {
            return str2;
        }
        return str + p.a(bArr) + str2;
    }

    private static int k(int i8) {
        return y(i8 * 2) / 8;
    }

    private static int l(int i8, int i9, int i10) {
        if (i8 != 1) {
            if (i8 != 2) {
                if (i8 == 4) {
                    return i9 + i10;
                }
                throw e.c("Unexpected flag: " + i8);
            }
            return i9;
        }
        throw e.c("HOT methods are not stored in the bitmap");
    }

    private static int[] m(InputStream inputStream, int i8) {
        int[] iArr = new int[i8];
        int i9 = 0;
        for (int i10 = 0; i10 < i8; i10++) {
            i9 += e.h(inputStream);
            iArr[i10] = i9;
        }
        return iArr;
    }

    private static int n(BitSet bitSet, int i8, int i9) {
        int i10 = bitSet.get(l(2, i8, i9)) ? 2 : 0;
        return bitSet.get(l(4, i8, i9)) ? i10 | 4 : i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] o(InputStream inputStream, byte[] bArr) {
        if (Arrays.equals(bArr, e.d(inputStream, bArr.length))) {
            return e.d(inputStream, p.f6519b.length);
        }
        throw e.c("Invalid magic");
    }

    private static void p(InputStream inputStream, d dVar) {
        int available = inputStream.available() - dVar.f6484f;
        int i8 = 0;
        while (inputStream.available() > available) {
            i8 += e.h(inputStream);
            dVar.f6487i.put(Integer.valueOf(i8), 1);
            for (int h8 = e.h(inputStream); h8 > 0; h8--) {
                A(inputStream);
            }
        }
        if (inputStream.available() != available) {
            throw e.c("Read too much data during profile line parse");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static d[] q(InputStream inputStream, byte[] bArr, byte[] bArr2, d[] dVarArr) {
        if (Arrays.equals(bArr, p.f6523f)) {
            if (Arrays.equals(p.f6518a, bArr2)) {
                throw e.c("Requires new Baseline Profile Metadata. Please rebuild the APK with Android Gradle Plugin 7.2 Canary 7 or higher");
            }
            return r(inputStream, bArr, dVarArr);
        } else if (Arrays.equals(bArr, p.f6524g)) {
            return t(inputStream, bArr2, dVarArr);
        } else {
            throw e.c("Unsupported meta version");
        }
    }

    static d[] r(InputStream inputStream, byte[] bArr, d[] dVarArr) {
        if (Arrays.equals(bArr, p.f6523f)) {
            int j8 = e.j(inputStream);
            byte[] e8 = e.e(inputStream, (int) e.i(inputStream), (int) e.i(inputStream));
            if (inputStream.read() <= 0) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(e8);
                try {
                    d[] s8 = s(byteArrayInputStream, j8, dVarArr);
                    byteArrayInputStream.close();
                    return s8;
                } catch (Throwable th) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
            throw e.c("Content found after the end of file");
        }
        throw e.c("Unsupported meta version");
    }

    private static d[] s(InputStream inputStream, int i8, d[] dVarArr) {
        if (inputStream.available() == 0) {
            return new d[0];
        }
        if (i8 == dVarArr.length) {
            String[] strArr = new String[i8];
            int[] iArr = new int[i8];
            for (int i9 = 0; i9 < i8; i9++) {
                int h8 = e.h(inputStream);
                iArr[i9] = e.h(inputStream);
                strArr[i9] = e.f(inputStream, h8);
            }
            for (int i10 = 0; i10 < i8; i10++) {
                d dVar = dVarArr[i10];
                if (!dVar.f6480b.equals(strArr[i10])) {
                    throw e.c("Order of dexfiles in metadata did not match baseline");
                }
                int i11 = iArr[i10];
                dVar.f6483e = i11;
                dVar.f6486h = m(inputStream, i11);
            }
            return dVarArr;
        }
        throw e.c("Mismatched number of dex files found in metadata");
    }

    static d[] t(InputStream inputStream, byte[] bArr, d[] dVarArr) {
        int h8 = e.h(inputStream);
        byte[] e8 = e.e(inputStream, (int) e.i(inputStream), (int) e.i(inputStream));
        if (inputStream.read() <= 0) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(e8);
            try {
                d[] u8 = u(byteArrayInputStream, bArr, h8, dVarArr);
                byteArrayInputStream.close();
                return u8;
            } catch (Throwable th) {
                try {
                    byteArrayInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        throw e.c("Content found after the end of file");
    }

    private static d[] u(InputStream inputStream, byte[] bArr, int i8, d[] dVarArr) {
        if (inputStream.available() == 0) {
            return new d[0];
        }
        if (i8 == dVarArr.length) {
            for (int i9 = 0; i9 < i8; i9++) {
                e.h(inputStream);
                String f5 = e.f(inputStream, e.h(inputStream));
                long i10 = e.i(inputStream);
                int h8 = e.h(inputStream);
                d i11 = i(dVarArr, f5);
                if (i11 == null) {
                    throw e.c("Missing profile key: " + f5);
                }
                i11.f6482d = i10;
                int[] m8 = m(inputStream, h8);
                if (Arrays.equals(bArr, p.f6522e)) {
                    i11.f6483e = h8;
                    i11.f6486h = m8;
                }
            }
            return dVarArr;
        }
        throw e.c("Mismatched number of dex files found in metadata");
    }

    private static void v(InputStream inputStream, d dVar) {
        BitSet valueOf = BitSet.valueOf(e.d(inputStream, e.a(dVar.f6485g * 2)));
        int i8 = 0;
        while (true) {
            int i9 = dVar.f6485g;
            if (i8 >= i9) {
                return;
            }
            int n8 = n(valueOf, i8, i9);
            if (n8 != 0) {
                Integer num = dVar.f6487i.get(Integer.valueOf(i8));
                if (num == null) {
                    num = 0;
                }
                dVar.f6487i.put(Integer.valueOf(i8), Integer.valueOf(n8 | num.intValue()));
            }
            i8++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static d[] w(InputStream inputStream, byte[] bArr, String str) {
        if (Arrays.equals(bArr, p.f6519b)) {
            int j8 = e.j(inputStream);
            byte[] e8 = e.e(inputStream, (int) e.i(inputStream), (int) e.i(inputStream));
            if (inputStream.read() <= 0) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(e8);
                try {
                    d[] x8 = x(byteArrayInputStream, str, j8);
                    byteArrayInputStream.close();
                    return x8;
                } catch (Throwable th) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
            throw e.c("Content found after the end of file");
        }
        throw e.c("Unsupported version");
    }

    private static d[] x(InputStream inputStream, String str, int i8) {
        if (inputStream.available() == 0) {
            return new d[0];
        }
        d[] dVarArr = new d[i8];
        for (int i9 = 0; i9 < i8; i9++) {
            int h8 = e.h(inputStream);
            int h9 = e.h(inputStream);
            dVarArr[i9] = new d(str, e.f(inputStream, h8), e.i(inputStream), 0L, h9, (int) e.i(inputStream), (int) e.i(inputStream), new int[h9], new TreeMap());
        }
        for (int i10 = 0; i10 < i8; i10++) {
            d dVar = dVarArr[i10];
            p(inputStream, dVar);
            dVar.f6486h = m(inputStream, dVar.f6483e);
            v(inputStream, dVar);
        }
        return dVarArr;
    }

    private static int y(int i8) {
        return ((i8 + 8) - 1) & (-8);
    }

    private static void z(byte[] bArr, int i8, int i9, d dVar) {
        int l8 = l(i8, i9, dVar.f6485g);
        int i10 = l8 / 8;
        bArr[i10] = (byte) ((1 << (l8 % 8)) | bArr[i10]);
    }
}
