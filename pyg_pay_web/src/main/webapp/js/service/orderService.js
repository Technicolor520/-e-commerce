app.service("orderService",function ($http) {

    // this.findCartList=function () {
    //     return $http.get("./cart/findCartList");
    // };
    // this.addGoodsToCartList=function (itemId,num) {
    //     return $http.get("./cart/addGoodsToCartList?itemId="+itemId+"&num="+num);
    // }

    //根据购物车的数据生成订单
    this.add=function (order) {
       return $http.post("./order/add",order)
    };


});