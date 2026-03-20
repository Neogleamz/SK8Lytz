package com.google.android.datatransport.cct.internal;

import com.google.android.datatransport.cct.internal.g;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class k {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {
        public abstract k a();

        public abstract a b(ClientInfo clientInfo);

        public abstract a c(List<j> list);

        abstract a d(Integer num);

        abstract a e(String str);

        public abstract a f(QosTier qosTier);

        public abstract a g(long j8);

        public abstract a h(long j8);

        public a i(int i8) {
            return d(Integer.valueOf(i8));
        }

        public a j(String str) {
            return e(str);
        }
    }

    public static a a() {
        return new g.b();
    }

    public abstract ClientInfo b();

    public abstract List<j> c();

    public abstract Integer d();

    public abstract String e();

    public abstract QosTier f();

    public abstract long g();

    public abstract long h();
}
