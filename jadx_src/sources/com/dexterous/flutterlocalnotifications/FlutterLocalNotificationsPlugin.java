package com.dexterous.flutterlocalnotifications;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import androidx.annotation.Keep;
import androidx.core.app.k;
import androidx.core.app.n;
import androidx.core.app.o;
import androidx.core.app.q;
import androidx.core.graphics.drawable.IconCompat;
import com.dexterous.flutterlocalnotifications.models.BitmapSource;
import com.dexterous.flutterlocalnotifications.models.DateTimeComponents;
import com.dexterous.flutterlocalnotifications.models.IconSource;
import com.dexterous.flutterlocalnotifications.models.MessageDetails;
import com.dexterous.flutterlocalnotifications.models.NotificationAction;
import com.dexterous.flutterlocalnotifications.models.NotificationChannelAction;
import com.dexterous.flutterlocalnotifications.models.NotificationChannelDetails;
import com.dexterous.flutterlocalnotifications.models.NotificationChannelGroupDetails;
import com.dexterous.flutterlocalnotifications.models.NotificationDetails;
import com.dexterous.flutterlocalnotifications.models.NotificationStyle;
import com.dexterous.flutterlocalnotifications.models.PersonDetails;
import com.dexterous.flutterlocalnotifications.models.RepeatInterval;
import com.dexterous.flutterlocalnotifications.models.ScheduleMode;
import com.dexterous.flutterlocalnotifications.models.ScheduledNotificationRepeatFrequency;
import com.dexterous.flutterlocalnotifications.models.SoundSource;
import com.dexterous.flutterlocalnotifications.models.styles.BigPictureStyleInformation;
import com.dexterous.flutterlocalnotifications.models.styles.BigTextStyleInformation;
import com.dexterous.flutterlocalnotifications.models.styles.DefaultStyleInformation;
import com.dexterous.flutterlocalnotifications.models.styles.InboxStyleInformation;
import com.dexterous.flutterlocalnotifications.models.styles.MessagingStyleInformation;
import com.dexterous.flutterlocalnotifications.models.styles.StyleInformation;
import com.dexterous.flutterlocalnotifications.utils.BooleanUtils;
import com.dexterous.flutterlocalnotifications.utils.StringUtils;
import io.flutter.plugin.common.i;
import io.flutter.plugin.common.j;
import io.flutter.plugin.common.l;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import yf.a;
@Keep
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class FlutterLocalNotificationsPlugin implements j.c, l.b, l.d, l.a, yf.a, zf.a {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String ACTION_ID = "actionId";
    private static final String ARE_NOTIFICATIONS_ENABLED_METHOD = "areNotificationsEnabled";
    private static final String CALLBACK_HANDLE = "callback_handle";
    private static final String CANCEL_ALL_METHOD = "cancelAll";
    private static final String CANCEL_ID = "id";
    private static final String CANCEL_METHOD = "cancel";
    static final String CANCEL_NOTIFICATION = "cancelNotification";
    private static final String CANCEL_TAG = "tag";
    private static final String CAN_SCHEDULE_EXACT_NOTIFICATIONS_METHOD = "canScheduleExactNotifications";
    private static final String CREATE_NOTIFICATION_CHANNEL_GROUP_METHOD = "createNotificationChannelGroup";
    private static final String CREATE_NOTIFICATION_CHANNEL_METHOD = "createNotificationChannel";
    private static final String DEFAULT_ICON = "defaultIcon";
    private static final String DELETE_NOTIFICATION_CHANNEL_GROUP_METHOD = "deleteNotificationChannelGroup";
    private static final String DELETE_NOTIFICATION_CHANNEL_METHOD = "deleteNotificationChannel";
    private static final String DISPATCHER_HANDLE = "dispatcher_handle";
    private static final String DRAWABLE = "drawable";
    private static final String EXACT_ALARMS_PERMISSION_ERROR_CODE = "exact_alarms_not_permitted";
    static final int EXACT_ALARM_PERMISSION_REQUEST_CODE = 2;
    static final int FULL_SCREEN_INTENT_PERMISSION_REQUEST_CODE = 3;
    private static final String GET_ACTIVE_NOTIFICATIONS_ERROR_MESSAGE = "Android version must be 6.0 or newer to use getActiveNotifications";
    private static final String GET_ACTIVE_NOTIFICATIONS_METHOD = "getActiveNotifications";
    private static final String GET_ACTIVE_NOTIFICATION_MESSAGING_STYLE_ERROR_CODE = "getActiveNotificationMessagingStyleError";
    private static final String GET_ACTIVE_NOTIFICATION_MESSAGING_STYLE_METHOD = "getActiveNotificationMessagingStyle";
    private static final String GET_CALLBACK_HANDLE_METHOD = "getCallbackHandle";
    private static final String GET_NOTIFICATION_APP_LAUNCH_DETAILS_METHOD = "getNotificationAppLaunchDetails";
    private static final String GET_NOTIFICATION_CHANNELS_ERROR_CODE = "getNotificationChannelsError";
    private static final String GET_NOTIFICATION_CHANNELS_METHOD = "getNotificationChannels";
    private static final String INITIALIZE_METHOD = "initialize";
    private static final String INPUT = "input";
    private static final String INPUT_RESULT = "FlutterLocalNotificationsPluginInputResult";
    private static final String INVALID_BIG_PICTURE_ERROR_CODE = "invalid_big_picture";
    private static final String INVALID_DRAWABLE_RESOURCE_ERROR_MESSAGE = "The resource %s could not be found. Please make sure it has been added as a drawable resource to your Android head project.";
    private static final String INVALID_ICON_ERROR_CODE = "invalid_icon";
    private static final String INVALID_LARGE_ICON_ERROR_CODE = "invalid_large_icon";
    private static final String INVALID_LED_DETAILS_ERROR_CODE = "invalid_led_details";
    private static final String INVALID_LED_DETAILS_ERROR_MESSAGE = "Must specify both ledOnMs and ledOffMs to configure the blink cycle on older versions of Android before Oreo";
    private static final String INVALID_RAW_RESOURCE_ERROR_MESSAGE = "The resource %s could not be found. Please make sure it has been added as a raw resource to your Android head project.";
    private static final String INVALID_SOUND_ERROR_CODE = "invalid_sound";
    private static final String METHOD_CHANNEL = "dexterous.com/flutter/local_notifications";
    static String NOTIFICATION_DETAILS = "notificationDetails";
    static final String NOTIFICATION_ID = "notificationId";
    private static final String NOTIFICATION_LAUNCHED_APP = "notificationLaunchedApp";
    static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;
    private static final String NOTIFICATION_RESPONSE_TYPE = "notificationResponseType";
    static final String NOTIFICATION_TAG = "notificationTag";
    static final String PAYLOAD = "payload";
    private static final String PENDING_NOTIFICATION_REQUESTS_METHOD = "pendingNotificationRequests";
    private static final String PERIODICALLY_SHOW_METHOD = "periodicallyShow";
    private static final String PERIODICALLY_SHOW_WITH_DURATION = "periodicallyShowWithDuration";
    private static final String PERMISSION_REQUEST_IN_PROGRESS_ERROR_CODE = "permissionRequestInProgress";
    private static final String PERMISSION_REQUEST_IN_PROGRESS_ERROR_MESSAGE = "Another permission request is already in progress";
    private static final String REQUEST_EXACT_ALARMS_PERMISSION_METHOD = "requestExactAlarmsPermission";
    private static final String REQUEST_FULL_SCREEN_INTENT_PERMISSION_METHOD = "requestFullScreenIntentPermission";
    private static final String REQUEST_NOTIFICATIONS_PERMISSION_METHOD = "requestNotificationsPermission";
    private static final String SCHEDULED_NOTIFICATIONS = "scheduled_notifications";
    private static final String SELECT_FOREGROUND_NOTIFICATION_ACTION = "SELECT_FOREGROUND_NOTIFICATION";
    private static final String SELECT_NOTIFICATION = "SELECT_NOTIFICATION";
    private static final String SHARED_PREFERENCES_KEY = "notification_plugin_cache";
    private static final String SHOW_METHOD = "show";
    private static final String START_FOREGROUND_SERVICE = "startForegroundService";
    private static final String STOP_FOREGROUND_SERVICE = "stopForegroundService";
    private static final String TAG = "FLTLocalNotifPlugin";
    private static final String UNSUPPORTED_OS_VERSION_ERROR_CODE = "unsupported_os_version";
    private static final String ZONED_SCHEDULE_METHOD = "zonedSchedule";
    static com.google.gson.e gson;
    private Context applicationContext;
    private com.dexterous.flutterlocalnotifications.a callback;
    private j channel;
    private Activity mainActivity;
    private g permissionRequestProgress = g.None;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends com.google.gson.reflect.a<ArrayList<NotificationDetails>> {
        a() {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements com.dexterous.flutterlocalnotifications.a {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ j.d f8842a;

        b(j.d dVar) {
            this.f8842a = dVar;
        }

        @Override // com.dexterous.flutterlocalnotifications.a
        public void a(String str) {
            this.f8842a.a(FlutterLocalNotificationsPlugin.PERMISSION_REQUEST_IN_PROGRESS_ERROR_CODE, str, (Object) null);
        }

        @Override // com.dexterous.flutterlocalnotifications.a
        public void b(boolean z4) {
            this.f8842a.success(Boolean.valueOf(z4));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements com.dexterous.flutterlocalnotifications.a {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ j.d f8844a;

        c(j.d dVar) {
            this.f8844a = dVar;
        }

        @Override // com.dexterous.flutterlocalnotifications.a
        public void a(String str) {
            this.f8844a.a(FlutterLocalNotificationsPlugin.PERMISSION_REQUEST_IN_PROGRESS_ERROR_CODE, str, (Object) null);
        }

        @Override // com.dexterous.flutterlocalnotifications.a
        public void b(boolean z4) {
            this.f8844a.success(Boolean.valueOf(z4));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d implements com.dexterous.flutterlocalnotifications.a {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ j.d f8846a;

        d(j.d dVar) {
            this.f8846a = dVar;
        }

        @Override // com.dexterous.flutterlocalnotifications.a
        public void a(String str) {
            this.f8846a.a(FlutterLocalNotificationsPlugin.PERMISSION_REQUEST_IN_PROGRESS_ERROR_CODE, str, (Object) null);
        }

        @Override // com.dexterous.flutterlocalnotifications.a
        public void b(boolean z4) {
            this.f8846a.success(Boolean.valueOf(z4));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class e {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f8848a;

        /* renamed from: b  reason: collision with root package name */
        static final /* synthetic */ int[] f8849b;

        /* renamed from: c  reason: collision with root package name */
        static final /* synthetic */ int[] f8850c;

        static {
            int[] iArr = new int[NotificationStyle.values().length];
            f8850c = iArr;
            try {
                iArr[NotificationStyle.BigPicture.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f8850c[NotificationStyle.BigText.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f8850c[NotificationStyle.Inbox.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f8850c[NotificationStyle.Messaging.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f8850c[NotificationStyle.Media.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            int[] iArr2 = new int[IconSource.values().length];
            f8849b = iArr2;
            try {
                iArr2[IconSource.DrawableResource.ordinal()] = 1;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f8849b[IconSource.BitmapFilePath.ordinal()] = 2;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f8849b[IconSource.ContentUri.ordinal()] = 3;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f8849b[IconSource.FlutterBitmapAsset.ordinal()] = 4;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f8849b[IconSource.ByteArray.ordinal()] = 5;
            } catch (NoSuchFieldError unused10) {
            }
            int[] iArr3 = new int[RepeatInterval.values().length];
            f8848a = iArr3;
            try {
                iArr3[RepeatInterval.EveryMinute.ordinal()] = 1;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                f8848a[RepeatInterval.Hourly.ordinal()] = 2;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                f8848a[RepeatInterval.Daily.ordinal()] = 3;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                f8848a[RepeatInterval.Weekly.ordinal()] = 4;
            } catch (NoSuchFieldError unused14) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f extends h {
        public f() {
            super(FlutterLocalNotificationsPlugin.EXACT_ALARMS_PERMISSION_ERROR_CODE, "Exact alarms are not permitted");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum g {
        None,
        RequestingNotificationPermission,
        RequestingExactAlarmsPermission,
        RequestingFullScreenIntentPermission
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h extends RuntimeException {

        /* renamed from: a  reason: collision with root package name */
        public final String f8856a;

        h(String str, String str2) {
            super(str2);
            this.f8856a = str;
        }
    }

    private static void applyGrouping(NotificationDetails notificationDetails, k.e eVar) {
        boolean z4;
        if (StringUtils.isNullOrEmpty(notificationDetails.groupKey).booleanValue()) {
            z4 = false;
        } else {
            eVar.z(notificationDetails.groupKey);
            z4 = true;
        }
        if (z4) {
            if (BooleanUtils.getValue(notificationDetails.setAsGroupSummary)) {
                eVar.B(true);
            }
            eVar.A(notificationDetails.groupAlertBehavior.intValue());
        }
    }

    private void areNotificationsEnabled(j.d dVar) {
        dVar.success(Boolean.valueOf(getNotificationManager(this.applicationContext).a()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static com.google.gson.e buildGson() {
        if (gson == null) {
            gson = new com.google.gson.f().d(ScheduleMode.class, new ScheduleMode.a()).e(RuntimeTypeAdapterFactory.of(StyleInformation.class).registerSubtype(DefaultStyleInformation.class).registerSubtype(BigTextStyleInformation.class).registerSubtype(BigPictureStyleInformation.class).registerSubtype(InboxStyleInformation.class).registerSubtype(MessagingStyleInformation.class)).b();
        }
        return gson;
    }

    private static o buildPerson(Context context, PersonDetails personDetails) {
        IconSource iconSource;
        if (personDetails == null) {
            return null;
        }
        o.b bVar = new o.b();
        bVar.b(BooleanUtils.getValue(personDetails.bot));
        Object obj = personDetails.icon;
        if (obj != null && (iconSource = personDetails.iconBitmapSource) != null) {
            bVar.c(getIconFromSource(context, obj, iconSource));
        }
        bVar.d(BooleanUtils.getValue(personDetails.important));
        String str = personDetails.key;
        if (str != null) {
            bVar.e(str);
        }
        String str2 = personDetails.name;
        if (str2 != null) {
            bVar.f(str2);
        }
        String str3 = personDetails.uri;
        if (str3 != null) {
            bVar.g(str3);
        }
        return bVar.a();
    }

    private static long calculateNextNotificationTrigger(long j8, long j9) {
        while (j8 < System.currentTimeMillis()) {
            j8 += j9;
        }
        return j8;
    }

    private static long calculateRepeatIntervalMilliseconds(NotificationDetails notificationDetails) {
        Integer num = notificationDetails.repeatIntervalMilliseconds;
        if (num != null) {
            return num.intValue();
        }
        int i8 = e.f8848a[notificationDetails.repeatInterval.ordinal()];
        if (i8 != 1) {
            if (i8 != 2) {
                if (i8 != 3) {
                    return i8 != 4 ? 0L : 604800000L;
                }
                return 86400000L;
            }
            return 3600000L;
        }
        return 60000L;
    }

    private static Boolean canCreateNotificationChannel(Context context, NotificationChannelDetails notificationChannelDetails) {
        NotificationChannelAction notificationChannelAction;
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = ((NotificationManager) context.getSystemService("notification")).getNotificationChannel(notificationChannelDetails.id);
            return Boolean.valueOf((notificationChannel == null && ((notificationChannelAction = notificationChannelDetails.channelAction) == null || notificationChannelAction == NotificationChannelAction.CreateIfNotExists)) || (notificationChannel != null && notificationChannelDetails.channelAction == NotificationChannelAction.Update));
        }
        return Boolean.FALSE;
    }

    private void cancel(i iVar, j.d dVar) {
        Map map = (Map) iVar.b();
        cancelNotification((Integer) map.get(CANCEL_ID), (String) map.get(CANCEL_TAG));
        dVar.success((Object) null);
    }

    private void cancelAllNotifications(j.d dVar) {
        getNotificationManager(this.applicationContext).d();
        ArrayList<NotificationDetails> loadScheduledNotifications = loadScheduledNotifications(this.applicationContext);
        if (loadScheduledNotifications == null || loadScheduledNotifications.isEmpty()) {
            dVar.success((Object) null);
            return;
        }
        Intent intent = new Intent(this.applicationContext, ScheduledNotificationReceiver.class);
        Iterator<NotificationDetails> it = loadScheduledNotifications.iterator();
        while (it.hasNext()) {
            getAlarmManager(this.applicationContext).cancel(getBroadcastPendingIntent(this.applicationContext, it.next().id.intValue(), intent));
        }
        saveScheduledNotifications(this.applicationContext, new ArrayList());
        dVar.success((Object) null);
    }

    private void cancelNotification(Integer num, String str) {
        getAlarmManager(this.applicationContext).cancel(getBroadcastPendingIntent(this.applicationContext, num.intValue(), new Intent(this.applicationContext, ScheduledNotificationReceiver.class)));
        n notificationManager = getNotificationManager(this.applicationContext);
        if (str == null) {
            notificationManager.b(num.intValue());
        } else {
            notificationManager.c(str, num.intValue());
        }
        removeNotificationFromCache(this.applicationContext, num);
    }

    private static byte[] castObjectToByteArray(Object obj) {
        if (obj instanceof ArrayList) {
            ArrayList arrayList = (ArrayList) obj;
            byte[] bArr = new byte[arrayList.size()];
            for (int i8 = 0; i8 < arrayList.size(); i8++) {
                bArr[i8] = (byte) ((Double) arrayList.get(i8)).intValue();
            }
            return bArr;
        }
        return (byte[]) obj;
    }

    private static void checkCanScheduleExactAlarms(AlarmManager alarmManager) {
        if (Build.VERSION.SDK_INT >= 31 && !alarmManager.canScheduleExactAlarms()) {
            throw new f();
        }
    }

    private static k.h.a createMessage(Context context, MessageDetails messageDetails) {
        String str;
        k.h.a aVar = new k.h.a(messageDetails.text, messageDetails.timestamp.longValue(), buildPerson(context, messageDetails.person));
        String str2 = messageDetails.dataUri;
        if (str2 != null && (str = messageDetails.dataMimeType) != null) {
            aVar.j(str, Uri.parse(str2));
        }
        return aVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Notification createNotification(Context context, NotificationDetails notificationDetails) {
        Intent intent;
        String str;
        int i8;
        int i9;
        PendingIntent broadcast;
        IconSource iconSource;
        NotificationChannelDetails fromNotificationDetails = NotificationChannelDetails.fromNotificationDetails(notificationDetails);
        if (canCreateNotificationChannel(context, fromNotificationDetails).booleanValue()) {
            setupNotificationChannel(context, fromNotificationDetails);
        }
        Intent launchIntent = getLaunchIntent(context);
        launchIntent.setAction(SELECT_NOTIFICATION);
        launchIntent.putExtra(NOTIFICATION_ID, notificationDetails.id);
        launchIntent.putExtra(PAYLOAD, notificationDetails.payload);
        int i10 = 23;
        PendingIntent activity = PendingIntent.getActivity(context, notificationDetails.id.intValue(), launchIntent, Build.VERSION.SDK_INT >= 23 ? 201326592 : 134217728);
        DefaultStyleInformation defaultStyleInformation = (DefaultStyleInformation) notificationDetails.styleInformation;
        k.e H = new k.e(context, notificationDetails.channelId).u(defaultStyleInformation.htmlFormatTitle.booleanValue() ? fromHtml(notificationDetails.title) : notificationDetails.title).t(defaultStyleInformation.htmlFormatBody.booleanValue() ? fromHtml(notificationDetails.body) : notificationDetails.body).R(notificationDetails.ticker).m(BooleanUtils.getValue(notificationDetails.autoCancel)).s(activity).I(notificationDetails.priority.intValue()).G(BooleanUtils.getValue(notificationDetails.ongoing)).M(BooleanUtils.getValue(notificationDetails.silent)).H(BooleanUtils.getValue(notificationDetails.onlyAlertOnce));
        if (notificationDetails.actions != null) {
            int intValue = notificationDetails.id.intValue() * 16;
            for (NotificationAction notificationAction : notificationDetails.actions) {
                IconCompat iconCompat = null;
                if (!TextUtils.isEmpty(notificationAction.icon) && (iconSource = notificationAction.iconSource) != null) {
                    iconCompat = getIconFromSource(context, notificationAction.icon, iconSource);
                }
                Boolean bool = notificationAction.showsUserInterface;
                if (bool == null || !bool.booleanValue()) {
                    intent = new Intent(context, ActionBroadcastReceiver.class);
                    str = "com.dexterous.flutterlocalnotifications.ActionBroadcastReceiver.ACTION_TAPPED";
                } else {
                    intent = getLaunchIntent(context);
                    str = SELECT_FOREGROUND_NOTIFICATION_ACTION;
                }
                intent.setAction(str);
                intent.putExtra(NOTIFICATION_ID, notificationDetails.id).putExtra(NOTIFICATION_TAG, notificationDetails.tag).putExtra(ACTION_ID, notificationAction.id).putExtra(CANCEL_NOTIFICATION, notificationAction.cancelNotification).putExtra(PAYLOAD, notificationDetails.payload);
                List<NotificationAction.NotificationActionInput> list = notificationAction.actionInputs;
                if (list == null || list.isEmpty()) {
                    if (Build.VERSION.SDK_INT >= i10) {
                        i8 = 201326592;
                    }
                    i8 = 134217728;
                } else {
                    if (Build.VERSION.SDK_INT >= 31) {
                        i8 = 167772160;
                    }
                    i8 = 134217728;
                }
                Boolean bool2 = notificationAction.showsUserInterface;
                if (bool2 == null || !bool2.booleanValue()) {
                    i9 = intValue + 1;
                    broadcast = PendingIntent.getBroadcast(context, intValue, intent, i8);
                } else {
                    i9 = intValue + 1;
                    broadcast = PendingIntent.getActivity(context, intValue, intent, i8);
                }
                intValue = i9;
                SpannableString spannableString = new SpannableString(notificationAction.title);
                if (notificationAction.titleColor != null) {
                    spannableString.setSpan(new ForegroundColorSpan(notificationAction.titleColor.intValue()), 0, spannableString.length(), 0);
                }
                k.a.C0030a c0030a = new k.a.C0030a(iconCompat, spannableString, broadcast);
                Boolean bool3 = notificationAction.contextual;
                if (bool3 != null) {
                    c0030a.e(bool3.booleanValue());
                }
                Boolean bool4 = notificationAction.showsUserInterface;
                if (bool4 != null) {
                    c0030a.f(bool4.booleanValue());
                }
                Boolean bool5 = notificationAction.allowGeneratedReplies;
                if (bool5 != null) {
                    c0030a.d(bool5.booleanValue());
                }
                List<NotificationAction.NotificationActionInput> list2 = notificationAction.actionInputs;
                if (list2 != null) {
                    for (NotificationAction.NotificationActionInput notificationActionInput : list2) {
                        q.e e8 = new q.e(INPUT_RESULT).e(notificationActionInput.f8866c);
                        Boolean bool6 = notificationActionInput.f8865b;
                        if (bool6 != null) {
                            e8.c(bool6.booleanValue());
                        }
                        List<String> list3 = notificationActionInput.f8867d;
                        if (list3 != null) {
                            for (String str2 : list3) {
                                e8.b(str2, true);
                            }
                        }
                        List<String> list4 = notificationActionInput.f8864a;
                        if (list4 != null) {
                            e8.d((CharSequence[]) list4.toArray(new CharSequence[0]));
                        }
                        c0030a.a(e8.a());
                    }
                }
                H.b(c0030a.b());
                i10 = 23;
            }
        }
        setSmallIcon(context, notificationDetails, H);
        H.C(getBitmapFromSource(context, notificationDetails.largeIcon, notificationDetails.largeIconBitmapSource));
        Integer num = notificationDetails.color;
        if (num != null) {
            H.q(num.intValue());
        }
        Boolean bool7 = notificationDetails.colorized;
        if (bool7 != null) {
            H.r(bool7.booleanValue());
        }
        Boolean bool8 = notificationDetails.showWhen;
        if (bool8 != null) {
            H.L(BooleanUtils.getValue(bool8));
        }
        Long l8 = notificationDetails.when;
        if (l8 != null) {
            H.W(l8.longValue());
        }
        Boolean bool9 = notificationDetails.usesChronometer;
        if (bool9 != null) {
            H.T(bool9.booleanValue());
        }
        Boolean bool10 = notificationDetails.chronometerCountDown;
        if (bool10 != null && Build.VERSION.SDK_INT >= 24) {
            H.p(bool10.booleanValue());
        }
        if (BooleanUtils.getValue(notificationDetails.fullScreenIntent)) {
            H.y(activity, true);
        }
        if (!StringUtils.isNullOrEmpty(notificationDetails.shortcutId).booleanValue()) {
            H.K(notificationDetails.shortcutId);
        }
        if (!StringUtils.isNullOrEmpty(notificationDetails.subText).booleanValue()) {
            H.Q(notificationDetails.subText);
        }
        Integer num2 = notificationDetails.number;
        if (num2 != null) {
            H.F(num2.intValue());
        }
        setVisibility(notificationDetails, H);
        applyGrouping(notificationDetails, H);
        setSound(context, notificationDetails, H);
        setVibrationPattern(notificationDetails, H);
        setLights(notificationDetails, H);
        setStyle(context, notificationDetails, H);
        setProgress(notificationDetails, H);
        setCategory(notificationDetails, H);
        setTimeoutAfter(notificationDetails, H);
        Notification c9 = H.c();
        int[] iArr = notificationDetails.additionalFlags;
        if (iArr != null && iArr.length > 0) {
            for (int i11 : iArr) {
                c9.flags = i11 | c9.flags;
            }
        }
        return c9;
    }

    private void createNotificationChannel(i iVar, j.d dVar) {
        setupNotificationChannel(this.applicationContext, NotificationChannelDetails.from((Map) iVar.b()));
        dVar.success((Object) null);
    }

    private void createNotificationChannelGroup(i iVar, j.d dVar) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 26) {
            NotificationChannelGroupDetails from = NotificationChannelGroupDetails.from((Map) iVar.b());
            NotificationManager notificationManager = (NotificationManager) this.applicationContext.getSystemService("notification");
            NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(from.id, from.name);
            if (i8 >= 28) {
                notificationChannelGroup.setDescription(from.description);
            }
            notificationManager.createNotificationChannelGroup(notificationChannelGroup);
        }
        dVar.success((Object) null);
    }

    private void deleteNotificationChannel(i iVar, j.d dVar) {
        if (Build.VERSION.SDK_INT >= 26) {
            ((NotificationManager) this.applicationContext.getSystemService("notification")).deleteNotificationChannel((String) iVar.b());
        }
        dVar.success((Object) null);
    }

    private void deleteNotificationChannelGroup(i iVar, j.d dVar) {
        if (Build.VERSION.SDK_INT >= 26) {
            ((NotificationManager) this.applicationContext.getSystemService("notification")).deleteNotificationChannelGroup((String) iVar.b());
        }
        dVar.success((Object) null);
    }

    private Map<String, Object> describeIcon(IconCompat iconCompat) {
        IconSource iconSource;
        String resourceEntryName;
        if (iconCompat == null) {
            return null;
        }
        int u8 = iconCompat.u();
        if (u8 == 2) {
            iconSource = IconSource.DrawableResource;
            resourceEntryName = this.applicationContext.getResources().getResourceEntryName(iconCompat.r());
        } else if (u8 != 4) {
            return null;
        } else {
            iconSource = IconSource.ContentUri;
            resourceEntryName = iconCompat.v().toString();
        }
        HashMap hashMap = new HashMap();
        hashMap.put("source", Integer.valueOf(iconSource.ordinal()));
        hashMap.put("data", resourceEntryName);
        return hashMap;
    }

    private Map<String, Object> describePerson(o oVar) {
        if (oVar == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("key", oVar.d());
        hashMap.put("name", oVar.e());
        hashMap.put("uri", oVar.f());
        hashMap.put("bot", Boolean.valueOf(oVar.g()));
        hashMap.put("important", Boolean.valueOf(oVar.h()));
        hashMap.put("icon", describeIcon(oVar.c()));
        return hashMap;
    }

    private NotificationDetails extractNotificationDetails(j.d dVar, Map<String, Object> map) {
        NotificationDetails from = NotificationDetails.from(map);
        if (hasInvalidIcon(dVar, from.icon) || hasInvalidLargeIcon(dVar, from.largeIcon, from.largeIconBitmapSource) || hasInvalidBigPictureResources(dVar, from) || hasInvalidRawSoundResource(dVar, from) || hasInvalidLedDetails(dVar, from)) {
            return null;
        }
        return from;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Map<String, Object> extractNotificationResponseMap(Intent intent) {
        int intExtra = intent.getIntExtra(NOTIFICATION_ID, 0);
        HashMap hashMap = new HashMap();
        hashMap.put(NOTIFICATION_ID, Integer.valueOf(intExtra));
        hashMap.put(NOTIFICATION_TAG, intent.getStringExtra(NOTIFICATION_TAG));
        hashMap.put(ACTION_ID, intent.getStringExtra(ACTION_ID));
        hashMap.put(PAYLOAD, intent.getStringExtra(PAYLOAD));
        Bundle k8 = q.k(intent);
        if (k8 != null) {
            hashMap.put(INPUT, k8.getString(INPUT_RESULT));
        }
        if (SELECT_NOTIFICATION.equals(intent.getAction())) {
            hashMap.put(NOTIFICATION_RESPONSE_TYPE, 0);
        }
        if (SELECT_FOREGROUND_NOTIFICATION_ACTION.equals(intent.getAction())) {
            hashMap.put(NOTIFICATION_RESPONSE_TYPE, 1);
        }
        return hashMap;
    }

    private static Spanned fromHtml(String str) {
        if (str == null) {
            return null;
        }
        return Build.VERSION.SDK_INT >= 24 ? Html.fromHtml(str, 0) : Html.fromHtml(str);
    }

    private void getActiveNotificationMessagingStyle(i iVar, j.d dVar) {
        StatusBarNotification[] activeNotifications;
        Notification notification;
        if (Build.VERSION.SDK_INT < 23) {
            dVar.a(UNSUPPORTED_OS_VERSION_ERROR_CODE, "Android version must be 6.0 or newer to use getActiveNotificationMessagingStyle", (Object) null);
            return;
        }
        NotificationManager notificationManager = (NotificationManager) this.applicationContext.getSystemService("notification");
        try {
            Map map = (Map) iVar.b();
            int intValue = ((Integer) map.get(CANCEL_ID)).intValue();
            String str = (String) map.get(CANCEL_TAG);
            for (StatusBarNotification statusBarNotification : notificationManager.getActiveNotifications()) {
                if (statusBarNotification.getId() == intValue && (str == null || str.equals(statusBarNotification.getTag()))) {
                    notification = statusBarNotification.getNotification();
                    break;
                }
            }
            notification = null;
            if (notification == null) {
                dVar.success((Object) null);
                return;
            }
            k.h y8 = k.h.y(notification);
            if (y8 == null) {
                dVar.success((Object) null);
                return;
            }
            HashMap hashMap = new HashMap();
            hashMap.put("groupConversation", Boolean.valueOf(y8.E()));
            hashMap.put("person", describePerson(y8.C()));
            hashMap.put("conversationTitle", y8.A());
            ArrayList arrayList = new ArrayList();
            for (k.h.a aVar : y8.B()) {
                HashMap hashMap2 = new HashMap();
                hashMap2.put("text", aVar.h());
                hashMap2.put("timestamp", Long.valueOf(aVar.i()));
                hashMap2.put("person", describePerson(aVar.g()));
                arrayList.add(hashMap2);
            }
            hashMap.put("messages", arrayList);
            dVar.success(hashMap);
        } catch (Throwable th) {
            dVar.a(GET_ACTIVE_NOTIFICATION_MESSAGING_STYLE_ERROR_CODE, th.getMessage(), Log.getStackTraceString(th));
        }
    }

    private void getActiveNotifications(j.d dVar) {
        if (Build.VERSION.SDK_INT < 23) {
            dVar.a(UNSUPPORTED_OS_VERSION_ERROR_CODE, GET_ACTIVE_NOTIFICATIONS_ERROR_MESSAGE, (Object) null);
            return;
        }
        try {
            StatusBarNotification[] activeNotifications = ((NotificationManager) this.applicationContext.getSystemService("notification")).getActiveNotifications();
            ArrayList arrayList = new ArrayList();
            for (StatusBarNotification statusBarNotification : activeNotifications) {
                HashMap hashMap = new HashMap();
                hashMap.put(CANCEL_ID, Integer.valueOf(statusBarNotification.getId()));
                Notification notification = statusBarNotification.getNotification();
                if (Build.VERSION.SDK_INT >= 26) {
                    hashMap.put("channelId", notification.getChannelId());
                }
                hashMap.put(CANCEL_TAG, statusBarNotification.getTag());
                hashMap.put("groupKey", notification.getGroup());
                hashMap.put("title", notification.extras.getCharSequence("android.title"));
                hashMap.put("body", notification.extras.getCharSequence("android.text"));
                hashMap.put("bigText", notification.extras.getCharSequence("android.bigText"));
                arrayList.add(hashMap);
            }
            dVar.success(arrayList);
        } catch (Throwable th) {
            dVar.a(UNSUPPORTED_OS_VERSION_ERROR_CODE, th.getMessage(), Log.getStackTraceString(th));
        }
    }

    private static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService("alarm");
    }

    private static Bitmap getBitmapFromSource(Context context, Object obj, BitmapSource bitmapSource) {
        if (bitmapSource == BitmapSource.DrawableResource) {
            return BitmapFactory.decodeResource(context.getResources(), getDrawableResourceId(context, (String) obj));
        }
        if (bitmapSource == BitmapSource.FilePath) {
            return BitmapFactory.decodeFile((String) obj);
        }
        if (bitmapSource == BitmapSource.ByteArray) {
            byte[] castObjectToByteArray = castObjectToByteArray(obj);
            return BitmapFactory.decodeByteArray(castObjectToByteArray, 0, castObjectToByteArray.length);
        }
        return null;
    }

    private static PendingIntent getBroadcastPendingIntent(Context context, int i8, Intent intent) {
        return PendingIntent.getBroadcast(context, i8, intent, Build.VERSION.SDK_INT >= 23 ? 201326592 : 134217728);
    }

    private void getCallbackHandle(j.d dVar) {
        dVar.success(new t2.a(this.applicationContext).c());
    }

    private static int getDrawableResourceId(Context context, String str) {
        return context.getResources().getIdentifier(str, DRAWABLE, context.getPackageName());
    }

    private static IconCompat getIconFromSource(Context context, Object obj, IconSource iconSource) {
        int i8 = e.f8849b[iconSource.ordinal()];
        if (i8 != 1) {
            if (i8 != 2) {
                if (i8 != 3) {
                    if (i8 != 4) {
                        if (i8 != 5) {
                            return null;
                        }
                        byte[] castObjectToByteArray = castObjectToByteArray(obj);
                        return IconCompat.n(castObjectToByteArray, 0, castObjectToByteArray.length);
                    }
                    try {
                        AssetFileDescriptor openFd = context.getAssets().openFd(sf.a.e().c().l((String) obj));
                        FileInputStream createInputStream = openFd.createInputStream();
                        IconCompat k8 = IconCompat.k(BitmapFactory.decodeStream(createInputStream));
                        createInputStream.close();
                        openFd.close();
                        return k8;
                    } catch (IOException e8) {
                        throw new RuntimeException(e8);
                    }
                }
                return IconCompat.m((String) obj);
            }
            return IconCompat.k(BitmapFactory.decodeFile((String) obj));
        }
        return IconCompat.o(context, getDrawableResourceId(context, (String) obj));
    }

    private static Intent getLaunchIntent(Context context) {
        return context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0108  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.HashMap<java.lang.String, java.lang.Object> getMappedNotificationChannel(android.app.NotificationChannel r9) {
        /*
            r8 = this;
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 26
            if (r1 < r2) goto L115
            java.lang.String r1 = r9.getId()
            java.lang.String r2 = "id"
            r0.put(r2, r1)
            java.lang.CharSequence r1 = r9.getName()
            java.lang.String r2 = "name"
            r0.put(r2, r1)
            java.lang.String r1 = r9.getDescription()
            java.lang.String r2 = "description"
            r0.put(r2, r1)
            java.lang.String r1 = r9.getGroup()
            java.lang.String r2 = "groupId"
            r0.put(r2, r1)
            boolean r1 = r9.canShowBadge()
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)
            java.lang.String r2 = "showBadge"
            r0.put(r2, r1)
            int r1 = r9.getImportance()
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            java.lang.String r2 = "importance"
            r0.put(r2, r1)
            android.net.Uri r1 = r9.getSound()
            r2 = 0
            java.lang.String r3 = "playSound"
            java.lang.String r4 = "sound"
            if (r1 != 0) goto L5d
        L54:
            r0.put(r4, r2)
            java.lang.Boolean r1 = java.lang.Boolean.FALSE
            r0.put(r3, r1)
            goto Ld0
        L5d:
            java.lang.Boolean r5 = java.lang.Boolean.TRUE
            r0.put(r3, r5)
            com.dexterous.flutterlocalnotifications.models.SoundSource[] r5 = com.dexterous.flutterlocalnotifications.models.SoundSource.values()
            java.util.List r5 = java.util.Arrays.asList(r5)
            java.lang.String r6 = r1.getScheme()
            java.lang.String r7 = "android.resource"
            boolean r6 = r6.equals(r7)
            java.lang.String r7 = "soundSource"
            if (r6 == 0) goto Lbc
            java.lang.String r1 = r1.toString()
            java.lang.String r6 = "/"
            java.lang.String[] r1 = r1.split(r6)
            int r6 = r1.length
            int r6 = r6 + (-1)
            r1 = r1[r6]
            java.lang.Integer r6 = r8.tryParseInt(r1)
            if (r6 != 0) goto L9b
            com.dexterous.flutterlocalnotifications.models.SoundSource r2 = com.dexterous.flutterlocalnotifications.models.SoundSource.RawResource
            int r2 = r5.indexOf(r2)
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r0.put(r7, r2)
            goto Lcd
        L9b:
            android.content.Context r1 = r8.applicationContext     // Catch: java.lang.Exception -> L54
            android.content.res.Resources r1 = r1.getResources()     // Catch: java.lang.Exception -> L54
            int r6 = r6.intValue()     // Catch: java.lang.Exception -> L54
            java.lang.String r1 = r1.getResourceEntryName(r6)     // Catch: java.lang.Exception -> L54
            if (r1 == 0) goto Ld0
            com.dexterous.flutterlocalnotifications.models.SoundSource r6 = com.dexterous.flutterlocalnotifications.models.SoundSource.RawResource     // Catch: java.lang.Exception -> L54
            int r5 = r5.indexOf(r6)     // Catch: java.lang.Exception -> L54
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch: java.lang.Exception -> L54
            r0.put(r7, r5)     // Catch: java.lang.Exception -> L54
            r0.put(r4, r1)     // Catch: java.lang.Exception -> L54
            goto Ld0
        Lbc:
            com.dexterous.flutterlocalnotifications.models.SoundSource r2 = com.dexterous.flutterlocalnotifications.models.SoundSource.Uri
            int r2 = r5.indexOf(r2)
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r0.put(r7, r2)
            java.lang.String r1 = r1.toString()
        Lcd:
            r0.put(r4, r1)
        Ld0:
            boolean r1 = r9.shouldVibrate()
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)
            java.lang.String r2 = "enableVibration"
            r0.put(r2, r1)
            long[] r1 = r9.getVibrationPattern()
            java.lang.String r2 = "vibrationPattern"
            r0.put(r2, r1)
            boolean r1 = r9.shouldShowLights()
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)
            java.lang.String r2 = "enableLights"
            r0.put(r2, r1)
            int r1 = r9.getLightColor()
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            java.lang.String r2 = "ledColor"
            r0.put(r2, r1)
            android.media.AudioAttributes r9 = r9.getAudioAttributes()
            if (r9 != 0) goto L108
            r9 = 5
            goto L10c
        L108:
            int r9 = r9.getUsage()
        L10c:
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            java.lang.String r1 = "audioAttributesUsage"
            r0.put(r1, r9)
        L115:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dexterous.flutterlocalnotifications.FlutterLocalNotificationsPlugin.getMappedNotificationChannel(android.app.NotificationChannel):java.util.HashMap");
    }

    private static String getNextFireDate(NotificationDetails notificationDetails) {
        LocalDateTime plusWeeks;
        ScheduledNotificationRepeatFrequency scheduledNotificationRepeatFrequency = notificationDetails.scheduledNotificationRepeatFrequency;
        if (scheduledNotificationRepeatFrequency == ScheduledNotificationRepeatFrequency.Daily) {
            plusWeeks = LocalDateTime.parse(notificationDetails.scheduledDateTime).plusDays(1L);
        } else if (scheduledNotificationRepeatFrequency != ScheduledNotificationRepeatFrequency.Weekly) {
            return null;
        } else {
            plusWeeks = LocalDateTime.parse(notificationDetails.scheduledDateTime).plusWeeks(1L);
        }
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(plusWeeks);
    }

    private static String getNextFireDateMatchingDateTimeComponents(NotificationDetails notificationDetails) {
        ZoneId of = ZoneId.of(notificationDetails.timeZoneName);
        ZonedDateTime of2 = ZonedDateTime.of(LocalDateTime.parse(notificationDetails.scheduledDateTime), of);
        ZonedDateTime now = ZonedDateTime.now(of);
        ZonedDateTime of3 = ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), of2.getHour(), of2.getMinute(), of2.getSecond(), of2.getNano(), of);
        while (of3.isBefore(now)) {
            of3 = of3.plusDays(1L);
        }
        DateTimeComponents dateTimeComponents = notificationDetails.matchDateTimeComponents;
        if (dateTimeComponents != DateTimeComponents.Time) {
            if (dateTimeComponents == DateTimeComponents.DayOfWeekAndTime) {
                while (of3.getDayOfWeek() != of2.getDayOfWeek()) {
                    of3 = of3.plusDays(1L);
                }
            } else if (dateTimeComponents == DateTimeComponents.DayOfMonthAndTime) {
                while (of3.getDayOfMonth() != of2.getDayOfMonth()) {
                    of3 = of3.plusDays(1L);
                }
            } else if (dateTimeComponents != DateTimeComponents.DateAndTime) {
                return null;
            } else {
                while (true) {
                    if (of3.getMonthValue() == of2.getMonthValue() && of3.getDayOfMonth() == of2.getDayOfMonth()) {
                        break;
                    }
                    of3 = of3.plusDays(1L);
                }
            }
        }
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(of3);
    }

    private void getNotificationAppLaunchDetails(j.d dVar) {
        HashMap hashMap = new HashMap();
        Boolean bool = Boolean.FALSE;
        Activity activity = this.mainActivity;
        if (activity != null) {
            Intent intent = activity.getIntent();
            Boolean valueOf = Boolean.valueOf(intent != null && (SELECT_NOTIFICATION.equals(intent.getAction()) || SELECT_FOREGROUND_NOTIFICATION_ACTION.equals(intent.getAction())) && !launchedActivityFromHistory(intent));
            if (valueOf.booleanValue()) {
                hashMap.put("notificationResponse", extractNotificationResponseMap(intent));
            }
            bool = valueOf;
        }
        hashMap.put(NOTIFICATION_LAUNCHED_APP, bool);
        dVar.success(hashMap);
    }

    private void getNotificationChannels(j.d dVar) {
        try {
            List<NotificationChannel> g8 = getNotificationManager(this.applicationContext).g();
            ArrayList arrayList = new ArrayList();
            for (NotificationChannel notificationChannel : g8) {
                arrayList.add(getMappedNotificationChannel(notificationChannel));
            }
            dVar.success(arrayList);
        } catch (Throwable th) {
            dVar.a(GET_NOTIFICATION_CHANNELS_ERROR_CODE, th.getMessage(), Log.getStackTraceString(th));
        }
    }

    private static n getNotificationManager(Context context) {
        return n.e(context);
    }

    private boolean hasInvalidBigPictureResources(j.d dVar, NotificationDetails notificationDetails) {
        if (notificationDetails.style == NotificationStyle.BigPicture) {
            BigPictureStyleInformation bigPictureStyleInformation = (BigPictureStyleInformation) notificationDetails.styleInformation;
            if (hasInvalidLargeIcon(dVar, bigPictureStyleInformation.largeIcon, bigPictureStyleInformation.largeIconBitmapSource)) {
                return true;
            }
            BitmapSource bitmapSource = bigPictureStyleInformation.bigPictureBitmapSource;
            if (bitmapSource == BitmapSource.DrawableResource) {
                String str = (String) bigPictureStyleInformation.bigPicture;
                return StringUtils.isNullOrEmpty(str).booleanValue() && !isValidDrawableResource(this.applicationContext, str, dVar, INVALID_BIG_PICTURE_ERROR_CODE);
            } else if (bitmapSource == BitmapSource.FilePath) {
                return StringUtils.isNullOrEmpty((String) bigPictureStyleInformation.bigPicture).booleanValue();
            } else {
                if (bitmapSource == BitmapSource.ByteArray) {
                    byte[] bArr = (byte[]) bigPictureStyleInformation.bigPicture;
                    return bArr == null || bArr.length == 0;
                }
                return false;
            }
        }
        return false;
    }

    private boolean hasInvalidIcon(j.d dVar, String str) {
        return (StringUtils.isNullOrEmpty(str).booleanValue() || isValidDrawableResource(this.applicationContext, str, dVar, INVALID_ICON_ERROR_CODE)) ? false : true;
    }

    private boolean hasInvalidLargeIcon(j.d dVar, Object obj, BitmapSource bitmapSource) {
        BitmapSource bitmapSource2 = BitmapSource.DrawableResource;
        if (bitmapSource != bitmapSource2 && bitmapSource != BitmapSource.FilePath) {
            return bitmapSource == BitmapSource.ByteArray && ((byte[]) obj).length == 0;
        }
        String str = (String) obj;
        return (StringUtils.isNullOrEmpty(str).booleanValue() || bitmapSource != bitmapSource2 || isValidDrawableResource(this.applicationContext, str, dVar, INVALID_LARGE_ICON_ERROR_CODE)) ? false : true;
    }

    private boolean hasInvalidLedDetails(j.d dVar, NotificationDetails notificationDetails) {
        if (notificationDetails.ledColor != null) {
            if (notificationDetails.ledOnMs == null || notificationDetails.ledOffMs == null) {
                dVar.a(INVALID_LED_DETAILS_ERROR_CODE, INVALID_LED_DETAILS_ERROR_MESSAGE, (Object) null);
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean hasInvalidRawSoundResource(j.d dVar, NotificationDetails notificationDetails) {
        SoundSource soundSource;
        if (StringUtils.isNullOrEmpty(notificationDetails.sound).booleanValue() || !(((soundSource = notificationDetails.soundSource) == null || soundSource == SoundSource.RawResource) && this.applicationContext.getResources().getIdentifier(notificationDetails.sound, "raw", this.applicationContext.getPackageName()) == 0)) {
            return false;
        }
        dVar.a(INVALID_SOUND_ERROR_CODE, String.format(INVALID_RAW_RESOURCE_ERROR_MESSAGE, notificationDetails.sound), (Object) null);
        return true;
    }

    private void initialize(i iVar, j.d dVar) {
        String str = (String) ((Map) iVar.b()).get(DEFAULT_ICON);
        if (isValidDrawableResource(this.applicationContext, str, dVar, INVALID_ICON_ERROR_CODE)) {
            Long a9 = u2.a.a(iVar.a(DISPATCHER_HANDLE));
            Long a10 = u2.a.a(iVar.a(CALLBACK_HANDLE));
            if (a9 != null && a10 != null) {
                new t2.a(this.applicationContext).e(a9, a10);
            }
            this.applicationContext.getSharedPreferences(SHARED_PREFERENCES_KEY, 0).edit().putString(DEFAULT_ICON, str).apply();
            dVar.success(Boolean.TRUE);
        }
    }

    private static boolean isValidDrawableResource(Context context, String str, j.d dVar, String str2) {
        if (context.getResources().getIdentifier(str, DRAWABLE, context.getPackageName()) == 0) {
            dVar.a(str2, String.format(INVALID_DRAWABLE_RESOURCE_ERROR_MESSAGE, str), (Object) null);
            return false;
        }
        return true;
    }

    private static boolean launchedActivityFromHistory(Intent intent) {
        return intent != null && (intent.getFlags() & 1048576) == 1048576;
    }

    private static ArrayList<NotificationDetails> loadScheduledNotifications(Context context) {
        ArrayList<NotificationDetails> arrayList = new ArrayList<>();
        String string = context.getSharedPreferences(SCHEDULED_NOTIFICATIONS, 0).getString(SCHEDULED_NOTIFICATIONS, null);
        return string != null ? (ArrayList) buildGson().m(string, new a().getType()) : arrayList;
    }

    private void pendingNotificationRequests(j.d dVar) {
        ArrayList<NotificationDetails> loadScheduledNotifications = loadScheduledNotifications(this.applicationContext);
        ArrayList arrayList = new ArrayList();
        Iterator<NotificationDetails> it = loadScheduledNotifications.iterator();
        while (it.hasNext()) {
            NotificationDetails next = it.next();
            HashMap hashMap = new HashMap();
            hashMap.put(CANCEL_ID, next.id);
            hashMap.put("title", next.title);
            hashMap.put("body", next.body);
            hashMap.put(PAYLOAD, next.payload);
            arrayList.add(hashMap);
        }
        dVar.success(arrayList);
    }

    private void processForegroundNotificationAction(Intent intent, Map<String, Object> map) {
        if (intent.getBooleanExtra(CANCEL_NOTIFICATION, false)) {
            n.e(this.applicationContext).b(((Integer) map.get(NOTIFICATION_ID)).intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void removeNotificationFromCache(Context context, Integer num) {
        ArrayList<NotificationDetails> loadScheduledNotifications = loadScheduledNotifications(context);
        Iterator<NotificationDetails> it = loadScheduledNotifications.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            } else if (it.next().id.equals(num)) {
                it.remove();
                break;
            }
        }
        saveScheduledNotifications(context, loadScheduledNotifications);
    }

    private void repeat(i iVar, j.d dVar) {
        NotificationDetails extractNotificationDetails = extractNotificationDetails(dVar, (Map) iVar.b());
        if (extractNotificationDetails != null) {
            try {
                repeatNotification(this.applicationContext, extractNotificationDetails, Boolean.TRUE);
                dVar.success((Object) null);
            } catch (h e8) {
                dVar.a(e8.f8856a, e8.getMessage(), (Object) null);
            }
        }
    }

    private static void repeatNotification(Context context, NotificationDetails notificationDetails, Boolean bool) {
        long calculateRepeatIntervalMilliseconds = calculateRepeatIntervalMilliseconds(notificationDetails);
        long longValue = notificationDetails.calledAt.longValue();
        if (notificationDetails.repeatTime != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(11, notificationDetails.repeatTime.hour.intValue());
            calendar.set(12, notificationDetails.repeatTime.minute.intValue());
            calendar.set(13, notificationDetails.repeatTime.second.intValue());
            Integer num = notificationDetails.day;
            if (num != null) {
                calendar.set(7, num.intValue());
            }
            longValue = calendar.getTimeInMillis();
        }
        long calculateNextNotificationTrigger = calculateNextNotificationTrigger(longValue, calculateRepeatIntervalMilliseconds);
        String u8 = buildGson().u(notificationDetails);
        Intent intent = new Intent(context, ScheduledNotificationReceiver.class);
        intent.putExtra(NOTIFICATION_DETAILS, u8);
        PendingIntent broadcastPendingIntent = getBroadcastPendingIntent(context, notificationDetails.id.intValue(), intent);
        AlarmManager alarmManager = getAlarmManager(context);
        if (notificationDetails.scheduleMode == null) {
            notificationDetails.scheduleMode = ScheduleMode.inexact;
        }
        if (notificationDetails.scheduleMode.useAllowWhileIdle()) {
            setupAllowWhileIdleAlarm(notificationDetails, alarmManager, calculateNextNotificationTrigger, broadcastPendingIntent);
        } else {
            alarmManager.setInexactRepeating(0, calculateNextNotificationTrigger, calculateRepeatIntervalMilliseconds, broadcastPendingIntent);
        }
        if (bool.booleanValue()) {
            saveScheduledNotification(context, notificationDetails);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void rescheduleNotifications(Context context) {
        Iterator<NotificationDetails> it = loadScheduledNotifications(context).iterator();
        while (it.hasNext()) {
            NotificationDetails next = it.next();
            try {
            } catch (f e8) {
                Log.e(TAG, e8.getMessage());
                removeNotificationFromCache(context, next.id);
            }
            if (next.repeatInterval == null && next.repeatIntervalMilliseconds == null) {
                if (next.timeZoneName != null) {
                    zonedScheduleNotification(context, next, Boolean.FALSE);
                } else {
                    scheduleNotification(context, next, Boolean.FALSE);
                }
            }
            repeatNotification(context, next, Boolean.FALSE);
        }
    }

    private static Uri retrieveSoundResourceUri(Context context, String str, SoundSource soundSource) {
        if (StringUtils.isNullOrEmpty(str).booleanValue()) {
            return RingtoneManager.getDefaultUri(2);
        }
        if (soundSource != null && soundSource != SoundSource.RawResource) {
            if (soundSource == SoundSource.Uri) {
                return Uri.parse(str);
            }
            return null;
        }
        return Uri.parse("android.resource://" + context.getPackageName() + "/raw/" + str);
    }

    private static void saveScheduledNotification(Context context, NotificationDetails notificationDetails) {
        ArrayList<NotificationDetails> loadScheduledNotifications = loadScheduledNotifications(context);
        ArrayList arrayList = new ArrayList();
        Iterator<NotificationDetails> it = loadScheduledNotifications.iterator();
        while (it.hasNext()) {
            NotificationDetails next = it.next();
            if (!next.id.equals(notificationDetails.id)) {
                arrayList.add(next);
            }
        }
        arrayList.add(notificationDetails);
        saveScheduledNotifications(context, arrayList);
    }

    private static void saveScheduledNotifications(Context context, ArrayList<NotificationDetails> arrayList) {
        context.getSharedPreferences(SCHEDULED_NOTIFICATIONS, 0).edit().putString(SCHEDULED_NOTIFICATIONS, buildGson().u(arrayList)).apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void scheduleNextNotification(Context context, NotificationDetails notificationDetails) {
        try {
            if (notificationDetails.scheduledNotificationRepeatFrequency != null) {
                zonedScheduleNextNotification(context, notificationDetails);
            } else if (notificationDetails.matchDateTimeComponents != null) {
                zonedScheduleNextNotificationMatchingDateComponents(context, notificationDetails);
            } else {
                if (notificationDetails.repeatInterval == null && notificationDetails.repeatIntervalMilliseconds == null) {
                    removeNotificationFromCache(context, notificationDetails.id);
                }
                scheduleNextRepeatingNotification(context, notificationDetails);
            }
        } catch (f e8) {
            Log.e(TAG, e8.getMessage());
            removeNotificationFromCache(context, notificationDetails.id);
        }
    }

    private static void scheduleNextRepeatingNotification(Context context, NotificationDetails notificationDetails) {
        long calculateNextNotificationTrigger = calculateNextNotificationTrigger(notificationDetails.calledAt.longValue(), calculateRepeatIntervalMilliseconds(notificationDetails));
        String u8 = buildGson().u(notificationDetails);
        Intent intent = new Intent(context, ScheduledNotificationReceiver.class);
        intent.putExtra(NOTIFICATION_DETAILS, u8);
        PendingIntent broadcastPendingIntent = getBroadcastPendingIntent(context, notificationDetails.id.intValue(), intent);
        AlarmManager alarmManager = getAlarmManager(context);
        if (notificationDetails.scheduleMode == null) {
            notificationDetails.scheduleMode = ScheduleMode.exactAllowWhileIdle;
        }
        setupAllowWhileIdleAlarm(notificationDetails, alarmManager, calculateNextNotificationTrigger, broadcastPendingIntent);
        saveScheduledNotification(context, notificationDetails);
    }

    private static void scheduleNotification(Context context, NotificationDetails notificationDetails, Boolean bool) {
        String u8 = buildGson().u(notificationDetails);
        Intent intent = new Intent(context, ScheduledNotificationReceiver.class);
        intent.putExtra(NOTIFICATION_DETAILS, u8);
        setupAlarm(notificationDetails, getAlarmManager(context), notificationDetails.millisecondsSinceEpoch.longValue(), getBroadcastPendingIntent(context, notificationDetails.id.intValue(), intent));
        if (bool.booleanValue()) {
            saveScheduledNotification(context, notificationDetails);
        }
    }

    private Boolean sendNotificationPayloadMessage(Intent intent) {
        if (SELECT_NOTIFICATION.equals(intent.getAction()) || SELECT_FOREGROUND_NOTIFICATION_ACTION.equals(intent.getAction())) {
            Map<String, Object> extractNotificationResponseMap = extractNotificationResponseMap(intent);
            if (SELECT_FOREGROUND_NOTIFICATION_ACTION.equals(intent.getAction())) {
                processForegroundNotificationAction(intent, extractNotificationResponseMap);
            }
            this.channel.c("didReceiveNotificationResponse", extractNotificationResponseMap);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private void setActivity(Activity activity) {
        this.mainActivity = activity;
    }

    private static void setBigPictureStyle(Context context, NotificationDetails notificationDetails, k.e eVar) {
        Bitmap bitmapFromSource;
        BigPictureStyleInformation bigPictureStyleInformation = (BigPictureStyleInformation) notificationDetails.styleInformation;
        k.b bVar = new k.b();
        if (bigPictureStyleInformation.contentTitle != null) {
            bVar.A(bigPictureStyleInformation.htmlFormatContentTitle.booleanValue() ? fromHtml(bigPictureStyleInformation.contentTitle) : bigPictureStyleInformation.contentTitle);
        }
        if (bigPictureStyleInformation.summaryText != null) {
            bVar.B(bigPictureStyleInformation.htmlFormatSummaryText.booleanValue() ? fromHtml(bigPictureStyleInformation.summaryText) : bigPictureStyleInformation.summaryText);
        }
        if (!bigPictureStyleInformation.hideExpandedLargeIcon.booleanValue()) {
            Object obj = bigPictureStyleInformation.largeIcon;
            if (obj != null) {
                bitmapFromSource = getBitmapFromSource(context, obj, bigPictureStyleInformation.largeIconBitmapSource);
            }
            bVar.z(getBitmapFromSource(context, bigPictureStyleInformation.bigPicture, bigPictureStyleInformation.bigPictureBitmapSource));
            eVar.P(bVar);
        }
        bitmapFromSource = null;
        bVar.y(bitmapFromSource);
        bVar.z(getBitmapFromSource(context, bigPictureStyleInformation.bigPicture, bigPictureStyleInformation.bigPictureBitmapSource));
        eVar.P(bVar);
    }

    private static void setBigTextStyle(NotificationDetails notificationDetails, k.e eVar) {
        BigTextStyleInformation bigTextStyleInformation = (BigTextStyleInformation) notificationDetails.styleInformation;
        k.c cVar = new k.c();
        if (bigTextStyleInformation.bigText != null) {
            cVar.x(bigTextStyleInformation.htmlFormatBigText.booleanValue() ? fromHtml(bigTextStyleInformation.bigText) : bigTextStyleInformation.bigText);
        }
        if (bigTextStyleInformation.contentTitle != null) {
            cVar.y(bigTextStyleInformation.htmlFormatContentTitle.booleanValue() ? fromHtml(bigTextStyleInformation.contentTitle) : bigTextStyleInformation.contentTitle);
        }
        if (bigTextStyleInformation.summaryText != null) {
            boolean booleanValue = bigTextStyleInformation.htmlFormatSummaryText.booleanValue();
            String str = bigTextStyleInformation.summaryText;
            Spanned spanned = str;
            if (booleanValue) {
                spanned = fromHtml(str);
            }
            cVar.z(spanned);
        }
        eVar.P(cVar);
    }

    private void setCanScheduleExactNotifications(j.d dVar) {
        dVar.success(Build.VERSION.SDK_INT < 31 ? Boolean.TRUE : Boolean.valueOf(getAlarmManager(this.applicationContext).canScheduleExactAlarms()));
    }

    private static void setCategory(NotificationDetails notificationDetails, k.e eVar) {
        String str = notificationDetails.category;
        if (str == null) {
            return;
        }
        eVar.n(str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [androidx.core.app.k$i, androidx.core.app.k$g] */
    /* JADX WARN: Type inference failed for: r2v2, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r2v4, types: [android.text.Spanned] */
    /* JADX WARN: Type inference failed for: r5v0, types: [androidx.core.app.k$e] */
    private static void setInboxStyle(NotificationDetails notificationDetails, k.e eVar) {
        InboxStyleInformation inboxStyleInformation = (InboxStyleInformation) notificationDetails.styleInformation;
        ?? gVar = new k.g();
        if (inboxStyleInformation.contentTitle != null) {
            gVar.y(inboxStyleInformation.htmlFormatContentTitle.booleanValue() ? fromHtml(inboxStyleInformation.contentTitle) : inboxStyleInformation.contentTitle);
        }
        if (inboxStyleInformation.summaryText != null) {
            gVar.z(inboxStyleInformation.htmlFormatSummaryText.booleanValue() ? fromHtml(inboxStyleInformation.summaryText) : inboxStyleInformation.summaryText);
        }
        ArrayList<String> arrayList = inboxStyleInformation.lines;
        if (arrayList != null) {
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (inboxStyleInformation.htmlFormatLines.booleanValue()) {
                    next = fromHtml(next);
                }
                gVar.x(next);
            }
        }
        eVar.P(gVar);
    }

    private static void setLights(NotificationDetails notificationDetails, k.e eVar) {
        if (!BooleanUtils.getValue(notificationDetails.enableLights) || notificationDetails.ledOnMs == null || notificationDetails.ledOffMs == null) {
            return;
        }
        eVar.D(notificationDetails.ledColor.intValue(), notificationDetails.ledOnMs.intValue(), notificationDetails.ledOffMs.intValue());
    }

    private static void setMediaStyle(k.e eVar) {
        eVar.P(new androidx.media.app.c());
    }

    private static void setMessagingStyle(Context context, NotificationDetails notificationDetails, k.e eVar) {
        MessagingStyleInformation messagingStyleInformation = (MessagingStyleInformation) notificationDetails.styleInformation;
        k.h hVar = new k.h(buildPerson(context, messagingStyleInformation.person));
        hVar.I(BooleanUtils.getValue(messagingStyleInformation.groupConversation));
        String str = messagingStyleInformation.conversationTitle;
        if (str != null) {
            hVar.H(str);
        }
        ArrayList<MessageDetails> arrayList = messagingStyleInformation.messages;
        if (arrayList != null && !arrayList.isEmpty()) {
            Iterator<MessageDetails> it = messagingStyleInformation.messages.iterator();
            while (it.hasNext()) {
                hVar.x(createMessage(context, it.next()));
            }
        }
        eVar.P(hVar);
    }

    private static void setProgress(NotificationDetails notificationDetails, k.e eVar) {
        if (BooleanUtils.getValue(notificationDetails.showProgress)) {
            eVar.J(notificationDetails.maxProgress.intValue(), notificationDetails.progress.intValue(), notificationDetails.indeterminate.booleanValue());
        }
    }

    private static void setSmallIcon(Context context, NotificationDetails notificationDetails, k.e eVar) {
        int intValue;
        if (StringUtils.isNullOrEmpty(notificationDetails.icon).booleanValue()) {
            String string = context.getSharedPreferences(SHARED_PREFERENCES_KEY, 0).getString(DEFAULT_ICON, null);
            intValue = StringUtils.isNullOrEmpty(string).booleanValue() ? notificationDetails.iconResourceId.intValue() : getDrawableResourceId(context, string);
        } else {
            intValue = getDrawableResourceId(context, notificationDetails.icon);
        }
        eVar.N(intValue);
    }

    private static void setSound(Context context, NotificationDetails notificationDetails, k.e eVar) {
        eVar.O(BooleanUtils.getValue(notificationDetails.playSound) ? retrieveSoundResourceUri(context, notificationDetails.sound, notificationDetails.soundSource) : null);
    }

    private static void setStyle(Context context, NotificationDetails notificationDetails, k.e eVar) {
        int i8 = e.f8850c[notificationDetails.style.ordinal()];
        if (i8 == 1) {
            setBigPictureStyle(context, notificationDetails, eVar);
        } else if (i8 == 2) {
            setBigTextStyle(notificationDetails, eVar);
        } else if (i8 == 3) {
            setInboxStyle(notificationDetails, eVar);
        } else if (i8 == 4) {
            setMessagingStyle(context, notificationDetails, eVar);
        } else if (i8 != 5) {
        } else {
            setMediaStyle(eVar);
        }
    }

    private static void setTimeoutAfter(NotificationDetails notificationDetails, k.e eVar) {
        Long l8 = notificationDetails.timeoutAfter;
        if (l8 == null) {
            return;
        }
        eVar.S(l8.longValue());
    }

    private static void setVibrationPattern(NotificationDetails notificationDetails, k.e eVar) {
        if (!BooleanUtils.getValue(notificationDetails.enableVibration)) {
            eVar.U(new long[]{0});
            return;
        }
        long[] jArr = notificationDetails.vibrationPattern;
        if (jArr == null || jArr.length <= 0) {
            return;
        }
        eVar.U(jArr);
    }

    private static void setVisibility(NotificationDetails notificationDetails, k.e eVar) {
        Integer num = notificationDetails.visibility;
        if (num == null) {
            return;
        }
        int intValue = num.intValue();
        int i8 = 1;
        if (intValue == 0) {
            i8 = 0;
        } else if (intValue != 1) {
            if (intValue != 2) {
                throw new IllegalArgumentException("Unknown index: " + notificationDetails.visibility);
            }
            i8 = -1;
        }
        eVar.V(i8);
    }

    private static void setupAlarm(NotificationDetails notificationDetails, AlarmManager alarmManager, long j8, PendingIntent pendingIntent) {
        if (notificationDetails.scheduleMode == null) {
            notificationDetails.scheduleMode = ScheduleMode.exact;
        }
        if (notificationDetails.scheduleMode.useAllowWhileIdle()) {
            setupAllowWhileIdleAlarm(notificationDetails, alarmManager, j8, pendingIntent);
        } else if (notificationDetails.scheduleMode.useExactAlarm()) {
            checkCanScheduleExactAlarms(alarmManager);
            androidx.core.app.e.c(alarmManager, 0, j8, pendingIntent);
        } else if (!notificationDetails.scheduleMode.useAlarmClock()) {
            alarmManager.set(0, j8, pendingIntent);
        } else {
            checkCanScheduleExactAlarms(alarmManager);
            androidx.core.app.e.a(alarmManager, j8, pendingIntent, pendingIntent);
        }
    }

    private static void setupAllowWhileIdleAlarm(NotificationDetails notificationDetails, AlarmManager alarmManager, long j8, PendingIntent pendingIntent) {
        if (notificationDetails.scheduleMode.useExactAlarm()) {
            checkCanScheduleExactAlarms(alarmManager);
            androidx.core.app.e.d(alarmManager, 0, j8, pendingIntent);
        } else if (!notificationDetails.scheduleMode.useAlarmClock()) {
            androidx.core.app.e.b(alarmManager, 0, j8, pendingIntent);
        } else {
            checkCanScheduleExactAlarms(alarmManager);
            androidx.core.app.e.a(alarmManager, j8, pendingIntent, pendingIntent);
        }
    }

    private static void setupNotificationChannel(Context context, NotificationChannelDetails notificationChannelDetails) {
        Integer num;
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelDetails.id, notificationChannelDetails.name, notificationChannelDetails.importance.intValue());
            notificationChannel.setDescription(notificationChannelDetails.description);
            notificationChannel.setGroup(notificationChannelDetails.groupId);
            if (notificationChannelDetails.playSound.booleanValue()) {
                Integer num2 = notificationChannelDetails.audioAttributesUsage;
                notificationChannel.setSound(retrieveSoundResourceUri(context, notificationChannelDetails.sound, notificationChannelDetails.soundSource), new AudioAttributes.Builder().setUsage(Integer.valueOf(num2 != null ? num2.intValue() : 5).intValue()).build());
            } else {
                notificationChannel.setSound(null, null);
            }
            notificationChannel.enableVibration(BooleanUtils.getValue(notificationChannelDetails.enableVibration));
            long[] jArr = notificationChannelDetails.vibrationPattern;
            if (jArr != null && jArr.length > 0) {
                notificationChannel.setVibrationPattern(jArr);
            }
            boolean value = BooleanUtils.getValue(notificationChannelDetails.enableLights);
            notificationChannel.enableLights(value);
            if (value && (num = notificationChannelDetails.ledColor) != null) {
                notificationChannel.setLightColor(num.intValue());
            }
            notificationChannel.setShowBadge(BooleanUtils.getValue(notificationChannelDetails.showBadge));
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void show(i iVar, j.d dVar) {
        NotificationDetails extractNotificationDetails = extractNotificationDetails(dVar, (Map) iVar.b());
        if (extractNotificationDetails != null) {
            showNotification(this.applicationContext, extractNotificationDetails);
            dVar.success((Object) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void showNotification(Context context, NotificationDetails notificationDetails) {
        Notification createNotification = createNotification(context, notificationDetails);
        n notificationManager = getNotificationManager(context);
        String str = notificationDetails.tag;
        int intValue = notificationDetails.id.intValue();
        if (str != null) {
            notificationManager.i(str, intValue, createNotification);
        } else {
            notificationManager.h(intValue, createNotification);
        }
    }

    private void startForegroundService(i iVar, j.d dVar) {
        String str;
        Map<String, Object> map = (Map) iVar.a("notificationData");
        Integer num = (Integer) iVar.a("startType");
        ArrayList arrayList = (ArrayList) iVar.a("foregroundServiceTypes");
        if (arrayList != null && arrayList.size() == 0) {
            str = "If foregroundServiceTypes is non-null it must not be empty!";
        } else if (map == null || num == null) {
            str = "An argument passed to startForegroundService was null!";
        } else {
            NotificationDetails extractNotificationDetails = extractNotificationDetails(dVar, map);
            if (extractNotificationDetails == null) {
                return;
            }
            if (extractNotificationDetails.id.intValue() != 0) {
                ForegroundServiceStartParameter foregroundServiceStartParameter = new ForegroundServiceStartParameter(extractNotificationDetails, num.intValue(), arrayList);
                Intent intent = new Intent(this.applicationContext, ForegroundService.class);
                intent.putExtra("com.dexterous.flutterlocalnotifications.ForegroundServiceStartParameter", foregroundServiceStartParameter);
                androidx.core.content.a.m(this.applicationContext, intent);
                dVar.success((Object) null);
                return;
            }
            str = "The id of the notification for a foreground service must not be 0!";
        }
        dVar.a("ARGUMENT_ERROR", str, (Object) null);
    }

    private void stopForegroundService(j.d dVar) {
        this.applicationContext.stopService(new Intent(this.applicationContext, ForegroundService.class));
        dVar.success((Object) null);
    }

    private Integer tryParseInt(String str) {
        try {
            return Integer.valueOf(Integer.parseInt(str));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private void zonedSchedule(i iVar, j.d dVar) {
        NotificationDetails extractNotificationDetails = extractNotificationDetails(dVar, (Map) iVar.b());
        if (extractNotificationDetails != null) {
            if (extractNotificationDetails.matchDateTimeComponents != null) {
                extractNotificationDetails.scheduledDateTime = getNextFireDateMatchingDateTimeComponents(extractNotificationDetails);
            }
            try {
                zonedScheduleNotification(this.applicationContext, extractNotificationDetails, Boolean.TRUE);
                dVar.success((Object) null);
            } catch (h e8) {
                dVar.a(e8.f8856a, e8.getMessage(), (Object) null);
            }
        }
    }

    private static void zonedScheduleNextNotification(Context context, NotificationDetails notificationDetails) {
        String nextFireDate = getNextFireDate(notificationDetails);
        if (nextFireDate == null) {
            return;
        }
        notificationDetails.scheduledDateTime = nextFireDate;
        zonedScheduleNotification(context, notificationDetails, Boolean.TRUE);
    }

    private static void zonedScheduleNextNotificationMatchingDateComponents(Context context, NotificationDetails notificationDetails) {
        String nextFireDateMatchingDateTimeComponents = getNextFireDateMatchingDateTimeComponents(notificationDetails);
        if (nextFireDateMatchingDateTimeComponents == null) {
            return;
        }
        notificationDetails.scheduledDateTime = nextFireDateMatchingDateTimeComponents;
        zonedScheduleNotification(context, notificationDetails, Boolean.TRUE);
    }

    private static void zonedScheduleNotification(Context context, NotificationDetails notificationDetails, Boolean bool) {
        String u8 = buildGson().u(notificationDetails);
        Intent intent = new Intent(context, ScheduledNotificationReceiver.class);
        intent.putExtra(NOTIFICATION_DETAILS, u8);
        setupAlarm(notificationDetails, getAlarmManager(context), ZonedDateTime.of(LocalDateTime.parse(notificationDetails.scheduledDateTime), ZoneId.of(notificationDetails.timeZoneName)).toInstant().toEpochMilli(), getBroadcastPendingIntent(context, notificationDetails.id.intValue(), intent));
        if (bool.booleanValue()) {
            saveScheduledNotification(context, notificationDetails);
        }
    }

    public boolean onActivityResult(int i8, int i9, Intent intent) {
        if (i8 == 1 || i8 == 2 || i8 == 3) {
            if (this.permissionRequestProgress == g.RequestingExactAlarmsPermission && i8 == 2 && Build.VERSION.SDK_INT >= 31) {
                this.callback.b(getAlarmManager(this.applicationContext).canScheduleExactAlarms());
                this.permissionRequestProgress = g.None;
            }
            if (this.permissionRequestProgress == g.RequestingFullScreenIntentPermission && i8 == 3 && Build.VERSION.SDK_INT >= 34) {
                this.callback.b(((NotificationManager) this.applicationContext.getSystemService("notification")).canUseFullScreenIntent());
                this.permissionRequestProgress = g.None;
            }
            return true;
        }
        return false;
    }

    public void onAttachedToActivity(zf.c cVar) {
        cVar.c(this);
        cVar.b(this);
        cVar.a(this);
        Activity activity = cVar.getActivity();
        this.mainActivity = activity;
        Intent intent = activity.getIntent();
        if (launchedActivityFromHistory(intent) || !SELECT_FOREGROUND_NOTIFICATION_ACTION.equals(intent.getAction())) {
            return;
        }
        processForegroundNotificationAction(intent, extractNotificationResponseMap(intent));
    }

    public void onAttachedToEngine(a.b bVar) {
        this.applicationContext = bVar.a();
        j jVar = new j(bVar.b(), METHOD_CHANNEL);
        this.channel = jVar;
        jVar.e(this);
    }

    public void onDetachedFromActivity() {
        this.mainActivity = null;
    }

    public void onDetachedFromActivityForConfigChanges() {
        this.mainActivity = null;
    }

    public void onDetachedFromEngine(a.b bVar) {
        this.channel.e((j.c) null);
        this.channel = null;
        this.applicationContext = null;
    }

    public void onMethodCall(i iVar, j.d dVar) {
        String str = iVar.a;
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -2096263152:
                if (str.equals(STOP_FOREGROUND_SERVICE)) {
                    c9 = 0;
                    break;
                }
                break;
            case -2041662895:
                if (str.equals(GET_NOTIFICATION_CHANNELS_METHOD)) {
                    c9 = 1;
                    break;
                }
                break;
            case -1873731438:
                if (str.equals(DELETE_NOTIFICATION_CHANNEL_GROUP_METHOD)) {
                    c9 = 2;
                    break;
                }
                break;
            case -1785484984:
                if (str.equals(REQUEST_NOTIFICATIONS_PERMISSION_METHOD)) {
                    c9 = 3;
                    break;
                }
                break;
            case -1367724422:
                if (str.equals(CANCEL_METHOD)) {
                    c9 = 4;
                    break;
                }
                break;
            case -1108601471:
                if (str.equals(REQUEST_EXACT_ALARMS_PERMISSION_METHOD)) {
                    c9 = 5;
                    break;
                }
                break;
            case -950516363:
                if (str.equals(REQUEST_FULL_SCREEN_INTENT_PERMISSION_METHOD)) {
                    c9 = 6;
                    break;
                }
                break;
            case -799130106:
                if (str.equals(PENDING_NOTIFICATION_REQUESTS_METHOD)) {
                    c9 = 7;
                    break;
                }
                break;
            case -208611345:
                if (str.equals(GET_NOTIFICATION_APP_LAUNCH_DETAILS_METHOD)) {
                    c9 = '\b';
                    break;
                }
                break;
            case 3529469:
                if (str.equals(SHOW_METHOD)) {
                    c9 = '\t';
                    break;
                }
                break;
            case 6625712:
                if (str.equals(PERIODICALLY_SHOW_METHOD)) {
                    c9 = '\n';
                    break;
                }
                break;
            case 116003316:
                if (str.equals(GET_ACTIVE_NOTIFICATION_MESSAGING_STYLE_METHOD)) {
                    c9 = 11;
                    break;
                }
                break;
            case 476547271:
                if (str.equals(CANCEL_ALL_METHOD)) {
                    c9 = '\f';
                    break;
                }
                break;
            case 548573423:
                if (str.equals(ZONED_SCHEDULE_METHOD)) {
                    c9 = '\r';
                    break;
                }
                break;
            case 767006947:
                if (str.equals(CREATE_NOTIFICATION_CHANNEL_GROUP_METHOD)) {
                    c9 = 14;
                    break;
                }
                break;
            case 825311171:
                if (str.equals(GET_CALLBACK_HANDLE_METHOD)) {
                    c9 = 15;
                    break;
                }
                break;
            case 871091088:
                if (str.equals(INITIALIZE_METHOD)) {
                    c9 = 16;
                    break;
                }
                break;
            case 891942317:
                if (str.equals(ARE_NOTIFICATIONS_ENABLED_METHOD)) {
                    c9 = 17;
                    break;
                }
                break;
            case 972029712:
                if (str.equals(CAN_SCHEDULE_EXACT_NOTIFICATIONS_METHOD)) {
                    c9 = 18;
                    break;
                }
                break;
            case 1008472557:
                if (str.equals(DELETE_NOTIFICATION_CHANNEL_METHOD)) {
                    c9 = 19;
                    break;
                }
                break;
            case 1207771056:
                if (str.equals(START_FOREGROUND_SERVICE)) {
                    c9 = 20;
                    break;
                }
                break;
            case 1594833996:
                if (str.equals(GET_ACTIVE_NOTIFICATIONS_METHOD)) {
                    c9 = 21;
                    break;
                }
                break;
            case 1653467900:
                if (str.equals(CREATE_NOTIFICATION_CHANNEL_METHOD)) {
                    c9 = 22;
                    break;
                }
                break;
            case 2147197514:
                if (str.equals(PERIODICALLY_SHOW_WITH_DURATION)) {
                    c9 = 23;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                stopForegroundService(dVar);
                return;
            case 1:
                getNotificationChannels(dVar);
                return;
            case 2:
                deleteNotificationChannelGroup(iVar, dVar);
                return;
            case 3:
                requestNotificationsPermission(new b(dVar));
                return;
            case 4:
                cancel(iVar, dVar);
                return;
            case 5:
                requestExactAlarmsPermission(new c(dVar));
                return;
            case 6:
                requestFullScreenIntentPermission(new d(dVar));
                return;
            case 7:
                pendingNotificationRequests(dVar);
                return;
            case '\b':
                getNotificationAppLaunchDetails(dVar);
                return;
            case '\t':
                show(iVar, dVar);
                return;
            case '\n':
            case 23:
                repeat(iVar, dVar);
                return;
            case 11:
                getActiveNotificationMessagingStyle(iVar, dVar);
                return;
            case '\f':
                cancelAllNotifications(dVar);
                return;
            case '\r':
                zonedSchedule(iVar, dVar);
                return;
            case 14:
                createNotificationChannelGroup(iVar, dVar);
                return;
            case 15:
                getCallbackHandle(dVar);
                return;
            case 16:
                initialize(iVar, dVar);
                return;
            case 17:
                areNotificationsEnabled(dVar);
                return;
            case 18:
                setCanScheduleExactNotifications(dVar);
                return;
            case 19:
                deleteNotificationChannel(iVar, dVar);
                return;
            case 20:
                startForegroundService(iVar, dVar);
                return;
            case 21:
                getActiveNotifications(dVar);
                return;
            case 22:
                createNotificationChannel(iVar, dVar);
                return;
            default:
                dVar.b();
                return;
        }
    }

    public boolean onNewIntent(Intent intent) {
        Activity activity;
        boolean booleanValue = sendNotificationPayloadMessage(intent).booleanValue();
        if (booleanValue && (activity = this.mainActivity) != null) {
            activity.setIntent(intent);
        }
        return booleanValue;
    }

    public void onReattachedToActivityForConfigChanges(zf.c cVar) {
        cVar.c(this);
        cVar.b(this);
        cVar.a(this);
        this.mainActivity = cVar.getActivity();
    }

    public boolean onRequestPermissionsResult(int i8, String[] strArr, int[] iArr) {
        boolean z4 = false;
        if (this.permissionRequestProgress == g.RequestingNotificationPermission && i8 == 1) {
            if (iArr.length > 0 && iArr[0] == 0) {
                z4 = true;
            }
            this.callback.b(z4);
            this.permissionRequestProgress = g.None;
        }
        return z4;
    }

    public void requestExactAlarmsPermission(com.dexterous.flutterlocalnotifications.a aVar) {
        g gVar = this.permissionRequestProgress;
        g gVar2 = g.None;
        if (gVar != gVar2) {
            aVar.a(PERMISSION_REQUEST_IN_PROGRESS_ERROR_MESSAGE);
            return;
        }
        this.callback = aVar;
        if (Build.VERSION.SDK_INT < 31) {
            aVar.b(true);
        } else if (getAlarmManager(this.applicationContext).canScheduleExactAlarms()) {
            this.callback.b(true);
            this.permissionRequestProgress = gVar2;
        } else {
            this.permissionRequestProgress = g.RequestingExactAlarmsPermission;
            Activity activity = this.mainActivity;
            activity.startActivityForResult(new Intent("android.settings.REQUEST_SCHEDULE_EXACT_ALARM", Uri.parse("package:" + this.applicationContext.getPackageName())), 2);
        }
    }

    public void requestFullScreenIntentPermission(com.dexterous.flutterlocalnotifications.a aVar) {
        g gVar = this.permissionRequestProgress;
        g gVar2 = g.None;
        if (gVar != gVar2) {
            aVar.a(PERMISSION_REQUEST_IN_PROGRESS_ERROR_MESSAGE);
            return;
        }
        this.callback = aVar;
        if (Build.VERSION.SDK_INT < 34) {
            aVar.b(true);
            return;
        }
        getAlarmManager(this.applicationContext);
        if (((NotificationManager) this.applicationContext.getSystemService("notification")).canUseFullScreenIntent()) {
            this.callback.b(true);
            this.permissionRequestProgress = gVar2;
            return;
        }
        this.permissionRequestProgress = g.RequestingFullScreenIntentPermission;
        Activity activity = this.mainActivity;
        activity.startActivityForResult(new Intent("android.settings.MANAGE_APP_USE_FULL_SCREEN_INTENT", Uri.parse("package:" + this.applicationContext.getPackageName())), 3);
    }

    public void requestNotificationsPermission(com.dexterous.flutterlocalnotifications.a aVar) {
        g gVar = this.permissionRequestProgress;
        g gVar2 = g.None;
        if (gVar != gVar2) {
            aVar.a(PERMISSION_REQUEST_IN_PROGRESS_ERROR_MESSAGE);
            return;
        }
        this.callback = aVar;
        if (Build.VERSION.SDK_INT < 33) {
            this.callback.b(n.e(this.mainActivity).a());
            return;
        }
        if (androidx.core.content.a.a(this.mainActivity, "android.permission.POST_NOTIFICATIONS") == 0) {
            this.callback.b(true);
            this.permissionRequestProgress = gVar2;
            return;
        }
        this.permissionRequestProgress = g.RequestingNotificationPermission;
        androidx.core.app.b.t(this.mainActivity, new String[]{"android.permission.POST_NOTIFICATIONS"}, 1);
    }
}
