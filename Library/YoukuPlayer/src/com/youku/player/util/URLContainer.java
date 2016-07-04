package com.youku.player.util;import android.app.Activity;import android.content.Context;import android.text.TextUtils;import com.baseproject.utils.Logger;import com.baseproject.utils.Util;import com.youku.analytics.data.Device;import com.youku.player.LogTag;import com.youku.player.ad.AdGetInfo;import com.youku.player.ad.MidAdModel;import com.youku.player.base.Plantform;import com.youku.player.config.MediaPlayerConfiguration;import com.youku.player.goplay.Profile;import com.youku.player.plugin.MediaPlayerDelegate;import java.io.UnsupportedEncodingException;import java.net.URLEncoder;/** * FIXME 所有接口去掉 openapi-wireless 路径 * * @author fengqve */public class URLContainer {    // 应用中使用的服务器地址    public static String YOUKU_WIRELESS_DOMAIN;    public static String YOUKU_WIRELESS_LAYOUT_DOMAIN;    public static String YOUKU_DOMAIN;    public static String YOUKU_LIVE_DOMAIN;    public static String STATIC_DOMAIN;    public static String YOUKU_FEEDBACK_URL;    public static String YOUKU_PROMOTED_APP_URL;    public static String TUDOU_DOMAIN;//    // public static String TUDOU_TEST = "http://test1.api.3g.tudou.com/"; http://play.api.3g.tudou.com/    public static String YOUKU_AD_DOMAIN = "http://test.api.3g.youku.com";    public static String TUDOU_AD_DOMAIN = "http://test1.api.3g.tudou.com";    public static String YOUKU_HLS_DOMAIN = "http://v.l.youku.com";    public static String TUDOU_DANMAKU_DOMAIN;    // fengqve    // 正式服务器地址    public static final String OFFICIAL_YOUKU_WIRELESS_DOMAIN = "http://api.mobile.youku.com/openapi-wireless/";    public static final String OFFICIAL_YOUKU_WIRELESS_LAYOUT_DOMAIN = "http://api.mobile.youku.com/layout/";    public static final String OFFICIAL_YOUKU_DOMAIN = "http://a.play.api.3g.youku.com";    public static final String OFFICIAL_TUDOU_DOMAIN = "http://play.api.lite.tudou.com";    public static final String OFFICIAL_YOUKU_AD_DOMAIN = "http://ad.api.3g.youku.com";    public static final String OFFICIAL_TUDOU_AD_DOMAIN = "http://ad.api.lite.tudou.com";    public static final String OFFICIAL_STATIS_DOMAIN = "http://statis.api.3g.youku.com";    public static final String OFFICIAL_TUDOU_DANMAKU_DOMAIN = "http://dm.api.lite.tudou.com";    public static final String OFFICIAL_YOUKU_LIVE_DOMAIN = "http://live.api.mobile.youku.com";    public static final String OFFICIAL_YOUKU_GREY_DOMAIN = "http://api.gray.cms.mobile.youku.com";    // 测试服务器地址    public static final String TEST_YOUKU_WIRELESS_DOMAIN = "http://test2.api.3g.youku.com/openapi-wireless/";    public static final String TEST_YOUKU_WIRELESS_LAYOUT_DOMAIN = "http://test.api.3g.youku.com/layout/";    public static final String TEST_YOUKU_DOMAIN = "http://test2.api.3g.youku.com";    public static final String TEST_TUDOU_DOMAIN = "http://test.new.api.3g.tudou.com";    public static final String TEST_YOUKU_AD_DOMAIN = "http://test2.api.3g.youku.com";    public static final String TEST_TUDOU_AD_DOMAIN = "http://test.new.api.3g.tudou.com";    public static final String TEST_TUDOU_DANMAKU_DOMAIN = "http://test.new.api.3g.tudou.com";    public static final String TEST_YOUKU_GREY_DOMAIN = "http://test.api.client.m.youku.com";    // 分享网址    // 土豆相关    private static final String SECRET_TYPE = "md5";    private static final String SECRET = "82ad208d0de64414c7a4ae34a667f0b4";    private static final String METHOD_GET = "GET";    private static final String METHOD_POST = "POST";    // 付费视频曝光地址    public static final String PLAY_LOG_URL = "http://v.youku.com/player/wplaylog";    // 新的广告损耗曝光地址    public static final String AD_LOSS_URL_NEW = "http://count.atm.youku.com/mlog?";    public static final String AD_LOSS_VERSION_NEW = "2";    public static final int AD_LOSS_STEP0_NEW = 0; //APP发出广告请求时    public static final int AD_LOSS_STEP1_NEW = 1; //广告接口请求中主动返回    public static final int AD_LOSS_STEP2_NEW = 2; //广告请求成功    public static final int AD_LOSS_STEP21_NEW = 21; //广告接口请求失败    public static final int AD_LOSS_STEP3_NEW = 3; //广告素材请求中主动返回    public static final int AD_LOSS_STEP4_NEW = 4; //广告素材请求成功    public static final int AD_LOSS_STEP6_NEW = 6; //广告播放加载失败；未出现广告第一帧    // 广告损耗曝光地址    public static final String AD_LOSS_URL = "http://valf.atm.youku.com/mlog?";    public static final String AD_LOSS_VERSION = "1";    public static final String AD_LOSS_MF = "mf"; //前贴    public static final String AD_LOSS_MB = "mb"; //后贴    public static final String AD_LOSS_MP = "mp"; //暂停    public static final String AD_LOSS_MC = "mc"; //角标    public static final String AD_LOSS_MI = "mi"; //开机图    public static final String AD_LOSS_MO = "mo"; //中插    public static final int AD_LOSS_STEP1 = 1; //广告接口请求中主动返回    public static final int AD_LOSS_STEP2 = 2; //广告接口请求失败    public static final int AD_LOSS_STEP3 = 3; //广告素材请求中主动返回    public static final int AD_LOSS_STEP4 = 4; //广告素材请求失败    public static final int AD_LOSS_STEP5 = 5; //广告播放加载中主动返回    public static final int AD_LOSS_STEP6 = 6; //广告播放加载失败；未出现广告第一帧    public static String YOUKU_USER_DOMAIN = TEST_YOUKU_DOMAIN;    // fengqve    public static final int PZ = 30;    // 返回版本更新地址    public static String statistic;    public static String verName;    /**     * 初始化统计需求参数 pid guid ver operator network     */    public static void getStatisticsParameter() {        final StringBuilder s = new StringBuilder();        s.append("pid=").append(Profile.pid);        if (!TextUtils.isEmpty(Device.guid))            s.append("&guid=").append(Device.guid);        s.append("&mac=").append(Device.mac).append("&imei=")                .append(Device.imei).append("&ver=").append(verName);        if (!TextUtils.isEmpty(Device.operator))            s.append("&operator=").append(Device.operator);        if (!TextUtils.isEmpty(Device.network))            s.append("&network=").append(Device.network);        statistic = s.toString();        Logger.d(LogTag.TAG_STATISTIC, "URL请求的statistic #getStatisticsParameter-->" + statistic);    }    public static String getStatisticsParameter(String requestMethod,                                                String relativePath) {        long tmp = System.currentTimeMillis() / 1000 + Util.TIME_STAMP;        String timeStamp = String.valueOf(tmp);// 时间戳        String numRaw = requestMethod + ":" + relativePath + ":" + timeStamp                + ":" + Util.SECRET;        Logger.d(LogTag.TAG_PLAYER, "numRaw:" + numRaw);        String md5NumRaw = Util.md5(numRaw);        StringBuilder s = new StringBuilder();        s.append(relativePath).append("?");        s.append("_t_=").append(timeStamp);        s.append(MediaPlayerConfiguration.getInstance().mPlantformController.getEncryptParam()).append(SECRET_TYPE);        s.append("&_s_=").append(md5NumRaw);        return s.toString();    }    public static String getHlsStatisticsParameter() {        StringBuffer s = new StringBuffer();        s.append("&pid=").append(Device.pid).append("&guid=")                .append(Device.guid).append("&gdid=").append(Device.gdid)                .append("&appname=").append(Device.appname).append("&appver=")                .append(Device.appver).append("&brand=").append(Device.brand)                .append("&btype=").append(Device.btype).append("&os=")                .append(Device.os).append("&osver=").append(Device.os_ver);        return s.toString();    }    public static String getTuDouParameter(String requestMethod,                                           String relativePath) {        long tmp = System.currentTimeMillis() / 1000 + Util.TIME_STAMP;        String timeStamp = String.valueOf(tmp);// 时间戳        String numRaw = requestMethod + ":" + relativePath + ":" + timeStamp                + ":" + Util.SECRET;        Logger.d(LogTag.TAG_PLAYER, "numRaw:" + numRaw);        String md5NumRaw = Util.md5(numRaw);        StringBuilder s = new StringBuilder();        s.append(relativePath).append("?");        s.append("pid=").append(Profile.pid);        s.append("&_t_=").append(timeStamp);        s.append(MediaPlayerConfiguration.getInstance().mPlantformController.getEncryptParam()).append(SECRET_TYPE);        s.append("&_s_=").append(md5NumRaw);        s.append("&guid=").append(Profile.GUID);        return s.toString();    }    public static String getDanmakuParameter(String iid, int minute_at,                                             int minute_count) {        return TUDOU_DANMAKU_DOMAIN + getTuDouParameter(METHOD_GET, "/v1/danmu/list")                + "&item_code=" + iid + "&minute_at=" + minute_at                + "&minute_count=" + minute_count + getTudouDanmakuAdditional();    }    public static String getDanmakuStatusParameter(String iid, int cid) {        return TUDOU_DANMAKU_DOMAIN                + getTuDouParameter(METHOD_GET, "/v1/danmu/profile")                + "&item_code=" + iid + "&cid=" + cid + getTudouDanmakuAdditional();    }    public static String submitDanmakuParameter(String ver, String iid,                                                String playat, String propertis, String content) {        return TUDOU_DANMAKU_DOMAIN + getTuDouParameter(METHOD_POST, "/v1/danmu/add")                + "&item_codes=" + iid + "&playat=" + playat                + "&propertis=" + propertis + "&content=" + getTextEncoder(content)                + getTudouDanmakuAdditional();    }    public static String getTudouDanmakuAdditional() {        StringBuilder s = new StringBuilder();        if (!TextUtils.isEmpty(Device.operator))            s.append("&operator=").append(Device.operator);        if (!TextUtils.isEmpty(Device.network))            s.append("&network=").append(Device.network);        s.append("&ver=").append(verName);        return s.toString();    }    /**     * ************* 统计 *********************     */    public static String getStatVVBegin(String VVEndUrl, boolean isExternalVideo) {        String vv = isExternalVideo ? "/openapi-wireless/statis/extervv" : "/openapi-wireless/statis/vv";        return STATIC_DOMAIN                + getStatisticsParameter(METHOD_POST, vv) + "&"                + statistic + VVEndUrl;    }    public static String getHlsStatVVBegin(String VVBeginUrl, Context context) {        String vv = "/mlvvlog";        return YOUKU_HLS_DOMAIN + getStatisticsParameter(METHOD_POST, vv) + "&"                + VVBeginUrl + getHlsExtParameter(context) + getHlsStatisticsParameter();    }    public static String getHlsVVPlayHeart(String VVPlayHeart, Context context) {        String vv = "/mltslog";        return YOUKU_HLS_DOMAIN + getStatisticsParameter(METHOD_POST, vv) + "&"                + VVPlayHeart + getHlsExtParameter(context) + getHlsStatisticsParameter();    }    public static String getHlsExtParameter(Context context) {        String barrage = Profile.getLiveDanmakuSwith(context) ? "off" : "on";        String bg_density = Profile.getDanmakuEffect(context) == 0 ? "comfort" : "concentrate";        String bg_pos = Profile.getDanmakuPosition(context) == 0 ? "up " : "full";        return "&ext=" + getTextEncoder("barrage=" + barrage + "&bg_density=" + bg_density + "&bg_pos=" + bg_pos);    }    public static String getStatVVEnd(String VVEndUrl, boolean isExternalVideo) {        String vv = isExternalVideo ? "/openapi-wireless/statis/extervv" : "/openapi-wireless/statis/vv";        return STATIC_DOMAIN                + getStatisticsParameter(METHOD_POST, vv) + "&"                + statistic + VVEndUrl;    }    /**     * 获取广告 (目前获取剧场标版广告的接口，优酷为新加，土豆沿用以前的接口)     *     * @param adGetInfo     * @param context   7表示前贴 10表示暂停 8表示中插     * @return     */    public static String getVideoAdv(AdGetInfo adGetInfo, Context context) {        return "";        // 王岩，注释广告请求//        // site = 1 代表目前是优酷 position=7代表取前貼 10 暂停//        if (adGetInfo == null) {//            return "";//        }//        String quality = null;//        if (adGetInfo.position == AdPosition.PRE || adGetInfo.position == AdPosition.MID) {//            switch (Profile.getVideoQuality(context)) {//                case Profile.VIDEO_QUALITY_HD2://                    quality = "hd2";//                    break;//                case Profile.VIDEO_QUALITY_HD://                    quality = "mp4";//                    break;//                default://                    quality = "flv";//            }//        }//        return//                // "http://ad.api.3g.youku.com/"//                MediaPlayerConfiguration.getInstance().mPlantformController.getAdDomain()//                        + getStatisticsParameter(METHOD_GET, "/adv")//                        + "&" + getVidText() + "="//                        + adGetInfo.vid//                        + "&site=" + getSiteValue() + "&position="//                        + adGetInfo.position//                        + "&is_fullscreen=" +//                        (adGetInfo.isFullscreen ? 1 : 0)//                        + "&player_type=mdevice&sessionid="//                        + SessionUnitil.playEvent_session//                        + "&device_type="//                        + (UIUtils.isTablet(context) ? "pad" : "phone")//                        + "&device_brand="//                        + getTextEncoder(android.os.Build.BRAND)//                        + "&net="//                        + Util.getNetworkType()//                        + "&mdl="//                        + getTextEncoder(android.os.Build.MODEL)//                        + "&dvw="//                        + DetailUtil.getScreenWidth((Activity) context)//                        + "&dvh="//                        + DetailUtil.getScreenHeight((Activity) context)//                        + "&dprm="//                        + (DetailUtil.getScreenDensity((Activity) context) * 1000) / 160//                        + "&osv="//                        + getTextEncoder(android.os.Build.VERSION.RELEASE)//                        + "&aid="//                        + DetailUtil.getAndroidId(context)//                        + "&aw=a&rst="//                        + (adGetInfo.position == AdPosition.PAUSE ? "" : "flv")//                        + (adGetInfo.position == AdPosition.PAUSE ? "" : ("&noqt=" + adGetInfo.noqt))//                        + ((adGetInfo.position == AdPosition.MID || adGetInfo.position == AdPosition.SD)? ("&ps=" + adGetInfo.ps + "&pt=" + adGetInfo.pt) : "")//                        + (quality == null ? "" : ("&dq=" + quality))//                        + "&playlistCode=" + (adGetInfo.playlistCode)//                        + "&version=1.0"//                        + (Profile.PLANTFORM != Plantform.TUDOU ? ""//                        : (("&paid=" + adGetInfo.paid) + (adGetInfo.paid == 1 ? ("&tt=" + adGetInfo.trailType) : "")))//                        + "&vc=" + (adGetInfo.isOfflineAd ? "1" : "0") + "&"//                        + statistic + "&ss=" + DetailUtil.getScreenInch((Activity) context)//                        + (adGetInfo.position == AdPosition.PRE && !TextUtils.isEmpty(adGetInfo.ev) ? ("&ev=" + adGetInfo.ev) : "")//                        + (TextUtils.isEmpty(adGetInfo.playlistId) ? "" : "&d=" + adGetInfo.playlistId)//                        + "&isvert=" + adGetInfo.isvert;    }    /**     * 灰度发布初始化接口     */    public static String getGreyInitURL(String pid, String YoukuVer, String sdkVer) {        StringBuilder s = new StringBuilder();        s.append(com.baseproject.utils.Profile.DEBUG ? TEST_YOUKU_GREY_DOMAIN : OFFICIAL_YOUKU_GREY_DOMAIN)                .append("/player/gray/publish?")                .append("pid=").append(pid)                .append("&sdk_ver=").append(sdkVer)                .append("&ver=").append(YoukuVer)                .append("&guid=").append(Device.guid);        return s.toString();    }    public static String getVideoAdv(AdGetInfo adGetInfo, Context context, String adext) {        return "";  // 王岩，注释广告请求//        // site = 1 代表目前是优酷 ; position=7代表取前貼 10 暂停 16代表标版广告//        if (adGetInfo == null) {//            return "";//        }//        String quality = null;//        if (adGetInfo.position == AdPosition.PRE || adGetInfo.position == AdPosition.MID) {//            switch (Profile.getVideoQuality(context)) {//                case Profile.VIDEO_QUALITY_HD2://                    quality = "hd2";//                    break;//                case Profile.VIDEO_QUALITY_HD://                    quality = "mp4";//                    break;//                default://                    quality = "flv";//            }//        }//        return//                // "http://ad.api.3g.youku.com/"//                MediaPlayerConfiguration.getInstance().mPlantformController.getAdDomain()//                        + getStatisticsParameter(METHOD_GET, "/adv")//                        + "&" + getVidText() + "="//                        + adGetInfo.vid//                        + "&site=" + getSiteValue() + "&position="//                        + adGetInfo.position//                        + "&is_fullscreen=" +//                        (adGetInfo.isFullscreen ? 1 : 0)//                        + "&player_type=mdevice&sessionid="//                        + SessionUnitil.playEvent_session//                        + "&device_type="//                        + (UIUtils.isTablet(context) ? "pad" : "phone")//                        + "&device_brand="//                        + getTextEncoder(android.os.Build.BRAND)//                        + "&net="//                        + Util.getNetworkType()//                        + "&mdl="//                        + getTextEncoder(android.os.Build.MODEL)//                        + "&dvw="//                        + DetailUtil.getScreenWidth((Activity) context)//                        + "&dvh="//                        + DetailUtil.getScreenHeight((Activity) context)//                        + "&dprm="//                        + (DetailUtil.getScreenDensity((Activity) context) * 1000) / 160//                        + "&osv="//                        + getTextEncoder(android.os.Build.VERSION.RELEASE)//                        + "&aid="//                        + DetailUtil.getAndroidId(context)//                        + "&aw=a&rst="//                        + (adGetInfo.position == AdPosition.PAUSE ? "" : "flv")//                        + (adGetInfo.position == AdPosition.PAUSE ? "" : ("&noqt=" + adGetInfo.noqt))//                        + ((adGetInfo.position == AdPosition.MID || adGetInfo.position == AdPosition.SD) ? ("&ps=" + adGetInfo.ps + "&pt=" + adGetInfo.pt) : "")//                        + (quality == null ? "" : ("&dq=" + quality))//                        + "&playlistCode=" + (adGetInfo.playlistCode)//                        + "&version=1.0"//                        + "&adext=" + adext//                        + (Profile.PLANTFORM != Plantform.TUDOU ? ""//                        : (("&paid=" + adGetInfo.paid) + (adGetInfo.paid == 1 ? ("&tt=" + adGetInfo.trailType) : "")))//                        + "&vc=" + (adGetInfo.isOfflineAd ? "1" : "0") + "&"//                        + statistic + "&ss=" + DetailUtil.getScreenInch((Activity) context);    }    /**     * 获取剧场标版广告 (目前优酷接口为新加，土豆标版广告沿用以前的接口)     */    public static String getContentAdv(AdGetInfo adGetInfo, Context context) {        // site = 1 代表目前是优酷 position=16代表标版广告        if (!getSiteValue().equals("1")) {            return getVideoAdv(adGetInfo, context);        }        if (adGetInfo == null) {            return "";        }        String quality = null;        switch (Profile.getVideoQuality(context)) {            case Profile.VIDEO_QUALITY_HD2:                quality = "hd2";                break;            case Profile.VIDEO_QUALITY_HD:                quality = "mp4";                break;            default:                quality = "flv";        }        return                // "http://ad.api.3g.youku.com/"                MediaPlayerConfiguration.getInstance().mPlantformController.getAdDomain()                        + getStatisticsParameter(METHOD_GET, "/adv/sd")                        + "&pid=" + Profile.pid                        + "&site=" + getSiteValue()                        + "&p=" + adGetInfo.position                        + "&ps=" + adGetInfo.ps // 标版广告点的序号，0:片头，10：片尾， 1-9：片中标版点                        + "&pt=" + adGetInfo.pt // 表示广告位触发广告请求的时长秒，即触发点，单位秒                        + "&fu=" + (adGetInfo.isFullscreen ? 1 : 0)                        + "&d=" + adGetInfo.playlistId // 土豆的豆单id，优酷的播单id                        + "&sid=" + SessionUnitil.playEvent_session                        + "&td=" // 视频对应的正本视频ID,没有就填0                        + "&v=" + adGetInfo.vid                        + "&wintype=mdevice"                        + "&vs=1.0"                        + "&bd=" + getTextEncoder(android.os.Build.BRAND)                        + "&aw=a"                        + "&net=" + Util.getNetworkType()                        + "&ouid=" // 即openudid，基于移动设备的UDID，用开源算法生成的客户唯一标识                        + "&mac=" + Device.mac                        + "&avs=" + verName                        + "&rst=" + "flv"// 表示返回广告素材URL地址的文件格式,取值范围:flv,m3u8,3gphd                        + "&dq=" + quality                        + "&dvw=" + DetailUtil.getScreenWidth((Activity) context)                        + "&dvh=" + DetailUtil.getScreenHeight((Activity) context)                        + "&osv=" + getTextEncoder(android.os.Build.VERSION.RELEASE)                        + "&ss=" + DetailUtil.getScreenInch((Activity) context)                        + (adGetInfo.paid == 1 ? ("&tt=" + adGetInfo.trailType) : "")                        + "&atm=" // wintype=BDSkin时候，OpenAPI给的一个字符串，用来验证partnerid的合法性。                        + "&partnerid=" // 形如：XMjAxNg== 合作伙伴ID的base64编码，与合作播放器是一一对应的                        + "&isvert=" + adGetInfo.isvert                        + "&guid=" + Device.guid;    }    private static String getSiteValue() {        return MediaPlayerConfiguration.getInstance().mPlantformController.getSiteValue();    }    private static String getVidText() {        // TODO:土豆平台的itemCode需要改成vid参数        return MediaPlayerConfiguration.getInstance().mPlantformController.getVidText();    }    /**     * 获得剧集列表url     *     * @param id     * @return     */    public static String getSeriesDescURL(String id, int page, int pz) {        return YOUKU_DOMAIN + "shows/" + id + "/reverse/videos?" + statistic                + "&fields=vid|comm&pg=" + page + "&pz=" + pz;    }    public static final int audiolang = 1;// 需要多语言支持。    public static String getOneVideoPlayUrl(String id, String password, String local_time,                                            String local_vid, String languageCode, String format,                                            String localPoint) {        return YOUKU_DOMAIN                + getStatisticsParameter(METHOD_GET, "/v3/play/address")                + "&point=1&id=" + id                + "&local_time=" + local_time + "&local_vid=" + local_vid                + "&format=" + format + "&language=" + languageCode                + "&did=" + Device.gdid + "&ctype=" + Profile.ctype                + "&local_point=" + localPoint + "&audiolang=" + audiolang                + "&" + statistic + getPasswordRequestText(password);    }    public static String getOnePayVideoPlayUrl(String id, String password, String local_time,                                               String local_vid, String languageCode, String format,                                               String localPoint, String playlistId) {        return YOUKU_DOMAIN + getStatisticsParameter(METHOD_GET, "/common/v3/play") + "&point=1&id=" + id                + "&local_time=" + local_time + "&local_vid=" + local_vid                + "&format=" + format + "&language=" + languageCode                + "&did=" + Device.gdid + "&ctype=" + Profile.ctype                + "&local_point=" + localPoint + "&audiolang=" + audiolang                + "&" + statistic + getPasswordRequestText(password)                + getPlaylistIdRequestText(playlistId);    }    public static String getOneVideoPlayUrlTudou(String id, String password, String local_time,                                                 String local_vid, String languageCode, String format,                                                 String localPoint, int tudouquality, String playlistCode, String albumID) {        String language = "";        if (!TextUtils.isEmpty(languageCode)) {            language = "&language=" + languageCode;        }        return TUDOU_DOMAIN + getStatisticsParameter(METHOD_GET, "/v1/android_play")                + "&id=" + id + "&youku_format=" + format + "&audio_lang="                + audiolang + language + "&point=1"                + "&local_time=" + local_time + "&local_vid=" + local_vid                + "&local_point=" + localPoint + "&" + statistic                + getPlaylistCodeRequestText(playlistCode)                + getPasswordRequestText(password);    }    public static String getMutilVideoPlayUrl(String id, String password, String local_time,                                              int videostage, String local_vid, String languageCode,                                              String format, String localPoint) {        if (videostage == 0) videostage = 1;        return YOUKU_DOMAIN + "/v3/play/address?point=1&id=" + id                + "&local_time=" + local_time + "&local_vid=" + local_vid                + "&format=" + format + "&videoseq=" + videostage                + "&did=" + Device.gdid + "&ctype=" + Profile.ctype                + "&language=" + languageCode + "&audiolang=" + audiolang                + "&local_point=" + localPoint + "&" + statistic                + getPasswordRequestText(password);    }    public static String getMutilPayVideoPlayUrl(String id, String password, String local_time,                                                 int videostage, String local_vid, String languageCode,                                                 String format, String localPoint, String playlistId) {        return YOUKU_DOMAIN                + getStatisticsParameter(METHOD_GET, "/common/v3/play")                + "&point=1&id=" + id                + "&local_time=" + local_time + "&local_vid=" + local_vid                + "&format=" + format + "&videoseq=" + videostage                + "&did=" + Device.gdid + "&ctype=" + Profile.ctype                + "&language=" + languageCode + "&audiolang=" + audiolang                + "&local_point=" + localPoint + "&" + statistic                + getPasswordRequestText(password)                + getPlaylistIdRequestText(playlistId);    }    // + albumId + "&youku_format=" + format + "&audiolang="    // + audiolang + "&videoseq=" + videostage + "&language="    // + languageCode + "&point=1" + "&local_time=" + local_time    // + "&local_vid=" + local_vid + "&local_point=" + localPoint;    public static String getMutilVideoPlayUrlTudou(String id, String password,                                                   String local_time, int videostage, String local_vid,                                                   String languageCode, String format, String localPoint,                                                   String playlistCode, String albumID) {        String language = "";        if (!TextUtils.isEmpty(languageCode)) {            language = "&language=" + languageCode;        }        return TUDOU_DOMAIN + getStatisticsParameter(METHOD_GET, "/v1/android_play")                + "&id=" + id + "&youku_format="                + format + "&audio_lang=" + audiolang + "&video_seq=" + videostage                + "&" + statistic                + language + "&point=1" + "&local_time="                + local_time + "&local_vid=" + local_vid + "&local_point="                + localPoint + getPlaylistCodeRequestText(playlistCode)                + getPasswordRequestText(password);    }    /**     * 通过vid获取下一集vid     */    public static String getNextSeries(String showid, String vid) {        return YOUKU_DOMAIN + "shows/" + showid + "/next_series?showid="                + showid + "&vid=" + vid + "&" + statistic;    }    public static String getTudouVideoUrl(String id, String albumid,                                          String tudouFormat, String fileType, String videoStage,                                          String videoSeq) {        String idUrl = null;        if (!TextUtils.isEmpty(id)) {            idUrl = "id=" + id;        } else if (!TextUtils.isEmpty(albumid)) {            idUrl = "albumid=" + albumid;        }        return TUDOU_DOMAIN + "v3_1/play?" + idUrl + "&tudou_format="                + tudouFormat + "&fileType=" + fileType + "&videostage="                + videoStage + "&videoseq=" + videoSeq + Util.getSecureRequestText("/v3_1/play");    }    private static String getAlbumIdRequestText(String albumID) {        return TextUtils.isEmpty(albumID) ? "" : "&albumid=" + albumID;    }    private static String getPlaylistIdRequestText(final String playlistId) {        return TextUtils.isEmpty(playlistId) ? "" : "&playlist_id=" + playlistId;    }    private static String getPlaylistCodeRequestText(final String playlistCode) {        return TextUtils.isEmpty(playlistCode) ? "" : "&playlistCode=" + playlistCode;    }    /**     * 获取password的请求串，如果password为null或"",返回""     */    private static String getPasswordRequestText(String password) {        String passWord = null;        if (Profile.PLANTFORM == Plantform.YOUKU) {            passWord = TextUtils.isEmpty(password) ? "" : ("&password=" + getTextEncoder(password));        } else if (Profile.PLANTFORM == Plantform.TUDOU) {            passWord = TextUtils.isEmpty(password) ? "" : ("&password=" + genPassword(password));        }        return passWord;    }    public static String getTextEncoder(String s) {        if (s == null || "".equals(s))            return "";        try {            s = URLEncoder.encode(s, "UTF-8");        } catch (UnsupportedEncodingException e) {            e.printStackTrace();        } catch (NullPointerException e) {            e.printStackTrace();        }        return s;    }    /**     * 加密传输密码 原始密码     *     * @param rawPassword     * @return 加密后的密码     */    public static String genPassword(String rawPassword) {        int i = 32 - rawPassword.length() % 32;        for (int j = 0; j < i; j++) {            rawPassword += " ";        }        return getTextEncoder(AESPlus.encrypt(rawPassword)) + "&pc=" + i;    }    /**     * 土豆请求410后根据返回的timestamp更新url     */    public static String updateUrl(String url, String requestMethod) {        String relativePath = url.substring(7);        relativePath = relativePath.substring(relativePath.indexOf("/"),                relativePath.indexOf("?"));        long tmp = System.currentTimeMillis() / 1000 + Util.TIME_STAMP;        String timeStamp = String.valueOf(tmp);// 时间戳        String numRaw = requestMethod + ":" + relativePath + ":" + timeStamp                + ":" + Util.SECRET;        String md5NumRaw = Util.md5(numRaw);        StringBuffer urlStr = new StringBuffer(url);        int i = urlStr.indexOf("_t_");        int j = urlStr.indexOf("&", i);        urlStr.delete(i, j + 1);        i = urlStr.indexOf("_s_");        j = urlStr.indexOf("&", i);        urlStr.delete(i, j + 1);        urlStr.append("&_t_=").append(timeStamp);        urlStr.append("&_s_=").append(md5NumRaw);        url = urlStr.toString();        return url;    }    /**     * 获取直播地址     */    public static String getHlsUrl(String liveid, String stream) {        return TUDOU_DOMAIN                + getStatisticsParameter(METHOD_GET, "/live/" + liveid                + "/stream/" + stream) + "&" + statistic;    }    public static String getYoukuHlsUrl(String liveid) {        return YOUKU_LIVE_DOMAIN                + getStatisticsParameter(METHOD_GET, "/common/v3/getLive") + "&liveid=" + liveid + "&did=" + Device.gdid +                "&ctype=" + Profile.ctypeHLS + "&" + statistic;    }    /**     * 上报硬解错误的地址     */    public static String getHwErrorUrl() {        return YOUKU_DOMAIN                + getStatisticsParameter(METHOD_POST, "/common/harddecode/info")                + "&" + statistic + "&device=" + getTextEncoder(android.os.Build.MODEL);    }    /**     * 获取密钥     *     * @return     */    public static String getSecretKey() {        return YOUKU_DOMAIN                + getStatisticsParameter(METHOD_GET, "/common/signature/secret")                + "&" + statistic;    }    public static String getUserExperience(String vvUserExperience) {        String vv = "/mluep";        return YOUKU_HLS_DOMAIN + getStatisticsParameter(METHOD_POST, vv) + "&"                + vvUserExperience + getHlsStatisticsParameter();    }    /**     * 获取验证联通第三方接口     *     * @return     */    public static String getUnicomFreeFlowUrl() {        StringBuilder builder = new StringBuilder(                URLContainer.YOUKU_USER_DOMAIN);        builder.append(getStatisticsParameter(METHOD_GET, "/unicom/free/flow"));        String url = builder.toString();        return url;    }    /**     * 设置DEBUG模式     */    public static void setDebugMode(boolean debug) {        if (debug) {            // debug模式            YOUKU_WIRELESS_DOMAIN = TEST_YOUKU_WIRELESS_DOMAIN;            YOUKU_WIRELESS_LAYOUT_DOMAIN = TEST_YOUKU_WIRELESS_LAYOUT_DOMAIN;            YOUKU_DOMAIN = TEST_YOUKU_DOMAIN;            YOUKU_AD_DOMAIN = TEST_YOUKU_AD_DOMAIN;            TUDOU_AD_DOMAIN = TEST_TUDOU_AD_DOMAIN;            TUDOU_DOMAIN = TEST_TUDOU_DOMAIN;            STATIC_DOMAIN = TEST_YOUKU_DOMAIN;            MidAdModel.COUNTDOWN_TIME = 30 * 1000;            TUDOU_DANMAKU_DOMAIN = TEST_TUDOU_DANMAKU_DOMAIN;            YOUKU_LIVE_DOMAIN = TEST_YOUKU_DOMAIN;        } else {            YOUKU_DOMAIN = OFFICIAL_YOUKU_DOMAIN;            TUDOU_DOMAIN = OFFICIAL_TUDOU_DOMAIN;            YOUKU_AD_DOMAIN = OFFICIAL_YOUKU_AD_DOMAIN;            TUDOU_AD_DOMAIN = OFFICIAL_TUDOU_AD_DOMAIN;            STATIC_DOMAIN = OFFICIAL_STATIS_DOMAIN;            TUDOU_DANMAKU_DOMAIN = OFFICIAL_TUDOU_DANMAKU_DOMAIN;            YOUKU_LIVE_DOMAIN = OFFICIAL_YOUKU_LIVE_DOMAIN;        }    }}