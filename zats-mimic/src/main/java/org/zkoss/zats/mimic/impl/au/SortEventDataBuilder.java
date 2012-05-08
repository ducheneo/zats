/* SortEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/5/7 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SortEvent;

/**
 * @author Hawk
 *
 */
public class SortEventDataBuilder implements EventDataBuilder<SortEvent> {

	public Map<String, Object> build(SortEvent event, Map<String, Object> data) {
		AuUtility.setEssential(data, "", event.isAscending());
		return data;
	}

	public Class<SortEvent> getEventClass() {
		return SortEvent.class;
	}

}