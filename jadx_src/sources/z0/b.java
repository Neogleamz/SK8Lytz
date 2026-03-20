package z0;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b extends c {
    public static b h(ByteBuffer byteBuffer) {
        return i(byteBuffer, new b());
    }

    public static b i(ByteBuffer byteBuffer, b bVar) {
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return bVar.f(byteBuffer.getInt(byteBuffer.position()) + byteBuffer.position(), byteBuffer);
    }

    public b f(int i8, ByteBuffer byteBuffer) {
        g(i8, byteBuffer);
        return this;
    }

    public void g(int i8, ByteBuffer byteBuffer) {
        c(i8, byteBuffer);
    }

    public a j(a aVar, int i8) {
        int b9 = b(6);
        if (b9 != 0) {
            return aVar.f(a(d(b9) + (i8 * 4)), this.f24516b);
        }
        return null;
    }

    public int k() {
        int b9 = b(6);
        if (b9 != 0) {
            return e(b9);
        }
        return 0;
    }

    public int l() {
        int b9 = b(4);
        if (b9 != 0) {
            return this.f24516b.getInt(b9 + this.f24515a);
        }
        return 0;
    }
}
