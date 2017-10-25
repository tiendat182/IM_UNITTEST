package com.viettel.web.errorcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.viettel.web.common.LazySorter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Created by thiendn1 on 20/4/2016.
 */
public class BusinessCodeLazyModel extends LazyDataModel<BusinessCode> {


    private List<BusinessCode> datasource;

    public BusinessCodeLazyModel(List<BusinessCode> datasource) {
        this.datasource = datasource;
    }

    @Override
    public BusinessCode getRowData(String rowKey) {
        for (BusinessCode bussinessCode : datasource) {
            if (bussinessCode.getId().equals(rowKey))
                return bussinessCode;
        }

        return null;
    }

    @Override
    public Object getRowKey(BusinessCode bussinessCode) {
        return bussinessCode.getId();
    }

    @Override
    public List<BusinessCode> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<BusinessCode> data = new ArrayList<>();
        //filter
        for (BusinessCode businessCode : datasource) {
            boolean match = true;

            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext(); ) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue =
                                (String) ReflectionTestUtils.getField(businessCode, filterProperty);
                        if (filterValue == null || fieldValue.contains(filterValue.toString())) {
                            match = true;
                        } else {
                            match = false;
                            break;
                        }
                    } catch (Exception e) {
                        match = false;
                    }
                }
            }

            if (match) {
                data.add(businessCode);
            }
        }
        //sort
        if (sortField != null) {
            Collections.sort(data, new LazySorter(sortField, sortOrder));
        }
        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);

        //paginate
        if (dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            } catch (IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        } else {
            return data;
        }
    }

}
