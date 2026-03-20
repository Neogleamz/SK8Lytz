package f;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.ext.SdkExtensions;
import f.a;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.q;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d extends f.a<androidx.activity.result.d, Uri> {

    /* renamed from: a  reason: collision with root package name */
    public static final a f19820a = new a(null);

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(i iVar) {
            this();
        }

        public final ResolveInfo a(Context context) {
            p.e(context, "context");
            return context.getPackageManager().resolveActivity(new Intent("com.google.android.gms.provider.action.PICK_IMAGES"), 1114112);
        }

        public final ResolveInfo b(Context context) {
            p.e(context, "context");
            return context.getPackageManager().resolveActivity(new Intent("androidx.activity.result.contract.action.PICK_IMAGES"), 1114112);
        }

        public final String c(f fVar) {
            p.e(fVar, "input");
            if (fVar instanceof c) {
                return "image/*";
            }
            if (fVar instanceof e) {
                return "video/*";
            }
            if (fVar instanceof C0168d) {
                return ((C0168d) fVar).a();
            }
            if (fVar instanceof b) {
                return null;
            }
            throw new NoWhenBranchMatchedException();
        }

        public final boolean d(Context context) {
            p.e(context, "context");
            return a(context) != null;
        }

        public final boolean e(Context context) {
            p.e(context, "context");
            return b(context) != null;
        }

        @SuppressLint({"ClassVerificationFailure", "NewApi"})
        public final boolean f() {
            int i8 = Build.VERSION.SDK_INT;
            return i8 >= 33 || (i8 >= 30 && SdkExtensions.getExtensionVersion(30) >= 2);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements f {

        /* renamed from: a  reason: collision with root package name */
        public static final b f19821a = new b();

        private b() {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c implements f {

        /* renamed from: a  reason: collision with root package name */
        public static final c f19822a = new c();

        private c() {
        }
    }

    /* renamed from: f.d$d  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0168d implements f {

        /* renamed from: a  reason: collision with root package name */
        private final String f19823a;

        public final String a() {
            return this.f19823a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e implements f {

        /* renamed from: a  reason: collision with root package name */
        public static final e f19824a = new e();

        private e() {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f {
    }

    @SuppressLint({"ClassVerificationFailure", "NewApi"})
    public static final boolean f() {
        return f19820a.f();
    }

    @Override // f.a
    /* renamed from: d */
    public Intent a(Context context, androidx.activity.result.d dVar) {
        ActivityInfo activityInfo;
        Intent intent;
        p.e(context, "context");
        p.e(dVar, "input");
        a aVar = f19820a;
        if (aVar.f()) {
            Intent intent2 = new Intent("android.provider.action.PICK_IMAGES");
            intent2.setType(aVar.c(dVar.a()));
            return intent2;
        }
        if (aVar.e(context)) {
            ResolveInfo b9 = aVar.b(context);
            if (b9 == null) {
                throw new IllegalStateException("Required value was null.".toString());
            }
            activityInfo = b9.activityInfo;
            intent = new Intent("androidx.activity.result.contract.action.PICK_IMAGES");
        } else if (!aVar.d(context)) {
            Intent intent3 = new Intent("android.intent.action.OPEN_DOCUMENT");
            intent3.setType(aVar.c(dVar.a()));
            if (intent3.getType() == null) {
                intent3.setType("*/*");
                intent3.putExtra("android.intent.extra.MIME_TYPES", new String[]{"image/*", "video/*"});
                return intent3;
            }
            return intent3;
        } else {
            ResolveInfo a9 = aVar.a(context);
            if (a9 == null) {
                throw new IllegalStateException("Required value was null.".toString());
            }
            activityInfo = a9.activityInfo;
            intent = new Intent("com.google.android.gms.provider.action.PICK_IMAGES");
        }
        intent.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
        intent.setType(aVar.c(dVar.a()));
        return intent;
    }

    @Override // f.a
    /* renamed from: e */
    public final a.C0167a<Uri> b(Context context, androidx.activity.result.d dVar) {
        p.e(context, "context");
        p.e(dVar, "input");
        return null;
    }

    @Override // f.a
    /* renamed from: g */
    public final Uri c(int i8, Intent intent) {
        if (!(i8 == -1)) {
            intent = null;
        }
        if (intent != null) {
            Uri data = intent.getData();
            if (data == null) {
                data = (Uri) q.w(f.b.f19817a.a(intent));
            }
            return data;
        }
        return null;
    }
}
