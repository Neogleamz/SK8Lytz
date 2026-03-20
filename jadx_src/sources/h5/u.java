package h5;

import android.os.Bundle;
import b6.l0;
import b6.p;
import b6.t;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.g;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u implements com.google.android.exoplayer2.g {

    /* renamed from: f  reason: collision with root package name */
    private static final String f20305f = l0.r0(0);

    /* renamed from: g  reason: collision with root package name */
    private static final String f20306g = l0.r0(1);

    /* renamed from: h  reason: collision with root package name */
    public static final g.a<u> f20307h = t.a;

    /* renamed from: a  reason: collision with root package name */
    public final int f20308a;

    /* renamed from: b  reason: collision with root package name */
    public final String f20309b;

    /* renamed from: c  reason: collision with root package name */
    public final int f20310c;

    /* renamed from: d  reason: collision with root package name */
    private final w0[] f20311d;

    /* renamed from: e  reason: collision with root package name */
    private int f20312e;

    public u(String str, w0... w0VarArr) {
        b6.a.a(w0VarArr.length > 0);
        this.f20309b = str;
        this.f20311d = w0VarArr;
        this.f20308a = w0VarArr.length;
        int k8 = t.k(w0VarArr[0].f11207m);
        this.f20310c = k8 == -1 ? t.k(w0VarArr[0].f11206l) : k8;
        h();
    }

    public u(w0... w0VarArr) {
        this(BuildConfig.FLAVOR, w0VarArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ u d(Bundle bundle) {
        ArrayList parcelableArrayList = bundle.getParcelableArrayList(f20305f);
        return new u(bundle.getString(f20306g, BuildConfig.FLAVOR), (w0[]) (parcelableArrayList == null ? ImmutableList.E() : b6.c.b(w0.D0, parcelableArrayList)).toArray(new w0[0]));
    }

    private static void e(String str, String str2, String str3, int i8) {
        p.d("TrackGroup", BuildConfig.FLAVOR, new IllegalStateException("Different " + str + " combined in one TrackGroup: '" + str2 + "' (track 0) and '" + str3 + "' (track " + i8 + ")"));
    }

    private static String f(String str) {
        return (str == null || str.equals("und")) ? BuildConfig.FLAVOR : str;
    }

    private static int g(int i8) {
        return i8 | 16384;
    }

    private void h() {
        String f5 = f(this.f20311d[0].f11198c);
        int g8 = g(this.f20311d[0].f11200e);
        int i8 = 1;
        while (true) {
            w0[] w0VarArr = this.f20311d;
            if (i8 >= w0VarArr.length) {
                return;
            }
            if (!f5.equals(f(w0VarArr[i8].f11198c))) {
                w0[] w0VarArr2 = this.f20311d;
                e("languages", w0VarArr2[0].f11198c, w0VarArr2[i8].f11198c, i8);
                return;
            } else if (g8 != g(this.f20311d[i8].f11200e)) {
                e("role flags", Integer.toBinaryString(this.f20311d[0].f11200e), Integer.toBinaryString(this.f20311d[i8].f11200e), i8);
                return;
            } else {
                i8++;
            }
        }
    }

    public w0 b(int i8) {
        return this.f20311d[i8];
    }

    public int c(w0 w0Var) {
        int i8 = 0;
        while (true) {
            w0[] w0VarArr = this.f20311d;
            if (i8 >= w0VarArr.length) {
                return -1;
            }
            if (w0Var == w0VarArr[i8]) {
                return i8;
            }
            i8++;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || u.class != obj.getClass()) {
            return false;
        }
        u uVar = (u) obj;
        return this.f20309b.equals(uVar.f20309b) && Arrays.equals(this.f20311d, uVar.f20311d);
    }

    public int hashCode() {
        if (this.f20312e == 0) {
            this.f20312e = ((527 + this.f20309b.hashCode()) * 31) + Arrays.hashCode(this.f20311d);
        }
        return this.f20312e;
    }
}
