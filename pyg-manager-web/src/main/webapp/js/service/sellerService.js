//服务层
app.service('sellerService',function($http){

    this.updateStatus=function (status,sellerId) {
        return $http.get('../seller/updateStatus/'+status+"/"+sellerId);
    };
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../seller/findAll');		
	}
	//分页 
	this.findPage=function(pageNum,pageSize){
		return $http.get('../seller/findPage?pageNum='+pageNum+'&pageSize='+pageSize);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../seller/findOne?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../seller/add',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../seller/update',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../seller/delete?ids='+ids);
	}
	//搜索
	this.query=function(pageNo,pageSize,queryEntity){
		return $http.post('../seller/query?pageNo='+pageNo+"&pageSize="+pageSize, queryEntity);
	}    	
});
