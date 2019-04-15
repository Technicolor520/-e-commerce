app.controller("payController",function ($scope,payService, $location,$interval) {

    $scope.createNative=function () {
        $scope.flag=false;
        // http://pay.pinyougou.com/pay.html#?out_trade_no=1115963993823956992
        // 1、从url中获取订单号
        $scope.out_trade_no = $location.search()['out_trade_no'];
        payService.createNative( $scope.out_trade_no).success(function (response) {
            $scope.resultMap=response;
            new QRious({
                element: document.getElementById('payImg'),
                size: 300,
                value: response.code_url
            });

            $scope.orderQuery();
        })
    };

    $scope.orderQuery=function () {

        //每隔3秒查询一次，知道查询的结果的response，trade_state是SUCCESS为止，但是也不会无休止查询，规定次数10次
        // var 定时器变量= $interval(函数,间隔时间,[运行次数]);
        // 取消定时器：$interval.cancel(定时器变量);

        // $scope.times=100;//TODO 项目上线
        $scope.times=10;

        var payInterval=$interval(function () {
            if($scope.times==0){//运行100次，
                $scope.flag=true;
                $interval.cancel(payInterval);//取消定时器
            }
        payService.orderQuery($scope.out_trade_no).success(function (response) {

            if(response==null||response==""){//在100次中查询支付失败
                $interval.cancel(payInterval);
                location.href="/payfail.html"
            }
            if(response.trade_state=="SUCCESS"){//在100次中查询支付成功
                $interval.cancel(payInterval);
                //修改订单状态
                payService.updateOrder($scope.out_trade_no,response.transaction_id).success(function (res) {
                    location.href="/paysuccess.html#?totalFee="+$scope.resultMap.totalFee;
                });

            }
        });
            $scope.times--;
        },3000);
    }



});