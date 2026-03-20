package u7;

import android.graphics.Typeface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends f {

    /* renamed from: a  reason: collision with root package name */
    private final Typeface f23090a;

    /* renamed from: b  reason: collision with root package name */
    private final InterfaceC0213a f23091b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f23092c;

    /* renamed from: u7.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface InterfaceC0213a {
        void a(Typeface typeface);
    }

    public a(InterfaceC0213a interfaceC0213a, Typeface typeface) {
        this.f23090a = typeface;
        this.f23091b = interfaceC0213a;
    }

    private void d(Typeface typeface) {
        if (this.f23092c) {
            return;
        }
        this.f23091b.a(typeface);
    }

    @Override // u7.f
    public void a(int i8) {
        d(this.f23090a);
    }

    @Override // u7.f
    public void b(Typeface typeface, boolean z4) {
        d(typeface);
    }

    public void c() {
        this.f23092c = true;
    }
}
