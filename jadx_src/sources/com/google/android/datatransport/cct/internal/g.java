package com.google.android.datatransport.cct.internal;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.datatransport.cct.internal.k;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g extends k {

    /* renamed from: a  reason: collision with root package name */
    private final long f9073a;

    /* renamed from: b  reason: collision with root package name */
    private final long f9074b;

    /* renamed from: c  reason: collision with root package name */
    private final ClientInfo f9075c;

    /* renamed from: d  reason: collision with root package name */
    private final Integer f9076d;

    /* renamed from: e  reason: collision with root package name */
    private final String f9077e;

    /* renamed from: f  reason: collision with root package name */
    private final List<j> f9078f;

    /* renamed from: g  reason: collision with root package name */
    private final QosTier f9079g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends k.a {

        /* renamed from: a  reason: collision with root package name */
        private Long f9080a;

        /* renamed from: b  reason: collision with root package name */
        private Long f9081b;

        /* renamed from: c  reason: collision with root package name */
        private ClientInfo f9082c;

        /* renamed from: d  reason: collision with root package name */
        private Integer f9083d;

        /* renamed from: e  reason: collision with root package name */
        private String f9084e;

        /* renamed from: f  reason: collision with root package name */
        private List<j> f9085f;

        /* renamed from: g  reason: collision with root package name */
        private QosTier f9086g;

        @Override // com.google.android.datatransport.cct.internal.k.a
        public k a() {
            Long l8 = this.f9080a;
            String str = BuildConfig.FLAVOR;
            if (l8 == null) {
                str = BuildConfig.FLAVOR + " requestTimeMs";
            }
            if (this.f9081b == null) {
                str = str + " requestUptimeMs";
            }
            if (str.isEmpty()) {
                return new g(this.f9080a.longValue(), this.f9081b.longValue(), this.f9082c, this.f9083d, this.f9084e, this.f9085f, this.f9086g);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override // com.google.android.datatransport.cct.internal.k.a
        public k.a b(ClientInfo clientInfo) {
            this.f9082c = clientInfo;
            return this;
        }

        @Override // com.google.android.datatransport.cct.internal.k.a
        public k.a c(List<j> list) {
            this.f9085f = list;
            return this;
        }

        @Override // com.google.android.datatransport.cct.internal.k.a
        k.a d(Integer num) {
            this.f9083d = num;
            return this;
        }

        @Override // com.google.android.datatransport.cct.internal.k.a
        k.a e(String str) {
            this.f9084e = str;
            return this;
        }

        @Override // com.google.android.datatransport.cct.internal.k.a
        public k.a f(QosTier qosTier) {
            this.f9086g = qosTier;
            return this;
        }

        @Override // com.google.android.datatransport.cct.internal.k.a
        public k.a g(long j8) {
            this.f9080a = Long.valueOf(j8);
            return this;
        }

        @Override // com.google.android.datatransport.cct.internal.k.a
        public k.a h(long j8) {
            this.f9081b = Long.valueOf(j8);
            return this;
        }
    }

    private g(long j8, long j9, ClientInfo clientInfo, Integer num, String str, List<j> list, QosTier qosTier) {
        this.f9073a = j8;
        this.f9074b = j9;
        this.f9075c = clientInfo;
        this.f9076d = num;
        this.f9077e = str;
        this.f9078f = list;
        this.f9079g = qosTier;
    }

    @Override // com.google.android.datatransport.cct.internal.k
    public ClientInfo b() {
        return this.f9075c;
    }

    @Override // com.google.android.datatransport.cct.internal.k
    public List<j> c() {
        return this.f9078f;
    }

    @Override // com.google.android.datatransport.cct.internal.k
    public Integer d() {
        return this.f9076d;
    }

    @Override // com.google.android.datatransport.cct.internal.k
    public String e() {
        return this.f9077e;
    }

    public boolean equals(Object obj) {
        ClientInfo clientInfo;
        Integer num;
        String str;
        List<j> list;
        if (obj == this) {
            return true;
        }
        if (obj instanceof k) {
            k kVar = (k) obj;
            if (this.f9073a == kVar.g() && this.f9074b == kVar.h() && ((clientInfo = this.f9075c) != null ? clientInfo.equals(kVar.b()) : kVar.b() == null) && ((num = this.f9076d) != null ? num.equals(kVar.d()) : kVar.d() == null) && ((str = this.f9077e) != null ? str.equals(kVar.e()) : kVar.e() == null) && ((list = this.f9078f) != null ? list.equals(kVar.c()) : kVar.c() == null)) {
                QosTier qosTier = this.f9079g;
                QosTier f5 = kVar.f();
                if (qosTier == null) {
                    if (f5 == null) {
                        return true;
                    }
                } else if (qosTier.equals(f5)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // com.google.android.datatransport.cct.internal.k
    public QosTier f() {
        return this.f9079g;
    }

    @Override // com.google.android.datatransport.cct.internal.k
    public long g() {
        return this.f9073a;
    }

    @Override // com.google.android.datatransport.cct.internal.k
    public long h() {
        return this.f9074b;
    }

    public int hashCode() {
        long j8 = this.f9073a;
        long j9 = this.f9074b;
        int i8 = (((((int) (j8 ^ (j8 >>> 32))) ^ 1000003) * 1000003) ^ ((int) ((j9 >>> 32) ^ j9))) * 1000003;
        ClientInfo clientInfo = this.f9075c;
        int hashCode = (i8 ^ (clientInfo == null ? 0 : clientInfo.hashCode())) * 1000003;
        Integer num = this.f9076d;
        int hashCode2 = (hashCode ^ (num == null ? 0 : num.hashCode())) * 1000003;
        String str = this.f9077e;
        int hashCode3 = (hashCode2 ^ (str == null ? 0 : str.hashCode())) * 1000003;
        List<j> list = this.f9078f;
        int hashCode4 = (hashCode3 ^ (list == null ? 0 : list.hashCode())) * 1000003;
        QosTier qosTier = this.f9079g;
        return hashCode4 ^ (qosTier != null ? qosTier.hashCode() : 0);
    }

    public String toString() {
        return "LogRequest{requestTimeMs=" + this.f9073a + ", requestUptimeMs=" + this.f9074b + ", clientInfo=" + this.f9075c + ", logSource=" + this.f9076d + ", logSourceName=" + this.f9077e + ", logEvents=" + this.f9078f + ", qosTier=" + this.f9079g + "}";
    }
}
