package c6;

import b6.u;
import b6.z;
import com.google.android.exoplayer2.ParserException;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    public final List<byte[]> f8325a;

    /* renamed from: b  reason: collision with root package name */
    public final int f8326b;

    /* renamed from: c  reason: collision with root package name */
    public final int f8327c;

    /* renamed from: d  reason: collision with root package name */
    public final int f8328d;

    /* renamed from: e  reason: collision with root package name */
    public final float f8329e;

    /* renamed from: f  reason: collision with root package name */
    public final String f8330f;

    private a(List<byte[]> list, int i8, int i9, int i10, float f5, String str) {
        this.f8325a = list;
        this.f8326b = i8;
        this.f8327c = i9;
        this.f8328d = i10;
        this.f8329e = f5;
        this.f8330f = str;
    }

    private static byte[] a(z zVar) {
        int N = zVar.N();
        int f5 = zVar.f();
        zVar.V(N);
        return b6.e.d(zVar.e(), f5, N);
    }

    public static a b(z zVar) {
        float f5;
        String str;
        int i8;
        try {
            zVar.V(4);
            int H = (zVar.H() & 3) + 1;
            if (H != 3) {
                ArrayList arrayList = new ArrayList();
                int H2 = zVar.H() & 31;
                for (int i9 = 0; i9 < H2; i9++) {
                    arrayList.add(a(zVar));
                }
                int H3 = zVar.H();
                for (int i10 = 0; i10 < H3; i10++) {
                    arrayList.add(a(zVar));
                }
                int i11 = -1;
                if (H2 > 0) {
                    u.c l8 = b6.u.l((byte[]) arrayList.get(0), H, ((byte[]) arrayList.get(0)).length);
                    int i12 = l8.f8134f;
                    int i13 = l8.f8135g;
                    float f8 = l8.f8136h;
                    str = b6.e.a(l8.f8129a, l8.f8130b, l8.f8131c);
                    i11 = i12;
                    i8 = i13;
                    f5 = f8;
                } else {
                    f5 = 1.0f;
                    str = null;
                    i8 = -1;
                }
                return new a(arrayList, H, i11, i8, f5, str);
            }
            throw new IllegalStateException();
        } catch (ArrayIndexOutOfBoundsException e8) {
            throw ParserException.a("Error parsing AVC config", e8);
        }
    }
}
