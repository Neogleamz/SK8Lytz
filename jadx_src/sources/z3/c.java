package z3;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.datatransport.runtime.firebase.transport.LogEventDropped;
import com.google.firebase.encoders.proto.Protobuf;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* renamed from: c  reason: collision with root package name */
    private static final c f24562c = new a().a();

    /* renamed from: a  reason: collision with root package name */
    private final String f24563a;

    /* renamed from: b  reason: collision with root package name */
    private final List<LogEventDropped> f24564b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private String f24565a = BuildConfig.FLAVOR;

        /* renamed from: b  reason: collision with root package name */
        private List<LogEventDropped> f24566b = new ArrayList();

        a() {
        }

        public c a() {
            return new c(this.f24565a, Collections.unmodifiableList(this.f24566b));
        }

        public a b(List<LogEventDropped> list) {
            this.f24566b = list;
            return this;
        }

        public a c(String str) {
            this.f24565a = str;
            return this;
        }
    }

    c(String str, List<LogEventDropped> list) {
        this.f24563a = str;
        this.f24564b = list;
    }

    public static a c() {
        return new a();
    }

    @Protobuf(tag = 2)
    public List<LogEventDropped> a() {
        return this.f24564b;
    }

    @Protobuf(tag = 1)
    public String b() {
        return this.f24563a;
    }
}
