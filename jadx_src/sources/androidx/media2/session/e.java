package androidx.media2.session;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;
import androidx.core.app.k;
import androidx.core.app.n;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class e extends f {

    /* renamed from: a  reason: collision with root package name */
    private final MediaSessionService f6239a;

    /* renamed from: b  reason: collision with root package name */
    private final n f6240b;

    /* renamed from: c  reason: collision with root package name */
    private final String f6241c;

    /* renamed from: d  reason: collision with root package name */
    private final Intent f6242d;

    /* renamed from: e  reason: collision with root package name */
    private final k.a f6243e = a(i.f6269b, j.f6274c, 4);

    /* renamed from: f  reason: collision with root package name */
    private final k.a f6244f = a(i.f6268a, j.f6273b, 2);

    /* renamed from: g  reason: collision with root package name */
    private final k.a f6245g = a(i.f6271d, j.f6276e, 16);

    /* renamed from: h  reason: collision with root package name */
    private final k.a f6246h = a(i.f6270c, j.f6275d, 32);

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(MediaSessionService mediaSessionService) {
        this.f6239a = mediaSessionService;
        this.f6242d = new Intent(mediaSessionService, mediaSessionService.getClass());
        this.f6240b = n.e(mediaSessionService);
        this.f6241c = mediaSessionService.getResources().getString(j.f6272a);
    }

    private k.a a(int i8, int i9, long j8) {
        return new k.a(i8, this.f6239a.getResources().getText(i9), b(j8));
    }

    private PendingIntent b(long j8) {
        int b9 = PlaybackStateCompat.b(j8);
        Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
        MediaSessionService mediaSessionService = this.f6239a;
        intent.setComponent(new ComponentName(mediaSessionService, mediaSessionService.getClass()));
        intent.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(0, b9));
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 26 || j8 == 2 || j8 == 1) {
            return PendingIntent.getService(this.f6239a, b9, intent, i8 >= 23 ? 67108864 : 0);
        }
        return androidx.media2.common.a.a(this.f6239a, b9, intent, 67108864);
    }
}
