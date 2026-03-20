package p4;

import b6.z;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.d3;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f implements a {

    /* renamed from: a  reason: collision with root package name */
    public final ImmutableList<a> f22367a;

    /* renamed from: b  reason: collision with root package name */
    private final int f22368b;

    private f(int i8, ImmutableList<a> immutableList) {
        this.f22368b = i8;
        this.f22367a = immutableList;
    }

    private static a a(int i8, int i9, z zVar) {
        switch (i8) {
            case 1718776947:
                return g.d(i9, zVar);
            case 1751742049:
                return c.b(zVar);
            case 1752331379:
                return d.c(zVar);
            case 1852994675:
                return h.a(zVar);
            default:
                return null;
        }
    }

    public static f c(int i8, z zVar) {
        ImmutableList.a aVar = new ImmutableList.a();
        int g8 = zVar.g();
        int i9 = -2;
        while (zVar.a() > 8) {
            int u8 = zVar.u();
            int f5 = zVar.f() + zVar.u();
            zVar.T(f5);
            a c9 = u8 == 1414744396 ? c(zVar.u(), zVar) : a(u8, i9, zVar);
            if (c9 != null) {
                if (c9.getType() == 1752331379) {
                    i9 = ((d) c9).b();
                }
                aVar.a(c9);
            }
            zVar.U(f5);
            zVar.T(g8);
        }
        return new f(i8, aVar.k());
    }

    public <T extends a> T b(Class<T> cls) {
        d3<a> it = this.f22367a.iterator();
        while (it.hasNext()) {
            T t8 = (T) it.next();
            if (t8.getClass() == cls) {
                return t8;
            }
        }
        return null;
    }

    @Override // p4.a
    public int getType() {
        return this.f22368b;
    }
}
