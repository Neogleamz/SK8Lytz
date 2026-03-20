package com.google.common.collect;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T[] a(Object[] objArr, int i8, int i9, T[] tArr) {
        return (T[]) Arrays.copyOfRange(objArr, i8, i9, tArr.getClass());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T[] b(T[] tArr, int i8) {
        return (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), i8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> Map<K, V> c(int i8) {
        return v.y(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> Set<E> d(int i8) {
        return w.p(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> Map<K, V> e(int i8) {
        return y.d0(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> Set<E> f(int i8) {
        return z.S(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> Map<K, V> g() {
        return v.t();
    }
}
