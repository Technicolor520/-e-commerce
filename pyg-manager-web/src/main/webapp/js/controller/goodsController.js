 //控制层 
app.controller('goodsController' ,function($scope,$controller,itemCatService   ,goodsService){
	
	$controller('baseController',{$scope:$scope});//继承


	$scope.itemCat={};
	$scope.findItemCat=function () {
		itemCatService.findAll().success(function (response) {
            for (var i = 0; i < response.length; i++) {
            	//将id：560，name：手机---->key：value形式（560：手机）
                 $scope.itemCat[response[i].id]=response[i].name;
            }
        })
    };
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	};
	
	//分页
	$scope.findPage=function(pageNum,pageSize){			
		goodsService.findPage(pageNum,pageSize).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};
	
	//查询实体 
	$scope.findOne=function(id){				
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	};
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	};
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
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
		goodsService.query(pageNum,pageSize,$scope.queryEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    $scope.updateAuditStatus=function (status, info) {
        //确认数组是否有值
        if($scope.selectIds.length==0){
            alert("please choose the data you want");
            return;
        }
        var flag =window.confirm("确定要"+info+"您选择的商品吗？")
        if(flag){
            goodsService.updateAuditStatus(status,$scope.selectIds).success(function (response) {
                if(response.success){
                    //重新加载页面
                    //清空数组
                    $scope.reloadList();
                    $scope.selectIds=[];
                }else{
                    alert(response.message)
                }
            })
        }
    };
    
});	
