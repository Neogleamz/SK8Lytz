package com.google.android.gms.internal.measurement;

import java.io.File;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class k1 implements h1 {
    @Override // com.google.android.gms.internal.measurement.h1
    public final /* synthetic */ String a(File file, String str, j1 j1Var) {
        return c(new File(file, str).getPath(), j1Var);
    }

    @Override // com.google.android.gms.internal.measurement.h1
    public final /* synthetic */ String b(File file, String str) {
        return a(file, str, j1.f12252a);
    }

    @Override // com.google.android.gms.internal.measurement.h1
    public final String c(String str, j1 j1Var) {
        return str;
    }

    @Override // com.google.android.gms.internal.measurement.h1
    public final /* synthetic */ String h(String str) {
        return c(str, j1.f12252a);
    }
}
