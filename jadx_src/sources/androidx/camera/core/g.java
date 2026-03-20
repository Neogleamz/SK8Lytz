package androidx.camera.core;

import androidx.camera.core.CameraState;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g extends CameraState.a {

    /* renamed from: a  reason: collision with root package name */
    private final int f2387a;

    /* renamed from: b  reason: collision with root package name */
    private final Throwable f2388b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(int i8, Throwable th) {
        this.f2387a = i8;
        this.f2388b = th;
    }

    @Override // androidx.camera.core.CameraState.a
    public Throwable c() {
        return this.f2388b;
    }

    @Override // androidx.camera.core.CameraState.a
    public int d() {
        return this.f2387a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof CameraState.a) {
            CameraState.a aVar = (CameraState.a) obj;
            if (this.f2387a == aVar.d()) {
                Throwable th = this.f2388b;
                Throwable c9 = aVar.c();
                if (th == null) {
                    if (c9 == null) {
                        return true;
                    }
                } else if (th.equals(c9)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int i8 = (this.f2387a ^ 1000003) * 1000003;
        Throwable th = this.f2388b;
        return i8 ^ (th == null ? 0 : th.hashCode());
    }

    public String toString() {
        return "StateError{code=" + this.f2387a + ", cause=" + this.f2388b + "}";
    }
}
