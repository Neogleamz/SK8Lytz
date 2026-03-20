package androidx.window.embedding;

import androidx.window.core.ExperimentalWindowApi;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.collections.q;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
@ExperimentalWindowApi
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ActivityRule extends EmbeddingRule {
    private final boolean alwaysExpand;
    private final Set<ActivityFilter> filters;

    public ActivityRule(Set<ActivityFilter> set, boolean z4) {
        p.e(set, "filters");
        this.alwaysExpand = z4;
        this.filters = q.V(set);
    }

    public /* synthetic */ ActivityRule(Set set, boolean z4, int i8, i iVar) {
        this(set, (i8 & 2) != 0 ? false : z4);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ActivityRule) {
            ActivityRule activityRule = (ActivityRule) obj;
            return p.a(this.filters, activityRule.filters) && this.alwaysExpand == activityRule.alwaysExpand;
        }
        return false;
    }

    public final boolean getAlwaysExpand() {
        return this.alwaysExpand;
    }

    public final Set<ActivityFilter> getFilters() {
        return this.filters;
    }

    public int hashCode() {
        return (this.filters.hashCode() * 31) + Boolean.hashCode(this.alwaysExpand);
    }

    public final ActivityRule plus$window_release(ActivityFilter activityFilter) {
        p.e(activityFilter, "filter");
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        linkedHashSet.addAll(this.filters);
        linkedHashSet.add(activityFilter);
        return new ActivityRule(q.V(linkedHashSet), this.alwaysExpand);
    }
}
