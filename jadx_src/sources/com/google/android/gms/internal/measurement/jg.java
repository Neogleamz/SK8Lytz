package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class jg extends m {

    /* renamed from: c  reason: collision with root package name */
    private boolean f12269c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f12270d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ bg f12271e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public jg(bg bgVar, boolean z4, boolean z8) {
        super("log");
        this.f12271e = bgVar;
        this.f12269c = z4;
        this.f12270d = z8;
    }

    @Override // com.google.android.gms.internal.measurement.m
    public final r c(g6 g6Var, List<r> list) {
        List<String> arrayList;
        kg kgVar;
        kg kgVar2;
        e5.k("log", 1, list);
        if (list.size() == 1) {
            kgVar2 = this.f12271e.f12111c;
            kgVar2.a(zzs.INFO, g6Var.b(list.get(0)).e(), Collections.emptyList(), this.f12269c, this.f12270d);
        } else {
            zzs c9 = zzs.c(e5.i(g6Var.b(list.get(0)).d().doubleValue()));
            String e8 = g6Var.b(list.get(1)).e();
            if (list.size() == 2) {
                kgVar = this.f12271e.f12111c;
                arrayList = Collections.emptyList();
            } else {
                arrayList = new ArrayList<>();
                for (int i8 = 2; i8 < Math.min(list.size(), 5); i8++) {
                    arrayList.add(g6Var.b(list.get(i8)).e());
                }
                kgVar = this.f12271e.f12111c;
            }
            kgVar.a(c9, e8, arrayList, this.f12269c, this.f12270d);
        }
        return r.f12463r;
    }
}
