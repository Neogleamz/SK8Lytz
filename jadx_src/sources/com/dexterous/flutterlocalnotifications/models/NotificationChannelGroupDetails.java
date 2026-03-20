package com.dexterous.flutterlocalnotifications.models;

import androidx.annotation.Keep;
import java.io.Serializable;
import java.util.Map;
@Keep
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class NotificationChannelGroupDetails implements Serializable {
    private static final String DESCRIPTION = "description";
    private static final String ID = "id";
    private static final String NAME = "name";
    public String description;
    public String id;
    public String name;

    public static NotificationChannelGroupDetails from(Map<String, Object> map) {
        NotificationChannelGroupDetails notificationChannelGroupDetails = new NotificationChannelGroupDetails();
        notificationChannelGroupDetails.id = (String) map.get(ID);
        notificationChannelGroupDetails.name = (String) map.get(NAME);
        notificationChannelGroupDetails.description = (String) map.get(DESCRIPTION);
        return notificationChannelGroupDetails;
    }
}
