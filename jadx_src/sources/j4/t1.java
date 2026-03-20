package j4;

import android.media.metrics.LogSessionId;
import b6.l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t1 {

    /* renamed from: b  reason: collision with root package name */
    public static final t1 f20710b;

    /* renamed from: a  reason: collision with root package name */
    private final a f20711a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a {

        /* renamed from: b  reason: collision with root package name */
        public static final a f20712b = new a(LogSessionId.LOG_SESSION_ID_NONE);

        /* renamed from: a  reason: collision with root package name */
        public final LogSessionId f20713a;

        public a(LogSessionId logSessionId) {
            this.f20713a = logSessionId;
        }
    }

    static {
        f20710b = l0.f8063a < 31 ? new t1() : new t1(a.f20712b);
    }

    public t1() {
        this((a) null);
        b6.a.f(l0.f8063a < 31);
    }

    public t1(LogSessionId logSessionId) {
        this(new a(logSessionId));
    }

    private t1(a aVar) {
        this.f20711a = aVar;
    }

    public LogSessionId a() {
        return ((a) b6.a.e(this.f20711a)).f20713a;
    }
}
