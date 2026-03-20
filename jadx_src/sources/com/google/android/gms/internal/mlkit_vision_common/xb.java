package com.google.android.gms.internal.mlkit_vision_common;

import android.content.Context;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class xb implements kb {

    /* renamed from: a  reason: collision with root package name */
    private q8.b f16081a;

    /* renamed from: b  reason: collision with root package name */
    private final q8.b f16082b;

    /* renamed from: c  reason: collision with root package name */
    private final mb f16083c;

    public xb(Context context, mb mbVar) {
        this.f16083c = mbVar;
        com.google.android.datatransport.cct.a aVar = com.google.android.datatransport.cct.a.f8922g;
        w3.t.f(context);
        final u3.h g8 = w3.t.c().g(aVar);
        if (aVar.a().contains(u3.c.b("json"))) {
            this.f16081a = new f8.t(new q8.b() { // from class: com.google.android.gms.internal.mlkit_vision_common.ub
                public final Object get() {
                    return u3.h.this.a("FIREBASE_ML_SDK", byte[].class, u3.c.b("json"), new u3.f() { // from class: com.google.android.gms.internal.mlkit_vision_common.wb
                        @Override // u3.f
                        public final Object apply(Object obj) {
                            return (byte[]) obj;
                        }
                    });
                }
            });
        }
        this.f16082b = new f8.t(new q8.b() { // from class: com.google.android.gms.internal.mlkit_vision_common.vb
            public final Object get() {
                return u3.h.this.a("FIREBASE_ML_SDK", byte[].class, u3.c.b("proto"), new u3.f() { // from class: com.google.android.gms.internal.mlkit_vision_common.tb
                    @Override // u3.f
                    public final Object apply(Object obj) {
                        return (byte[]) obj;
                    }
                });
            }
        });
    }

    static u3.d b(mb mbVar, jb jbVar) {
        return u3.d.g(jbVar.c(mbVar.a(), false));
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.kb
    public final void a(jb jbVar) {
        q8.b bVar;
        if (this.f16083c.a() == 0) {
            bVar = this.f16081a;
            if (bVar == null) {
                return;
            }
        } else {
            bVar = this.f16082b;
        }
        ((u3.g) bVar.get()).a(b(this.f16083c, jbVar));
    }
}
