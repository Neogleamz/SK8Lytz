package c6;

import b6.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: a  reason: collision with root package name */
    public final int f8343a;

    /* renamed from: b  reason: collision with root package name */
    public final int f8344b;

    /* renamed from: c  reason: collision with root package name */
    public final String f8345c;

    private d(int i8, int i9, String str) {
        this.f8343a = i8;
        this.f8344b = i9;
        this.f8345c = str;
    }

    public static d a(z zVar) {
        String str;
        zVar.V(2);
        int H = zVar.H();
        int i8 = H >> 1;
        int H2 = ((zVar.H() >> 3) & 31) | ((H & 1) << 5);
        if (i8 == 4 || i8 == 5 || i8 == 7) {
            str = "dvhe";
        } else if (i8 == 8) {
            str = "hev1";
        } else if (i8 != 9) {
            return null;
        } else {
            str = "avc3";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(".0");
        sb.append(i8);
        sb.append(H2 >= 10 ? "." : ".0");
        sb.append(H2);
        return new d(i8, H2, sb.toString());
    }
}
