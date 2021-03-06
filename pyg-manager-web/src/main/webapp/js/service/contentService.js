//服务层
app.service('contentService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../content/findAll');		
	}
	//分页 
	this.findPage=function(pageNum,pageSize){
		return $http.get('../content/findPage?pageNum='+pageNum+'&pageSize='+pageSize);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../content/findOne?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../content/add',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../content/update',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../content/delete?ids='+ids);
	}
	//搜索
	this.query=function(pageNum,pageSize,queryEntity){
		return $http.post('../content/query?pageNum='+pageNum+"&pageSize="+pageSize, queryEntity);
	}    	
});
