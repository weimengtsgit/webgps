package com.autonavi.directl.idirectl;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:终端控制抽象类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: www.mapabc.com
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public abstract class TerminalControl extends BaseDictate {
	public TerminalControl() {
	}

	/**
	 * 重新启动
	 */
	public abstract String reset(String seq);

	/**
	 * 关闭
	 */
	public abstract String turnOff(String seq,String v);

	/**
	 * 开关油门 state:0代表断开油门,1代表打开油门
	 */
	public abstract String setOilState(String seq,String state);

	/**
	 * 开关电流 state:0代表断开电流,1代表打开电流
	 */
	public abstract String setEletricityState(String seq,String state);

	/**
	 * 遥控熄火 type:0立即熄火，1到一定速度熄火 state:0熄火，1取消熄火
	 */
	public abstract String setFlameout(String seq,String type, String state, String speed);

	/**
	 * 通话设定 type:0通话模式，1监听模式 state:0允许外拨，1禁止外拨
	 */
	public abstract String setCallSetting(String seq,String type, String state);

	/**
	 * 密码开锁 pwd :密码
	 */
	public abstract String setOpenWithPwd(String seq,String pwd);

	/**
	 * 锁定车门 state:0代表锁定车门,1代表打开锁定车门
	 */
	public abstract String setDoorState(String seq,String state);

	/**
	 * 设置监听
	 */
	public abstract String setListening(String seq,String state, String tel);

	/**
	 * 控制终端工作状态
	 */
	public abstract String setWorkState(String seq,String v);

	/**
	 * 发布公共信息
	 */
	public abstract String sentPubMsg(String seq,String msg);

	/**
	 * 短信抢答
	 * 
	 * @param v：是否是范围抢答
	 * @param xy
	 * @param XY
	 * @param msg
	 * @return
	 */
	public abstract String quickRevertShortMessage(String seq,String ptype, EarthCoord xy,
			EarthCoord XY, String msg);

	/**
	 * 电话抢答
	 * 
	 * @param v
	 * @param xy
	 * @param XY
	 * @param tel
	 * @param msg
	 * @return
	 */
	public abstract String quickRevertCalling(String seq,String ptype, EarthCoord xy,
			EarthCoord XY, String tel, String msg);

	/**
	 * 短信调度
	 */
	public abstract String shortMessageAttemper(String seq,String ptype, EarthCoord xy,
			EarthCoord XY, String msg);

	/**
	 * 电话调度
	 */
	public abstract String telphoneAttemper(String seq,String ptype, EarthCoord xy,
			EarthCoord XY, String tel, String msg);

	/**
	 * 数字回传
	 */
	public abstract String replyNum(String seq,String ptype, EarthCoord xy, EarthCoord XY,
			String msg);

	/**
	 * 要求终端进入GPRS模式
	 */
	public abstract String gointoGPRS(String seq,String v, String pTime);

	/**
	 * 某范围点名
	 */
	public abstract String rangeCalling(String seq,EarthCoord xy, EarthCoord XY);

	/**
	 * 设置定时进入省电模式
	 */
	public abstract String startSaveModeByTime(String seq,String time);

	/**
	 * 终端回拨
	 * 
	 * @param tel
	 * @return
	 */
	public abstract String callBack(String seq,String tel);

	/**
	 * 给终端附件发送信息
	 * 
	 * @param affix:终端附件代码,如：显示平代码为04
	 * @param protocol：协议
	 * @param msg：发送的信息
	 * @return
	 */
	public abstract String sentAffixMsg(String seq,String affix, String protocol,
			String msg);

	/**
	 * 测试连接开关
	 * 
	 * @param tel
	 * @return
	 */
	public abstract String testConnectSwitch(String seq,String testState);

	/**
	 * 文字调度信息 msg：文字信息
	 * 
	 * @param tel
	 * @return
	 */
	public abstract String SendMessage(String seq,String msg);

	/**
	 * 下发短信息。
	 * 
	 * @param type
	 *            String --- 信息类型
	 * @param msg
	 *            String --- 信息
	 * @return String
	 */
	public abstract String sendMessageByType(String seq,String type, String msg);

	/**
	 * 取消求助信息 yl：1取消医疗，0无效，jt：1取消交通求助 0无效 dh：1取消导航，0 无效
	 * 
	 * @param tel
	 * @return
	 */
	public abstract String CancelHelp(String seq,String yl, String jt, String dh);

	/**
	 * 自检 type 自检类型 1：自检 2监听状态设置 3 语音调度状态设置 4 强行打开主电 5 gps断电复位 6 gps热启动 7
	 * gps温启动1 8 gps温启动2 9 gps冷启动 10 gps工厂复位 11 gps电源关闭 12 gps电源打开
	 * 
	 * @param tel
	 * @return
	 */
	public abstract String SelfCheck(String seq,String type);

	/**
	 * 路径下发
	 * 
	 * @param points
	 *            String
	 * @return String
	 */
	public abstract String sendRoute(String seq,String points);

	/**
	 * 目的地下发
	 * 
	 * @param point
	 *            String
	 * @return String
	 */
	public abstract String sendDest(String seq,String point);

	/**
	 * 回复信息
	 * 
	 * @return String
	 */
	public abstract String restoreInfor(String seq,String simcard);

	/**
	 * 普通命令（信息）格式
	 * 
	 * @param content
	 *            String
	 * @return String
	 */
	public abstract String commonCmdFormat(String seq,String content);

	/**
	 * 叫车消息 需要司机回复的消息，可以按确认或取消。
	 * 
	 * @param content
	 *            String
	 * @return String
	 */
	public abstract String callCarInfor(String seq,String content);

	/**
	 * 密码恢复
	 * 
	 * @param centerNum
	 *            String
	 * @return String
	 */
	public abstract String pwdResume(String seq,String centerNum);

	/**
	 * 添加信息 （发送显示屏信息）
	 * 
	 * @param content
	 *            String
	 * @return String
	 */
	public abstract String sentMsgAloneToLED(String seq,String content);

	/**
	 * 批量添加/修改信息 （单次添加信息的变体)
	 * 
	 * @param content
	 *            String
	 * @return String
	 */
	public abstract String batchSentMsgToLED(String seq,String content);

	/**
	 * 删除信息
	 * 
	 * @param inforNums
	 *            String --- 信息序号字符串
	 * @return String
	 */
	public abstract String deleteInfor(String seq,String inforNums);

	/**
	 * 清空显示屏内容
	 * 
	 * @return String
	 */
	public abstract String clearLEDContent();

	/**
	 * 外界数据发送（服务器--->终端）
	 * 
	 * @param content
	 *            String
	 */
	public abstract String sendExternalDate(String seq,String content);

	/**
	 * 终端拍照功能（参数是按中交科信的协议）
	 * 
	 * @param size
	 *            照片大小
	 * @param action
	 *            动作
	 * @param no
	 *            相机序号
	 * @return
	 */
	public abstract String takePictures(String seq,String size, String action, String no);
	/**
	 * 提取终端图片
	 * @param seq：序列号
	 * @param trackNo：终端拍照通道号
	 * @param picId：图片ID
	 * @return
	 */
	public abstract String getPicture(String seq, String trackNo, String picId);

	/**
	 * 断油断电 （上海永太协议）
	 * 
	 * @param state --
	 *            状态
	 * @param param --
	 *            参数
	 * @return
	 */
	public String stopOilAndElec(String seq,String state, String param) {
		return "";
	}

	// 设置油锁控制（星网锐捷）
	public abstract String setOilAndLock(String seq,String state, String param);

	// 设置摄像监控,单路（星网锐捷）
	public abstract String setCameraControl(String seq,String type, String action,
			String times, String interval, String zhiliang, String liangdu,
			String duibdu, String baohd, String huidu);
	
	public abstract String camera(String seq);

}
