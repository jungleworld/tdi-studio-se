// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui;

import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.ProcessEditorInput;
import org.talend.designer.core.ui.editor.TalendEditor;

/**
 * This class is the main editor, the differents pages in it are: <br/><b>1)</b> {@link TalendEditor} <br/><b>2)</b>
 * {@link Text Editor on the generated code} <br/><br/> This class uses the interface ISelectionListener, it allows to
 * propage the Delete evenement to the designer. <br/>
 * 
 * $Id$
 * 
 */
public class MultiPageTalendEditor extends AbstractMultiPageTalendEditor {

    public static final String ID = "org.talend.designer.core.ui.MultiPageTalendEditor"; //$NON-NLS-1$

    public MultiPageTalendEditor() {
        super();
        designerEditor = new TalendEditor();
    }

    /**
     * Creates the pages of the multi-page editor.
     */
    @Override
    protected void createPages() {
        super.createPages();
        setTitleImage(ImageProvider.getImage(ECoreImage.PROCESS_ICON));
    }

    /**
     * The <code>MultiPageEditorExample</code> implementation of this method checks that the input is an instance of
     * <code>IFileEditorInput</code>.
     */
    @Override
    public void init(final IEditorSite site, final IEditorInput editorInput) throws PartInitException {
        if (!(editorInput instanceof IFileEditorInput) && !(editorInput instanceof ProcessEditorInput)) {
            throw new PartInitException(Messages.getString("MultiPageTalendEditor.InvalidInput")); //$NON-NLS-1$
        }
        super.init(site, editorInput);
    }

    /**
     * DOC smallet Comment method "setName".
     * 
     * @param label
     */
    public void setName() {
        super.setName();
        String label = getEditorInput().getName();
        // if (getActivePage() == 1) {
        setPartName(Messages.getString("MultiPageTalendEditor.Job", label)); //$NON-NLS-1$
        // } else {
        // setPartName(Messages.getString("other Label??", label));
        // //$NON-NLS-1$
        // }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.AbstractMultiPageTalendEditor#getEditorId()
     */
    @Override
    public String getEditorId() {
        return ID;
    }

}
