package l5;

import android.net.Uri;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import l5.k;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class j {

    /* renamed from: a  reason: collision with root package name */
    public final long f21683a;

    /* renamed from: b  reason: collision with root package name */
    public final w0 f21684b;

    /* renamed from: c  reason: collision with root package name */
    public final ImmutableList<l5.b> f21685c;

    /* renamed from: d  reason: collision with root package name */
    public final long f21686d;

    /* renamed from: e  reason: collision with root package name */
    public final List<e> f21687e;

    /* renamed from: f  reason: collision with root package name */
    public final List<e> f21688f;

    /* renamed from: g  reason: collision with root package name */
    public final List<e> f21689g;

    /* renamed from: h  reason: collision with root package name */
    private final i f21690h;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends j implements k5.e {

        /* renamed from: i  reason: collision with root package name */
        final k.a f21691i;

        public b(long j8, w0 w0Var, List<l5.b> list, k.a aVar, List<e> list2, List<e> list3, List<e> list4) {
            super(j8, w0Var, list, aVar, list2, list3, list4);
            this.f21691i = aVar;
        }

        @Override // k5.e
        public long a(long j8) {
            return this.f21691i.j(j8);
        }

        @Override // k5.e
        public long b(long j8, long j9) {
            return this.f21691i.h(j8, j9);
        }

        @Override // k5.e
        public long c(long j8, long j9) {
            return this.f21691i.d(j8, j9);
        }

        @Override // k5.e
        public long d(long j8, long j9) {
            return this.f21691i.f(j8, j9);
        }

        @Override // k5.e
        public i e(long j8) {
            return this.f21691i.k(this, j8);
        }

        @Override // k5.e
        public long f(long j8, long j9) {
            return this.f21691i.i(j8, j9);
        }

        @Override // k5.e
        public boolean g() {
            return this.f21691i.l();
        }

        @Override // k5.e
        public long h() {
            return this.f21691i.e();
        }

        @Override // k5.e
        public long i(long j8) {
            return this.f21691i.g(j8);
        }

        @Override // k5.e
        public long j(long j8, long j9) {
            return this.f21691i.c(j8, j9);
        }

        @Override // l5.j
        public String k() {
            return null;
        }

        @Override // l5.j
        public k5.e l() {
            return this;
        }

        @Override // l5.j
        public i m() {
            return null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c extends j {

        /* renamed from: i  reason: collision with root package name */
        public final Uri f21692i;

        /* renamed from: j  reason: collision with root package name */
        public final long f21693j;

        /* renamed from: k  reason: collision with root package name */
        private final String f21694k;

        /* renamed from: l  reason: collision with root package name */
        private final i f21695l;

        /* renamed from: m  reason: collision with root package name */
        private final m f21696m;

        public c(long j8, w0 w0Var, List<l5.b> list, k.e eVar, List<e> list2, List<e> list3, List<e> list4, String str, long j9) {
            super(j8, w0Var, list, eVar, list2, list3, list4);
            this.f21692i = Uri.parse(list.get(0).f21630a);
            i c9 = eVar.c();
            this.f21695l = c9;
            this.f21694k = str;
            this.f21693j = j9;
            this.f21696m = c9 != null ? null : new m(new i(null, 0L, j9));
        }

        @Override // l5.j
        public String k() {
            return this.f21694k;
        }

        @Override // l5.j
        public k5.e l() {
            return this.f21696m;
        }

        @Override // l5.j
        public i m() {
            return this.f21695l;
        }
    }

    private j(long j8, w0 w0Var, List<l5.b> list, k kVar, List<e> list2, List<e> list3, List<e> list4) {
        b6.a.a(!list.isEmpty());
        this.f21683a = j8;
        this.f21684b = w0Var;
        this.f21685c = ImmutableList.x(list);
        this.f21687e = list2 == null ? Collections.emptyList() : Collections.unmodifiableList(list2);
        this.f21688f = list3;
        this.f21689g = list4;
        this.f21690h = kVar.a(this);
        this.f21686d = kVar.b();
    }

    public static j o(long j8, w0 w0Var, List<l5.b> list, k kVar, List<e> list2, List<e> list3, List<e> list4, String str) {
        if (kVar instanceof k.e) {
            return new c(j8, w0Var, list, (k.e) kVar, list2, list3, list4, str, -1L);
        }
        if (kVar instanceof k.a) {
            return new b(j8, w0Var, list, (k.a) kVar, list2, list3, list4);
        }
        throw new IllegalArgumentException("segmentBase must be of type SingleSegmentBase or MultiSegmentBase");
    }

    public abstract String k();

    public abstract k5.e l();

    public abstract i m();

    public i n() {
        return this.f21690h;
    }
}
