package y;

import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c0 {

    /* renamed from: a  reason: collision with root package name */
    private static final Object f24289a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private static final Map<Object, m> f24290b = new HashMap();

    public static m a(Object obj) {
        m mVar;
        synchronized (f24289a) {
            mVar = f24290b.get(obj);
        }
        return mVar == null ? m.f24301a : mVar;
    }
}
