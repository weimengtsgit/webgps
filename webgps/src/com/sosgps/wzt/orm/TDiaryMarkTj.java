package com.sosgps.wzt.orm;

import java.util.Date;

@SuppressWarnings("serial")
public class TDiaryMarkTj extends AbstractTDiaryMarkTj implements java.io.Serializable {

    /** default constructor */
    public TDiaryMarkTj() {
    }

    /** full constructor */
    public TDiaryMarkTj(Long id, Date tjDate, String deviceId, String entCode, Long userId, 
			Date diaryDate, Double arrivalRate) {
        super(id, tjDate, deviceId, entCode, userId, 
    			diaryDate, arrivalRate);
    }
}
