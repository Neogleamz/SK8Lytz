package w3;

import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p implements u3.h {

    /* renamed from: a  reason: collision with root package name */
    private final Set<u3.c> f23515a;

    /* renamed from: b  reason: collision with root package name */
    private final o f23516b;

    /* renamed from: c  reason: collision with root package name */
    private final s f23517c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(Set<u3.c> set, o oVar, s sVar) {
        this.f23515a = set;
        this.f23516b = oVar;
        this.f23517c = sVar;
    }

    @Override // u3.h
    public <T> u3.g<T> a(String str, Class<T> cls, u3.c cVar, u3.f<T, byte[]> fVar) {
        if (this.f23515a.contains(cVar)) {
            return new r(this.f23516b, str, cVar, fVar, this.f23517c);
        }
        throw new IllegalArgumentException(String.format("%s is not supported byt this factory. Supported encodings are: %s.", cVar, this.f23515a));
    }
}
