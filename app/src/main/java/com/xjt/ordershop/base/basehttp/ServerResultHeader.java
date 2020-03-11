package com.xjt.ordershop.base.basehttp;


import java.io.Serializable;

/**
 * 响应结果
 * 内容未解析
 */
public class ServerResultHeader implements Serializable {

    private static final long serialVersionUID = 5271302355916459053L;
    private String message;
    private int authState;
    private String serverTimeDate;
    private long serverTimeMillisecond;
    private String costTime;
    private int stockThreshold;
    /**
     * 无法访问到服务端的数据
     */
    private boolean bNetworkProblem = false;

    /**
     * 服务端的结果状态码
     */
    private int resultCode = -1;
//
//    /**
//     * 服务端的结果描述信息
//     */
//    private String resultMessage;

    private boolean datas;

    /**
     * 接口响应内容的处理方式 0-原始内容 1- Gzip压缩
     */
    private int bodyEncryptType = 0;

    /**
     * 返回内容
     */
    private String responseJson;

//    /**
//     * 服务器当前时间
//     */
//    private long serverTime;

    private ServerResultInterceptor interceptor;

    /**
     * 请求是否成功
     *
     * @return
     */
    public boolean isRequestOK() {
        return !bNetworkProblem && resultCode == ResultCodeMap.SERVER_RESPONSE_CODE_SUCCESS;
    }

    public boolean isbNetworkProblem() {
        return bNetworkProblem;
    }

    public void setbNetworkProblem(boolean bNetworkProblem) {
        this.bNetworkProblem = bNetworkProblem;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
        //网络访问异常时，统一打点，上报当前的网络运营商
//        if (resultCode != 0) {
//            String nt = TelephoneUtil.getNT(Global.getContext());
//            String res = "unknown";
//            if ("10".equals(nt)) {
//                res = "wifi";
//            } else if ("31".equals(nt)) {
//                res = "cucc";
//            } else if ("32".equals(nt)) {
//                res = "ctcc";
//            } else if ("53".equals(nt)) {
//                res = "cmcc";
//            }
////			HiAnalytics.submitEvent(Global.getContext(), AnalyticsConstant.CODE_NET_ERROR_NETWORKTYPE, res);
//        }

        if (interceptor != null) {
            interceptor.onIntercept(null,this.resultCode);
        }
    }
    public void setResultCodeMessage(String message,int resultCode){
        this.resultCode = resultCode;
        this.message = message;
        if (interceptor != null) {
            interceptor.onIntercept(this.message,this.resultCode);
        }
    }

    public String getResultMessage() {
        return message;
    }

    public void setResultMessage(String resultMessage) {
        this.message = resultMessage;
    }

    public int getBodyEncryptType() {
        return bodyEncryptType;
    }

    public void setBodyEncryptType(int bodyEncryptType) {
        this.bodyEncryptType = bodyEncryptType;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAuthState() {
        return authState;
    }

    public void setAuthState(int authState) {
        this.authState = authState;
    }

    public String getServerTimeDate() {
        return serverTimeDate;
    }

    public void setServerTimeDate(String serverTimeDate) {
        this.serverTimeDate = serverTimeDate;
    }

    public long getServerTimeMillisecond() {
        return serverTimeMillisecond;
    }

    public void setServerTimeMillisecond(long serverTimeMillisecond) {
        this.serverTimeMillisecond = serverTimeMillisecond;
    }

    public String getCostTime() {
        return costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }

    public int getStockThreshold() {
        return stockThreshold;
    }

    public void setStockThreshold(int stockThreshold) {
        this.stockThreshold = stockThreshold;
    }

    public ServerResultInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(ServerResultInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public boolean getDatas() {
        return datas;
    }

    public void setDatas(boolean datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("resultCode=" + resultCode + ";").append("resultMessage=" + message + ";");
        strBuf.append("responseJson=" + responseJson + ";");
        return strBuf.toString();
    }
}
