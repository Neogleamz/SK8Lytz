package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.DrmInitData;
import j4.t1;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface m {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final byte[] f9627a;

        /* renamed from: b  reason: collision with root package name */
        private final String f9628b;

        /* renamed from: c  reason: collision with root package name */
        private final int f9629c;

        public a(byte[] bArr, String str, int i8) {
            this.f9627a = bArr;
            this.f9628b = str;
            this.f9629c = i8;
        }

        public byte[] a() {
            return this.f9627a;
        }

        public String b() {
            return this.f9628b;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a(m mVar, byte[] bArr, int i8, int i9, byte[] bArr2);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        m a(UUID uuid);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        private final byte[] f9630a;

        /* renamed from: b  reason: collision with root package name */
        private final String f9631b;

        public d(byte[] bArr, String str) {
            this.f9630a = bArr;
            this.f9631b = str;
        }

        public byte[] a() {
            return this.f9630a;
        }

        public String b() {
            return this.f9631b;
        }
    }

    Map<String, String> a(byte[] bArr);

    d b();

    l4.b c(byte[] bArr);

    byte[] d();

    boolean e(byte[] bArr, String str);

    void f(byte[] bArr, byte[] bArr2);

    void g(byte[] bArr);

    void h(b bVar);

    byte[] i(byte[] bArr, byte[] bArr2);

    void j(byte[] bArr);

    a k(byte[] bArr, List<DrmInitData.SchemeData> list, int i8, HashMap<String, String> hashMap);

    int l();

    default void m(byte[] bArr, t1 t1Var) {
    }

    void release();
}
