package w3;

import android.content.Context;
import java.util.Collections;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class t implements s {

    /* renamed from: e  reason: collision with root package name */
    private static volatile u f23523e;

    /* renamed from: a  reason: collision with root package name */
    private final g4.a f23524a;

    /* renamed from: b  reason: collision with root package name */
    private final g4.a f23525b;

    /* renamed from: c  reason: collision with root package name */
    private final c4.e f23526c;

    /* renamed from: d  reason: collision with root package name */
    private final d4.p f23527d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t(g4.a aVar, g4.a aVar2, c4.e eVar, d4.p pVar, d4.t tVar) {
        this.f23524a = aVar;
        this.f23525b = aVar2;
        this.f23526c = eVar;
        this.f23527d = pVar;
        tVar.c();
    }

    private i b(n nVar) {
        return i.a().i(this.f23524a.a()).k(this.f23525b.a()).j(nVar.g()).h(new h(nVar.b(), nVar.d())).g(nVar.c().a()).d();
    }

    public static t c() {
        u uVar = f23523e;
        if (uVar != null) {
            return uVar.b();
        }
        throw new IllegalStateException("Not initialized!");
    }

    private static Set<u3.c> d(f fVar) {
        return fVar instanceof g ? Collections.unmodifiableSet(((g) fVar).a()) : Collections.singleton(u3.c.b("proto"));
    }

    public static void f(Context context) {
        if (f23523e == null) {
            synchronized (t.class) {
                if (f23523e == null) {
                    f23523e = e.c().b(context).a();
                }
            }
        }
    }

    @Override // w3.s
    public void a(n nVar, u3.i iVar) {
        this.f23526c.a(nVar.f().f(nVar.c().c()), b(nVar), iVar);
    }

    public d4.p e() {
        return this.f23527d;
    }

    public u3.h g(f fVar) {
        return new p(d(fVar), o.a().b(fVar.getName()).c(fVar.getExtras()).a(), this);
    }
}
