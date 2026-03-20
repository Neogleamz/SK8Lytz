package com.google.common.collect;

import java.io.Serializable;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ImmutableClassToInstanceMap<B> extends m0<Class<? extends B>, B> implements Serializable {

    /* renamed from: b  reason: collision with root package name */
    private static final ImmutableClassToInstanceMap<Object> f18948b = new ImmutableClassToInstanceMap<>(ImmutableMap.n());

    /* renamed from: a  reason: collision with root package name */
    private final ImmutableMap<Class<? extends B>, B> f18949a;

    private ImmutableClassToInstanceMap(ImmutableMap<Class<? extends B>, B> immutableMap) {
        this.f18949a = immutableMap;
    }

    public static <B> ImmutableClassToInstanceMap<B> q() {
        return (ImmutableClassToInstanceMap<B>) f18948b;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.m0, com.google.common.collect.p0
    /* renamed from: i */
    public Map<Class<? extends B>, B> h() {
        return this.f18949a;
    }

    Object readResolve() {
        return isEmpty() ? q() : this;
    }
}
