package androidx.camera.camera2.internal;

import android.content.Context;
import android.media.CamcorderProfile;
import android.util.Size;
import androidx.camera.core.impl.SurfaceConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c1 implements y.o {

    /* renamed from: a  reason: collision with root package name */
    private final Map<String, m2> f1760a;

    /* renamed from: b  reason: collision with root package name */
    private final c f1761b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements c {
        a() {
        }

        @Override // androidx.camera.camera2.internal.c
        public CamcorderProfile a(int i8, int i9) {
            return CamcorderProfile.get(i8, i9);
        }

        @Override // androidx.camera.camera2.internal.c
        public boolean b(int i8, int i9) {
            return CamcorderProfile.hasProfile(i8, i9);
        }
    }

    c1(Context context, c cVar, Object obj, Set<String> set) {
        this.f1760a = new HashMap();
        androidx.core.util.h.h(cVar);
        this.f1761b = cVar;
        c(context, obj instanceof s.l0 ? (s.l0) obj : s.l0.a(context), set);
    }

    public c1(Context context, Object obj, Set<String> set) {
        this(context, new a(), obj, set);
    }

    private void c(Context context, s.l0 l0Var, Set<String> set) {
        androidx.core.util.h.h(context);
        for (String str : set) {
            this.f1760a.put(str, new m2(context, str, l0Var, this.f1761b));
        }
    }

    @Override // y.o
    public SurfaceConfig a(String str, int i8, Size size) {
        m2 m2Var = this.f1760a.get(str);
        if (m2Var != null) {
            return m2Var.A(i8, size);
        }
        return null;
    }

    @Override // y.o
    public Map<androidx.camera.core.impl.v<?>, Size> b(String str, List<y.a> list, List<androidx.camera.core.impl.v<?>> list2) {
        androidx.core.util.h.b(!list2.isEmpty(), "No new use cases to be bound.");
        m2 m2Var = this.f1760a.get(str);
        if (m2Var != null) {
            return m2Var.q(list, list2);
        }
        throw new IllegalArgumentException("No such camera id in supported combination list: " + str);
    }
}
