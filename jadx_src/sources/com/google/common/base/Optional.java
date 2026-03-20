package com.google.common.base;

import java.io.Serializable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class Optional<T> implements Serializable {
    private static final long serialVersionUID = 0;

    public static <T> Optional<T> a() {
        return a.f();
    }

    public static <T> Optional<T> d(T t8) {
        return new o(l.n(t8));
    }

    public abstract T b();

    public abstract boolean c();

    public abstract T e(T t8);
}
