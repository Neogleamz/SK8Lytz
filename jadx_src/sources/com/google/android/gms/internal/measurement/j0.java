package com.google.android.gms.internal.measurement;

import java.util.Arrays;
import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j0 implements Comparator<r> {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ m f12250a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ g6 f12251b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j0(m mVar, g6 g6Var) {
        this.f12250a = mVar;
        this.f12251b = g6Var;
    }

    @Override // java.util.Comparator
    public final /* synthetic */ int compare(r rVar, r rVar2) {
        r rVar3 = rVar;
        r rVar4 = rVar2;
        m mVar = this.f12250a;
        g6 g6Var = this.f12251b;
        if (rVar3 instanceof y) {
            return !(rVar4 instanceof y) ? 1 : 0;
        } else if (rVar4 instanceof y) {
            return -1;
        } else {
            return mVar == null ? rVar3.e().compareTo(rVar4.e()) : (int) e5.a(mVar.c(g6Var, Arrays.asList(rVar3, rVar4)).d().doubleValue());
        }
    }
}
