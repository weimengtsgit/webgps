var $=function(id){return document.getElementById(id)};
Array.prototype.extend=function(C){for(var B=0,A=C.length;B<A;B++){this.push(C[B]);}return this;}
function getElementPos(element){
	var offsetTop = element.offsetTop;
	var offsetLeft = element.offsetLeft;
	while(element = element.offsetParent){
		offsetTop += element.offsetTop;
		offsetLeft += element.offsetLeft;
	}	
	return {x:offsetLeft ,y:offsetTop};
}
function divDrag(){
	this.objs=new Array();//存储对象
	this.dragFlag = true;//默认能拖拽
	this.setDragFlag=function(flag){
		this.dragFlag=flag;
		var size = this.objs.length;
		for(var i=0;i<size;i++){
			this.objs[i].dragFlag=flag;
			//alert(this.objs[i].id);
		}
	}
	var A,B,$cn,$cn_sun,$cn_brother;
	var offset_x=10,offset_y=0;//偏移量
	var zIndex=1;
	this.dragStart=function(e){
		if(!this.dragFlag)
			return;
		e=e||window.event;
		if((e.which && (e.which!=1))||(e.button && (e.button!=1)))
			return;
		if(!this.other){//单个
			$cn=this;
			save_position($cn, e);
		}else if(this.parentNode && this.parentNode==this.other){//父子关系,this.other为父
			$cn=this.other;
			$cn_sun=this;
			save_position($cn, e);
		}else if(this.parentNode && this.other.parentNode==this){//父子关系,this为父
			$cn=this;
			$cn_sun=this.other;
			save_position($cn, e);
		}else{//兄弟关系
			$cn=this;
			$cn_brother=this.other;
			save_position($cn, e, $cn_brother);
		}
		this.stop(e);
	}
	//拖动事件方法
	this.dragMove=function(e){
		e=e||window.event;
		obj_move($cn, e);
		obj_move($cn_brother, e);
		this.stop(e);
	}
	//拖动停止事件方法
	this.dragEnd=function(e){
		var pos=$cn.$pos;
		//恢复成原来的定位模式
		if($cn.style.position!=$cn.old_position){
			$cn.style.position=$cn.old_position;
			$cn.style.top=$cn.old_top;
			$cn.style.left=$cn.old_left;
			$cn.style.bottom=$cn.old_bottom;
			$cn.style.right=$cn.old_right;
		}
		//恢复成原来的定位模式
		if($cn_brother && $cn_brother.style.position!=$cn_brother.old_position){
			$cn_brother.style.position=$cn_brother.old_position;
			$cn_brother.style.top=$cn_brother.old_top;
			$cn_brother.style.left=$cn_brother.old_left;
			$cn_brother.style.bottom=$cn_brother.old_bottom;
			$cn_brother.style.right=$cn_brother.old_right;
		}
		e=e||window.event;
		if((e.which && (e.which!=1))||(e.button && (e.button!=1)))
			return;
		$cn.style.backgroundColor=pos.color;
		$cn.style.zIndex=(zIndex-1);
		if(!!($cn_sun)){
			$cn.style.backgroundColor=pos.color;
			$cn_sun.style.backgroundColor=$cn_sun.$pos.color;
			$cn_sun=null;
		}
		if(!!($cn_brother)){
			$cn_brother.style.backgroundColor=$cn_brother.$pos.color;
			$cn_brother.style.zIndex=(zIndex-1);
			$cn_brother=null;
		}
		if(document.removeEventListener){
			document.removeEventListener("mousemove",A,false);
			document.removeEventListener("mouseup",B,false);
		}else{
			document.detachEvent("onmousemove",A);
			document.detachEvent("onmouseup",B);
		}
		A=null;
		B=null;
		this.stop(e);
	}
	this.shiftColor=function(){
		this.style.backgroundColor="#EEEEEE";
	}
	this.position=function (e,xx_type,yy_type){
		var t=e.offsetTop;
		var l=e.offsetLeft;
		while(e=e.offsetParent){
			t+=e.offsetTop;
			l+=e.offsetLeft;
		}
		return {
			x:l,y:t,left_x:-1,top_y:-1,right_x:-1,bottom_y:-1,y_type:yy_type==''?'top':yy_type,x_type:xx_type==''?'left':xx_type,color:null
		}
	}
	this.stop=function(e){
		if(e.stopPropagation){
			e.stopPropagation();
		}else{
			e.cancelBubble=true;
		}
		if(e.preventDefault){
			e.preventDefault();
		}else{
			e.returnValue=false;
		}
	}
	this.stop1=function(e){
		e=e||window.event;
		if(e.stopPropagation){
			e.stopPropagation();
		}else{
			e.cancelBubble=true;
		}
	}
	this.create=function(bind){
		var B=this;
		var A=bind;
		return function(e){
			return B.apply(A,[e]);
		}
	}
	this.dragStart.create=this.create;
	this.dragMove.create=this.create;
	this.dragEnd.create=this.create;
	this.shiftColor.create=this.create;
	//初始化
	this.initialize=function(){
		/**
		for(var A=0,B=arguments.length;A<B;A++){
			C=arguments[A];
			if(!(C.push)){
				C=[C];
			}
			$C=(typeof(C[0])=='object')?C[0]:(typeof(C[0])=='string'?$(C[0]):null);
			if(!$C)
				continue;
			$C.$pos=this.position($C);
			$C.dragMove=this.dragMove;
			$C.dragEnd=this.dragEnd;
			$C.stop=this.stop;
			$C.dragFlag=this.dragFlag;
			if(!!C[1]){
				$C.other=C[1];
				$C.$pos.color=$C.style.backgroundColor;
				
				$C1=(typeof(C[1])=='object')?C[1]:(typeof(C[1])=='string'?$(C[1]):null);
				if($C1){
					$C1.$pos=this.position($C);
					$C1.dragMove=this.dragMove;
					$C1.dragEnd=this.dragEnd;
					$C1.stop=this.stop;
					$C1.dragFlag=this.dragFlag;
					$C1.$pos.color=$C1.style.backgroundColor;
				}
			}
			if($C.addEventListener){
				$C.addEventListener("mousedown",this.dragStart.create($C),false);
				if(!!C[1]){
					$C.addEventListener("mousedown",this.shiftColor.create($C),false);
				}
			}else{
				$C.attachEvent("onmousedown",this.dragStart.create($C));
				if(!!C[1]){
					$C.attachEvent("onmousedown",this.shiftColor.create($C));
				}
			}
			this.objs[this.objs.length]=$C;
		}//*/
	}
	this.initialize.apply(this,arguments);//通过参数初始化
	//增加拖动对象
	this.add=function(args){
		//for(var A=0,B=args.length;A<B;A++){
		if(args==null || args.length<1)
			return;
			C=[args[0]];
			$C=(typeof(C[0])=='object')?C[0]:(typeof(C[0])=='string'?$(C[0]):null);
			if(!$C)
				return;
				//continue;
			xx_type=[args[1]];//x坐标对齐类型： left或者right
			yy_type=[args[2]];//y坐标对齐类型： top或者bottom
			$C.$pos=this.position($C,xx_type,yy_type);
			$C.dragMove=this.dragMove;
			$C.dragEnd=this.dragEnd;
			$C.stop=this.stop;
			$C.dragFlag=this.dragFlag;
			$C.$pos.color=$C.style.backgroundColor;
			C1=[args[3]];//关系对象（父子、子父、兄弟关系）
			if(C1!=''){
				$C1=(typeof(C1[0])=='object')?C1[0]:(typeof(C1[0])=='string'?$(C1[0]):null);
				if($C1){
					$C.other=$C1;
					$C1.$pos=this.position($C,xx_type,yy_type);
					$C1.dragMove=this.dragMove;
					$C1.dragEnd=this.dragEnd;
					$C1.stop=this.stop;
					$C1.dragFlag=this.dragFlag;
					$C1.$pos.color=$C1.style.backgroundColor;
				}
			}
			C2=[args[4]];//监听开始拖动事件的具体对象
			//增加监听开始拖动事件
			if(C2!=''){
				$C2=(typeof(C2[0])=='object')?C2[0]:(typeof(C2[0])=='string'?$(C2[0]):null);
				if($C2.addEventListener){
					$C2.addEventListener("mousedown",this.dragStart.create($C),false);
					if(C1!=''){
						$C2.addEventListener("mousedown",this.shiftColor.create($C),false);
					}
				}else{
					$C2.attachEvent("onmousedown",this.dragStart.create($C));
					if(C1!=''){
						$C2.attachEvent("onmousedown",this.shiftColor.create($C));
					}
				}
			}else{
				if($C.addEventListener){
					$C.addEventListener("mousedown",this.dragStart.create($C),false);
					if(C1!=''){
						$C.addEventListener("mousedown",this.shiftColor.create($C),false);
					}
				}else{
					$C.attachEvent("onmousedown",this.dragStart.create($C));
					if(C1!=''){
						$C.attachEvent("onmousedown",this.shiftColor.create($C));
					}
				}
			}
			this.objs[this.objs.length]=$C;
			//alert($C.innerHTML+" "+C[0].id);
	}
	//记录位置类型、添加监听事件、获得对象位置
	function save_position(obj, e, brother_obj){
		get_obj_position(obj, e, 0);
		obj.style.zIndex=(zIndex+1);
		if(!!A){
			if(document.removeEventListener){
				document.removeEventListener("mousemove",A,false);
				document.removeEventListener("mouseup",B,false);
			}else{
				document.detachEvent("onmousemove",A);
				document.detachEvent("onmouseup",B);
			}
		}
		A=obj.dragMove.create(obj);
		B=obj.dragEnd.create(obj);
		if(document.addEventListener){
			document.addEventListener("mousemove",A,false);
			document.addEventListener("mouseup",B,false);
		}else{
			document.attachEvent("onmousemove",A);
			document.attachEvent("onmouseup",B);
		}
		//处理兄弟节点
		if(brother_obj){
			get_obj_position(brother_obj, e, obj.offsetHeight);
			brother_obj.style.zIndex=(zIndex+1);
		}
	}
	//获得对象位置
	function get_obj_position(obj, e, height){
		var pos = obj.$pos;
		if(document.defaultView){
			_top=document.defaultView.getComputedStyle(obj,null).getPropertyValue("top");
			//_bottom=document.defaultView.getComputedStyle(obj,null).getPropertyValue("top");
			_bottom=document.documentElement.clientHeight-parseInt(_top)-obj.clientHeight;//81
			//alert("top:"+_top+" bottom:"+_bottom);
			_left=document.defaultView.getComputedStyle(obj,null).getPropertyValue("left");
			_right=document.defaultView.getComputedStyle(obj,null).getPropertyValue("right");
			//_right=document.documentElement.clientWidth-parseInt(_left)-obj.clientWidth;//94
			//alert(_left+" "+_right);
			_position=document.defaultView.getComputedStyle(obj,null).getPropertyValue("position");
		}else{
			if(obj.currentStyle){
				_top=obj.currentStyle["top"];
				_bottom=obj.currentStyle["bottom"];
				_left=obj.currentStyle["left"];
				_right=obj.currentStyle["right"];
				_position=obj.currentStyle["position"];
			}
		}
		obj.style.position=_position;
		//如果原来不是绝对定位保存
		if(obj.style.position!="absolute"){
			obj.old_position=obj.style.position;
		}else{
			obj.old_position="absolute";
		}
		obj.old_top=obj.style.top;
		obj.old_bottom=obj.style.bottom;
		obj.old_left=obj.style.left;
		obj.old_right=obj.style.right;

		poss = getElementPos(obj);
		if(_top!="auto"){//如果css属性top已定义
			//window.status="e.clientY="+e.clientY+"\r\n scrollTop="+document.documentElement.scrollTop +"\r\n _top="+_top;
			pos.top_y=(e.pageY||(e.clientY+document.documentElement.scrollTop))-parseInt(_top);
		}else{//css属性top未定义
			//pos.top_y=(e.pageY||(e.clientY-document.documentElement.scrollTop))-height;
			//pos.top_y=parseInt(poss.y)-offset_y-height;
			pos.top_y=-offset_y-height;
		}
		if(_bottom!="auto"){//如果css属性bottom已定义
			//window.status="e.clientY="+e.clientY+"\r\n clientHeight="+document.documentElement.clientHeight +"\r\nscrollTop="+document.documentElement.scrollTop+"\r\n _bottom="+_bottom;
			if(!e.pageY){
				pos.bottom_y=((document.documentElement.clientHeight-e.clientY-document.documentElement.scrollTop))-parseInt(_bottom);
			}else{
				pos.bottom_y=((document.documentElement.clientHeight-e.pageY))-parseInt(_bottom);
			}
		}else{//css属性bottom未定义
			if(!e.pageY){
				pos.bottom_y=((document.documentElement.clientHeight-e.clientY-document.documentElement.scrollTop))+offset_y-height;
			}else{
				pos.bottom_y=((document.documentElement.clientHeight-e.pageY))+offset_y-height;
			}
		}
		if(_left!="auto"){//如果css属性left已定义
			pos.left_x=(e.pageX||(e.clientX+document.documentElement.scrollLeft))-parseInt(_left);
		}else{//css属性left未定义
			//pos.left_x=-document.documentElement.scrollLeft;
			//pos.left_x=parseInt(poss.x)-offset_x;
			pos.left_x=-offset_x;
		}
		if(_right!="auto"){//如果css属性right已定义
			//window.status="e.clientX="+e.clientX+"\r\n clientWidth="+document.documentElement.clientWidth +"\r\nscrollLeft="+document.documentElement.scrollLeft+"\r\n _right="+_right;
			if(!e.pageX){
				pos.right_x=((document.documentElement.clientWidth-e.clientX-document.documentElement.scrollLeft))-parseInt(_right);
			}else{
				pos.right_x=((document.documentElement.clientWidth-e.pageX))-parseInt(_right);
			}
		}else{//css属性right未定义
			if(!e.pageX){
				pos.right_x=((document.documentElement.clientWidth-e.clientX-document.documentElement.scrollLeft))+offset_x;
			}else{
				pos.right_x=((document.documentElement.clientWidth-e.pageX))+offset_x;
			}
		}
		var alertCon='';
		if(pos.y_type=='top'){
			alertCon+='top_y='+pos.top_y;
		}else{
			alertCon+='bottom_y='+pos.bottom_y;
		}
		if(pos.x_type=='left'){
			alertCon+=' left_x='+pos.left_x;
		}else{
			alertCon+=' right_x='+pos.right_x;
		}
		//window.status=alertCon;
		//alert(alertCon);
	}
	//移动对象 
	function obj_move(obj, e){
		if(!obj)
			return;
		var pos=obj.$pos;
		if(pos.y_type=='top'){
			//window.status="e.clientY="+e.clientY+"\r\n scrollTop="+document.documentElement.scrollTop +"\r\n pos.top_y="+pos.top_y;
			var temp_top = (e.pageY||(e.clientY+document.documentElement.scrollTop))-parseInt(pos.top_y);
			if(temp_top <0)
				temp_top = 0;
			if(temp_top > document.documentElement.clientHeight-obj.offsetHeight)
				temp_top = document.documentElement.clientHeight-obj.offsetHeight;
			obj.style.top=temp_top+'px';
		}else{
			//window.status="e.pageY="+e.pageY+" e.clientY="+e.clientY+"\r\n clientHeight="+document.documentElement.clientHeight +"\r\nscrollTop="+document.documentElement.scrollTop+"\r\n pos.bottom_y="+pos.bottom_y;
			var temp_bottom;
			if(!e.pageY){
				temp_bottom=((document.documentElement.clientHeight-e.clientY-document.documentElement.scrollTop))-parseInt(pos.bottom_y);
			}else{
				temp_bottom=((document.documentElement.clientHeight-e.pageY))-parseInt(pos.bottom_y);
			}
			if(temp_bottom < 0)
				temp_bottom = 0;
			//alert(temp_bottom+obj.offsetWidth);
			if(temp_bottom > document.documentElement.clientHeight - obj.offsetHeight)
				temp_bottom = document.documentElement.clientHeight - obj.offsetHeight;
			obj.style.bottom=temp_bottom+'px';
		}
		if(pos.x_type=='left'){
			//window.status="e.clientY="+e.clientY+"\r\n scrollTop="+document.documentElement.scrollTop +"\r\n pos.top_y="+pos.top_y;
			var temp_left = (e.pageX||(e.clientX+document.documentElement.scrollLeft))-parseInt(pos.left_x);
			if(temp_left < 0)
				temp_left = 0;
			if(temp_left > document.documentElement.clientWidth-obj.offsetWidth)
				temp_left=document.documentElement.clientWidth-obj.offsetWidth;
			obj.style.left=temp_left+'px';
		}else{
			var temp_right;
			if(!e.pageX){
				temp_right=((document.documentElement.clientWidth-e.clientX-document.documentElement.scrollLeft))-parseInt(pos.right_x);
			}else{
				temp_right=((document.documentElement.clientWidth-e.pageX))-parseInt(pos.right_x);
			}
			if(temp_right<0)
				temp_right=0;
			if(temp_right>document.documentElement.clientWidth - obj.offsetWidth)
				temp_right = document.documentElement.clientWidth - obj.offsetWidth;
			obj.style.right=temp_right+'px';
		}
		//如果原来不是绝对定位修改成绝对定位
		if(obj.style.position!="absolute"){
			obj.style.position="absolute";
		}
	}
}