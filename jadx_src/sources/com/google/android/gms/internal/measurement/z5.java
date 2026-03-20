package com.google.android.gms.internal.measurement;

import android.database.ContentObserver;
import android.os.Handler;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class z5 extends ContentObserver {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ x5 f12728a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public z5(x5 x5Var, Handler handler) {
        super(null);
        this.f12728a = x5Var;
    }

    @Override // android.database.ContentObserver
    public final void onChange(boolean z4) {
        this.f12728a.e();
    }
}
