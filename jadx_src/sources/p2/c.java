package p2;

import android.view.ViewGroup;
import com.chad.library.adapter.base.entity.SectionEntity;
import java.util.List;
import p2.d;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class c<T extends SectionEntity, K extends d> extends b<T, K> {
    protected int K;

    public c(int i8, int i9, List<T> list) {
        super(i8, list);
        this.K = i9;
    }

    @Override // p2.b
    protected int O(int i8) {
        return ((SectionEntity) this.A.get(i8)).f8834a ? 1092 : 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // p2.b
    public boolean b0(int i8) {
        return super.b0(i8) || i8 == 1092;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // p2.b, androidx.recyclerview.widget.RecyclerView.g
    /* renamed from: h0 */
    public void r(K k8, int i8) {
        if (k8.l() != 1092) {
            super.r(k8, i8);
            return;
        }
        l0(k8);
        p0(k8, (SectionEntity) T(i8 - R()));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // p2.b
    public K i0(ViewGroup viewGroup, int i8) {
        return i8 == 1092 ? L(U(this.K, viewGroup)) : (K) super.i0(viewGroup, i8);
    }

    protected abstract void p0(K k8, T t8);
}
