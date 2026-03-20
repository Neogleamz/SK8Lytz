package y5;

import android.text.TextUtils;
import b6.z;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import java.util.ArrayList;
import p5.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h extends g {

    /* renamed from: o  reason: collision with root package name */
    private final z f24471o;

    /* renamed from: p  reason: collision with root package name */
    private final c f24472p;

    public h() {
        super("WebvttDecoder");
        this.f24471o = new z();
        this.f24472p = new c();
    }

    private static int B(z zVar) {
        int i8 = 0;
        int i9 = -1;
        while (i9 == -1) {
            i8 = zVar.f();
            String s8 = zVar.s();
            i9 = s8 == null ? 0 : "STYLE".equals(s8) ? 2 : s8.startsWith("NOTE") ? 1 : 3;
        }
        zVar.U(i8);
        return i9;
    }

    private static void C(z zVar) {
        do {
        } while (!TextUtils.isEmpty(zVar.s()));
    }

    @Override // p5.g
    protected p5.h A(byte[] bArr, int i8, boolean z4) {
        e m8;
        this.f24471o.S(bArr, i8);
        ArrayList arrayList = new ArrayList();
        try {
            i.e(this.f24471o);
            do {
            } while (!TextUtils.isEmpty(this.f24471o.s()));
            ArrayList arrayList2 = new ArrayList();
            while (true) {
                int B = B(this.f24471o);
                if (B == 0) {
                    return new k(arrayList2);
                }
                if (B == 1) {
                    C(this.f24471o);
                } else if (B == 2) {
                    if (!arrayList2.isEmpty()) {
                        throw new SubtitleDecoderException("A style block was found after the first cue.");
                    }
                    this.f24471o.s();
                    arrayList.addAll(this.f24472p.d(this.f24471o));
                } else if (B == 3 && (m8 = f.m(this.f24471o, arrayList)) != null) {
                    arrayList2.add(m8);
                }
            }
        } catch (ParserException e8) {
            throw new SubtitleDecoderException(e8);
        }
    }
}
