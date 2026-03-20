package androidx.browser.customtabs;

import android.os.Bundle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    public final Integer f1669a;

    /* renamed from: b  reason: collision with root package name */
    public final Integer f1670b;

    /* renamed from: c  reason: collision with root package name */
    public final Integer f1671c;

    /* renamed from: d  reason: collision with root package name */
    public final Integer f1672d;

    /* renamed from: androidx.browser.customtabs.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0014a {

        /* renamed from: a  reason: collision with root package name */
        private Integer f1673a;

        /* renamed from: b  reason: collision with root package name */
        private Integer f1674b;

        /* renamed from: c  reason: collision with root package name */
        private Integer f1675c;

        /* renamed from: d  reason: collision with root package name */
        private Integer f1676d;

        public a a() {
            return new a(this.f1673a, this.f1674b, this.f1675c, this.f1676d);
        }
    }

    a(Integer num, Integer num2, Integer num3, Integer num4) {
        this.f1669a = num;
        this.f1670b = num2;
        this.f1671c = num3;
        this.f1672d = num4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle a() {
        Bundle bundle = new Bundle();
        Integer num = this.f1669a;
        if (num != null) {
            bundle.putInt("android.support.customtabs.extra.TOOLBAR_COLOR", num.intValue());
        }
        Integer num2 = this.f1670b;
        if (num2 != null) {
            bundle.putInt("android.support.customtabs.extra.SECONDARY_TOOLBAR_COLOR", num2.intValue());
        }
        Integer num3 = this.f1671c;
        if (num3 != null) {
            bundle.putInt("androidx.browser.customtabs.extra.NAVIGATION_BAR_COLOR", num3.intValue());
        }
        Integer num4 = this.f1672d;
        if (num4 != null) {
            bundle.putInt("androidx.browser.customtabs.extra.NAVIGATION_BAR_DIVIDER_COLOR", num4.intValue());
        }
        return bundle;
    }
}
