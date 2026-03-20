package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e implements DynamiteModule.a {
    @Override // com.google.android.gms.dynamite.DynamiteModule.a
    public final DynamiteModule.a.b a(Context context, String str, DynamiteModule.a.InterfaceC0126a interfaceC0126a) {
        DynamiteModule.a.b bVar = new DynamiteModule.a.b();
        int b9 = interfaceC0126a.b(context, str);
        bVar.f12032a = b9;
        if (b9 != 0) {
            bVar.f12034c = -1;
        } else {
            int a9 = interfaceC0126a.a(context, str, true);
            bVar.f12033b = a9;
            if (a9 != 0) {
                bVar.f12034c = 1;
            }
        }
        return bVar;
    }
}
