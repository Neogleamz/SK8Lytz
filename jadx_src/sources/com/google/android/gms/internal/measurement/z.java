package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class z {

    /* renamed from: a  reason: collision with root package name */
    final List<zzbv> f12724a = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    public final r a(String str) {
        if (this.f12724a.contains(e5.c(str))) {
            throw new UnsupportedOperationException("Command not implemented: " + str);
        }
        throw new IllegalArgumentException("Command not supported");
    }

    public abstract r b(String str, g6 g6Var, List<r> list);
}
