package com.google.common.reflect;

import com.google.common.base.l;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {
    public static <T> T a(Class<T> cls, InvocationHandler invocationHandler) {
        l.n(invocationHandler);
        l.i(cls.isInterface(), "%s is not an interface", cls);
        return cls.cast(Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, invocationHandler));
    }
}
