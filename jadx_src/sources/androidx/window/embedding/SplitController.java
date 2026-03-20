package androidx.window.embedding;

import android.app.Activity;
import android.content.Context;
import androidx.window.core.ExperimentalWindowApi;
import cj.a0;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.collections.p0;
import kotlin.collections.q;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
@ExperimentalWindowApi
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SplitController {
    private static volatile SplitController globalInstance = null;
    public static final boolean sDebug = false;
    private final EmbeddingBackend embeddingBackend;
    private Set<? extends EmbeddingRule> staticSplitRules;
    public static final Companion Companion = new Companion(null);
    private static final ReentrantLock globalLock = new ReentrantLock();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(i iVar) {
            this();
        }

        public final SplitController getInstance() {
            if (SplitController.globalInstance == null) {
                ReentrantLock reentrantLock = SplitController.globalLock;
                reentrantLock.lock();
                try {
                    if (SplitController.globalInstance == null) {
                        Companion companion = SplitController.Companion;
                        SplitController.globalInstance = new SplitController(null);
                    }
                    a0 a0Var = a0.a;
                } finally {
                    reentrantLock.unlock();
                }
            }
            SplitController splitController = SplitController.globalInstance;
            p.b(splitController);
            return splitController;
        }

        public final void initialize(Context context, int i8) {
            p.e(context, "context");
            Set<EmbeddingRule> parseSplitRules$window_release = new SplitRuleParser().parseSplitRules$window_release(context, i8);
            SplitController companion = getInstance();
            if (parseSplitRules$window_release == null) {
                parseSplitRules$window_release = p0.d();
            }
            companion.setStaticSplitRules(parseSplitRules$window_release);
        }
    }

    private SplitController() {
        this.embeddingBackend = ExtensionEmbeddingBackend.Companion.getInstance();
        this.staticSplitRules = p0.d();
    }

    public /* synthetic */ SplitController(i iVar) {
        this();
    }

    public static final SplitController getInstance() {
        return Companion.getInstance();
    }

    public static final void initialize(Context context, int i8) {
        Companion.initialize(context, i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setStaticSplitRules(Set<? extends EmbeddingRule> set) {
        this.staticSplitRules = set;
        this.embeddingBackend.setSplitRules(set);
    }

    public final void addSplitListener(Activity activity, Executor executor, androidx.core.util.a<List<SplitInfo>> aVar) {
        p.e(activity, "activity");
        p.e(executor, "executor");
        p.e(aVar, "consumer");
        this.embeddingBackend.registerSplitListenerForActivity(activity, executor, aVar);
    }

    public final void clearRegisteredRules() {
        this.embeddingBackend.setSplitRules(this.staticSplitRules);
    }

    public final Set<EmbeddingRule> getSplitRules() {
        return q.V(this.embeddingBackend.getSplitRules());
    }

    public final boolean isSplitSupported() {
        return this.embeddingBackend.isSplitSupported();
    }

    public final void registerRule(EmbeddingRule embeddingRule) {
        p.e(embeddingRule, "rule");
        this.embeddingBackend.registerRule(embeddingRule);
    }

    public final void removeSplitListener(androidx.core.util.a<List<SplitInfo>> aVar) {
        p.e(aVar, "consumer");
        this.embeddingBackend.unregisterSplitListenerForActivity(aVar);
    }

    public final void unregisterRule(EmbeddingRule embeddingRule) {
        p.e(embeddingRule, "rule");
        this.embeddingBackend.unregisterRule(embeddingRule);
    }
}
