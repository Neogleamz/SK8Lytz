package androidx.media.app;

import android.app.PendingIntent;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.RemoteViews;
import androidx.core.app.k;
import androidx.media.h;
import androidx.media.i;
import androidx.media.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c extends k.i {

    /* renamed from: e  reason: collision with root package name */
    int[] f6081e = null;

    /* renamed from: f  reason: collision with root package name */
    MediaSessionCompat.Token f6082f;

    /* renamed from: g  reason: collision with root package name */
    boolean f6083g;

    /* renamed from: h  reason: collision with root package name */
    PendingIntent f6084h;

    private RemoteViews z(k.a aVar) {
        boolean z4 = aVar.a() == null;
        RemoteViews remoteViews = new RemoteViews(this.f4544a.f4507a.getPackageName(), j.f6103a);
        int i8 = h.f6098a;
        remoteViews.setImageViewResource(i8, aVar.e());
        if (!z4) {
            remoteViews.setOnClickPendingIntent(i8, aVar.a());
        }
        if (Build.VERSION.SDK_INT >= 15) {
            a.a(remoteViews, i8, aVar.j());
        }
        return remoteViews;
    }

    int A(int i8) {
        return i8 <= 3 ? j.f6105c : j.f6104b;
    }

    int B() {
        return j.f6106d;
    }

    @Override // androidx.core.app.k.i
    public void b(androidx.core.app.j jVar) {
        if (Build.VERSION.SDK_INT >= 21) {
            b.d(jVar.a(), b.b(b.a(), this.f6081e, this.f6082f));
        } else if (this.f6083g) {
            jVar.a().setOngoing(true);
        }
    }

    @Override // androidx.core.app.k.i
    public RemoteViews s(androidx.core.app.j jVar) {
        if (Build.VERSION.SDK_INT >= 21) {
            return null;
        }
        return x();
    }

    @Override // androidx.core.app.k.i
    public RemoteViews t(androidx.core.app.j jVar) {
        if (Build.VERSION.SDK_INT >= 21) {
            return null;
        }
        return y();
    }

    RemoteViews x() {
        int min = Math.min(this.f4544a.f4508b.size(), 5);
        RemoteViews c9 = c(false, A(min), false);
        c9.removeAllViews(h.f6101d);
        if (min > 0) {
            for (int i8 = 0; i8 < min; i8++) {
                c9.addView(h.f6101d, z(this.f4544a.f4508b.get(i8)));
            }
        }
        if (this.f6083g) {
            int i9 = h.f6099b;
            c9.setViewVisibility(i9, 0);
            c9.setInt(i9, "setAlpha", this.f4544a.f4507a.getResources().getInteger(i.f6102a));
            c9.setOnClickPendingIntent(i9, this.f6084h);
        } else {
            c9.setViewVisibility(h.f6099b, 8);
        }
        return c9;
    }

    RemoteViews y() {
        RemoteViews c9 = c(false, B(), true);
        int size = this.f4544a.f4508b.size();
        int[] iArr = this.f6081e;
        int min = iArr == null ? 0 : Math.min(iArr.length, 3);
        c9.removeAllViews(h.f6101d);
        if (min > 0) {
            for (int i8 = 0; i8 < min; i8++) {
                if (i8 >= size) {
                    throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", Integer.valueOf(i8), Integer.valueOf(size - 1)));
                }
                c9.addView(h.f6101d, z(this.f4544a.f4508b.get(this.f6081e[i8])));
            }
        }
        if (this.f6083g) {
            c9.setViewVisibility(h.f6100c, 8);
            int i9 = h.f6099b;
            c9.setViewVisibility(i9, 0);
            c9.setOnClickPendingIntent(i9, this.f6084h);
            c9.setInt(i9, "setAlpha", this.f4544a.f4507a.getResources().getInteger(i.f6102a));
        } else {
            c9.setViewVisibility(h.f6100c, 0);
            c9.setViewVisibility(h.f6099b, 8);
        }
        return c9;
    }
}
