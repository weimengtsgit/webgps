/**
 * 
 */
package com.sosgps.wzt.system.service.impl;

import java.util.List;

import com.sosgps.wzt.orm.RefEntCallednumber;
import com.sosgps.wzt.orm.TEnt;
import com.sosgps.wzt.orm.TEntDAO;
import com.sosgps.wzt.system.dao.RefEntCalledNumberDao;
import com.sosgps.wzt.system.service.RefEntCalledNumberService;

/**
 * @author xiaojun.luan
 *
 */
public class RefEntCalledNumnberServiceImpl implements
		RefEntCalledNumberService {

	public void deleteAll(String[] ids) {
		// TODO Auto-generated method stub
		for(int i=0;i<ids.length;i++){
			Long id=Long.valueOf(ids[i]);
			refEntCalledNumberDao.delete(refEntCalledNumberDao.findById(id));
		}
	}

	/* (non-Javadoc)
	 * @see com.sosgps.wzt.system.service.RefEntCalledNumberService#delete(com.sosgps.wzt.orm.RefEntCallednumber)
	 */
	public void delete(RefEntCallednumber refEntCallednumber) {
		// TODO Auto-generated method stub
		refEntCalledNumberDao.delete(refEntCallednumber);
	}

	/* (non-Javadoc)
	 * @see com.sosgps.wzt.system.service.RefEntCalledNumberService#deleteById(java.lang.Long)
	 */
	public void deleteByCallednumber(String calledNumber) {
		
		RefEntCallednumber refEntCalledNumber=refEntCalledNumberDao.findByCallednumber(calledNumber);
		if(refEntCalledNumber!=null){
			refEntCalledNumberDao.delete(refEntCalledNumber);
		}
	}
	public void save(String entCode,String callednumber){
		TEnt tEnt=tEntDAO.findById(entCode);
		RefEntCallednumber refEntCallednumber=new RefEntCallednumber();
		refEntCallednumber.setCalledNumber(callednumber);
		refEntCallednumber.setTEnt(tEnt);
		save(refEntCallednumber);
	}

	/* (non-Javadoc)
	 * @see com.sosgps.wzt.system.service.RefEntCalledNumberService#findByCallednumber(java.lang.String)
	 */
	public RefEntCallednumber findByCallednumber(String callednumber) {
		return refEntCalledNumberDao.findByCallednumber(callednumber);	
	}
	public RefEntCallednumber findById(Long id){
		return refEntCalledNumberDao.findById(id);
	}
	public List findByEntCode(String entCode){
		TEnt tEnt=tEntDAO.findById(entCode);
		return refEntCalledNumberDao.findByEntCode(tEnt);
	}

	/* (non-Javadoc)
	 * @see com.sosgps.wzt.system.service.RefEntCalledNumberService#save(com.sosgps.wzt.orm.RefEntCallednumber)
	 */
	public void save(RefEntCallednumber refEntCallednumber) {
		// TODO Auto-generated method stub
		refEntCalledNumberDao.save(refEntCallednumber);
	}

	/* (non-Javadoc)
	 * @see com.sosgps.wzt.system.service.RefEntCalledNumberService#update(com.sosgps.wzt.orm.RefEntCallednumber)
	 */
	public void update(RefEntCallednumber refEntCallednumber) {
		// TODO Auto-generated method stub
		refEntCalledNumberDao.update(refEntCallednumber);
	}

	private RefEntCalledNumberDao refEntCalledNumberDao;
	private TEntDAO tEntDAO;
	
	public RefEntCalledNumberDao getRefEntCalledNumberDao() {
		return refEntCalledNumberDao;
	}

	public void setRefEntCalledNumberDao(RefEntCalledNumberDao refEntCalledNumberDao) {
		this.refEntCalledNumberDao = refEntCalledNumberDao;
	}

	public TEntDAO getTEntDAO() {
		return tEntDAO;
	}

	public void setTEntDAO(TEntDAO entDAO) {
		tEntDAO = entDAO;
	}
}
