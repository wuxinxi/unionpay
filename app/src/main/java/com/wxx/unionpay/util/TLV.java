package com.wxx.unionpay.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TLV {
	
	private static final String TAG = "quickPass";
	// eg. tlv="9F36020B889F270180" return {9F27=80, 9F36=0B88}
	public static Map<String, String> decodingTLV(List<String[]> list) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			String[] tlv = (String[]) list.get(i);
			Log.i(TAG, "tag:" + tlv[0]  + "  value:" + tlv[2]);
			map.put(tlv[0], tlv[2]);
		}
		return map;
	}

	// eg. tlv="9F36020B889F270180" return {[9F36,02,0B88], [9F27,01,80]}
	public static List<String[]> decodingTLV(String str) {

		List<String[]> ls = new ArrayList<String[]>();

		if (str == null || str.length() % 2 != 0) {
			throw new RuntimeException("Invalid tlv, null or odd length");
		}

		for (int i = 0; i < str.length();) {
			try {

				String tag = str.substring(i, i = i + 2);

				// extra byte for TAG field
				if ((Integer.parseInt(tag, 16) & 0x1F) == 0x1F) {
					tag += str.substring(i, i = i + 2);
				}

				String len = str.substring(i, i = i + 2);
				int length = Integer.parseInt(len, 16);
				// more than 1 byte for length
				if (length > 128) {// 临界值，当是128即10000000时，长度还是一位，而不是两位
					int bytesLength = length - 128;
					len = str.substring(i, i = i + (bytesLength * 2));
					length = Integer.parseInt(len, 16);
				}
				length *= 2;

				String value = str.substring(i, i = i + length);
				
//				System.out.println("tag:" + tag + " len:" + len + " value:" + value);
				
				ls.add(new String[] { tag, len, value });
				
				String tagtmp = tag.substring(0,2);
				if ((Integer.parseInt(tagtmp, 16) & 0x20) == 0x20) 
				{
					ls.addAll(decodingTLV(value));
				}

			} catch (NumberFormatException e) {
				throw new RuntimeException("Error parsing number", e);
			} catch (IndexOutOfBoundsException e) {
				throw new RuntimeException("Error processing field", e);
			}
		}
		return ls;
	}
	
	
	public static List<String[]> decodingPDOL(String str) {
		List<String[]> ls = new ArrayList<String[]>();

		if (str == null || str.length() % 2 != 0) {
			throw new RuntimeException("Invalid tlv, null or odd length");
		}

		for (int i = 0; i < str.length();) {
			try {

				String tag = str.substring(i, i = i + 2);

				// extra byte for TAG field
				if ((Integer.parseInt(tag, 16) & 0x1F) == 0x1F) {
					tag += str.substring(i, i = i + 2);
				}

				String len = str.substring(i, i = i + 2);
				int length = Integer.parseInt(len, 16);
				// more than 1 byte for length
				if (length > 128) {// 临界值，当是128即10000000时，长度还是一位，而不是两位
					int bytesLength = length - 128;
					len = str.substring(i, i = i + (bytesLength * 2));
					length = Integer.parseInt(len, 16);
				}
				
				
//				System.out.println("tag:" + tag + " len:" + len );
				
				ls.add(new String[] { tag,"",len });
			
			} catch (NumberFormatException e) {
				throw new RuntimeException("Error parsing number", e);
			} catch (IndexOutOfBoundsException e) {
				throw new RuntimeException("Error processing field", e);
			}
		}
		return ls;
	}

	public static String encodingTLV(Map tlvMap) {
		String str = "";
		Iterator iter = tlvMap.entrySet().iterator();
		String tag = "";
		String length = "";
		String value = "";
		Entry entry;
		while (iter.hasNext()) {
			entry = (Entry) iter.next();
			tag = (String) entry.getKey();
			value = (String) entry.getValue();
			length = String.valueOf(Integer.parseInt(
					String.valueOf(value.length() / 2), 16));
			str += tag + length + value;
		}
		return str;
	}

	public static String encodingTLV(List tlvList) {
		String str = "";
		for (int i = 0; i < tlvList.size(); i++) {
			String[] tlv = (String[]) tlvList.get(i);
			str += tlv[0] + tlv[1] + tlv[2];
		}
		return str;
	}

}
