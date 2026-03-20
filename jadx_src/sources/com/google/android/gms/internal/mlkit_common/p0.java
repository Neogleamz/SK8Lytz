package com.google.android.gms.internal.mlkit_common;

import android.content.Context;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p0 implements e0 {

    /* renamed from: a  reason: collision with root package name */
    private q8.b f13003a;

    /* renamed from: b  reason: collision with root package name */
    private final q8.b f13004b;

    /* renamed from: c  reason: collision with root package name */
    private final g0 f13005c;

    public p0(Context context, g0 g0Var) {
        this.f13005c = g0Var;
        com.google.android.datatransport.cct.a aVar = com.google.android.datatransport.cct.a.f8922g;
        w3.t.f(context);
        final u3.h g8 = w3.t.c().g(aVar);
        if (aVar.a().contains(u3.c.b("json"))) {
            this.f13003a = new f8.t(new q8.b() { // from class: com.google.android.gms.internal.mlkit_common.m0
                public final Object get() {
                    return u3.h.this.a("FIREBASE_ML_SDK", byte[].class, u3.c.b("json"), new u3.f() { // from class: com.google.android.gms.internal.mlkit_common.o0
                        @Override // u3.f
                        public final Object apply(Object obj) {
                            return (byte[]) obj;
                        }
                    });
                }
            });
        }
        this.f13004b = new f8.t(new q8.b() { // from class: com.google.android.gms.internal.mlkit_common.n0
            public final Object get() {
                return u3.h.this.a("FIREBASE_ML_SDK", byte[].class, u3.c.b("proto"), new u3.f() { // from class: com.google.android.gms.internal.mlkit_common.l0
                    @Override // u3.f
                    public final Object apply(Object obj) {
                        return (byte[]) obj;
                    }
                });
            }
        });
    }
}
