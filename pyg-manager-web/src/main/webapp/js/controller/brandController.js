//表现层，和页面数据交互
app.controller("brandController",function ($scope,brandService,$controller) {

    $controller("baseController",{$scope:$scope});//brandController继承baseController

    ////查询所有数据方法（分页）
    $scope.findAll = function () {         //创建方法
        brandService.findAll().success(function (response) { //远程内置服务提交异步请求
            $scope.list = response;    //接受后台数据   json--> 数组      调用参数是遍历
        })
    };


    //保存方法
    $scope.save = function () { //创建新增方法和修改
        var obj=null;
        if($scope.entity !=null){
            obj= brandService.add($scope.entity)
        }else {
            obj=brandService.update($scope.entity)
        }
        obj.success(function (response) {
            if (response.success) { //增加成功刷新页面
                $scope.reloadList();//重新加载页面
            } else {
                alert(response.message)
            }
        });
    };

    //根据id数据回显
    $scope.findOne = function (id) {
        brandService.findOne(id).success(function (response) {
            $scope.entity = response;
        })
    };

    //删除方法
    $scope.dele = function () {
        if ($scope.selectIds.length == 0) {
            alert("请您选择您要删除的品牌");
            return;
        }
        var flag = window.confirm("请您确认你选择删除的品牌");
        if (flag) {
            brandService.dele($scope.selectIds).success(function (response) {
                if (response.success) {
                    $scope.reloadList();
                    $scope.selectIds = [];
                } else {
                    alert(response.message)
                }
            })
        }
    };

    //模糊查询
    $scope.queryEntity = {};
    $scope.query = function (pageNo,pageSize) {
       brandService.query(pageNo,pageSize, $scope.queryEntity).success(function (response) {
            $scope.list = response.rows; //将返回值交给list
            $scope.paginationConf.totalItems = response.total; //将查询总数返回给分页插件
        })
    }
});