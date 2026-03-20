package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.kg;
import com.google.android.gms.internal.measurement.zzs;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w5 implements kg {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ r5 f17071a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w5(r5 r5Var) {
        this.f17071a = r5Var;
    }

    @Override // com.google.android.gms.internal.measurement.kg
    public final void a(zzs zzsVar, String str, List<String> list, boolean z4, boolean z8) {
        z4 D;
        int i8 = y5.f17175a[zzsVar.ordinal()];
        if (i8 == 1) {
            D = this.f17071a.i().D();
        } else if (i8 == 2) {
            x4 i9 = this.f17071a.i();
            D = z4 ? i9.G() : !z8 ? i9.F() : i9.E();
        } else if (i8 != 3) {
            D = i8 != 4 ? this.f17071a.i().H() : this.f17071a.i().I();
        } else {
            x4 i10 = this.f17071a.i();
            D = z4 ? i10.L() : !z8 ? i10.K() : i10.J();
        }
        int size = list.size();
        if (size == 1) {
            D.b(str, list.get(0));
        } else if (size == 2) {
            D.c(str, list.get(0), list.get(1));
        } else if (size != 3) {
            D.a(str);
        } else {
            D.d(str, list.get(0), list.get(1), list.get(2));
        }
    }
}
