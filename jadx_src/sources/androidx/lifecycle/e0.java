package androidx.lifecycle;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class e0 {

    /* renamed from: a  reason: collision with root package name */
    private final Map<String, Object> f5866a = new HashMap();

    /* renamed from: b  reason: collision with root package name */
    private final Set<Closeable> f5867b = new LinkedHashSet();

    /* renamed from: c  reason: collision with root package name */
    private volatile boolean f5868c = false;

    private static void b(Object obj) {
        if (obj instanceof Closeable) {
            try {
                ((Closeable) obj).close();
            } catch (IOException e8) {
                throw new RuntimeException(e8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a() {
        this.f5868c = true;
        Map<String, Object> map = this.f5866a;
        if (map != null) {
            synchronized (map) {
                for (Object obj : this.f5866a.values()) {
                    b(obj);
                }
            }
        }
        Set<Closeable> set = this.f5867b;
        if (set != null) {
            synchronized (set) {
                for (Closeable closeable : this.f5867b) {
                    b(closeable);
                }
            }
        }
        d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <T> T c(String str) {
        T t8;
        Map<String, Object> map = this.f5866a;
        if (map == null) {
            return null;
        }
        synchronized (map) {
            t8 = (T) this.f5866a.get(str);
        }
        return t8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void d() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <T> T e(String str, T t8) {
        Object obj;
        synchronized (this.f5866a) {
            obj = this.f5866a.get(str);
            if (obj == null) {
                this.f5866a.put(str, t8);
            }
        }
        if (obj != null) {
            t8 = obj;
        }
        if (this.f5868c) {
            b(t8);
        }
        return t8;
    }
}
