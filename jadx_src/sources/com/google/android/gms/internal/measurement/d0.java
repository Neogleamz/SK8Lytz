package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d0 {

    /* renamed from: a  reason: collision with root package name */
    private Map<String, z> f12126a = new HashMap();

    /* renamed from: b  reason: collision with root package name */
    private s0 f12127b = new s0();

    public d0() {
        b(new x());
        b(new c0());
        b(new e0());
        b(new i0());
        b(new k0());
        b(new q0());
        b(new v0());
    }

    private final void b(z zVar) {
        for (zzbv zzbvVar : zVar.f12724a) {
            this.f12126a.put(zzbvVar.toString(), zVar);
        }
    }

    public final r a(g6 g6Var, r rVar) {
        e5.b(g6Var);
        if (rVar instanceof u) {
            u uVar = (u) rVar;
            ArrayList<r> h8 = uVar.h();
            String c9 = uVar.c();
            return (this.f12126a.containsKey(c9) ? this.f12126a.get(c9) : this.f12127b).b(c9, g6Var, h8);
        }
        return rVar;
    }
}
