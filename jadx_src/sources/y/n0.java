package y;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class n0<C> {

    /* renamed from: a  reason: collision with root package name */
    private Set<C> f24310a = new HashSet();

    public void a(List<C> list) {
        this.f24310a.addAll(list);
    }

    @Override // 
    /* renamed from: b */
    public abstract n0<C> clone();

    public List<C> c() {
        return Collections.unmodifiableList(new ArrayList(this.f24310a));
    }
}
