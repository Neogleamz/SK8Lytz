package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.c;
import java.io.FileNotFoundException;
import java.io.IOException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b implements c {

    /* renamed from: a  reason: collision with root package name */
    private final int f10963a;

    public b() {
        this(-1);
    }

    public b(int i8) {
        this.f10963a = i8;
    }

    @Override // com.google.android.exoplayer2.upstream.c
    public long a(c.C0117c c0117c) {
        IOException iOException = c0117c.f10972c;
        if ((iOException instanceof ParserException) || (iOException instanceof FileNotFoundException) || (iOException instanceof HttpDataSource$CleartextNotPermittedException) || (iOException instanceof Loader.UnexpectedLoaderException) || DataSourceException.a(iOException)) {
            return -9223372036854775807L;
        }
        return Math.min((c0117c.f10973d - 1) * 1000, 5000);
    }

    @Override // com.google.android.exoplayer2.upstream.c
    public c.b b(c.a aVar, c.C0117c c0117c) {
        if (e(c0117c.f10972c)) {
            if (aVar.a(1)) {
                return new c.b(1, 300000L);
            }
            if (aVar.a(2)) {
                return new c.b(2, 60000L);
            }
            return null;
        }
        return null;
    }

    @Override // com.google.android.exoplayer2.upstream.c
    public int d(int i8) {
        int i9 = this.f10963a;
        return i9 == -1 ? i8 == 7 ? 6 : 3 : i9;
    }

    protected boolean e(IOException iOException) {
        if (iOException instanceof HttpDataSource$InvalidResponseCodeException) {
            int i8 = ((HttpDataSource$InvalidResponseCodeException) iOException).f10902d;
            return i8 == 403 || i8 == 404 || i8 == 410 || i8 == 416 || i8 == 500 || i8 == 503;
        }
        return false;
    }
}
