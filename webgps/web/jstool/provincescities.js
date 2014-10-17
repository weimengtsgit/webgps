var provinces = 
[[1,'����'],[2,'�Ϻ�'],[3,'�����'],[4,'�ӱ�ʡ'],[5,'ɽ��ʡ'],[6,'���ɹ�������']
,[7,'����ʡ'],[8,'����ʡ'],[9,'������ʡ'],[10,'����ʡ'],[11,'�㽭ʡ'],[12,'����ʡ']
,[13,'����ʡ'],[14,'����ʡ'],[15,'ɽ��ʡ'],[16,'����ʡ'],[17,'����ʡ'],[18,'����ʡ']
,[19,'�㶫ʡ'],[20,'����������'],[21,'����ʡ'],[22,'������'],[23,'�Ĵ�ʡ'],[24,'����ʡ']
,[25,'����ʡ'],[26,'����������'],[27,'����ʡ'],[28,'����ʡ'],[29,'�ຣʡ'],[30,'����������']
,[31,'�½�������'],[32,'����ر�������'],[33,'�����ر�������'],[34,'̨��ʡ']];


var cities = new Array();
cities[1] = [[12,'����'],[13,'����'],[14,'����'],[15,'����'],[16,'����'],[17,'����'],[18,'��̨'],[19,'ͨ��'],[110,'ʯ��ɽ'],[111,'��ƽ'],[112,'��ɽ'],[113,'����'],[114,'����'],[115,'��ͷ��'],[116,'����'],[117,'ƽ��'],[118,'˳��'],[119,'�ོ'],[120,'����'],[11,'����']];    
cities[2] = [[21,'��ɽ'],[22,'����'],[23,'բ��'],[24,'����'],[25,'����'],[26,'���'],[27,'����'],[28,'�ζ�'],[29,'��ɽ'],[210,'����'],[211,'¬��'],[212,'����'],[213,'�ֶ�'],[214,'����'],[215,'����'],[216,'�ɽ�'],[217,'���'],[218,'����'],[219,'����']];
cities[3] = [[31,'����'],[32,'����'],[33,'����'],[34,'��������'],[35,'����'],[36,'�ӱ�'],[37,'�Ӷ�'],[38,'��ƽ'],[39,'����'],[310,'����'],[311,'����'],[312,'����'],[313,'����'],[314,'����'],[315,'�Ͽ�'],[316,'����'],[317,'����']];
//�ӱ�ʡ
cities[4] = [[41,'ʯ��ׯ'],[42,'�żҿ���'],[43,'�е���'],[44,'�ػʵ���'],[45,'��ɽ��'],[46,'�ȷ���'],[47,'������'],[48,'������'],[49,'��ˮ��'],[410,'��̨��'],[411,'������']];
//ɽ��ʡ
cities[5] = [[51,'̫ԭ��'],[52,'��ͬ��'],[53,'˷����'],[54,'��Ȫ��'],[55,'������'],[56,'������'],[57,'������'],[58,'������'],[59,'������'],[510,'�ٷ���'],[511,'�˳���']];
//���ɹ�������
cities[6] = [[61,'���ͺ���'],[62,'��ͷ��'],[63,'�ں���'],[64,'�����'],[65,'���ױ�����'],[66,'�˰���'],[67,'����ľ��'],[68,'���ֹ�����'],[69,'�����첼��'],[610,'������˹��'],[611,'�����׶���'],[612,'��������']];
//����ʡ
cities[7] = [[71,'������'],[72,'������'],[73,'������'],[74,'������'],[75,'��˳��'],[76,'��Ϫ��'],[77,'������'],[78,'��ɽ��'],[79,'������'],[710,'������'],[711,'Ӫ����'],[712,'�̽���'],[713,'������'],[714,'��«����']];
//����ʡ
cities[8] = [[81,'������'],[812,'�׳���'],[813,'��ԭ��'],[814,'������'],[815,'��ƽ��'],[816,'��Դ��'],[817,'ͨ����'],[818,'��ɽ��'],[819,'�ӱ߳�����������']];
//������ʡ[91,'�Ϻ�']
cities[9] = [[92,'��������'], [93,'���������'], [94,'�ں���'],   [95,'������'],  [96,'������'], [97,'�׸���'], [98,'��ľ˹��'], [99,'˫Ѽɽ��'], [910,'��̨����'], [911,'������'], [912,'ĵ������'], [913,'�绯��'], [914,'���˰�']];
//����ʡ[101,'�Ϻ�']
cities[10] = [[101,'�Ͼ���'], [102,'������'],   [103,'���Ƹ�'],   [104,'��Ǩ��'], [105,'������'], [106,'�γ���'], [107,'������'], [108,'̩����'], [109,'��ͨ��'], [1010,'����'], [1011,'������'], [1012,'������'], [1013,'������']];
//�㽭ʡ[111,'�Ϻ�']
cities[11] = [[111,'������'], [112,'������'],   [113,'������'],   [114,'��ɽ��'], [115,'������'], [116,'������'], [117,'����'], [118,'̨����'], [119,'������'], [1110,'��ˮ��']];
//����ʡ[121,'�Ϻ�'],
cities[12] = [[121,'�Ϸ���'], [122,'������'],   [123,'������'],   [124,'������'], [125,'������'], [126,'������'], [127,'������'], [128,'����ɽ��'], [129,'�ߺ���'], [1210,'ͭ����'], [1211,'������'], [1212,'��ɽ��'], [1213,'������'], [1214,'������'], [1215,'������'], [1216,'������']];
//����ʡ[131,'�Ϻ�']
cities[13] = [[131,'������'], [132,'��ƽ��'],   [133,'������'],   [134,'������'], [135,'Ȫ����'],   [136,'������'],   [137,'������'], [138,'������'], [139,'������']];
//����ʡ[141,'������']                                                                        
cities[14] = [[141,'�ϲ���'], [142,'�Ž���'],   [143,'��������'], [144,'ӥ̶��'], [145,'������'],   [146,'Ƽ����'],   [147,'������'], [148,'������'], [149,'������'], [1410,'�˴���'], [1411,'������']];
       
//ɽ��ʡ[151,'�ϲ���']                                                                          
cities[15] = [[151,'������'], [152,'�ĳ���'],   [153,'������'],   [154,'��Ӫ��'], [155,'�Ͳ���'],   [156,'Ϋ����'],   [157,'��̨��'], [158,'������'], [159,'�ൺ��'], [1510,'������'], [1511,'������'], [1512,'��ׯ��'], [1513,'������'], [154,'̩����'], [155,'������'], [156,'������'], [157,'������']];
//����ʡ[151,'������']                                                                                                                                      
cities[16] = [[151,'֣����'], [152,'����Ͽ��'], [153,'������'],   [154,'������'], [155,'������'],   [156,'�ױ���'],   [157,'������'], [158,'�����'], [159,'������'], [1510,'������'], [1511,'������'], [1512,'�����'], [1513,'ƽ��ɽ��'], [154,'������'], [155,'������'], [156,'ʡֱϽ������λ'], [157,'�ܿ���'], [158,'פ������']];
//����ʡ[171,'֣����']                                                                                                                                
cities[17] = [[171,'�人��'], [172,'ʮ����'],   [173,'������'],   [174,'������'], [175,'Т����'],   [176,'�Ƹ���'],   [177,'������'], [178,'��ʯ��'], [179,'������'], [1710,'������'], [1711,'�˲���'], [1712,'ʡֱϽ������λ'], [1713,'��ʩ����������������'], [1714,'�差��']];
//����ʡ[181,'�Ϻ�']                                                                                                                                               
cities[18] = [[181,'��ɳ��'], [182,'�żҽ���'], [183,'������'],   [184,'������'], [185,'������'],   [186,'������'],   [187,'��̶��'], [188,'������'], [189,'������'], [1810,'������'], [1811,'������'], [1812,'������'], [1813,'¦����'], [1814,'��������������������']];
//�㶫ʡ[191,'�Ϻ�']                                                                                                                                              
cities[19] = [[191,'������'], [192,'��Զ��'],   [193,'�ع���'],   [194,'��Դ��'], [195,'÷����'],   [196,'������'],   [197,'��ͷ��'], [198,'������'], [199,'��β��'], [1910,'������'], [1911,'��ݸ��'], [1912,'������'], [1913,'�麣��'], [1914,'������'], [1915,'��ɽ��'], [1916,'������'], [1917,'�Ƹ���'], [1918,'������'], [1919,'ï����'], [1920,'տ����']];
//����������                                                                                 
cities[20] = [[201,'������'], [202,'������'],   [203,'������'],   [204,'������'], [205,'�����'],   [206,'������'],   [207,'������'], [208,'������'], [209,'���Ǹ���'], [2010,'������'], [2011,'��ɫ��'], [2012,'�ӳ���'], [2013,'������'], [2014,'������']];
//����ʡ[211,'�Ϻ�']                                                                           
cities[21] = [[211,'������'], [212,'������'],   [213,'ʡֱϽ��']];
//������
cities[22] = [['����','����'],['��������','��������'],['����','����'],['��ɿ�','��ɿ�'],['������','������'],['����','����'],['������','������'],['�ϰ�','�ϰ�'],['ɳƺ��','ɳƺ��'],['�山','�山'],['����','����'],['����','����']];                  
//�Ĵ�ʡ[231,'�Ϻ�']                             
cities[23] = [[231,'�ɶ���'], [232,'��Ԫ��'],   [233,'������'], [234,'������'],   [235,'�ϳ���'],   [236,'�㰲��'],   [237,'������'], [238,'�ڽ���'], [239,'��ɽ��'], [2310,'�Թ���'], [2311,'������'], [2312,'�˱���'], [2313,'��֦����'], [2314,'������'], [2315,'�ﴨ��'], [2316,'������'], [2317,'üɽ��'], [2318,'�Ű���'], [2319,'���Ӳ���Ǽ��������'], [2320,'���β���������'], [2321,'��ɽ����������']];
//����ʡ[241,'�Ϻ�']                                            
cities[24] = [[241,'������'], [242,'����ˮ��'], [243,'������'], [244,'�Ͻ���'], [245,'ͭ����'], [246,'��˳��'], [247,'ǭ�������嶱��������'], [248,'ǭ�ϲ���������������'], [249,'ǭ���ϲ���������������']];
//����ʡ[251,'�Ϻ�']                                            
cities[25] = [[251,'������'], [252,'������'],   [253,'��Ϫ��'], [254,'������'], [255,'��ͨ��'], [256,'˼é��'], [257,'�ٲ���'], [258,'��ɽ��'], [259,'�º���徰����������'], [2510,'ŭ��������������'], [2511,'�������������'], [2512,'��������������'], [2513,'��������������'], [2514,'��ӹ�����������'], [2515,'��ɽ׳��������'], [2516,'��˫���ɴ���������']];
//����������[261,'�Ϻ�']                                        
cities[26] = [[261,'������'], [262,'������'],[263,'������'],[264,'��֥��'], [265,'ɽ����'], [266,'�տ���'],   [267,'������']];
//����ʡ[271,'�Ϻ�']                                      
cities[27] = [[271,'������'], [272,'�Ӱ���'],   [273,'ͭ����'], [274,'μ����'],   [275,'������'],   [276,'������'],   [277,'������'], [278,'������'], [279,'������'], [2710,'������']];
//����ʡ[281,'�Ϻ�']                                   
cities[28] = [[281,'������'], [282,'��������'], [283,'�����'], [284,'������'],   [285,'��ˮ��'],   [286,'��Ȫ��'], [287,'��Ҵ��'], [288,'������'], [289,'������'], [2810,'ƽ����'], [2811,'������'], [2812,'¤����'], [2813,'���Ļ���������'], [2814,'���ϲ���������']];
//�ຣʡ[291,'�Ϻ�']                                
cities[29] = [[291,'������'], [291,'������'], [291,'������'], [294,'��������'], [295,'���ϲ���'], [296,'���ϲ���'], [297,'�������'], [298,'��������'], [299,'�����ɹ�']];
//����������[301,'�Ϻ�']                               
cities[30] = [[301,'������'], [302,'ʯ��ɽ��'], [303,'������'], [304,'��ԭ��']];
//�½�������[311,'�Ϻ�']
cities[31] = [[311,'��³ľ����'], [312,'����������'], [313,'������ֱϽ������λ'], [314,'��ʲ��'], [315,'��������'], [316,'������'], [317,'��³����'], [318,'������'], [319,'�������տ¶�����'], [3110,'���������ɹ�������'], [3111,'��������������'], [3112,'���������ɹ�������'], [3113,'���������������'], [3114,'������'], [3115,'������'], [3116,'����̩��']];
//����ر�������
cities[32] = [[321,'����ر�������']];
//�����ر�������
cities[33] = [[331,'�����ر�������']];
//̨��ʡ
cities[34] = [[341,'̨��ʡ']];



var comboProvinces = new Ext.form.ComboBox({   

           store: new Ext.data.SimpleStore(  {   
                   fields: ["provinceId", "provinceName"],   
                   data: provinces   
          }),   

           listeners:{   
                  select:function(combo, record,index){   
                         comboCities.clearValue();   
                         comboCities.store.loadData(cities[record.data.provinceId]);   
                 }   
          },   

          valueField :"provinceName",   
          displayField: "provinceName",   
          mode: 'local',   
          forceSelection: true,   
          blankText:'��ѡ��ʡ��',   
          emptyText:'��ѡ��ʡ��',   
          hiddenName:'provinceName',   
          editable: false,   
          triggerAction: 'all',   
          allowBlank:true,   
          fieldLabel: '��ѡ��ʡ��',   
          name: 'provinceName',   
          listWidth : 150,
          id : 'frmprovince',
          width: 150    

});   

var comboCities = new Ext.form.ComboBox({   
            store: new Ext.data.SimpleStore(  {   
                        fields: ["cityId",'cityName'],   
                       data:[]   
            }),   

            valueField :"cityName",   
            displayField: "cityName",   
            mode: 'local',     
            forceSelection: true,   
            blankText:'ѡ�����',   
            emptyText:'ѡ�����',   
            hiddenName:'cityName',   
            editable: false,   
            triggerAction: 'all',   
            allowBlank:true,   
            fieldLabel: 'ѡ�����',   
            name: 'cityName',   
            listWidth : 150,
            id : 'frmcity',
            width: 150
});