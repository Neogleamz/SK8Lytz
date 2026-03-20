package l7;

import android.animation.TypeEvaluator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c implements TypeEvaluator<Integer> {

    /* renamed from: a  reason: collision with root package name */
    private static final c f21791a = new c();

    public static c b() {
        return f21791a;
    }

    @Override // android.animation.TypeEvaluator
    /* renamed from: a */
    public Integer evaluate(float f5, Integer num, Integer num2) {
        int intValue = num.intValue();
        float f8 = ((intValue >> 24) & 255) / 255.0f;
        int intValue2 = num2.intValue();
        float pow = (float) Math.pow(((intValue >> 16) & 255) / 255.0f, 2.2d);
        float pow2 = (float) Math.pow(((intValue >> 8) & 255) / 255.0f, 2.2d);
        float pow3 = (float) Math.pow((intValue & 255) / 255.0f, 2.2d);
        float pow4 = (float) Math.pow(((intValue2 >> 16) & 255) / 255.0f, 2.2d);
        float pow5 = pow3 + (f5 * (((float) Math.pow((intValue2 & 255) / 255.0f, 2.2d)) - pow3));
        return Integer.valueOf((Math.round(((float) Math.pow(pow + ((pow4 - pow) * f5), 0.45454545454545453d)) * 255.0f) << 16) | (Math.round((f8 + (((((intValue2 >> 24) & 255) / 255.0f) - f8) * f5)) * 255.0f) << 24) | (Math.round(((float) Math.pow(pow2 + ((((float) Math.pow(((intValue2 >> 8) & 255) / 255.0f, 2.2d)) - pow2) * f5), 0.45454545454545453d)) * 255.0f) << 8) | Math.round(((float) Math.pow(pow5, 0.45454545454545453d)) * 255.0f));
    }
}
