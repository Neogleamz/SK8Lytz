package p4;

import b6.l0;
import b6.p;
import b6.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d implements a {

    /* renamed from: a  reason: collision with root package name */
    public final int f22349a;

    /* renamed from: b  reason: collision with root package name */
    public final int f22350b;

    /* renamed from: c  reason: collision with root package name */
    public final int f22351c;

    /* renamed from: d  reason: collision with root package name */
    public final int f22352d;

    /* renamed from: e  reason: collision with root package name */
    public final int f22353e;

    /* renamed from: f  reason: collision with root package name */
    public final int f22354f;

    private d(int i8, int i9, int i10, int i11, int i12, int i13) {
        this.f22349a = i8;
        this.f22350b = i9;
        this.f22351c = i10;
        this.f22352d = i11;
        this.f22353e = i12;
        this.f22354f = i13;
    }

    public static d c(z zVar) {
        int u8 = zVar.u();
        zVar.V(12);
        int u9 = zVar.u();
        int u10 = zVar.u();
        int u11 = zVar.u();
        zVar.V(4);
        int u12 = zVar.u();
        int u13 = zVar.u();
        zVar.V(8);
        return new d(u8, u9, u10, u11, u12, u13);
    }

    public long a() {
        return l0.O0(this.f22353e, this.f22351c * 1000000, this.f22352d);
    }

    public int b() {
        int i8 = this.f22349a;
        if (i8 != 1935960438) {
            if (i8 != 1935963489) {
                if (i8 != 1937012852) {
                    p.i("AviStreamHeaderChunk", "Found unsupported streamType fourCC: " + Integer.toHexString(this.f22349a));
                    return -1;
                }
                return 3;
            }
            return 1;
        }
        return 2;
    }

    @Override // p4.a
    public int getType() {
        return 1752331379;
    }
}
