package e4;

import java.util.Objects;
import w3.o;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b extends k {

    /* renamed from: a  reason: collision with root package name */
    private final long f19762a;

    /* renamed from: b  reason: collision with root package name */
    private final o f19763b;

    /* renamed from: c  reason: collision with root package name */
    private final w3.i f19764c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(long j8, o oVar, w3.i iVar) {
        this.f19762a = j8;
        Objects.requireNonNull(oVar, "Null transportContext");
        this.f19763b = oVar;
        Objects.requireNonNull(iVar, "Null event");
        this.f19764c = iVar;
    }

    @Override // e4.k
    public w3.i b() {
        return this.f19764c;
    }

    @Override // e4.k
    public long c() {
        return this.f19762a;
    }

    @Override // e4.k
    public o d() {
        return this.f19763b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof k) {
            k kVar = (k) obj;
            return this.f19762a == kVar.c() && this.f19763b.equals(kVar.d()) && this.f19764c.equals(kVar.b());
        }
        return false;
    }

    public int hashCode() {
        long j8 = this.f19762a;
        return ((((((int) (j8 ^ (j8 >>> 32))) ^ 1000003) * 1000003) ^ this.f19763b.hashCode()) * 1000003) ^ this.f19764c.hashCode();
    }

    public String toString() {
        return "PersistedEvent{id=" + this.f19762a + ", transportContext=" + this.f19763b + ", event=" + this.f19764c + "}";
    }
}
