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
    Log.getInstance().outLog("企业用户(" + cTread.csc.getLoginUser().getUrname()+","+ cTread.csc.getLoginUser().getIP()+")已经进入");
    Log.getInstance().outLog("共"+instance.size()+"个企业用户登入系统");
  }
  public void removeClient(LoginUser loginUser){
    instance.remove(loginUser.getIP());
    Log.getInstance().outLog("企业用户(" + loginUser.getUrname()+","+ loginUser.getIP()+ ")已经退出");
    Log.getInstance().outLog("共"+instance.size()+"个企业用户登入系统");
  }
}
