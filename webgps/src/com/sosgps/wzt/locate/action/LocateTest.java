package com.sosgps.wzt.locate.action;

import com.mapabc.geom.DPoint;

/**
 * @Title:
 * @Description:
 * @Company:
 * @author: weimeng
 * @version: 1.0
 * @date: 2013-2-17 ÏÂÎç5:13:39
 */
public class LocateTest {

    public static void main(String args[]) {
        com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
        String posDesc = "aaa";
        double xs[] = { 116.1234 };
        double ys[] = { 39.4567 };
        com.sos.sosgps.api.DPoint[] dps = null;
        try {
            dps = coordApizw.encryptConvert(xs, ys);
//            com.sos.sosgps.api.DPoint dps1 = coordApizw.decryptConvert(116.315, 39.9831);
//            System.out.println(dps1.x+","+dps1.y);
            posDesc = coordApizw.getAddress(coordApizw.encrypt(dps[0].x), coordApizw.encrypt(dps[0].y));
            System.out.println(dps[0].x+","+dps[0].y+";"+dps[0].encryptX+","+dps[0].encryptY+";"+posDesc);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}
