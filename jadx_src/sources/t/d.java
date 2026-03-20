package t;

import android.hardware.camera2.params.OutputConfiguration;
import android.view.Surface;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d extends c {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        final OutputConfiguration f22816a;

        /* renamed from: b  reason: collision with root package name */
        String f22817b;

        a(OutputConfiguration outputConfiguration) {
            this.f22816a = outputConfiguration;
        }

        public boolean equals(Object obj) {
            if (obj instanceof a) {
                a aVar = (a) obj;
                return Objects.equals(this.f22816a, aVar.f22816a) && Objects.equals(this.f22817b, aVar.f22817b);
            }
            return false;
        }

        public int hashCode() {
            int hashCode = this.f22816a.hashCode() ^ 31;
            int i8 = (hashCode << 5) - hashCode;
            String str = this.f22817b;
            return (str == null ? 0 : str.hashCode()) ^ i8;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(int i8, Surface surface) {
        this(new a(new OutputConfiguration(i8, surface)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(Object obj) {
        super(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static d i(OutputConfiguration outputConfiguration) {
        return new d(new a(outputConfiguration));
    }

    @Override // t.g, t.b.a
    public void b(Surface surface) {
        ((OutputConfiguration) f()).addSurface(surface);
    }

    @Override // t.c, t.g, t.b.a
    public String c() {
        return ((a) this.f22818a).f22817b;
    }

    @Override // t.c, t.g, t.b.a
    public void d() {
        ((OutputConfiguration) f()).enableSurfaceSharing();
    }

    @Override // t.c, t.g, t.b.a
    public void e(String str) {
        ((a) this.f22818a).f22817b = str;
    }

    @Override // t.c, t.g, t.b.a
    public Object f() {
        androidx.core.util.h.a(this.f22818a instanceof a);
        return ((a) this.f22818a).f22816a;
    }

    @Override // t.c, t.g
    final boolean g() {
        throw new AssertionError("isSurfaceSharingEnabled() should not be called on API >= 26");
    }
}
