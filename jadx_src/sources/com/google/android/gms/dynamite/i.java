package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class i implements DynamiteModule.a {
    @Override // com.google.android.gms.dynamite.DynamiteModule.a
    public final DynamiteModule.a.b a(Context context, String str, DynamiteModule.a.InterfaceC0126a interfaceC0126a) {
        DynamiteModule.a.b bVar = new DynamiteModule.a.b();
        bVar.f12032a = interfaceC0126a.b(context, str);
        int i8 = 1;
        int a9 = interfaceC0126a.a(context, str, true);
        bVar.f12033b = a9;
        int i9 = bVar.f12032a;
        if (i9 == 0) {
            if (a9 == 0) {
                i8 = 0;
                bVar.f12034c = i8;
                return bVar;
            }
            i9 = 0;
        }
        if (a9 < i9) {
            i8 = -1;
        }
        bVar.f12034c = i8;
        return bVar;
    }
}
