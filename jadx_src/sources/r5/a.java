package r5;

import b6.z;
import java.util.List;
import p5.g;
import p5.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends g {

    /* renamed from: o  reason: collision with root package name */
    private final b f22670o;

    public a(List<byte[]> list) {
        super("DvbDecoder");
        z zVar = new z(list.get(0));
        this.f22670o = new b(zVar.N(), zVar.N());
    }

    @Override // p5.g
    protected h A(byte[] bArr, int i8, boolean z4) {
        if (z4) {
            this.f22670o.r();
        }
        return new c(this.f22670o.b(bArr, i8));
    }
}
