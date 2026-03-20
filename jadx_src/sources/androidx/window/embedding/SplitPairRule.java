package androidx.window.embedding;

import androidx.window.core.ExperimentalWindowApi;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.collections.q;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
@ExperimentalWindowApi
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SplitPairRule extends SplitRule {
    private final boolean clearTop;
    private final Set<SplitPairFilter> filters;
    private final boolean finishPrimaryWithSecondary;
    private final boolean finishSecondaryWithPrimary;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SplitPairRule(Set<SplitPairFilter> set, boolean z4, boolean z8, boolean z9, int i8, int i9, float f5, int i10) {
        super(i8, i9, f5, i10);
        p.e(set, "filters");
        this.finishPrimaryWithSecondary = z4;
        this.finishSecondaryWithPrimary = z8;
        this.clearTop = z9;
        this.filters = q.V(set);
    }

    public /* synthetic */ SplitPairRule(Set set, boolean z4, boolean z8, boolean z9, int i8, int i9, float f5, int i10, int i11, i iVar) {
        this(set, (i11 & 2) != 0 ? false : z4, (i11 & 4) != 0 ? true : z8, (i11 & 8) != 0 ? false : z9, (i11 & 16) != 0 ? 0 : i8, (i11 & 32) == 0 ? i9 : 0, (i11 & 64) != 0 ? 0.5f : f5, (i11 & RecognitionOptions.ITF) != 0 ? 3 : i10);
    }

    @Override // androidx.window.embedding.SplitRule
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj instanceof SplitPairRule) && super.equals(obj)) {
            SplitPairRule splitPairRule = (SplitPairRule) obj;
            return p.a(this.filters, splitPairRule.filters) && this.finishPrimaryWithSecondary == splitPairRule.finishPrimaryWithSecondary && this.finishSecondaryWithPrimary == splitPairRule.finishSecondaryWithPrimary && this.clearTop == splitPairRule.clearTop;
        }
        return false;
    }

    public final boolean getClearTop() {
        return this.clearTop;
    }

    public final Set<SplitPairFilter> getFilters() {
        return this.filters;
    }

    public final boolean getFinishPrimaryWithSecondary() {
        return this.finishPrimaryWithSecondary;
    }

    public final boolean getFinishSecondaryWithPrimary() {
        return this.finishSecondaryWithPrimary;
    }

    @Override // androidx.window.embedding.SplitRule
    public int hashCode() {
        return (((((((super.hashCode() * 31) + this.filters.hashCode()) * 31) + Boolean.hashCode(this.finishPrimaryWithSecondary)) * 31) + Boolean.hashCode(this.finishSecondaryWithPrimary)) * 31) + Boolean.hashCode(this.clearTop);
    }

    public final SplitPairRule plus$window_release(SplitPairFilter splitPairFilter) {
        p.e(splitPairFilter, "filter");
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        linkedHashSet.addAll(this.filters);
        linkedHashSet.add(splitPairFilter);
        return new SplitPairRule(q.V(linkedHashSet), this.finishPrimaryWithSecondary, this.finishSecondaryWithPrimary, this.clearTop, getMinWidth(), getMinSmallestWidth(), getSplitRatio(), getLayoutDirection());
    }
}
