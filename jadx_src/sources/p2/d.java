package p2;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashSet;
import java.util.LinkedHashSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d extends RecyclerView.b0 {

    /* renamed from: t  reason: collision with root package name */
    private final SparseArray<View> f22320t;

    /* renamed from: u  reason: collision with root package name */
    private final HashSet<Integer> f22321u;

    /* renamed from: v  reason: collision with root package name */
    private final LinkedHashSet<Integer> f22322v;

    /* renamed from: w  reason: collision with root package name */
    private final LinkedHashSet<Integer> f22323w;

    /* renamed from: x  reason: collision with root package name */
    private b f22324x;
    @Deprecated

    /* renamed from: y  reason: collision with root package name */
    public View f22325y;

    public d(View view) {
        super(view);
        this.f22320t = new SparseArray<>();
        this.f22322v = new LinkedHashSet<>();
        this.f22323w = new LinkedHashSet<>();
        this.f22321u = new HashSet<>();
        this.f22325y = view;
    }

    public <T extends View> T M(int i8) {
        T t8 = (T) this.f22320t.get(i8);
        if (t8 == null) {
            T t9 = (T) this.f6628a.findViewById(i8);
            this.f22320t.put(i8, t9);
            return t9;
        }
        return t8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public d N(b bVar) {
        this.f22324x = bVar;
        return this;
    }

    public d O(int i8, boolean z4) {
        M(i8).setVisibility(z4 ? 0 : 8);
        return this;
    }

    public d P(int i8, CharSequence charSequence) {
        ((TextView) M(i8)).setText(charSequence);
        return this;
    }

    public d Q(int i8, int i9) {
        ((TextView) M(i8)).setTextColor(i9);
        return this;
    }

    public d R(int i8, boolean z4) {
        M(i8).setVisibility(z4 ? 0 : 4);
        return this;
    }
}
