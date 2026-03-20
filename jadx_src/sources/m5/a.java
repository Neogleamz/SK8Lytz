package m5;

import a6.y;
import android.net.Uri;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class a implements a6.h {

    /* renamed from: a  reason: collision with root package name */
    private final a6.h f21838a;

    /* renamed from: b  reason: collision with root package name */
    private final byte[] f21839b;

    /* renamed from: c  reason: collision with root package name */
    private final byte[] f21840c;

    /* renamed from: d  reason: collision with root package name */
    private CipherInputStream f21841d;

    public a(a6.h hVar, byte[] bArr, byte[] bArr2) {
        this.f21838a = hVar;
        this.f21839b = bArr;
        this.f21840c = bArr2;
    }

    @Override // a6.h
    public void close() {
        if (this.f21841d != null) {
            this.f21841d = null;
            this.f21838a.close();
        }
    }

    protected Cipher l() {
        return Cipher.getInstance("AES/CBC/PKCS7Padding");
    }

    @Override // a6.f
    public final int read(byte[] bArr, int i8, int i9) {
        b6.a.e(this.f21841d);
        int read = this.f21841d.read(bArr, i8, i9);
        if (read < 0) {
            return -1;
        }
        return read;
    }

    @Override // a6.h
    public final Uri v() {
        return this.f21838a.v();
    }

    @Override // a6.h
    public final void w(y yVar) {
        b6.a.e(yVar);
        this.f21838a.w(yVar);
    }

    @Override // a6.h
    public final long x(com.google.android.exoplayer2.upstream.a aVar) {
        try {
            Cipher l8 = l();
            try {
                l8.init(2, new SecretKeySpec(this.f21839b, "AES"), new IvParameterSpec(this.f21840c));
                a6.i iVar = new a6.i(this.f21838a, aVar);
                this.f21841d = new CipherInputStream(iVar, l8);
                iVar.b();
                return -1L;
            } catch (InvalidAlgorithmParameterException | InvalidKeyException e8) {
                throw new RuntimeException(e8);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e9) {
            throw new RuntimeException(e9);
        }
    }

    @Override // a6.h
    public final Map<String, List<String>> y() {
        return this.f21838a.y();
    }
}
