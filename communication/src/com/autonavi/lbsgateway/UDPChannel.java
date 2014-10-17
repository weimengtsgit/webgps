package com.autonavi.lbsgateway;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class UDPChannel {
	private DatagramChannel datagramchannel;
	
	public UDPChannel(DatagramChannel datagramchannel  ){
		this.datagramchannel= datagramchannel;
	}

	public DatagramChannel getDatagramchannel() {
		return datagramchannel;
	}
	
	public synchronized byte[] readBytesFromChannel(){
		byte[]ret =null;
		try{
			DatagramChannel dataChannel = this.getDatagramchannel();
			ByteBuffer bf=ByteBuffer.allocate(1024);
	        bf.clear();
	        dataChannel.receive(bf);	           
	        bf.flip();	
	        ret= new byte[bf.limit()];	            
	        bf.get(ret, 0, bf.limit());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			return ret;
		}
	}
	
	public synchronized void writeBytesToChannel(byte[]bs){
		DatagramChannel dataChannel = this.getDatagramchannel();
		ByteBuffer buf = ByteBuffer.allocate(bs.length);
		buf.put(bs);
		
		try {
			dataChannel.send(buf, dataChannel.socket().getLocalSocketAddress());
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	
}