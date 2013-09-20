/*
 * @(#)PandaBoxAction.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Pedro Santos
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Pandabox Module.
 *
 *   The Pandabox Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Pandabox Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Pandabox Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.pandabox.presentation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.contents.Node;
import pt.ist.bennu.core.domain.groups.AnyoneGroup;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.vaadin.actions.VaadinContextAction;
import pt.ist.bennu.vaadin.domain.contents.VaadinNode;
import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.vaadinframework.fragment.FragmentQuery;

@Mapping(path = "/panda")
/**
 * 
 * @author Pedro Santos
 * 
 */
public class PandaBoxAction extends VaadinContextAction {
    private static final String bundle = "resources.PandaboxResources";

    @CreateNodeAction(bundle = "PANDABOX_RESOURCES", key = "module.pandabox.boxpage", groupKey = "module.pandabox")
    public final ActionForward createRepositoryNode(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");

	final Node parent = getDomainObject(request, "parentOfNodesToManageId");

	VaadinNode.createVaadinNode(virtualHost, parent, bundle, "module.pandabox.boxpage",
		new FragmentQuery(PandaBox.class).getQueryString(), AnyoneGroup.getInstance());

	return forwardToMuneConfiguration(request, virtualHost, parent);
    }
}
