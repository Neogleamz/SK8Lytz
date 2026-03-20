package androidx.camera.core.impl;

import androidx.camera.core.impl.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import y.a1;
import y.n0;
import y.o0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {

    /* renamed from: h  reason: collision with root package name */
    public static final Config.a<Integer> f2555h = Config.a.a("camerax.core.captureConfig.rotation", Integer.TYPE);

    /* renamed from: i  reason: collision with root package name */
    public static final Config.a<Integer> f2556i = Config.a.a("camerax.core.captureConfig.jpegQuality", Integer.class);

    /* renamed from: a  reason: collision with root package name */
    final List<DeferrableSurface> f2557a;

    /* renamed from: b  reason: collision with root package name */
    final Config f2558b;

    /* renamed from: c  reason: collision with root package name */
    final int f2559c;

    /* renamed from: d  reason: collision with root package name */
    final List<y.h> f2560d;

    /* renamed from: e  reason: collision with root package name */
    private final boolean f2561e;

    /* renamed from: f  reason: collision with root package name */
    private final a1 f2562f;

    /* renamed from: g  reason: collision with root package name */
    private final y.j f2563g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final Set<DeferrableSurface> f2564a;

        /* renamed from: b  reason: collision with root package name */
        private m f2565b;

        /* renamed from: c  reason: collision with root package name */
        private int f2566c;

        /* renamed from: d  reason: collision with root package name */
        private List<y.h> f2567d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f2568e;

        /* renamed from: f  reason: collision with root package name */
        private o0 f2569f;

        /* renamed from: g  reason: collision with root package name */
        private y.j f2570g;

        public a() {
            this.f2564a = new HashSet();
            this.f2565b = n.P();
            this.f2566c = -1;
            this.f2567d = new ArrayList();
            this.f2568e = false;
            this.f2569f = o0.f();
        }

        private a(f fVar) {
            HashSet hashSet = new HashSet();
            this.f2564a = hashSet;
            this.f2565b = n.P();
            this.f2566c = -1;
            this.f2567d = new ArrayList();
            this.f2568e = false;
            this.f2569f = o0.f();
            hashSet.addAll(fVar.f2557a);
            this.f2565b = n.Q(fVar.f2558b);
            this.f2566c = fVar.f2559c;
            this.f2567d.addAll(fVar.b());
            this.f2568e = fVar.h();
            this.f2569f = o0.g(fVar.f());
        }

        public static a j(v<?> vVar) {
            b p8 = vVar.p(null);
            if (p8 != null) {
                a aVar = new a();
                p8.a(vVar, aVar);
                return aVar;
            }
            throw new IllegalStateException("Implementation is missing option unpacker for " + vVar.w(vVar.toString()));
        }

        public static a k(f fVar) {
            return new a(fVar);
        }

        public void a(Collection<y.h> collection) {
            for (y.h hVar : collection) {
                c(hVar);
            }
        }

        public void b(a1 a1Var) {
            this.f2569f.e(a1Var);
        }

        public void c(y.h hVar) {
            if (this.f2567d.contains(hVar)) {
                return;
            }
            this.f2567d.add(hVar);
        }

        public <T> void d(Config.a<T> aVar, T t8) {
            this.f2565b.s(aVar, t8);
        }

        public void e(Config config) {
            for (Config.a<?> aVar : config.e()) {
                Object f5 = this.f2565b.f(aVar, null);
                Object a9 = config.a(aVar);
                if (f5 instanceof n0) {
                    ((n0) f5).a(((n0) a9).c());
                } else {
                    if (a9 instanceof n0) {
                        a9 = ((n0) a9).clone();
                    }
                    this.f2565b.o(aVar, config.g(aVar), a9);
                }
            }
        }

        public void f(DeferrableSurface deferrableSurface) {
            this.f2564a.add(deferrableSurface);
        }

        public void g(String str, Object obj) {
            this.f2569f.h(str, obj);
        }

        public f h() {
            return new f(new ArrayList(this.f2564a), o.N(this.f2565b), this.f2566c, this.f2567d, this.f2568e, a1.b(this.f2569f), this.f2570g);
        }

        public void i() {
            this.f2564a.clear();
        }

        public Set<DeferrableSurface> l() {
            return this.f2564a;
        }

        public int m() {
            return this.f2566c;
        }

        public void n(y.j jVar) {
            this.f2570g = jVar;
        }

        public void o(Config config) {
            this.f2565b = n.Q(config);
        }

        public void p(int i8) {
            this.f2566c = i8;
        }

        public void q(boolean z4) {
            this.f2568e = z4;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a(v<?> vVar, a aVar);
    }

    f(List<DeferrableSurface> list, Config config, int i8, List<y.h> list2, boolean z4, a1 a1Var, y.j jVar) {
        this.f2557a = list;
        this.f2558b = config;
        this.f2559c = i8;
        this.f2560d = Collections.unmodifiableList(list2);
        this.f2561e = z4;
        this.f2562f = a1Var;
        this.f2563g = jVar;
    }

    public static f a() {
        return new a().h();
    }

    public List<y.h> b() {
        return this.f2560d;
    }

    public y.j c() {
        return this.f2563g;
    }

    public Config d() {
        return this.f2558b;
    }

    public List<DeferrableSurface> e() {
        return Collections.unmodifiableList(this.f2557a);
    }

    public a1 f() {
        return this.f2562f;
    }

    public int g() {
        return this.f2559c;
    }

    public boolean h() {
        return this.f2561e;
    }
}
