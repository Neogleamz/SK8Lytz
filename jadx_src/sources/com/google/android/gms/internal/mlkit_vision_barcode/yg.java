package com.google.android.gms.internal.mlkit_vision_barcode;

import android.content.Context;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class yg implements gg {

    /* renamed from: a  reason: collision with root package name */
    private q8.b f14260a;

    /* renamed from: b  reason: collision with root package name */
    private final q8.b f14261b;

    /* renamed from: c  reason: collision with root package name */
    private final ig f14262c;

    public yg(Context context, ig igVar) {
        this.f14262c = igVar;
        com.google.android.datatransport.cct.a aVar = com.google.android.datatransport.cct.a.f8922g;
        w3.t.f(context);
        final u3.h g8 = w3.t.c().g(aVar);
        if (aVar.a().contains(u3.c.b("json"))) {
            this.f14260a = new f8.t(new q8.b() { // from class: com.google.android.gms.internal.mlkit_vision_barcode.vg
                public final Object get() {
                    return u3.h.this.a("FIREBASE_ML_SDK", byte[].class, u3.c.b("json"), new u3.f() { // from class: com.google.android.gms.internal.mlkit_vision_barcode.xg
                        @Override // u3.f
                        public final Object apply(Object obj) {
                            return (byte[]) obj;
                        }
                    });
                }
            });
        }
        this.f14261b = new f8.t(new q8.b() { // from class: com.google.android.gms.internal.mlkit_vision_barcode.wg
            public final Object get() {
                return u3.h.this.a("FIREBASE_ML_SDK", byte[].class, u3.c.b("proto"), new u3.f() { // from class: com.google.android.gms.internal.mlkit_vision_barcode.ug
                    @Override // u3.f
                    public final Object apply(Object obj) {
                        return (byte[]) obj;
                    }
                });
            }
        });
    }

    static u3.d b(ig igVar, fg fgVar) {
        int a9 = igVar.a();
        int zza = fgVar.zza();
        byte[] d8 = fgVar.d(a9, false);
        return zza != 0 ? u3.d.e(d8) : u3.d.g(d8);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.gg
    public final void a(fg fgVar) {
        q8.b bVar;
        if (this.f14262c.a() == 0) {
            bVar = this.f14260a;
            if (bVar == null) {
                return;
            }
        } else {
            bVar = this.f14261b;
        }
        ((u3.g) bVar.get()).a(b(this.f14262c, fgVar));
    }
}
