package a6;

import android.net.Uri;
import android.util.Base64;
import b6.l0;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.upstream.DataSourceException;
import java.net.URLDecoder;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g extends e {

    /* renamed from: e  reason: collision with root package name */
    private com.google.android.exoplayer2.upstream.a f88e;

    /* renamed from: f  reason: collision with root package name */
    private byte[] f89f;

    /* renamed from: g  reason: collision with root package name */
    private int f90g;

    /* renamed from: h  reason: collision with root package name */
    private int f91h;

    public g() {
        super(false);
    }

    @Override // a6.h
    public void close() {
        if (this.f89f != null) {
            this.f89f = null;
            m();
        }
        this.f88e = null;
    }

    @Override // a6.f
    public int read(byte[] bArr, int i8, int i9) {
        if (i9 == 0) {
            return 0;
        }
        int i10 = this.f91h;
        if (i10 == 0) {
            return -1;
        }
        int min = Math.min(i9, i10);
        System.arraycopy(l0.j(this.f89f), this.f90g, bArr, i8, min);
        this.f90g += min;
        this.f91h -= min;
        l(min);
        return min;
    }

    @Override // a6.h
    public Uri v() {
        com.google.android.exoplayer2.upstream.a aVar = this.f88e;
        if (aVar != null) {
            return aVar.f10942a;
        }
        return null;
    }

    @Override // a6.h
    public long x(com.google.android.exoplayer2.upstream.a aVar) {
        n(aVar);
        this.f88e = aVar;
        Uri uri = aVar.f10942a;
        String scheme = uri.getScheme();
        boolean equals = "data".equals(scheme);
        b6.a.b(equals, "Unsupported scheme: " + scheme);
        String[] R0 = l0.R0(uri.getSchemeSpecificPart(), ",");
        if (R0.length != 2) {
            throw ParserException.b("Unexpected URI format: " + uri, null);
        }
        String str = R0[1];
        if (R0[0].contains(";base64")) {
            try {
                this.f89f = Base64.decode(str, 0);
            } catch (IllegalArgumentException e8) {
                throw ParserException.b("Error while parsing Base64 encoded string: " + str, e8);
            }
        } else {
            this.f89f = l0.m0(URLDecoder.decode(str, com.google.common.base.e.f18815a.name()));
        }
        long j8 = aVar.f10948g;
        byte[] bArr = this.f89f;
        if (j8 > bArr.length) {
            this.f89f = null;
            throw new DataSourceException(2008);
        }
        int i8 = (int) j8;
        this.f90g = i8;
        int length = bArr.length - i8;
        this.f91h = length;
        long j9 = aVar.f10949h;
        if (j9 != -1) {
            this.f91h = (int) Math.min(length, j9);
        }
        o(aVar);
        long j10 = aVar.f10949h;
        return j10 != -1 ? j10 : this.f91h;
    }
}
