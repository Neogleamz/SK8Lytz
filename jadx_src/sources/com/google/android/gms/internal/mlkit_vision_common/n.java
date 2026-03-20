package com.google.android.gms.internal.mlkit_vision_common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n {

    /* renamed from: a  reason: collision with root package name */
    private final Map f15699a;

    /* renamed from: b  reason: collision with root package name */
    private final Map f15700b;

    /* renamed from: c  reason: collision with root package name */
    private final j8.c f15701c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(Map map, Map map2, j8.c cVar) {
        this.f15699a = map;
        this.f15700b = map2;
        this.f15701c = cVar;
    }

    public final byte[] a(Object obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new k(byteArrayOutputStream, this.f15699a, this.f15700b, this.f15701c).i(obj);
        } catch (IOException unused) {
        }
        return byteArrayOutputStream.toByteArray();
    }
}
