package androidx.window.embedding;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import androidx.window.core.ExperimentalWindowApi;
import kotlin.jvm.internal.p;
@ExperimentalWindowApi
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ActivityFilter {
    private final ComponentName componentName;
    private final String intentAction;

    public ActivityFilter(ComponentName componentName, String str) {
        p.e(componentName, "componentName");
        this.componentName = componentName;
        this.intentAction = str;
        String packageName = componentName.getPackageName();
        p.d(packageName, "componentName.packageName");
        String className = componentName.getClassName();
        p.d(className, "componentName.className");
        boolean z4 = true;
        if (!(packageName.length() > 0)) {
            throw new IllegalArgumentException("Package name must not be empty".toString());
        }
        if (!(className.length() > 0)) {
            throw new IllegalArgumentException("Activity class name must not be empty.".toString());
        }
        if (!(!vj.e.x(packageName, "*", false, 2, (Object) null) || vj.e.G(packageName, "*", 0, false, 6, (Object) null) == packageName.length() - 1)) {
            throw new IllegalArgumentException("Wildcard in package name is only allowed at the end.".toString());
        }
        if (vj.e.x(className, "*", false, 2, (Object) null) && vj.e.G(className, "*", 0, false, 6, (Object) null) != className.length() - 1) {
            z4 = false;
        }
        if (!z4) {
            throw new IllegalArgumentException("Wildcard in class name is only allowed at the end.".toString());
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ActivityFilter) {
            ActivityFilter activityFilter = (ActivityFilter) obj;
            return p.a(this.componentName, activityFilter.componentName) && p.a(this.intentAction, activityFilter.intentAction);
        }
        return false;
    }

    public final ComponentName getComponentName() {
        return this.componentName;
    }

    public final String getIntentAction() {
        return this.intentAction;
    }

    public int hashCode() {
        int hashCode = this.componentName.hashCode() * 31;
        String str = this.intentAction;
        return hashCode + (str == null ? 0 : str.hashCode());
    }

    public final boolean matchesActivity(Activity activity) {
        p.e(activity, "activity");
        if (MatcherUtils.INSTANCE.areActivityOrIntentComponentsMatching$window_release(activity, this.componentName)) {
            String str = this.intentAction;
            if (str != null) {
                Intent intent = activity.getIntent();
                if (p.a(str, intent == null ? null : intent.getAction())) {
                }
            }
            return true;
        }
        return false;
    }

    public final boolean matchesIntent(Intent intent) {
        p.e(intent, "intent");
        if (MatcherUtils.INSTANCE.areComponentsMatching$window_release(intent.getComponent(), this.componentName)) {
            String str = this.intentAction;
            return str == null || p.a(str, intent.getAction());
        }
        return false;
    }

    public String toString() {
        return "ActivityFilter(componentName=" + this.componentName + ", intentAction=" + ((Object) this.intentAction) + ')';
    }
}
