package com.autonavi.lbsgateway;
import java.util.Hashtable;
import com.autonavi.directl.Log;
public class ClientThreadList extends Hashtable  {
  static ClientThreadList instance=null;
  public int clientCount=0;
  public static ClientThreadList getInstance(){
    if(instance==null){
      instance=new ClientThreadList();
    }
    return instance;
  }
  public ClientThreadList() {
  }

  public void addClient(ClientThread cTread){

    instance.put(cTread.csc.getLoginUser().getIP(),cTread);
    Log.getInstance().outLog("��ҵ�û�(" + cTread.csc.getLoginUser().getUrname()+","+ cTread.csc.getLoginUser().getIP()+")�Ѿ�����");
    Log.getInstance().outLog("��"+instance.size()+"����ҵ�û�����ϵͳ");
  }
  public void removeClient(LoginUser loginUser){
    instance.remove(loginUser.getIP());
    Log.getInstance().outLog("��ҵ�û�(" + loginUser.getUrname()+","+ loginUser.getIP()+ ")�Ѿ��˳�");
    Log.getInstance().outLog("��"+instance.size()+"����ҵ�û�����ϵͳ");
  }
}
