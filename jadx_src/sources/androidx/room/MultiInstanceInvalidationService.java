package androidx.room;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import androidx.room.r;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MultiInstanceInvalidationService extends Service {

    /* renamed from: a  reason: collision with root package name */
    int f7048a = 0;

    /* renamed from: b  reason: collision with root package name */
    final HashMap<Integer, String> f7049b = new HashMap<>();

    /* renamed from: c  reason: collision with root package name */
    final RemoteCallbackList<q> f7050c = new a();

    /* renamed from: d  reason: collision with root package name */
    private final r.a f7051d = new b();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends RemoteCallbackList<q> {
        a() {
        }

        @Override // android.os.RemoteCallbackList
        /* renamed from: a */
        public void onCallbackDied(q qVar, Object obj) {
            MultiInstanceInvalidationService.this.f7049b.remove(Integer.valueOf(((Integer) obj).intValue()));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends r.a {
        b() {
        }

        @Override // androidx.room.r
        public void N1(int i8, String[] strArr) {
            synchronized (MultiInstanceInvalidationService.this.f7050c) {
                String str = MultiInstanceInvalidationService.this.f7049b.get(Integer.valueOf(i8));
                if (str == null) {
                    Log.w("ROOM", "Remote invalidation client ID not registered");
                    return;
                }
                int beginBroadcast = MultiInstanceInvalidationService.this.f7050c.beginBroadcast();
                for (int i9 = 0; i9 < beginBroadcast; i9++) {
                    int intValue = ((Integer) MultiInstanceInvalidationService.this.f7050c.getBroadcastCookie(i9)).intValue();
                    String str2 = MultiInstanceInvalidationService.this.f7049b.get(Integer.valueOf(intValue));
                    if (i8 != intValue && str.equals(str2)) {
                        try {
                            MultiInstanceInvalidationService.this.f7050c.getBroadcastItem(i9).t(strArr);
                        } catch (RemoteException e8) {
                            Log.w("ROOM", "Error invoking a remote callback", e8);
                        }
                    }
                }
                MultiInstanceInvalidationService.this.f7050c.finishBroadcast();
            }
        }

        @Override // androidx.room.r
        public int U(q qVar, String str) {
            if (str == null) {
                return 0;
            }
            synchronized (MultiInstanceInvalidationService.this.f7050c) {
                MultiInstanceInvalidationService multiInstanceInvalidationService = MultiInstanceInvalidationService.this;
                int i8 = multiInstanceInvalidationService.f7048a + 1;
                multiInstanceInvalidationService.f7048a = i8;
                if (multiInstanceInvalidationService.f7050c.register(qVar, Integer.valueOf(i8))) {
                    MultiInstanceInvalidationService.this.f7049b.put(Integer.valueOf(i8), str);
                    return i8;
                }
                MultiInstanceInvalidationService multiInstanceInvalidationService2 = MultiInstanceInvalidationService.this;
                multiInstanceInvalidationService2.f7048a--;
                return 0;
            }
        }

        @Override // androidx.room.r
        public void V1(q qVar, int i8) {
            synchronized (MultiInstanceInvalidationService.this.f7050c) {
                MultiInstanceInvalidationService.this.f7050c.unregister(qVar);
                MultiInstanceInvalidationService.this.f7049b.remove(Integer.valueOf(i8));
            }
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.f7051d;
    }
}
