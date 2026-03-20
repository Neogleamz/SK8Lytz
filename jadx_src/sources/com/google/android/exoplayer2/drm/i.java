package com.google.android.exoplayer2.drm;

import android.os.Looper;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.h;
import com.google.android.exoplayer2.w0;
import j4.t1;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface i {

    /* renamed from: a  reason: collision with root package name */
    public static final i f9623a;
    @Deprecated

    /* renamed from: b  reason: collision with root package name */
    public static final i f9624b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements i {
        a() {
        }

        @Override // com.google.android.exoplayer2.drm.i
        public void b(Looper looper, t1 t1Var) {
        }

        @Override // com.google.android.exoplayer2.drm.i
        public int c(w0 w0Var) {
            return w0Var.q != null ? 1 : 0;
        }

        @Override // com.google.android.exoplayer2.drm.i
        public DrmSession d(h.a aVar, w0 w0Var) {
            if (w0Var.q == null) {
                return null;
            }
            return new l(new DrmSession.DrmSessionException(new UnsupportedDrmException(1), 6001));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {

        /* renamed from: a  reason: collision with root package name */
        public static final b f9625a = m4.j.b;

        /* JADX INFO: Access modifiers changed from: private */
        static /* synthetic */ void a() {
        }

        void release();
    }

    static {
        a aVar = new a();
        f9623a = aVar;
        f9624b = aVar;
    }

    default void a() {
    }

    void b(Looper looper, t1 t1Var);

    int c(w0 w0Var);

    DrmSession d(h.a aVar, w0 w0Var);

    default b e(h.a aVar, w0 w0Var) {
        return b.f9625a;
    }

    default void release() {
    }
}
