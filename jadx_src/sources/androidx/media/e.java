package androidx.media;

import android.content.Context;
import androidx.media.d;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class e extends g {
    /* JADX INFO: Access modifiers changed from: package-private */
    public e(Context context) {
        super(context);
        this.f6093a = context;
    }

    private boolean e(d.c cVar) {
        return b().checkPermission("android.permission.MEDIA_CONTENT_CONTROL", cVar.b(), cVar.a()) == 0;
    }

    @Override // androidx.media.g, androidx.media.d.a
    public boolean a(d.c cVar) {
        return e(cVar) || super.a(cVar);
    }
}
