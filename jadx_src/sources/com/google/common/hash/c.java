package com.google.common.hash;

import com.google.common.base.r;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c {

    /* renamed from: a  reason: collision with root package name */
    private static final r<com.google.common.hash.b> f19558a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements r<com.google.common.hash.b> {
        a() {
        }

        @Override // com.google.common.base.r
        /* renamed from: a */
        public com.google.common.hash.b get() {
            return new d();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements r<com.google.common.hash.b> {
        b() {
        }

        @Override // com.google.common.base.r
        /* renamed from: a */
        public com.google.common.hash.b get() {
            return new C0157c(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: com.google.common.hash.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0157c extends AtomicLong implements com.google.common.hash.b {
        private C0157c() {
        }

        /* synthetic */ C0157c(a aVar) {
            this();
        }

        @Override // com.google.common.hash.b
        public void a(long j8) {
            getAndAdd(j8);
        }
    }

    static {
        r<com.google.common.hash.b> bVar;
        try {
            new d();
            bVar = new a();
        } catch (Throwable unused) {
            bVar = new b();
        }
        f19558a = bVar;
    }

    public static com.google.common.hash.b a() {
        return f19558a.get();
    }
}
