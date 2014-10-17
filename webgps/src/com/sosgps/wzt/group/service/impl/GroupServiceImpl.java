package com.sosgps.wzt.group.service.impl;

import java.util.List;

import org.sos.helper.SpringHelper;
import org.springframework.beans.BeanUtils;

import cn.net.sosgps.memcache.Memcache;
import cn.net.sosgps.memcache.bean.Group;

import com.sosgps.v21.util.Constants;
import com.sosgps.wzt.group.dao.GroupDAO;
import com.sosgps.wzt.group.service.GroupService;
import com.sosgps.wzt.manage.terminalgroup.service.TerminalGroupManageService;
import com.sosgps.wzt.orm.TEnt;
import com.sosgps.wzt.orm.TEntDAO;
import com.sosgps.wzt.orm.TTermGroup;

public class GroupServiceImpl implements GroupService {

    private GroupDAO tTargetGroupDao;

    private TEntDAO tEntDAO;

    /**
     * @return the tEntDAO
     */
    public TEntDAO getTEntDAO() {
        return this.tEntDAO;
    }

    /**
     * @param entDAO the tEntDAO to set
     */
    public void setTEntDAO(TEntDAO entDAO) {
        this.tEntDAO = entDAO;
    }

    public GroupDAO getTTargetGroupDao() {
        return tTargetGroupDao;
    }

    public void setTTargetGroupDao(GroupDAO targetGroupDao) {
        tTargetGroupDao = targetGroupDao;
    }

    /**
     * ����group id update��/���Žṹ����
     * 
     * @param groupId
     * @param groupName
     * @param ctl
     * @param childGroupName
     */
    public boolean modTGNameById(Long groupId, String groupName, String starttime, String endtime,
            String groupstatus, Long week) {
        try {
            TTermGroup tTargetGroup = tTargetGroupDao.findById(groupId);
            tTargetGroup.setGroupName(groupName);
            tTargetGroup.setStartTime(starttime);
            tTargetGroup.setEndTime(endtime);
            tTargetGroup.setGroupStatus(groupstatus);
            tTargetGroup.setWeek(week);
            tTargetGroupDao.update(tTargetGroup);

            /** ���ն�����Ϣ���浽memcache��* */
            Group groupCached = new Group();
            BeanUtils.copyProperties(tTargetGroup, groupCached);
            Constants.GROUP_CACHE.set(String.valueOf(tTargetGroup.getId()), groupCached,
                    10 * Memcache.EXPIRE_DAY);// ����10��
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * �����/���Žṹ
     * 
     * @return
     */
    public List getTTargetGroup(String empCode) {
        return tTargetGroupDao.getTTargetGroup(empCode);
    }

    /**
     * ɾ����/����
     * 
     * @return
     */
    public boolean delTGById(Long groupId) {
        try {
            TTermGroup tTargetGroup = tTargetGroupDao.findById(groupId);
            List tTargetGroupList = tTargetGroupDao.findChild(tTargetGroup.getId());
            if (tTargetGroupList != null && tTargetGroupList.size() > 0) {
                for (int i = 0; i < tTargetGroupList.size(); i++) {
                    TTermGroup tTargetGroup1 = (TTermGroup) tTargetGroupList.get(i);
                    if (tTargetGroup1 != null && tTargetGroup1.getId() != null) {
                        this.delTGById(tTargetGroup1.getId());

                        /** ���ն�����Ϣ��memcache��ɾ��* */
                        Constants.GROUP_CACHE.delete(String.valueOf(tTargetGroup1.getId()));
                    }
                }
            }
            // List tUserViTarGList=tUserViTarGDao.findByGroupId(groupId);;
            // if(tUserViTarGList!=null&&tUserViTarGList.size()>0){
            // for(int i=0;i<tUserViTarGList.size();i++){
            // TUserViTarG tUserViTar=(TUserViTarG)tUserViTarGList.get(i);
            // tUserViTarGDao.delete(tUserViTar);
            // }
            // }
            // List tTargetObjectList = tTargetObjectDao.findByGroupId(groupId);
            // if(tTargetObjectList!=null&&tTargetObjectList.size()>0){
            // TTargetGroup
            // tTargetGroup2=tTargetGroupDao.findByParentId((long)-1);
            // for(int i=0;i<tTargetObjectList.size();i++){
            // TTargetObject
            // tTargetObject=(TTargetObject)tTargetObjectList.get(i);
            // tTargetObject.setTTargetGroup(tTargetGroup2);
            // tTargetObjectDao.update(tTargetObject);
            // }
            // }

            tTargetGroupDao.delete(tTargetGroup);

            /** ���ն�����Ϣ��memcache��ɾ��* */
            Constants.GROUP_CACHE.delete(String.valueOf(tTargetGroup.getId()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * �������/����
     * 
     * @param userId
     */
    public boolean addChildTG(Long groupId, String childGroupName, TEnt ent, String starttime,
            String endtime, Long userId, Long week) {
        try {

            Long groupSort = (long) 1;
            // Long groupLevel=(long)2;
            // TTermGroup tTargetGroup1=tTargetGroupDao.findById(groupId);
            // groupLevel=tTargetGroup1.getGroupLevel()+1;
            List result = tTargetGroupDao.findChildLastSortById(groupId);
            if (result == null || result.size() <= 0) {

            } else {
                groupSort = ((TTermGroup) result.get(0)).getGroupSort() + 1;
            }
            TTermGroup tTargetGroup = new TTermGroup();
            tTargetGroup.setGroupName(childGroupName);
            tTargetGroup.setParentId(groupId);
            tTargetGroup.setGroupSort(groupSort);
            tTargetGroup.setTEnt(ent);
            tTargetGroup.setStartTime(starttime);
            tTargetGroup.setEndTime(endtime);
            tTargetGroup.setGroupStatus("1");
            tTargetGroup.setWeek(week);
            tTargetGroupDao.save(tTargetGroup);
            // �û��ɼ��ն���
            TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
                    .getBean("TerminalGroupManageServiceImpl");
            boolean re = terminalGroupManageService.addUserTermGroup(ent.getEntCode(), userId,
                    tTargetGroup.getId());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sosgps.wzt.group.service.GroupService#getEnt(java.lang.String)
     */
    public TEnt getEnt(String id) {
        // TODO Auto-generated method stub
        return this.tEntDAO.findById(id);
    }

    public List findRefTermGroupById(Long groupId) {
        try {
            return tTargetGroupDao.findRefTermGroupById(groupId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
