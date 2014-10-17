
GroupHeaderPlugin 为grid的header合并组件，封装了Ext.Grid


一、引入JS，css文件

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/ui/GroupHeaderPlugin.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui/GroupHeaderPlugin.js"></script>


二、前台页面调用

Ext.onReady(function() {
 new Ext.Viewport({
  layout: 'fit',
  items: [{
   xtype: 'grid',
   title: 'GroupHeaderPlugin Example',
   store: new Ext.data.SimpleStore({
    fields: ['id', 'nr1', 'text1', 'info1', 'special1', 'nr2', 'text2', 'info2', 'special2', 'special3', 'changed'],
    data: [
     ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11']
    ]
   }),
   colModel: new Ext.grid.ColumnModel({
    columns: [
     {header: 'Id', width: 25, dataIndex: 'id'},
     {header: 'Nr', width: 25, dataIndex: 'nr1'},
     {header: 'Text', width: 50, dataIndex: 'text1'},
     {header: 'Info', width: 50, dataIndex: 'info1'},
     {header: 'Special', width: 60, dataIndex: 'special1'},
     {header: 'Nr', width: 25, dataIndex: 'nr2'},
     {header: 'Text', width: 50, dataIndex: 'text2'},
     {header: 'Info', width: 50, dataIndex: 'info2'},
     {header: 'Special', width: 60, dataIndex: 'special2'},
     {header: 'Special', width: 60, dataIndex: 'special3'},
     {header: 'Changed', width: 50, dataIndex: 'changed'}
    ],
    defaultSortable: true,
    rows: [
     [
      {rowspan: 2},
      {header: 'Before', colspan: 4, align: 'center'},
      {header: 'After', colspan: 4, align: 'center'},
      {header: 'Sum', colspan: 2, align: 'center', rowspan: 2}
     ], [
      {},
      {header: 'Merchandise', colspan: 3, align: 'center'},
      {},
      {header: 'Merchandise', colspan: 3, align: 'center'}
     ]
    ]
   }),
   enableColumnMove: false,
   viewConfig: {
    forceFit: true
   },
   plugins: [new Ext.ux.plugins.GroupHeaderGrid()]
  }]
 });
});