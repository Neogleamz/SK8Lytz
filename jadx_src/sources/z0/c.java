package z0;

import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {

    /* renamed from: a  reason: collision with root package name */
    protected int f24515a;

    /* renamed from: b  reason: collision with root package name */
    protected ByteBuffer f24516b;

    /* renamed from: c  reason: collision with root package name */
    private int f24517c;

    /* renamed from: d  reason: collision with root package name */
    private int f24518d;

    /* renamed from: e  reason: collision with root package name */
    d f24519e = d.a();

    /* JADX INFO: Access modifiers changed from: protected */
    public int a(int i8) {
        return i8 + this.f24516b.getInt(i8);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int b(int i8) {
        if (i8 < this.f24518d) {
            return this.f24516b.getShort(this.f24517c + i8);
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void c(int i8, ByteBuffer byteBuffer) {
        short s8;
        this.f24516b = byteBuffer;
        if (byteBuffer != null) {
            this.f24515a = i8;
            int i9 = i8 - byteBuffer.getInt(i8);
            this.f24517c = i9;
            s8 = this.f24516b.getShort(i9);
        } else {
            s8 = 0;
            this.f24515a = 0;
            this.f24517c = 0;
        }
        this.f24518d = s8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int d(int i8) {
        int i9 = i8 + this.f24515a;
        return i9 + this.f24516b.getInt(i9) + 4;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int e(int i8) {
        int i9 = i8 + this.f24515a;
        return this.f24516b.getInt(i9 + this.f24516b.getInt(i9));
    }
}
