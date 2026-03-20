package u5;

import android.text.Layout;
import b6.l0;
import b6.p;
import b6.z;
import com.google.common.base.e;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import p5.g;
import p5.h;
import u5.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends g {

    /* renamed from: t  reason: collision with root package name */
    private static final Pattern f23028t = Pattern.compile("(?:(\\d+):)?(\\d+):(\\d+)[:.](\\d+)");

    /* renamed from: o  reason: collision with root package name */
    private final boolean f23029o;

    /* renamed from: p  reason: collision with root package name */
    private final b f23030p;
    private Map<String, c> q;

    /* renamed from: r  reason: collision with root package name */
    private float f23031r;

    /* renamed from: s  reason: collision with root package name */
    private float f23032s;

    public a(List<byte[]> list) {
        super("SsaDecoder");
        this.f23031r = -3.4028235E38f;
        this.f23032s = -3.4028235E38f;
        if (list == null || list.isEmpty()) {
            this.f23029o = false;
            this.f23030p = null;
            return;
        }
        this.f23029o = true;
        String D = l0.D(list.get(0));
        b6.a.a(D.startsWith("Format:"));
        this.f23030p = (b) b6.a.e(b.a(D));
        H(new z(list.get(1)), e.f18817c);
    }

    private static int B(long j8, List<Long> list, List<List<p5.b>> list2) {
        int i8;
        int size = list.size() - 1;
        while (true) {
            if (size < 0) {
                i8 = 0;
                break;
            } else if (list.get(size).longValue() == j8) {
                return size;
            } else {
                if (list.get(size).longValue() < j8) {
                    i8 = size + 1;
                    break;
                }
                size--;
            }
        }
        list.add(i8, Long.valueOf(j8));
        list2.add(i8, i8 == 0 ? new ArrayList() : new ArrayList(list2.get(i8 - 1)));
        return i8;
    }

    private static float C(int i8) {
        if (i8 != 0) {
            if (i8 != 1) {
                return i8 != 2 ? -3.4028235E38f : 0.95f;
            }
            return 0.5f;
        }
        return 0.05f;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0092  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static p5.b D(java.lang.String r8, u5.c r9, u5.c.b r10, float r11, float r12) {
        /*
            Method dump skipped, instructions count: 244
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: u5.a.D(java.lang.String, u5.c, u5.c$b, float, float):p5.b");
    }

    private Charset E(z zVar) {
        Charset P = zVar.P();
        return P != null ? P : e.f18817c;
    }

    private void F(String str, b bVar, List<List<p5.b>> list, List<Long> list2) {
        int i8;
        StringBuilder sb;
        b6.a.a(str.startsWith("Dialogue:"));
        String[] split = str.substring(9).split(",", bVar.f23037e);
        if (split.length != bVar.f23037e) {
            sb = new StringBuilder();
            sb.append("Skipping dialogue line with fewer columns than format: ");
        } else {
            long K = K(split[bVar.f23033a]);
            if (K == -9223372036854775807L) {
                sb = new StringBuilder();
            } else {
                long K2 = K(split[bVar.f23034b]);
                if (K2 != -9223372036854775807L) {
                    Map<String, c> map = this.q;
                    c cVar = (map == null || (i8 = bVar.f23035c) == -1) ? null : map.get(split[i8].trim());
                    String str2 = split[bVar.f23036d];
                    p5.b D = D(c.b.d(str2).replace("\\N", "\n").replace("\\n", "\n").replace("\\h", " "), cVar, c.b.b(str2), this.f23031r, this.f23032s);
                    int B = B(K2, list2, list);
                    for (int B2 = B(K, list2, list); B2 < B; B2++) {
                        list.get(B2).add(D);
                    }
                    return;
                }
                sb = new StringBuilder();
            }
            sb.append("Skipping invalid timing: ");
        }
        sb.append(str);
        p.i("SsaDecoder", sb.toString());
    }

    private void G(z zVar, List<List<p5.b>> list, List<Long> list2, Charset charset) {
        b bVar = this.f23029o ? this.f23030p : null;
        while (true) {
            String t8 = zVar.t(charset);
            if (t8 == null) {
                return;
            }
            if (t8.startsWith("Format:")) {
                bVar = b.a(t8);
            } else if (t8.startsWith("Dialogue:")) {
                if (bVar == null) {
                    p.i("SsaDecoder", "Skipping dialogue line before complete format: " + t8);
                } else {
                    F(t8, bVar, list, list2);
                }
            }
        }
    }

    private void H(z zVar, Charset charset) {
        while (true) {
            String t8 = zVar.t(charset);
            if (t8 == null) {
                return;
            }
            if ("[Script Info]".equalsIgnoreCase(t8)) {
                I(zVar, charset);
            } else if ("[V4+ Styles]".equalsIgnoreCase(t8)) {
                this.q = J(zVar, charset);
            } else if ("[V4 Styles]".equalsIgnoreCase(t8)) {
                p.f("SsaDecoder", "[V4 Styles] are not supported");
            } else if ("[Events]".equalsIgnoreCase(t8)) {
                return;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0059 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:4:0x0006  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void I(b6.z r5, java.nio.charset.Charset r6) {
        /*
            r4 = this;
        L0:
            java.lang.String r0 = r5.t(r6)
            if (r0 == 0) goto L59
            int r1 = r5.a()
            if (r1 == 0) goto L14
            char r1 = r5.h(r6)
            r2 = 91
            if (r1 == r2) goto L59
        L14:
            java.lang.String r1 = ":"
            java.lang.String[] r0 = r0.split(r1)
            int r1 = r0.length
            r2 = 2
            if (r1 == r2) goto L1f
            goto L0
        L1f:
            r1 = 0
            r1 = r0[r1]
            java.lang.String r1 = r1.trim()
            java.lang.String r1 = com.google.common.base.c.e(r1)
            r1.hashCode()
            java.lang.String r2 = "playresx"
            boolean r2 = r1.equals(r2)
            r3 = 1
            if (r2 != 0) goto L4c
            java.lang.String r2 = "playresy"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto L3f
            goto L0
        L3f:
            r0 = r0[r3]     // Catch: java.lang.NumberFormatException -> L0
            java.lang.String r0 = r0.trim()     // Catch: java.lang.NumberFormatException -> L0
            float r0 = java.lang.Float.parseFloat(r0)     // Catch: java.lang.NumberFormatException -> L0
            r4.f23032s = r0     // Catch: java.lang.NumberFormatException -> L0
            goto L0
        L4c:
            r0 = r0[r3]     // Catch: java.lang.NumberFormatException -> L0
            java.lang.String r0 = r0.trim()     // Catch: java.lang.NumberFormatException -> L0
            float r0 = java.lang.Float.parseFloat(r0)     // Catch: java.lang.NumberFormatException -> L0
            r4.f23031r = r0     // Catch: java.lang.NumberFormatException -> L0
            goto L0
        L59:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: u5.a.I(b6.z, java.nio.charset.Charset):void");
    }

    private static Map<String, c> J(z zVar, Charset charset) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        c.a aVar = null;
        while (true) {
            String t8 = zVar.t(charset);
            if (t8 == null || (zVar.a() != 0 && zVar.h(charset) == '[')) {
                break;
            } else if (t8.startsWith("Format:")) {
                aVar = c.a.a(t8);
            } else if (t8.startsWith("Style:")) {
                if (aVar == null) {
                    p.i("SsaDecoder", "Skipping 'Style:' line before 'Format:' line: " + t8);
                } else {
                    c b9 = c.b(t8, aVar);
                    if (b9 != null) {
                        linkedHashMap.put(b9.f23038a, b9);
                    }
                }
            }
        }
        return linkedHashMap;
    }

    private static long K(String str) {
        Matcher matcher = f23028t.matcher(str.trim());
        if (matcher.matches()) {
            return (Long.parseLong((String) l0.j(matcher.group(1))) * 60 * 60 * 1000000) + (Long.parseLong((String) l0.j(matcher.group(2))) * 60 * 1000000) + (Long.parseLong((String) l0.j(matcher.group(3))) * 1000000) + (Long.parseLong((String) l0.j(matcher.group(4))) * 10000);
        }
        return -9223372036854775807L;
    }

    private static int L(int i8) {
        switch (i8) {
            case -1:
                return Integer.MIN_VALUE;
            case 0:
            default:
                p.i("SsaDecoder", "Unknown alignment: " + i8);
                return Integer.MIN_VALUE;
            case 1:
            case 2:
            case 3:
                return 2;
            case 4:
            case 5:
            case 6:
                return 1;
            case 7:
            case 8:
            case 9:
                return 0;
        }
    }

    private static int M(int i8) {
        switch (i8) {
            case -1:
                return Integer.MIN_VALUE;
            case 0:
            default:
                p.i("SsaDecoder", "Unknown alignment: " + i8);
                return Integer.MIN_VALUE;
            case 1:
            case 4:
            case 7:
                return 0;
            case 2:
            case 5:
            case 8:
                return 1;
            case 3:
            case 6:
            case 9:
                return 2;
        }
    }

    private static Layout.Alignment N(int i8) {
        switch (i8) {
            case -1:
                return null;
            case 0:
            default:
                p.i("SsaDecoder", "Unknown alignment: " + i8);
                return null;
            case 1:
            case 4:
            case 7:
                return Layout.Alignment.ALIGN_NORMAL;
            case 2:
            case 5:
            case 8:
                return Layout.Alignment.ALIGN_CENTER;
            case 3:
            case 6:
            case 9:
                return Layout.Alignment.ALIGN_OPPOSITE;
        }
    }

    @Override // p5.g
    protected h A(byte[] bArr, int i8, boolean z4) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        z zVar = new z(bArr, i8);
        Charset E = E(zVar);
        if (!this.f23029o) {
            H(zVar, E);
        }
        G(zVar, arrayList, arrayList2, E);
        return new d(arrayList, arrayList2);
    }
}
