package n6;

import android.content.Context;
import android.util.SparseIntArray;
import com.google.android.gms.common.api.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y {

    /* renamed from: a  reason: collision with root package name */
    private final SparseIntArray f22210a = new SparseIntArray();

    /* renamed from: b  reason: collision with root package name */
    private com.google.android.gms.common.b f22211b;

    public y(com.google.android.gms.common.b bVar) {
        j.l(bVar);
        this.f22211b = bVar;
    }

    public final int a(Context context, int i8) {
        return this.f22210a.get(i8, -1);
    }

    public final int b(Context context, a.f fVar) {
        j.l(context);
        j.l(fVar);
        int i8 = 0;
        if (fVar.i()) {
            int j8 = fVar.j();
            int a9 = a(context, j8);
            if (a9 == -1) {
                int i9 = 0;
                while (true) {
                    if (i9 >= this.f22210a.size()) {
                        i8 = -1;
                        break;
                    }
                    int keyAt = this.f22210a.keyAt(i9);
                    if (keyAt > j8 && this.f22210a.get(keyAt) == 0) {
                        break;
                    }
                    i9++;
                }
                a9 = i8 == -1 ? this.f22211b.h(context, j8) : i8;
                this.f22210a.put(j8, a9);
            }
            return a9;
        }
        return 0;
    }

    public final void c() {
        this.f22210a.clear();
    }
}
