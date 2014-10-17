/*----------------------------------------------------------------------------\
|                       Cross Browser Tree Widget 1.17                        |
|-----------------------------------------------------------------------------|
|                          Created by Emil A Eklund                           |
|                  (http://webfx.eae.net/contact.html#emil)                   |
|                      For WebFX (c/)                      |
|-----------------------------------------------------------------------------|
| An object based tree widget,  emulating the one found in microsoft windows, |
| with persistence using cookies. Works in IE 5+, Mozilla and konqueror 3.    |
|-----------------------------------------------------------------------------|
|                   Copyright (c) 1999 - 2002 Emil A Eklund                   |
|-----------------------------------------------------------------------------|
| This software is provided "as is", without warranty of any kind, express or |
| implied, including  but not limited  to the warranties of  merchantability, |
| fitness for a particular purpose and noninfringement. In no event shall the |
| authors or  copyright  holders be  liable for any claim,  damages or  other |
| liability, whether  in an  action of  contract, tort  or otherwise, arising |
| from,  out of  or in  connection with  the software or  the  use  or  other |
| dealings in the software.                                                   |
| - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - |
| This  software is  available under the  three different licenses  mentioned |
| below.  To use this software you must chose, and qualify, for one of those. |
| - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - |
| The WebFX Non-Commercial License          http://webfx.eae.net/license.html |
| Permits  anyone the right to use the  software in a  non-commercial context |
| free of charge.                                                             |
| - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - |
| The WebFX Commercial license           http://webfx.eae.net/commercial.html |
| Permits the  license holder the right to use  the software in a  commercial |
| context. Such license must be specifically obtained, however it's valid for |
| any number of  implementations of the licensed software.                    |
| - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - |
| GPL - The GNU General Public License    http://www.gnu.org/licenses/gpl.txt |
| Permits anyone the right to use and modify the software without limitations |
| as long as proper  credits are given  and the original  and modified source |
| code are included. Requires  that the final product, software derivate from |
| the original  source or any  software  utilizing a GPL  component, such  as |
| this, is also licensed under the GPL license.                               |
|-----------------------------------------------------------------------------|
| Dependencies: xtree.css (To set up the CSS of the tree classes)             |
|-----------------------------------------------------------------------------|
| 2001-01-10 | Original Version Posted.                                       |
| 2001-03-18 | Added getSelected and get/setBehavior  that can make it behave |
|            | more like windows explorer, check usage for more information.  |
| 2001-09-23 | Version 1.1 - New features included  keyboard  navigation (ie) |
|            | and the ability  to add and  remove nodes dynamically and some |
|            | other small tweaks and fixes.                                  |
| 2002-01-27 | Version 1.11 - Bug fixes and improved mozilla support.         |
| 2002-06-11 | Version 1.12 - Fixed a bug that prevented the indentation line |
|            | from  updating correctly  under some  circumstances.  This bug |
|            | happened when removing the last item in a subtree and items in |
|            | siblings to the remove subtree where not correctly updated.    |
| 2002-06-13 | Fixed a few minor bugs cased by the 1.12 bug-fix.              |
| 2002-08-20 | Added usePersistence flag to allow disable of cookies.         |
| 2002-10-23 | (1.14) Fixed a plus icon issue                                 |
| 2002-10-29 | (1.15) Last changes broke more than they fixed. This version   |
|            | is based on 1.13 and fixes the bugs 1.14 fixed withou breaking |
|            | lots of other things.                                          |
| 2003-02-15 | The  selected node can now be made visible even when  the tree |
|            | control  loses focus.  It uses a new class  declaration in the |
|            | css file '.webfx-tree-item a.selected-inactive', by default it |
|            | puts a light-gray rectangle around the selected node.          |
| 2003-03-16 | Adding target support after lots of lobbying...                |
|-----------------------------------------------------------------------------|
| Created 2000-12-11 | All changes are in the log above. | Updated 2003-03-16 |
\----------------------------------------------------------------------------*/

var webFXTreeConfig = {		
	rootIcon        : this.imagePath + '/foldericon.png',
	openRootIcon    : this.imagePath + '/openfoldericon.png',
	folderIcon      :  this.imagePath + '/foldericon.png',
	openFolderIcon  :this.imagePath + '/openfoldericon.png',
	fileIcon        : this.imagePath + '/file.png',
	iIcon           : this.imagePath + '/I.png',
	lIcon           : this.imagePath + '/L.png',
	lMinusIcon      : this.imagePath + '/Lminus.png',
	lPlusIcon       : this.imagePath + '/Lplus.png',
	tIcon           : this.imagePath + '/T.png',
	tMinusIcon   : this.imagePath + '/Tminus.png',
	tPlusIcon       : this.imagePath + '/Tplus.png',
	blankIcon       : this.imagePath + '/blank.png',
	defaultText     : 'Tree Item',
	defaultAction   : 'javascript:void(0);',
	defaultBehavior : 'classic', 
	usePersistence	: true,
    disableColor : "#9B9B9B",   // luohc [add] 2004-12-1. 不可用时的文本颜色。
	defaultColor : "black",   // luohc [add] 2006-4-30. 节点的文本颜色。
	cascadeCheck: true,    // luohc. 定义checkbox树是否级联选择。 2005.02.15
	cascadeCheckUp: false,    //sjw. 定义checkbox树是否级联向上选择。 2009.04.27
	//imagePath : "js/images",  // luohc. 图片路径.	2005.02.15  // 这个属性有点问题，请不要使用. 参见 setImagePath 方法.	
	dragFlag        : false, //是否支持拖拽
	setDragFlag: function(flag){
		this.dragFlag = flag;
	},
	setImagePath: function(path){  // 设置图片路径。直接设置 imagePath 属性不起作用。
		if(path == null) path = "js/images/";
		if(path.charAt(path.length-1) != '/') path += "/";
        //this.imagePath = path;
		this.rootIcon = path + "foldericon.png";
		this.openRootIcon = path + 'openfoldericon.png';
		this.folderIcon     =  path + 'foldericon.png';
		this.openFolderIcon  = path + 'openfoldericon.png';
		this.fileIcon       = path + 'file.png';
		this.iIcon           = path + 'I.png';
		this.lIcon           = path + 'L.png';
		this.lMinusIcon   =  path + 'Lminus.png';
		this.lPlusIcon     = path + 'Lplus.png';
		this.tIcon          = path + 'T.png';
		this.tMinusIcon  = path + 'Tminus.png';
		this.tPlusIcon     = path + 'Tplus.png';
		this.blankIcon     = path + 'blank.png';
	}	
	
};

var webFXTreeHandler = {
	idCounter : 0,
	id : 0,
	idPrefix  : "web-tree-sjw-",
	all       : {},
	behavior  : null,
	selected  : null,
	onSelect  : null, /* should be part of tree, not handler */
	getIdCounter     : function() { return this.idPrefix + "auto-" + this.idCounter++; },
	getId     : function() { return this.idPrefix + this.id; },
	toggle    : function (oItem) { this.all[oItem.id.replace('-plus','')].toggle(); },
	select    : function (oItem) { this.all[oItem.id.replace('-icon','')].select(); },
	focus     : function (oItem) { this.all[oItem.id.replace('-anchor','')].focus(); },
	blur      : function (oItem) { this.all[oItem.id.replace('-anchor','')].blur(); },
	keydown   : function (oItem, e) { return this.all[oItem.id].keydown(e.keyCode); },
	cookies   : new WebFXCookie(),
	insertHTMLBeforeEnd	:	function (oElement, sHTML) {
		if (oElement.insertAdjacentHTML != null) {
			oElement.insertAdjacentHTML("BeforeEnd", sHTML)
			return;
		}
		var df;	// DocumentFragment
		var r = oElement.ownerDocument.createRange();
		r.selectNodeContents(oElement);
		r.collapse(false);
		df = r.createContextualFragment(sHTML);
		oElement.appendChild(df);
	}
};

/*
 * WebFXCookie class
 */

function WebFXCookie() {
	this.cookieKey = "extreecookie";
	if (document.cookie.length) { 	        
		this.cookies = ' ' +  document.cookie; 
	}	
}

WebFXCookie.prototype.deleteCookie = function(){
	document.cookie = this.cookieKey + "=none";
	document.cookie = this.cookieKey + "=nothing; expires=Thu, 01-Jan-1970 00:00:01 GMT";
}

WebFXCookie.prototype.setCookie = function (key, value) {
	value = key + "=" + value + ";";
	this.cookies = ' ' +  document.cookie; 
	var values = this.getCookieValue(" " + this.cookieKey, this.cookies);
	if(values == null){		
		document.cookie = this.cookieKey + "=" + escape(value);
	}else{
		values = unescape(values);
		var start = values.indexOf(key + "=");
		if(start>=0){
			var end = values.indexOf(";", start);
			values = values.substring(0, start) + values.substring(end+1) + value;
		}else{
			values = values + value;
		}	
		document.cookie = this.cookieKey + "=" + escape(values);
	}
}

WebFXCookie.prototype.getCookie = function(key){
	this.cookies = ' ' +  document.cookie; 
	if(this.cookies == null) return null;
	var values = this.getCookieValue(" " + this.cookieKey, this.cookies);
    
	var value = null;
	if(values != null){
	   value = this.getCookieValue(key, unescape(values));
	}
	return value;
}

// 在 "key1=value1;key2=value2;" 这样的键值对字符串中获取某键值对应的值。
WebFXCookie.prototype.getCookieValue = function (key, values) {
	if(values == null) return null;
	var newKey = key + "=";
	var start = values.indexOf(newKey);
	if(start == -1) return null;
	start += newKey.length;
	var end = values.indexOf(";", start);
	if(end == -1) end = values.length;
	var value = values.substring(start, end); 
	return value;
}

/*
 * WebFXTreeAbstractNode class
 */

function WebFXTreeAbstractNode(sText, sAction, sId, sDesc, sExpend1, sExpend2, sExpend3, sExpend4, sExpend5, sExpend6) {
	this.childNodes  = [];
	if(sId!=null){
		webFXTreeHandler.id=sId;
		this.realId = sId;
		this.id     = webFXTreeHandler.getId();
	}else{
		this.id = webFXTreeHandler.getIdCounter();
		//this.realId = 0;
	}
	this.text   = sText || webFXTreeConfig.defaultText;
	if(sDesc!=null){
		this.desc = sDesc;
	}else{
		this.desc = "";
	}
	this.expend1  = sExpend1;
	this.expend2  = sExpend2;
	this.expend3  = sExpend3;
	this.expend4  = sExpend4;
	this.expend5  = sExpend5;
	this.expend6  = sExpend6;
	
	
	//this.action = sAction || webFXTreeConfig.defaultAction;
    
	if(sAction != null && sAction.length > 0){
		// luohc [add] 2004-12-07. 在new一个节点时可以省略javascript:前缀。
		var leftBracket = sAction.indexOf("(");   // 左括号.
		var rightBracket = sAction.indexOf(")");  // 右括号.
		var js = sAction.toLowerCase().indexOf("javascript:");  // 前缀 javascript: 。
		// 如果有左右括号(说明是方法)，但又没有javascript:前缀。
		if(leftBracket>0 && rightBracket>leftBracket && js!=0){
			sAction = "javascript:" + sAction;
		}
		this.action = sAction;
	}else{
		this.action = webFXTreeConfig.defaultAction;
	}
	this.isOndblclick = false;
	this._last  = false;
	webFXTreeHandler.all[this.id] = this;
}

/*
 * To speed thing up if you're adding multiple nodes at once (after load)
 * use the bNoIdent parameter to prevent automatic re-indentation and call
 * the obj.ident() method manually once all nodes has been added.
 */

WebFXTreeAbstractNode.prototype.add = function (node, bNoIdent) {
	node.parentNode = this;
	this.childNodes[this.childNodes.length] = node;
	var root = this;
	if (this.childNodes.length >= 2) {
		this.childNodes[this.childNodes.length - 2]._last = false;
	}
	while (root.parentNode) { root = root.parentNode; }
	if (root.rendered) {
		if (this.childNodes.length >= 2) {
			document.getElementById(this.childNodes[this.childNodes.length - 2].id + '-plus').src = ((this.childNodes[this.childNodes.length -2].folder)?((this.childNodes[this.childNodes.length -2].open)?webFXTreeConfig.tMinusIcon:webFXTreeConfig.tPlusIcon):webFXTreeConfig.tIcon);
			this.childNodes[this.childNodes.length - 2].plusIcon = webFXTreeConfig.tPlusIcon;
			this.childNodes[this.childNodes.length - 2].minusIcon = webFXTreeConfig.tMinusIcon;
			this.childNodes[this.childNodes.length - 2]._last = false;
		}
		this._last = true;
		var foo = this;
		while (foo.parentNode) {
			for (var i = 0; i < foo.parentNode.childNodes.length; i++) {
				if (foo.id == foo.parentNode.childNodes[i].id) { break; }
			}
			if (i == foo.parentNode.childNodes.length - 1) { foo.parentNode._last = true; }
			else { foo.parentNode._last = false; }
			foo = foo.parentNode;
		}
		webFXTreeHandler.insertHTMLBeforeEnd(document.getElementById(this.id + '-cont'), node.toString());
		if (!this.folder) {
			this.icon = webFXTreeConfig.folderIcon;
			this.openIcon = webFXTreeConfig.openFolderIcon;
		}
		if (!this.folder) { this.folder = true; this.collapse(true); }
		if (!bNoIdent) { this.indent(); }
	}
	return node;
}

WebFXTreeAbstractNode.prototype.toggle = function() {
	if (this.folder) {
		if (this.open) { this.collapse(); }
		else { this.expand(); }
}	}

WebFXTreeAbstractNode.prototype.select = function() {
	document.getElementById(this.id + '-anchor').focus();
}

WebFXTreeAbstractNode.prototype.deSelect = function() {
	document.getElementById(this.id + '-anchor').className = '';
	webFXTreeHandler.selected = null;
}

WebFXTreeAbstractNode.prototype.focus = function() {
	if ((webFXTreeHandler.selected) && (webFXTreeHandler.selected != this)) { webFXTreeHandler.selected.deSelect(); }
	webFXTreeHandler.selected = this;
	if ((this.openIcon) && (webFXTreeHandler.behavior != 'classic')) { document.getElementById(this.id + '-icon').src = this.openIcon; }

	document.getElementById(this.id + '-anchor').className = 'selected';
	try{document.getElementById(this.id + '-anchor').focus();}catch(e){}
	if (webFXTreeHandler.onSelect) { webFXTreeHandler.onSelect(this); }
}

WebFXTreeAbstractNode.prototype.blur = function() {
	if ((this.openIcon) && (webFXTreeHandler.behavior != 'classic')) { document.getElementById(this.id + '-icon').src = this.icon; }
	document.getElementById(this.id + '-anchor').className = 'selected-inactive';
}

WebFXTreeAbstractNode.prototype.doExpand = function() {
	_expand_node(this);		
}

//luohc add 2006-05-12.
function _expand_node(node){
	if (webFXTreeHandler.behavior == 'classic') { document.getElementById(node.id + '-icon').src = node.openIcon; }
	if (node.childNodes.length) {  document.getElementById(node.id + '-cont').style.display = 'block'; }
	node.open = true;
	if (webFXTreeConfig.usePersistence) {
		webFXTreeHandler.cookies.setCookie(node.id.substr(18,node.id.length - 18), '1');
    }
}

WebFXTreeAbstractNode.prototype.doCollapse = function() {
	if (webFXTreeHandler.behavior == 'classic') { document.getElementById(this.id + '-icon').src = this.icon; }
	if (this.childNodes.length) { document.getElementById(this.id + '-cont').style.display = 'none'; }
	this.open = false;
	if (webFXTreeConfig.usePersistence) {
		webFXTreeHandler.cookies.setCookie(this.id.substr(18,this.id.length - 18), '0');
}	}

WebFXTreeAbstractNode.prototype.expandAll = function() {
	this.expandChildren();
	if ((this.folder) && (!this.open)) { this.expand(); }
}

WebFXTreeAbstractNode.prototype.expandChildren = function() {
	for (var i = 0; i < this.childNodes.length; i++) {
		this.childNodes[i].expandAll();
} }
//展开第一层
WebFXTreeAbstractNode.prototype.expandFirstChildren = function() {
 		if(this.childNodes.length){
 			this.childNodes[0].expand();
 		}
  }

WebFXTreeAbstractNode.prototype.collapseAll = function() {
	this.collapseChildren();
	if ((this.folder) && (this.open)) { this.collapse(true); }
}

WebFXTreeAbstractNode.prototype.collapseChildren = function() {
	for (var i = 0; i < this.childNodes.length; i++) {
		this.childNodes[i].collapseAll();
} }

WebFXTreeAbstractNode.prototype.indent = function(lvl, del, last, level, nodesLeft) {
	/*
	 * Since we only want to modify items one level below ourself,
	 * and since the rightmost indentation position is occupied by
	 * the plus icon we set this to -2
	 */
	if (lvl == null) { lvl = -2; }
	var state = 0;
	for (var i = this.childNodes.length - 1; i >= 0 ; i--) {
		state = this.childNodes[i].indent(lvl + 1, del, last, level);
		if (state) { return; }
	}
	if (del) {
		if ((level >= this._level) && (document.getElementById(this.id + '-plus'))) {
			if (this.folder) {
				document.getElementById(this.id + '-plus').src = (this.open)?webFXTreeConfig.lMinusIcon:webFXTreeConfig.lPlusIcon;
				this.plusIcon = webFXTreeConfig.lPlusIcon;
				this.minusIcon = webFXTreeConfig.lMinusIcon;
			}
			else if (nodesLeft) { document.getElementById(this.id + '-plus').src = webFXTreeConfig.lIcon; }
			return 1;
	}	}
	var foo = document.getElementById(this.id + '-indent-' + lvl);
	if (foo) {
		if ((foo._last) || ((del) && (last))) { foo.src =  webFXTreeConfig.blankIcon; }
		else { foo.src =  webFXTreeConfig.iIcon; }
	}
	return 0;
}

// 获得节点的id。luohc 2005-11-30.
WebFXTreeAbstractNode.prototype.getId = function () {
	if(this.oid){
	    return this.oid;
	}else{
		return this.id;
	}
}

// 根据Id获得节点对象。 luohc 2005-11-30.
function getNodeById(id){
	return webFXTreeHandler.all[id];
}

// 获得节点的颜色。luohc 2005-7-28.
WebFXTreeAbstractNode.prototype.getColor = function (color) {
	if(this.color){
		return this.color;		
	}else if(this._disabled){
		return webFXTreeConfig.disableColor;
	}else{
		return webFXTreeConfig.defaultColor;
	}
}

// 根据real Id获得节点对象。 sjw 2008-11-21.
function getNodeByRealId(id){
	return webFXTreeHandler.all[webFXTreeHandler.idPrefix+id];
}

// 设置节点的id。luohc 2005-11-30.
WebFXTreeAbstractNode.prototype.setId = function (id) {
	this.oid = id;
	webFXTreeHandler.all[id] = this;
}

// 设置节点的名称。luohc 2005-7-28.
WebFXTreeAbstractNode.prototype.setText = function (text) {
	var id = this.id + "-anchor";
	var anchor = document.getElementById(id);
	if(anchor != null){
		anchor.innerHTML = text;
	}
	this.text=text;
}

// 设置节点的颜色。luohc 2005-7-28.
WebFXTreeAbstractNode.prototype.setColor = function (color) {
	var id = this.id + "-anchor";
	var anchor = document.getElementById(id);
	if(anchor != null){
		anchor.style.color = color;
	}
	this.color = color;
}
// 设置叶子节点图片的颜色。sjw 2009-3-27.
WebFXTreeAbstractNode.prototype.setIconColor = function (color) {
	if(this.folder){
		return;
	}
	var id = this.id + "-icon";
	var icon = document.getElementById(id);
	if(icon != null){
		var i = this.icon.lastIndexOf(".");
		if(color == null || color == ""){
			icon.src=this.icon;
		}else{
			icon.src=this.icon.substring(0,i)+"_"+color+this.icon.substring(i);
		}
	}
}
// 设置节点的描述。sjw 2008-11-21.
WebFXTreeAbstractNode.prototype.setDesc = function (desc, displayType) {
	var parent = this.parentNode;
	while(parent){
		document.getElementById(parent.id+"-cont").style.whiteSpace="normal";
		document.getElementById(parent.id).style.whiteSpace="normal";
		parent = parent.parentNode;
	}
	document.getElementById(this.id).style.whiteSpace="normal";
	var id = this.id + "-desc";
	var descDiv = document.getElementById(id);
	if(descDiv != null){
		descDiv.innerHTML = desc;
		if(desc!=null&&desc!=""){
			if(displayType==null || displayType==""){
				displayType = "block";
			}
			descDiv.style.display=displayType;
			document.getElementById(this.id).style.whiteSpace="nowrap";
		}else{
			descDiv.style.display="none";
			document.getElementById(this.id).style.whiteSpace="nowrap";
			parent = this.parentNode;
			while(parent){
				document.getElementById(parent.id+"-cont").style.whiteSpace="normal";
				document.getElementById(parent.id).style.whiteSpace="normal";
				parent = parent.parentNode;
			}
		}
	}
	this.desc=desc;
}

//递归设置子树所有叶子颜色 sjw 2008-09-16
function setTreeColor(node,color){
	for (var i = 0; i < node.childNodes.length; i++) {
		//alert(node.childNodes[i].getAction());
		if(node.childNodes[i].getAction()!=null && node.childNodes[i].getAction()!="" && node.childNodes[i].getAction()!="javascript:void(0);"){
			//改变颜色
			node.childNodes[i].setColor(color);
		}
		//递归
		setTreeColor(node.childNodes[i],color);
	}
}
WebFXTreeAbstractNode.prototype.setFolder = function (isFolder){
	this.folder=isFolder;
	if (!this.folder) {
		this.icon = webFXTreeConfig.folderIcon;
		this.openIcon = webFXTreeConfig.openFolderIcon;
	}
}
//设置action是否是双击事件
WebFXTreeAbstractNode.prototype.setOndblclick = function (isOndblclick){
	this.isOndblclick = isOndblclick;
}
//设置action
WebFXTreeAbstractNode.prototype.setAction = function (sAction){
	if(sAction != null && sAction.length > 0){
		// luohc [add] 2004-12-07. 在new一个节点时可以省略javascript:前缀。
		var leftBracket = sAction.indexOf("(");   // 左括号.
		var rightBracket = sAction.indexOf(")");  // 右括号.
		var js = sAction.toLowerCase().indexOf("javascript:");  // 前缀 javascript: 。
		// 如果有左右括号(说明是方法)，但又没有javascript:前缀。
		if(leftBracket>0 && rightBracket>leftBracket && js!=0){
			sAction = "javascript:" + sAction;
		}
		this.action = sAction;
	}else{
		this.action = webFXTreeConfig.defaultAction;
	}
}
//设置根节点类型为checkbox、radio、普通
WebFXTreeAbstractNode.prototype.setRootType = function (type, value){
	if(type!=null){
		if(type=="checkbox"){//checkbox
			this.isCheckBox = true;
			this.isRadio = false;
		}else if(type=="radio"){//radio
			this.isCheckBox = false;
			this.isRadio = true;
		}else{//普通
			this.isCheckBox = false;
			this.isRadio = false;
		}
	}
	this.value = value;
	this._checked = false;
}
// luohc.
function getTreeRoot(){
   return tree_root;
}

/*
 * WebFXTree class
 */
var tree_root; // luohc
function WebFXTree(sText, sAction, sBehavior, sIcon, sOpenIcon, sId, sDesc, sExpend1, sExpend2, sExpend3) {
	this.base = WebFXTreeAbstractNode;
	this.base(sText, sAction, sId, sDesc, sExpend1, sExpend2, sExpend3);
	this.icon      =  webFXTreeConfig.rootIcon;
	this.openIcon  =  webFXTreeConfig.openRootIcon;
	if(sIcon){
		this.icon = sIcon;
		this.openIcon  = sIcon;
	}
	if(sOpenIcon){
		this.openIcon = sOpenIcon;
	}
	/* Defaults to open */
	if (webFXTreeConfig.usePersistence) {
		this.open  = (webFXTreeHandler.cookies.getCookie(this.id.substr(18,this.id.length - 18)) == '0')?false:true;
	} else { this.open  = true; }
	this.folder    = true;
	this.rendered  = false;
	this.onSelect  = null;
	if (!webFXTreeHandler.behavior) {  webFXTreeHandler.behavior = sBehavior || webFXTreeConfig.defaultBehavior; }

	tree_root = this; // luohc 2004-7-30.
}

WebFXTree.prototype = new WebFXTreeAbstractNode;

WebFXTree.prototype.setBehavior = function (sBehavior) {
	webFXTreeHandler.behavior =  sBehavior;
};

WebFXTree.prototype.getBehavior = function (sBehavior) {
	return webFXTreeHandler.behavior;
};

WebFXTree.prototype.getSelected = function() {
	if (webFXTreeHandler.selected) { return webFXTreeHandler.selected; }
	else { return null; }
}

WebFXTree.prototype.remove = function() { }

WebFXTree.prototype.expand = function() {
	this.doExpand();
}

WebFXTree.prototype.collapse = function(b) {
	if (!b) { this.focus(); }
	this.doCollapse();
}

WebFXTree.prototype.getFirst = function() {
	return null;
}

WebFXTree.prototype.getLast = function() {
	return null;
}

WebFXTree.prototype.getNextSibling = function() {
	return null;
}

WebFXTree.prototype.getPreviousSibling = function() {
	return null;
}

WebFXTree.prototype.keydown = function(key) {
	if (key == 39) {
		if (!this.open) { this.expand(); }
		else if (this.childNodes.length) { this.childNodes[0].select(); }
		return false;
	}
	if (key == 37) { this.collapse(); return false; }
	if ((key == 40) && (this.open) && (this.childNodes.length)) { this.childNodes[0].select(); return false; }
	return true;
}
//设置根节点选中事件
WebFXTree.prototype.setChecked = function (bChecked) {
	this._checked = bChecked;
	if(this.isCheckBox){//checkbox
		doCheck(this, bChecked);
	}else if(this.isRadio){//radio
	    if(bChecked != null && bChecked == true){
			_setCheckedObject(this);
	    }else{
    		_setCheckedObject(null);
		}
		setRadioChecked(this.value, bChecked);
	}
	if (typeof this.onchange == "function"){
		this.onchange(this.text, this.value, bChecked);
	}else if(this.onchange != null && this.onchange != ""){
		var str = this.onchange + "('" + this.text + "','" + this.value + "'," + bChecked + ");";
		eval(str);
	}
}
var searchFlag = false;//是否支持搜索
//设置是否支持搜索
WebFXTree.prototype.setSearchFlag = function (searchFlag) {
	this.searchFlag = searchFlag;
	if(document.getElementById("searchInputDiv")!=null){
		if(searchFlag){
			document.getElementById("searchInputDiv").style.display="block";
		}else{
			document.getElementById("searchInputDiv").style.display="none";
		}
	}
	// onpropertychange=\"findNode(this.value)\"
	//判断浏览器，支持firefox
	if(navigator.userAgent.indexOf("MSIE")>0){
		document.getElementById('searchDiv').attachEvent("onpropertychange",findNode);
	}else if(navigator.userAgent.indexOf("Firefox")>0){
		document.getElementById('searchDiv').addEventListener("input",findNode,false);
	}
}
var rightMenuFlag = false;//是否支持右键菜单
WebFXTree.prototype.setRightMenuFlag = function (rightMenuFlag) {
	this.rightMenuFlag = rightMenuFlag;
}
//添加右键菜单
WebFXTree.prototype.addRightMenuFlag = function (menuName, functionName) {
	var tempStr = "";
	tempStr += "<div id=\"rightMenuItem\"><a href=\"javascript:void(0);\" onclick=\"eval('"+functionName+"()')\">"+menuName+"</a></div>";
	document.getElementById("rightMenuDiv").innerHTML+=tempStr;
}
//右键事件
document.oncontextmenu=function(e){
	e = window.event;
	var target = e.target || e.srcElement;
	//alert(target.tagName +" "+ target.id);
	if(target.tagName.toUpperCase()!='A'){
		document.getElementById("rightMenuDiv").style.display="none";
		return true;
	}
	//alert(target.id);
	document.getElementById("rightMenuDiv").style.display="";
	document.getElementById("rightMenuDiv").style.pixelTop=event.clientY + document.body.scrollTop;
	document.getElementById("rightMenuDiv").style.pixelLeft=event.clientX + document.body.scrollLeft;
	return false;
}
document.onclick=function(){
	document.getElementById("rightMenuDiv").style.display="none";
}
WebFXTree.prototype.toString = function() {
	var tempStr = "";
	if(this.isCheckBox){
		tempStr = "<input type=\"checkbox\"" + " class=\"tree-check-box\"" +
		(this._checked ? " checked=\"checked\"" : "") +
		" onclick=\"webFXTreeHandler.all[this.parentNode.id].setChecked(this.checked)\"" +
		" value=\"" + this.value + "\" id=\"" + this.id + "-input\" name=\"" + this.value 
		+ "-input\" " + (this._disabled?" disabled ": "") + ">";
	}else if(this.isRadio){
		tempStr += "<input type=\"radio\"" + " class=\"tree-radio\"" +
		(this._checked ? " checked=\"checked\"" : "") +
		" onclick=\"webFXTreeHandler.all[this.parentNode.id].setChecked(this.checked)\"" +
		" value=\"" + this.value + "\" myId=\"" + this.id + "\" id=\"" + this.value 
		+ "-input\" " + (this._disabled?" disabled ": "") + " name=\"radioButton\">";
	}
	var inputHeight="20px";
	if(navigator.userAgent.indexOf("MSIE")>0){
		inputHeight="15px";
	}else if(navigator.userAgent.indexOf("Firefox")>0){
		inputHeight="15px";
	}
	var str = "<div id=\"rightMenuDiv\" style=\"position:absolute;display:none;cursor:default;\" onClick=\"return false;\"></div>"+//右键菜单div
		"<div id=\"searchInputDiv\" "+(this.searchFlag?"":"style=\"display:none\"")+"><input type=\"text\" id=\"searchDiv\" size=\"12\" style=\"height:"+inputHeight+"\"><input type=\"button\" value=\"确定\" onclick=\"endSearch()\" style=\"margin: 0px; padding: 0px; height: 22px;\"></div>"+//搜索输入框以及确定按钮div
		"<div id=\"searchResult\" style=\"display:none;z-index:10000;width:173px; height:368px;overflow:auto;\"></div>"+//搜索结果显示div
		"<div id=\"" + this.id + "\" class=\"webfx-tree-item\" onkeydown=\"return webFXTreeHandler.keydown(this, event)\">" +tempStr+
		"<img id=\"" + this.id + "-icon\" class=\"webfx-tree-icon\" src=\"" + ((webFXTreeHandler.behavior == 'classic' && this.open)?this.openIcon:this.icon) + "\" onclick=\"webFXTreeHandler.select(this);\">" +
		"<a href=\"javascript:void(0);\""+(this.isOndblclick?" ondblclick":"onclick")+"=\"" + this.action + "\" id=\"" + this.id + "-anchor\" onfocus=\"webFXTreeHandler.focus(this);\" onblur=\"webFXTreeHandler.blur(this);\"" +
		(this.target ? " target=\"" + this.target + "\"" : "") + " style='color:" + this.getColor() + "'" +
		">" + this.text + "</a></div>" +
		"<div id=\"" + this.id + "-cont\" class=\"webfx-tree-container\" style=\"display: " + ((this.open)?'block':'none') + ";\">";
	var sb = [];
	for (var i = 0; i < this.childNodes.length; i++) {
		sb[i] = this.childNodes[i].toString(i, this.childNodes.length);
	}
	this.rendered = true;
	return str + sb.join("") + "</div>";
};
//模糊查找非组节点名称中含有nodeText值的节点
function findNode(nodeText, theNode){
	nodeText=document.getElementById('searchDiv').value;
	if(theNode == undefined){
		theNode = null;
	}
	//alert(theNode!=null?theNode.text:"");
	var searchResultDiv = document.getElementById("searchResult");
	//过滤值为空则显示树
	if(nodeText==null || nodeText == ""){
		searchResultDiv.innerHTML="";
		searchResultDiv.style.display="none";
		if(theNode == null){
			theNode = tree_root;
		}
		document.getElementById(theNode.id).style.display="";
		document.getElementById(theNode.id+"-cont").style.display="";
		for (var i = 0; i < theNode.childNodes.length; i++) {
			var node = theNode.childNodes[i];
			document.getElementById(node.id).style.display="";
			document.getElementById(node.id+"-cont").style.display="";
			if(i == theNode.childNodes.length-1){
				//document.getElementById(node.id+"-desc").style.border="1px green solid";
				document.getElementById(node.id+"-cont").style.border="1px transparent solid";
			}
			if (node.folder) {
				if (node.open) {
					//node.expand();
				}else {
					node.collapse(true);
				}
			}
			if(node != null){
				findNode(nodeText, node);//递归
			}
		}
		if (theNode.folder) {
			if (theNode.open) {
				//theNode.expand();
			}else {
				theNode.collapse(true);
			}
		}
		return;
	}
	//隐藏树，显示节点名称包括过滤值的节点信息
	if(theNode == null){
		theNode = tree_root;
		searchResultDiv.innerHTML="";
		searchResultDiv.style.display="block";
	}
	document.getElementById(theNode.id).style.display="none";
	document.getElementById(theNode.id+"-cont").style.display="none";
	for (var i = 0; i < theNode.childNodes.length; i++) {
		var node = theNode.childNodes[i];
		document.getElementById(node.id).style.display="none";
		document.getElementById(node.id+"-cont").style.display="none";
		if(node == null){
			continue;
		}
		//节点非组、名称包含过滤值
		if (!node.folder && node.text!=null && node.text.indexOf(nodeText) != -1) {
			//alert(node.text);
			////redo
			//alert(typeof(node.getRadio) == "function");
			if(node.value==null){
				searchResultDiv.innerHTML+=""+node.text+"<br/>";
			}else if(typeof(node.getRadio) == "function"){//radio
				//alert(node.text+" WebFXCheckBoxTreeItem");
				var tempStr = "<input type=\"radio\"" + " class=\"tree-radio\"" +
					(node._checked ? " checked=\"checked\"" : "") +
					" onclick=\"getNodeById('"+node.id+"').setChecked(this.checked)\"" +
					" value=\"" + node.value + "\" myId=\"" + node.id + "\" " + (node._disabled?" disabled ": "") + " name=\"radioButton2\">";
				searchResultDiv.innerHTML+=tempStr+"<a href=\"javascript:void(0);\""+(node.isOndblclick?" ondblclick":"onclick")+"=\"" + node.action + "\" id=\"" + node.id + "-anchor2\">"+node.text+"</a><br/>";
			}else{//checkbox
				//alert(node.text+" WebFXRadioTreeItem");
				//alert(node.text+" "+node._checked);
				var tempStr = "<input type=\"checkbox\"" + " class=\"tree-check-box\"" +
					(node._checked ? " checked=\"checked\"" : "") +
					" onclick=\"getNodeById('"+node.id+"').setChecked(this.checked)\"" +
					" value=\"" + node.value + "\" name=\"" + node.value 
					+ "-input\" " + (node._disabled?" disabled ": "") + ">";

				searchResultDiv.innerHTML+=tempStr+"<a href=\"javascript:void(0);\""+(node.isOndblclick?" ondblclick":"onclick")+"=\"" + node.action + "\" id=\"" + node.id + "-anchor2\">"+node.text+"</a><br/>";
			}
			theNode.expand();
		}
		findNode(nodeText, node);//递归
	}
}
//停止查询
function endSearch(theNode){
	if(theNode == undefined && (document.getElementById("searchDiv").value==null||document.getElementById("searchDiv").value=="")){
		alert("请输入要搜索的节点名称");
		return;
	}
	if(theNode == undefined){
		theNode = null;
	}
	if(theNode == null){
		theNode = tree_root;
		var searchResultDiv = document.getElementById("searchResult");
		//var str = document.getElementById("searchDiv").onpropertychange + "('');";
		//eval(str);
		document.getElementById("searchDiv").value="";
		//searchResultDiv.innerHTML="";
		searchResultDiv.style.display="none";
	}
	document.getElementById(theNode.id).style.display="";
	document.getElementById(theNode.id+"-cont").style.display="";
	for (var i = 0; i < theNode.childNodes.length; i++) {
		var node = theNode.childNodes[i];
		document.getElementById(node.id).style.display="";
		document.getElementById(node.id+"-cont").style.display="";
		if (node.folder) {
			if (node.open) {
				//node.expand();
			}else {
				node.collapse(true);
			}
		}
		if(node != null){
			endSearch(node);//递归
		}
	}
	if (theNode.folder) {
		if (theNode.open) {
			//theNode.expand();
		}else {
			theNode.collapse(true);
		}
	}
}

/*
 * WebFXTreeItem class
 */

function WebFXTreeItem(sText, sAction, eParent, sIcon, sOpenIcon, sId, sDesc, sExpend1, sExpend2, sExpend3, sExpend4, sExpend5, sExpend6) {
	this.base = WebFXTreeAbstractNode;
	this.base(sText, sAction, sId, sDesc, sExpend1, sExpend2, sExpend3, sExpend4, sExpend5, sExpend6);
	/* Defaults to close */
	if (webFXTreeConfig.usePersistence) {
		this.open = (webFXTreeHandler.cookies.getCookie(this.id.substr(18,this.id.length - 18)) == '1')?true:false;
	} else { this.open = false; }

	if (sIcon) { 
		this.icon = sIcon; 
		this.openIcon = sIcon; 
	}
	if (sOpenIcon) { this.openIcon = sOpenIcon; }
	if (eParent) { eParent.add(this); }
}

WebFXTreeItem.prototype = new WebFXTreeAbstractNode;

WebFXTreeItem.prototype.remove = function() {
	var iconSrc = document.getElementById(this.id + '-plus').src;
	var parentNode = this.parentNode;
	var prevSibling = this.getPreviousSibling(true);
	var nextSibling = this.getNextSibling(true);
	var folder = this.parentNode.folder;
	var last = ((nextSibling) && (nextSibling.parentNode) && (nextSibling.parentNode.id == parentNode.id))?false:true;
	//this.getPreviousSibling().focus();  // luohc 2006-05-15 注释掉.
	this._remove();
	if (parentNode.childNodes.length == 0) {
		//iconSrc = document.getElementById(parentNode.id + '-plus').src;
		//iconSrc = iconSrc.replace('minus', '').replace('plus', '');
		//document.getElementById(parentNode.id + '-plus').src = iconSrc;
		//iconSrc = document.getElementById(parentNode.id + '-icon').src;
		//iconSrc = iconSrc.replace('openfoldericon','file');
		//this.parentNode.icon=webFXTreeConfig.fileIcon;
		//document.getElementById(parentNode.id + '-icon').src = iconSrc;
		document.getElementById(parentNode.id + '-cont').style.display = 'none';
		//parentNode.doCollapse();
		//parentNode.folder = false;
		//parentNode.open = false;
	}
	if (!nextSibling || last) { parentNode.indent(null, true, last, this._level, parentNode.childNodes.length); }
	if (!prevSibling && (prevSibling == parentNode) && !(parentNode.childNodes.length)) {
		prevSibling.folder = false;
		prevSibling.open = false;
		iconSrc = document.getElementById(prevSibling.id + '-plus').src;
		iconSrc = iconSrc.replace('minus', '').replace('plus', '');
		document.getElementById(prevSibling.id + '-plus').src = iconSrc;
		//document.getElementById(prevSibling.id + '-icon').src = webFXTreeConfig.fileIcon; // luohc.
	}
	if (prevSibling!=null && document.getElementById(prevSibling.id + '-plus')) {
		if (parentNode == prevSibling.parentNode) {
			iconSrc = iconSrc.replace('minus', '').replace('plus', '');
			document.getElementById(prevSibling.id + '-plus').src = iconSrc;
}	}	}

WebFXTreeItem.prototype._remove = function() {
	for (var i = this.childNodes.length - 1; i >= 0; i--) {
		this.childNodes[i]._remove();
 	}
	for (var i = 0; i < this.parentNode.childNodes.length; i++) {
		if (this == this.parentNode.childNodes[i]) {
			for (var j = i; j < this.parentNode.childNodes.length; j++) {
				this.parentNode.childNodes[j] = this.parentNode.childNodes[j+1];
			}
			this.parentNode.childNodes.length -= 1;
			if (i + 1 == this.parentNode.childNodes.length) { this.parentNode._last = true; }
			break;
	}	}
	webFXTreeHandler.all[this.id] = null;
	var tmp = document.getElementById(this.id);
	if (tmp) { tmp.parentNode.removeChild(tmp); }
	tmp = document.getElementById(this.id + '-cont');
	if (tmp) { tmp.parentNode.removeChild(tmp); }
}


WebFXTreeItem.prototype.expand = function() {
	// 增加展开父节点。 luohc 2006-05-12.	
	if(this.parentNode != null && this.parentNode.open == false) this.parentNode.expand();
    //if(this.childNodes.length){
		this.doExpand();
		document.getElementById(this.id + '-plus').src = this.minusIcon;
	//}
}

WebFXTreeItem.prototype.collapse = function(b) {
	if (!b) { this.focus(); }
	this.doCollapse();
	document.getElementById(this.id + '-plus').src = this.plusIcon;
}

WebFXTreeItem.prototype.getFirst = function() {
	if(this.childNodes.length){
		return this.childNodes[0];
	}else{
		return null;
	}
}

WebFXTreeItem.prototype.getLast = function() {
	if(this.childNodes.length==0) return null;
	if (this.childNodes[this.childNodes.length - 1].open) { return this.childNodes[this.childNodes.length - 1].getLast(); }
	else { return this.childNodes[this.childNodes.length - 1]; }
}

WebFXTreeItem.prototype.getNextSibling = function() {
	for (var i = 0; i < this.parentNode.childNodes.length; i++) {
		if (this == this.parentNode.childNodes[i]) { break; }
	}
	if (++i == this.parentNode.childNodes.length) { return this.parentNode.getNextSibling(); }
	else { return this.parentNode.childNodes[i]; }
}

WebFXTreeItem.prototype.getPreviousSibling = function(b) {
	for (var i = 0; i < this.parentNode.childNodes.length; i++) {
		if (this == this.parentNode.childNodes[i]) { break; }
	}
	if (i == 0) { return this.parentNode; }
	else {
		if ((this.parentNode.childNodes[--i].open) || (b && this.parentNode.childNodes[i].folder)) { return this.parentNode.childNodes[i].getLast(); }
		else { return this.parentNode.childNodes[i]; }
} }

WebFXTreeItem.prototype.keydown = function(key) {
	if ((key == 39) && (this.folder)) {
		if (!this.open) { this.expand(); }
		else { this.getFirst().select(); }
		return false;
	}
	else if (key == 37) {
		if (this.open) { this.collapse(); }
		else { this.parentNode.select(); }
		return false;
	}
	else if (key == 40) {
		if (this.open) { this.getFirst().select(); }
		else {
			var sib = this.getNextSibling();
			if (sib) { sib.select(); }
		}
		return false;
	}
	else if (key == 38) { this.getPreviousSibling().select(); return false; }
	return true;
}

WebFXTreeItem.prototype.toString = function (nItem, nItemCount) {
	var foo = this.parentNode;
	var indent = '';
	if (nItem + 1 == nItemCount) { this.parentNode._last = true; }
	var i = 0;
	while (foo.parentNode) {
		foo = foo.parentNode;
		indent = "<img id=\"" + this.id + "-indent-" + i + "\" src=\"" 
			+ ((foo._last)?webFXTreeConfig.blankIcon:webFXTreeConfig.iIcon) + "\">" + indent;
		i++;
	}
	this._level = i;
	if (this.childNodes.length) { this.folder = 1; }
	else { this.open = false; }
	if ((this.folder) || (webFXTreeHandler.behavior != 'classic')) {
		if (!this.icon) { this.icon = webFXTreeConfig.folderIcon; }
		if (!this.openIcon) { this.openIcon = webFXTreeConfig.openFolderIcon; }
	} else if (!this.icon) { this.icon = webFXTreeConfig.fileIcon; }

	if(!this.openIcon){ this.openIcon = webFXTreeConfig.fileIcon; }

	var label = this.text.replace(/</g, '&lt;').replace(/>/g, '&gt;');
	var str = "<div id=\"" + this.id + "\" class=\"webfx-tree-item\" onkeydown=\"return webFXTreeHandler.keydown(this, event)\">" +
		indent +
		"<img id=\"" + this.id + "-plus\" src=\"" + ((this.folder)?((this.open)?((this.parentNode._last)?webFXTreeConfig.lMinusIcon:webFXTreeConfig.tMinusIcon):((this.parentNode._last)?webFXTreeConfig.lPlusIcon:webFXTreeConfig.tPlusIcon)):((this.parentNode._last)?webFXTreeConfig.lIcon:webFXTreeConfig.tIcon)) + "\" onclick=\"webFXTreeHandler.toggle(this);\">" +
		"<img id=\"" + this.id + "-icon\" class=\"webfx-tree-icon\" src=\"" + ((webFXTreeHandler.behavior == 'classic' && this.open)?this.openIcon:this.icon) + "\" onclick=\"webFXTreeHandler.select(this);\">" +
		"<a href=\"javascript:void(0);\""+(this.isOndblclick?" ondblclick":"onclick")+"=\"" + this.action + "\" id=\"" + this.id + "-anchor\" onfocus=\"webFXTreeHandler.focus(this);\" onblur=\"webFXTreeHandler.blur(this);\"" +
		(this.target ? " target=\"" + this.target + "\"" : "") + " style='color:" + this.getColor() + "' onmousedown=\"eXtree_startDrag(this)\" onmousemove=\"eXtree_drag(this)\" onmouseup=\"eXtree_stopDrag(this)\"" +
		">" + label + "</a><div id=\"" + this.id + "-desc\" class=\"webfx-tree-item-desc\" style=\"display: " + ((this.desc!=null&&this.desc!="")?'block':'none') + "\">" + this.desc + "</div></div>" +
		"<div id=\"" + this.id + "-cont\" class=\"webfx-tree-container\" style=\"display: " + ((this.open)?'block':'none') + ";\">";
	var sb = [];
	for (var i = 0; i < this.childNodes.length; i++) {
		sb[i] = this.childNodes[i].toString(i,this.childNodes.length);
	}
	this.plusIcon = ((this.parentNode._last)?webFXTreeConfig.lPlusIcon:webFXTreeConfig.tPlusIcon);
	this.minusIcon = ((this.parentNode._last)?webFXTreeConfig.lMinusIcon:webFXTreeConfig.tMinusIcon);
	return str + sb.join("") + "</div>";
}

// 2006-05-12 新增节点移动方法. luohc.
WebFXTreeItem.prototype.move = function (pNode, index) {	
	/*
	var _children = [];
	if(index != null && index >=0 && index<pNode.childNodes.length){
		for(var i=index; i<pNode.childNodes.length; i++){
			var n = pNode.childNodes[i];
			n = _moveChildBeforeRemove(n);
			_children[_children.length] = n;
			n.remove();
		}		
	} */

    var node = _moveChildBeforeRemove(this);
	this.remove();
	_addChildTree(pNode, node);
    /*
	for(var i=0; i<_children.length; i++){
		_addChildTree(pNode, _children[i]);
	}*/
};

function _moveChildBeforeRemove(node){
	node.children = [];
	if(node.childNodes.length){
		for(var i=0; i<node.childNodes.length; i++){
			node.children[node.children.length] = node.childNodes[i];
			_moveChildBeforeRemove(node.childNodes[i]);
		}
	}
	return node;
}

function _addChildTree(pNode, node){
	webFXTreeHandler.all[node.id] = node;
	pNode.add(node);
	if(node.children){
		for(var i=0; i<node.children.length; i++){
			_addChildTree(node, node.children[i]);
		}
	}
}
//节点拖动
var eXtree_x0=0,eXtree_y0=0,eXtree_x1=0,eXtree_y1=0;
var eXtree_divNumber = 0;
var eXtree_offx=0,eXtree_offy=-150;
var eXtree_moveable=false;
var eXtree_hover='orange',eXtree_normal='white';//color;
var eXtree_index=10000;//z-index;
var draging_object = null;//正在拖拽的对象
//拖动
function eXtree_startDrag(obj){
	if(webFXTreeConfig.dragFlag){
		if(!eXtree_moveable && event.button==1){
			//锁定标题栏;
			obj.setCapture();
			//定义对象;
			var eXtree_win = obj.parentNode;
			var eXtree_sha = eXtree_win.nextSibling;
			//记录鼠标和层位置;
			eXtree_x0 = event.clientX;
			eXtree_y0 = event.clientY;
			eXtree_x1 = parseInt(eXtree_getX(eXtree_win));
			eXtree_y1 = parseInt(eXtree_getY(eXtree_win));
			//alert(x1+" "+y1);
			//记录颜色;
			eXtree_normal = obj.style.backgroundColor;
			//改变风格;
			obj.style.backgroundColor = eXtree_hover;
			eXtree_win.style.borderColor = eXtree_hover;
			eXtree_sha.style.backgroundColor = eXtree_hover;
			obj.nextSibling.style.color = eXtree_hover;
			eXtree_moveable = true;
			draging_object = obj;
		}else{
			if(draging_object==null){
				eXtree_moveable = false;
				return;
			}
			var eXtree_win = draging_object.parentNode;
			var eXtree_sha = eXtree_win.nextSibling;
			eXtree_win.style.position="";
			eXtree_win.style.borderColor    = eXtree_normal;
			draging_object.style.backgroundColor = eXtree_normal;
			if(eXtree_sha!=null){
				eXtree_sha.style.position="";
				eXtree_sha.style.borderColor    = eXtree_normal;
				eXtree_sha.style.backgroundColor = eXtree_normal;
			}
			draging_object.releaseCapture();
			eXtree_moveable = false;
			draging_object = null;
		}
	}
}

//拖动;
function eXtree_drag(obj){
	if(eXtree_moveable && event.button==1){
		var eXtree_win = obj.parentNode;
		var eXtree_sha = eXtree_win.nextSibling;
		eXtree_win.style.position="absolute";
		eXtree_win.style.filter = "alpha(opacity='90')";
		eXtree_sha.style.position="absolute";
		eXtree_sha.style.filter = "alpha(opacity='90')";
		//eXtree_win.style.border="solid 1px red";
		eXtree_win.style.left = event.clientX + document.body.scrollLeft + eXtree_offx;
		eXtree_win.style.top  = event.clientY + document.body.scrollTop + eXtree_offy;
		eXtree_sha.style.left = event.clientX + document.body.scrollLeft + eXtree_offx;
		eXtree_sha.style.top  = event.clientY + document.body.scrollTop + eXtree_win.offsetHeight + eXtree_offy;
	}
}

//停止拖动;
function eXtree_stopDrag(obj,e){
	if(eXtree_moveable && event.button==1){
		e = window.event;
		var target = e.target || e.srcElement;
		if(target==null){
		}else if(target.id!=null && target.id!= obj.id && obj.parentNode.id!=null && target.id!=obj.parentNode.id){
			if((target.tagName.toUpperCase()=='A'||target.tagName.toUpperCase()=='SPAN') && target.id!=null && target.id.indexOf("-anchor")!=-1){
				thisNode = getNodeById(obj.parentNode.id);
				theOldParentNode = getNodeById(obj.parentNode.id).parentNode;
				theParentNode = getNodeById(target.parentNode.id);
				if(theOldParentNode.id!=theParentNode.id){
					//拖拽至非组节点
					if(typeof(theParentNode.folder) == undefined || !theParentNode.folder){
						//alert("不能拖动至非组节点下！");
						
					}else{
						if(!confirm("确定将 \""+thisNode.text+"\" 移动到 \""+theParentNode.text+"\" 下吗？")){
						}else{
							////redo
							//判断非组节点不能拖拽到非组节点下，并且ajax处理后台同步成功后页面再更新
							//此处根据情况判断realId第一个字符为数字则为非组节点
							var url = null;
							//拖拽非组节点
							if(typeof(thisNode.folder) != undefined && !thisNode.folder){
								var deviceIds = thisNode.realId;
								var groupId = theParentNode.realId.substring(1);
								url = getAppPath()+"/manage/terminalManage.do?actionMethod=terminalInGroup&deviceIds="+deviceIds+"&groupId="+groupId;
							}
							//拖拽组节点
							else if(typeof(thisNode.folder) != undefined && thisNode.folder){
								var groupId = thisNode.realId.substring(1);
								var parentGroupId = theParentNode.realId.substring(1);
								url = getAppPath()+"/manage/termGroupManage.do?actionMethod=editGroupInGroup&groupId="+groupId+"&parentGroupId="+parentGroupId;
							}else{
								//alert(typeof(thisNode.folder));
							}
							if(url!=null){
								var xmlHttp = XmlHttp.create();
								xmlHttp.open("GET", url, true);	// async
								xmlHttp.onreadystatechange = function () {
									if (xmlHttp.readyState == 4) {
										//alert(xmlHttp.responseText);
										eXtree_parseResp(xmlHttp.responseXML, thisNode, theParentNode);
									}
								};
								// call in new thread to allow ui to update
								window.setTimeout(function () {
									//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
									//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
									xmlHttp.send(null);
								}, 10);
							}
							//thisNode.move(theParentNode);
							//theParentNode.expand();
						}
					}
				}
			}
		}else{
		}
		var eXtree_win = obj.parentNode;
		var eXtree_sha = eXtree_win.nextSibling;
		eXtree_win.style.position="";
		eXtree_win.style.borderColor    = eXtree_normal;
		obj.style.backgroundColor = eXtree_normal;
		if(eXtree_sha!=null){
			eXtree_sha.style.position="";
			eXtree_sha.style.borderColor    = eXtree_normal;
			eXtree_sha.style.backgroundColor = eXtree_normal;
		}
		obj.releaseCapture();
		eXtree_moveable = false;
		draging_object = null;
	}
}

function eXtree_getX(obj){  
	return obj.offsetLeft + (obj.offsetParent ? eXtree_getX(obj.offsetParent) : obj.x ? obj.x : 0);  
}          
function eXtree_getY(obj){
	return (obj.offsetParent ? obj.offsetTop + eXtree_getY(obj.offsetParent) : obj.y ? obj.y : 0);  
}

//解析拖拽返回
function eXtree_parseResp(oXmlDoc, thisNode, theParentNode){
	if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	if(oXmlDoc.getElementsByTagName("r")!=null && oXmlDoc.getElementsByTagName("r").length!=0){
		var r = oXmlDoc.getElementsByTagName("r")[0].firstChild.nodeValue;
		if(r != null && r == "true"){
			if( typeof theParentNode.loaded == "undefined" || theParentNode.loaded==null){//非异步加载树
				thisNode.move(theParentNode);
				theParentNode.expand();
			}else if (!theParentNode.loaded && !theParentNode.loading) {//未打开也未正在打开
				if(thisNode.folder){//是目录则移动
					thisNode.move(theParentNode);
				}else{//不是目录则删除，等打开目录自动加载此节点
					thisNode.remove();
				}
				theParentNode.expand();
			}else{// 已经打开了
				thisNode.move(theParentNode);
			}
		}else{
			alert("异常！");
		}
	}else{
		alert("返回格式错误！");
	}
}