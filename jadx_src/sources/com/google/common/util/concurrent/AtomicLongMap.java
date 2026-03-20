package com.google.common.util.concurrent;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class AtomicLongMap<K> implements Serializable {

    /* renamed from: a  reason: collision with root package name */
    private final ConcurrentHashMap<K, AtomicLong> f19657a;

    public String toString() {
        return this.f19657a.toString();
    }
}
