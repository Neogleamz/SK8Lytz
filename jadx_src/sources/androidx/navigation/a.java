package androidx.navigation;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import androidx.navigation.q;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@q.b("activity")
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends q<C0068a> {

    /* renamed from: a  reason: collision with root package name */
    private Context f6298a;

    /* renamed from: b  reason: collision with root package name */
    private Activity f6299b;

    /* renamed from: androidx.navigation.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0068a extends i {

        /* renamed from: k  reason: collision with root package name */
        private Intent f6300k;

        /* renamed from: l  reason: collision with root package name */
        private String f6301l;

        public C0068a(q<? extends C0068a> qVar) {
            super(qVar);
        }

        @Override // androidx.navigation.i
        boolean E() {
            return false;
        }

        public final String F() {
            Intent intent = this.f6300k;
            if (intent == null) {
                return null;
            }
            return intent.getAction();
        }

        public final ComponentName G() {
            Intent intent = this.f6300k;
            if (intent == null) {
                return null;
            }
            return intent.getComponent();
        }

        public final String H() {
            return this.f6301l;
        }

        public final Intent I() {
            return this.f6300k;
        }

        public final C0068a K(String str) {
            if (this.f6300k == null) {
                this.f6300k = new Intent();
            }
            this.f6300k.setAction(str);
            return this;
        }

        public final C0068a L(ComponentName componentName) {
            if (this.f6300k == null) {
                this.f6300k = new Intent();
            }
            this.f6300k.setComponent(componentName);
            return this;
        }

        public final C0068a M(Uri uri) {
            if (this.f6300k == null) {
                this.f6300k = new Intent();
            }
            this.f6300k.setData(uri);
            return this;
        }

        public final C0068a N(String str) {
            this.f6301l = str;
            return this;
        }

        public final C0068a O(String str) {
            if (this.f6300k == null) {
                this.f6300k = new Intent();
            }
            this.f6300k.setPackage(str);
            return this;
        }

        @Override // androidx.navigation.i
        public String toString() {
            String F;
            ComponentName G = G();
            StringBuilder sb = new StringBuilder();
            sb.append(super.toString());
            if (G == null) {
                F = F();
                if (F != null) {
                    sb.append(" action=");
                }
                return sb.toString();
            }
            sb.append(" class=");
            F = G.getClassName();
            sb.append(F);
            return sb.toString();
        }

        @Override // androidx.navigation.i
        public void x(Context context, AttributeSet attributeSet) {
            super.x(context, attributeSet);
            TypedArray obtainAttributes = context.getResources().obtainAttributes(attributeSet, t.f6445a);
            String string = obtainAttributes.getString(t.f6450f);
            if (string != null) {
                string = string.replace("${applicationId}", context.getPackageName());
            }
            O(string);
            String string2 = obtainAttributes.getString(t.f6446b);
            if (string2 != null) {
                if (string2.charAt(0) == '.') {
                    string2 = context.getPackageName() + string2;
                }
                L(new ComponentName(context, string2));
            }
            K(obtainAttributes.getString(t.f6447c));
            String string3 = obtainAttributes.getString(t.f6448d);
            if (string3 != null) {
                M(Uri.parse(string3));
            }
            N(obtainAttributes.getString(t.f6449e));
            obtainAttributes.recycle();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements q.a {

        /* renamed from: a  reason: collision with root package name */
        private final int f6302a;

        /* renamed from: b  reason: collision with root package name */
        private final androidx.core.app.c f6303b;

        public androidx.core.app.c a() {
            return this.f6303b;
        }

        public int b() {
            return this.f6302a;
        }
    }

    public a(Context context) {
        this.f6298a = context;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                this.f6299b = (Activity) context;
                return;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
    }

    @Override // androidx.navigation.q
    public boolean e() {
        Activity activity = this.f6299b;
        if (activity != null) {
            activity.finish();
            return true;
        }
        return false;
    }

    @Override // androidx.navigation.q
    /* renamed from: f */
    public C0068a a() {
        return new C0068a(this);
    }

    final Context g() {
        return this.f6298a;
    }

    @Override // androidx.navigation.q
    /* renamed from: h */
    public i b(C0068a c0068a, Bundle bundle, n nVar, q.a aVar) {
        Intent intent;
        int intExtra;
        if (c0068a.I() == null) {
            throw new IllegalStateException("Destination " + c0068a.q() + " does not have an Intent set.");
        }
        Intent intent2 = new Intent(c0068a.I());
        if (bundle != null) {
            intent2.putExtras(bundle);
            String H = c0068a.H();
            if (!TextUtils.isEmpty(H)) {
                StringBuffer stringBuffer = new StringBuffer();
                Matcher matcher = Pattern.compile("\\{(.+?)\\}").matcher(H);
                while (matcher.find()) {
                    String group = matcher.group(1);
                    if (!bundle.containsKey(group)) {
                        throw new IllegalArgumentException("Could not find " + group + " in " + bundle + " to fill data pattern " + H);
                    }
                    matcher.appendReplacement(stringBuffer, BuildConfig.FLAVOR);
                    stringBuffer.append(Uri.encode(bundle.get(group).toString()));
                }
                matcher.appendTail(stringBuffer);
                intent2.setData(Uri.parse(stringBuffer.toString()));
            }
        }
        boolean z4 = aVar instanceof b;
        if (z4) {
            intent2.addFlags(((b) aVar).b());
        }
        if (!(this.f6298a instanceof Activity)) {
            intent2.addFlags(268435456);
        }
        if (nVar != null && nVar.g()) {
            intent2.addFlags(536870912);
        }
        Activity activity = this.f6299b;
        if (activity != null && (intent = activity.getIntent()) != null && (intExtra = intent.getIntExtra("android-support-navigation:ActivityNavigator:current", 0)) != 0) {
            intent2.putExtra("android-support-navigation:ActivityNavigator:source", intExtra);
        }
        intent2.putExtra("android-support-navigation:ActivityNavigator:current", c0068a.q());
        Resources resources = g().getResources();
        if (nVar != null) {
            int c9 = nVar.c();
            int d8 = nVar.d();
            if ((c9 <= 0 || !resources.getResourceTypeName(c9).equals("animator")) && (d8 <= 0 || !resources.getResourceTypeName(d8).equals("animator"))) {
                intent2.putExtra("android-support-navigation:ActivityNavigator:popEnterAnim", c9);
                intent2.putExtra("android-support-navigation:ActivityNavigator:popExitAnim", d8);
            } else {
                Log.w("ActivityNavigator", "Activity destinations do not support Animator resource. Ignoring popEnter resource " + resources.getResourceName(c9) + " and popExit resource " + resources.getResourceName(d8) + "when launching " + c0068a);
            }
        }
        if (z4) {
            ((b) aVar).a();
        }
        this.f6298a.startActivity(intent2);
        if (nVar == null || this.f6299b == null) {
            return null;
        }
        int a9 = nVar.a();
        int b9 = nVar.b();
        if ((a9 <= 0 || !resources.getResourceTypeName(a9).equals("animator")) && (b9 <= 0 || !resources.getResourceTypeName(b9).equals("animator"))) {
            if (a9 >= 0 || b9 >= 0) {
                this.f6299b.overridePendingTransition(Math.max(a9, 0), Math.max(b9, 0));
                return null;
            }
            return null;
        }
        Log.w("ActivityNavigator", "Activity destinations do not support Animator resource. Ignoring enter resource " + resources.getResourceName(a9) + " and exit resource " + resources.getResourceName(b9) + "when launching " + c0068a);
        return null;
    }
}
