package com.youku.player.util;import android.annotation.SuppressLint;import android.annotation.TargetApi;import android.content.Context;import android.content.res.Configuration;import android.net.ConnectivityManager;import android.net.NetworkInfo;import android.net.wifi.WifiManager;import android.os.Build;import android.os.Handler;import android.os.Message;import android.text.TextUtils;import android.text.format.Formatter;import android.view.KeyCharacterMap;import android.view.KeyEvent;import android.view.View;import android.view.ViewConfiguration;import com.baseproject.utils.Logger;import com.baseproject.utils.UIUtils;import com.youku.player.LogTag;import com.youku.player.config.MediaPlayerConfiguration;import com.youku.player.goplay.Profile;import com.youku.player.goplay.StaticsUtil;import com.youku.player.module.VideoUrlInfo;import com.youku.player.module.VideoUrlInfo.Source;import com.youku.player.plugin.MediaPlayerDelegate;import com.youku.player.plugin.PluginOverlay;import com.youku.uplayer.MediaPlayerProxy;import org.json.JSONException;import org.json.JSONObject;import java.io.BufferedReader;import java.io.ByteArrayOutputStream;import java.io.FileInputStream;import java.io.IOException;import java.io.InputStream;import java.io.InputStreamReader;import java.io.UnsupportedEncodingException;import java.lang.reflect.InvocationTargetException;import java.lang.reflect.Method;import java.net.HttpURLConnection;import java.net.Inet4Address;import java.net.InetAddress;import java.net.MalformedURLException;import java.net.NetworkInterface;import java.net.SocketException;import java.net.URL;import java.security.InvalidKeyException;import java.security.NoSuchAlgorithmException;import java.security.SecureRandom;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Date;import java.util.Enumeration;import java.util.List;import java.util.Locale;import java.util.Random;import javax.crypto.BadPaddingException;import javax.crypto.Cipher;import javax.crypto.IllegalBlockSizeException;import javax.crypto.KeyGenerator;import javax.crypto.NoSuchPaddingException;import javax.crypto.spec.SecretKeySpec;/** * @author 刘仲男 81595157@qq.com * @author LongFan * @version 1.0 * @Description: 工具类 * @created time 2012-9-13下午3:31:47 * @modification 2013-4-23 14:52:02 * @Description 简化了工具类 其余由baseProject提供 */@SuppressLint("NewApi")public final class PlayerUtil {    // 汉字    public static final int CHINESE = 0;    // 数字或字母    public static final int NUMBER_OR_CHARACTER = 1;    // 数字和字母    public static final int NUMBER_CHARACTER = 2;    // 数字、字母、汉字    public static final int MIX = 3;    private static final int GET_FINAL_URL_CONNECT_TIMEOUT = 3000;    private static final int GET_FINAL_URL_READ_TIMEOUT = 3000;    private PlayerUtil() {    }    public final static String LINE_SEPARATOR = System            .getProperty("line.separator");    public static boolean useUplayer(VideoUrlInfo info) {        if (info != null && info.isDRMVideo())            return false;        else            return MediaPlayerProxy.isUplayerSupported()                    && !Profile.USE_SYSTEM_PLAYER;    }    public static void showTips(String tipsString) {        showTips(tipsString, -1L);    }    /**     * threshold the downloadtask create time     */    public static void showTips(String tipsString, long threshold) {        // Message msg = Message.obtain();        // msg.what = 1;        // Bundle bundle = new Bundle();        // bundle.putString("tipsString", tipsString);        // bundle.putLong("threshold", threshold);        // msg.setData(bundle);        // msgHandler.sendMessage(msg);    }    // private static Handler msgHandler = new Handler() {    //    // @Override    // public void handleMessage(Message msg) {    // switch (msg.what) {    // case 0:    // break;    // case 1:    // handleShowTipsEvents(msg);    // break;    // }    // super.handleMessage(msg);    // }    //    // private long previousToastShow;    // private String previousToastString = "";    //    // private void handleShowTipsEvents(Message msg) {    // long thisTime = System.currentTimeMillis();    // String thisTimeMsg = msg.getData().getString("tipsString");    // long threshold = msg.getData().getLong("threshold", -1L);    // String temp = previousToastString;    // previousToastString = thisTimeMsg;    // long tempTime = previousToastShow;    // previousToastShow = thisTime;    // if (thisTimeMsg == null || thisTime - tempTime <= 3500    // && thisTimeMsg.equalsIgnoreCase(temp)) {    // previousToastString = temp;    // previousToastShow = tempTime;    // return;    // }    // // View v = mInflater.inflate(R.layout.youku_tips, null);    // // TextView text = (TextView) v.findViewById(R.id.tips_text);    // // text.setText(thisTimeMsg);    // // v.setMinimumHeight(Util.dip2px(40));    // // t.setView(v);    // // t.show();    // // wrapper.setMinimumWidth(DeviceInfo.WIDTH < DeviceInfo.HEIGHT ?    // // DeviceInfo.WIDTH : DeviceInfo.HEIGHT);    // previousToastShow = thisTime;    // }    // };    /**     * 转换文件大小     *     * @param size 单位为kb     * @return     */    public static String formatSize(float size) {        long kb = 1024;        long mb = (kb * 1024);        long gb = (mb * 1024);        if (size < kb) {            return String.format("%d B", (int) size);        } else if (size < mb) {            return String.format("%.1f KB", size / kb); // 保留两位小数        } else if (size < gb) {            return String.format("%.1f MB", size / mb);        } else {            return String.format("%.1f GB", size / gb);        }    }    /**     * @author meky 判断字符串是否为空     */    public static boolean isNull(String str) {        if (str == null || str.length() == 0)            return true;        return false;    }	/*     * (non-Javadoc)	 * 	 * @see android.app.Application#onCreate()	 */    public static boolean isFinalUrl(String url) {        url = url.toLowerCase().trim();        if (url.endsWith(".3gp") || url.endsWith(".mp4")                || url.endsWith(".3gphd") || url.endsWith(".flv")                || url.endsWith(".3gp") || url.endsWith(".m3u8"))            return true;        return false;    }    /**     * 解析302跳转获取最终地址     */    public static String getFinnalUrl(String url, String exceptionString) {        try {            if (PlayerUtil.isFinalUrl(url)) {                return url;            }            HttpURLConnection con = (HttpURLConnection) (new URL(url)                    .openConnection());            con.setInstanceFollowRedirects(false);            con.connect();            String newUrl = null;            if (con.getResponseCode() == 302) {                newUrl = con.getHeaderField("Location");                con.disconnect();                if (PlayerUtil.isFinalUrl(newUrl)) {                    return newUrl;                } else {                    return getFinnalUrl(newUrl, exceptionString);                }            } else {                return null;            }        } catch (MalformedURLException e) {            exceptionString += e.toString();            Logger.e(PlayerUtil.TAG_GLOBAL, "Task_getVideoUrl.getLastUrl(),"                    + exceptionString, e);            return null;        } catch (IOException e) {            exceptionString += e.toString();            Logger.e(PlayerUtil.TAG_GLOBAL, "Task_getVideoUrl.getLastUrl(),"                    + exceptionString, e);            return null;        } catch (Exception e) {            return null;        }    }    private static Random random = new Random(System.currentTimeMillis());    public static int rand(int maxvalue) {        return (random.nextInt() << 1 >>> 1) % maxvalue;    }    public static void sendMessage(Handler mhandler, int msg) {        try {            Message message = Message.obtain();            message.what = msg;            mhandler.sendMessage(message);        } catch (Exception e) {            Logger.e(PlayerUtil.TAG_GLOBAL, "F.sendMessage()", e);        }    }    public static String getJsonValue(JSONObject object, String name) {        if (object != null)            return object.optString(name);        return "";    }    public static int getJsonInit(JSONObject object, String name,                                  int defaultValue) {        try {            return object.isNull(name) ? defaultValue : object.getInt(name);        } catch (JSONException e) {            Logger.d(PlayerUtil.TAG_GLOBAL, "F.getJsonInit()", e);            return defaultValue;        }    }    static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8',            '9', 'a', 'b', 'c', 'd', 'e', 'f'};    public static int flags;    public static String TAG_GLOBAL = "UPlayer";    /**     * 把byte[]数组转换成十六进制字符串表示形式     *     * @param tmp 要转换的byte[]     * @return 十六进制字符串表示形式     */    public static String byteToHexString(byte[] tmp) {        String s;        // 用字节表示就是 16 个字节        char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，        // 所以表示成 16 进制需要 32 个字符        int k = 0; // 表示转换结果中对应的字符位置        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节            // 转换成 16 进制字符的转换            byte byte0 = tmp[i]; // 取第 i 个字节            str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,            // >>> 为逻辑右移，将符号位一起右移            str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换        }        s = new String(str); // 换后的结果转换为字符串        return s;    }    public static final int EXCEPTION = -1;    public static void out(int id, String str) {        switch (id) {            case EXCEPTION:                Logger.e(PlayerUtil.TAG_GLOBAL, "F.out()," + str);                break;            case 0:                Logger.d(PlayerUtil.TAG_GLOBAL, "F.out()," + str);                break;            default:                Logger.d(PlayerUtil.TAG_GLOBAL, "F.out()," + str);                break;        }    }    public static String getFormattedTime(long time) {        String format = "yyyy-MM-dd HH:mm:ss";        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINESE);        return sdf.format(new Date(time));    }    public static boolean isHD2Supported() {        if (isNeonSupported()) {            double totalMemory = getTotalMemory();            double maxCpuFreq = getCpuMaxFreq();            return (totalMemory >= 1000000 && maxCpuFreq >= 1200000);        }        return false;    }    public static boolean isNeonSupported() {        InputStream in = null;        try {            String[] featureArgs = {"/system/bin/cat", "/proc/cpuinfo"};            ProcessBuilder cmd = new ProcessBuilder(featureArgs);            Process process1 = cmd.start();            in = process1.getInputStream();            byte[] temp = new byte[1024];            int count = 0;            ByteArrayOutputStream bao = new ByteArrayOutputStream();            while ((count = in.read(temp)) > 0)                bao.write(temp, 0, count);            String cpuInfo = new String(bao.toByteArray()).toLowerCase();            if (cpuInfo.contains("neon") && cpuInfo.contains("armv7")) {                return true;            }        } catch (Exception e) {            e.printStackTrace();        } finally {            try {                in.close();            } catch (IOException e) {                e.printStackTrace();            }        }        return false;    }    public static double getTotalMemory() {        InputStream in = null;        try {            String[] freqArgs = {"/system/bin/cat", "/proc/meminfo"};            ProcessBuilder cmd = new ProcessBuilder(freqArgs);            Process process = cmd.start();            in = process.getInputStream();            BufferedReader reader = new BufferedReader(                    new InputStreamReader(in));            String content = null;            while ((content = reader.readLine()) != null) {                content = content.trim().toLowerCase();                if (content.contains("memtotal")) {                    return Double.valueOf(content.substring(                            content.indexOf(":") + 1, content.indexOf("kb"))                            .trim());                }            }        } catch (Exception e) {            e.printStackTrace();        } finally {            try {                in.close();            } catch (IOException e) {                e.printStackTrace();            }        }        return 0;    }    public static double getCpuMaxFreq() {        InputStream in = null;        try {            String[] freqArgs = {"/system/bin/cat",                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};            ProcessBuilder cmd = new ProcessBuilder(freqArgs);            Process process = cmd.start();            in = process.getInputStream();            byte[] temp = new byte[1024];            int count = 0;            ByteArrayOutputStream bao = new ByteArrayOutputStream();            while ((count = in.read(temp)) > 0)                bao.write(temp, 0, count);            String cpuMaxFreq = new String(bao.toByteArray());            return Double.valueOf(cpuMaxFreq);        } catch (Exception e) {            e.printStackTrace();        } finally {            try {                in.close();            } catch (IOException e) {                e.printStackTrace();            }        }        return 0;    }    /**     * 解析302跳转获取最终地址     */    public static String getFinnalUrl(String url) {        try {            if (isFinalUrl(url)) {                return url;            }            HttpURLConnection con = (HttpURLConnection) (new URL(url)                    .openConnection());            con.setInstanceFollowRedirects(false);            con.setConnectTimeout(GET_FINAL_URL_CONNECT_TIMEOUT);            con.setReadTimeout(GET_FINAL_URL_READ_TIMEOUT);            con.connect();            String newUrl = null;            int responseCode = con.getResponseCode();            if (responseCode == 302) {                newUrl = con.getHeaderField("Location");                con.disconnect();                if (isFinalUrl(newUrl)) {                    return newUrl;                } else {                    return getFinnalUrl(newUrl);                }            } else {                Logger.e(LogTag.TAG_PLAYER, "getFinnalUrl failed,responseCode:"                        + responseCode);                return null;            }        } catch (MalformedURLException e) {            return null;        } catch (IOException e) {            return null;        }    }    /**     * onCurrentPostionUpdate 或者duration 转化为时间     *     * @param millseconds     * @return     */    public static String getFormatTime(long millseconds) {        long seconds = millseconds / 1000;        StringBuffer buf = new StringBuffer();        long hour = seconds / 3600;        long min = seconds / 60 - hour * 60;        long sec = seconds - hour * 3600 - min * 60;        if (hour < 10) {            buf.append("0");        }        buf.append(hour).append(":");        if (min < 10) {            buf.append("0");        }        buf.append(min).append(":");        if (sec < 10) {            buf.append("0");        }        buf.append(sec);        return buf.toString();    }    /**     * 显示toast     *     * @param msg     */    public static void showToast(String msg) {        if (MediaPlayerDelegate.mIToast != null)            MediaPlayerDelegate.mIToast.showToast(msg);    }    public static String getM3u8File(String path) {        FileInputStream fin = null;        ByteArrayOutputStream bao = null;        try {            fin = new FileInputStream(path);            byte[] temp = new byte[1024];            int count = 0;            bao = new ByteArrayOutputStream();            while ((count = fin.read(temp, 0, 1024)) > 0) {                bao.write(temp, 0, count);            }            return new String(bao.toByteArray());        } catch (Exception e) {            return null;        } finally {            if (fin != null)                try {                    fin.close();                } catch (IOException e) {                    e.printStackTrace();                }            if (bao != null)                try {                    bao.close();                } catch (IOException e) {                    e.printStackTrace();                }        }    }    /**     * 判断用户是否登录     *     * @return     */    public static boolean isLogin() {        if (MediaPlayerDelegate.mIUserInfo != null) {            return MediaPlayerDelegate.mIUserInfo.isLogin();        }        return false;    }    public static String getCookie() {        if (MediaPlayerDelegate.mIUserInfo == null)            return null;        return MediaPlayerDelegate.mIUserInfo.getCookie();    }    /**     * aes 解密     *     * @param content     * @param password     * @return     */    public static byte[] decrypt(byte[] content, String password) {        try {            KeyGenerator kgen = KeyGenerator.getInstance("AES");            byte[] raw = password.getBytes("utf-8");            kgen.init(new SecureRandom(raw));            SecretKeySpec key = new SecretKeySpec(raw, ALGORITHM);            Cipher cipher = Cipher.getInstance(ALGORITHM);// 创建密码器            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化            return cipher.doFinal(content);        } catch (NoSuchAlgorithmException e) {            e.printStackTrace();        } catch (NoSuchPaddingException e) {            e.printStackTrace();        } catch (InvalidKeyException e) {            e.printStackTrace();        } catch (IllegalBlockSizeException e) {            e.printStackTrace();        } catch (BadPaddingException e) {            e.printStackTrace();        } catch (UnsupportedEncodingException e) {            e.printStackTrace();        }        return null;    }    // 密钥算法    public static String ALGORITHM = "AES/ECB/NoPadding";    public static boolean isBaiduQvodSource(Source source) {        if (source == Source.BAIDU || source == Source.KUAIBO) {            return true;        }        return false;    }    /**     * 是否有虚拟按键     *     * @return     */    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)    public static boolean hasVirtualButtonBar(Context context) {        if (Build.VERSION.SDK_INT >= 18) {            return !ViewConfiguration.get(context).hasPermanentMenuKey();        } else {            return false;        }    }    public static String intToIP(int ip) {        return new StringBuilder().append((ip & 0xff)).append('.')                .append((ip >> 8) & 0xff).append('.').append((ip >> 16) & 0xff)                .append('.').append(((ip >> 24) & 0xff)).toString();    }    /**     * 判断是否是优酷的pad     */    public static boolean isYoukuTablet(Context context) {        if (com.baseproject.utils.Profile.FROM == com.baseproject.utils.Profile.FROM_TUDOU) {            return false;        } else if (com.baseproject.utils.Profile.FROM == com.baseproject.utils.Profile.FROM_TUDOU_HD) {            return true;        } else {            return UIUtils.isTablet(context)                    && Configuration.ORIENTATION_LANDSCAPE == UIUtils                    .getDeviceDefaultOrientation(context);        }    }    /**     * 判断是否为优酷pad的直播     */    public static boolean isYoukuHlsTablet(Context context, MediaPlayerDelegate mediaPlayerDelegate) {        return isYoukuTablet(context) && MediaPlayerConfiguration.getInstance().livePortrait()                && mediaPlayerDelegate != null && mediaPlayerDelegate.videoInfo != null && mediaPlayerDelegate.videoInfo.isHLS;    }    /**     * 直播302跳转     *     * @param url     * @return     */    public static String getHlsFinnalUrl(String url) {        Logger.d(LogTag.TAG_PLAYER, "getHlsFinalUrl");        try {            if (TextUtils.isEmpty(url)) {                return "";            }            HttpURLConnection con = (HttpURLConnection) (new URL(url)                    .openConnection());            con.setConnectTimeout(GET_FINAL_URL_CONNECT_TIMEOUT);            con.setReadTimeout(GET_FINAL_URL_READ_TIMEOUT);            con.setInstanceFollowRedirects(false);            con.connect();            String newUrl = null;            int code = con.getResponseCode();            if (code == 302) {                newUrl = con.getHeaderField("Location");                con.disconnect();                return newUrl;            } else {                return null;            }        } catch (MalformedURLException e) {            return null;        } catch (IOException e) {            return null;        } finally {            Logger.d(LogTag.TAG_PLAYER, "getHlsFinalUrl return");        }    }    /**     * added for Uplayer 2.4.1 获取url的接�?     * http://test.api.3g.youku.com/layout/phone2_1     * /play.text?point=1&id=XNDQyMDcxODQ4     * &pid=a1c0f66d02e2a816&format=6,5,1,7&language     * =guoyu&audiolang=1&guid=a83cf513b78750adcab88523857b652d     * &ver=2.4.0.1&network=WIFI 当传入的是mp4，flv，hd2的时候需要返回所有的格式（超标高），用于获取所有的URL     * m3u8的超标高接口数据有所不同，参见接�?     *     * @param videoType 当前需要播放的格式     * @return 需要传给接口的format     */    public static String getFormatAll(String videoType) {        if (PlayerUtil.isNull(videoType))            return "";        if (videoType.equals("4")) {            PlayerUtil.out(1, "3gphd");        } else if (videoType.equals("2")) {            PlayerUtil.out(1, "3gp");        } else if (videoType.equals("6")) {            PlayerUtil.out(1, "m3u8");        } else if (videoType.equals("5")) {            PlayerUtil.out(1, "flv");            return "1,5,7"; // 超标�?        } else if (videoType.equals("7")) {            PlayerUtil.out(1, "hd2");            return "1,5,7"; // 超标�?        } else if (videoType.equals("1")) {            PlayerUtil.out(1, "mp4");            return "1,5,7"; // 超标�?        }        return videoType;    }    public static boolean isnofreedata(String name) {        if (name.startsWith("GT-I9228") || name.startsWith("Note")                || name.startsWith("9220") || name.startsWith("I889")                || name.startsWith("I717") || name.startsWith("I9228")) {            return true;        }        return false;    }    public static int getFormat(int videoType) {        // if (F.isNull(videoType))        // return "";        if (videoType == 4) {            PlayerUtil.out(1, "3gphd");        } else if (videoType == 1) {            PlayerUtil.out(1, "mp4");        } else if (videoType == 2) {            PlayerUtil.out(1, "3gp");        } else if (videoType == 5) {            PlayerUtil.out(1, "flv");        } else if (videoType == 6) {            PlayerUtil.out(1, "m3u8");        }        return videoType;    }    public static boolean isFromLocal(final VideoUrlInfo videoInfo) {        //noinspection RedundantIfStatement        if (Profile.from == Profile.PHONE_BROWSER                || (videoInfo != null && StaticsUtil.PLAY_TYPE_LOCAL                .equals(videoInfo.getPlayType())))            return true;        return false;    }    /**     * 隐藏系统按键，竖屏不隐藏     *     * @param plugin     */    public static void hideSystemUI(Context context, final PluginOverlay plugin, boolean isFullScreen) {        if (isVirtualKeyShow(context) && isFullScreen) {            plugin.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION                    | View.SYSTEM_UI_FLAG_FULLSCREEN);        }    }    /**     * 显示系统按键     *     * @param plugin     */    public static void showSystemUI(Context context, final PluginOverlay plugin) {        if (isVirtualKeyShow(context) && plugin != null) {            plugin.setSystemUiVisibility(0);        }    }    /**     * 判断手机带有虚拟按键     */    public static boolean isVirtualKeyShow(Context context) {        try {            if ("Coolpad A8-930".equals(Build.MODEL) && "YuLong".equals(Build.MANUFACTURER.trim())) {                return true;            }            if ("HUAWEI D2-0082".equals(Build.MODEL) && "HUAWEI".equals(Build.MANUFACTURER.trim())) {                return true;            }        } catch (Exception e) {        }        if (Build.VERSION.SDK_INT >= 18) {            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);            if (!hasMenuKey && !hasBackKey) {                return true;            }            return false;        } else {            return false;        }    }    public static List<String> getDNS() {        try {            Class<?> SystemProperties = Class.forName("android.os.SystemProperties");            Method method = SystemProperties.getMethod("get", new Class[]{String.class});            ArrayList<String> dns = new ArrayList<String>();            for (String name : new String[]{"net.dns1", "net.dns2"}) {                String value = (String) method.invoke(null, name);                if (!TextUtils.isEmpty(value) && !dns.contains(value))                    dns.add(value);            }            return dns;        } catch (ClassNotFoundException e) {            e.printStackTrace();        } catch (InvocationTargetException e) {            e.printStackTrace();        } catch (NoSuchMethodException e) {            e.printStackTrace();        } catch (IllegalAccessException e) {            e.printStackTrace();        }        return null;    }    public static String getIp() {        String ip = "";        ConnectivityManager cm = (ConnectivityManager) com.baseproject.utils.Profile.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);        NetworkInfo info = cm.getActiveNetworkInfo();        if (info == null)            return "";        try {            WifiManager wm = (WifiManager) com.baseproject.utils.Profile.mContext.getSystemService(Context.WIFI_SERVICE);            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());            if (!TextUtils.isEmpty(ip)) {                if (("0.0.0.0").equals(ip))                    ip = "";                else                    return ip;            }            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();            while (networkInterfaceEnumeration.hasMoreElements()) {                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();                Enumeration<InetAddress> netAddressEnumeration = networkInterface.getInetAddresses();                while (netAddressEnumeration.hasMoreElements()) {                    InetAddress inetAddress = netAddressEnumeration.nextElement();                    if (!inetAddress.isLoopbackAddress() &&                            !inetAddress.isLinkLocalAddress() &&                            inetAddress.isSiteLocalAddress() &&                            inetAddress                                    instanceof Inet4Address) {                        ip = inetAddress.getHostAddress();                        return ip;                    }                }            }        } catch (SocketException e) {            e.printStackTrace();        } catch (Exception e) {            e.printStackTrace();        }        return ip;    }}