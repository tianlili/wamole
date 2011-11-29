package com.baidu.wamole.model;

import java.io.File;
import java.io.IOException;

import com.baidu.wamole.xml.XmlFile;

/**
 * 提供统一接口，从配置文件载入对象实例
 * @author yangbo
 *
 */
public class Models {
	
	public static Model load(File dir) throws IOException{
		return Model.class.cast(getConfigFile(dir).read());
	}
	
    /**
     * The file we save our configuration.
     */
    public static XmlFile getConfigFile(File dir) {
        return new XmlFile(new File(dir,"config.xml"));
    }
    
    /**
     * The file we save our configuration.
     */
    public static XmlFile getConfigFile(Model model) {
        return getConfigFile(model.getRootDir());
    }
    
//    public static final XStream XSTREAM = new XStream();
}
