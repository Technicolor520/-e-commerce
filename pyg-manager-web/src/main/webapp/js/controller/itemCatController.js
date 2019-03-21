 //控制层 
app.controller('itemCatController' ,function($scope,$controller,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承


	//记录当前列表显示第几级数据，默认为第一级
	$scope.grade=1;

    $scope.entity1=null;//接收面包屑上一级分类对象
    $scope.entity2=null;//接收面包屑上二级分类对象

	$scope.parentId=0;//即将保存商品分类对象的parentId初始值为0

	//点击查询下级按钮时grade+1
	$scope.setGrade=function (grade,pojo) {
		$scope.grade=grade;
        $scope.parentId=pojo.id; //点击时获取parentId
		//当grade==1时，entity1和entity2没有值
		if($scope.grade==1){
			$scope.entity1=null;
			$scope.entity2=null;
		}
		//当grade==2时，entity1赋值pojo
		if($scope.grade==2){
			$scope.entity1=pojo;
			$scope.entity2=null;
		}
		//当grade==3时，entity2赋值pojo
		if($scope.grade==3){
			$scope.entity2=pojo;
		}

    };

	//根据parentId查询分类数据
	$scope.findByParentId=function (parentId) {
		itemCatService.findByParentId(parentId).success(function (response) {
			$scope.list=response;
        })
    };

	$scope.findTypeTemplateList=function () {
        typeTemplateService.findAll().success(function (response) {
			$scope.typeTemplateList=response;
        })
    }
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	};
	
	//分页
	$scope.findPage=function(pageNum,pageSize){			
		itemCatService.findPage(pageNum,pageSize).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	};
	
	//保存 
	$scope.save=function(){
		var serviceObject;//服务层对象
        $scope.entity['parentId']=$scope.parentId;
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.findByParentId($scope.parentId)//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	};
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	};
	
	$scope.queryEntity={};//定义搜索对象 
	
	//搜索
	$scope.query=function(pageNum,pageSize){			
		itemCatService.query(pageNum,pageSize,$scope.queryEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    
});	
