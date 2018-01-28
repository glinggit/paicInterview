package com.paic.arch.utils;

public class ClockUtil {
	/**
	 * 
	 * @param lampColor 灯的颜色
	 * @param lampNum   灯的数量
	 * @return
	 */
	public static String get4TimeLamp(char lampColor, int lampNum) {
		char[] lampChar = getIniChar(4);
		for(int i=0; i<lampNum; i++) {
			lampChar[i] = lampColor;
		}
		return String.valueOf(lampChar);
	}

	/**
	 * 
	 * @param lampNum 灯的数量
	 * @return
	 */
	public static String get11TimeLamp(int lampNum) {
		char[] lampChar = getIniChar(11);
		for(int i=0; i<lampNum; i++) {
			if(((i+1)%3) == 0) {
				lampChar[i] = 'R';
			}else {
				lampChar[i] = 'Y';
			}
		}
		return String.valueOf(lampChar);
	}
	
	private static char[] getIniChar(int length) {
		char[] charArray = new char[length]; 
		for(int i=0; i<charArray.length; i++) {
			charArray[i] = 'O';
		}
		return charArray;
	}
}
