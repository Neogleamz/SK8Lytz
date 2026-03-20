package com.dexterous.flutterlocalnotifications;

import com.dexterous.flutterlocalnotifications.models.NotificationDetails;
import java.io.Serializable;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ForegroundServiceStartParameter implements Serializable {

    /* renamed from: a  reason: collision with root package name */
    public final NotificationDetails f8857a;

    /* renamed from: b  reason: collision with root package name */
    public final int f8858b;

    /* renamed from: c  reason: collision with root package name */
    public final ArrayList<Integer> f8859c;

    public ForegroundServiceStartParameter(NotificationDetails notificationDetails, int i8, ArrayList<Integer> arrayList) {
        this.f8857a = notificationDetails;
        this.f8858b = i8;
        this.f8859c = arrayList;
    }

    public String toString() {
        return "ForegroundServiceStartParameter{notificationData=" + this.f8857a + ", startMode=" + this.f8858b + ", foregroundServiceTypes=" + this.f8859c + '}';
    }
}
