package androidx.appcompat.view.menu;

import android.content.DialogInterface;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.c;
import androidx.appcompat.view.menu.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class h implements DialogInterface.OnKeyListener, DialogInterface.OnClickListener, DialogInterface.OnDismissListener, m.a {

    /* renamed from: a  reason: collision with root package name */
    private g f955a;

    /* renamed from: b  reason: collision with root package name */
    private androidx.appcompat.app.c f956b;

    /* renamed from: c  reason: collision with root package name */
    e f957c;

    /* renamed from: d  reason: collision with root package name */
    private m.a f958d;

    public h(g gVar) {
        this.f955a = gVar;
    }

    public void a() {
        androidx.appcompat.app.c cVar = this.f956b;
        if (cVar != null) {
            cVar.dismiss();
        }
    }

    public void b(IBinder iBinder) {
        g gVar = this.f955a;
        c.a aVar = new c.a(gVar.w());
        e eVar = new e(aVar.b(), g.g.f19972l);
        this.f957c = eVar;
        eVar.j(this);
        this.f955a.b(this.f957c);
        aVar.c(this.f957c.a(), this);
        View A = gVar.A();
        if (A != null) {
            aVar.e(A);
        } else {
            aVar.f(gVar.y()).m(gVar.z());
        }
        aVar.j(this);
        androidx.appcompat.app.c a9 = aVar.a();
        this.f956b = a9;
        a9.setOnDismissListener(this);
        WindowManager.LayoutParams attributes = this.f956b.getWindow().getAttributes();
        attributes.type = 1003;
        if (iBinder != null) {
            attributes.token = iBinder;
        }
        attributes.flags |= 131072;
        this.f956b.show();
    }

    @Override // androidx.appcompat.view.menu.m.a
    public void c(g gVar, boolean z4) {
        if (z4 || gVar == this.f955a) {
            a();
        }
        m.a aVar = this.f958d;
        if (aVar != null) {
            aVar.c(gVar, z4);
        }
    }

    @Override // androidx.appcompat.view.menu.m.a
    public boolean d(g gVar) {
        m.a aVar = this.f958d;
        if (aVar != null) {
            return aVar.d(gVar);
        }
        return false;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i8) {
        this.f955a.N((i) this.f957c.a().getItem(i8), 0);
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        this.f957c.c(this.f955a, true);
    }

    @Override // android.content.DialogInterface.OnKeyListener
    public boolean onKey(DialogInterface dialogInterface, int i8, KeyEvent keyEvent) {
        Window window;
        View decorView;
        KeyEvent.DispatcherState keyDispatcherState;
        View decorView2;
        KeyEvent.DispatcherState keyDispatcherState2;
        if (i8 == 82 || i8 == 4) {
            if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                Window window2 = this.f956b.getWindow();
                if (window2 != null && (decorView2 = window2.getDecorView()) != null && (keyDispatcherState2 = decorView2.getKeyDispatcherState()) != null) {
                    keyDispatcherState2.startTracking(keyEvent, this);
                    return true;
                }
            } else if (keyEvent.getAction() == 1 && !keyEvent.isCanceled() && (window = this.f956b.getWindow()) != null && (decorView = window.getDecorView()) != null && (keyDispatcherState = decorView.getKeyDispatcherState()) != null && keyDispatcherState.isTracking(keyEvent)) {
                this.f955a.e(true);
                dialogInterface.dismiss();
                return true;
            }
        }
        return this.f955a.performShortcut(i8, keyEvent, 0);
    }
}
