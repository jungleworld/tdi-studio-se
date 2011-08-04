// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.editor;

import java.util.EventObject;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.talend.designer.xmlmap.dnd.XmlDragSourceListener;
import org.talend.designer.xmlmap.dnd.XmlDropTargetListener;
import org.talend.designer.xmlmap.editor.actions.CreateAttributeAction;
import org.talend.designer.xmlmap.editor.actions.CreateElementAction;
import org.talend.designer.xmlmap.editor.actions.CreateNameSpaceAction;
import org.talend.designer.xmlmap.editor.actions.DeleteTreeNodeAction;
import org.talend.designer.xmlmap.editor.actions.FixValueAction;
import org.talend.designer.xmlmap.editor.actions.ImportTreeFromXml;
import org.talend.designer.xmlmap.editor.actions.InputImportTreeFromRepository;
import org.talend.designer.xmlmap.editor.actions.OutputImportTreeFromRepository;
import org.talend.designer.xmlmap.editor.actions.OutputImportTreeFromXml;
import org.talend.designer.xmlmap.editor.actions.SetGroupAction;
import org.talend.designer.xmlmap.editor.actions.SetLoopAction;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlMapEditor extends GraphicalEditor {

    private KeyHandler sharedKeyHandler;

    MapperManager mapperManager;

    public XmlMapEditor(MapperManager mapperManager) {
        DefaultEditDomain defaultEditDomain = new DefaultEditDomain(this);
        defaultEditDomain.getCommandStack().addCommandStackListener(this);
        setEditDomain(defaultEditDomain);
        this.mapperManager = mapperManager;
    }

    protected void createGraphicalViewer(final Composite parent) {
        // rulerComp = new RulerComposite(parent, SWT.BORDER);
        XmlMapGraphicViewer viewer = new XmlMapGraphicViewer();
        viewer.setMapperManager(mapperManager);
        viewer.createControl(parent);
        setGraphicalViewer(viewer);
        configureGraphicalViewer();
        hookGraphicalViewer();
        initializeGraphicalViewer();
        // super.createGraphicalViewer(rulerComp);
        // rulerComp.setGraphicalViewer( getGraphicalViewer());
    }

    /**
     * @see org.eclipse.gef.commands.CommandStackListener#commandStackChanged(java.util.EventObject)
     */
    public void commandStackChanged(EventObject event) {
        firePropertyChange(IEditorPart.PROP_DIRTY);
        super.commandStackChanged(event);
    }

    /**
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#createActions()
     */
    protected void createActions() {

        OutputImportTreeFromXml outputImportAction = new OutputImportTreeFromXml(this, getGraphicalViewer().getControl()
                .getShell());
        outputImportAction.setMapperManager(mapperManager);
        getActionRegistry().registerAction(outputImportAction);
        getSelectionActions().add(outputImportAction.getId());

        ImportTreeFromXml importAction = new ImportTreeFromXml(this, getGraphicalViewer().getControl().getShell());
        importAction.setMapperManager(mapperManager);
        getActionRegistry().registerAction(importAction);
        getSelectionActions().add(importAction.getId());

        CreateAttributeAction createAttribute = new CreateAttributeAction(this);
        createAttribute.setMapperManager(mapperManager);
        getActionRegistry().registerAction(createAttribute);
        getSelectionActions().add(createAttribute.getId());

        CreateElementAction createElement = new CreateElementAction(this);
        createElement.setMapperManager(mapperManager);
        getActionRegistry().registerAction(createElement);
        getSelectionActions().add(createElement.getId());

        DeleteTreeNodeAction deleteAction = new DeleteTreeNodeAction(this);
        deleteAction.setMapperManager(mapperManager);
        getActionRegistry().registerAction(deleteAction);
        getSelectionActions().add(deleteAction.getId());

        SetLoopAction loopAction = new SetLoopAction(this);
        loopAction.setMapperManager(mapperManager);
        getActionRegistry().registerAction(loopAction);
        getSelectionActions().add(loopAction.getId());

        IAction groupAction = new SetGroupAction(this);
        getActionRegistry().registerAction(groupAction);
        getSelectionActions().add(groupAction.getId());

        InputImportTreeFromRepository importFromRepository = new InputImportTreeFromRepository(this, getGraphicalViewer()
                .getControl().getShell());
        importFromRepository.setMapperManager(mapperManager);
        getActionRegistry().registerAction(importFromRepository);
        getSelectionActions().add(importFromRepository.getId());

        OutputImportTreeFromRepository outputImportFromRepository = new OutputImportTreeFromRepository(this, getGraphicalViewer()
                .getControl().getShell());
        outputImportFromRepository.setMapperManager(mapperManager);
        getActionRegistry().registerAction(outputImportFromRepository);
        getSelectionActions().add(outputImportFromRepository.getId());

        CreateNameSpaceAction createNameSpace = new CreateNameSpaceAction(this);
        createNameSpace.setMapperManager(mapperManager);
        getActionRegistry().registerAction(createNameSpace);
        getSelectionActions().add(createNameSpace.getId());

        FixValueAction fixValueAction = new FixValueAction(this);
        getActionRegistry().registerAction(fixValueAction);
        getSelectionActions().add(fixValueAction.getId());
    }

    /**
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
     */
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        getGraphicalViewer().setRootEditPart(new XmlMapScalableRootEditPart());
        getGraphicalViewer().setEditPartFactory(new XmlMapPartFactory());
        getGraphicalViewer().setKeyHandler(new GraphicalViewerKeyHandler(getGraphicalViewer()).setParent(getCommonKeyHandler()));

    }

    /**
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
     */
    protected void initializeGraphicalViewer() {

        // getGraphicalViewer().setContents(getContents());

        getGraphicalViewer().addDragSourceListener(new XmlDragSourceListener(getGraphicalViewer()));

        getGraphicalViewer().addDropTargetListener(new XmlDropTargetListener(getGraphicalViewer()));

        getGraphicalViewer().addSelectionChangedListener(mapperManager);

        getGraphicalViewer().setContextMenu(new MenueProvider(getGraphicalViewer()));
        initializeActionRegistry();
    }

    public MapperManager getMapperManager() {
        return this.mapperManager;
    }

    public void setContent(Object content) {
        getGraphicalViewer().setContents(content);
    }

    protected KeyHandler getCommonKeyHandler() {
        if (sharedKeyHandler == null) {
            // sharedKeyHandler = new KeyHandler();
            // sharedKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
            // getActionRegistry().getAction(ActionFactory.DELETE.getId()));
            // sharedKeyHandler.put(KeyStroke.getPressed(SWT.F2, 0),
            // getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
        }
        return sharedKeyHandler;
    }

    /**
     * Get the viewer in the editor.
     * 
     * @return
     */
    public GraphicalViewer getViewer() {
        return getGraphicalViewer();
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void hookGraphicalViewer() {
        getSelectionSynchronizer().addViewer(getGraphicalViewer());
    }

    /**
     * wchen class global comment. Detailled comment
     */
    class MenueProvider extends ContextMenuProvider {

        public MenueProvider(EditPartViewer viewer) {
            super(viewer);
        }

        @Override
        public void buildContextMenu(IMenuManager menu) {
            // context menu should only display in the document tree
            List selectedEditParts = getGraphicalViewer().getSelectedEditParts();
            if (selectedEditParts != null && !selectedEditParts.isEmpty()) {

                // get last selected part in case there are two objects in the list , see the change in
                // XmlMapGraphicViewer primDeselectAll()
                Object object = selectedEditParts.get(selectedEditParts.size() - 1);

                if (object instanceof OutputTreeNodeEditPart) {
                    OutputTreeNode model = (OutputTreeNode) ((OutputTreeNodeEditPart) object).getModel();
                    if (XmlMapUtil.DOCUMENT.equals(model.getType()) || XmlMapUtil.getXPathLength(model.getXpath()) > 2) {
                        OutputImportTreeFromXml importAction = (OutputImportTreeFromXml) getActionRegistry().getAction(
                                OutputImportTreeFromXml.ID);
                        importAction.update(object);
                        if (importAction.isEnabled()) {
                            menu.add(importAction);
                        }

                        CreateElementAction createElement = (CreateElementAction) getActionRegistry().getAction(
                                CreateElementAction.ID);
                        createElement.update(object);
                        createElement.setInput(false);
                        if (createElement.isEnabled()) {
                            menu.add(createElement);
                        }

                        CreateAttributeAction createAttribute = (CreateAttributeAction) getActionRegistry().getAction(
                                CreateAttributeAction.ID);
                        createAttribute.update(object);
                        createAttribute.setInput(false);
                        if (createAttribute.isEnabled()) {
                            menu.add(createAttribute);
                        }

                        CreateNameSpaceAction createNameSpace = (CreateNameSpaceAction) getActionRegistry().getAction(
                                CreateNameSpaceAction.ID);
                        createNameSpace.update(object);
                        if (createNameSpace.isEnabled()) {
                            menu.add(createNameSpace);
                        }

                        FixValueAction fixValueAction = (FixValueAction) getActionRegistry().getAction(FixValueAction.ID);
                        fixValueAction.update(object);
                        if (fixValueAction.isEnabled()) {
                            menu.add(fixValueAction);
                        }

                        DeleteTreeNodeAction action = (DeleteTreeNodeAction) getActionRegistry().getAction(
                                DeleteTreeNodeAction.ID);
                        action.update(object);
                        action.setInput(false);
                        if (action.isEnabled()) {
                            menu.add(action);
                        }

                        SetLoopAction loopAction = (SetLoopAction) getActionRegistry().getAction(SetLoopAction.ID);
                        loopAction.update(object);
                        if (loopAction.isEnabled()) {
                            menu.add(loopAction);
                        }

                        SetGroupAction grouptAction = (SetGroupAction) getActionRegistry().getAction(SetGroupAction.ID);
                        grouptAction.update(object);
                        if (grouptAction.isEnabled()) {
                            menu.add(grouptAction);
                        }

                        OutputImportTreeFromRepository importFromRepository = (OutputImportTreeFromRepository) getActionRegistry()
                                .getAction(OutputImportTreeFromRepository.ID);
                        importFromRepository.update(object);
                        if (importFromRepository.isEnabled()) {
                            menu.add(importFromRepository);
                        }

                    }

                } else if (object instanceof TreeNodeEditPart) {
                    ImportTreeFromXml importAction = (ImportTreeFromXml) getActionRegistry().getAction(ImportTreeFromXml.ID);
                    importAction.update(object);
                    if (importAction.isEnabled()) {
                        menu.add(importAction);
                    }

                    CreateElementAction createElement = (CreateElementAction) getActionRegistry().getAction(
                            CreateElementAction.ID);
                    createElement.setInput(true);
                    createElement.update(object);
                    if (createElement.isEnabled()) {
                        menu.add(createElement);
                    }

                    CreateAttributeAction createAttribute = (CreateAttributeAction) getActionRegistry().getAction(
                            CreateAttributeAction.ID);
                    createAttribute.setInput(true);
                    createAttribute.update(object);
                    if (createAttribute.isEnabled()) {
                        menu.add(createAttribute);
                    }

                    DeleteTreeNodeAction deleteAction = (DeleteTreeNodeAction) getActionRegistry().getAction(
                            DeleteTreeNodeAction.ID);
                    deleteAction.setInput(true);
                    deleteAction.update(object);
                    if (deleteAction.isEnabled()) {
                        menu.add(deleteAction);
                    }

                    SetLoopAction loopAction = (SetLoopAction) getActionRegistry().getAction(SetLoopAction.ID);
                    loopAction.update(object);
                    if (loopAction.isEnabled()) {
                        menu.add(loopAction);
                    }

                    InputImportTreeFromRepository importFromRepository = (InputImportTreeFromRepository) getActionRegistry()
                            .getAction(InputImportTreeFromRepository.ID);
                    importFromRepository.update(object);
                    if (importFromRepository.isEnabled()) {
                        menu.add(importFromRepository);
                    }
                }
            }

        }
    }

}
