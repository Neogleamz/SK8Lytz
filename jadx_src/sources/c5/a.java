package c5;

import a5.c;
import a5.e;
import b6.z;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import java.nio.ByteBuffer;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends e {
    @Override // a5.e
    protected Metadata b(c cVar, ByteBuffer byteBuffer) {
        return new Metadata(c(new z(byteBuffer.array(), byteBuffer.limit())));
    }

    public EventMessage c(z zVar) {
        return new EventMessage((String) b6.a.e(zVar.B()), (String) b6.a.e(zVar.B()), zVar.A(), zVar.A(), Arrays.copyOfRange(zVar.e(), zVar.f(), zVar.g()));
    }
}
