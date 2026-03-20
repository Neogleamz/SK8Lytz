package z3;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.firebase.encoders.proto.Protobuf;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import w3.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: e  reason: collision with root package name */
    private static final a f24550e = new C0235a().b();

    /* renamed from: a  reason: collision with root package name */
    private final e f24551a;

    /* renamed from: b  reason: collision with root package name */
    private final List<c> f24552b;

    /* renamed from: c  reason: collision with root package name */
    private final b f24553c;

    /* renamed from: d  reason: collision with root package name */
    private final String f24554d;

    /* renamed from: z3.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0235a {

        /* renamed from: a  reason: collision with root package name */
        private e f24555a = null;

        /* renamed from: b  reason: collision with root package name */
        private List<c> f24556b = new ArrayList();

        /* renamed from: c  reason: collision with root package name */
        private b f24557c = null;

        /* renamed from: d  reason: collision with root package name */
        private String f24558d = BuildConfig.FLAVOR;

        C0235a() {
        }

        public C0235a a(c cVar) {
            this.f24556b.add(cVar);
            return this;
        }

        public a b() {
            return new a(this.f24555a, Collections.unmodifiableList(this.f24556b), this.f24557c, this.f24558d);
        }

        public C0235a c(String str) {
            this.f24558d = str;
            return this;
        }

        public C0235a d(b bVar) {
            this.f24557c = bVar;
            return this;
        }

        public C0235a e(e eVar) {
            this.f24555a = eVar;
            return this;
        }
    }

    a(e eVar, List<c> list, b bVar, String str) {
        this.f24551a = eVar;
        this.f24552b = list;
        this.f24553c = bVar;
        this.f24554d = str;
    }

    public static C0235a e() {
        return new C0235a();
    }

    @Protobuf(tag = 4)
    public String a() {
        return this.f24554d;
    }

    @Protobuf(tag = 3)
    public b b() {
        return this.f24553c;
    }

    @Protobuf(tag = 2)
    public List<c> c() {
        return this.f24552b;
    }

    @Protobuf(tag = 1)
    public e d() {
        return this.f24551a;
    }

    public byte[] f() {
        return l.a(this);
    }
}
