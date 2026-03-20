package androidx.core.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s implements Iterable<Intent> {

    /* renamed from: a  reason: collision with root package name */
    private final ArrayList<Intent> f4616a = new ArrayList<>();

    /* renamed from: b  reason: collision with root package name */
    private final Context f4617b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        Intent getSupportParentActivityIntent();
    }

    private s(Context context) {
        this.f4617b = context;
    }

    public static s k(Context context) {
        return new s(context);
    }

    public s e(Intent intent) {
        this.f4616a.add(intent);
        return this;
    }

    public s g(Intent intent) {
        ComponentName component = intent.getComponent();
        if (component == null) {
            component = intent.resolveActivity(this.f4617b.getPackageManager());
        }
        if (component != null) {
            i(component);
        }
        e(intent);
        return this;
    }

    public s h(Activity activity) {
        Intent supportParentActivityIntent = activity instanceof a ? ((a) activity).getSupportParentActivityIntent() : null;
        if (supportParentActivityIntent == null) {
            supportParentActivityIntent = i.a(activity);
        }
        if (supportParentActivityIntent != null) {
            ComponentName component = supportParentActivityIntent.getComponent();
            if (component == null) {
                component = supportParentActivityIntent.resolveActivity(this.f4617b.getPackageManager());
            }
            i(component);
            e(supportParentActivityIntent);
        }
        return this;
    }

    public s i(ComponentName componentName) {
        int size = this.f4616a.size();
        try {
            Context context = this.f4617b;
            while (true) {
                Intent b9 = i.b(context, componentName);
                if (b9 == null) {
                    return this;
                }
                this.f4616a.add(size, b9);
                context = this.f4617b;
                componentName = b9.getComponent();
            }
        } catch (PackageManager.NameNotFoundException e8) {
            Log.e("TaskStackBuilder", "Bad ComponentName while traversing activity parent metadata");
            throw new IllegalArgumentException(e8);
        }
    }

    @Override // java.lang.Iterable
    @Deprecated
    public Iterator<Intent> iterator() {
        return this.f4616a.iterator();
    }

    public void n() {
        p(null);
    }

    public void p(Bundle bundle) {
        if (this.f4616a.isEmpty()) {
            throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
        }
        Intent[] intentArr = (Intent[]) this.f4616a.toArray(new Intent[0]);
        intentArr[0] = new Intent(intentArr[0]).addFlags(268484608);
        if (androidx.core.content.a.k(this.f4617b, intentArr, bundle)) {
            return;
        }
        Intent intent = new Intent(intentArr[intentArr.length - 1]);
        intent.addFlags(268435456);
        this.f4617b.startActivity(intent);
    }
}
