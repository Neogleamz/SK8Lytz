package p2;

import android.util.SparseIntArray;
import android.view.ViewGroup;
import java.util.List;
import p2.d;
import r2.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a<T extends r2.a, K extends d> extends b<T, K> {
    private SparseIntArray K;

    public a(List<T> list) {
        super(list);
    }

    private int q0(int i8) {
        return this.K.get(i8, -404);
    }

    @Override // p2.b
    protected int O(int i8) {
        r2.a aVar = (r2.a) this.A.get(i8);
        if (aVar != null) {
            return aVar.a();
        }
        return -255;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // p2.b
    public K i0(ViewGroup viewGroup, int i8) {
        return M(viewGroup, q0(i8));
    }

    protected void p0(int i8, int i9) {
        if (this.K == null) {
            this.K = new SparseIntArray();
        }
        this.K.put(i8, i9);
    }
}
