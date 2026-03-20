package h5;

import android.os.Bundle;
import b6.l0;
import b6.p;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.g;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w implements com.google.android.exoplayer2.g {

    /* renamed from: d  reason: collision with root package name */
    public static final w f20313d = new w(new u[0]);

    /* renamed from: e  reason: collision with root package name */
    private static final String f20314e = l0.r0(0);

    /* renamed from: f  reason: collision with root package name */
    public static final g.a<w> f20315f = v.a;

    /* renamed from: a  reason: collision with root package name */
    public final int f20316a;

    /* renamed from: b  reason: collision with root package name */
    private final ImmutableList<u> f20317b;

    /* renamed from: c  reason: collision with root package name */
    private int f20318c;

    public w(u... uVarArr) {
        this.f20317b = ImmutableList.y(uVarArr);
        this.f20316a = uVarArr.length;
        e();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ w d(Bundle bundle) {
        ArrayList parcelableArrayList = bundle.getParcelableArrayList(f20314e);
        return parcelableArrayList == null ? new w(new u[0]) : new w((u[]) b6.c.b(u.f20307h, parcelableArrayList).toArray(new u[0]));
    }

    private void e() {
        int i8 = 0;
        while (i8 < this.f20317b.size()) {
            int i9 = i8 + 1;
            for (int i10 = i9; i10 < this.f20317b.size(); i10++) {
                if (this.f20317b.get(i8).equals(this.f20317b.get(i10))) {
                    p.d("TrackGroupArray", BuildConfig.FLAVOR, new IllegalArgumentException("Multiple identical TrackGroups added to one TrackGroupArray."));
                }
            }
            i8 = i9;
        }
    }

    public u b(int i8) {
        return this.f20317b.get(i8);
    }

    public int c(u uVar) {
        int indexOf = this.f20317b.indexOf(uVar);
        if (indexOf >= 0) {
            return indexOf;
        }
        return -1;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || w.class != obj.getClass()) {
            return false;
        }
        w wVar = (w) obj;
        return this.f20316a == wVar.f20316a && this.f20317b.equals(wVar.f20317b);
    }

    public int hashCode() {
        if (this.f20318c == 0) {
            this.f20318c = this.f20317b.hashCode();
        }
        return this.f20318c;
    }
}
