package com.viettel.bccs.inventory.repo;
import com.viettel.bccs.inventory.dto.ReasonDTO;
import com.viettel.bccs.inventory.dto.RevokeKitDetailDTO;
import com.viettel.bccs.inventory.dto.RevokeKitFileImportDTO;
import com.viettel.bccs.sale.dto.SubscriberDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.Date;
import java.util.List;
import com.viettel.bccs.inventory.model.RevokeKitDetail;
public interface RevokeKitDetailRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 /**
  * ham tra cuu thu hoi kit theo shop
  * @author thanhnt77
  * @param shopId
  * @param fromDate
  * @param toDate
  * @return
  * @throws LogicException
     * @throws Exception
     */
 public List<RevokeKitDetailDTO> searchRevokeKitDetailByShopAndDate(Long shopId, Date fromDate, Date toDate) throws LogicException, Exception;


 /**
  * ham tim kiem kit thu hoi theo serial va isdn
  * @author thanhnt77
  * @param serial
  * @param isdn
  * @return
  * @throws LogicException
     * @throws Exception
     */
 public List<RevokeKitDetailDTO> findRevokeDetailBySerialAndIsdn(String serial, String isdn) throws LogicException, Exception;

 /**
  * ham xu ly tra cuu serial, isdn theo list
  * @author thanhnt77
  * @param lsRevokeSearch
  * @return
  * @throws LogicException
  * @throws Exception
     */
 public List<RevokeKitDetailDTO> findRevokeDetailByListSearch(List<RevokeKitFileImportDTO> lsRevokeSearch) throws LogicException, Exception;

 List<SubscriberDTO> findSubsToRevokeBySerialAndIsdnWithReason(String isdn, String serial) throws LogicException, Exception;

 List<SubscriberDTO> findSubsToRevokeBySerialAndIsdn(String isdn, String serial) throws LogicException, Exception;

 ReasonDTO findReasonWithReasonId(String reasonId) throws LogicException, Exception;

 Long findProductOfferCodeToRevokeKit() throws LogicException, Exception;
}