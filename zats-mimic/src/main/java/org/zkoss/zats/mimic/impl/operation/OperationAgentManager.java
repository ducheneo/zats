/* OperationAgentManager.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zats.mimic.Agent;
import org.zkoss.zats.mimic.impl.Util;
import org.zkoss.zats.mimic.impl.operation.input.DateInputAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.input.DecimalInputAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.input.DecimalStringInputAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.input.IntegerInputAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.input.IntegerStringInputAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.input.TextInputAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.input.TimeInputAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.select.ComboitemSelectAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.select.LisitemSelectAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.select.ListitemMultipleSelectAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.select.TabSelectAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.select.TreeSelectAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.select.TreeitemMultipleSelectAgentBuilder;
import org.zkoss.zats.mimic.operation.OperationAgent;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.A;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.East;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listgroup;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.North;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Slider;
import org.zkoss.zul.South;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Splitter;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.West;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

public class OperationAgentManager {
	private static Map<Key, OperationAgentBuilder<? extends Agent, ? extends OperationAgent>> builders;

	static {
		builders = new HashMap<OperationAgentManager.Key, OperationAgentBuilder<? extends Agent, ? extends OperationAgent>>();

		//most common agents
		registerBuilder("5.0.0", "*", Desktop.class, new DesktopBookmarkAgentBuilder());

		registerBuilder("5.0.0", "*", AbstractComponent.class, new GenericClickAgentBuilder());
		registerBuilder("5.0.0", "*", AbstractComponent.class, new GenericKeyStrokeAgentBuilder());

		// the focus
		registerBuilder("5.0.0", "*", InputElement.class, new GenericFocusAgentBuilder());
		registerBuilder("5.0.0", "*", A.class, new GenericFocusAgentBuilder());
		registerBuilder("5.0.0", "*", Button.class, new GenericFocusAgentBuilder());
		registerBuilder("5.0.0", "*", Checkbox.class, new GenericFocusAgentBuilder());
		registerBuilder("5.0.0", "*", Listbox.class, new GenericFocusAgentBuilder());
		registerBuilder("5.0.0", "*", Tree.class,new GenericFocusAgentBuilder());
		
		// the inputs
		registerBuilder("5.0.0", "*", InputElement.class, new TextInputAgentBuilder());
		registerBuilder("5.0.0", "*", Intbox.class, new IntegerInputAgentBuilder());
		registerBuilder("5.0.0", "*", Longbox.class, new IntegerStringInputAgentBuilder());
		registerBuilder("5.0.0", "*", Spinner.class, new IntegerInputAgentBuilder());

		registerBuilder("5.0.0", "*", Decimalbox.class, new DecimalStringInputAgentBuilder());
		registerBuilder("5.0.0", "*", Doublebox.class, new DecimalInputAgentBuilder());
		registerBuilder("5.0.0", "*", Doublespinner.class, new DecimalInputAgentBuilder());

		registerBuilder("5.0.0", "*", Datebox.class, new DateInputAgentBuilder());
		registerBuilder("5.0.0", "*", Timebox.class, new TimeInputAgentBuilder());

		// the check
		registerBuilder("5.0.0", "*", Menuitem.class,new GenericCheckAgentBuilder());
		// the check of check box and radio button
		registerBuilder("5.0.0", "*", Checkbox.class, new GenericCheckAgentBuilder());
		
		// the single select
		registerBuilder("5.0.0", "*", Comboitem.class, new ComboitemSelectAgentBuilder());
		registerBuilder("5.0.0", "*", Tab.class, new TabSelectAgentBuilder());
		registerBuilder("5.0.0", "*", Listitem.class, new LisitemSelectAgentBuilder());
		registerBuilder("5.0.0", "*", Treeitem.class, new TreeSelectAgentBuilder());
		
		// the multiple select
		registerBuilder("5.0.0", "*", Listitem.class, new ListitemMultipleSelectAgentBuilder());
		registerBuilder("5.0.0", "*", Treeitem.class, new TreeitemMultipleSelectAgentBuilder());
		
		// the open
		registerBuilder("5.0.0", "*", Groupbox.class, new GenericOpenAgentBuilder());
		registerBuilder("5.0.0", "*", Detail.class, new GenericOpenAgentBuilder());
		registerBuilder("5.0.0", "*", Group.class, new GenericOpenAgentBuilder());
		registerBuilder("5.0.0", "*", Listgroup.class, new GenericOpenAgentBuilder());
		registerBuilder("5.0.0", "*", Treeitem.class, new GenericOpenAgentBuilder());
		registerBuilder("5.0.0", "*", Window.class, new GenericOpenAgentBuilder());
		registerBuilder("5.0.0", "*", Panel.class, new GenericOpenAgentBuilder());
		registerBuilder("5.0.0", "*", Center.class, new GenericOpenAgentBuilder());
		registerBuilder("5.0.0", "*", North.class, new GenericOpenAgentBuilder());
		registerBuilder("5.0.0", "*", East.class, new GenericOpenAgentBuilder());
		registerBuilder("5.0.0", "*", West.class, new GenericOpenAgentBuilder());
		registerBuilder("5.0.0", "*", South.class, new GenericOpenAgentBuilder());
		registerBuilder("5.0.0", "*", Splitter.class, new GenericOpenAgentBuilder());
		registerBuilder("5.0.0", "*", Popup.class, new GenericOpenAgentBuilder());
		
		registerBuilder("5.0.0", "*", Bandbox.class, new TextboxOpenAgentBuilder());
		registerBuilder("5.0.0", "*", Combobox.class, new TextboxOpenAgentBuilder());
		
		// the close
		registerBuilder("5.0.0", "*", Window.class,new GenericCloseAgentBuilder());
		registerBuilder("5.0.0", "*", Panel.class, new GenericCloseAgentBuilder());
		registerBuilder("5.0.0", "*", Tab.class,new GenericCloseAgentBuilder());

		// the render
		registerBuilder("5.0.0", "*", Listbox.class, new ListboxRenderAgentBuilder());
		registerBuilder("5.0.0", "*", Grid.class, new GridRenderAgentBuilder());

		// the resize
		registerBuilder("5.0.0", "*", Window.class, new WindowSizeAgentBuilder());
		registerBuilder("5.0.0", "*", Panel.class, new PanelSizeAgentBuilder());
		registerBuilder("5.0.0", "*", Column.class, new HeaderSizeAgentBuilder());
		registerBuilder("5.0.0", "*", Listheader.class, new HeaderSizeAgentBuilder());
		registerBuilder("5.0.0", "*", Treecol.class, new HeaderSizeAgentBuilder());

		//drag & drop
		registerBuilder("5.0.0", "*", HtmlBasedComponent.class, new GenericDragAgentBuilder());
		
		//hover
		registerBuilder("5.0.0", "*", HtmlBasedComponent.class, new GenericHoverAgentBuilder());
		
		//paging
		registerBuilder("5.0.0", "*", Paging.class, new PagingAgentBuilder());
		
		//group
		registerBuilder("5.0.0", "*", Column.class, new GenericGroupAgentBuilder());

		//sort
		registerBuilder("5.0.0", "*", Column.class, new ColumnSortAgentBuilder());
		registerBuilder("5.0.0", "*", Listheader.class, new ListheaderSortAgentBuilder());
		registerBuilder("5.0.0", "*", Treecol.class, new TreecolSortAgentBuilder());
		
		// the scroll
		registerBuilder("5.0.0", "*", Slider.class, new SliderInputAgentBuilder());
		
		// the move
		registerBuilder("5.0.0", "*", Window.class, new GenericMoveAgentBuilder());
		registerBuilder("5.0.0", "*", Panel.class, new GenericMoveAgentBuilder());
		
		//----------special case ---
		
		
		String extClz; 
		
		//colorbox in zkex.jar which is optional
		extClz = "org.zkoss.zkex.zul.Colorbox";
		if(Util.hasClass(extClz)){
			registerBuilder("5.0.0", "*", extClz,
					"org.zkoss.zats.mimic.impl.operation.input.ColorboxInputAgentBuilder");
		}
		
		// the ckeditor (optional)
		extClz = "org.zkforge.ckez.CKeditor";
		if(Util.hasClass(extClz)){
			registerBuilder("5.0.0", "*", extClz,
					"org.zkoss.zats.mimic.impl.operation.input.TextInputAgentBuilder");
		} 
		
		// the check of zhtml (optional)
		extClz = "org.zkoss.zhtml.Input";
		if(Util.hasClass(extClz)){
			registerBuilder("5.0.0", "*", extClz,
					"org.zkoss.zats.mimic.impl.operation.GenericCheckAgentBuilder");
		} 
	}

	/**
	 * Register a operation builder mapping to component and operation. We can
	 * specify zk version worked on. The version text could be normal version
	 * format (e.g 6.0.0 or 5.0.7.1) or "*" sign means no specify. If specify
	 * version range doesn't include current zk version at runtime, this
	 * register will be ignored.
	 * <p/>
	 * 
	 * Use this API if the component is only in a particular zk version only to
	 * avoid initial exception.
	 * <p/>
	 * 
	 * @param startVersion
	 *            start version (include)
	 * @param endVersion
	 *            end version (include)
	 * @param delegateeClass
	 *            the component class that builder maps to ( *notice: it should
	 *            not specify interface)
	 * @param builderClazz
	 *            operation builder
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <O extends OperationAgent> void registerBuilder(String startVersion, String endVersion,
			String delegateeClass, String builderClazz) {
		if (startVersion == null || endVersion == null || delegateeClass == null || builderClazz == null)
			throw new IllegalArgumentException();

		if (!Util.checkVersion(startVersion, endVersion))
			return;

		Class clz = null;
		try {
			clz = Class.forName(delegateeClass);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("delegateeClass " + delegateeClass + " not found ", e);
		}
		OperationAgentBuilder<Agent,O> builder = null;
		try {
			Class buildClz = Class.forName(builderClazz);
			builder = (OperationAgentBuilder<Agent,O>) buildClz.newInstance();
		} catch (Exception x) {
			throw new IllegalArgumentException(x.getMessage(), x);
		}

		registerBuilder(startVersion, endVersion, clz, builder);
	}

	/**
	 * register a operation builder mapping to component and operation. We can
	 * specify zk version worked on. The version text could be normal version
	 * format (e.g 6.0.0 or 5.0.7.1) or "*" sign means no specify. If specify
	 * version range doesn't include current zk version at runtime, this
	 * register will be ignored.
	 * 
	 * @param startVersion
	 *            start version (include)
	 * @param endVersion
	 *            end version (include)
	 * @param delegateeClass
	 *            the component class that builder maps to ( *notice: it should
	 *            not specify interface)
	 * @param builder
	 *            operation builder
	 */
	public static <O extends OperationAgent, C extends Object> void registerBuilder(String startVersion,
			String endVersion, Class<C> delegateeClass, OperationAgentBuilder<? extends Agent,O> builder) {

		if (startVersion == null || endVersion == null || delegateeClass == null || builder == null)
			throw new IllegalArgumentException();

		if (!Util.checkVersion(startVersion, endVersion))
			return;

		// component and operation classes mapping to builder
		// builder would be replace by later register
		builders.put(new Key(delegateeClass, builder.getOperationClass()), builder);
	}

	@SuppressWarnings("unchecked")
	public static <O extends OperationAgent> OperationAgentBuilder<Agent, O> getBuilder(Object delegatee, Class<O> operation) {
		// search from self class to parent class
		Class<?> c = delegatee.getClass();
		while (c != null) {
			OperationAgentBuilder<? extends Agent, ? extends OperationAgent> builder = builders.get(new Key(c, operation));
			if (builder != null)
				return (OperationAgentBuilder<Agent, O>) builder;
			//check interface also
			Class<?>[] ifs = c.getInterfaces();
			for(Class<?> i:ifs){
				builder = builders.get(new Key(i, operation));
				if (builder != null)
					return (OperationAgentBuilder<Agent, O>) builder;
			}
			//check super
			c = c.getSuperclass();
		}
		return null; // not found
	}

	/**
	 * for operation builder mapping
	 * 
	 * @author pao
	 */
	private static class Key {
		public Class<?> c;
		public Class<?> o;

		public <O extends OperationAgent, C extends Object> Key(Class<C> c, Class<O> o) {
			this.c = c;
			this.o = o;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((c == null) ? 0 : c.hashCode());
			result = prime * result + ((o == null) ? 0 : o.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			else if (obj instanceof Key) {
				Key o = (Key) obj;
				return o.o == this.o && o.c == this.c;
			}
			return false;
		}
	}
}
