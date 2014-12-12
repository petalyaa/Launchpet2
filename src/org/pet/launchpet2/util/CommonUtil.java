package org.pet.launchpet2.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.pet.launchpet2.model.HomeNewsItem;
import org.pet.launchpet2.model.LauncherApplication;

import android.os.Environment;

public class CommonUtil {

	public static final void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static final String getMD5(String s) {
		final String MD5 = "MD5";
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();
			StringBuilder hexString = new StringBuilder();
			for (byte aMessageDigest : messageDigest) {
				String h = Integer.toHexString(0xFF & aMessageDigest);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static final File getLauncherItemFile() {
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/" + ConfigurationUtil.APPLICATION_SD_DIRECTORY);
		if(!myDir.exists())
			myDir.mkdir();
		myDir = new File(myDir, ConfigurationUtil.SUBDIRECTORY_CACHE);
		if(!myDir.exists())
			myDir.mkdir();
		myDir = new File(myDir, ConfigurationUtil.SUBDIRECTORY_APPS);
		if(!myDir.exists())
			myDir.mkdir();
		String fileName = getMD5(ConfigurationUtil.FILENAME_APPS_CACHE);
		myDir = new File(myDir, fileName);
		return myDir;
	}

	public static final File getCacheNewsItemFile() {
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/" + ConfigurationUtil.APPLICATION_SD_DIRECTORY);
		if(!myDir.exists())
			myDir.mkdir();
		myDir = new File(myDir, ConfigurationUtil.SUBDIRECTORY_CACHE);
		if(!myDir.exists())
			myDir.mkdir();
		myDir = new File(myDir, ConfigurationUtil.SUBDIRECTORY_NEWS);
		if(!myDir.exists())
			myDir.mkdir();
		String fileName = getMD5(ConfigurationUtil.FILENAME_NEWS_CACHE);
		myDir = new File(myDir, fileName);
		return myDir;
	}

	@SuppressWarnings("unchecked")
	public static final List<HomeNewsItem> deserializeHomeNewsObject() {
		return (List<HomeNewsItem>) deserializeList(getCacheNewsItemFile());
	}
	
	@SuppressWarnings("unchecked")
	public static final List<LauncherApplication> deserializeLauncherApps() {
		return (List<LauncherApplication>) deserializeList(getLauncherItemFile());
	}
	
	public static final void serializeApplicationobjectList(List<LauncherApplication> appList) {
		serializeList(appList, getLauncherItemFile());
	}

	public static final void serializeHomeNewsObjectList(List<HomeNewsItem> homeNewsItemList) {
		serializeList(homeNewsItemList, getCacheNewsItemFile());
	}
	
	public static final List<?> deserializeList(File file) {
		List<?> list= null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			list = (List<?>) ois.readObject();
		} catch(IOException e){
			e.printStackTrace();
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		} finally {
			try {
				if(ois != null)
					ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static final void serializeList(List<?> list, File file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try{
			fos= new FileOutputStream(file);
			oos= new ObjectOutputStream(fos);
			oos.writeObject(list);
		}catch(IOException ioe){
			ioe.printStackTrace();
		} finally {
			try {
				if(oos != null)
					oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
