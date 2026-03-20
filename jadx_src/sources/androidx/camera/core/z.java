package androidx.camera.core;

import androidx.camera.core.impl.g;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a implements y.u {

        /* renamed from: a  reason: collision with root package name */
        final List<androidx.camera.core.impl.g> f2893a;

        a(List<androidx.camera.core.impl.g> list) {
            if (list == null || list.isEmpty()) {
                throw new IllegalArgumentException("Cannot set an empty CaptureStage list.");
            }
            this.f2893a = Collections.unmodifiableList(new ArrayList(list));
        }

        @Override // y.u
        public List<androidx.camera.core.impl.g> a() {
            return this.f2893a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static y.u a(List<androidx.camera.core.impl.g> list) {
        return new a(list);
    }

    static y.u b(androidx.camera.core.impl.g... gVarArr) {
        return new a(Arrays.asList(gVarArr));
    }

    public static y.u c() {
        return b(new g.a());
    }
}
