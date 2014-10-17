package com.sosgps.wzt.orm;

import java.util.Date;

@SuppressWarnings("serial")
public class TDiaryMark extends AbstractTDiaryMark implements java.io.Serializable {

    /** default constructor */
    public TDiaryMark() {
    }

    /** full constructor */
    public TDiaryMark(Long id, Long diaryId, String deviceId, Double longitude, Double latitude,
			Date createDate, Date changeDate, Long userId, String entCode, Date diaryDate, Long isArrive,
			Date tjDate, Long distance) {
        super(id, diaryId, deviceId, longitude, latitude, createDate, changeDate, userId, entCode, diaryDate, isArrive,
    			tjDate, distance);
    }
}
