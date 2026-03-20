package androidx.camera.core.impl.utils;

import android.util.Size;
import java.util.Comparator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d implements Comparator<Size> {

    /* renamed from: a  reason: collision with root package name */
    private boolean f2625a;

    public d() {
        this(false);
    }

    public d(boolean z4) {
        this.f2625a = false;
        this.f2625a = z4;
    }

    @Override // java.util.Comparator
    /* renamed from: a */
    public int compare(Size size, Size size2) {
        int signum = Long.signum((size.getWidth() * size.getHeight()) - (size2.getWidth() * size2.getHeight()));
        return this.f2625a ? signum * (-1) : signum;
    }
}
