package com.sosgps.v21.email.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sosgps.v21.dao.EmailDao;
import com.sosgps.v21.email.service.EmailService;
import com.sosgps.v21.model.Email;

public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory
			.getLogger(EmailServiceImpl.class);

	private EmailDao emailDao;

	public EmailDao getEmailDao() {
		return emailDao;
	}

	public void setEmailDao(EmailDao emailDao) {
		this.emailDao = emailDao;
	}

	public void saveEmails(List<Email> emailList) {
		for (Email email : emailList) {
			emailDao.saveEmails(email);
		}
	}

	public List<Email> getEmails(String entCode) {
		return emailDao.getEmails(entCode);
	}

	public void removeEmails(String entCode) {
		emailDao.removeEmails(entCode);
	}
}
