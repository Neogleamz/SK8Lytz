package n6;

import android.app.Activity;
import android.content.Intent;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class v extends x {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ Intent f22205a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ Activity f22206b;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ int f22207c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public v(Intent intent, Activity activity, int i8) {
        this.f22205a = intent;
        this.f22206b = activity;
        this.f22207c = i8;
    }

    @Override // n6.x
    public final void a() {
        Intent intent = this.f22205a;
        if (intent != null) {
            this.f22206b.startActivityForResult(intent, this.f22207c);
        }
    }
}
