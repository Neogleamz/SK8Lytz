package n4;

import a6.f;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.EOFException;
import n4.b0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j implements b0 {

    /* renamed from: a  reason: collision with root package name */
    private final byte[] f22123a = new byte[RecognitionOptions.AZTEC];

    @Override // n4.b0
    public int a(f fVar, int i8, boolean z4, int i9) {
        int read = fVar.read(this.f22123a, 0, Math.min(this.f22123a.length, i8));
        if (read == -1) {
            if (z4) {
                return -1;
            }
            throw new EOFException();
        }
        return read;
    }

    @Override // n4.b0
    public void d(long j8, int i8, int i9, int i10, b0.a aVar) {
    }

    @Override // n4.b0
    public void e(b6.z zVar, int i8, int i9) {
        zVar.V(i8);
    }

    @Override // n4.b0
    public void f(w0 w0Var) {
    }
}
