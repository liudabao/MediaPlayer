package com.geniusgithub.mediaplayer.dlna.proxy;


import android.content.Context;
import android.content.Intent;

import com.geniusgithub.mediaplayer.dlna.UpnpUtil;
import com.geniusgithub.mediaplayer.dlna.center.DlnaService;
import com.geniusgithub.mediaplayer.dlna.model.AbstractMediaMng;
import com.geniusgithub.mediaplayer.dlna.model.MediaServerMng;
import com.geniusgithub.mediaplayer.util.CommonLog;
import com.geniusgithub.mediaplayer.util.LogFactory;

import org.cybergarage.upnp.Device;

import java.util.List;


public class AllShareProxy implements IDeviceOperator,
										IDeviceOperator.IDMSDeviceOperator{

	private static final CommonLog log = LogFactory.createLog();

	
	private static  AllShareProxy instance;
	private Context mContext;
	
	private AbstractMediaMng dmsMediaMng;
	
	private AllShareProxy(Context context) {
		mContext = context;
		dmsMediaMng = new MediaServerMng(context);

	}

	public static synchronized AllShareProxy getInstance(Context context) {
		if (instance == null){
			instance  = new AllShareProxy(context);
		}
		return instance;
	}

	public void startSearch(){
		Intent intent = new Intent(DlnaService.SEARCH_DEVICES);
		intent.setPackage(mContext.getPackageName());
		mContext.startService(intent);
	}
	
	public void resetSearch(){
		Intent intent = new Intent(DlnaService.RESET_SEARCH_DEVICES);
		intent.setPackage(mContext.getPackageName());
		mContext.startService(intent);
		clearDevice();
	}
	
	public void exitSearch(){
		mContext.stopService(new Intent(mContext, DlnaService.class));
		clearDevice();
	}
	
	
	@Override
	public void addDevice(Device d) {
	    if (UpnpUtil.isMediaServerDevice(d)){
			dmsMediaMng.addDevice(d);
		}
	}

	@Override
	public void removeDevice(Device d) {
		if (UpnpUtil.isMediaServerDevice(d)){
			dmsMediaMng.removeDevice(d);
		}
	}

	@Override
	public void clearDevice() {
		dmsMediaMng.clear();
	}

	@Override
	public List<Device> getDMSDeviceList() {
		return dmsMediaMng.getDeviceList();
	}


	@Override
	public void setDMSSelectedDevice(Device selectedDevice) {
		dmsMediaMng.setSelectedDevice(selectedDevice);
	}

	@Override
	public Device getDMSSelectedDevice() {
		return dmsMediaMng.getSelectedDevice();
	}

}
