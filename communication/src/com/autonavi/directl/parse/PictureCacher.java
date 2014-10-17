package com.autonavi.directl.parse;

import java.io.*;
import java.sql.*;

import com.autonavi.directl.*;

/**
 * <p>Title: </p>
 *
 * <p>Description:
 * ǰ��������
 * 1��ÿ���ն�ͬʱֻ��һ������ͷ��ͼƬ�����ϴ�
 * 2���ϴ�ʱ���հ���˳������ϴ�������ϴ�δ�ɹ������ϴ�
 * 3��ͼ�����ݴ洢�ڷ���ı�����ʱ�ļ��У�ͼ��Ļ�����Ϣ�ڱ����л���
 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Beijing Mapabc Co., Ltd.</p>
 *
 * @author �ն˲�Ʒ�� ��ɽ��Ŀ��
 * @version 1.0
 */
public class PictureCacher {
    /** ���ش洢���ļ��� */
    private String localFileName;

    /** ͼƬ���ֽ��� */
    private int byteLength;

    /** �ϴ��ְ��� */
    private int packageCount;

    /** �Ѿ��ϴ��İ��� */
    private int curPackageNum = -1;

    /** ��ǰ�Ѿ��ϴ��İ��� */
    private int curPackageCount = 0;

    /** ��ǰ�Ѿ�������ֽ��� */
    private int curByteSize = 0;
    /**�ϴ�����ʱ��λ��*/
    private float x = 0.0f;
    private float y = 0.0f;
    /**�ϴ�ʱ��*/
    private Timestamp picTime = null;
    /**�����ֻ���SIM��*/
    private String simCard = null;

    private boolean transOver;
    private byte[] bytes;


    /**
     * Ĭ�Ϲ��캯��
     */
    public PictureCacher() {

    }

    /**
     * ���캯��
     * @param localFileName String
     * @param byteLength int
     * @param packageCount int
     */
    public void init(String localFileName, int byteLength, int packageCount) {
        this.localFileName = localFileName;
        this.byteLength = byteLength;
        this.packageCount = packageCount;

        //ɾ��ԭ�е�ͬ���ļ�
        java.io.File file = new java.io.File(localFileName);
        if (file.isFile()) {
            file.delete();
        }

        curByteSize = 0;
        curPackageCount = 0;
        curPackageNum = -1;
    }

    /**
     * �����л�������
     * @param curPackageNum int ���ݵİ���
     * @param pics byte[] �����ֽ�����
     * @param length int  �����е���Чλ��
     * @return int 0����洢�ɹ���1����洢����2�����������, 3���������߼��������
     */
    public synchronized int pushBytes(int curPackageNum, byte[] pics,
                                      int length) {
        //��������
        if (curPackageNum < 0 || pics == null) {
            return 2;
        }
        

        //�����ݴ洢����ʱ�ļ���
        java.io.OutputStream os = null;
        try {
            java.io.File file = new java.io.File(localFileName);
            os = new java.io.FileOutputStream(file, true);
            os.write(pics, 0, length);

            //���������Ϣ
            curByteSize += length;
            curPackageCount++;
            this.curPackageNum = curPackageNum;
        } catch (FileNotFoundException ex1) {
            String msg = "ͼ�񻺴�ʱδ�ҵ������ļ�'" + localFileName + "'��" +
                         ex1.getMessage();
            Log.getInstance().errorLog(msg, ex1);
        } catch (IOException ee) {
            String msg = "��ͼ�񻺴��ļ�'" + localFileName + "'�������ʱ����" +
                         ee.getMessage();
            Log.getInstance().errorLog(msg, ee);
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException ex) {
                }
            }
        }

        return 0;
    }

    public InputStream getImg() {
        //�����ݴ洢����ʱ�ļ���
        java.io.InputStream is = null;
        java.io.File file = null;
        try {
            file = new java.io.File(localFileName);
            is = new java.io.FileInputStream(file);
            // clean();//�����ʱ�ļ�
        } catch (Exception e) {
            Log.getInstance().errorLog("��ȡͼƬ�쳣��" + e.getMessage(), e);
        }
        return is;
    }


    /**
     *  ������Ϣ�Ƿ����ϴ����
     * @return boolean
     */
    public boolean isTransOver() {
        if (curByteSize == this.byteLength && curPackageCount == packageCount) {
            return true;
        }
        return false;
    }

    /**
     * �ڱ�Ҫ��ʱ����ջ�����ļ�
     * @return boolean
     */
    public boolean clean() {
        boolean bRet = false;
        //ɾ��ԭ�е�ͬ���ļ�
        java.io.File file = new java.io.File(localFileName);
        if (file.isFile()) {
            bRet = file.delete();
        }
        return bRet;
    }

    /**
     * ���ͼ�����ݣ��ӻ����ļ��ж�ȡ����
     * @return byte[]
     */
    public byte[] getBytes() {
        byte[] pics = new byte[byteLength];
        byte[] picTemp = new byte[2048];
        //�����ݴ洢����ʱ�ļ���
        java.io.InputStream is = null;
        try {
            java.io.File file = new java.io.File(localFileName);
            is = new java.io.FileInputStream(file);
            int pos = 0;
            int readSize = is.read(picTemp);
            while (readSize != -1) {
                System.arraycopy(picTemp, 0, pics, pos, readSize);
                pos += readSize;
                readSize = is.read(picTemp);
            }
        } catch (FileNotFoundException ex1) {
            String msg = "ͼ�񻺴�ʱδ�ҵ������ļ�'" + localFileName + "'��" +
                         ex1.getMessage();
            Log.getInstance().errorLog(msg, ex1);
        } catch (IOException ee) {
            String msg = "��ͼ�񻺴��ļ�'" + localFileName + "'�������ʱ����" +
                         ee.getMessage();
            Log.getInstance().errorLog(msg, ee);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                }
            }
        }

        return pics;
    }


    public static void main(String[] args) {

        try {
            String srcName = "c:/src.bin";
            String outName = "c:/out.bin";
            int packageSize = 1024;
            java.io.File srcFile = new java.io.File(srcName);
            java.io.InputStream is = new FileInputStream(srcFile);
            int fileSize = is.available();
            int packageNum = fileSize / packageSize + 1; //�򵥵��ж�
            PictureCacher picturecacher = new PictureCacher();
            picturecacher.init(outName, fileSize, packageNum);
            byte[] bytes = new byte[1024];
            int readByte = is.read(bytes);
            int curPackageNum = 0;
            while (readByte != -1) {
                int iRet = picturecacher.pushBytes(curPackageNum, bytes,
                        readByte);
                if (iRet == 0) {
                    curPackageNum++;
                }
                readByte = is.read(bytes);
            }

            if (picturecacher.isTransOver()) {
                Log.getInstance().outLog("trans over");
            }

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public String getLocalFileName() {
        return localFileName;
    }

    public int getCurByteSize() {
        return curByteSize;
    }

    public int getCurPackageCount() {
        return curPackageCount;
    }

    public int getCurPackageNum() {
        return curPackageNum;
    }

    public int getPackageCount() {
        return packageCount;
    }

    public int getByteLength() {
        return byteLength;
    }
 
    public synchronized float getX() {
		return x;
	}

	public synchronized void setX(float x) {
		this.x = x;
	}

	public synchronized float getY() {
		return y;
	}

	public synchronized void setY(float y) {
		this.y = y;
	}

	public Timestamp getPicTime() {
        return picTime;
    }

    public String getSimCard() {
        return simCard;
    }

    public void setCurByteSize(int curByteSize) {
        this.curByteSize = curByteSize;
    }

    public void setCurPackageCount(int curPackageCount) {
        this.curPackageCount = curPackageCount;
    }

    public void setCurPackageNum(int curPackageNum) {
        this.curPackageNum = curPackageNum;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }

    public void setPackageCount(int packageCount) {
        this.packageCount = packageCount;
    }

    public void setByteLength(int byteLength) {
        this.byteLength = byteLength;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    

    public void setPicTime(Timestamp picTime) {
        this.picTime = picTime;
    }

    public void setSimCard(String simCard) {
        this.simCard = simCard;
    }
}
