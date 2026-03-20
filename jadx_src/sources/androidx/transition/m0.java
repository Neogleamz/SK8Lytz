package androidx.transition;

import android.os.IBinder;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class m0 implements o0 {

    /* renamed from: a  reason: collision with root package name */
    private final IBinder f7590a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m0(IBinder iBinder) {
        this.f7590a = iBinder;
    }

    public boolean equals(Object obj) {
        return (obj instanceof m0) && ((m0) obj).f7590a.equals(this.f7590a);
    }

    public int hashCode() {
        return this.f7590a.hashCode();
    }
}
