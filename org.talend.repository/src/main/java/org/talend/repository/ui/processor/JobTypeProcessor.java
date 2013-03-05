package org.talend.repository.ui.processor;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;

/**
 * bqian TypeProcessor for Job. <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class JobTypeProcessor extends SingleTypeProcessor {

    private List<String> jobIDList;

    /**
     * ggu JobTypeProcessor constructor comment.
     */
    public JobTypeProcessor(String curJobId) {
        super(curJobId);
    }

    @Override
    protected ERepositoryObjectType getType() {
        return ERepositoryObjectType.PROCESS;
    }

    public List<String> getJobIDList() {
        return jobIDList;
    }

    public void setJobIDList(List<String> jobIDList) {
        this.jobIDList = jobIDList;
    }

    @Override
    public boolean isSelectionValid(RepositoryNode node) {
        ERepositoryObjectType t = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        List<String> idList = getJobIDList();
        if (idList != null && t == ERepositoryObjectType.PROCESS) {
            if (idList.contains(node.getObject().getId())) {
                MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
                        Messages.getString("RepositoryReviewDialog.waringTitle"), //$NON-NLS-1$
                        Messages.getString("RepositoryReviewDialog.waringMessages")); //$NON-NLS-1$
                return false;
            }

        }
        if (node.getProperties(EProperties.CONTENT_TYPE) == getType()) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean selectRepositoryNode(Viewer viewer, RepositoryNode parentNode, RepositoryNode node) {
        if (getRepositoryType() != null && node.getObject() != null) {
            if (node.getObject().getId() == null || node.getObject().getId().equals(getRepositoryType())) {
                return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.dialog.ITypeProcessor#getDialogTitle()
     */
    @Override
    public String getDialogTitle() {
        return Messages.getString("JobTypeProcessor.jobSelectionDialog.findJob"); //$NON-NLS-1$
    }
}