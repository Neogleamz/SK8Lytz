package x;

import android.util.Size;
import androidx.camera.core.h0;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.f;
import androidx.camera.core.impl.j;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import x.l;
import y.u;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o {

    /* renamed from: g  reason: collision with root package name */
    static final e0.a f23735g = new e0.a();

    /* renamed from: a  reason: collision with root package name */
    private final j f23736a;

    /* renamed from: b  reason: collision with root package name */
    private final androidx.camera.core.impl.f f23737b;

    /* renamed from: c  reason: collision with root package name */
    private final l f23738c;

    /* renamed from: d  reason: collision with root package name */
    private final f0 f23739d;

    /* renamed from: e  reason: collision with root package name */
    private final z f23740e;

    /* renamed from: f  reason: collision with root package name */
    private final l.a f23741f;

    public o(j jVar, Size size) {
        androidx.camera.core.impl.utils.m.a();
        this.f23736a = jVar;
        this.f23737b = f.a.j(jVar).h();
        l lVar = new l();
        this.f23738c = lVar;
        f0 f0Var = new f0();
        this.f23739d = f0Var;
        Executor R = jVar.R(z.a.c());
        Objects.requireNonNull(R);
        z zVar = new z(R);
        this.f23740e = zVar;
        l.a g8 = l.a.g(size, jVar.m());
        this.f23741f = g8;
        zVar.p(f0Var.f(lVar.i(g8)));
    }

    private i b(u uVar, o0 o0Var, g0 g0Var) {
        ArrayList arrayList = new ArrayList();
        String valueOf = String.valueOf(uVar.hashCode());
        List<androidx.camera.core.impl.g> a9 = uVar.a();
        Objects.requireNonNull(a9);
        for (androidx.camera.core.impl.g gVar : a9) {
            f.a aVar = new f.a();
            aVar.p(this.f23737b.g());
            aVar.e(this.f23737b.d());
            aVar.a(o0Var.m());
            aVar.f(this.f23741f.f());
            if (this.f23741f.c() == 256) {
                if (f23735g.a()) {
                    aVar.d(androidx.camera.core.impl.f.f2555h, Integer.valueOf(o0Var.k()));
                }
                aVar.d(androidx.camera.core.impl.f.f2556i, Integer.valueOf(g(o0Var)));
            }
            aVar.e(gVar.a().d());
            aVar.g(valueOf, Integer.valueOf(gVar.e()));
            aVar.c(this.f23741f.b());
            arrayList.add(aVar.h());
        }
        return new i(arrayList, g0Var);
    }

    private u c() {
        u L = this.f23736a.L(androidx.camera.core.z.c());
        Objects.requireNonNull(L);
        return L;
    }

    private a0 d(u uVar, o0 o0Var, g0 g0Var) {
        return new a0(uVar, o0Var.j(), o0Var.f(), o0Var.k(), o0Var.h(), o0Var.l(), g0Var);
    }

    public void a() {
        androidx.camera.core.impl.utils.m.a();
        this.f23738c.g();
        this.f23739d.d();
        this.f23740e.n();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public androidx.core.util.d<i, a0> e(o0 o0Var, g0 g0Var) {
        androidx.camera.core.impl.utils.m.a();
        u c9 = c();
        return new androidx.core.util.d<>(b(c9, o0Var, g0Var), d(c9, o0Var, g0Var));
    }

    public SessionConfig.b f() {
        SessionConfig.b o5 = SessionConfig.b.o(this.f23736a);
        o5.h(this.f23741f.f());
        return o5;
    }

    int g(o0 o0Var) {
        return ((o0Var.i() != null) && androidx.camera.core.impl.utils.n.e(o0Var.f(), this.f23741f.e())) ? o0Var.e() == 0 ? 100 : 95 : o0Var.h();
    }

    public int h() {
        androidx.camera.core.impl.utils.m.a();
        return this.f23738c.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i(a0 a0Var) {
        androidx.camera.core.impl.utils.m.a();
        this.f23741f.d().accept(a0Var);
    }

    public void j(h0.a aVar) {
        androidx.camera.core.impl.utils.m.a();
        this.f23738c.h(aVar);
    }
}
