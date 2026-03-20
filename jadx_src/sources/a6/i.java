package a6;

import java.io.InputStream;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i extends InputStream {

    /* renamed from: a  reason: collision with root package name */
    private final h f92a;

    /* renamed from: b  reason: collision with root package name */
    private final com.google.android.exoplayer2.upstream.a f93b;

    /* renamed from: f  reason: collision with root package name */
    private long f97f;

    /* renamed from: d  reason: collision with root package name */
    private boolean f95d = false;

    /* renamed from: e  reason: collision with root package name */
    private boolean f96e = false;

    /* renamed from: c  reason: collision with root package name */
    private final byte[] f94c = new byte[1];

    public i(h hVar, com.google.android.exoplayer2.upstream.a aVar) {
        this.f92a = hVar;
        this.f93b = aVar;
    }

    private void a() {
        if (this.f95d) {
            return;
        }
        this.f92a.x(this.f93b);
        this.f95d = true;
    }

    public void b() {
        a();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.f96e) {
            return;
        }
        this.f92a.close();
        this.f96e = true;
    }

    @Override // java.io.InputStream
    public int read() {
        if (read(this.f94c) == -1) {
            return -1;
        }
        return this.f94c[0] & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i8, int i9) {
        b6.a.f(!this.f96e);
        a();
        int read = this.f92a.read(bArr, i8, i9);
        if (read == -1) {
            return -1;
        }
        this.f97f += read;
        return read;
    }
}
