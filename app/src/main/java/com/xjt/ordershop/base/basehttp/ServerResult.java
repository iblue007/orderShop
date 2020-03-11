package com.xjt.ordershop.base.basehttp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * @param <T>
 * @author cfb
 */
public class ServerResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public Map<String, Object> extraData = new HashMap<String, Object>();

    private ServerResultHeader csResult = new ServerResultHeader();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * 分页信息
     */
    private PageInfo pageInfo = new PageInfo();

    /**
     * 返回结果集
     */
    public ArrayList<T> itemList = new ArrayList<T>();
    public T resultBean;

    public T getResultBean() {
        return resultBean;
    }

    public void setResultBean(T resultBean) {
        this.resultBean = resultBean;
    }

    public boolean atLastPage = false;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public ServerResultHeader getCsResult() {
        return csResult;
    }

    public void setCsResult(ServerResultHeader csResult) {
        if (csResult != null) {
            this.csResult.setbNetworkProblem(csResult.isbNetworkProblem());
            this.csResult.setResultCode(csResult.getResultCode());
            if(csResult.getResultCode() == 10013){
                this.csResult.setResultMessage("");
            }else {
                this.csResult.setResultMessage(csResult.getResultMessage());
            }
            this.csResult.setBodyEncryptType(csResult.getBodyEncryptType());
            this.csResult.setResponseJson(csResult.getResponseJson());
            this.csResult.setServerTimeDate(csResult.getServerTimeDate());
        }
    }

    @Override
    public String toString() {
        return csResult.toString();
    }

    public boolean isRequestSuccess() {

        return 200 == csResult.getResultCode();
    }

    public  int getResultCode(){
        return  csResult.getResultCode();
    }
}
