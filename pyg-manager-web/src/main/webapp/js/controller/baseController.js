app.controller("baseController",function ($scope) {
    $scope.paginationConf = {
        currentPage: 1,      //当前页
        totalItems: 10,        //总条数
        itemsPerPage: 10,       //每页条数
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {    //加载触发<paginationConf>对象触发，属性值改变时
            $scope.reloadList();//重新加载
        }
    };

    $scope.reloadList = function () {// 创建分页查询方法
        // $http.get("../brand/findPage?pageNo="+$scope.paginationConf.currentPage+"&pageSize="+$scope.paginationConf.itemsPerPage).success(function (response) {
        //     $scope.list=response.rows; //将返回值交给list
        //     $scope.paginationConf.totalItems=response.total; //将查询总数返回给分页插件
        // })
        $scope.query($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    };



    $scope.selectIds = [];//准备一个空数组
    $scope.updateSelections = function (evnet, id) {//点击复选框方法
        //event.target 点击的对象(复选框)
        if (event.target.checked) {//勾选
            //向数组中添加id，push方法
            $scope.selectIds.push(id);
        } else {//取消勾选
            //从数组中移除id，splice方法
            var index = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index, 1);
        }
    };
});