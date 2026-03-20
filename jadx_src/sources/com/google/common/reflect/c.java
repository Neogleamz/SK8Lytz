package com.google.common.reflect;

import com.google.common.base.l;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class c<T> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public final Type a() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        l.i(genericSuperclass instanceof ParameterizedType, "%s isn't parameterized", genericSuperclass);
        return ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
    }
}
