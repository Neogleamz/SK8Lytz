package androidx.core.content.res;

import java.lang.reflect.Array;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g {
    public static int[] a(int[] iArr, int i8, int i9) {
        if (i8 + 1 > iArr.length) {
            int[] iArr2 = new int[c(i8)];
            System.arraycopy(iArr, 0, iArr2, 0, i8);
            iArr = iArr2;
        }
        iArr[i8] = i9;
        return iArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4, types: [java.lang.Object[], java.lang.Object] */
    public static <T> T[] b(T[] tArr, int i8, T t8) {
        if (i8 + 1 > tArr.length) {
            ?? r02 = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), c(i8));
            System.arraycopy(tArr, 0, r02, 0, i8);
            tArr = r02;
        }
        tArr[i8] = t8;
        return tArr;
    }

    public static int c(int i8) {
        if (i8 <= 4) {
            return 8;
        }
        return i8 * 2;
    }
}
