package h1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: f  reason: collision with root package name */
    private static final Object f20223f = new Object();

    /* renamed from: g  reason: collision with root package name */
    private static a f20224g;

    /* renamed from: a  reason: collision with root package name */
    private final Context f20225a;

    /* renamed from: b  reason: collision with root package name */
    private final HashMap<BroadcastReceiver, ArrayList<c>> f20226b = new HashMap<>();

    /* renamed from: c  reason: collision with root package name */
    private final HashMap<String, ArrayList<c>> f20227c = new HashMap<>();

    /* renamed from: d  reason: collision with root package name */
    private final ArrayList<b> f20228d = new ArrayList<>();

    /* renamed from: e  reason: collision with root package name */
    private final Handler f20229e;

    /* renamed from: h1.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class HandlerC0171a extends Handler {
        HandlerC0171a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                super.handleMessage(message);
            } else {
                a.this.a();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        final Intent f20231a;

        /* renamed from: b  reason: collision with root package name */
        final ArrayList<c> f20232b;

        b(Intent intent, ArrayList<c> arrayList) {
            this.f20231a = intent;
            this.f20232b = arrayList;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        final IntentFilter f20233a;

        /* renamed from: b  reason: collision with root package name */
        final BroadcastReceiver f20234b;

        /* renamed from: c  reason: collision with root package name */
        boolean f20235c;

        /* renamed from: d  reason: collision with root package name */
        boolean f20236d;

        c(IntentFilter intentFilter, BroadcastReceiver broadcastReceiver) {
            this.f20233a = intentFilter;
            this.f20234b = broadcastReceiver;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder((int) RecognitionOptions.ITF);
            sb.append("Receiver{");
            sb.append(this.f20234b);
            sb.append(" filter=");
            sb.append(this.f20233a);
            if (this.f20236d) {
                sb.append(" DEAD");
            }
            sb.append("}");
            return sb.toString();
        }
    }

    private a(Context context) {
        this.f20225a = context;
        this.f20229e = new HandlerC0171a(context.getMainLooper());
    }

    public static a b(Context context) {
        a aVar;
        synchronized (f20223f) {
            if (f20224g == null) {
                f20224g = new a(context.getApplicationContext());
            }
            aVar = f20224g;
        }
        return aVar;
    }

    void a() {
        int size;
        b[] bVarArr;
        while (true) {
            synchronized (this.f20226b) {
                size = this.f20228d.size();
                if (size <= 0) {
                    return;
                }
                bVarArr = new b[size];
                this.f20228d.toArray(bVarArr);
                this.f20228d.clear();
            }
            for (int i8 = 0; i8 < size; i8++) {
                b bVar = bVarArr[i8];
                int size2 = bVar.f20232b.size();
                for (int i9 = 0; i9 < size2; i9++) {
                    c cVar = bVar.f20232b.get(i9);
                    if (!cVar.f20236d) {
                        cVar.f20234b.onReceive(this.f20225a, bVar.f20231a);
                    }
                }
            }
        }
    }

    public void c(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        synchronized (this.f20226b) {
            c cVar = new c(intentFilter, broadcastReceiver);
            ArrayList<c> arrayList = this.f20226b.get(broadcastReceiver);
            if (arrayList == null) {
                arrayList = new ArrayList<>(1);
                this.f20226b.put(broadcastReceiver, arrayList);
            }
            arrayList.add(cVar);
            for (int i8 = 0; i8 < intentFilter.countActions(); i8++) {
                String action = intentFilter.getAction(i8);
                ArrayList<c> arrayList2 = this.f20227c.get(action);
                if (arrayList2 == null) {
                    arrayList2 = new ArrayList<>(1);
                    this.f20227c.put(action, arrayList2);
                }
                arrayList2.add(cVar);
            }
        }
    }

    public boolean d(Intent intent) {
        int i8;
        String str;
        ArrayList arrayList;
        ArrayList<c> arrayList2;
        String str2;
        synchronized (this.f20226b) {
            String action = intent.getAction();
            String resolveTypeIfNeeded = intent.resolveTypeIfNeeded(this.f20225a.getContentResolver());
            Uri data = intent.getData();
            String scheme = intent.getScheme();
            Set<String> categories = intent.getCategories();
            boolean z4 = (intent.getFlags() & 8) != 0;
            if (z4) {
                Log.v("LocalBroadcastManager", "Resolving type " + resolveTypeIfNeeded + " scheme " + scheme + " of intent " + intent);
            }
            ArrayList<c> arrayList3 = this.f20227c.get(intent.getAction());
            if (arrayList3 != null) {
                if (z4) {
                    Log.v("LocalBroadcastManager", "Action list: " + arrayList3);
                }
                ArrayList arrayList4 = null;
                int i9 = 0;
                while (i9 < arrayList3.size()) {
                    c cVar = arrayList3.get(i9);
                    if (z4) {
                        Log.v("LocalBroadcastManager", "Matching against filter " + cVar.f20233a);
                    }
                    if (cVar.f20235c) {
                        if (z4) {
                            Log.v("LocalBroadcastManager", "  Filter's target already added");
                        }
                        i8 = i9;
                        arrayList2 = arrayList3;
                        str = action;
                        str2 = resolveTypeIfNeeded;
                        arrayList = arrayList4;
                    } else {
                        i8 = i9;
                        str = action;
                        arrayList = arrayList4;
                        arrayList2 = arrayList3;
                        str2 = resolveTypeIfNeeded;
                        int match = cVar.f20233a.match(action, resolveTypeIfNeeded, scheme, data, categories, "LocalBroadcastManager");
                        if (match >= 0) {
                            if (z4) {
                                Log.v("LocalBroadcastManager", "  Filter matched!  match=0x" + Integer.toHexString(match));
                            }
                            arrayList4 = arrayList == null ? new ArrayList() : arrayList;
                            arrayList4.add(cVar);
                            cVar.f20235c = true;
                            i9 = i8 + 1;
                            action = str;
                            arrayList3 = arrayList2;
                            resolveTypeIfNeeded = str2;
                        } else if (z4) {
                            Log.v("LocalBroadcastManager", "  Filter did not match: " + (match != -4 ? match != -3 ? match != -2 ? match != -1 ? "unknown reason" : "type" : "data" : "action" : "category"));
                        }
                    }
                    arrayList4 = arrayList;
                    i9 = i8 + 1;
                    action = str;
                    arrayList3 = arrayList2;
                    resolveTypeIfNeeded = str2;
                }
                ArrayList arrayList5 = arrayList4;
                if (arrayList5 != null) {
                    for (int i10 = 0; i10 < arrayList5.size(); i10++) {
                        ((c) arrayList5.get(i10)).f20235c = false;
                    }
                    this.f20228d.add(new b(intent, arrayList5));
                    if (!this.f20229e.hasMessages(1)) {
                        this.f20229e.sendEmptyMessage(1);
                    }
                    return true;
                }
            }
            return false;
        }
    }

    public void e(BroadcastReceiver broadcastReceiver) {
        synchronized (this.f20226b) {
            ArrayList<c> remove = this.f20226b.remove(broadcastReceiver);
            if (remove == null) {
                return;
            }
            for (int size = remove.size() - 1; size >= 0; size--) {
                c cVar = remove.get(size);
                cVar.f20236d = true;
                for (int i8 = 0; i8 < cVar.f20233a.countActions(); i8++) {
                    String action = cVar.f20233a.getAction(i8);
                    ArrayList<c> arrayList = this.f20227c.get(action);
                    if (arrayList != null) {
                        for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                            c cVar2 = arrayList.get(size2);
                            if (cVar2.f20234b == broadcastReceiver) {
                                cVar2.f20236d = true;
                                arrayList.remove(size2);
                            }
                        }
                        if (arrayList.size() <= 0) {
                            this.f20227c.remove(action);
                        }
                    }
                }
            }
        }
    }
}
