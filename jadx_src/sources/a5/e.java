package a5;

import com.google.android.exoplayer2.metadata.Metadata;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class e implements a {
    @Override // a5.a
    public final Metadata a(c cVar) {
        ByteBuffer byteBuffer = (ByteBuffer) b6.a.e(cVar.f9512c);
        b6.a.a(byteBuffer.position() == 0 && byteBuffer.hasArray() && byteBuffer.arrayOffset() == 0);
        if (cVar.s()) {
            return null;
        }
        return b(cVar, byteBuffer);
    }

    protected abstract Metadata b(c cVar, ByteBuffer byteBuffer);
}
