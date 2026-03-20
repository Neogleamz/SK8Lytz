package androidx.window.embedding;

import android.content.Intent;
import androidx.window.core.ExperimentalWindowApi;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.collections.q;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
@ExperimentalWindowApi
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SplitPlaceholderRule extends SplitRule {
    private final Set<ActivityFilter> filters;
    private final Intent placeholderIntent;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SplitPlaceholderRule(Set<ActivityFilter> set, Intent intent, int i8, int i9, float f5, int i10) {
        super(i8, i9, f5, i10);
        p.e(set, "filters");
        p.e(intent, "placeholderIntent");
        this.placeholderIntent = intent;
        this.filters = q.V(set);
    }

    public /* synthetic */ SplitPlaceholderRule(Set set, Intent intent, int i8, int i9, float f5, int i10, int i11, i iVar) {
        this(set, intent, (i11 & 4) != 0 ? 0 : i8, (i11 & 8) != 0 ? 0 : i9, (i11 & 16) != 0 ? 0.5f : f5, (i11 & 32) != 0 ? 3 : i10);
    }

    @Override // androidx.window.embedding.SplitRule
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj instanceof SplitPlaceholderRule) && super.equals(obj) && super.equals(obj)) {
            SplitPlaceholderRule splitPlaceholderRule = (SplitPlaceholderRule) obj;
            return p.a(this.filters, splitPlaceholderRule.filters) && p.a(this.placeholderIntent, splitPlaceholderRule.placeholderIntent);
        }
        return false;
    }

    public final Set<ActivityFilter> getFilters() {
        return this.filters;
    }

    public final Intent getPlaceholderIntent() {
        return this.placeholderIntent;
    }

    @Override // androidx.window.embedding.SplitRule
    public int hashCode() {
        return (((super.hashCode() * 31) + this.filters.hashCode()) * 31) + this.placeholderIntent.hashCode();
    }

    public final SplitPlaceholderRule plus$window_release(ActivityFilter activityFilter) {
        p.e(activityFilter, "filter");
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        linkedHashSet.addAll(this.filters);
        linkedHashSet.add(activityFilter);
        return new SplitPlaceholderRule(q.V(linkedHashSet), this.placeholderIntent, getMinWidth(), getMinSmallestWidth(), getSplitRatio(), getLayoutDirection());
    }
}
