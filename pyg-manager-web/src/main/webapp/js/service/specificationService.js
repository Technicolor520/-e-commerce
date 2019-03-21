// 业务逻辑层和后台数据交互
app.service("specificationService",function ($http) {



    /**
     * 此方法是添加模板时要求的，数据格式为[{"id":1,"text":"联想"},{"id":2,"text":"华为"}]
     */
    this.findSpecList=function () {
        return $http.get("../specification/findSpecList");
    };

    this.findAll = function () {         //创建方法
        // $http.post("../specification/findAll").success(function (response) { //远程内置服务提交异步请求
        //     $scope.list = response;    //接受后台数据   json--> 数组      调用参数是遍历
        // })
       return $http.post("../specification/findAll");
    };

    //保存方法
    this.add=function (entity) {
        return $http.post("../specification/add",entity);
    };

    // this.addTbSpecificationOptions=function (entity) {
    //     return $http.post("../specification/add",entity);
    // }
    this.update=function (entity) {
        return $http.post("../specification/update",entity);
    };

    this.findOne = function (id) {
        return $http.get("../specification/findOne?id=" + id)
    };
    //删除
    this.dele = function (selectIds) {
        return $http.get("../specification/dele?Ids="+selectIds)
        };

    this.query = function (pageNo,pageSize,queryEntity) {
        return $http.post("../specification/query?pageNo="+pageNo+"&pageSize="+pageSize,queryEntity)
    };

});