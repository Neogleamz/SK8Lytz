package b6;

import android.util.Pair;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: a  reason: collision with root package name */
    private static final byte[] f8035a = {0, 0, 0, 1};

    /* renamed from: b  reason: collision with root package name */
    private static final String[] f8036b = {BuildConfig.FLAVOR, "A", "B", "C"};

    public static String a(int i8, int i9, int i10) {
        return String.format("avc1.%02X%02X%02X", Integer.valueOf(i8), Integer.valueOf(i9), Integer.valueOf(i10));
    }

    public static List<byte[]> b(boolean z4) {
        return Collections.singletonList(z4 ? new byte[]{1} : new byte[]{0});
    }

    public static String c(int i8, boolean z4, int i9, int i10, int[] iArr, int i11) {
        Object[] objArr = new Object[5];
        objArr[0] = f8036b[i8];
        objArr[1] = Integer.valueOf(i9);
        objArr[2] = Integer.valueOf(i10);
        objArr[3] = Character.valueOf(z4 ? 'H' : 'L');
        objArr[4] = Integer.valueOf(i11);
        StringBuilder sb = new StringBuilder(l0.C("hvc1.%s%d.%X.%c%d", objArr));
        int length = iArr.length;
        while (length > 0 && iArr[length - 1] == 0) {
            length--;
        }
        for (int i12 = 0; i12 < length; i12++) {
            sb.append(String.format(".%02X", Integer.valueOf(iArr[i12])));
        }
        return sb.toString();
    }

    public static byte[] d(byte[] bArr, int i8, int i9) {
        byte[] bArr2 = f8035a;
        byte[] bArr3 = new byte[bArr2.length + i9];
        System.arraycopy(bArr2, 0, bArr3, 0, bArr2.length);
        System.arraycopy(bArr, i8, bArr3, bArr2.length, i9);
        return bArr3;
    }

    private static int e(byte[] bArr, int i8) {
        int length = bArr.length - f8035a.length;
        while (i8 <= length) {
            if (f(bArr, i8)) {
                return i8;
            }
            i8++;
        }
        return -1;
    }

    private static boolean f(byte[] bArr, int i8) {
        if (bArr.length - i8 <= f8035a.length) {
            return false;
        }
        int i9 = 0;
        while (true) {
            byte[] bArr2 = f8035a;
            if (i9 >= bArr2.length) {
                return true;
            }
            if (bArr[i8 + i9] != bArr2[i9]) {
                return false;
            }
            i9++;
        }
    }

    public static Pair<Integer, Integer> g(byte[] bArr) {
        z zVar = new z(bArr);
        zVar.U(9);
        int H = zVar.H();
        zVar.U(20);
        return Pair.create(Integer.valueOf(zVar.L()), Integer.valueOf(H));
    }

    public static boolean h(List<byte[]> list) {
        return list.size() == 1 && list.get(0).length == 1 && list.get(0)[0] == 1;
    }

    public static byte[][] i(byte[] bArr) {
        if (f(bArr, 0)) {
            ArrayList arrayList = new ArrayList();
            int i8 = 0;
            do {
                arrayList.add(Integer.valueOf(i8));
                i8 = e(bArr, i8 + f8035a.length);
            } while (i8 != -1);
            byte[][] bArr2 = new byte[arrayList.size()];
            int i9 = 0;
            while (i9 < arrayList.size()) {
                int intValue = ((Integer) arrayList.get(i9)).intValue();
                int intValue2 = (i9 < arrayList.size() + (-1) ? ((Integer) arrayList.get(i9 + 1)).intValue() : bArr.length) - intValue;
                byte[] bArr3 = new byte[intValue2];
                System.arraycopy(bArr, intValue, bArr3, 0, intValue2);
                bArr2[i9] = bArr3;
                i9++;
            }
            return bArr2;
        }
        return null;
    }
}
