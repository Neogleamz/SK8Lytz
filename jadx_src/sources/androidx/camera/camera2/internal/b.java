package androidx.camera.camera2.internal;

import android.util.Size;
import androidx.camera.camera2.internal.g0;
import androidx.camera.core.impl.SessionConfig;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b extends g0.h {

    /* renamed from: a  reason: collision with root package name */
    private final String f1723a;

    /* renamed from: b  reason: collision with root package name */
    private final Class<?> f1724b;

    /* renamed from: c  reason: collision with root package name */
    private final SessionConfig f1725c;

    /* renamed from: d  reason: collision with root package name */
    private final androidx.camera.core.impl.v<?> f1726d;

    /* renamed from: e  reason: collision with root package name */
    private final Size f1727e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(String str, Class<?> cls, SessionConfig sessionConfig, androidx.camera.core.impl.v<?> vVar, Size size) {
        Objects.requireNonNull(str, "Null useCaseId");
        this.f1723a = str;
        Objects.requireNonNull(cls, "Null useCaseType");
        this.f1724b = cls;
        Objects.requireNonNull(sessionConfig, "Null sessionConfig");
        this.f1725c = sessionConfig;
        Objects.requireNonNull(vVar, "Null useCaseConfig");
        this.f1726d = vVar;
        this.f1727e = size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.camera2.internal.g0.h
    public SessionConfig c() {
        return this.f1725c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.camera2.internal.g0.h
    public Size d() {
        return this.f1727e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.camera2.internal.g0.h
    public androidx.camera.core.impl.v<?> e() {
        return this.f1726d;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof g0.h) {
            g0.h hVar = (g0.h) obj;
            if (this.f1723a.equals(hVar.f()) && this.f1724b.equals(hVar.g()) && this.f1725c.equals(hVar.c()) && this.f1726d.equals(hVar.e())) {
                Size size = this.f1727e;
                Size d8 = hVar.d();
                if (size == null) {
                    if (d8 == null) {
                        return true;
                    }
                } else if (size.equals(d8)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.camera2.internal.g0.h
    public String f() {
        return this.f1723a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.camera2.internal.g0.h
    public Class<?> g() {
        return this.f1724b;
    }

    public int hashCode() {
        int hashCode = (((((((this.f1723a.hashCode() ^ 1000003) * 1000003) ^ this.f1724b.hashCode()) * 1000003) ^ this.f1725c.hashCode()) * 1000003) ^ this.f1726d.hashCode()) * 1000003;
        Size size = this.f1727e;
        return hashCode ^ (size == null ? 0 : size.hashCode());
    }

    public String toString() {
        return "UseCaseInfo{useCaseId=" + this.f1723a + ", useCaseType=" + this.f1724b + ", sessionConfig=" + this.f1725c + ", useCaseConfig=" + this.f1726d + ", surfaceResolution=" + this.f1727e + "}";
    }
}
