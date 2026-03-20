package y;

import androidx.camera.core.CameraUnavailableException;
import androidx.camera.core.InitializationException;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.p1;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r {

    /* renamed from: a  reason: collision with root package name */
    private final Object f24311a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private final Map<String, CameraInternal> f24312b = new LinkedHashMap();

    /* renamed from: c  reason: collision with root package name */
    private final Set<CameraInternal> f24313c = new HashSet();

    public LinkedHashSet<CameraInternal> a() {
        LinkedHashSet<CameraInternal> linkedHashSet;
        synchronized (this.f24311a) {
            linkedHashSet = new LinkedHashSet<>(this.f24312b.values());
        }
        return linkedHashSet;
    }

    public void b(p pVar) {
        synchronized (this.f24311a) {
            try {
                try {
                    for (String str : pVar.a()) {
                        p1.a("CameraRepository", "Added camera: " + str);
                        this.f24312b.put(str, pVar.b(str));
                    }
                } catch (CameraUnavailableException e8) {
                    throw new InitializationException(e8);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
