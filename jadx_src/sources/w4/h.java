package w4;

import b6.z;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import k4.v;
import n4.e0;
import w4.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h extends i {

    /* renamed from: o  reason: collision with root package name */
    private static final byte[] f23572o = {79, 112, 117, 115, 72, 101, 97, 100};

    /* renamed from: p  reason: collision with root package name */
    private static final byte[] f23573p = {79, 112, 117, 115, 84, 97, 103, 115};

    /* renamed from: n  reason: collision with root package name */
    private boolean f23574n;

    private static boolean n(z zVar, byte[] bArr) {
        if (zVar.a() < bArr.length) {
            return false;
        }
        int f5 = zVar.f();
        byte[] bArr2 = new byte[bArr.length];
        zVar.l(bArr2, 0, bArr.length);
        zVar.U(f5);
        return Arrays.equals(bArr2, bArr);
    }

    public static boolean o(z zVar) {
        return n(zVar, f23572o);
    }

    @Override // w4.i
    protected long f(z zVar) {
        return c(v.e(zVar.e()));
    }

    @Override // w4.i
    protected boolean h(z zVar, long j8, i.b bVar) {
        w0.b Z;
        if (n(zVar, f23572o)) {
            byte[] copyOf = Arrays.copyOf(zVar.e(), zVar.g());
            int c9 = v.c(copyOf);
            List<byte[]> a9 = v.a(copyOf);
            if (bVar.f23588a != null) {
                return true;
            }
            Z = new w0.b().g0("audio/opus").J(c9).h0(48000).V(a9);
        } else {
            byte[] bArr = f23573p;
            if (!n(zVar, bArr)) {
                b6.a.h(bVar.f23588a);
                return false;
            }
            b6.a.h(bVar.f23588a);
            if (this.f23574n) {
                return true;
            }
            this.f23574n = true;
            zVar.V(bArr.length);
            Metadata c10 = e0.c(ImmutableList.y(e0.j(zVar, false, false).f22089b));
            if (c10 == null) {
                return true;
            }
            Z = bVar.f23588a.b().Z(c10.b(bVar.f23588a.f11205k));
        }
        bVar.f23588a = Z.G();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // w4.i
    public void l(boolean z4) {
        super.l(z4);
        if (z4) {
            this.f23574n = false;
        }
    }
}
