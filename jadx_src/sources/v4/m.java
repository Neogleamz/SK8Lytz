package v4;

import b6.z;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.mp4.SlowMotionData;
import java.util.ArrayList;
import java.util.List;
import n4.y;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class m {

    /* renamed from: d  reason: collision with root package name */
    private static final com.google.common.base.p f23290d = com.google.common.base.p.d(':');

    /* renamed from: e  reason: collision with root package name */
    private static final com.google.common.base.p f23291e = com.google.common.base.p.d('*');

    /* renamed from: a  reason: collision with root package name */
    private final List<a> f23292a = new ArrayList();

    /* renamed from: b  reason: collision with root package name */
    private int f23293b = 0;

    /* renamed from: c  reason: collision with root package name */
    private int f23294c;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final int f23295a;

        /* renamed from: b  reason: collision with root package name */
        public final long f23296b;

        /* renamed from: c  reason: collision with root package name */
        public final int f23297c;

        public a(int i8, long j8, int i9) {
            this.f23295a = i8;
            this.f23296b = j8;
            this.f23297c = i9;
        }
    }

    private void a(n4.l lVar, y yVar) {
        z zVar = new z(8);
        lVar.readFully(zVar.e(), 0, 8);
        this.f23294c = zVar.u() + 8;
        if (zVar.q() != 1397048916) {
            yVar.f22152a = 0L;
            return;
        }
        yVar.f22152a = lVar.getPosition() - (this.f23294c - 12);
        this.f23293b = 2;
    }

    private static int b(String str) {
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -1711564334:
                if (str.equals("SlowMotion_Data")) {
                    c9 = 0;
                    break;
                }
                break;
            case -1332107749:
                if (str.equals("Super_SlowMotion_Edit_Data")) {
                    c9 = 1;
                    break;
                }
                break;
            case -1251387154:
                if (str.equals("Super_SlowMotion_Data")) {
                    c9 = 2;
                    break;
                }
                break;
            case -830665521:
                if (str.equals("Super_SlowMotion_Deflickering_On")) {
                    c9 = 3;
                    break;
                }
                break;
            case 1760745220:
                if (str.equals("Super_SlowMotion_BGM")) {
                    c9 = 4;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                return 2192;
            case 1:
                return 2819;
            case 2:
                return 2816;
            case 3:
                return 2820;
            case 4:
                return 2817;
            default:
                throw ParserException.a("Invalid SEF name", null);
        }
    }

    private void d(n4.l lVar, y yVar) {
        long j8;
        long b9 = lVar.b();
        int i8 = (this.f23294c - 12) - 8;
        z zVar = new z(i8);
        lVar.readFully(zVar.e(), 0, i8);
        for (int i9 = 0; i9 < i8 / 12; i9++) {
            zVar.V(2);
            short w8 = zVar.w();
            if (w8 == 2192 || w8 == 2816 || w8 == 2817 || w8 == 2819 || w8 == 2820) {
                this.f23292a.add(new a(w8, (b9 - this.f23294c) - zVar.u(), zVar.u()));
            } else {
                zVar.V(8);
            }
        }
        if (this.f23292a.isEmpty()) {
            j8 = 0;
        } else {
            this.f23293b = 3;
            j8 = this.f23292a.get(0).f23296b;
        }
        yVar.f22152a = j8;
    }

    private void e(n4.l lVar, List<Metadata.Entry> list) {
        long position = lVar.getPosition();
        int b9 = (int) ((lVar.b() - lVar.getPosition()) - this.f23294c);
        z zVar = new z(b9);
        lVar.readFully(zVar.e(), 0, b9);
        for (int i8 = 0; i8 < this.f23292a.size(); i8++) {
            a aVar = this.f23292a.get(i8);
            zVar.U((int) (aVar.f23296b - position));
            zVar.V(4);
            int u8 = zVar.u();
            int b10 = b(zVar.E(u8));
            int i9 = aVar.f23297c - (u8 + 8);
            if (b10 == 2192) {
                list.add(f(zVar, i9));
            } else if (b10 != 2816 && b10 != 2817 && b10 != 2819 && b10 != 2820) {
                throw new IllegalStateException();
            }
        }
    }

    private static SlowMotionData f(z zVar, int i8) {
        ArrayList arrayList = new ArrayList();
        List<String> f5 = f23291e.f(zVar.E(i8));
        for (int i9 = 0; i9 < f5.size(); i9++) {
            List<String> f8 = f23290d.f(f5.get(i9));
            if (f8.size() != 3) {
                throw ParserException.a(null, null);
            }
            try {
                arrayList.add(new SlowMotionData.Segment(Long.parseLong(f8.get(0)), Long.parseLong(f8.get(1)), 1 << (Integer.parseInt(f8.get(2)) - 1)));
            } catch (NumberFormatException e8) {
                throw ParserException.a(null, e8);
            }
        }
        return new SlowMotionData(arrayList);
    }

    public int c(n4.l lVar, y yVar, List<Metadata.Entry> list) {
        int i8 = this.f23293b;
        long j8 = 0;
        if (i8 == 0) {
            long b9 = lVar.b();
            if (b9 != -1 && b9 >= 8) {
                j8 = b9 - 8;
            }
            yVar.f22152a = j8;
            this.f23293b = 1;
        } else if (i8 == 1) {
            a(lVar, yVar);
        } else if (i8 == 2) {
            d(lVar, yVar);
        } else if (i8 != 3) {
            throw new IllegalStateException();
        } else {
            e(lVar, list);
            yVar.f22152a = 0L;
        }
        return 1;
    }

    public void g() {
        this.f23292a.clear();
        this.f23293b = 0;
    }
}
