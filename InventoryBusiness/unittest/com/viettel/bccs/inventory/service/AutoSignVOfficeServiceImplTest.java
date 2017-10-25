package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.SignatureImageListObjDTO;
import com.viettel.bccs.inventory.dto.SignatureImageObjDTO;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.ws.common.utils.WebServiceHandler;
import com.viettel.ws.common.utils.WsRequestCreator;
import com.viettel.ws.provider.WsCallerFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.jws.WebService;
import java.util.List;
import java.util.ResourceBundle;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by TruNV on 9/9/2017.
 * Modify by DatLT on 27/9/2017 : start with testGetSignStatus_1
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BaseStockService.class, AutoSignVOfficeServiceImpl.class, WebServiceHandler.class, WebService.class})
@PowerMockIgnore({"javax.management.*", "org.apache.http.conn.ssl.*", "javax.net.ssl.*" , "javax.crypto.*"})
public class AutoSignVOfficeServiceImplTest {

    @InjectMocks
    private AutoSignVOfficeServiceImpl autoSignVOfficeService;

    @Mock
    private WsCallerFactory wsCallerFactory;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for autoSignVOfficeService.checkAccount method ------------ **/
    /*** --------------------------------------------------------------------**/
    @Test
    public void testCheckAccount_1() throws Exception {
        WsRequestCreator wsConfig = new WsRequestCreator();
        wsConfig.setWsAddress("2");
        Mockito.doReturn(wsConfig).when(wsCallerFactory).createWsCaller(AutoSignVOfficeService.class, Const.SERVICE_VOFFICE.CHECK_ACCOUNT, true );
        PowerMockito.mockStatic(WebServiceHandler.class);
        when(WebServiceHandler.webServiceCaller(any())).thenReturn("1");
        when(WebServiceHandler.wsServiceHandler(any(), any())).thenReturn("1");
        autoSignVOfficeService.checkAccount("UserNam", "Password");
    }

    @Test
    public void testCheckAccount_2() throws Exception {
        WsRequestCreator wsConfig = new WsRequestCreator();
        wsConfig.setUsername("dat");
        wsConfig.setPassword("123");
        Mockito.doReturn(wsConfig).when(wsCallerFactory).createWsCaller(AutoSignVOfficeService.class, Const.SERVICE_VOFFICE.CHECK_ACCOUNT, true );

        PowerMockito.mockStatic(WebServiceHandler.class);
        when(WebServiceHandler.webServiceCaller(any())).thenReturn("1");
        when(WebServiceHandler.wsServiceHandler(any(), any())).thenReturn("1");
        autoSignVOfficeService.checkAccount("UserNam", "Password");
    }

    @Test
    public void testCheckAccount_3() throws Exception {
        WsRequestCreator wsConfig = new WsRequestCreator();
        wsConfig.setUsername("dat");
        wsConfig.setPassword("123");
        Mockito.doReturn(wsConfig).when(wsCallerFactory).createWsCaller(AutoSignVOfficeService.class, Const.SERVICE_VOFFICE.CHECK_ACCOUNT, true );
        PowerMockito.mockStatic(WebServiceHandler.class);
        when(WebServiceHandler.webServiceCaller(any())).thenReturn(null);
        when(WebServiceHandler.wsServiceHandler(any(), any())).thenReturn("1");
        autoSignVOfficeService.checkAccount("UserNam", "Password");
    }

    @Test(expected = Exception.class)
    public void testCheckAccount_4() throws Exception {
        WsRequestCreator wsConfig = new WsRequestCreator();
        wsConfig.setWsAddress("2");
        Mockito.doReturn(wsConfig).when(wsCallerFactory).createWsCaller(AutoSignVOfficeService.class, Const.SERVICE_VOFFICE.CHECK_ACCOUNT, true );
        PowerMockito.mockStatic(WebServiceHandler.class);
        when(WebServiceHandler.webServiceCaller(any())).thenThrow(new Exception());
        Assert.assertFalse(autoSignVOfficeService.checkAccount("UserNam", "Password"));
    }

    /** -------------------------------------------------------------------- **/
    /** --- Test for autoSignVOfficeService.getSignStatus method ----------- **/
    /*** --------------------------------------------------------------------**/
    @Test(expected = Exception.class)
    public void testGetSignStatus_1() throws Exception {
        WsRequestCreator wsConfig = new WsRequestCreator();
        wsConfig.setUsername("dat");
        wsConfig.setPassword("123");
        String transCode = "transCode";
        when(wsCallerFactory.createWsCaller(anyString(),anyString(),anyString())).thenReturn(wsConfig);
        PowerMockito.mockStatic(WebServiceHandler.class);
        when(WebServiceHandler.wsServiceHandler(any(), any())).thenReturn("1");
        ResourceBundle resource = PowerMockito.mock(ResourceBundle.class);
        PowerMockito.mockStatic(ResourceBundle.class);
        when(ResourceBundle.getBundle("spring")).thenReturn(resource);
        Mockito.when(resource.getString("voffice.ws.appCode")).thenReturn("ENC(01234567890123456789012345678901)");
        autoSignVOfficeService.getSignStatus(transCode);
    }

    @Test
    public void testGetSignStatus_2() throws Exception {
        WsRequestCreator wsConfig = new WsRequestCreator();
        wsConfig.setUsername("dat");
        wsConfig.setPassword("123");
        String transCode = "transCode";
        SignatureImageListObjDTO lst= new SignatureImageListObjDTO();
        when(wsCallerFactory.createWsCaller(anyString(),anyString(),anyString())).thenReturn(wsConfig);
        PowerMockito.mockStatic(WebServiceHandler.class);
        when(WebServiceHandler.wsServiceHandler(any(), any())).thenReturn(lst);
        when(WebServiceHandler.webServiceCaller(any())).thenReturn("response");
        ResourceBundle resource = PowerMockito.mock(ResourceBundle.class);
        Mockito.when(resource.getString("voffice.ws.appCode")).thenReturn(null);
        autoSignVOfficeService.getSignStatus(transCode);
    }

    @Test
    public void testGetSignStatus_3() throws Exception {
        SignatureImageObjDTO signatureImageObjDTO = new SignatureImageObjDTO();
        signatureImageObjDTO.setEmail("bccs@viettel.vn");
        List<SignatureImageObjDTO> lsOffice = Lists.newArrayList(signatureImageObjDTO);
        WsRequestCreator wsConfig = new WsRequestCreator();
        wsConfig.setUsername("dat");
        wsConfig.setPassword("123");
        String transCode = "transCode";
        SignatureImageListObjDTO lst= new SignatureImageListObjDTO();
        lst.setLstSignatureImageObj(lsOffice);
        when(wsCallerFactory.createWsCaller(anyString(),anyString(),anyString())).thenReturn(wsConfig);
        PowerMockito.mockStatic(WebServiceHandler.class);
        when(WebServiceHandler.wsServiceHandler(any(), any())).thenReturn(lst);
        when(WebServiceHandler.webServiceCaller(any())).thenReturn("1");
        ResourceBundle resource = PowerMockito.mock(ResourceBundle.class);
        Mockito.when(resource.getString("voffice.ws.appCode")).thenReturn(null);
        autoSignVOfficeService.getSignStatus(transCode);
    }

    @Test(expected = LogicException.class)
    public void testGetSignStatus_4() throws Exception {
        WsRequestCreator wsConfig = new WsRequestCreator();
        wsConfig.setUsername("dat");
        wsConfig.setPassword("123");
        String transCode = "transCode";
        when(wsCallerFactory.createWsCaller(anyString(),anyString(),anyString())).thenThrow(new LogicException());
        PowerMockito.mockStatic(WebServiceHandler.class);
        when(WebServiceHandler.wsServiceHandler(any(), any())).thenThrow(new LogicException());
        ResourceBundle resource = PowerMockito.mock(ResourceBundle.class);
        Mockito.when(resource.getString("voffice.ws.appCode")).thenReturn("ENC(darius.properties)");
        autoSignVOfficeService.getSignStatus(transCode);
    }
}