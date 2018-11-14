package com.chun.calc.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


/**
 * 类说明 Created on 2018-01-13 
 * 
 * @author Dachun Li
 */
public class FunUtil {
    private static final String TAG = "FunUtil";
	private static SimpleDateFormat dateFormat1 = new SimpleDateFormat(
			"MM-dd HH:mm");
	private static SimpleDateFormat dateFormat2 = new SimpleDateFormat(
			"今天 HH:mm");
	private static SimpleDateFormat dateFormat3 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public static String getTimeStr(long time) {
		return getTimeStr(new Date(time));
	}
	public static String getTimeStr(Date aDate) {
		try {
			Date current = new Date(System.currentTimeMillis());
			long beforTimes = System.currentTimeMillis() - aDate.getTime();
			if (beforTimes < 51 * 1000) {
				return Math.round(beforTimes / 10000 + 0.5) + "0秒前";
			}
			if (beforTimes < 60 * 60 * 1000) {
				return Math.round(beforTimes / (1000 * 60) + 0.5) + "分钟前";
			}
			if (aDate.getYear() == current.getYear()
					&& aDate.getMonth() == current.getMonth()
					&& aDate.getDate() == current.getDate()) {
				return dateFormat2.format(aDate);
			} else if (aDate.getYear() == current.getYear()) {
				return dateFormat1.format(aDate);
			} else {
				return dateFormat3.format(aDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
    public static boolean isNotEmpty(Object str){
        return !isEmpty((String)str);
    }
    public static boolean isEmpty(Object str){
        return isEmpty((String)str);
    }
    public static boolean isEmpty(Double str){
        return str == null;
    }
	public static boolean isEmpty(List list){
		return list == null || list.size() == 0;
	}

	public static String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static String getPath(Context context, Uri uri) {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it
			}
		}

		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
		}
		return version;
	}
	
    public static String getUserMobileNum(String account){
        return "";
    }
    /**
     *转换为时间格式
     * @param time
     * @param i
     * @return  string
     */
    public static String getDateFormat(Timestamp time,int i){
        if(time==null) return "";
        String format = "yyyy-MM-dd";
        switch(i){
            case 1:format="yyyy-MM-dd";break;
            case 2:format="yyyy-MM-dd HH:mm";break;
            case 3:format="HH点mm分";break;
            case 4:format="yyyy年MM月dd日 HH点mm分";break;
            case 5:format="yyyyMMdd";break;
            case 6:format="yyyy年MM月dd日";break;
            case 7:format="yyyyMMdd";break;
            case 8:format="MM月dd日 HH点mm分";break;
            case 9:format="MM-dd HH:mm";break;
            case 10:format="MM月dd日";break;
            case 11:format=" HH:mm";break;
            case 12:format="yyyy";break;
            case 13:format="MM-dd";break;
            case 14:format="yyyyMM";break;
            case 15:format="yyyy-MM-dd hh:mm";break;
            case 16:format="yy-MMdd hh:mm";break;
            case 17:format="yyyyMMddHH";break;
            case 18:format="HH";break;
            case 19:format="dd";break;
            case 20:format="MM/dd";break;
            case 21:format="yyyyMMddHHmmss";break;
            case 22:format="yyyyMMddHHmm";break;
            default: format = "yyyy-MM-dd";break;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    public static String getDateFormat(java.util.Date time, String format) {
        if (time == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    public static boolean isActivity(Context context,String activityClass){
        Intent intent = new Intent();
        String packName = context.getPackageName();
        intent.setClassName(packName, activityClass);
        if (context.getPackageManager().resolveActivity(intent, 0) == null) {
            return false;
        } else {
            return true;
        }
    }
    public static boolean isActivity(Context context,String packageName,String activityClass){
        Intent intent = new Intent();
        intent.setClassName(packageName, activityClass);
        if (context.getPackageManager().resolveActivity(intent, 0) == null) {
            return false;
        } else {
            return true;
        }
    }
    public static void Log(String text){
        Log.v("XJJOA", text);
    }
    public static void ERR(String text){
        Log.e("XJJOA",text);
    }

    public static String toJson(Serializable obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static String encode(String str){
        /*if(str==null) return str;
        try{
            return URLEncoder.encode(str,"UTF-8");
        }catch(UnsupportedEncodingException ex){
            return str;
        }*/
        return str;
    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    /**
     * 生成32位md5码
     * @param password
     * @return
     */
    public static String md5Password(String password) {

        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取两段日期中间的所有日期
     *
     * @param dBegin 开始时间
     * @param dEnd   结束时间
     * @return 日期列表
     */
    public static List<Date> getDatesByBeginEnd(Date dBegin, Date dEnd) {
        if (dBegin == null || dEnd == null) {
            return null;
        }
        List<Date> lDate = new ArrayList();
        lDate.add(dBegin);
        java.util.Calendar calBegin = java.util.Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        java.util.Calendar calEnd = java.util.Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(java.util.Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }

    public static String getFilePathByUri(Context context, Uri uri) {
        String path = null;
        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            path = uri.getPath();
            return path;
        }
        // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{ MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (columnIndex > -1) {
                        path = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
            return path;
        }
        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                    return path;
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                    return path;
                }
            }
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

	
}
