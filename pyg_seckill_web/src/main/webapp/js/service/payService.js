app.service("payService",function ($http) {



    //生成二维码
    this.createNative=function (out_trade_no) {
        return $http.get("./pay/createNative?out_trade_no="+out_trade_no);
    };
    //查询订单
    this.orderQuery=function (out_trade_no) {
        return $http.get("./pay/orderQuery?out_trade_no="+out_trade_no);
    };

    //修改订单状态
    this.updateOrder=function (out_trade_no,transaction_id) {
        return $http.get("./pay/updateOrder?out_trade_no="+out_trade_no+"&transaction_id="+transaction_id);
    };
});