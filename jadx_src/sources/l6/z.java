package l6;

import com.google.android.gms.signin.internal.zak;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class z implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ zak f21784a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ b0 f21785b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public z(b0 b0Var, zak zakVar) {
        this.f21785b = b0Var;
        this.f21784a = zakVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        b0.q(this.f21785b, this.f21784a);
    }
}
