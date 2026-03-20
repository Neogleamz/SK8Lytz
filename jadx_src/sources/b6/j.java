package b6;

import android.net.Uri;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j {
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static int a(String str) {
        char c9;
        if (str == null) {
            return -1;
        }
        String t8 = t.t(str);
        t8.hashCode();
        switch (t8.hashCode()) {
            case -2123537834:
                if (t8.equals("audio/eac3-joc")) {
                    c9 = 0;
                    break;
                }
                c9 = 65535;
                break;
            case -1662384011:
                if (t8.equals("video/mp2p")) {
                    c9 = 1;
                    break;
                }
                c9 = 65535;
                break;
            case -1662384007:
                if (t8.equals("video/mp2t")) {
                    c9 = 2;
                    break;
                }
                c9 = 65535;
                break;
            case -1662095187:
                if (t8.equals("video/webm")) {
                    c9 = 3;
                    break;
                }
                c9 = 65535;
                break;
            case -1606874997:
                if (t8.equals("audio/amr-wb")) {
                    c9 = 4;
                    break;
                }
                c9 = 65535;
                break;
            case -1487394660:
                if (t8.equals("image/jpeg")) {
                    c9 = 5;
                    break;
                }
                c9 = 65535;
                break;
            case -1248337486:
                if (t8.equals("application/mp4")) {
                    c9 = 6;
                    break;
                }
                c9 = 65535;
                break;
            case -1079884372:
                if (t8.equals("video/x-msvideo")) {
                    c9 = 7;
                    break;
                }
                c9 = 65535;
                break;
            case -1004728940:
                if (t8.equals("text/vtt")) {
                    c9 = '\b';
                    break;
                }
                c9 = 65535;
                break;
            case -387023398:
                if (t8.equals("audio/x-matroska")) {
                    c9 = '\t';
                    break;
                }
                c9 = 65535;
                break;
            case -43467528:
                if (t8.equals("application/webm")) {
                    c9 = '\n';
                    break;
                }
                c9 = 65535;
                break;
            case 13915911:
                if (t8.equals("video/x-flv")) {
                    c9 = 11;
                    break;
                }
                c9 = 65535;
                break;
            case 187078296:
                if (t8.equals("audio/ac3")) {
                    c9 = '\f';
                    break;
                }
                c9 = 65535;
                break;
            case 187078297:
                if (t8.equals("audio/ac4")) {
                    c9 = '\r';
                    break;
                }
                c9 = 65535;
                break;
            case 187078669:
                if (t8.equals("audio/amr")) {
                    c9 = 14;
                    break;
                }
                c9 = 65535;
                break;
            case 187090232:
                if (t8.equals("audio/mp4")) {
                    c9 = 15;
                    break;
                }
                c9 = 65535;
                break;
            case 187091926:
                if (t8.equals("audio/ogg")) {
                    c9 = 16;
                    break;
                }
                c9 = 65535;
                break;
            case 187099443:
                if (t8.equals("audio/wav")) {
                    c9 = 17;
                    break;
                }
                c9 = 65535;
                break;
            case 1331848029:
                if (t8.equals("video/mp4")) {
                    c9 = 18;
                    break;
                }
                c9 = 65535;
                break;
            case 1503095341:
                if (t8.equals("audio/3gpp")) {
                    c9 = 19;
                    break;
                }
                c9 = 65535;
                break;
            case 1504578661:
                if (t8.equals("audio/eac3")) {
                    c9 = 20;
                    break;
                }
                c9 = 65535;
                break;
            case 1504619009:
                if (t8.equals("audio/flac")) {
                    c9 = 21;
                    break;
                }
                c9 = 65535;
                break;
            case 1504824762:
                if (t8.equals("audio/midi")) {
                    c9 = 22;
                    break;
                }
                c9 = 65535;
                break;
            case 1504831518:
                if (t8.equals("audio/mpeg")) {
                    c9 = 23;
                    break;
                }
                c9 = 65535;
                break;
            case 1505118770:
                if (t8.equals("audio/webm")) {
                    c9 = 24;
                    break;
                }
                c9 = 65535;
                break;
            case 2039520277:
                if (t8.equals("video/x-matroska")) {
                    c9 = 25;
                    break;
                }
                c9 = 65535;
                break;
            default:
                c9 = 65535;
                break;
        }
        switch (c9) {
            case 0:
            case '\f':
            case 20:
                return 0;
            case 1:
                return 10;
            case 2:
                return 11;
            case 3:
            case '\t':
            case '\n':
            case 24:
            case 25:
                return 6;
            case 4:
            case 14:
            case 19:
                return 3;
            case 5:
                return 14;
            case 6:
            case 15:
            case 18:
                return 8;
            case 7:
                return 16;
            case '\b':
                return 13;
            case 11:
                return 5;
            case '\r':
                return 1;
            case 16:
                return 9;
            case 17:
                return 12;
            case 21:
                return 4;
            case 22:
                return 15;
            case 23:
                return 7;
            default:
                return -1;
        }
    }

    public static int b(Map<String, List<String>> map) {
        List<String> list = map.get("Content-Type");
        return a((list == null || list.isEmpty()) ? null : list.get(0));
    }

    public static int c(Uri uri) {
        String lastPathSegment = uri.getLastPathSegment();
        if (lastPathSegment == null) {
            return -1;
        }
        if (lastPathSegment.endsWith(".ac3") || lastPathSegment.endsWith(".ec3")) {
            return 0;
        }
        if (lastPathSegment.endsWith(".ac4")) {
            return 1;
        }
        if (lastPathSegment.endsWith(".adts") || lastPathSegment.endsWith(".aac")) {
            return 2;
        }
        if (lastPathSegment.endsWith(".amr")) {
            return 3;
        }
        if (lastPathSegment.endsWith(".flac")) {
            return 4;
        }
        if (lastPathSegment.endsWith(".flv")) {
            return 5;
        }
        if (lastPathSegment.endsWith(".mid") || lastPathSegment.endsWith(".midi") || lastPathSegment.endsWith(".smf")) {
            return 15;
        }
        if (lastPathSegment.startsWith(".mk", lastPathSegment.length() - 4) || lastPathSegment.endsWith(".webm")) {
            return 6;
        }
        if (lastPathSegment.endsWith(".mp3")) {
            return 7;
        }
        if (lastPathSegment.endsWith(".mp4") || lastPathSegment.startsWith(".m4", lastPathSegment.length() - 4) || lastPathSegment.startsWith(".mp4", lastPathSegment.length() - 5) || lastPathSegment.startsWith(".cmf", lastPathSegment.length() - 5)) {
            return 8;
        }
        if (lastPathSegment.startsWith(".og", lastPathSegment.length() - 4) || lastPathSegment.endsWith(".opus")) {
            return 9;
        }
        if (lastPathSegment.endsWith(".ps") || lastPathSegment.endsWith(".mpeg") || lastPathSegment.endsWith(".mpg") || lastPathSegment.endsWith(".m2p")) {
            return 10;
        }
        if (lastPathSegment.endsWith(".ts") || lastPathSegment.startsWith(".ts", lastPathSegment.length() - 4)) {
            return 11;
        }
        if (lastPathSegment.endsWith(".wav") || lastPathSegment.endsWith(".wave")) {
            return 12;
        }
        if (lastPathSegment.endsWith(".vtt") || lastPathSegment.endsWith(".webvtt")) {
            return 13;
        }
        if (lastPathSegment.endsWith(".jpg") || lastPathSegment.endsWith(".jpeg")) {
            return 14;
        }
        return lastPathSegment.endsWith(".avi") ? 16 : -1;
    }
}
