package f;

import android.content.Context;
import android.content.Intent;
import androidx.activity.result.ActivityResult;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f extends f.a<Intent, ActivityResult> {

    /* renamed from: a  reason: collision with root package name */
    public static final a f19826a = new a(null);

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(i iVar) {
            this();
        }
    }

    @Override // f.a
    /* renamed from: d */
    public Intent a(Context context, Intent intent) {
        p.e(context, "context");
        p.e(intent, "input");
        return intent;
    }

    @Override // f.a
    /* renamed from: e */
    public ActivityResult c(int i8, Intent intent) {
        return new ActivityResult(i8, intent);
    }
}
