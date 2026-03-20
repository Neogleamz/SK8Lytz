package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import i4.q;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    public final Uri f10942a;

    /* renamed from: b  reason: collision with root package name */
    public final long f10943b;

    /* renamed from: c  reason: collision with root package name */
    public final int f10944c;

    /* renamed from: d  reason: collision with root package name */
    public final byte[] f10945d;

    /* renamed from: e  reason: collision with root package name */
    public final Map<String, String> f10946e;
    @Deprecated

    /* renamed from: f  reason: collision with root package name */
    public final long f10947f;

    /* renamed from: g  reason: collision with root package name */
    public final long f10948g;

    /* renamed from: h  reason: collision with root package name */
    public final long f10949h;

    /* renamed from: i  reason: collision with root package name */
    public final String f10950i;

    /* renamed from: j  reason: collision with root package name */
    public final int f10951j;

    /* renamed from: k  reason: collision with root package name */
    public final Object f10952k;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private Uri f10953a;

        /* renamed from: b  reason: collision with root package name */
        private long f10954b;

        /* renamed from: c  reason: collision with root package name */
        private int f10955c;

        /* renamed from: d  reason: collision with root package name */
        private byte[] f10956d;

        /* renamed from: e  reason: collision with root package name */
        private Map<String, String> f10957e;

        /* renamed from: f  reason: collision with root package name */
        private long f10958f;

        /* renamed from: g  reason: collision with root package name */
        private long f10959g;

        /* renamed from: h  reason: collision with root package name */
        private String f10960h;

        /* renamed from: i  reason: collision with root package name */
        private int f10961i;

        /* renamed from: j  reason: collision with root package name */
        private Object f10962j;

        public b() {
            this.f10955c = 1;
            this.f10957e = Collections.emptyMap();
            this.f10959g = -1L;
        }

        private b(a aVar) {
            this.f10953a = aVar.f10942a;
            this.f10954b = aVar.f10943b;
            this.f10955c = aVar.f10944c;
            this.f10956d = aVar.f10945d;
            this.f10957e = aVar.f10946e;
            this.f10958f = aVar.f10948g;
            this.f10959g = aVar.f10949h;
            this.f10960h = aVar.f10950i;
            this.f10961i = aVar.f10951j;
            this.f10962j = aVar.f10952k;
        }

        public a a() {
            b6.a.i(this.f10953a, "The uri must be set.");
            return new a(this.f10953a, this.f10954b, this.f10955c, this.f10956d, this.f10957e, this.f10958f, this.f10959g, this.f10960h, this.f10961i, this.f10962j);
        }

        public b b(int i8) {
            this.f10961i = i8;
            return this;
        }

        public b c(byte[] bArr) {
            this.f10956d = bArr;
            return this;
        }

        public b d(int i8) {
            this.f10955c = i8;
            return this;
        }

        public b e(Map<String, String> map) {
            this.f10957e = map;
            return this;
        }

        public b f(String str) {
            this.f10960h = str;
            return this;
        }

        public b g(long j8) {
            this.f10959g = j8;
            return this;
        }

        public b h(long j8) {
            this.f10958f = j8;
            return this;
        }

        public b i(Uri uri) {
            this.f10953a = uri;
            return this;
        }

        public b j(String str) {
            this.f10953a = Uri.parse(str);
            return this;
        }
    }

    static {
        q.a("goog.exo.datasource");
    }

    public a(Uri uri) {
        this(uri, 0L, -1L);
    }

    private a(Uri uri, long j8, int i8, byte[] bArr, Map<String, String> map, long j9, long j10, String str, int i9, Object obj) {
        byte[] bArr2 = bArr;
        long j11 = j8 + j9;
        boolean z4 = true;
        b6.a.a(j11 >= 0);
        b6.a.a(j9 >= 0);
        if (j10 <= 0 && j10 != -1) {
            z4 = false;
        }
        b6.a.a(z4);
        this.f10942a = uri;
        this.f10943b = j8;
        this.f10944c = i8;
        this.f10945d = (bArr2 == null || bArr2.length == 0) ? null : bArr2;
        this.f10946e = Collections.unmodifiableMap(new HashMap(map));
        this.f10948g = j9;
        this.f10947f = j11;
        this.f10949h = j10;
        this.f10950i = str;
        this.f10951j = i9;
        this.f10952k = obj;
    }

    public a(Uri uri, long j8, long j9) {
        this(uri, 0L, 1, null, Collections.emptyMap(), j8, j9, null, 0, null);
    }

    public static String c(int i8) {
        if (i8 != 1) {
            if (i8 != 2) {
                if (i8 == 3) {
                    return "HEAD";
                }
                throw new IllegalStateException();
            }
            return "POST";
        }
        return "GET";
    }

    public b a() {
        return new b();
    }

    public final String b() {
        return c(this.f10944c);
    }

    public boolean d(int i8) {
        return (this.f10951j & i8) == i8;
    }

    public a e(long j8) {
        long j9 = this.f10949h;
        return f(j8, j9 != -1 ? j9 - j8 : -1L);
    }

    public a f(long j8, long j9) {
        return (j8 == 0 && this.f10949h == j9) ? this : new a(this.f10942a, this.f10943b, this.f10944c, this.f10945d, this.f10946e, this.f10948g + j8, j9, this.f10950i, this.f10951j, this.f10952k);
    }

    public String toString() {
        return "DataSpec[" + b() + " " + this.f10942a + ", " + this.f10948g + ", " + this.f10949h + ", " + this.f10950i + ", " + this.f10951j + "]";
    }
}
