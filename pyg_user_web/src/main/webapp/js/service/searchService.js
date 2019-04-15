app.service("searchService",function ($http) {
    
//已进入页面就初始化方法：根据关键字查询
    this.searchByParamMap=function (paramMap) {
        return $http.post("./search/searchByParamMap",paramMap);
    }

});