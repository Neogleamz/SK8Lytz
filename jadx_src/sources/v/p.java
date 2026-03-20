package v;

import android.util.Size;
import java.util.ArrayList;
import java.util.Comparator;
import u.b0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class p {

    /* renamed from: b  reason: collision with root package name */
    private static final Size f23132b = new Size(320, 240);

    /* renamed from: c  reason: collision with root package name */
    private static final Comparator<Size> f23133c = new androidx.camera.core.impl.utils.d();

    /* renamed from: a  reason: collision with root package name */
    private final b0 f23134a = (b0) u.l.a(b0.class);

    public Size[] a(Size[] sizeArr) {
        if (this.f23134a == null || !b0.a()) {
            return sizeArr;
        }
        ArrayList arrayList = new ArrayList();
        for (Size size : sizeArr) {
            if (f23133c.compare(size, f23132b) >= 0) {
                arrayList.add(size);
            }
        }
        return (Size[]) arrayList.toArray(new Size[0]);
    }
}
