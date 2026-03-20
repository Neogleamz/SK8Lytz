package com.google.android.datatransport.cct.internal;

import com.google.android.datatransport.cct.internal.f;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class j {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {
        public abstract j a();

        public abstract a b(Integer num);

        public abstract a c(long j8);

        public abstract a d(long j8);

        public abstract a e(NetworkConnectionInfo networkConnectionInfo);

        abstract a f(byte[] bArr);

        abstract a g(String str);

        public abstract a h(long j8);
    }

    private static a a() {
        return new f.b();
    }

    public static a i(String str) {
        return a().g(str);
    }

    public static a j(byte[] bArr) {
        return a().f(bArr);
    }

    public abstract Integer b();

    public abstract long c();

    public abstract long d();

    public abstract NetworkConnectionInfo e();

    public abstract byte[] f();

    public abstract String g();

    public abstract long h();
}
