package i2;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f {

    /* renamed from: a  reason: collision with root package name */
    private final String f20443a;

    /* renamed from: b  reason: collision with root package name */
    private final f f20444b;

    /* renamed from: c  reason: collision with root package name */
    private final String f20445c;

    /* renamed from: d  reason: collision with root package name */
    private final f[] f20446d;

    /* renamed from: e  reason: collision with root package name */
    private final e[] f20447e;

    public f(Throwable th) {
        this(th, Collections.newSetFromMap(new IdentityHashMap()));
    }

    public f(Throwable th, Set<Throwable> set) {
        set.add(th);
        this.f20443a = th.getMessage();
        this.f20444b = (th.getCause() == null || set.contains(th.getCause())) ? null : new f(th.getCause(), set);
        this.f20445c = th.getClass().getName();
        Throwable[] suppressed = th.getSuppressed();
        LinkedList linkedList = new LinkedList();
        int length = suppressed.length;
        for (int i8 = 0; i8 < length; i8++) {
            if (!set.contains(suppressed[i8])) {
                linkedList.add(new f(suppressed[i8], set));
            }
        }
        this.f20446d = (f[]) linkedList.toArray(new f[0]);
        StackTraceElement[] stackTrace = th.getStackTrace();
        this.f20447e = new e[stackTrace.length];
        int length2 = stackTrace.length;
        for (int i9 = 0; i9 < length2; i9++) {
            this.f20447e[i9] = new e(stackTrace[i9]);
        }
    }

    public f a() {
        return this.f20444b;
    }

    public String b() {
        return this.f20445c;
    }

    public String c() {
        return this.f20443a;
    }

    public e[] d() {
        return this.f20447e;
    }

    public f[] e() {
        return this.f20446d;
    }
}
