package androidx.emoji2.text;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class l {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a implements c {

        /* renamed from: a  reason: collision with root package name */
        private final ByteBuffer f5291a;

        a(ByteBuffer byteBuffer) {
            this.f5291a = byteBuffer;
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
        }

        @Override // androidx.emoji2.text.l.c
        public void a(int i8) {
            ByteBuffer byteBuffer = this.f5291a;
            byteBuffer.position(byteBuffer.position() + i8);
        }

        @Override // androidx.emoji2.text.l.c
        public int b() {
            return this.f5291a.getInt();
        }

        @Override // androidx.emoji2.text.l.c
        public long c() {
            return l.c(this.f5291a.getInt());
        }

        @Override // androidx.emoji2.text.l.c
        public long getPosition() {
            return this.f5291a.position();
        }

        @Override // androidx.emoji2.text.l.c
        public int readUnsignedShort() {
            return l.d(this.f5291a.getShort());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        private final long f5292a;

        /* renamed from: b  reason: collision with root package name */
        private final long f5293b;

        b(long j8, long j9) {
            this.f5292a = j8;
            this.f5293b = j9;
        }

        long a() {
            return this.f5292a;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        void a(int i8);

        int b();

        long c();

        long getPosition();

        int readUnsignedShort();
    }

    private static b a(c cVar) {
        long j8;
        cVar.a(4);
        int readUnsignedShort = cVar.readUnsignedShort();
        if (readUnsignedShort <= 100) {
            cVar.a(6);
            int i8 = 0;
            while (true) {
                if (i8 >= readUnsignedShort) {
                    j8 = -1;
                    break;
                }
                int b9 = cVar.b();
                cVar.a(4);
                j8 = cVar.c();
                cVar.a(4);
                if (1835365473 == b9) {
                    break;
                }
                i8++;
            }
            if (j8 != -1) {
                cVar.a((int) (j8 - cVar.getPosition()));
                cVar.a(12);
                long c9 = cVar.c();
                for (int i9 = 0; i9 < c9; i9++) {
                    int b10 = cVar.b();
                    long c10 = cVar.c();
                    long c11 = cVar.c();
                    if (1164798569 == b10 || 1701669481 == b10) {
                        return new b(c10 + j8, c11);
                    }
                }
            }
            throw new IOException("Cannot read metadata.");
        }
        throw new IOException("Cannot read metadata.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static z0.b b(ByteBuffer byteBuffer) {
        ByteBuffer duplicate = byteBuffer.duplicate();
        duplicate.position((int) a(new a(duplicate)).a());
        return z0.b.h(duplicate);
    }

    static long c(int i8) {
        return i8 & 4294967295L;
    }

    static int d(short s8) {
        return s8 & 65535;
    }
}
