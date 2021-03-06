/*
app.controller("searchController",function ($scope, searchService) {


    $scope.paramMap={keyword:"小米"};
    //已进入页面就初始化方法：根据关键字查询
    $scope.initSearch=function () {

        // http://search.pinyougou.com/search.html?keyword=三星
        // 从url地址中获取参数
        // alert(location.search.split("=")[1]);//原生JS的参数获取方式

        // $location.search()['keyword']; 使用angularJS的方式获取参数
        $scope.paramMap.keyword=$location.search()['keyword'];
        $scope.keyword=$scope.paramMap.keyword;  //给页面上input位置赋值
        $scope.search();

    };

    //点击页面上的搜索按钮
    $scope.searchByKeyword=function () {
        //替换关键字
        $scope.paramMap.keyword=$scope.keyword;
        $scope.search();

    };


    $scope.search=function () {
        searchService.searchByParamMap($scope.paramMap).success(function (response) {
            $scope.resultMap=response;//通过查询后获取结果
        })
    }
});
*/

app.controller("searchController",function ($scope,searchService,$location) {
    $scope.paramMap={keyword:"小米",category:'',brand:'',spec:{},price:'',order:'asc',pageNo:1}; //初始化的关键字

    // 点击分类、品牌、价格触发的方法
    $scope.addParamToParamMap=function (key,value) {
        $scope.paramMap[key]=value;
        $scope.search();
    };
    // 点击删除分类、品牌触发的方法
    $scope.removeParamFromParamMap=function (key) {
        $scope.paramMap[key]="";
        $scope.search();
    };

    // 点击规格小项触发的方法
    $scope.addParamToParamMapSpec=function (key,value) {
        $scope.paramMap.spec[key]=value;
        $scope.search();
    };
    // 从map对象中移除一对数据
    $scope.removeParamFromParamMapSpec=function (key) {
        delete $scope.paramMap.spec[key];  //从map对象中移除一对数据
        $scope.search();
    };

    // 初始化方法 一开打页面时
    $scope.initSearch=function () {
        // http://search.pinyougou.com/search.html?keyword=三星
        // 从url地址中获取参数
        // alert(location.search.split("=")[1]);//原生JS的参数获取方式

        // $location.search()['keyword']; 使用angularJS的方式获取参数
        if($location.search()['keyword']==undefined||$location.search()['keyword']=='undefined'){
            // url地址上没有参数
        }else{
            $scope.paramMap.keyword=$location.search()['keyword'];
        }

        $scope.keyword=$scope.paramMap.keyword;  //给页面上input位置赋值
        $scope.search();
    };

    $scope.searchByKeyword=function () {
        $scope.paramMap={keyword:"小米",category:'',brand:'',spec:{},price:'',order:'asc',pageNo:1}; //初始化的关键字
        // 替换关键字
        $scope.paramMap.keyword=$scope.keyword;
        $scope.search();
    };

    $scope.search=function(){
        searchService.searchByParamMap($scope.paramMap).success(function (response) {
            $scope.resultMap = response;
            buildPageLabel();
            // $scope.pageLabel=[];
            // for (var i = 1; i <= response.totalPages; i++) {
            //     $scope.pageLabel.push(i);
            // }

        })
    };


    function buildPageLabel() {
        $scope.pageLable = [];//新增分页栏属性
        var maxPageNo = $scope.resultMap.totalPages;//得到最后页码
        var firstPage = 1;//开始页码
        var lastPage = maxPageNo;//截止页码
        $scope.firstDot = true;//前面有点
        $scope.lastDot = true;//后边有点
        if ($scope.resultMap.totalPages > 5) { //如果总页数大于 5 页,显示部分页码
            if ($scope.paramMap.pageNo <= 3) {//如果当前页小于等于 3
                lastPage = 5; //前 5 页
                $scope.firstDot = false;//前面没点
            } else if ($scope.paramMap.pageNo >= lastPage - 2) {//如果当前页大于等于最大页码-2
                firstPage = maxPageNo - 4;  //后 5 页
                $scope.lastDot = false;//后边没点
            } else { //显示当前页为中心的 5 页
                firstPage = $scope.paramMap.pageNo - 2;
                lastPage = $scope.paramMap.pageNo + 2;
            }
        } else {
            $scope.firstDot = false;//前面无点
            $scope.lastDot = false;//后边无点
        }
        //循环产生页码标签
        for (var i = firstPage; i <= lastPage; i++) {
            $scope.pageLable.push(i);
        }
    }

});