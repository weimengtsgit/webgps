package com.autonavi.directl.parse;
/**
 * CELLID解析实现类
 * <p>Title: GPS网关</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: www.mapabc.com</p>
 *
 * @author musicjiang@sohu.com
 * @version 1.0
 */
public class ParseCELLID extends ParseBase{
  public ParseCELLID() {
  }

  public void parseGPRS(String phnum, String content) {
  }

  public void parseGPRS(String phnum, byte[] content) {
  }

  public void parseGPRS(String hexString) {
    String[] coords=hexString.split(",");
    this.setPhnum( coords[0] );
     this.setTime( coords[1] );
    this.setCoordX( coords[2] );
    this.setCoordY(coords[3]);
    this.sentPost(true);
  }

  public void parseGPRS(byte[] hexString) {
  }

  public void parseSMS(String phnum, String content) {
  }
}
