package androidx.camera.core.impl;

import androidx.camera.core.impl.SessionConfig;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.List;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b extends SessionConfig.e {

    /* renamed from: a  reason: collision with root package name */
    private final DeferrableSurface f2532a;

    /* renamed from: b  reason: collision with root package name */
    private final List<DeferrableSurface> f2533b;

    /* renamed from: c  reason: collision with root package name */
    private final String f2534c;

    /* renamed from: d  reason: collision with root package name */
    private final int f2535d;

    /* renamed from: androidx.camera.core.impl.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class C0017b extends SessionConfig.e.a {

        /* renamed from: a  reason: collision with root package name */
        private DeferrableSurface f2536a;

        /* renamed from: b  reason: collision with root package name */
        private List<DeferrableSurface> f2537b;

        /* renamed from: c  reason: collision with root package name */
        private String f2538c;

        /* renamed from: d  reason: collision with root package name */
        private Integer f2539d;

        @Override // androidx.camera.core.impl.SessionConfig.e.a
        public SessionConfig.e a() {
            DeferrableSurface deferrableSurface = this.f2536a;
            String str = BuildConfig.FLAVOR;
            if (deferrableSurface == null) {
                str = BuildConfig.FLAVOR + " surface";
            }
            if (this.f2537b == null) {
                str = str + " sharedSurfaces";
            }
            if (this.f2539d == null) {
                str = str + " surfaceGroupId";
            }
            if (str.isEmpty()) {
                return new b(this.f2536a, this.f2537b, this.f2538c, this.f2539d.intValue());
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override // androidx.camera.core.impl.SessionConfig.e.a
        public SessionConfig.e.a b(String str) {
            this.f2538c = str;
            return this;
        }

        @Override // androidx.camera.core.impl.SessionConfig.e.a
        public SessionConfig.e.a c(List<DeferrableSurface> list) {
            Objects.requireNonNull(list, "Null sharedSurfaces");
            this.f2537b = list;
            return this;
        }

        @Override // androidx.camera.core.impl.SessionConfig.e.a
        public SessionConfig.e.a d(int i8) {
            this.f2539d = Integer.valueOf(i8);
            return this;
        }

        public SessionConfig.e.a e(DeferrableSurface deferrableSurface) {
            Objects.requireNonNull(deferrableSurface, "Null surface");
            this.f2536a = deferrableSurface;
            return this;
        }
    }

    private b(DeferrableSurface deferrableSurface, List<DeferrableSurface> list, String str, int i8) {
        this.f2532a = deferrableSurface;
        this.f2533b = list;
        this.f2534c = str;
        this.f2535d = i8;
    }

    @Override // androidx.camera.core.impl.SessionConfig.e
    public String b() {
        return this.f2534c;
    }

    @Override // androidx.camera.core.impl.SessionConfig.e
    public List<DeferrableSurface> c() {
        return this.f2533b;
    }

    @Override // androidx.camera.core.impl.SessionConfig.e
    public DeferrableSurface d() {
        return this.f2532a;
    }

    @Override // androidx.camera.core.impl.SessionConfig.e
    public int e() {
        return this.f2535d;
    }

    public boolean equals(Object obj) {
        String str;
        if (obj == this) {
            return true;
        }
        if (obj instanceof SessionConfig.e) {
            SessionConfig.e eVar = (SessionConfig.e) obj;
            return this.f2532a.equals(eVar.d()) && this.f2533b.equals(eVar.c()) && ((str = this.f2534c) != null ? str.equals(eVar.b()) : eVar.b() == null) && this.f2535d == eVar.e();
        }
        return false;
    }

    public int hashCode() {
        int hashCode = (((this.f2532a.hashCode() ^ 1000003) * 1000003) ^ this.f2533b.hashCode()) * 1000003;
        String str = this.f2534c;
        return ((hashCode ^ (str == null ? 0 : str.hashCode())) * 1000003) ^ this.f2535d;
    }

    public String toString() {
        return "OutputConfig{surface=" + this.f2532a + ", sharedSurfaces=" + this.f2533b + ", physicalCameraId=" + this.f2534c + ", surfaceGroupId=" + this.f2535d + "}";
    }
}
