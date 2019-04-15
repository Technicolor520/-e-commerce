app.controller("indexController",function ($scope, contentService) {

    //显示轮播图
    $scope.findByCategotyId=function (categoryId) {
        contentService.findByCategotyId(categoryId).success(function (response) {
             $scope.bannerList=response;
        })
    };

    $scope.keyword="";
    $scope.search=function () {
        if($scope.keyword==""){ //如果为空带默认的关键字
            $scope.keyword="华为";
        }
        // 跳转页面
        location.href="http://search.pinyougou.com/search.html#?keyword="+$scope.keyword;
        // location.href="http://localhost:8084/search.html#?keyword="+$scope.keyword;
    }
});
