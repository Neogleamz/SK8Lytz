package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ib implements d5 {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f16688a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ gb f16689b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ib(gb gbVar, String str) {
        this.f16688a = str;
        this.f16689b = gbVar;
    }

    @Override // com.google.android.gms.measurement.internal.d5
    public final void a(String str, int i8, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        this.f16689b.I(true, i8, th, bArr, this.f16688a);
    }
}
