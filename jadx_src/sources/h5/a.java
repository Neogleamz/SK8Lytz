package h5;

import com.google.android.exoplayer2.source.m;
import n4.k;
import n4.l;
import n4.p;
import n4.y;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements m {

    /* renamed from: a  reason: collision with root package name */
    private final p f20267a;

    /* renamed from: b  reason: collision with root package name */
    private k f20268b;

    /* renamed from: c  reason: collision with root package name */
    private l f20269c;

    public a(p pVar) {
        this.f20267a = pVar;
    }

    @Override // com.google.android.exoplayer2.source.m
    public void c(long j8, long j9) {
        ((k) b6.a.e(this.f20268b)).c(j8, j9);
    }

    @Override // com.google.android.exoplayer2.source.m
    public int d(y yVar) {
        return ((k) b6.a.e(this.f20268b)).e((l) b6.a.e(this.f20269c), yVar);
    }

    @Override // com.google.android.exoplayer2.source.m
    public long e() {
        l lVar = this.f20269c;
        if (lVar != null) {
            return lVar.getPosition();
        }
        return -1L;
    }

    @Override // com.google.android.exoplayer2.source.m
    public void f() {
        k kVar = this.f20268b;
        if (kVar instanceof u4.f) {
            ((u4.f) kVar).j();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x003f, code lost:
        if (r6.getPosition() != r11) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0061, code lost:
        if (r6.getPosition() != r11) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0064, code lost:
        r1 = false;
     */
    @Override // com.google.android.exoplayer2.source.m
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void g(a6.f r8, android.net.Uri r9, java.util.Map<java.lang.String, java.util.List<java.lang.String>> r10, long r11, long r13, n4.m r15) {
        /*
            r7 = this;
            n4.e r6 = new n4.e
            r0 = r6
            r1 = r8
            r2 = r11
            r4 = r13
            r0.<init>(r1, r2, r4)
            r7.f20269c = r6
            n4.k r8 = r7.f20268b
            if (r8 == 0) goto L10
            return
        L10:
            n4.p r8 = r7.f20267a
            n4.k[] r8 = r8.c(r9, r10)
            int r10 = r8.length
            r13 = 0
            r14 = 1
            if (r10 != r14) goto L20
            r8 = r8[r13]
            r7.f20268b = r8
            goto L74
        L20:
            int r10 = r8.length
            r0 = r13
        L22:
            if (r0 >= r10) goto L70
            r1 = r8[r0]
            boolean r2 = r1.g(r6)     // Catch: java.lang.Throwable -> L42 java.io.EOFException -> L57
            if (r2 == 0) goto L35
            r7.f20268b = r1     // Catch: java.lang.Throwable -> L42 java.io.EOFException -> L57
            b6.a.f(r14)
            r6.h()
            goto L70
        L35:
            n4.k r1 = r7.f20268b
            if (r1 != 0) goto L66
            long r1 = r6.getPosition()
            int r1 = (r1 > r11 ? 1 : (r1 == r11 ? 0 : -1))
            if (r1 != 0) goto L64
            goto L66
        L42:
            r8 = move-exception
            n4.k r9 = r7.f20268b
            if (r9 != 0) goto L4f
            long r9 = r6.getPosition()
            int r9 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r9 != 0) goto L50
        L4f:
            r13 = r14
        L50:
            b6.a.f(r13)
            r6.h()
            throw r8
        L57:
            n4.k r1 = r7.f20268b
            if (r1 != 0) goto L66
            long r1 = r6.getPosition()
            int r1 = (r1 > r11 ? 1 : (r1 == r11 ? 0 : -1))
            if (r1 != 0) goto L64
            goto L66
        L64:
            r1 = r13
            goto L67
        L66:
            r1 = r14
        L67:
            b6.a.f(r1)
            r6.h()
            int r0 = r0 + 1
            goto L22
        L70:
            n4.k r10 = r7.f20268b
            if (r10 == 0) goto L7a
        L74:
            n4.k r8 = r7.f20268b
            r8.b(r15)
            return
        L7a:
            com.google.android.exoplayer2.source.UnrecognizedInputFormatException r10 = new com.google.android.exoplayer2.source.UnrecognizedInputFormatException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "None of the available extractors ("
            r11.append(r12)
            java.lang.String r8 = b6.l0.M(r8)
            r11.append(r8)
            java.lang.String r8 = ") could read the stream."
            r11.append(r8)
            java.lang.String r8 = r11.toString()
            java.lang.Object r9 = b6.a.e(r9)
            android.net.Uri r9 = (android.net.Uri) r9
            r10.<init>(r8, r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: h5.a.g(a6.f, android.net.Uri, java.util.Map, long, long, n4.m):void");
    }

    @Override // com.google.android.exoplayer2.source.m
    public void release() {
        k kVar = this.f20268b;
        if (kVar != null) {
            kVar.release();
            this.f20268b = null;
        }
        this.f20269c = null;
    }
}
