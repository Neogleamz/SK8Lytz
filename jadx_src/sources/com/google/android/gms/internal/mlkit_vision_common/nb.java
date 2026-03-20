package com.google.android.gms.internal.mlkit_vision_common;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class nb implements kb {

    /* renamed from: a  reason: collision with root package name */
    final List f15724a;

    public nb(Context context, mb mbVar) {
        ArrayList arrayList = new ArrayList();
        this.f15724a = arrayList;
        if (mbVar.c()) {
            arrayList.add(new xb(context, mbVar));
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.kb
    public final void a(jb jbVar) {
        for (kb kbVar : this.f15724a) {
            kbVar.a(jbVar);
        }
    }
}
