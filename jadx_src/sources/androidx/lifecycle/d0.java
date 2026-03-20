package androidx.lifecycle;

import kotlin.jvm.internal.Lambda;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d0 {

    /* JADX INFO: Add missing generic type declarations: [X] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class a<X> extends Lambda implements mj.l<X, cj.a0> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ n<Y> f5863a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ mj.l<X, Y> f5864b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(n<Y> nVar, mj.l<X, Y> lVar) {
            super(1);
            this.f5863a = nVar;
            this.f5864b = lVar;
        }

        public final void c(X x8) {
            this.f5863a.o(this.f5864b.invoke(x8));
        }

        /* JADX WARN: Multi-variable type inference failed */
        public /* bridge */ /* synthetic */ Object invoke(Object obj) {
            c(obj);
            return cj.a0.a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b implements q, kotlin.jvm.internal.l {

        /* renamed from: a  reason: collision with root package name */
        private final /* synthetic */ mj.l f5865a;

        b(mj.l lVar) {
            kotlin.jvm.internal.p.e(lVar, "function");
            this.f5865a = lVar;
        }

        public final cj.g<?> a() {
            return this.f5865a;
        }

        @Override // androidx.lifecycle.q
        public final /* synthetic */ void b(Object obj) {
            this.f5865a.invoke(obj);
        }

        public final boolean equals(Object obj) {
            if ((obj instanceof q) && (obj instanceof kotlin.jvm.internal.l)) {
                return kotlin.jvm.internal.p.a(a(), ((kotlin.jvm.internal.l) obj).a());
            }
            return false;
        }

        public final int hashCode() {
            return a().hashCode();
        }
    }

    public static final <X, Y> LiveData<Y> a(LiveData<X> liveData, mj.l<X, Y> lVar) {
        kotlin.jvm.internal.p.e(liveData, "<this>");
        kotlin.jvm.internal.p.e(lVar, "transform");
        n nVar = new n();
        nVar.p(liveData, new b(new a(nVar, lVar)));
        return nVar;
    }
}
