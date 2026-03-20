package f;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import f.a;
import f.d;
import java.util.List;
import kotlin.collections.q;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c extends f.a<androidx.activity.result.d, List<Uri>> {

    /* renamed from: b  reason: collision with root package name */
    public static final a f19818b = new a(null);

    /* renamed from: a  reason: collision with root package name */
    private final int f19819a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(i iVar) {
            this();
        }
    }

    public c(int i8) {
        this.f19819a = i8;
        if (!(i8 > 1)) {
            throw new IllegalArgumentException("Max items must be higher than 1".toString());
        }
    }

    @Override // f.a
    @SuppressLint({"NewApi", "ClassVerificationFailure"})
    /* renamed from: d */
    public Intent a(Context context, androidx.activity.result.d dVar) {
        p.e(context, "context");
        p.e(dVar, "input");
        d.a aVar = d.f19820a;
        if (aVar.f()) {
            Intent intent = new Intent("android.provider.action.PICK_IMAGES");
            intent.setType(aVar.c(dVar.a()));
            if (this.f19819a <= MediaStore.getPickImagesMaxLimit()) {
                intent.putExtra("android.provider.extra.PICK_IMAGES_MAX", this.f19819a);
                return intent;
            }
            throw new IllegalArgumentException("Max items must be less or equals MediaStore.getPickImagesMaxLimit()".toString());
        } else if (aVar.e(context)) {
            ResolveInfo b9 = aVar.b(context);
            if (b9 != null) {
                ActivityInfo activityInfo = b9.activityInfo;
                Intent intent2 = new Intent("androidx.activity.result.contract.action.PICK_IMAGES");
                intent2.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
                intent2.setType(aVar.c(dVar.a()));
                intent2.putExtra("com.google.android.gms.provider.extra.PICK_IMAGES_MAX", this.f19819a);
                return intent2;
            }
            throw new IllegalStateException("Required value was null.".toString());
        } else if (aVar.d(context)) {
            ResolveInfo a9 = aVar.a(context);
            if (a9 != null) {
                ActivityInfo activityInfo2 = a9.activityInfo;
                Intent intent3 = new Intent("com.google.android.gms.provider.action.PICK_IMAGES");
                intent3.setClassName(activityInfo2.applicationInfo.packageName, activityInfo2.name);
                intent3.putExtra("com.google.android.gms.provider.extra.PICK_IMAGES_MAX", this.f19819a);
                return intent3;
            }
            throw new IllegalStateException("Required value was null.".toString());
        } else {
            Intent intent4 = new Intent("android.intent.action.OPEN_DOCUMENT");
            intent4.setType(aVar.c(dVar.a()));
            intent4.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
            if (intent4.getType() == null) {
                intent4.setType("*/*");
                intent4.putExtra("android.intent.extra.MIME_TYPES", new String[]{"image/*", "video/*"});
                return intent4;
            }
            return intent4;
        }
    }

    @Override // f.a
    /* renamed from: e */
    public final a.C0167a<List<Uri>> b(Context context, androidx.activity.result.d dVar) {
        p.e(context, "context");
        p.e(dVar, "input");
        return null;
    }

    @Override // f.a
    /* renamed from: f */
    public final List<Uri> c(int i8, Intent intent) {
        List<Uri> a9;
        if (!(i8 == -1)) {
            intent = null;
        }
        return (intent == null || (a9 = b.f19817a.a(intent)) == null) ? q.f() : a9;
    }
}
