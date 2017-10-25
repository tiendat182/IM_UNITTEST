package com.viettel.web.log;

import com.viettel.fw.log.LogNotify;
import org.springframework.stereotype.Service;

/**
 * Created by tuyennt17 on 11/11/2015.
 */

@Service
public class ReportLogService {
    public LogNotify errorMessage(LogNotify logNotifyDTO) {
        return logNotifyDTO;
    }

    public LogNotify successMessage(LogNotify logNotifyDTO) {
        return logNotifyDTO;
    }
}
