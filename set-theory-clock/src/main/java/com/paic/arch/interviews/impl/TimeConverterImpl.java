package com.paic.arch.interviews.impl;

import java.util.regex.Pattern;

import com.paic.arch.interviews.TimeConverter;
import com.paic.arch.utils.ClockUtil;

public class TimeConverterImpl implements TimeConverter{

	@Override
	public String convertTime(String aTime) {
		String pattern = "^(([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]))|(24:00:00)$";
		boolean isMatch = Pattern.matches(pattern, aTime);
		if(isMatch) {
			StringBuilder result = new StringBuilder();
			String[] timeArr = aTime.split(":");
			String enter = "\r\n";
			//判断秒的奇偶
			if((Integer.parseInt(timeArr[2]) & 1) == 1) {
				result.append("O" + enter);
			}else {
				result.append("Y" + enter);
			}
			//将小时解析到对应的灯
			result.append(ClockUtil.get4TimeLamp('R', Integer.parseInt(timeArr[0])/5) + enter);
			result.append(ClockUtil.get4TimeLamp('R', Integer.parseInt(timeArr[0])%5) + enter);
			//将分钟解析到对应的灯
			result.append(ClockUtil.get11TimeLamp(Integer.parseInt(timeArr[1])/5) + enter);
			result.append(ClockUtil.get4TimeLamp('Y', Integer.parseInt(timeArr[1])%5));
			
			return result.toString();
		}else {
			return null;
		}
	}
}
