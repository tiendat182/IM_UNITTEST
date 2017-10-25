package com.viettel.bccs.im1.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.im1.model.QStaffIm1;
import com.viettel.bccs.im1.model.StaffIm1;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.List;

public class StaffIm1RepoImpl implements StaffIm1RepoCustom {

    public static final Logger logger = Logger.getLogger(StaffIm1RepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIm1;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStaffIm1 staff = QStaffIm1.staff;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                StaffIm1.COLUMNS column = StaffIm1.COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ADDRESS:
                        expression = DbUtil.createStringExpression(staff.address, filter);
                        break;
                    case AREACODE:
                        expression = DbUtil.createStringExpression(staff.areaCode, filter);
                        break;
                    case BANKPLUSMOBILE:
                        expression = DbUtil.createStringExpression(staff.bankplusMobile, filter);
                        break;
                    case BIRTHDAY:
                        expression = DbUtil.createDateExpression(staff.birthday, filter);
                        break;
                    case BUSINESSLICENCE:
                        expression = DbUtil.createStringExpression(staff.businessLicence, filter);
                        break;
                    case BUSINESSMETHOD:
                        expression = DbUtil.createLongExpression(staff.businessMethod, filter);
                        break;
                    case CHANNELTYPEID:
                        expression = DbUtil.createLongExpression(staff.channelTypeId, filter);
                        break;
                    case CONTRACTFROMDATE:
                        expression = DbUtil.createDateExpression(staff.contractFromDate, filter);
                        break;
                    case CONTRACTMETHOD:
                        expression = DbUtil.createLongExpression(staff.contractMethod, filter);
                        break;
                    case CONTRACTNO:
                        expression = DbUtil.createStringExpression(staff.contractNo, filter);
                        break;
                    case CONTRACTTODATE:
                        expression = DbUtil.createDateExpression(staff.contractToDate, filter);
                        break;
                    case DEPOSITVALUE:
                        expression = DbUtil.createLongExpression(staff.depositValue, filter);
                        break;
                    case DISCOUNTPOLICY:
                        expression = DbUtil.createStringExpression(staff.discountPolicy, filter);
                        break;
                    case DISTRICT:
                        expression = DbUtil.createStringExpression(staff.district, filter);
                        break;
                    case EMAIL:
                        expression = DbUtil.createStringExpression(staff.email, filter);
                        break;

                    case FILENAME:
                        expression = DbUtil.createStringExpression(staff.fileName, filter);
                        break;
                    case HASEQUIPMENT:
                        expression = DbUtil.createLongExpression(staff.hasEquipment, filter);
                        break;
                    case HASTIN:
                        expression = DbUtil.createLongExpression(staff.hasTin, filter);
                        break;
                    case HOME:
                        expression = DbUtil.createStringExpression(staff.home, filter);
                        break;
                    case IDISSUEDATE:
                        expression = DbUtil.createDateExpression(staff.idIssueDate, filter);
                        break;
                    case IDISSUEPLACE:
                        expression = DbUtil.createStringExpression(staff.idIssuePlace, filter);
                        break;
                    case IDNO:
                        expression = DbUtil.createStringExpression(staff.idNo, filter);
                        break;
                    case ISDN:
                        expression = DbUtil.createStringExpression(staff.isdn, filter);
                        break;
                    case LASTLOCKTIME:
                        expression = DbUtil.createDateExpression(staff.lastLockTime, filter);
                        break;
                    case LASTMODIFIED:
                        expression = DbUtil.createDateExpression(staff.lastModified, filter);
                        break;
                    case LOCKSTATUS:
                        expression = DbUtil.createLongExpression(staff.lockStatus, filter);
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(staff.name, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(staff.note, filter);
                        break;
                    case PIN:
                        expression = DbUtil.createStringExpression(staff.pin, filter);
                        break;
                    case POINTOFSALE:
                        expression = DbUtil.createStringExpression(staff.pointOfSale, filter);
                        break;
                    case POINTOFSALETYPE:
                        expression = DbUtil.createLongExpression(staff.pointOfSaleType, filter);
                        break;
                    case PRECINCT:
                        expression = DbUtil.createStringExpression(staff.precinct, filter);
                        break;
                    case PRICEPOLICY:
                        expression = DbUtil.createStringExpression(staff.pricePolicy, filter);
                        break;
                    case PROVINCE:
                        expression = DbUtil.createStringExpression(staff.province, filter);
                        break;
                    case SERIAL:
                        expression = DbUtil.createStringExpression(staff.serial, filter);
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(staff.shopId, filter);
                        break;
                    case SHOPOWNERID:
                        expression = DbUtil.createLongExpression(staff.shopOwnerId, filter);
                        break;
                    case STAFFCODE:
                        expression = DbUtil.createStringExpression(staff.staffCode, filter);
                        break;
                    case STAFFID:
                        expression = DbUtil.createLongExpression(staff.staffId, filter);
                        break;
                    case STAFFOWNTYPE:
                        expression = DbUtil.createStringExpression(staff.staffOwnType, filter);
                        break;
                    case STAFFOWNERID:
                        expression = DbUtil.createLongExpression(staff.staffOwnerId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(staff.status, filter);
                        break;
                    case STOCKNUM:
                        expression = DbUtil.createLongExpression(staff.stockNum, filter);
                        break;
                    case STOCKNUMIMP:
                        expression = DbUtil.createLongExpression(staff.stockNumImp, filter);
                        break;
                    case STREET:
                        expression = DbUtil.createStringExpression(staff.street, filter);
                        break;
                    case STREETBLOCK:
                        expression = DbUtil.createStringExpression(staff.streetBlock, filter);
                        break;
                    case SUBOWNERID:
                        expression = DbUtil.createLongExpression(staff.subOwnerId, filter);
                        break;
                    case SUBOWNERTYPE:
                        expression = DbUtil.createLongExpression(staff.subOwnerType, filter);
                        break;
                    case TEL:
                        expression = DbUtil.createStringExpression(staff.tel, filter);
                        break;
                    case TIN:
                        expression = DbUtil.createStringExpression(staff.tin, filter);
                        break;
                    case TTNSCODE:
                        expression = DbUtil.createStringExpression(staff.ttnsCode, filter);
                        break;
                    case TYPE:
                        expression = DbUtil.createLongExpression(staff.type, filter);
                        break;
                    case USERID:
                        expression = DbUtil.createLongExpression(staff.userId, filter);
                        break;
                    case EXCLUSE_ID_LIST:
                        break;
                }
                if (expression != null) {
                    result = result.and(expression);
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        logger.info("Result Predicate :: " + (result != null ? result.toString() : ""));
        logger.info("Exiting predicates");
        return result;
    }

    @Override
    public StaffIm1 findOneStaff(Long staffId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from bccs_im.staff where staff_id = #staffId");
        Query query = emIm1.createNativeQuery(sql.toString(), StaffIm1.class);
        query.setParameter("staffId", staffId);
        List<StaffIm1> lstResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lstResult)) {
            return lstResult.get(0);
        }
        return null;
    }

    @Override
    public String getTransCode(String prefixTransCode, String transType, StaffDTO staffDTO) throws Exception {
        if (staffDTO == null) {
            return "";
        }
        String transCode = "";
        Integer index = staffDTO.getStaffCode().indexOf("_");
        String staffCode = staffDTO.getStaffCode();
        if (index != null && index > 0) {
            staffCode = staffDTO.getStaffCode().substring(0, staffDTO.getStaffCode().indexOf("_"));

        }
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT ");
        if (Const.STOCK_TRANS_TYPE.EXPORT.equals(transType) || Const.STOCK_TRANS_TYPE.ISDN.equals(transType)) {
            strQuery.append(" ltrim(to_char(mod(NVL(STOCK_NUM,0)+1,1000000),'000000')) AS transCode ");
        } else {
            strQuery.append(" ltrim(to_char(mod(NVL(STOCK_NUM_IMP,0)+1,1000000),'000000')) AS transCode ");
        }
        strQuery.append(" FROM bccs_im.staff where staff_id = #staffId  ");
        strQuery.append("   AND status= #status  ");
        Query query = emIm1.createNativeQuery(strQuery.toString());
        query.setParameter("staffId", staffDTO.getStaffId());
        query.setParameter("status", Const.STATUS.ACTIVE);
        List<String> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            transCode = DataUtil.safeToString(prefixTransCode) + "_" + DataUtil.safeToString(staffDTO.getShopCode()).toUpperCase() + "_" + DataUtil.safeToString(staffCode).toUpperCase() + "_" + DataUtil.safeToString(lst.get(0));
            if (transCode != null && transCode.length() > 50) {
                transCode = transCode.substring(0, 49);
            }
        }
        return transCode;
    }

    @Override
    public String getTransCodeDeposit(String prefixTransCode, StaffDTO staffDTO) throws Exception {
        if (staffDTO == null) {
            return "";
        }
        String transCode = "";
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT ");
        strQuery.append(" ltrim(to_char(mod(NVL(STOCK_NUM_IMP,0)+1,1000000),'000')) AS transCode ");
        strQuery.append(" FROM bccs_im.staff where staff_id=#staffId  ");
        strQuery.append("   AND status=#status  ");
        Query query = emIm1.createNativeQuery(strQuery.toString());
        query.setParameter("staffId", staffDTO.getStaffId());
        query.setParameter("status", Const.STATUS.ACTIVE);
        List<String> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            Calendar cal = Calendar.getInstance();
            String year = DataUtil.safeToString(cal.get(Calendar.YEAR));
            year = year.substring(year.length() - 2);
            int month = cal.get(Calendar.MONTH) + 1;
            String monthYear;
            if (month < 10) {
                monthYear = year + "0" + DataUtil.safeToString(month);
            } else {
                monthYear = year + DataUtil.safeToString(month);
            }

            transCode = DataUtil.safeToString(prefixTransCode) + "_" + DataUtil.safeToString(staffDTO.getShopCode()).toUpperCase() + "_" + monthYear.toUpperCase() + DataUtil.safeToString(lst.get(0));
            if (transCode != null && transCode.length() > 50) {
                transCode = transCode.substring(0, 49);
            }
        }
        return transCode;
    }

    @Override
    public String getTransCodeByStaffId(Long staffId) throws Exception {
        String transCode = "";
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT ltrim(to_char(mod( NVL(STOCK_NUM_IMP,0)+1, 1000000),'000000')) AS transCode FROM bccs_im.Staff where staff_id=#staffId");
        Query query = emIm1.createNativeQuery(strQuery.toString());
        query.setParameter("staffId", staffId);
        List<String> lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            transCode = DataUtil.safeToString(lst.get(0));
            if (transCode != null && transCode.length() > 50) {
                transCode = transCode.substring(0, 49);
            }
        }
        return transCode;
    }

    @Override
    public void increaseStockNum(Connection conn, Long staffId, String column) throws Exception {
        PreparedStatement ps = null;
        try {
            StringBuilder builder = new StringBuilder("update bccs_im.staff set ")
                    .append(column).append("=").append(column).append(" +1")
                    .append(" where staff_id=?");
            ps = conn.prepareCall(builder.toString());
            ps.setLong(1, staffId);
            ps.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }
}