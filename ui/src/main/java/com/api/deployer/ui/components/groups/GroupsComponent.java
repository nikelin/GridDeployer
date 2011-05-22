/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.api.deployer.ui.components.groups;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.actions.ComponentAction;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.events.components.ComponentEvents;

/**
 *
 * @author root
 */
public class GroupsComponent extends AbstractComponent {

	public static class Events extends ComponentEvents {

		protected Events( String code ) {
			super(code);
		}

		public static class Views extends Events {

			protected Views( String code ) {
				super(code);
			}

			public static final Views List = new Views("GroupsComponent.Events.Views.List");

		}

		public static class Station extends Events {

			protected Station( String code ) {
				super(code);
			}

			public static final Station Added = new Station("GroupsComponent.Events.Station.Added");
			public static final Station Add = new Station("GroupsComponent.Events.Station.Add");
			public static final Station Remove = new Station("GroupsComponent.Events.Station.Remove");
		}

		public final static Events Rename = new Events("GroupsComponent.Events.Rename");
		public final static Events Remove = new Events("GroupsComponent.Events.Remove");
		public final static Events Add = new Events("GroupsComponent.Events.Add");
	}

	public GroupsComponent() {
		super("groups", "Groups");
	}

	@Override
	public void init() {
		Dispatcher.get().addController( new GroupsController() );

		this.addAction( new ComponentAction("List", this, GroupsComponent.Events.Views.List ) );
	}

}
