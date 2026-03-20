package n6;

import android.content.Intent;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class w extends x {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ Intent f22208a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ l6.f f22209b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w(Intent intent, l6.f fVar, int i8) {
        this.f22208a = intent;
        this.f22209b = fVar;
    }

    @Override // n6.x
    public final void a() {
        Intent intent = this.f22208a;
        if (intent != null) {
            this.f22209b.startActivityForResult(intent, 2);
        }
    }
}
