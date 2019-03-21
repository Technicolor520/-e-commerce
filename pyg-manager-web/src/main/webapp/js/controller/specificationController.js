//表现层，和页面数据交互
app.controller("specificationController",function ($scope,specificationService,$controller) {

    $controller("baseController",{$scope:$scope});//specificationController继承baseController

    //添加规格小项：从数据本质上就是向$scope.entity.tbSpecificationOptionList中添加一个对象
    $scope.addTbSpecificationOptions=function () {
        $scope.entity.tbSpecificationOptionList.push({})
    };
    //删除规格小项：从数据本质上就是向$scope.entity.tbSpecificationOptionList中移除一个对象
    $scope.deleTbSpecificationOptions=function (index) {
        $scope.entity.tbSpecificationOptionList.splice(index,1);
    };


    $scope.findAll = function () {         //创建方法
        specificationService.findAll().success(function (response) { //远程内置服务提交异步请求
            $scope.list = response;    //接受后台数据   json--> 数组      调用参数是遍历
        })
    };


    // 规格管理{"tbSpecificationOptionList":[{"optionName":"玻璃","orders":"1"},
    //     {"optionName":"陶瓷","orders":"2"}],"tbSpecification":{"specName":"水杯"}}
    $scope.save = function () { //创建新增方法和修改
        var obj=null;
        if($scope.entity.tbSpecification.id !=null){
            obj= specificationService.update($scope.entity)
        }else {
            obj=specificationService.add($scope.entity)
        }
        obj.success(function (response) {
            if (response.success) { //增加成功刷新页面
                $scope.reloadList();//重新加载页面
            } else {
                alert(response.message)
            }
        });
    };

    $scope.findOne = function (id) {
        specificationService.findOne(id).success(function (response) {
            $scope.entity = response;
        })
    };

    $scope.dele = function () {
        if ($scope.selectIds.length == 0) {
            alert("请您选择您要删除的品牌");
            return;
        }
        var flag = window.confirm("请您确认你选择删除的品牌");
        if (flag) {
            specificationService.dele($scope.selectIds).success(function (response) {
                if (response.success) {
                    $scope.reloadList();
                    $scope.selectIds = [];
                } else {
                    alert(response.message)
                }
            })
        }
    };
    $scope.queryEntity = {};
    $scope.query = function (pageNo,pageSize) {
       specificationService.query(pageNo,pageSize, $scope.queryEntity).success(function (response) {
            $scope.list = response.rows; //将返回值交给list
            $scope.paginationConf.totalItems = response.total; //将查询总数返回给分页插件
        })
    }
});