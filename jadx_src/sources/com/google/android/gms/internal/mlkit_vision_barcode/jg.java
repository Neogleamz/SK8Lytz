package com.google.android.gms.internal.mlkit_vision_barcode;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class jg implements gg {

    /* renamed from: a  reason: collision with root package name */
    final List f13614a;

    public jg(Context context, ig igVar) {
        ArrayList arrayList = new ArrayList();
        this.f13614a = arrayList;
        if (igVar.c()) {
            arrayList.add(new yg(context, igVar));
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.gg
    public final void a(fg fgVar) {
        for (gg ggVar : this.f13614a) {
            ggVar.a(fgVar);
        }
    }
}
