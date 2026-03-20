package com.google.common.collect;

import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface z2<R, C, V> {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a<R, C, V> {
        C a();

        R b();

        V getValue();
    }

    Set<a<R, C, V>> a();

    Map<R, Map<C, V>> b();

    int size();
}
