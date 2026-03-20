package androidx.room;

import t1.c;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l implements c.InterfaceC0207c {

    /* renamed from: a  reason: collision with root package name */
    private final c.InterfaceC0207c f7142a;

    /* renamed from: b  reason: collision with root package name */
    private final a f7143b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l(c.InterfaceC0207c interfaceC0207c, a aVar) {
        this.f7142a = interfaceC0207c;
        this.f7143b = aVar;
    }

    @Override // t1.c.InterfaceC0207c
    /* renamed from: b */
    public h a(c.b bVar) {
        return new h(this.f7142a.a(bVar), this.f7143b);
    }
}
