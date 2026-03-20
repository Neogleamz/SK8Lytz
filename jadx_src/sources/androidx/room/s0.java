package androidx.room;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Callable;
import t1.c;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class s0 implements c.InterfaceC0207c {

    /* renamed from: a  reason: collision with root package name */
    private final String f7192a;

    /* renamed from: b  reason: collision with root package name */
    private final File f7193b;

    /* renamed from: c  reason: collision with root package name */
    private final Callable<InputStream> f7194c;

    /* renamed from: d  reason: collision with root package name */
    private final c.InterfaceC0207c f7195d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s0(String str, File file, Callable<InputStream> callable, c.InterfaceC0207c interfaceC0207c) {
        this.f7192a = str;
        this.f7193b = file;
        this.f7194c = callable;
        this.f7195d = interfaceC0207c;
    }

    @Override // t1.c.InterfaceC0207c
    public t1.c a(c.b bVar) {
        return new r0(bVar.f22837a, this.f7192a, this.f7193b, this.f7194c, bVar.f22839c.f22836a, this.f7195d.a(bVar));
    }
}
