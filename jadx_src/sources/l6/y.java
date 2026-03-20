package l6;

import com.google.android.gms.common.ConnectionResult;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ b0 f21783a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y(b0 b0Var) {
        this.f21783a = b0Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        a0 a0Var;
        a0Var = this.f21783a.f21737g;
        a0Var.b(new ConnectionResult(4));
    }
}
