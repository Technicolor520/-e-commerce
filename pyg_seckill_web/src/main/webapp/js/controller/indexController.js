app.controller("indexController",function ($scope, contentService) {

    //显示轮播图
    $scope.findByCategotyId=function (categoryId) {
        contentService.findByCategotyId(categoryId).success(function (response) {
             $scope.bannerList=response;
        })
    }
});