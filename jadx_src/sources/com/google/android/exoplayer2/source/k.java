package com.google.android.exoplayer2.source;

import android.os.Handler;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.z0;
import j4.t1;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface k {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends h5.j {
        public b(h5.j jVar) {
            super(jVar);
        }

        public b(Object obj) {
            super(obj);
        }

        public b(Object obj, int i8, int i9, long j8) {
            super(obj, i8, i9, j8);
        }

        public b(Object obj, long j8) {
            super(obj, j8);
        }

        public b(Object obj, long j8, int i8) {
            super(obj, j8, i8);
        }

        public b c(Object obj) {
            return new b(super.a(obj));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        void a(k kVar, h2 h2Var);
    }

    void a(c cVar, a6.y yVar, t1 t1Var);

    j b(b bVar, a6.b bVar2, long j8);

    void c(c cVar);

    void e(Handler handler, l lVar);

    void f(l lVar);

    void g(c cVar);

    z0 i();

    void l(Handler handler, com.google.android.exoplayer2.drm.h hVar);

    void m(com.google.android.exoplayer2.drm.h hVar);

    void n();

    default boolean o() {
        return true;
    }

    void p(j jVar);

    default h2 q() {
        return null;
    }

    void r(c cVar);
}
