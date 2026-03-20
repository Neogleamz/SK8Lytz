package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class v1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object a(Object obj, int i8) {
        if (obj != null) {
            return obj;
        }
        StringBuilder sb = new StringBuilder(20);
        sb.append("at index ");
        sb.append(i8);
        throw new NullPointerException(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object[] b(Object... objArr) {
        c(objArr, objArr.length);
        return objArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object[] c(Object[] objArr, int i8) {
        for (int i9 = 0; i9 < i8; i9++) {
            a(objArr[i9], i9);
        }
        return objArr;
    }

    private static Object[] d(Iterable<?> iterable, Object[] objArr) {
        Iterator<?> it = iterable.iterator();
        int i8 = 0;
        while (it.hasNext()) {
            objArr[i8] = it.next();
            i8++;
        }
        return objArr;
    }

    public static <T> T[] e(T[] tArr, int i8) {
        return (T[]) z1.b(tArr, i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object[] f(Collection<?> collection) {
        return d(collection, new Object[collection.size()]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T[] g(Collection<?> collection, T[] tArr) {
        int size = collection.size();
        if (tArr.length < size) {
            tArr = (T[]) e(tArr, size);
        }
        d(collection, tArr);
        if (tArr.length > size) {
            tArr[size] = null;
        }
        return tArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T[] h(Object[] objArr, int i8, int i9, T[] tArr) {
        com.google.common.base.l.r(i8, i8 + i9, objArr.length);
        if (tArr.length < i9) {
            tArr = (T[]) e(tArr, i9);
        } else if (tArr.length > i9) {
            tArr[i9] = null;
        }
        System.arraycopy(objArr, i8, tArr, 0, i9);
        return tArr;
    }
}
