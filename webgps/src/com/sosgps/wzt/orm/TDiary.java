package com.sosgps.wzt.orm;

import java.util.Date;

@SuppressWarnings("serial")
public class TDiary extends AbstractTDiary implements java.io.Serializable {

    /** default constructor */
    public TDiary() {
    }

    /** full constructor */
    public TDiary(Long id, String termName, String title, String content, String remark, Date createDate, Date modifyDate, Long userId, String entCode, Date diaryDate, Date remarkDate, String deviceId) {
        super(id, termName, title, content, remark, createDate, modifyDate, userId, entCode, diaryDate, remarkDate, deviceId);
    }
}
