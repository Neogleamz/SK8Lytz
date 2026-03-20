package j5;

import b6.l0;
import com.google.android.exoplayer2.w0;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class l extends f {

    /* renamed from: j  reason: collision with root package name */
    private byte[] f20781j;

    /* renamed from: k  reason: collision with root package name */
    private volatile boolean f20782k;

    public l(a6.h hVar, com.google.android.exoplayer2.upstream.a aVar, int i8, w0 w0Var, int i9, Object obj, byte[] bArr) {
        super(hVar, aVar, i8, w0Var, i9, obj, -9223372036854775807L, -9223372036854775807L);
        l lVar;
        byte[] bArr2;
        if (bArr == null) {
            bArr2 = l0.f8068f;
            lVar = this;
        } else {
            lVar = this;
            bArr2 = bArr;
        }
        lVar.f20781j = bArr2;
    }

    private void i(int i8) {
        byte[] bArr = this.f20781j;
        if (bArr.length < i8 + 16384) {
            this.f20781j = Arrays.copyOf(bArr, bArr.length + 16384);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.e
    public final void a() {
        try {
            this.f20749i.x(this.f20742b);
            int i8 = 0;
            int i9 = 0;
            while (i8 != -1 && !this.f20782k) {
                i(i9);
                i8 = this.f20749i.read(this.f20781j, i9, 16384);
                if (i8 != -1) {
                    i9 += i8;
                }
            }
            if (!this.f20782k) {
                g(this.f20781j, i9);
            }
        } finally {
            a6.j.a(this.f20749i);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.e
    public final void c() {
        this.f20782k = true;
    }

    protected abstract void g(byte[] bArr, int i8);

    public byte[] h() {
        return this.f20781j;
    }
}
