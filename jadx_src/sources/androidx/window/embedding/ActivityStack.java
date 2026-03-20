package androidx.window.embedding;

import android.app.Activity;
import androidx.window.core.ExperimentalWindowApi;
import java.util.List;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
@ExperimentalWindowApi
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ActivityStack {
    private final List<Activity> activities;
    private final boolean isEmpty;

    /* JADX WARN: Multi-variable type inference failed */
    public ActivityStack(List<? extends Activity> list, boolean z4) {
        p.e(list, "activities");
        this.activities = list;
        this.isEmpty = z4;
    }

    public /* synthetic */ ActivityStack(List list, boolean z4, int i8, i iVar) {
        this(list, (i8 & 2) != 0 ? false : z4);
    }

    public final boolean contains(Activity activity) {
        p.e(activity, "activity");
        return this.activities.contains(activity);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ActivityStack) {
            ActivityStack activityStack = (ActivityStack) obj;
            return (p.a(this.activities, activityStack.activities) || this.isEmpty == activityStack.isEmpty) ? false : true;
        }
        return false;
    }

    public final List<Activity> getActivities$window_release() {
        return this.activities;
    }

    public int hashCode() {
        return ((this.isEmpty ? 1 : 0) * 31) + this.activities.hashCode();
    }

    public final boolean isEmpty() {
        return this.isEmpty;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ActivityStack{");
        sb.append(p.m("activities=", getActivities$window_release()));
        sb.append("isEmpty=" + this.isEmpty + '}');
        String sb2 = sb.toString();
        p.d(sb2, "StringBuilder().apply(builderAction).toString()");
        return sb2;
    }
}
