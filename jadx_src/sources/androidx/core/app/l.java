package androidx.core.app;

import android.app.Notification;
import android.app.RemoteInput;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.RemoteViews;
import androidx.core.app.k;
import androidx.core.graphics.drawable.IconCompat;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l implements j {

    /* renamed from: a  reason: collision with root package name */
    private final Context f4548a;

    /* renamed from: b  reason: collision with root package name */
    private final Notification.Builder f4549b;

    /* renamed from: c  reason: collision with root package name */
    private final k.e f4550c;

    /* renamed from: d  reason: collision with root package name */
    private RemoteViews f4551d;

    /* renamed from: e  reason: collision with root package name */
    private RemoteViews f4552e;

    /* renamed from: f  reason: collision with root package name */
    private final List<Bundle> f4553f = new ArrayList();

    /* renamed from: g  reason: collision with root package name */
    private final Bundle f4554g = new Bundle();

    /* renamed from: h  reason: collision with root package name */
    private int f4555h;

    /* renamed from: i  reason: collision with root package name */
    private RemoteViews f4556i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l(k.e eVar) {
        int i8;
        Icon icon;
        List<String> e8;
        Bundle bundle;
        String str;
        this.f4550c = eVar;
        this.f4548a = eVar.f4507a;
        int i9 = Build.VERSION.SDK_INT;
        Context context = eVar.f4507a;
        this.f4549b = i9 >= 26 ? new Notification.Builder(context, eVar.L) : new Notification.Builder(context);
        Notification notification = eVar.T;
        this.f4549b.setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, eVar.f4515i).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 2) != 0).setOnlyAlertOnce((notification.flags & 8) != 0).setAutoCancel((notification.flags & 16) != 0).setDefaults(notification.defaults).setContentTitle(eVar.f4511e).setContentText(eVar.f4512f).setContentInfo(eVar.f4517k).setContentIntent(eVar.f4513g).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(eVar.f4514h, (notification.flags & RecognitionOptions.ITF) != 0).setLargeIcon(eVar.f4516j).setNumber(eVar.f4518l).setProgress(eVar.f4526u, eVar.f4527v, eVar.f4528w);
        if (i9 < 21) {
            this.f4549b.setSound(notification.sound, notification.audioStreamType);
        }
        if (i9 >= 16) {
            this.f4549b.setSubText(eVar.f4523r).setUsesChronometer(eVar.f4521o).setPriority(eVar.f4519m);
            Iterator<k.a> it = eVar.f4508b.iterator();
            while (it.hasNext()) {
                b(it.next());
            }
            Bundle bundle2 = eVar.E;
            if (bundle2 != null) {
                this.f4554g.putAll(bundle2);
            }
            if (Build.VERSION.SDK_INT < 20) {
                if (eVar.A) {
                    this.f4554g.putBoolean("android.support.localOnly", true);
                }
                String str2 = eVar.f4529x;
                if (str2 != null) {
                    this.f4554g.putString("android.support.groupKey", str2);
                    if (eVar.f4530y) {
                        bundle = this.f4554g;
                        str = "android.support.isGroupSummary";
                    } else {
                        bundle = this.f4554g;
                        str = "android.support.useSideChannel";
                    }
                    bundle.putBoolean(str, true);
                }
                String str3 = eVar.f4531z;
                if (str3 != null) {
                    this.f4554g.putString("android.support.sortKey", str3);
                }
            }
            this.f4551d = eVar.I;
            this.f4552e = eVar.J;
        }
        int i10 = Build.VERSION.SDK_INT;
        if (i10 >= 17) {
            this.f4549b.setShowWhen(eVar.f4520n);
        }
        if (i10 >= 19 && i10 < 21 && (e8 = e(g(eVar.f4509c), eVar.W)) != null && !e8.isEmpty()) {
            this.f4554g.putStringArray("android.people", (String[]) e8.toArray(new String[e8.size()]));
        }
        if (i10 >= 20) {
            this.f4549b.setLocalOnly(eVar.A).setGroup(eVar.f4529x).setGroupSummary(eVar.f4530y).setSortKey(eVar.f4531z);
            this.f4555h = eVar.P;
        }
        if (i10 >= 21) {
            this.f4549b.setCategory(eVar.D).setColor(eVar.F).setVisibility(eVar.G).setPublicVersion(eVar.H).setSound(notification.sound, notification.audioAttributes);
            List<String> e9 = i10 < 28 ? e(g(eVar.f4509c), eVar.W) : eVar.W;
            if (e9 != null && !e9.isEmpty()) {
                for (String str4 : e9) {
                    this.f4549b.addPerson(str4);
                }
            }
            this.f4556i = eVar.K;
            if (eVar.f4510d.size() > 0) {
                Bundle bundle3 = eVar.g().getBundle("android.car.EXTENSIONS");
                bundle3 = bundle3 == null ? new Bundle() : bundle3;
                Bundle bundle4 = new Bundle(bundle3);
                Bundle bundle5 = new Bundle();
                for (int i11 = 0; i11 < eVar.f4510d.size(); i11++) {
                    bundle5.putBundle(Integer.toString(i11), m.b(eVar.f4510d.get(i11)));
                }
                bundle3.putBundle("invisible_actions", bundle5);
                bundle4.putBundle("invisible_actions", bundle5);
                eVar.g().putBundle("android.car.EXTENSIONS", bundle3);
                this.f4554g.putBundle("android.car.EXTENSIONS", bundle4);
            }
        }
        int i12 = Build.VERSION.SDK_INT;
        if (i12 >= 23 && (icon = eVar.V) != null) {
            this.f4549b.setSmallIcon(icon);
        }
        if (i12 >= 24) {
            this.f4549b.setExtras(eVar.E).setRemoteInputHistory(eVar.f4525t);
            RemoteViews remoteViews = eVar.I;
            if (remoteViews != null) {
                this.f4549b.setCustomContentView(remoteViews);
            }
            RemoteViews remoteViews2 = eVar.J;
            if (remoteViews2 != null) {
                this.f4549b.setCustomBigContentView(remoteViews2);
            }
            RemoteViews remoteViews3 = eVar.K;
            if (remoteViews3 != null) {
                this.f4549b.setCustomHeadsUpContentView(remoteViews3);
            }
        }
        if (i12 >= 26) {
            this.f4549b.setBadgeIconType(eVar.M).setSettingsText(eVar.f4524s).setShortcutId(eVar.N).setTimeoutAfter(eVar.O).setGroupAlertBehavior(eVar.P);
            if (eVar.C) {
                this.f4549b.setColorized(eVar.B);
            }
            if (!TextUtils.isEmpty(eVar.L)) {
                this.f4549b.setSound(null).setDefaults(0).setLights(0, 0, 0).setVibrate(null);
            }
        }
        if (i12 >= 28) {
            Iterator<o> it2 = eVar.f4509c.iterator();
            while (it2.hasNext()) {
                this.f4549b.addPerson(it2.next().j());
            }
        }
        int i13 = Build.VERSION.SDK_INT;
        if (i13 >= 29) {
            this.f4549b.setAllowSystemGeneratedContextualActions(eVar.R);
            this.f4549b.setBubbleMetadata(k.d.a(eVar.S));
        }
        if (i13 >= 31 && (i8 = eVar.Q) != 0) {
            this.f4549b.setForegroundServiceBehavior(i8);
        }
        if (eVar.U) {
            if (this.f4550c.f4530y) {
                this.f4555h = 2;
            } else {
                this.f4555h = 1;
            }
            this.f4549b.setVibrate(null);
            this.f4549b.setSound(null);
            int i14 = notification.defaults & (-2);
            notification.defaults = i14;
            int i15 = i14 & (-3);
            notification.defaults = i15;
            this.f4549b.setDefaults(i15);
            if (i13 >= 26) {
                if (TextUtils.isEmpty(this.f4550c.f4529x)) {
                    this.f4549b.setGroup("silent");
                }
                this.f4549b.setGroupAlertBehavior(this.f4555h);
            }
        }
    }

    private void b(k.a aVar) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 20) {
            if (i8 >= 16) {
                this.f4553f.add(m.f(this.f4549b, aVar));
                return;
            }
            return;
        }
        IconCompat f5 = aVar.f();
        Notification.Action.Builder builder = i8 >= 23 ? new Notification.Action.Builder(f5 != null ? f5.A() : null, aVar.j(), aVar.a()) : new Notification.Action.Builder(f5 != null ? f5.r() : 0, aVar.j(), aVar.a());
        if (aVar.g() != null) {
            for (RemoteInput remoteInput : q.b(aVar.g())) {
                builder.addRemoteInput(remoteInput);
            }
        }
        Bundle bundle = aVar.d() != null ? new Bundle(aVar.d()) : new Bundle();
        bundle.putBoolean("android.support.allowGeneratedReplies", aVar.b());
        int i9 = Build.VERSION.SDK_INT;
        if (i9 >= 24) {
            builder.setAllowGeneratedReplies(aVar.b());
        }
        bundle.putInt("android.support.action.semanticAction", aVar.h());
        if (i9 >= 28) {
            builder.setSemanticAction(aVar.h());
        }
        if (i9 >= 29) {
            builder.setContextual(aVar.l());
        }
        if (i9 >= 31) {
            builder.setAuthenticationRequired(aVar.k());
        }
        bundle.putBoolean("android.support.action.showsUserInterface", aVar.i());
        builder.addExtras(bundle);
        this.f4549b.addAction(builder.build());
    }

    private static List<String> e(List<String> list, List<String> list2) {
        if (list == null) {
            return list2;
        }
        if (list2 == null) {
            return list;
        }
        k0.b bVar = new k0.b(list.size() + list2.size());
        bVar.addAll(list);
        bVar.addAll(list2);
        return new ArrayList(bVar);
    }

    private static List<String> g(List<o> list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (o oVar : list) {
            arrayList.add(oVar.i());
        }
        return arrayList;
    }

    private void h(Notification notification) {
        notification.sound = null;
        notification.vibrate = null;
        int i8 = notification.defaults & (-2);
        notification.defaults = i8;
        notification.defaults = i8 & (-3);
    }

    @Override // androidx.core.app.j
    public Notification.Builder a() {
        return this.f4549b;
    }

    public Notification c() {
        Bundle a9;
        RemoteViews u8;
        RemoteViews s8;
        k.i iVar = this.f4550c.q;
        if (iVar != null) {
            iVar.b(this);
        }
        RemoteViews t8 = iVar != null ? iVar.t(this) : null;
        Notification d8 = d();
        if (t8 != null || (t8 = this.f4550c.I) != null) {
            d8.contentView = t8;
        }
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 16 && iVar != null && (s8 = iVar.s(this)) != null) {
            d8.bigContentView = s8;
        }
        if (i8 >= 21 && iVar != null && (u8 = this.f4550c.q.u(this)) != null) {
            d8.headsUpContentView = u8;
        }
        if (i8 >= 16 && iVar != null && (a9 = k.a(d8)) != null) {
            iVar.a(a9);
        }
        return d8;
    }

    protected Notification d() {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 26) {
            return this.f4549b.build();
        }
        if (i8 >= 24) {
            Notification build = this.f4549b.build();
            if (this.f4555h != 0) {
                if (build.getGroup() != null && (build.flags & RecognitionOptions.UPC_A) != 0 && this.f4555h == 2) {
                    h(build);
                }
                if (build.getGroup() != null && (build.flags & RecognitionOptions.UPC_A) == 0 && this.f4555h == 1) {
                    h(build);
                }
            }
            return build;
        } else if (i8 >= 21) {
            this.f4549b.setExtras(this.f4554g);
            Notification build2 = this.f4549b.build();
            RemoteViews remoteViews = this.f4551d;
            if (remoteViews != null) {
                build2.contentView = remoteViews;
            }
            RemoteViews remoteViews2 = this.f4552e;
            if (remoteViews2 != null) {
                build2.bigContentView = remoteViews2;
            }
            RemoteViews remoteViews3 = this.f4556i;
            if (remoteViews3 != null) {
                build2.headsUpContentView = remoteViews3;
            }
            if (this.f4555h != 0) {
                if (build2.getGroup() != null && (build2.flags & RecognitionOptions.UPC_A) != 0 && this.f4555h == 2) {
                    h(build2);
                }
                if (build2.getGroup() != null && (build2.flags & RecognitionOptions.UPC_A) == 0 && this.f4555h == 1) {
                    h(build2);
                }
            }
            return build2;
        } else if (i8 >= 20) {
            this.f4549b.setExtras(this.f4554g);
            Notification build3 = this.f4549b.build();
            RemoteViews remoteViews4 = this.f4551d;
            if (remoteViews4 != null) {
                build3.contentView = remoteViews4;
            }
            RemoteViews remoteViews5 = this.f4552e;
            if (remoteViews5 != null) {
                build3.bigContentView = remoteViews5;
            }
            if (this.f4555h != 0) {
                if (build3.getGroup() != null && (build3.flags & RecognitionOptions.UPC_A) != 0 && this.f4555h == 2) {
                    h(build3);
                }
                if (build3.getGroup() != null && (build3.flags & RecognitionOptions.UPC_A) == 0 && this.f4555h == 1) {
                    h(build3);
                }
            }
            return build3;
        } else if (i8 >= 19) {
            SparseArray<Bundle> a9 = m.a(this.f4553f);
            if (a9 != null) {
                this.f4554g.putSparseParcelableArray("android.support.actionExtras", a9);
            }
            this.f4549b.setExtras(this.f4554g);
            Notification build4 = this.f4549b.build();
            RemoteViews remoteViews6 = this.f4551d;
            if (remoteViews6 != null) {
                build4.contentView = remoteViews6;
            }
            RemoteViews remoteViews7 = this.f4552e;
            if (remoteViews7 != null) {
                build4.bigContentView = remoteViews7;
            }
            return build4;
        } else if (i8 >= 16) {
            Notification build5 = this.f4549b.build();
            Bundle a10 = k.a(build5);
            Bundle bundle = new Bundle(this.f4554g);
            for (String str : this.f4554g.keySet()) {
                if (a10.containsKey(str)) {
                    bundle.remove(str);
                }
            }
            a10.putAll(bundle);
            SparseArray<Bundle> a11 = m.a(this.f4553f);
            if (a11 != null) {
                k.a(build5).putSparseParcelableArray("android.support.actionExtras", a11);
            }
            RemoteViews remoteViews8 = this.f4551d;
            if (remoteViews8 != null) {
                build5.contentView = remoteViews8;
            }
            RemoteViews remoteViews9 = this.f4552e;
            if (remoteViews9 != null) {
                build5.bigContentView = remoteViews9;
            }
            return build5;
        } else {
            return this.f4549b.getNotification();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Context f() {
        return this.f4548a;
    }
}
