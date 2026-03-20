package q6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import n6.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: a  reason: collision with root package name */
    private final List f22627a;

    /* renamed from: b  reason: collision with root package name */
    private final q6.a f22628b;

    /* renamed from: c  reason: collision with root package name */
    private final Executor f22629c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f22630d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        private final List f22631a = new ArrayList();

        /* renamed from: b  reason: collision with root package name */
        private boolean f22632b = true;

        /* renamed from: c  reason: collision with root package name */
        private q6.a f22633c;

        /* renamed from: d  reason: collision with root package name */
        private Executor f22634d;

        public a a(k6.b bVar) {
            this.f22631a.add(bVar);
            return this;
        }

        public d b() {
            return new d(this.f22631a, this.f22633c, this.f22634d, this.f22632b, null);
        }
    }

    /* synthetic */ d(List list, q6.a aVar, Executor executor, boolean z4, g gVar) {
        j.m(list, "APIs must not be null.");
        j.b(!list.isEmpty(), "APIs must not be empty.");
        if (executor != null) {
            j.m(aVar, "Listener must not be null when listener executor is set.");
        }
        this.f22627a = list;
        this.f22628b = aVar;
        this.f22629c = executor;
        this.f22630d = z4;
    }

    public static a d() {
        return new a();
    }

    public List<k6.b> a() {
        return this.f22627a;
    }

    public q6.a b() {
        return this.f22628b;
    }

    public Executor c() {
        return this.f22629c;
    }

    public final boolean e() {
        return this.f22630d;
    }
}
