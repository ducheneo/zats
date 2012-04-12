/* ZatsContext.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.zkoss.zats.ZatsException;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EmulatorClient;
import org.zkoss.zats.mimic.impl.emulator.Emulator;
import org.zkoss.zats.mimic.impl.emulator.EmulatorBuilder;

/**
 * A default implementation of ZatsContext
 *   
 * @author Hawk
 * @author Dennis
 */
public class DefaultZatsContext implements ZatsContext{
	private static Logger logger = Logger.getLogger(DefaultZatsContext.class.getName());;
	
	
	private List<Client> clients = new LinkedList<Client>();
	private Emulator emulator ;
	
	//keep the file should be clean when destroying.
	private List<File> tempFiles = new ArrayList<File>();
	
	private boolean builtinConfig = true;
	
	/**
	 * Create a zats context, it use built-in config file(web.xml, zk.xml) to init the context safely.
	 */
	public DefaultZatsContext(){}
	
	/**
	 * Create a zats context, it use built-in config files(web.xml, zk.xml) to init the context quickly and safely. <br/>
	 * If you want to use the application's config file, you have to set builtinConfig to false
	 * and provide them in /WEB-INF/ in your resourceRoot folder (resourceRoot is provide when calling {@link #init(String)})
	 * @param builtinConfig use the builtin web.xml and zk.xml, default true
	 */
	public DefaultZatsContext(boolean builtinConfig){
		this.builtinConfig = builtinConfig;
	}

	public void init(String resourceRoot){
		if(emulator!=null) {
			throw new ZatsException("already started up");
		}
		// prepare builtin environment
		File builtInWeb = null;
		if(builtinConfig){
			String tmpDir = System.getProperty("java.io.tmpdir", ".");
			File tmpZats = new File(tmpDir,"zats");
			tmpZats.mkdirs();
			
			builtInWeb = new File(tmpZats,Long.toHexString(System.currentTimeMillis()));
			builtInWeb.mkdirs();
			tempFiles.add(0,builtInWeb);
			
			File webinf = new File(builtInWeb, "WEB-INF");
			tempFiles.add(0,webinf);
			if (!webinf.mkdirs())
				throw new ZatsException("can't create temp directory : "+webinf);
			
			File webxml = new File(webinf, "web.xml");
			copy(webxml,EmulatorClient.class.getResourceAsStream("WEB-INF/web.xml"));
			tempFiles.add(0,webxml);
			
			File zkxml = new File(webinf, "zk.xml");
			copy(zkxml,EmulatorClient.class.getResourceAsStream("WEB-INF/zk.xml"));
			tempFiles.add(0,zkxml);
		}
		
		EmulatorBuilder builder = new EmulatorBuilder();
		
		if(builtinConfig){
			builder.addContentRoot(builtInWeb.getAbsolutePath());
		}
		builder.addContentRoot(resourceRoot);
		emulator = builder.create();
	}

	private void copy(File file, InputStream src) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			byte[] buf = new byte[65536];
			int len;
			while (true) {
				len = src.read(buf);
				if (len < 0)
					break;
				os.write(buf, 0, len);
			}
		} catch (Exception e) {
			throw new ZatsException("fail to copy file "+file, e);
		} finally {
			close(src);
			close(os);
		}
	}

	private static void close(Closeable c) {
		try {
			c.close();
		} catch (Throwable e) {
			logger.severe("Cannot close" +c);
		}
	}
	
	public void destroy() {
		cleanup();
		if (emulator!=null){
			emulator.close();
			emulator=null;
		}
		for(File f:tempFiles){
			if(!f.delete()){
				f.deleteOnExit();
			}
		}
		tempFiles.clear();
	}

	/**
	 * create a client.
	 * @return
	 */
	public Client newClient(){
		if(emulator==null){
			throw new ZatsException("not initialize yet, please call init first");
		}
		Client client = new EmulatorClient(emulator);
		((ClientCtrl)client).setDestroyListener(new ClientCtrl.DestroyListener() {
			public void willDestroy(Client conv) {
				clients.remove(conv);//just remove it from list, client will destory itself
			}
		});
		clients.add(client);
		return client;
	}
	
	/**
	 * close all client and release resources.
	 */
	public void cleanup() {
		//to avoid concurrent modification exception in willClose
		for (Client c : clients.toArray(new Client[clients.size()])){
			c.destroy();
		}
	}
	

}
