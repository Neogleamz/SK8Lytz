package androidx.camera.core.impl.utils;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class b extends FilterOutputStream {

    /* renamed from: a  reason: collision with root package name */
    final OutputStream f2623a;

    /* renamed from: b  reason: collision with root package name */
    private ByteOrder f2624b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(OutputStream outputStream, ByteOrder byteOrder) {
        super(outputStream);
        this.f2623a = outputStream;
        this.f2624b = byteOrder;
    }

    public void a(ByteOrder byteOrder) {
        this.f2624b = byteOrder;
    }

    public void b(int i8) {
        this.f2623a.write(i8);
    }

    public void c(int i8) {
        OutputStream outputStream;
        int i9;
        ByteOrder byteOrder = this.f2624b;
        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            this.f2623a.write((i8 >>> 0) & 255);
            this.f2623a.write((i8 >>> 8) & 255);
            this.f2623a.write((i8 >>> 16) & 255);
            outputStream = this.f2623a;
            i9 = i8 >>> 24;
        } else if (byteOrder != ByteOrder.BIG_ENDIAN) {
            return;
        } else {
            this.f2623a.write((i8 >>> 24) & 255);
            this.f2623a.write((i8 >>> 16) & 255);
            this.f2623a.write((i8 >>> 8) & 255);
            outputStream = this.f2623a;
            i9 = i8 >>> 0;
        }
        outputStream.write(i9 & 255);
    }

    public void d(short s8) {
        OutputStream outputStream;
        int i8;
        ByteOrder byteOrder = this.f2624b;
        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            this.f2623a.write((s8 >>> 0) & 255);
            outputStream = this.f2623a;
            i8 = s8 >>> 8;
        } else if (byteOrder != ByteOrder.BIG_ENDIAN) {
            return;
        } else {
            this.f2623a.write((s8 >>> 8) & 255);
            outputStream = this.f2623a;
            i8 = s8 >>> 0;
        }
        outputStream.write(i8 & 255);
    }

    public void f(long j8) {
        c((int) j8);
    }

    public void h(int i8) {
        d((short) i8);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr) {
        this.f2623a.write(bArr);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i8, int i9) {
        this.f2623a.write(bArr, i8, i9);
    }
}
