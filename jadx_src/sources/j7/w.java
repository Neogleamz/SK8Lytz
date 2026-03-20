package j7;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class w implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ x f20843a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w(x xVar) {
        this.f20843a = xVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Object obj;
        d dVar;
        d dVar2;
        obj = this.f20843a.f20845b;
        synchronized (obj) {
            x xVar = this.f20843a;
            dVar = xVar.f20846c;
            if (dVar != null) {
                dVar2 = xVar.f20846c;
                dVar2.a();
            }
        }
    }
}
