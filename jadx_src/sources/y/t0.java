package y;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class t0 {

    /* renamed from: a  reason: collision with root package name */
    private final List<s0> f24314a;

    public t0(List<s0> list) {
        this.f24314a = new ArrayList(list);
    }

    public boolean a(Class<? extends s0> cls) {
        for (s0 s0Var : this.f24314a) {
            if (cls.isAssignableFrom(s0Var.getClass())) {
                return true;
            }
        }
        return false;
    }

    public <T extends s0> T b(Class<T> cls) {
        Iterator<s0> it = this.f24314a.iterator();
        while (it.hasNext()) {
            T t8 = (T) it.next();
            if (t8.getClass() == cls) {
                return t8;
            }
        }
        return null;
    }
}
