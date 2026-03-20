package androidx.transition;

import android.view.View;
import android.view.WindowId;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class n0 implements o0 {

    /* renamed from: a  reason: collision with root package name */
    private final WindowId f7591a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n0(View view) {
        this.f7591a = view.getWindowId();
    }

    public boolean equals(Object obj) {
        return (obj instanceof n0) && ((n0) obj).f7591a.equals(this.f7591a);
    }

    public int hashCode() {
        return this.f7591a.hashCode();
    }
}
