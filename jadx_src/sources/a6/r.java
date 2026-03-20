package a6;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r {

    /* renamed from: a  reason: collision with root package name */
    private final Map<String, String> f171a = new HashMap();

    /* renamed from: b  reason: collision with root package name */
    private Map<String, String> f172b;

    public synchronized void a(Map<String, String> map) {
        this.f172b = null;
        this.f171a.clear();
        this.f171a.putAll(map);
    }

    public synchronized Map<String, String> b() {
        if (this.f172b == null) {
            this.f172b = Collections.unmodifiableMap(new HashMap(this.f171a));
        }
        return this.f172b;
    }
}
