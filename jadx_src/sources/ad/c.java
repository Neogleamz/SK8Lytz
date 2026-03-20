package ad;

import com.zengge.wifi.Common.App;
import com.zengge.wifi.WebService.Models.LoginResponse;
import oh.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final /* synthetic */ class c implements g {

    /* renamed from: a  reason: collision with root package name */
    public final /* synthetic */ App f203a;

    /* renamed from: b  reason: collision with root package name */
    public final /* synthetic */ String f204b;

    /* renamed from: c  reason: collision with root package name */
    public final /* synthetic */ App.c f205c;

    public /* synthetic */ c(App app, String str, App.c cVar) {
        this.f203a = app;
        this.f204b = str;
        this.f205c = cVar;
    }

    public final void accept(Object obj) {
        App.d(this.f203a, this.f204b, this.f205c, (LoginResponse) obj);
    }
}
