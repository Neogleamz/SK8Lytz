package w3;

import w3.c;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class n {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {
        public abstract n a();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract a b(u3.c cVar);

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract a c(u3.d<?> dVar);

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract a d(u3.f<?, byte[]> fVar);

        public abstract a e(o oVar);

        public abstract a f(String str);
    }

    public static a a() {
        return new c.b();
    }

    public abstract u3.c b();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract u3.d<?> c();

    public byte[] d() {
        return e().apply(c().b());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract u3.f<?, byte[]> e();

    public abstract o f();

    public abstract String g();
}
