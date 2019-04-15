app.service("seckillService",function ($http) {

    this.findSeckillGoods=function () {
        return $http.post("./seckill/findSeckillGoods");
    };

    this.findOne=function (id) {
        return $http.post("./seckill/findOne/"+id);
    };

    this.saveOrder=function (id) {
        return $http.get("./seckill/saveOrder/"+id);
    }



});