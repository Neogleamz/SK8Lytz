package v2;

import hk.g;
import java.util.List;
import kotlin.jvm.internal.p;
@g
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* renamed from: a  reason: collision with root package name */
    private final String f23163a;

    /* renamed from: b  reason: collision with root package name */
    private final List<a> f23164b;

    @g
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final String f23165a;

        /* renamed from: b  reason: collision with root package name */
        private final Integer f23166b;

        /* renamed from: c  reason: collision with root package name */
        private final String f23167c;

        public final String a() {
            return this.f23165a;
        }

        public final Integer b() {
            return this.f23166b;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof a) {
                a aVar = (a) obj;
                return p.a(this.f23165a, aVar.f23165a) && p.a(this.f23166b, aVar.f23166b) && p.a(this.f23167c, aVar.f23167c);
            }
            return false;
        }

        public int hashCode() {
            int hashCode = this.f23165a.hashCode() * 31;
            Integer num = this.f23166b;
            int hashCode2 = (hashCode + (num == null ? 0 : num.hashCode())) * 31;
            String str = this.f23167c;
            return hashCode2 + (str != null ? str.hashCode() : 0);
        }

        public String toString() {
            return "Asset(asset=" + this.f23165a + ", weight=" + this.f23166b + ", style=" + this.f23167c + ')';
        }
    }

    public final String a() {
        return this.f23163a;
    }

    public final List<a> b() {
        return this.f23164b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof c) {
            c cVar = (c) obj;
            return p.a(this.f23163a, cVar.f23163a) && p.a(this.f23164b, cVar.f23164b);
        }
        return false;
    }

    public int hashCode() {
        return (this.f23163a.hashCode() * 31) + this.f23164b.hashCode();
    }

    public String toString() {
        return "FontManifestEntry(family=" + this.f23163a + ", fonts=" + this.f23164b + ')';
    }
}
