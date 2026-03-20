package c4;

import d4.v;
import f4.a;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import w3.o;
import w3.t;
import x3.k;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c implements e {

    /* renamed from: f  reason: collision with root package name */
    private static final Logger f8307f = Logger.getLogger(t.class.getName());

    /* renamed from: a  reason: collision with root package name */
    private final v f8308a;

    /* renamed from: b  reason: collision with root package name */
    private final Executor f8309b;

    /* renamed from: c  reason: collision with root package name */
    private final x3.d f8310c;

    /* renamed from: d  reason: collision with root package name */
    private final e4.d f8311d;

    /* renamed from: e  reason: collision with root package name */
    private final f4.a f8312e;

    public c(Executor executor, x3.d dVar, v vVar, e4.d dVar2, f4.a aVar) {
        this.f8309b = executor;
        this.f8310c = dVar;
        this.f8308a = vVar;
        this.f8311d = dVar2;
        this.f8312e = aVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object d(o oVar, w3.i iVar) {
        this.f8311d.k0(oVar, iVar);
        this.f8308a.a(oVar, 1);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void e(final o oVar, u3.i iVar, w3.i iVar2) {
        try {
            k a9 = this.f8310c.a(oVar.b());
            if (a9 == null) {
                String format = String.format("Transport backend '%s' is not registered", oVar.b());
                f8307f.warning(format);
                iVar.a(new IllegalArgumentException(format));
                return;
            }
            final w3.i b9 = a9.b(iVar2);
            this.f8312e.b(new a.InterfaceC0170a() { // from class: c4.a
                @Override // f4.a.InterfaceC0170a
                public final Object h() {
                    Object d8;
                    d8 = c.this.d(oVar, b9);
                    return d8;
                }
            });
            iVar.a(null);
        } catch (Exception e8) {
            Logger logger = f8307f;
            logger.warning("Error scheduling event " + e8.getMessage());
            iVar.a(e8);
        }
    }

    @Override // c4.e
    public void a(final o oVar, final w3.i iVar, final u3.i iVar2) {
        this.f8309b.execute(new Runnable() { // from class: c4.b
            @Override // java.lang.Runnable
            public final void run() {
                c.this.e(oVar, iVar2, iVar);
            }
        });
    }
}
