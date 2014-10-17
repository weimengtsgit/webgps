package com.autonavi.directl.bean;

public class UdpPhoto {
	private String PhotoNum;//图片编号
	private int PhotoPackets;//图片总包数
	private int CurrentPhotoPacket;//当前包号
	private int DataSize;//数据长度
	private byte[] Data;
	private String deviceId;
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getPhotoNum() {
		return PhotoNum;
	}
	public void setPhotoNum(String photoNum) {
		PhotoNum = photoNum;
	}
	public int getPhotoPackets() {
		return PhotoPackets;
	}
	public void setPhotoPackets(int photoPackets) {
		PhotoPackets = photoPackets;
	}
	public int getCurrentPhotoPacket() {
		return CurrentPhotoPacket;
	}
	public void setCurrentPhotoPacket(int currentPhotoPacket) {
		CurrentPhotoPacket = currentPhotoPacket;
	}
	public int getDataSize() {
		return DataSize;
	}
	public void setDataSize(int dataSize) {
		DataSize = dataSize;
	}
	public byte[] getData() {
		return Data;
	}
	public void setData(byte[] data) {
		Data = data;
	}
	
}
