app.service("contentService",function ($http) {
    
    //显示轮播图
    this.findByCategotyId=function (categoryId) {
        return $http.post("./index/findByCategotyId/"+categoryId);
    }
});