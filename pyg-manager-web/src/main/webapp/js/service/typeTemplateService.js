// 业务逻辑层和后台数据交互
app.service("typeTemplateService",function ($http) {

    this.findAll = function () {         //创建方法
        // $http.post("../typeTemplate/findAll").success(function (response) { //远程内置服务提交异步请求
        //     $scope.list = response;    //接受后台数据   json--> 数组      调用参数是遍历
        // })
       return $http.post("../typeTemplate/findAll");
    };

    //保存方法
    this.add=function (entity) {
        return $http.post("../typeTemplate/add",entity);
    };
    //修改方法
    this.update=function (entity) {
        return $http.post("../typeTemplate/update",entity);
    };

    //数据回显
    this.findOne = function (id) {
        return $http.get("../typeTemplate/findOne?id=" + id)
    };
    //删除
    this.dele = function (selectIds) {
        return $http.get("../typeTemplate/dele?Ids="+selectIds)
        };

    //模糊查询
    this.query = function (pageNo,pageSize,queryEntity) {
        return $http.post("../typeTemplate/query?pageNo="+pageNo+"&pageSize="+pageSize,queryEntity)
    };

});