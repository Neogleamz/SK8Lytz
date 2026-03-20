package z5;

import android.os.Bundle;
import b6.l0;
import com.google.android.exoplayer2.g;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w implements g {

    /* renamed from: c  reason: collision with root package name */
    private static final String f24702c = l0.r0(0);

    /* renamed from: d  reason: collision with root package name */
    private static final String f24703d = l0.r0(1);

    /* renamed from: e  reason: collision with root package name */
    public static final g.a<w> f24704e = v.a;

    /* renamed from: a  reason: collision with root package name */
    public final h5.u f24705a;

    /* renamed from: b  reason: collision with root package name */
    public final ImmutableList<Integer> f24706b;

    public w(h5.u uVar, List<Integer> list) {
        if (!list.isEmpty() && (((Integer) Collections.min(list)).intValue() < 0 || ((Integer) Collections.max(list)).intValue() >= uVar.f20308a)) {
            throw new IndexOutOfBoundsException();
        }
        this.f24705a = uVar;
        this.f24706b = ImmutableList.x(list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ w c(Bundle bundle) {
        return new w(h5.u.f20307h.a((Bundle) b6.a.e(bundle.getBundle(f24702c))), com.google.common.primitives.g.c((int[]) b6.a.e(bundle.getIntArray(f24703d))));
    }

    public int b() {
        return this.f24705a.f20310c;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || w.class != obj.getClass()) {
            return false;
        }
        w wVar = (w) obj;
        return this.f24705a.equals(wVar.f24705a) && this.f24706b.equals(wVar.f24706b);
    }

    public int hashCode() {
        return this.f24705a.hashCode() + (this.f24706b.hashCode() * 31);
    }
}
