//表现层，和页面数据交互
app.controller("typeTemplateController",function ($scope,typeTemplateService,brandService,specificationService,$controller) {

    $controller("baseController",{$scope:$scope});//typeTemplateController继承baseController

    //把数组变成字符串
    $scope.arrayToString=function (array) {
        array=JSON.parse(array);
        var str="";
        for(var i=0;i<array.length;i++){
            if(i==array.length-1){
                str+=array[i].text;
            }else {
                str+=array[i].text+",";
            }
        }
        return str;
    };


    $scope.findSpecList = function () {         //创建方法
        specificationService.findSpecList().success(function (response) { //远程内置服务提交异步请求
            $scope.specList ={data:response};    //接受后台数据   json--> 数组      调用参数是遍历
        })
    };

    // $scope.brandList={data:[{"id":1,"text":"联想"},{"id":2,"text":"华为"}]};
    //查询所有品牌方法
    $scope.findBrandList = function () {         //创建方法
        brandService.findBrandList().success(function (response) { //远程内置服务提交异步请求
            $scope.brandList ={data:response};    //接受后台数据   json--> 数组      调用参数是遍历
        })
    };

    //动态添加扩展属性，数据上：向$scope.entity.customAttributeItems中添加一个对象
    $scope.addCustomAttributeItems=function () {
        $scope.entity.customAttributeItems.push({});
    };

    //动态移除扩展属性，数据上：向$scope.entity.customAttributeItems中移除一个对象
    $scope.deleCustomAttributeItems=function (index) {
        $scope.entity.customAttributeItems.splice(index,1);
    };

    //查询所有数据方法（分页）
    $scope.findAll = function () {         //创建方法
        typeTemplateService.findAll().success(function (response) { //远程内置服务提交异步请求
            $scope.list = response;    //接受后台数据   json--> 数组      调用参数是遍历
        })
    };

    //保存方法
    $scope.save = function () { //创建新增方法和修改
        var obj=null;
        if($scope.entity !=null){
            obj= typeTemplateService.add($scope.entity)
        }else {
            obj=typeTemplateService.update($scope.entity)
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
        typeTemplateService.findOne(id).success(function (response) {
            //把字符串转成json对象数组
            response.brandIds=JSON.parse(response.brandIds);
            response.specIds=JSON.parse(response.specIds);
            response.customAttributeItems=JSON.parse(response.customAttributeItems);
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
            typeTemplateService.dele($scope.selectIds).success(function (response) {
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
       typeTemplateService.query(pageNo,pageSize, $scope.queryEntity).success(function (response) {
            $scope.list = response.rows; //将返回值交给list
            $scope.paginationConf.totalItems = response.total; //将查询总数返回给分页插件
        })
    }
});