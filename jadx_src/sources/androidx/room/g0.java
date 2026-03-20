package androidx.room;

import androidx.room.RoomDatabase;
import java.util.concurrent.Executor;
import t1.c;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g0 implements c.InterfaceC0207c {

    /* renamed from: a  reason: collision with root package name */
    private final c.InterfaceC0207c f7118a;

    /* renamed from: b  reason: collision with root package name */
    private final RoomDatabase.e f7119b;

    /* renamed from: c  reason: collision with root package name */
    private final Executor f7120c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g0(c.InterfaceC0207c interfaceC0207c, RoomDatabase.e eVar, Executor executor) {
        this.f7118a = interfaceC0207c;
        this.f7119b = eVar;
        this.f7120c = executor;
    }

    @Override // t1.c.InterfaceC0207c
    public t1.c a(c.b bVar) {
        return new f0(this.f7118a.a(bVar), this.f7119b, this.f7120c);
    }
}
