package com.sosgps.v21.dao;

import java.util.List;

import com.sosgps.v21.model.Email;

public interface EmailDao {
	public void saveEmails(Email email);

	public List<Email> getEmails(String entCode);

	public void removeEmails(String entCode);
}