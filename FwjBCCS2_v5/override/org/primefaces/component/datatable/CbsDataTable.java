package org.primefaces.component.datatable;

import com.google.common.collect.Maps;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import org.primefaces.component.api.UIData;

import java.io.Serializable;
import java.util.Map;

/**
 * Override default datatable of primefaces
 * @author nhannt34
 */
public class CbsDataTable extends DataTable {

    private static final Map<Serializable, Object> defaultValues = Maps.newConcurrentMap();

    static {
        defaultValues.put("common.paging.rows.default", Integer.valueOf(GetTextFromBundleHelper.getText("common.paging.rows.default")));
        defaultValues.put(PropertyKeys.emptyMessage, GetTextFromBundleHelper.getText("common.search.not.record.found"));
        defaultValues.put(UIData.PropertyKeys.rowsPerPageTemplate, GetTextFromBundleHelper.getText("common.paging.rowsPerPageTemplate"));
        defaultValues.put(UIData.PropertyKeys.paginator, Boolean.valueOf(GetTextFromBundleHelper.getText("common.paging.paginator")));
        defaultValues.put(UIData.PropertyKeys.paginator, defaultValues.get(UIData.PropertyKeys.paginator));
        defaultValues.put(PropertyKeys.resizableColumns, Boolean.valueOf(GetTextFromBundleHelper.getText("common.paging.resizableColumns")));
        defaultValues.put(PropertyKeys.draggableColumns, Boolean.valueOf(GetTextFromBundleHelper.getText("common.paging.draggableColumns")));
        defaultValues.put(PropertyKeys.draggableRows, Boolean.valueOf(GetTextFromBundleHelper.getText("common.paging.draggableRows")));
        defaultValues.put(UIData.PropertyKeys.paginatorPosition, GetTextFromBundleHelper.getText("common.paging.paginatorPosition"));
        defaultValues.put(UIData.PropertyKeys.paginatorAlwaysVisible, Boolean.valueOf(GetTextFromBundleHelper.getText("common.paging.paginatorAlwaysVisible")));
        defaultValues.put(UIData.PropertyKeys.paginatorTemplate, GetTextFromBundleHelper.getText("common.paging.paginatorTemplate"));
        defaultValues.put(UIData.PropertyKeys.currentPageReportTemplate, GetTextFromBundleHelper.getText("common.paging.currentPageReportTemplate"));
        defaultValues.put(PropertyKeys.reflow, Boolean.TRUE);
    }

    /**
     * Can not override rows property because it belongs to internal jsf properties which are package protected
     * @return number of row per page of datatable
     */
    public int getRows() {
        int rows = super.getRows();
        return rows == 0 ? (int) defaultValues.get("common.paging.rows.default") : rows;
    }

    public String getEmptyMessage() {
        return (String) getStateHelper().eval(PropertyKeys.emptyMessage, defaultValues.get(PropertyKeys.emptyMessage));
    }

    public String getRowsPerPageTemplate() {
        return (String) getStateHelper().eval(UIData.PropertyKeys.rowsPerPageTemplate, defaultValues.get(UIData.PropertyKeys.rowsPerPageTemplate));
    }

    public boolean isPaginator() {
        return (boolean) getStateHelper().eval(UIData.PropertyKeys.paginator, defaultValues.get(UIData.PropertyKeys.paginator));
    }

    public boolean isResizableColumns() {
        return (boolean) getStateHelper().eval(PropertyKeys.resizableColumns, defaultValues.get(PropertyKeys.resizableColumns));
    }

    public boolean isDraggableColumns() {
        return (boolean) getStateHelper().eval(PropertyKeys.draggableColumns, defaultValues.get(PropertyKeys.draggableColumns));
    }

    public boolean isDraggableRows() {
        return (boolean) getStateHelper().eval(PropertyKeys.draggableRows, defaultValues.get(PropertyKeys.draggableRows));
    }

    public java.lang.String getPaginatorPosition() {
        return (String) getStateHelper().eval(UIData.PropertyKeys.paginatorPosition, defaultValues.get(UIData.PropertyKeys.paginatorPosition));
    }

    public boolean isPaginatorAlwaysVisible() {
        return (boolean) getStateHelper().eval(UIData.PropertyKeys.paginatorAlwaysVisible, defaultValues.get(UIData.PropertyKeys.paginatorAlwaysVisible));
    }

    public String getPaginatorTemplate() {
        return (String) getStateHelper().eval(UIData.PropertyKeys.paginatorTemplate, defaultValues.get(UIData.PropertyKeys.paginatorTemplate));
    }

    public String getCurrentPageReportTemplate() {
        return (String) getStateHelper().eval(UIData.PropertyKeys.currentPageReportTemplate, defaultValues.get(UIData.PropertyKeys.currentPageReportTemplate));
    }

    public boolean isReflow() {
        return (boolean) getStateHelper().eval(PropertyKeys.reflow, defaultValues.get(PropertyKeys.reflow));
    }
}
