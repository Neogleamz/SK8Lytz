package n4;

import com.google.android.exoplayer2.metadata.Metadata;
import e5.b;
import java.io.EOFException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w {

    /* renamed from: a  reason: collision with root package name */
    private final b6.z f22147a = new b6.z(10);

    public Metadata a(l lVar, b.a aVar) {
        Metadata metadata = null;
        int i8 = 0;
        while (true) {
            try {
                lVar.k(this.f22147a.e(), 0, 10);
                this.f22147a.U(0);
                if (this.f22147a.K() != 4801587) {
                    break;
                }
                this.f22147a.V(3);
                int G = this.f22147a.G();
                int i9 = G + 10;
                if (metadata == null) {
                    byte[] bArr = new byte[i9];
                    System.arraycopy(this.f22147a.e(), 0, bArr, 0, 10);
                    lVar.k(bArr, 10, G);
                    metadata = new e5.b(aVar).e(bArr, i9);
                } else {
                    lVar.f(G);
                }
                i8 += i9;
            } catch (EOFException unused) {
            }
        }
        lVar.h();
        lVar.f(i8);
        return metadata;
    }
}
