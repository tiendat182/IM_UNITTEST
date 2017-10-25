package com.viettel.bccs.inventory.controller.infrastructure;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.BrasIppoolDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.web.common.controller.BaseController;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Date;
import java.util.List;

/**
 * Created by anhvv4 on 6/24/2015.
 */
public class ImportBrasIppoolHander extends BaseController {

    public static List<BrasIppoolDTO> getListBrasIppoolImoport (Sheet sheet, FormulaEvaluator evaluator) throws LogicException {
        long indexRow = 0;
        List<BrasIppoolDTO> memberList = Lists.newArrayList();
        List<String> headers = Lists.newArrayList("STT", "IP", "POOL NAME","DOMAIN","BRAS IP","TRẠNG THÁI","IP MARK","POOL UNIQ","NGÀY CẬP NHẬT","TỈNH/TP","IP ĐẶC THÙ","LABEL IP");
        for (Row row : sheet) {
            BrasIppoolDTO currentBrasIppool = new BrasIppoolDTO();
            if (indexRow == 0) {
                indexRow++;
                continue;
            }
            String ip = null;
            String poolName = null;
            String status = null;
            String ipMark = null;
            String poolUniq = null;
            Date dateUpdate = null;
            String province = null;
            String ipType = null;
            String labelIp = null;
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = row.getCell(i);
                if (cell != null) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    Object object = handleCell(cell.getCellType(), cell, evaluator);
                    switch (i) {
                        case 0: break;
                        case 1:
                            ip = DataUtil.safeToString(object).trim();
                            break;
                        case 2:
                            poolName = DataUtil.safeToString(object).trim();
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            status = DataUtil.safeToString(object).trim();
                            break;
                        case 6:
                            ipMark = DataUtil.safeToString(object).trim();
                            break;
                        case 7:
                            poolUniq = DataUtil.safeToString(object).trim();
                            break;
                        case 8:
                            if(!DataUtil.isNullObject(object)){
                                dateUpdate = DateUtil.string2DateByPattern(object.toString(),"dd/mm/yyyy");
                            }
                            break;
                        case 9:
                            province = DataUtil.safeToString(object).trim();
                            break;
                        case 10:
                            ipType = DataUtil.safeToString(object).trim();
                            break;
                        default:
                            labelIp = DataUtil.safeToString(object).trim();

                    }
                }
            }
            currentBrasIppool.setRowIndex(indexRow);
            currentBrasIppool.setIpPool(ip);
            currentBrasIppool.setPoolName(poolName);
//            currentBrasIppool.setDomain(domain);
//            currentBrasIppool.setBrasIp(brasIp);
            currentBrasIppool.setStatus(status);
            currentBrasIppool.setIpMask(ipMark);
            currentBrasIppool.setPoolUniq(poolUniq);
            currentBrasIppool.setUpdateDatetime(dateUpdate);
            currentBrasIppool.setProvince(province);
            currentBrasIppool.setIpType(ipType);
            currentBrasIppool.setIpLabel(labelIp);
            memberList.add(currentBrasIppool);
        }
        return memberList;
    }

    public static String getKeyMap(String key) {
        return GetTextFromBundleHelper.getText(key);
    }

    public static Object handleCell(int type, Cell cell, FormulaEvaluator evaluator) {
        if (type == HSSFCell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        } else if (type == HSSFCell.CELL_TYPE_NUMERIC) {
            return cell.getNumericCellValue();
        } else if (type == HSSFCell.CELL_TYPE_BOOLEAN) {
            return cell.getBooleanCellValue();
        } else if (type == HSSFCell.CELL_TYPE_FORMULA) {
            // Re-run based on the formula type
            evaluator.evaluateFormulaCell(cell);
            handleCell(cell.getCachedFormulaResultType(), cell, evaluator);
        }
        return null;
    }

}
