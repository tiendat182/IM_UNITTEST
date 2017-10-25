package com.viettel.bccs.im1.service;

import com.viettel.bccs.im1.dto.ShopIm1DTO;
import com.viettel.bccs.im1.model.ShopIm1;
import com.viettel.bccs.im1.repo.ShopIm1Repo;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import java.util.List;

@Service
public class ShopIm1ServiceImpl extends BaseServiceImpl implements ShopIm1Service {

    private final BaseMapper<ShopIm1, ShopIm1DTO> mapper = new BaseMapper(ShopIm1.class, ShopIm1DTO.class);

    @Autowired
    private ShopIm1Repo repository;
    public static final Logger logger = Logger.getLogger(ShopIm1Service.class);

    @WebMethod
    public ShopIm1DTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ShopIm1DTO save(ShopIm1DTO shopDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(shopDTO)));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteStock(List<Long> lstShopId) throws Exception {
        repository.deleteStock(lstShopId);
    }
}
