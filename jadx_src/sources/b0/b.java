package b0;

import java.io.EOFException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b extends OutputStream {

    /* renamed from: a  reason: collision with root package name */
    private final ByteBuffer f7919a;

    public b(ByteBuffer byteBuffer) {
        this.f7919a = byteBuffer;
    }

    @Override // java.io.OutputStream
    public void write(int i8) {
        if (!this.f7919a.hasRemaining()) {
            throw new EOFException("Output ByteBuffer has no bytes remaining.");
        }
        this.f7919a.put((byte) i8);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i8, int i9) {
        int i10;
        Objects.requireNonNull(bArr);
        if (i8 < 0 || i8 > bArr.length || i9 < 0 || (i10 = i8 + i9) > bArr.length || i10 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i9 == 0) {
            return;
        }
        if (this.f7919a.remaining() < i9) {
            throw new EOFException("Output ByteBuffer has insufficient bytes remaining.");
        }
        this.f7919a.put(bArr, i8, i9);
    }
}
