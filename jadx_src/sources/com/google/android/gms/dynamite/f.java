package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f implements DynamiteModule.a {
    @Override // com.google.android.gms.dynamite.DynamiteModule.a
    public final DynamiteModule.a.b a(Context context, String str, DynamiteModule.a.InterfaceC0126a interfaceC0126a) {
        DynamiteModule.a.b bVar = new DynamiteModule.a.b();
        int a9 = interfaceC0126a.a(context, str, false);
        bVar.f12033b = a9;
        bVar.f12034c = a9 != 0 ? 1 : 0;
        return bVar;
    }
}
