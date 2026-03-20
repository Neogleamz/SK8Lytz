package p5;

import android.os.Bundle;
import b6.l0;
import com.google.android.exoplayer2.g;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e implements com.google.android.exoplayer2.g {

    /* renamed from: c  reason: collision with root package name */
    public static final e f22406c = new e(ImmutableList.E(), 0);

    /* renamed from: d  reason: collision with root package name */
    private static final String f22407d = l0.r0(0);

    /* renamed from: e  reason: collision with root package name */
    private static final String f22408e = l0.r0(1);

    /* renamed from: f  reason: collision with root package name */
    public static final g.a<e> f22409f = d.a;

    /* renamed from: a  reason: collision with root package name */
    public final ImmutableList<b> f22410a;

    /* renamed from: b  reason: collision with root package name */
    public final long f22411b;

    public e(List<b> list, long j8) {
        this.f22410a = ImmutableList.x(list);
        this.f22411b = j8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final e b(Bundle bundle) {
        ArrayList parcelableArrayList = bundle.getParcelableArrayList(f22407d);
        return new e(parcelableArrayList == null ? ImmutableList.E() : b6.c.b(b.X, parcelableArrayList), bundle.getLong(f22408e));
    }
}
