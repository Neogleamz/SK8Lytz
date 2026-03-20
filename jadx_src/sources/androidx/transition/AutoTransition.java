package androidx.transition;

import android.content.Context;
import android.util.AttributeSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AutoTransition extends TransitionSet {
    public AutoTransition() {
        B0();
    }

    public AutoTransition(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        B0();
    }

    private void B0() {
        y0(1);
        q0(new Fade(2)).q0(new ChangeBounds()).q0(new Fade(1));
    }
}
