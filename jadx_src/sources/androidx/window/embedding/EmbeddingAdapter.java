package androidx.window.embedding;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Pair;
import android.view.WindowMetrics;
import androidx.window.core.ExperimentalWindowApi;
import androidx.window.extensions.embedding.ActivityRule;
import androidx.window.extensions.embedding.SplitPairRule;
import androidx.window.extensions.embedding.SplitPlaceholderRule;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import kotlin.collections.q;
import kotlin.jvm.internal.p;
@ExperimentalWindowApi
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class EmbeddingAdapter {
    private final <F, S> F component1(Pair<F, S> pair) {
        p.e(pair, "<this>");
        return (F) pair.first;
    }

    private final <F, S> S component2(Pair<F, S> pair) {
        p.e(pair, "<this>");
        return (S) pair.second;
    }

    private final SplitInfo translate(androidx.window.extensions.embedding.SplitInfo splitInfo) {
        boolean z4;
        androidx.window.extensions.embedding.ActivityStack primaryActivityStack = splitInfo.getPrimaryActivityStack();
        p.d(primaryActivityStack, "splitInfo.primaryActivityStack");
        boolean z8 = false;
        try {
            z4 = primaryActivityStack.isEmpty();
        } catch (NoSuchMethodError unused) {
            z4 = false;
        }
        List activities = primaryActivityStack.getActivities();
        p.d(activities, "primaryActivityStack.activities");
        ActivityStack activityStack = new ActivityStack(activities, z4);
        androidx.window.extensions.embedding.ActivityStack secondaryActivityStack = splitInfo.getSecondaryActivityStack();
        p.d(secondaryActivityStack, "splitInfo.secondaryActivityStack");
        try {
            z8 = secondaryActivityStack.isEmpty();
        } catch (NoSuchMethodError unused2) {
        }
        List activities2 = secondaryActivityStack.getActivities();
        p.d(activities2, "secondaryActivityStack.activities");
        return new SplitInfo(activityStack, new ActivityStack(activities2, z8), splitInfo.getSplitRatio());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: translateActivityIntentPredicates$lambda-3  reason: not valid java name */
    public static final boolean m1translateActivityIntentPredicates$lambda3(EmbeddingAdapter embeddingAdapter, Set set, Pair pair) {
        p.e(embeddingAdapter, "this$0");
        p.e(set, "$splitPairFilters");
        p.d(pair, "(first, second)");
        Activity activity = (Activity) embeddingAdapter.component1(pair);
        Intent intent = (Intent) embeddingAdapter.component2(pair);
        if ((set instanceof Collection) && set.isEmpty()) {
            return false;
        }
        Iterator it = set.iterator();
        while (it.hasNext()) {
            if (((SplitPairFilter) it.next()).matchesActivityIntentPair(activity, intent)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: translateActivityPairPredicates$lambda-1  reason: not valid java name */
    public static final boolean m2translateActivityPairPredicates$lambda1(EmbeddingAdapter embeddingAdapter, Set set, Pair pair) {
        p.e(embeddingAdapter, "this$0");
        p.e(set, "$splitPairFilters");
        p.d(pair, "(first, second)");
        Activity activity = (Activity) embeddingAdapter.component1(pair);
        Activity activity2 = (Activity) embeddingAdapter.component2(pair);
        if ((set instanceof Collection) && set.isEmpty()) {
            return false;
        }
        Iterator it = set.iterator();
        while (it.hasNext()) {
            if (((SplitPairFilter) it.next()).matchesActivityPair(activity, activity2)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: translateActivityPredicates$lambda-6  reason: not valid java name */
    public static final boolean m3translateActivityPredicates$lambda6(Set set, Activity activity) {
        p.e(set, "$activityFilters");
        if ((set instanceof Collection) && set.isEmpty()) {
            return false;
        }
        Iterator it = set.iterator();
        while (it.hasNext()) {
            p.d(activity, "activity");
            if (((ActivityFilter) it.next()).matchesActivity(activity)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: translateIntentPredicates$lambda-8  reason: not valid java name */
    public static final boolean m4translateIntentPredicates$lambda8(Set set, Intent intent) {
        p.e(set, "$activityFilters");
        if ((set instanceof Collection) && set.isEmpty()) {
            return false;
        }
        Iterator it = set.iterator();
        while (it.hasNext()) {
            p.d(intent, "intent");
            if (((ActivityFilter) it.next()).matchesIntent(intent)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: translateParentMetricsPredicate$lambda-4  reason: not valid java name */
    public static final boolean m5translateParentMetricsPredicate$lambda4(SplitRule splitRule, WindowMetrics windowMetrics) {
        p.e(splitRule, "$splitRule");
        p.d(windowMetrics, "windowMetrics");
        return splitRule.checkParentMetrics(windowMetrics);
    }

    public final List<SplitInfo> translate(List<? extends androidx.window.extensions.embedding.SplitInfo> list) {
        p.e(list, "splitInfoList");
        ArrayList arrayList = new ArrayList(q.m(list, 10));
        for (androidx.window.extensions.embedding.SplitInfo splitInfo : list) {
            arrayList.add(translate(splitInfo));
        }
        return arrayList;
    }

    public final Set<androidx.window.extensions.embedding.EmbeddingRule> translate(Set<? extends EmbeddingRule> set) {
        androidx.window.extensions.embedding.SplitPairRule build;
        String str;
        p.e(set, "rules");
        ArrayList arrayList = new ArrayList(q.m(set, 10));
        for (EmbeddingRule embeddingRule : set) {
            if (embeddingRule instanceof SplitPairRule) {
                SplitPairRule splitPairRule = (SplitPairRule) embeddingRule;
                build = new SplitPairRule.Builder(translateActivityPairPredicates(splitPairRule.getFilters()), translateActivityIntentPredicates(splitPairRule.getFilters()), translateParentMetricsPredicate((SplitRule) embeddingRule)).setSplitRatio(splitPairRule.getSplitRatio()).setLayoutDirection(splitPairRule.getLayoutDirection()).setShouldFinishPrimaryWithSecondary(splitPairRule.getFinishPrimaryWithSecondary()).setShouldFinishSecondaryWithPrimary(splitPairRule.getFinishSecondaryWithPrimary()).setShouldClearTop(splitPairRule.getClearTop()).build();
                str = "SplitPairRuleBuilder(\n  …                 .build()";
            } else if (embeddingRule instanceof SplitPlaceholderRule) {
                SplitPlaceholderRule splitPlaceholderRule = (SplitPlaceholderRule) embeddingRule;
                build = new SplitPlaceholderRule.Builder(splitPlaceholderRule.getPlaceholderIntent(), translateActivityPredicates(splitPlaceholderRule.getFilters()), translateIntentPredicates(splitPlaceholderRule.getFilters()), translateParentMetricsPredicate((SplitRule) embeddingRule)).setSplitRatio(splitPlaceholderRule.getSplitRatio()).setLayoutDirection(splitPlaceholderRule.getLayoutDirection()).build();
                str = "SplitPlaceholderRuleBuil…                 .build()";
            } else if (!(embeddingRule instanceof ActivityRule)) {
                throw new IllegalArgumentException("Unsupported rule type");
            } else {
                ActivityRule activityRule = (ActivityRule) embeddingRule;
                build = new ActivityRule.Builder(translateActivityPredicates(activityRule.getFilters()), translateIntentPredicates(activityRule.getFilters())).setShouldAlwaysExpand(activityRule.getAlwaysExpand()).build();
                str = "ActivityRuleBuilder(\n   …                 .build()";
            }
            p.d(build, str);
            arrayList.add((androidx.window.extensions.embedding.EmbeddingRule) build);
        }
        return q.V(arrayList);
    }

    @SuppressLint({"ClassVerificationFailure", "NewApi"})
    public final Predicate<Pair<Activity, Intent>> translateActivityIntentPredicates(final Set<SplitPairFilter> set) {
        p.e(set, "splitPairFilters");
        return new Predicate() { // from class: androidx.window.embedding.a
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean m1translateActivityIntentPredicates$lambda3;
                m1translateActivityIntentPredicates$lambda3 = EmbeddingAdapter.m1translateActivityIntentPredicates$lambda3(EmbeddingAdapter.this, set, (Pair) obj);
                return m1translateActivityIntentPredicates$lambda3;
            }
        };
    }

    @SuppressLint({"ClassVerificationFailure", "NewApi"})
    public final Predicate<Pair<Activity, Activity>> translateActivityPairPredicates(final Set<SplitPairFilter> set) {
        p.e(set, "splitPairFilters");
        return new Predicate() { // from class: androidx.window.embedding.b
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean m2translateActivityPairPredicates$lambda1;
                m2translateActivityPairPredicates$lambda1 = EmbeddingAdapter.m2translateActivityPairPredicates$lambda1(EmbeddingAdapter.this, set, (Pair) obj);
                return m2translateActivityPairPredicates$lambda1;
            }
        };
    }

    @SuppressLint({"ClassVerificationFailure", "NewApi"})
    public final Predicate<Activity> translateActivityPredicates(final Set<ActivityFilter> set) {
        p.e(set, "activityFilters");
        return new Predicate() { // from class: androidx.window.embedding.d
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean m3translateActivityPredicates$lambda6;
                m3translateActivityPredicates$lambda6 = EmbeddingAdapter.m3translateActivityPredicates$lambda6(set, (Activity) obj);
                return m3translateActivityPredicates$lambda6;
            }
        };
    }

    @SuppressLint({"ClassVerificationFailure", "NewApi"})
    public final Predicate<Intent> translateIntentPredicates(final Set<ActivityFilter> set) {
        p.e(set, "activityFilters");
        return new Predicate() { // from class: androidx.window.embedding.e
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean m4translateIntentPredicates$lambda8;
                m4translateIntentPredicates$lambda8 = EmbeddingAdapter.m4translateIntentPredicates$lambda8(set, (Intent) obj);
                return m4translateIntentPredicates$lambda8;
            }
        };
    }

    @SuppressLint({"ClassVerificationFailure", "NewApi"})
    public final Predicate<WindowMetrics> translateParentMetricsPredicate(final SplitRule splitRule) {
        p.e(splitRule, "splitRule");
        return new Predicate() { // from class: androidx.window.embedding.c
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean m5translateParentMetricsPredicate$lambda4;
                m5translateParentMetricsPredicate$lambda4 = EmbeddingAdapter.m5translateParentMetricsPredicate$lambda4(SplitRule.this, (WindowMetrics) obj);
                return m5translateParentMetricsPredicate$lambda4;
            }
        };
    }
}
