package com.baidu.wamole.model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jetty.util.log.Log;

import com.baidu.wamole.task.JsResultBrowser;
import com.baidu.wamole.task.JsResultTable;
import com.baidu.wamole.task.Result;

public abstract class AbstractModelGroup<Parent extends ModelGroup> extends
		AbstractModel<Parent> implements ModelGroup {

	protected transient List<Model> models;

	@Override
	public List<Model> getModels() {
		if (models == null)
			models = new ArrayList<Model>();
		return models;
	}

	/**
	 * 获取一种类型的model，无法在此处直接追加新实例
	 * 
	 * @param clazz
	 * @return
	 */
	public <M extends Model> Collection<M> getModels(Class<M> clazz) {
		ArrayList<M> list = new ArrayList<M>();
		for (Model o : getModels()) {
			if (clazz.isInstance(o))
				list.add(clazz.cast(o));
		}
		return Collections.unmodifiableCollection(list);
	}

	/**
	 * 根据类型和名称获取特定实例
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public <M extends Model> M getModel(Class<M> clazz, String name) {
		for (M m : getModels(clazz))
			if (m.getName().equals(name))
				return m;
		return null;
	}

	/**
	 * 取第一个实例，用于单实例场景
	 * 
	 * @param clazz
	 * @return
	 */
	public <M extends Model> M getModel(Class<M> clazz) {
		return getModels(clazz).iterator().next();
	}

	public <M extends Model> void addModel(M m) {
		getModels().add(m);
	}

	public void loadChildren(String path) throws IOException {
		loadChildren(path, "config.xml");
	}

	public void loadChildren(final String path, final String file)
			throws IOException {

		File modelsDir = new File(this.getRootDir(), path);
		if (!modelsDir.isDirectory() && !modelsDir.mkdirs()) {
			if (modelsDir.exists())
				throw new IOException(modelsDir + " is not a directory");
			throw new IOException(
					"Unable to create "
							+ modelsDir
							+ "\nPermission issue? Please create this directory manually.");
		}
		File[] subdirs = modelsDir.listFiles(new FileFilter() {
			public boolean accept(File child) {
				return child.isDirectory()
						&& getConfigFile(child, file).exists();
			}
		});

		for (final File subdir : subdirs) {
			this.addModel(loadChild(subdir, file));
		}
	}

	public Model loadChild(File dir, String file) throws IOException {
		Log.info("load model, dir : " + dir + ", file : " + file);
		// 名称取目录路径
		Model m = getConfigFile(dir, file).read();
		m.setName(dir.getName());
		m.setParent(this);

		if (m instanceof ModelGroup)
			ModelGroup.class.cast(m).load();
		Log.info("load model finish, class : " + m.getClass());
		return m;
	}
}
