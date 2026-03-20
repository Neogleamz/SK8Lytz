package j7;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class y implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ j f20847a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ z f20848b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y(z zVar, j jVar) {
        this.f20848b = zVar;
        this.f20847a = jVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Object obj;
        e eVar;
        e eVar2;
        obj = this.f20848b.f20850b;
        synchronized (obj) {
            z zVar = this.f20848b;
            eVar = zVar.f20851c;
            if (eVar != null) {
                eVar2 = zVar.f20851c;
                eVar2.a(this.f20847a);
            }
        }
    }
}
