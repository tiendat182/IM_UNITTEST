package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.PartnerContractDTO;
import com.viettel.bccs.inventory.dto.StockTransOfflineDTO;
import com.viettel.bccs.inventory.model.*;
import com.viettel.bccs.inventory.repo.*;
import com.viettel.bccs.sale.repo.SaleTransDetailRepo;
import com.viettel.bccs.sale.repo.SaleTransRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class StockTransOfflineServiceImpl extends BaseServiceImpl implements StockTransOfflineService {

    private final BaseMapper<StockTransOffline, StockTransOfflineDTO> mapper = new BaseMapper<>(StockTransOffline.class, StockTransOfflineDTO.class);
    private final BaseMapper<PartnerContract, PartnerContractDTO> mapperPartnerContract = new BaseMapper<>(PartnerContract.class, PartnerContractDTO.class);

    @Autowired
    private StockTransOfflineRepo repository;

    @Autowired
    private StockTransRepo repoStockTrans;

    @Autowired
    private StockTransActionRepo repoStockTransAction;

    @Autowired
    private StockTransFullOfflineRepo repoStockTransFullOfflineRepo;

    @Autowired
    private StockTransSerialOfflineRepo repoStockTransSerialOffline;

    @Autowired
    private ProductOfferingRepo repoProductOffering;

    @Autowired
    private StockTransDetailRepo repoStockTransDetail;

    @Autowired
    private StaffRepo repoStaff;

    @Autowired
    private ShopRepo repoShop;

    @Autowired
    private SaleTransRepo repoSaleTransRepo;

    @Autowired
    private SaleTransDetailRepo repoSaleTransDetail;

    @Autowired
    private ChannelTypeRepo repoChannelType;

//    @Autowired
//    private AccountAgentRepo repoAccountAgentRepo;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager emSale;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.ANYPAY)
    private EntityManager emAnypay;

    public static final Logger logger = Logger.getLogger(StockTransOfflineService.class);


    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }


    public StockTransOfflineDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    public List<StockTransOfflineDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    public List<StockTransOfflineDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @Transactional(rollbackFor = Exception.class)
    public StockTransOfflineDTO save(StockTransOfflineDTO stockTransOfflineDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransOfflineDTO)));
    }

    @Transactional(rollbackFor = Exception.class)
    public void processExpStockOffine(StockTrans stockTrans) throws Exception, LogicException {
    }

    public StockTransOfflineDTO getStockTransOfflineById(Long stockTransId) throws Exception {
        List<FilterRequest> filterRequests = Lists.newArrayList(new FilterRequest(StockTransOffline.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ, stockTransId));
        List<StockTransOfflineDTO> listResult = findByFilter(filterRequests);
        if (DataUtil.isNullOrEmpty(listResult)) {
            return null;
        }
        return listResult.get(0);
    }
}
