/*********   ���Ų�ѯ����   *********/
/**
 * ����ʱ����
 * @type Number
 */
var filtertimeparam = 0;
/**
 * ��ѯ�ն�id
 * @type 
 */
var trackSearchDeviceid = '';
var trackSearchDevicelocateType = '';
var trackSearchDevicesimcard = '';
var trackSearchDevicevehicleNumber = '';
var trackSearchStartTime = '';
var trackSearchEndTime = '';
var trackSearchWeek = '127';

/*********   ���ſ��Ʋ���   *********/
/**
 * ���ſ��ƶ���,�������ƶ�ʱ����
 * @type 
 */
var playinterval;
/**
 * �����ٶ�(��)
 * @type Number
 */
var refreshTime=1;
/**
 * ���Ž���ֵ
 * @type Number
 */
var sliderposition=0;
/**
 * ���Ž���ֵ�Ƿ񳬳��켣�����ֵ
 * @type Number
 */
var sliderpositionflag= false;

/**
 * ���Ž������ܳ���
 * @type Number
 */
var sliderlen=0;
/**
 * ��ͼ��Ұ����,1:����;2:������Ұ
 * @type Number
 */
var mapViewModal = false;
/**
 * ��ǰ״̬,��һ֡:0,����:1,��ͣ:2,ֹͣ:3,���һ֡:4
 * @type Number
 */
var currentstate = 0;
/**
 * ��һ�λ������ͼ���λ��
 * @type Number
 */
var prevPointPosition = 1;
/******   �켣�طŲ�������   ******/
/**
 * ���
 * @type 
 */
var trackQidian;
/**
 * �յ�
 * @type 
 */
var trackZhongdian;
/**
 * ȫ�Թ켣��
 * @type 
 */
var trackAllPoint = new Array();
/**
 * ȫ�Լ�ͷ
 */
var arrowAllPoint = new Array();
/**
 * ȫ�Թ켣��
 * @type 
 */
var trackAllLine;
/**
 * �ɲ����켣��,�����켣����ʱ����
 */
var trackPointArr = new Array();
/**
 * ��ǰ���Ŷ���
 * @type 
 */
var trackCurrent;
/**
 * map����mapObj
 * @type 
 */
var trackmap;

/******   �켣�طŵ�ͼ��   ******/
var globalPointImage = path+'/track/images/track/redpoint.gif';
var bluePointImage = path+'/track/images/track/bluepoint.png';
var currentPointImage = path+'/track/images/track/2.gif';
var prevPointImage = path+'/track/images/track/2.gif';
var qidianImage = path+'/track/images/track/qidian.png';
var zhongdianImage = path+'/track/images/track/zhongdian.png';

//add by liuhongxiao 2012-02-07
var filterCarStop;
//���(��)
var filterCarSpacing;
//���(����)
var filterCarInterval;

