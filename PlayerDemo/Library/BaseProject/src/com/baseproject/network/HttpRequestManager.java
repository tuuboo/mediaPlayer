package com.baseproject.network;import java.io.IOException;import java.io.InputStream;import java.net.HttpURLConnection;import java.net.MalformedURLException;import java.net.ProtocolException;import java.net.SocketTimeoutException;import java.net.URL;import android.text.TextUtils;import com.alibaba.fastjson.JSON;import com.baseproject.image.Utils;import com.baseproject.utils.Profile;import com.baseproject.utils.Logger;import com.baseproject.utils.Util;/** * * HTTP请求类 使用方法示例： Initial initial = new Initial(); IHttpRequest httpRequest = * YoukuService.getService(IHttpRequest.class, true); HttpIntent httpIntent = * new HttpIntent(URLContainer.getInitURL()); *  * httpRequest.request(httpIntent, new IHttpRequestCallBack() { *  * @Override public void onSuccess(HttpRequestManager httpRequestManager) { *           initial = httpRequestManager.parse(initial); } * @Override public void onFailed(String failReason) { *  *           } }); *  * @author fengqve * @modify  liubing * @desc   {@link #downloadUri(String, String, boolean)} * 		Add： * 				if (TextUtils.isEmpty(cookie)) {						throw new NullPointerException("the cookie is not setted!");					} else {						conn.setRequestProperty("Cookie", cookie);					} * 		Cookie 使用示例： * 		HttpRequestManager httpRequestTask = new HttpRequestManager();		httpRequestTask.setCookie(cookie);		HttpIntent httpIntent = new HttpIntent(URLContainer.getPlayHistoryInCloud(), true); */public class HttpRequestManager implements IHttpRequest {	public static final int SUCCESS = 0x1;	public static final int FAIL = 0x2;	/**	 * 执行状态	 */	private int state = FAIL;	public static final String METHOD_GET = "GET";	public static final String METHOD_POST = "POST";	private String method = METHOD_GET;	private boolean isSetCookie;		private String cookie;		/**	 * 默认连接超时, 默认读取超时	 */	private int connect_timeout_millis, read_timout_millis;	/**	 * 失败原因	 */	public String fail_reason;	/**	 * 传递过来需要解析的数据对象	 */	// private Object dataObject;	/**	 * 从网络拿到的数据	 */	private String dataString;	/**	 * 请求的url地址	 */	public String uri;	public HttpRequestManager() {	}	/**	 * 设置请求方法	 * 	 * @param method	 */	public void setMethod(String method) {		this.method = method;	}	/**	 * 下载给出Uri的数据	 * 	 * @param uri	 * @param method	 * @param isSetCookie	 * @return	 * @throws NullPointerException	 */	private String downloadUri(String uri, String method, boolean isSetCookie)			throws NullPointerException {		if (Util.hasInternet()) {			Utils.disableConnectionReuseIfNecessary();			InputStream is = null;			try {				URL url = new URL(uri);				HttpURLConnection conn = (HttpURLConnection) url.openConnection();				conn.setReadTimeout(read_timout_millis);				conn.setConnectTimeout(connect_timeout_millis);				conn.setRequestMethod(method);				conn.setDoInput(true);				if (isSetCookie) {					if (TextUtils.isEmpty(cookie)) {						throw new NullPointerException("the cookie is not setted!");					} else {						conn.setRequestProperty("Cookie", cookie);					}				}				conn.setRequestProperty("User-Agent", Profile.User_Agent);				// Starts the query				conn.connect();				trackNetEvent(NetEvent.netRequest, uri, "");				int response = conn.getResponseCode();				if (response == HttpURLConnection.HTTP_OK) {					is = conn.getInputStream();					dataString = Util.convertStreamToString(is);					state = SUCCESS;				} else if (response == HttpURLConnection.HTTP_BAD_REQUEST) {					is = conn.getErrorStream();					fail_reason = Util.convertStreamToString(is);				} else {					fail_reason = String.valueOf(response + " " + conn.getResponseMessage());				}				return dataString;			} catch (MalformedURLException e) {				e.printStackTrace();				fail_reason = e.toString();				return dataString;			} catch (ProtocolException e) {				e.printStackTrace();				fail_reason = e.toString();				return dataString;			} catch (SocketTimeoutException e) {				e.printStackTrace();				fail_reason = STATE_ERROR_TIMEOUT;				return dataString;			} catch (IOException e) {				e.printStackTrace();				fail_reason = e.toString();				return dataString;			} finally {				if (null != is) {					try {						is.close();					} catch (IOException e) {					}				}			}		} else {			fail_reason = STATE_ERROR_WITHOUT_NETWORK;			return dataString;		}	}	/**	 * 无网络连接错误提示	 */	public static final String STATE_ERROR_WITHOUT_NETWORK = "无网络连接，请检查后重试，先去本地视频看看吧。";	/**	 * IO异常错误提示	 */	public static final String STATE_ERROR_IO_EXCEPTION = "IO异常哦";	/**	 * 网络超时错误提示	 */	public static final String STATE_ERROR_TIMEOUT = "咦，暂时没有获取到数据，请稍后再试。";	/**	 * 协议错误提示	 */	public static final String STATE_ERROR_PROTOCOL_EXCEPTION = "协议不正确哦";	/**	 * URL地址错误提示	 */	public static final String STATE_ERROR_MALFORMED_URL_EXCEPTION = "地址不合法哦";	private YoukuAsyncTask<Object, Integer, Object> task;	/**	 * 解析json数据	 * 	 * @return	 * @throws NullPointerException	 */	@SuppressWarnings("unchecked")	public <T> T parse(T dataObject) throws NullPointerException {		dataObject = (T) JSON.parseObject(dataString, dataObject.getClass());		return dataObject;	}	@Override	public void request(HttpIntent httpIntent, final IHttpRequestCallBack callBack) {		uri = httpIntent.getStringExtra(HttpIntent.URI);		method = httpIntent.getStringExtra(HttpIntent.METHOD);		isSetCookie = httpIntent.getBooleanExtra(HttpIntent.IS_SET_COOKIE, false);		connect_timeout_millis = httpIntent.getIntExtra(HttpIntent.CONNECT_TIMEOUT, 0);		read_timout_millis = httpIntent.getIntExtra(HttpIntent.READ_TIMEOUT, 0);		task = new YoukuAsyncTask<Object, Integer, Object>() {			@Override			protected Object doInBackground(Object... params) {				return downloadUri(uri, method, isSetCookie);			}			/*			 * @see			 * com.youku.phone.YoukuAsyncTask#onPostExecute(java.lang.Object)			 */			@Override			protected void onPostExecute(Object result) {				super.onPostExecute(result);				switch (state) {				case SUCCESS:					if (callBack != null)						callBack.onSuccess(HttpRequestManager.this);					trackNetEvent(NetEvent.netResponse, uri, "1");					break;				case FAIL:					if (callBack != null) {						Logger.d(								"HttpRequestManager.request(...).new YoukuAsyncTask() {...}#onPostExecute()",								fail_reason);						callBack.onFailed(fail_reason);					}					if (!fail_reason.equals(STATE_ERROR_WITHOUT_NETWORK)) {						trackNetEvent(NetEvent.netResponse, uri, "0");					}					break;				}			}		};		task.execute();	}	@Override	public void cancel() {		if (null != task && !task.isCancelled()) {			task.cancel(true);		}	}	@Override	public String getDataString() {		return dataString;	}	private void trackNetEvent(NetEvent event, String uri, String result) {//		if (event == NetEvent.netRequest)//			TrackerEvent.netRequest(uri, Profile.isLogined);//		else if (event == NetEvent.netResponse)//			TrackerEvent.netResponse(uri, result, Profile.isLogined);	}	public enum NetEvent {		netRequest,		/** 接收网络反馈事件 */		netResponse,		/** 播放器启动，或者播放器获取到新的vid */	}	@Override	public void setCookie(String cookie) {		this.cookie = cookie;	}}