package d4;

import e4.d;
import java.util.concurrent.Executor;
import w3.o;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class t {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f19729a;

    /* renamed from: b  reason: collision with root package name */
    private final d f19730b;

    /* renamed from: c  reason: collision with root package name */
    private final v f19731c;

    /* renamed from: d  reason: collision with root package name */
    private final f4.a f19732d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t(Executor executor, d dVar, v vVar, f4.a aVar) {
        this.f19729a = executor;
        this.f19730b = dVar;
        this.f19731c = vVar;
        this.f19732d = aVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object d() {
        for (o oVar : this.f19730b.W()) {
            this.f19731c.a(oVar, 1);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void e() {
        this.f19732d.b(new r(this));
    }

    public void c() {
        this.f19729a.execute(new s(this));
    }
}
