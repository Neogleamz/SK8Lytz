package m5;

import android.text.TextUtils;
import b6.h0;
import b6.z;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import n4.b0;
import n4.m;
import n4.y;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r implements n4.k {

    /* renamed from: g  reason: collision with root package name */
    private static final Pattern f21962g = Pattern.compile("LOCAL:([^,]+)");

    /* renamed from: h  reason: collision with root package name */
    private static final Pattern f21963h = Pattern.compile("MPEGTS:(-?\\d+)");

    /* renamed from: a  reason: collision with root package name */
    private final String f21964a;

    /* renamed from: b  reason: collision with root package name */
    private final h0 f21965b;

    /* renamed from: d  reason: collision with root package name */
    private m f21967d;

    /* renamed from: f  reason: collision with root package name */
    private int f21969f;

    /* renamed from: c  reason: collision with root package name */
    private final z f21966c = new z();

    /* renamed from: e  reason: collision with root package name */
    private byte[] f21968e = new byte[RecognitionOptions.UPC_E];

    public r(String str, h0 h0Var) {
        this.f21964a = str;
        this.f21965b = h0Var;
    }

    private b0 a(long j8) {
        b0 e8 = this.f21967d.e(0, 3);
        e8.f(new w0.b().g0("text/vtt").X(this.f21964a).k0(j8).G());
        this.f21967d.o();
        return e8;
    }

    private void d() {
        z zVar = new z(this.f21968e);
        y5.i.e(zVar);
        long j8 = 0;
        long j9 = 0;
        for (String s8 = zVar.s(); !TextUtils.isEmpty(s8); s8 = zVar.s()) {
            if (s8.startsWith("X-TIMESTAMP-MAP")) {
                Matcher matcher = f21962g.matcher(s8);
                if (!matcher.find()) {
                    throw ParserException.a("X-TIMESTAMP-MAP doesn't contain local timestamp: " + s8, null);
                }
                Matcher matcher2 = f21963h.matcher(s8);
                if (!matcher2.find()) {
                    throw ParserException.a("X-TIMESTAMP-MAP doesn't contain media timestamp: " + s8, null);
                }
                j9 = y5.i.d((String) b6.a.e(matcher.group(1)));
                j8 = h0.f(Long.parseLong((String) b6.a.e(matcher2.group(1))));
            }
        }
        Matcher a9 = y5.i.a(zVar);
        if (a9 == null) {
            a(0L);
            return;
        }
        long d8 = y5.i.d((String) b6.a.e(a9.group(1)));
        long b9 = this.f21965b.b(h0.j((j8 + d8) - j9));
        b0 a10 = a(b9 - d8);
        this.f21966c.S(this.f21968e, this.f21969f);
        a10.b(this.f21966c, this.f21969f);
        a10.d(b9, 1, this.f21969f, 0, null);
    }

    @Override // n4.k
    public void b(m mVar) {
        this.f21967d = mVar;
        mVar.m(new z.b(-9223372036854775807L));
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        throw new IllegalStateException();
    }

    @Override // n4.k
    public int e(n4.l lVar, y yVar) {
        b6.a.e(this.f21967d);
        int b9 = (int) lVar.b();
        int i8 = this.f21969f;
        byte[] bArr = this.f21968e;
        if (i8 == bArr.length) {
            this.f21968e = Arrays.copyOf(bArr, ((b9 != -1 ? b9 : bArr.length) * 3) / 2);
        }
        byte[] bArr2 = this.f21968e;
        int i9 = this.f21969f;
        int read = lVar.read(bArr2, i9, bArr2.length - i9);
        if (read != -1) {
            int i10 = this.f21969f + read;
            this.f21969f = i10;
            if (b9 == -1 || i10 != b9) {
                return 0;
            }
        }
        d();
        return -1;
    }

    @Override // n4.k
    public boolean g(n4.l lVar) {
        lVar.d(this.f21968e, 0, 6, false);
        this.f21966c.S(this.f21968e, 6);
        if (y5.i.b(this.f21966c)) {
            return true;
        }
        lVar.d(this.f21968e, 6, 3, false);
        this.f21966c.S(this.f21968e, 9);
        return y5.i.b(this.f21966c);
    }

    @Override // n4.k
    public void release() {
    }
}
