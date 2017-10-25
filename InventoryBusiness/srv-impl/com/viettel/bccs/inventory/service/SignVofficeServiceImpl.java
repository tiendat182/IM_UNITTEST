package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.bccs.inventory.model.StockTransVoffice;
import com.viettel.bccs.inventory.repo.StockTransVofficeRepo;
import com.viettel.bccs.inventory.service.BaseStock.BaseVofficeService;
import com.viettel.bccs.inventory.service.BaseStock.VofficeFactory;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by hoangnt14 on 12/5/2016.
 */
@Service
public class SignVofficeServiceImpl extends BaseServiceImpl implements SignVofficeService {

    public static final Logger logger = Logger.getLogger(SignVofficeServiceImpl.class);

    private final BaseMapper<StockTransVoffice, StockTransVofficeDTO> mapper = new BaseMapper(StockTransVoffice.class, StockTransVofficeDTO.class);

    @Autowired
    private VofficeFactory vofficeFactory;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private StockTransVofficeRepo repository;

    @Override
    public void signVoffice(String mode, String prefixTemplate, StockTransVofficeDTO stockTransVofficeDTO) throws LogicException, Exception {
        StockTransVofficeDTO stockTransVofficeSave = DataUtil.cloneBean(stockTransVofficeDTO);
        try {
            BaseVofficeService baseVofficeService = vofficeFactory.getInstance(mode, prefixTemplate);
            Long result = baseVofficeService.doSignVOffice(stockTransVofficeSave);
            if (DataUtil.safeEqual(result, 1L)) {
                stockTransVofficeSave.setStatus(Const.STOCK_TRANS_VOFFICE.V_SIGNING.toString());
            } else {
                stockTransVofficeSave.setRetry(stockTransVofficeSave.getRetry() == null ? 0L : stockTransVofficeSave.getRetry() + 1L);
                stockTransVofficeSave.setStatus(Const.STOCK_TRANS_VOFFICE.V_VALIDATE_ERROR.toString());
                stockTransVofficeSave.setResponseCode(DataUtil.safeToString(result));
                stockTransVofficeSave.setResponseCodeDescription(GetTextFromBundleHelper.getText("sign.state" + result));
            }

        } catch (LogicException le) {
            logger.error(le.getMessage(), le);
            stockTransVofficeSave.setErrorCode(le.getErrorCode());
            stockTransVofficeSave.setErrorCodeDescription(le.getMessage());
            stockTransVofficeSave.setStatus(Const.STOCK_TRANS_VOFFICE.V_VALIDATE_ERROR.toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            stockTransVofficeSave.setErrorCode("999");
            stockTransVofficeSave.setErrorCodeDescription(GetTextFromBundleHelper.getText("sign.state.error"));
            stockTransVofficeSave.setStatus(Const.STOCK_TRANS_VOFFICE.V_ERROR.toString());
            stockTransVofficeSave.setRetry(stockTransVofficeDTO.getRetry() == null ? 0L : stockTransVofficeDTO.getRetry() + 1L);
        }
        stockTransVofficeSave.setLastModify(getSysDate(em));
        repository.save(mapper.toPersistenceBean(stockTransVofficeSave));
    }
}
