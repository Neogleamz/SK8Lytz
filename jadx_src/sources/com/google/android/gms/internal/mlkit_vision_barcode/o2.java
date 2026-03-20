package com.google.android.gms.internal.mlkit_vision_barcode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o2 {

    /* renamed from: a  reason: collision with root package name */
    private final Map f13850a;

    /* renamed from: b  reason: collision with root package name */
    private final Map f13851b;

    /* renamed from: c  reason: collision with root package name */
    private final j8.c f13852c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o2(Map map, Map map2, j8.c cVar) {
        this.f13850a = map;
        this.f13851b = map2;
        this.f13852c = cVar;
    }

    public final byte[] a(Object obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new l2(byteArrayOutputStream, this.f13850a, this.f13851b, this.f13852c).i(obj);
        } catch (IOException unused) {
        }
        return byteArrayOutputStream.toByteArray();
    }
}
