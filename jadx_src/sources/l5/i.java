package l5;

import android.net.Uri;
import b6.j0;
import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i {

    /* renamed from: a  reason: collision with root package name */
    public final long f21679a;

    /* renamed from: b  reason: collision with root package name */
    public final long f21680b;

    /* renamed from: c  reason: collision with root package name */
    private final String f21681c;

    /* renamed from: d  reason: collision with root package name */
    private int f21682d;

    public i(String str, long j8, long j9) {
        this.f21681c = str == null ? BuildConfig.FLAVOR : str;
        this.f21679a = j8;
        this.f21680b = j9;
    }

    public i a(i iVar, String str) {
        String c9 = c(str);
        if (iVar != null && c9.equals(iVar.c(str))) {
            long j8 = this.f21680b;
            if (j8 != -1) {
                long j9 = this.f21679a;
                if (j9 + j8 == iVar.f21679a) {
                    long j10 = iVar.f21680b;
                    return new i(c9, j9, j10 != -1 ? j8 + j10 : -1L);
                }
            }
            long j11 = iVar.f21680b;
            if (j11 != -1) {
                long j12 = iVar.f21679a;
                if (j12 + j11 == this.f21679a) {
                    return new i(c9, j12, j8 != -1 ? j11 + j8 : -1L);
                }
            }
        }
        return null;
    }

    public Uri b(String str) {
        return j0.e(str, this.f21681c);
    }

    public String c(String str) {
        return j0.d(str, this.f21681c);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || i.class != obj.getClass()) {
            return false;
        }
        i iVar = (i) obj;
        return this.f21679a == iVar.f21679a && this.f21680b == iVar.f21680b && this.f21681c.equals(iVar.f21681c);
    }

    public int hashCode() {
        if (this.f21682d == 0) {
            this.f21682d = ((((527 + ((int) this.f21679a)) * 31) + ((int) this.f21680b)) * 31) + this.f21681c.hashCode();
        }
        return this.f21682d;
    }

    public String toString() {
        return "RangedUri(referenceUri=" + this.f21681c + ", start=" + this.f21679a + ", length=" + this.f21680b + ")";
    }
}
