package t;

import android.hardware.camera2.params.OutputConfiguration;
import android.view.Surface;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c extends g {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        final OutputConfiguration f22813a;

        /* renamed from: b  reason: collision with root package name */
        String f22814b;

        /* renamed from: c  reason: collision with root package name */
        boolean f22815c;

        a(OutputConfiguration outputConfiguration) {
            this.f22813a = outputConfiguration;
        }

        public boolean equals(Object obj) {
            if (obj instanceof a) {
                a aVar = (a) obj;
                return Objects.equals(this.f22813a, aVar.f22813a) && this.f22815c == aVar.f22815c && Objects.equals(this.f22814b, aVar.f22814b);
            }
            return false;
        }

        public int hashCode() {
            int hashCode = this.f22813a.hashCode() ^ 31;
            int i8 = (this.f22815c ? 1 : 0) ^ ((hashCode << 5) - hashCode);
            int i9 = (i8 << 5) - i8;
            String str = this.f22814b;
            return (str == null ? 0 : str.hashCode()) ^ i9;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(int i8, Surface surface) {
        this(new a(new OutputConfiguration(i8, surface)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(Object obj) {
        super(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static c h(OutputConfiguration outputConfiguration) {
        return new c(new a(outputConfiguration));
    }

    @Override // t.g, t.b.a
    public String c() {
        return ((a) this.f22818a).f22814b;
    }

    @Override // t.g, t.b.a
    public void d() {
        ((a) this.f22818a).f22815c = true;
    }

    @Override // t.g, t.b.a
    public void e(String str) {
        ((a) this.f22818a).f22814b = str;
    }

    @Override // t.g, t.b.a
    public Object f() {
        androidx.core.util.h.a(this.f22818a instanceof a);
        return ((a) this.f22818a).f22813a;
    }

    @Override // t.g
    boolean g() {
        return ((a) this.f22818a).f22815c;
    }

    @Override // t.g, t.b.a
    public Surface getSurface() {
        return ((OutputConfiguration) f()).getSurface();
    }
}
