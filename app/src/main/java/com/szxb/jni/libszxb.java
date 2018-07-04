/**  
 * Project Name:Q6  
 * File Name:libtest.java  
 * Package Name:com.szxb.jni  
 * Date:Apr 13, 20177:37:45 PM  
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.  
 *  
 */

package com.szxb.jni;


import android.content.res.AssetManager;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * ClassName:libtest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Apr 13, 2017 7:37:45 PM <br/>
 * 
 * @author lilei
 * @version
 * @since JDK 1.6
 * @see
 */
public class libszxb {

	static {
		try {
			System.loadLibrary("szxb");
		} catch (Throwable e) {
			Log.e("jni", "i can't find business so!");
			e.printStackTrace();
		}
	}
	
	static {
		try {
			System.loadLibrary("ymodem");
		} catch (Throwable e) {
			Log.e("jni", "i can't find ymodem so!");
			e.printStackTrace();
		}
	}
	
	
	static {
		try {
			System.loadLibrary("halcrypto");
		} catch (Throwable e) {
			Log.e("jni", "i can't find halcrypto so!");
			e.printStackTrace();
		}
	}
	
	
	//公交车接口
	
	public native static void deviceReset();
	public static native int deviceTime(byte[] buf, boolean flag);
	public static native int devicekey(byte[] recv);
	public static native int getBarcode(byte[] recv);
	public static native int setNixietube(byte[] nos);
	public static native int setLed(byte[] leds);
	public static native String getVersion();
	public static native String getCarInfo();
	public static native String doTrans(int value);
	public static native int deviceSerialSetBaudrate(int devNo,int Baudrate);
	public static native int deviceSerialSend(int devNo,byte[] sendBuf,int sendLen);
	public static native int deviceSerialRecv(int devNo,byte[] recvBuf,int timeOut);
	

	
	public static int deviceSettime(int year, int month, int date, int hour,
			int min, int sec) {

		byte[] settime = new byte[8];

		settime[0] = (byte) ((year>>8)&0xff);
		settime[1] = (byte) (year&0xff);
		settime[2] = (byte) (month&0xff);
		settime[3] = (byte) (date&0xff);
		settime[4] = (byte) (hour&0xff);
		settime[5] = (byte) (min&0xff);
		settime[6] = (byte) (sec&0xff);

		return libszxb.deviceTime(settime, true);
	}

	public static Calendar deviceGettime() {
		int ret;
		byte[] gettime = new byte[8];

		ret = libszxb.deviceTime(gettime, false);
		if (0 == ret) {
			int year = ((gettime[0]<<8) & 0xff00) | (gettime[1] & 0xff);
			
			return new GregorianCalendar(year, gettime[2], gettime[3],gettime[4],gettime[5],gettime[6]); 
		}
		return null;

	}

	//更新固件
	public static native int ymodemUpdate(AssetManager ass, String filename);


	
	//加密
	public static native String RSA_public_decrypt(String strN,String sInput,int e);
	public static native String Hash1(String inputStr);
	public static native String Hash224(String inputStr);
	public static native byte TripleDES(byte[] pszData, byte[] pszResult,byte[] pszKey, byte nFlag);
	public static native byte SingleDES(byte[] pszSrc, byte[]pszDst,byte[] pszDesKey, byte nFlag);
	
	
	
	
	
	//非接接口
	
	public native static int RFIDModuleOpen();

	public native static int RFIDMoudleClose();
	
	public native static int RFID_setAnt(int value);

	public native static String MifareGetSNR(byte[] cardType);
	public native static int qxcardprocess(byte[] money,byte[] rec);
	public native static String TypeA_RATS();

	public native static String[] RFID_APDU(String sendApdu);

	public native static int RFIDAuthenCard(byte nBlock, byte keyType,byte[] key);

	public static native int RFIDReadCard(byte nBlock, byte[] buf);

	public static native int RFIDWriteCard(byte nBlock, byte[] buf);

	public static native int RFIDInitValue(byte nBlock, int nMoney);

	public static native int RFIDInctValue(byte nBlock, int nMoney);

	public static native int RFIDDectValue(byte nBlock, int nMoney);

	public static native int RFIDRestor(byte nSrcBlock, byte nDesBlock);
	
	
	
	//psam卡接口
	public static native int OpenPsamMoudle();
	public static native int ClosePsamModule();
	public static native String psamCardReset(int baud,int slot);
	public static native String[] psamCardSendAPDUT0(int slot ,String sendApdu);

	
	//接触卡接口
//	public static native int ICcardOpen();
//	public static native int ICcardClose();
//	public static native String CpuCardPowerOn(byte slot);
//	public static native int CpuCardPowerOff(byte slot);
//	public static native String[] CpuCardSendAPDU(byte slot,String sendData);
	
	
	//磁条接口
	
//    public native static int msr_open();
//  
//    public native static int msr_close();
//   
//    public native static int msr_poll(int nTimeout_MS);
//    
//    public native static int msr_get_track_error(int nTrackIndex);
//   
//    public native static int msr_get_track_data_length(int nTrackIndex);
//    
//    public native static int msr_get_track_data(int nTrackIndex, byte byteArry[], int nLength);
	

}
