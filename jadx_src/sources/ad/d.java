package ad;

import com.zengge.wifi.Common.App;
import com.zengge.wifi.WebService.Models.SynchronizeGroupScene;
import oh.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final /* synthetic */ class d implements g {

    /* renamed from: a  reason: collision with root package name */
    public final /* synthetic */ App f206a;

    /* renamed from: b  reason: collision with root package name */
    public final /* synthetic */ String f207b;

    /* renamed from: c  reason: collision with root package name */
    public final /* synthetic */ App.c f208c;

    public /* synthetic */ d(App app, String str, App.c cVar) {
        this.f206a = app;
        this.f207b = str;
        this.f208c = cVar;
    }

    public final void accept(Object obj) {
        App.b(this.f206a, this.f207b, this.f208c, (SynchronizeGroupScene) obj);
    }
}
