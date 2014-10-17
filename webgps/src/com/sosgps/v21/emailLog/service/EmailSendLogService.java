package com.sosgps.v21.emailLog.service;

import com.sosgps.v21.model.EmailSendLog;
import com.sosgps.wzt.manage.util.Page;


public interface EmailSendLogService {
    
    public Page<EmailSendLog> listEmailLogs(Long startDateL, Long endDateL, String entCode, String entName, String contactName, int pageNo, int pageSize);
    public void deleteEmailLogs(Long[] ids);
    public Page<EmailSendLog> listEmailLogs(Long startDateL, Long endDateL, String entCode, String entName, String contactName);
    

}
