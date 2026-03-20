package com.google.android.gms.internal.measurement;

import android.database.ContentObserver;
import android.os.Handler;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class t5 extends ContentObserver {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ r5 f12522a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public t5(r5 r5Var, Handler handler) {
        super(null);
        this.f12522a = r5Var;
    }

    @Override // android.database.ContentObserver
    public final void onChange(boolean z4) {
        AtomicBoolean atomicBoolean;
        atomicBoolean = this.f12522a.f12475a;
        atomicBoolean.set(true);
    }
}
