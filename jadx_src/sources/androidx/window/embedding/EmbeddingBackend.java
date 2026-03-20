package androidx.window.embedding;

import android.app.Activity;
import androidx.window.core.ExperimentalWindowApi;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
@ExperimentalWindowApi
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface EmbeddingBackend {
    Set<EmbeddingRule> getSplitRules();

    boolean isSplitSupported();

    void registerRule(EmbeddingRule embeddingRule);

    void registerSplitListenerForActivity(Activity activity, Executor executor, androidx.core.util.a<List<SplitInfo>> aVar);

    void setSplitRules(Set<? extends EmbeddingRule> set);

    void unregisterRule(EmbeddingRule embeddingRule);

    void unregisterSplitListenerForActivity(androidx.core.util.a<List<SplitInfo>> aVar);
}
