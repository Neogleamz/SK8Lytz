package w3;

import java.util.Arrays;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h {

    /* renamed from: a  reason: collision with root package name */
    private final u3.c f23509a;

    /* renamed from: b  reason: collision with root package name */
    private final byte[] f23510b;

    public h(u3.c cVar, byte[] bArr) {
        Objects.requireNonNull(cVar, "encoding is null");
        Objects.requireNonNull(bArr, "bytes is null");
        this.f23509a = cVar;
        this.f23510b = bArr;
    }

    public byte[] a() {
        return this.f23510b;
    }

    public u3.c b() {
        return this.f23509a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof h) {
            h hVar = (h) obj;
            if (this.f23509a.equals(hVar.f23509a)) {
                return Arrays.equals(this.f23510b, hVar.f23510b);
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return ((this.f23509a.hashCode() ^ 1000003) * 1000003) ^ Arrays.hashCode(this.f23510b);
    }

    public String toString() {
        return "EncodedPayload{encoding=" + this.f23509a + ", bytes=[...]}";
    }
}
