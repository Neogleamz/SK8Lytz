package com.google.android.exoplayer2.extractor.flv;

import b6.z;
import com.google.android.exoplayer2.extractor.flv.TagPayloadReader;
import com.google.android.exoplayer2.w0;
import java.util.Collections;
import k4.a;
import n4.b0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a extends TagPayloadReader {

    /* renamed from: e  reason: collision with root package name */
    private static final int[] f9665e = {5512, 11025, 22050, 44100};

    /* renamed from: b  reason: collision with root package name */
    private boolean f9666b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f9667c;

    /* renamed from: d  reason: collision with root package name */
    private int f9668d;

    public a(b0 b0Var) {
        super(b0Var);
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    protected boolean b(z zVar) {
        w0.b h02;
        if (this.f9666b) {
            zVar.V(1);
        } else {
            int H = zVar.H();
            int i8 = (H >> 4) & 15;
            this.f9668d = i8;
            if (i8 == 2) {
                h02 = new w0.b().g0("audio/mpeg").J(1).h0(f9665e[(H >> 2) & 3]);
            } else if (i8 == 7 || i8 == 8) {
                h02 = new w0.b().g0(i8 == 7 ? "audio/g711-alaw" : "audio/g711-mlaw").J(1).h0(8000);
            } else {
                if (i8 != 10) {
                    throw new TagPayloadReader.UnsupportedFormatException("Audio format not supported: " + this.f9668d);
                }
                this.f9666b = true;
            }
            this.f9664a.f(h02.G());
            this.f9667c = true;
            this.f9666b = true;
        }
        return true;
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    protected boolean c(z zVar, long j8) {
        if (this.f9668d == 2) {
            int a9 = zVar.a();
            this.f9664a.b(zVar, a9);
            this.f9664a.d(j8, 1, a9, 0, null);
            return true;
        }
        int H = zVar.H();
        if (H != 0 || this.f9667c) {
            if (this.f9668d != 10 || H == 1) {
                int a10 = zVar.a();
                this.f9664a.b(zVar, a10);
                this.f9664a.d(j8, 1, a10, 0, null);
                return true;
            }
            return false;
        }
        int a11 = zVar.a();
        byte[] bArr = new byte[a11];
        zVar.l(bArr, 0, a11);
        a.b f5 = k4.a.f(bArr);
        this.f9664a.f(new w0.b().g0("audio/mp4a-latm").K(f5.f20983c).J(f5.f20982b).h0(f5.f20981a).V(Collections.singletonList(bArr)).G());
        this.f9667c = true;
        return false;
    }
}
