package com.hxqc.mall.core.model.order;

/**
 * liaoguilng
 * 违章处理bean
 * Created by CPR113 on 2016/4/26.
 */
public class AnnualnspectionBean extends BaseOrderStatus.WCAOrderStatus{
    public  String orderID;
    public  float amount;
    public String plateNumber;
    public String paymentID;//'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下|'BALANCE'余额|'DISCOUNT' 优惠抵扣 string
    public String paymentIDText;//'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下|'BALANCE'余额|'DISCOUNT' 优惠抵扣 string
    public String exemption;//车辆免检期判断

}
