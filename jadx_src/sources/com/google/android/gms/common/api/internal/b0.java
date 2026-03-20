package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b0 extends l6.q {

    /* renamed from: b  reason: collision with root package name */
    protected final j7.k f11621b;

    public b0(int i8, j7.k kVar) {
        super(i8);
        this.f11621b = kVar;
    }

    @Override // com.google.android.gms.common.api.internal.g0
    public final void a(Status status) {
        this.f11621b.d(new ApiException(status));
    }

    @Override // com.google.android.gms.common.api.internal.g0
    public final void b(Exception exc) {
        this.f11621b.d(exc);
    }

    @Override // com.google.android.gms.common.api.internal.g0
    public final void c(r rVar) {
        try {
            h(rVar);
        } catch (DeadObjectException e8) {
            a(g0.e(e8));
            throw e8;
        } catch (RemoteException e9) {
            a(g0.e(e9));
        } catch (RuntimeException e10) {
            this.f11621b.d(e10);
        }
    }

    protected abstract void h(r rVar);
}
