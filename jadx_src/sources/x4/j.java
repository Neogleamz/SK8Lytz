package x4;

import android.util.SparseArray;
import b6.z;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j implements i0.c {

    /* renamed from: a  reason: collision with root package name */
    private final int f23939a;

    /* renamed from: b  reason: collision with root package name */
    private final List<w0> f23940b;

    public j(int i8) {
        this(i8, ImmutableList.E());
    }

    public j(int i8, List<w0> list) {
        this.f23939a = i8;
        this.f23940b = list;
    }

    private d0 c(i0.b bVar) {
        return new d0(e(bVar));
    }

    private k0 d(i0.b bVar) {
        return new k0(e(bVar));
    }

    private List<w0> e(i0.b bVar) {
        String str;
        int i8;
        if (f(32)) {
            return this.f23940b;
        }
        z zVar = new z(bVar.f23933d);
        List<w0> list = this.f23940b;
        while (zVar.a() > 0) {
            int H = zVar.H();
            int f5 = zVar.f() + zVar.H();
            if (H == 134) {
                list = new ArrayList<>();
                int H2 = zVar.H() & 31;
                for (int i9 = 0; i9 < H2; i9++) {
                    String E = zVar.E(3);
                    int H3 = zVar.H();
                    boolean z4 = (H3 & RecognitionOptions.ITF) != 0;
                    if (z4) {
                        i8 = H3 & 63;
                        str = "application/cea-708";
                    } else {
                        str = "application/cea-608";
                        i8 = 1;
                    }
                    byte H4 = (byte) zVar.H();
                    zVar.V(1);
                    List<byte[]> list2 = null;
                    if (z4) {
                        list2 = b6.e.b((H4 & 64) != 0);
                    }
                    list.add(new w0.b().g0(str).X(E).H(i8).V(list2).G());
                }
            }
            zVar.U(f5);
        }
        return list;
    }

    private boolean f(int i8) {
        return (i8 & this.f23939a) != 0;
    }

    @Override // x4.i0.c
    public i0 a(int i8, i0.b bVar) {
        if (i8 != 2) {
            if (i8 == 3 || i8 == 4) {
                return new w(new t(bVar.f23931b));
            }
            if (i8 != 21) {
                if (i8 == 27) {
                    if (f(4)) {
                        return null;
                    }
                    return new w(new p(c(bVar), f(1), f(8)));
                } else if (i8 != 36) {
                    if (i8 != 89) {
                        if (i8 != 138) {
                            if (i8 != 172) {
                                if (i8 != 257) {
                                    if (i8 == 134) {
                                        if (f(16)) {
                                            return null;
                                        }
                                        return new c0(new v("application/x-scte35"));
                                    }
                                    if (i8 != 135) {
                                        switch (i8) {
                                            case 15:
                                                if (f(2)) {
                                                    return null;
                                                }
                                                return new w(new i(false, bVar.f23931b));
                                            case 16:
                                                return new w(new o(d(bVar)));
                                            case 17:
                                                if (f(2)) {
                                                    return null;
                                                }
                                                return new w(new s(bVar.f23931b));
                                            default:
                                                switch (i8) {
                                                    case RecognitionOptions.ITF /* 128 */:
                                                        break;
                                                    case 129:
                                                        break;
                                                    case 130:
                                                        if (!f(64)) {
                                                            return null;
                                                        }
                                                        break;
                                                    default:
                                                        return null;
                                                }
                                        }
                                    }
                                    return new w(new c(bVar.f23931b));
                                }
                                return new c0(new v("application/vnd.dvb.ait"));
                            }
                            return new w(new f(bVar.f23931b));
                        }
                        return new w(new k(bVar.f23931b));
                    }
                    return new w(new l(bVar.f23932c));
                } else {
                    return new w(new q(c(bVar)));
                }
            }
            return new w(new r());
        }
        return new w(new n(d(bVar)));
    }

    @Override // x4.i0.c
    public SparseArray<i0> b() {
        return new SparseArray<>();
    }
}
