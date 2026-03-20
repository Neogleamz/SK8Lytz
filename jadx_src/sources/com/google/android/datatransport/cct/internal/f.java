package com.google.android.datatransport.cct.internal;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.datatransport.cct.internal.j;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f extends j {

    /* renamed from: a  reason: collision with root package name */
    private final long f9059a;

    /* renamed from: b  reason: collision with root package name */
    private final Integer f9060b;

    /* renamed from: c  reason: collision with root package name */
    private final long f9061c;

    /* renamed from: d  reason: collision with root package name */
    private final byte[] f9062d;

    /* renamed from: e  reason: collision with root package name */
    private final String f9063e;

    /* renamed from: f  reason: collision with root package name */
    private final long f9064f;

    /* renamed from: g  reason: collision with root package name */
    private final NetworkConnectionInfo f9065g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b extends j.a {

        /* renamed from: a  reason: collision with root package name */
        private Long f9066a;

        /* renamed from: b  reason: collision with root package name */
        private Integer f9067b;

        /* renamed from: c  reason: collision with root package name */
        private Long f9068c;

        /* renamed from: d  reason: collision with root package name */
        private byte[] f9069d;

        /* renamed from: e  reason: collision with root package name */
        private String f9070e;

        /* renamed from: f  reason: collision with root package name */
        private Long f9071f;

        /* renamed from: g  reason: collision with root package name */
        private NetworkConnectionInfo f9072g;

        @Override // com.google.android.datatransport.cct.internal.j.a
        public j a() {
            Long l8 = this.f9066a;
            String str = BuildConfig.FLAVOR;
            if (l8 == null) {
                str = BuildConfig.FLAVOR + " eventTimeMs";
            }
            if (this.f9068c == null) {
                str = str + " eventUptimeMs";
            }
            if (this.f9071f == null) {
                str = str + " timezoneOffsetSeconds";
            }
            if (str.isEmpty()) {
                return new f(this.f9066a.longValue(), this.f9067b, this.f9068c.longValue(), this.f9069d, this.f9070e, this.f9071f.longValue(), this.f9072g);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override // com.google.android.datatransport.cct.internal.j.a
        public j.a b(Integer num) {
            this.f9067b = num;
            return this;
        }

        @Override // com.google.android.datatransport.cct.internal.j.a
        public j.a c(long j8) {
            this.f9066a = Long.valueOf(j8);
            return this;
        }

        @Override // com.google.android.datatransport.cct.internal.j.a
        public j.a d(long j8) {
            this.f9068c = Long.valueOf(j8);
            return this;
        }

        @Override // com.google.android.datatransport.cct.internal.j.a
        public j.a e(NetworkConnectionInfo networkConnectionInfo) {
            this.f9072g = networkConnectionInfo;
            return this;
        }

        @Override // com.google.android.datatransport.cct.internal.j.a
        j.a f(byte[] bArr) {
            this.f9069d = bArr;
            return this;
        }

        @Override // com.google.android.datatransport.cct.internal.j.a
        j.a g(String str) {
            this.f9070e = str;
            return this;
        }

        @Override // com.google.android.datatransport.cct.internal.j.a
        public j.a h(long j8) {
            this.f9071f = Long.valueOf(j8);
            return this;
        }
    }

    private f(long j8, Integer num, long j9, byte[] bArr, String str, long j10, NetworkConnectionInfo networkConnectionInfo) {
        this.f9059a = j8;
        this.f9060b = num;
        this.f9061c = j9;
        this.f9062d = bArr;
        this.f9063e = str;
        this.f9064f = j10;
        this.f9065g = networkConnectionInfo;
    }

    @Override // com.google.android.datatransport.cct.internal.j
    public Integer b() {
        return this.f9060b;
    }

    @Override // com.google.android.datatransport.cct.internal.j
    public long c() {
        return this.f9059a;
    }

    @Override // com.google.android.datatransport.cct.internal.j
    public long d() {
        return this.f9061c;
    }

    @Override // com.google.android.datatransport.cct.internal.j
    public NetworkConnectionInfo e() {
        return this.f9065g;
    }

    public boolean equals(Object obj) {
        Integer num;
        String str;
        if (obj == this) {
            return true;
        }
        if (obj instanceof j) {
            j jVar = (j) obj;
            if (this.f9059a == jVar.c() && ((num = this.f9060b) != null ? num.equals(jVar.b()) : jVar.b() == null) && this.f9061c == jVar.d()) {
                if (Arrays.equals(this.f9062d, jVar instanceof f ? ((f) jVar).f9062d : jVar.f()) && ((str = this.f9063e) != null ? str.equals(jVar.g()) : jVar.g() == null) && this.f9064f == jVar.h()) {
                    NetworkConnectionInfo networkConnectionInfo = this.f9065g;
                    NetworkConnectionInfo e8 = jVar.e();
                    if (networkConnectionInfo == null) {
                        if (e8 == null) {
                            return true;
                        }
                    } else if (networkConnectionInfo.equals(e8)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    @Override // com.google.android.datatransport.cct.internal.j
    public byte[] f() {
        return this.f9062d;
    }

    @Override // com.google.android.datatransport.cct.internal.j
    public String g() {
        return this.f9063e;
    }

    @Override // com.google.android.datatransport.cct.internal.j
    public long h() {
        return this.f9064f;
    }

    public int hashCode() {
        long j8 = this.f9059a;
        int i8 = (((int) (j8 ^ (j8 >>> 32))) ^ 1000003) * 1000003;
        Integer num = this.f9060b;
        int hashCode = num == null ? 0 : num.hashCode();
        long j9 = this.f9061c;
        int hashCode2 = (((((i8 ^ hashCode) * 1000003) ^ ((int) (j9 ^ (j9 >>> 32)))) * 1000003) ^ Arrays.hashCode(this.f9062d)) * 1000003;
        String str = this.f9063e;
        int hashCode3 = str == null ? 0 : str.hashCode();
        long j10 = this.f9064f;
        int i9 = (((hashCode2 ^ hashCode3) * 1000003) ^ ((int) ((j10 >>> 32) ^ j10))) * 1000003;
        NetworkConnectionInfo networkConnectionInfo = this.f9065g;
        return i9 ^ (networkConnectionInfo != null ? networkConnectionInfo.hashCode() : 0);
    }

    public String toString() {
        return "LogEvent{eventTimeMs=" + this.f9059a + ", eventCode=" + this.f9060b + ", eventUptimeMs=" + this.f9061c + ", sourceExtension=" + Arrays.toString(this.f9062d) + ", sourceExtensionJsonProto3=" + this.f9063e + ", timezoneOffsetSeconds=" + this.f9064f + ", networkConnectionInfo=" + this.f9065g + "}";
    }
}
