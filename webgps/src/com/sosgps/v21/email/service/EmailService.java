package com.sosgps.v21.email.service;

import java.util.List;

import com.sosgps.v21.model.Email;

public interface EmailService {
	public void saveEmails(List<Email> emailList);

	public List<Email> getEmails(String entCode);

	public void removeEmails(String entCode);
}
