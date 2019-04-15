app.controller("orderController",function ($scope,orderService,addressService,cartService) {

    $scope.selectedAddress=null;
    $scope.findAddressList=function () {
        addressService.findAddressListByUserId().success(function (response) {
            $scope.addressList=response;
            for (var i = 0; i < response.length; i++) {
                if(response[i].isDefault=='1'){
                    $scope.selectedAddress = response[i];
                    break;//跳出循环
                }
            }
            if($scope.selectedAddress==null){  //若没有默认值  给第一个
                if(response!=null&&response.length>0){
                $scope.selectedAddress=response[0];
                }
            }
        })
    };

    $scope.updateSelectedAddress=function (pojo) {
        $scope.selectedAddress=pojo;
    };

    $scope.isSelectedAdress=function (pojo) {
        return $scope.selectedAddress==pojo;
    };

    $scope.findCartList=function () {
        cartService.findCartList().success(function (response) {
            $scope.cartList=response;
            $scope.totalNum=0;
            $scope.totalMoney=0.00;
            for (var i = 0; i < response.length; i++) {
                var cart = response[i];
                var orderItemList = cart.orderItemList;
                for (var j = 0; j < orderItemList.length; j++) {
                    var tbOrderItem = orderItemList[j];
                    $scope.totalNum+=tbOrderItem.num;
                    $scope.totalMoney+=tbOrderItem.totalFee;
                }
            }
        })
    };



    //根据购物车的数据生成订单
    $scope.entity={sourceType:'2',paymentType:'1'};
    $scope.saveOrder=function () {
        // `payment_type` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '支付类型，1、在线支付 微信，2、货到付款',
        //     `receiver_area_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人地区名称(省，市，县)街道',
        //     `receiver_mobile` varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人手机',
        //     `receiver` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人',
        //     `source_type` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端',
        $scope.entity['receiverAreaName']=$scope.selectedAddress.address;
        $scope.entity['receiverMobile']=$scope.selectedAddress.mobile;
        $scope.entity['receiver']=$scope.selectedAddress.contact;

        orderService.add($scope.entity).success(function (response) {
            if(response.success){
                location.href="http://pay.pinyougou.com/pay.html#?out_trade_no="+response.message;
            }else {
                alert(response.message);
            }
        });


    }
});