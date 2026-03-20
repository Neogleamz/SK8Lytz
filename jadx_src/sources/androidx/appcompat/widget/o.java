package androidx.appcompat.widget;

import android.view.textclassifier.TextClassificationManager;
import android.view.textclassifier.TextClassifier;
import android.widget.TextView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class o {

    /* renamed from: a  reason: collision with root package name */
    private TextView f1546a;

    /* renamed from: b  reason: collision with root package name */
    private TextClassifier f1547b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a {
        static TextClassifier a(TextView textView) {
            TextClassificationManager textClassificationManager = (TextClassificationManager) textView.getContext().getSystemService(TextClassificationManager.class);
            return textClassificationManager != null ? textClassificationManager.getTextClassifier() : TextClassifier.NO_OP;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public o(TextView textView) {
        this.f1546a = (TextView) androidx.core.util.h.h(textView);
    }

    public TextClassifier a() {
        TextClassifier textClassifier = this.f1547b;
        return textClassifier == null ? a.a(this.f1546a) : textClassifier;
    }

    public void b(TextClassifier textClassifier) {
        this.f1547b = textClassifier;
    }
}
