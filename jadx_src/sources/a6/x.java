package a6;

import android.net.Uri;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x implements h {

    /* renamed from: a  reason: collision with root package name */
    private final h f189a;

    /* renamed from: b  reason: collision with root package name */
    private long f190b;

    /* renamed from: c  reason: collision with root package name */
    private Uri f191c = Uri.EMPTY;

    /* renamed from: d  reason: collision with root package name */
    private Map<String, List<String>> f192d = Collections.emptyMap();

    public x(h hVar) {
        this.f189a = (h) b6.a.e(hVar);
    }

    @Override // a6.h
    public void close() {
        this.f189a.close();
    }

    public long l() {
        return this.f190b;
    }

    public Uri m() {
        return this.f191c;
    }

    public Map<String, List<String>> n() {
        return this.f192d;
    }

    public void o() {
        this.f190b = 0L;
    }

    @Override // a6.f
    public int read(byte[] bArr, int i8, int i9) {
        int read = this.f189a.read(bArr, i8, i9);
        if (read != -1) {
            this.f190b += read;
        }
        return read;
    }

    @Override // a6.h
    public Uri v() {
        return this.f189a.v();
    }

    @Override // a6.h
    public void w(y yVar) {
        b6.a.e(yVar);
        this.f189a.w(yVar);
    }

    @Override // a6.h
    public long x(com.google.android.exoplayer2.upstream.a aVar) {
        this.f191c = aVar.f10942a;
        this.f192d = Collections.emptyMap();
        long x8 = this.f189a.x(aVar);
        this.f191c = (Uri) b6.a.e(v());
        this.f192d = y();
        return x8;
    }

    @Override // a6.h
    public Map<String, List<String>> y() {
        return this.f189a.y();
    }
}
