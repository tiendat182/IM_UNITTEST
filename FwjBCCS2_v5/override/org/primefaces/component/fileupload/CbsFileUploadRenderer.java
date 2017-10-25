package org.primefaces.component.fileupload;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Created by ThanhNT77 on 15/12/2015.
 */
public class CbsFileUploadRenderer extends FileUploadRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        if (context.getExternalContext().getRequestContentType().toLowerCase().startsWith("multipart/")) {
            super.decode(context, component);
        }
    }

}
