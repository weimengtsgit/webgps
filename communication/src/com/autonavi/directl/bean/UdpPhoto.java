package com.autonavi.directl.bean;

public class UdpPhoto {
	private String PhotoNum;//ͼƬ���
	private int PhotoPackets;//ͼƬ�ܰ���
	private int CurrentPhotoPacket;//��ǰ����
	private int DataSize;//���ݳ���
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
