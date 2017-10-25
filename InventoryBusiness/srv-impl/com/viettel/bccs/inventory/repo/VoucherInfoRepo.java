package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.VoucherInfo;
import com.viettel.fw.persistence.BaseRepository;

/**
 * @author HoangAnh
 */
public interface VoucherInfoRepo extends BaseRepository<VoucherInfo, Long>, VoucherInfoRepoCustom {

}