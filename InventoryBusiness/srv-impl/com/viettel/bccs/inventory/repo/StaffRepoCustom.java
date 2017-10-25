package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.sql.Connection;
import java.util.List;


public interface StaffRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    /**
     * lay staffDto theo staff code
     *
     * @param staffCode
     * @return StaffDTO
     * @throws Exception
     * @author thanhnt77
     */
    public StaffDTO getStaffByStaffCode(String staffCode) throws Exception;

    /**
     * @param staffOwnerId
     * @return
     * @throws Exception
     * @author minhvh1
     */
    public List<Staff> getStaffList(Long staffOwnerId) throws Exception;

    public List<Staff> findByStaffCodeAndStatusActive(String staffCode) throws Exception;

    /*

     * @author ThanhNT77
     * Hàm lấy ra số phiếu xuất/nhập tự động
     * Đầu vào : Tiền tố,loại phiếu
     * TransType 1 :Phiếu xuất,2 :Phiếu nhập
     */
    public String getTransCode(String prefixTransCode, String transType, StaffDTO staffDTO) throws Exception;

    /**
     * @param staffId
     * @param shopId
     * @return
     * @throws Exception
     */
    public boolean validateConstraints(Long staffId, Long shopId) throws Exception;


    /**
     * @param staffId
     * @return tra lai prefix duong template
     * @throws Exception
     * @author LuanNT23
     */
    public String getStockReportTemplate(Long staffId) throws Exception;

    /**
     * @return
     * @throws Exception
     * @author LuanNT23
     */
    public List<Long> getListChannelTypeForStaff(Long vtUnit) throws Exception;

    /*

         * @author Hoangnt14
         * Hàm lấy ra số phiếu thu/chi tự động
         * Đầu vào : Tiền tố,loại phiếu
         * TransType 1 :Phiếu thu,2 :Phiếu chi
         */
    public String getTransCodeDeposit(String prefixTransCode, StaffDTO staffDTO) throws Exception;

    public String getTransCodeByStaffId(Long staffId) throws Exception;

    public void increaseStockNum(Connection conn, Long staffId, String collumn) throws Exception;
}