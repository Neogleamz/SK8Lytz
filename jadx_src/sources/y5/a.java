package y5;

import b6.l0;
import b6.z;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import java.util.ArrayList;
import java.util.Collections;
import p5.b;
import p5.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends g {

    /* renamed from: o  reason: collision with root package name */
    private final z f24422o;

    public a() {
        super("Mp4WebvttDecoder");
        this.f24422o = new z();
    }

    private static p5.b B(z zVar, int i8) {
        CharSequence charSequence = null;
        b.C0196b c0196b = null;
        while (i8 > 0) {
            if (i8 < 8) {
                throw new SubtitleDecoderException("Incomplete vtt cue box header found.");
            }
            int q = zVar.q();
            int q8 = zVar.q();
            int i9 = q - 8;
            String E = l0.E(zVar.e(), zVar.f(), i9);
            zVar.V(i9);
            i8 = (i8 - 8) - i9;
            if (q8 == 1937011815) {
                c0196b = f.o(E);
            } else if (q8 == 1885436268) {
                charSequence = f.q(null, E.trim(), Collections.emptyList());
            }
        }
        if (charSequence == null) {
            charSequence = BuildConfig.FLAVOR;
        }
        return c0196b != null ? c0196b.o(charSequence).a() : f.l(charSequence);
    }

    @Override // p5.g
    protected p5.h A(byte[] bArr, int i8, boolean z4) {
        this.f24422o.S(bArr, i8);
        ArrayList arrayList = new ArrayList();
        while (this.f24422o.a() > 0) {
            if (this.f24422o.a() < 8) {
                throw new SubtitleDecoderException("Incomplete Mp4Webvtt Top Level box header found.");
            }
            int q = this.f24422o.q();
            if (this.f24422o.q() == 1987343459) {
                arrayList.add(B(this.f24422o, q - 8));
            } else {
                this.f24422o.V(q - 8);
            }
        }
        return new b(arrayList);
    }
}
