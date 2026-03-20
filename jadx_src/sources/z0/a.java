package z0;

import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends c {
    public a f(int i8, ByteBuffer byteBuffer) {
        g(i8, byteBuffer);
        return this;
    }

    public void g(int i8, ByteBuffer byteBuffer) {
        c(i8, byteBuffer);
    }

    public int h(int i8) {
        int b9 = b(16);
        if (b9 != 0) {
            return this.f24516b.getInt(d(b9) + (i8 * 4));
        }
        return 0;
    }

    public int i() {
        int b9 = b(16);
        if (b9 != 0) {
            return e(b9);
        }
        return 0;
    }

    public boolean j() {
        int b9 = b(6);
        return (b9 == 0 || this.f24516b.get(b9 + this.f24515a) == 0) ? false : true;
    }

    public short k() {
        int b9 = b(14);
        if (b9 != 0) {
            return this.f24516b.getShort(b9 + this.f24515a);
        }
        return (short) 0;
    }

    public int l() {
        int b9 = b(4);
        if (b9 != 0) {
            return this.f24516b.getInt(b9 + this.f24515a);
        }
        return 0;
    }

    public short m() {
        int b9 = b(8);
        if (b9 != 0) {
            return this.f24516b.getShort(b9 + this.f24515a);
        }
        return (short) 0;
    }

    public short n() {
        int b9 = b(12);
        if (b9 != 0) {
            return this.f24516b.getShort(b9 + this.f24515a);
        }
        return (short) 0;
    }
}
