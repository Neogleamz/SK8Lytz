package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s extends m {

    /* renamed from: c  reason: collision with root package name */
    private final List<String> f12494c;

    /* renamed from: d  reason: collision with root package name */
    private final List<r> f12495d;

    /* renamed from: e  reason: collision with root package name */
    private g6 f12496e;

    private s(s sVar) {
        super(sVar.f12328a);
        ArrayList arrayList = new ArrayList(sVar.f12494c.size());
        this.f12494c = arrayList;
        arrayList.addAll(sVar.f12494c);
        ArrayList arrayList2 = new ArrayList(sVar.f12495d.size());
        this.f12495d = arrayList2;
        arrayList2.addAll(sVar.f12495d);
        this.f12496e = sVar.f12496e;
    }

    public s(String str, List<r> list, List<r> list2, g6 g6Var) {
        super(str);
        this.f12494c = new ArrayList();
        this.f12496e = g6Var;
        if (!list.isEmpty()) {
            for (r rVar : list) {
                this.f12494c.add(rVar.e());
            }
        }
        this.f12495d = new ArrayList(list2);
    }

    @Override // com.google.android.gms.internal.measurement.m, com.google.android.gms.internal.measurement.r
    public final r a() {
        return new s(this);
    }

    @Override // com.google.android.gms.internal.measurement.m
    public final r c(g6 g6Var, List<r> list) {
        String str;
        r rVar;
        g6 d8 = this.f12496e.d();
        for (int i8 = 0; i8 < this.f12494c.size(); i8++) {
            if (i8 < list.size()) {
                str = this.f12494c.get(i8);
                rVar = g6Var.b(list.get(i8));
            } else {
                str = this.f12494c.get(i8);
                rVar = r.f12463r;
            }
            d8.e(str, rVar);
        }
        for (r rVar2 : this.f12495d) {
            r b9 = d8.b(rVar2);
            if (b9 instanceof u) {
                b9 = d8.b(rVar2);
            }
            if (b9 instanceof k) {
                return ((k) b9).c();
            }
        }
        return r.f12463r;
    }
}
