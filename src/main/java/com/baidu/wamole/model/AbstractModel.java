package com.baidu.wamole.model;

import java.io.File;
import java.io.IOException;

import com.baidu.wamole.data.Exported;
import com.baidu.wamole.xml.XmlFile;

/**
 * 提供统一的单配置和组配置存储解决方案
 * @author yangbo
 *
 */
public abstract class AbstractModel<P extends ModelGroup> implements Model, Savable, Persistence{
	protected /*final*/ transient String name;
	P parent;
	
    protected AbstractModel(P parent, String name) {
    	this.parent = parent;
    	setName(name);
    }
    
    public P getParent() {
    	return parent;
    }
    
    @Exported
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public synchronized void save() throws IOException {
        getConfigFile().write(this);
    }
    
    public final XmlFile getConfigFile() {
        return new XmlFile(new File(getRootDir(), "config.xml"));
    }
    
}
