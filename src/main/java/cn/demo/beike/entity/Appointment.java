package cn.demo.beike.entity;

import java.util.Date;

public class Appointment {
    private Integer id;

    private Integer typeid;

    private Date createtime;

    private Date detaildate;

    private Date begintime;

    private Date endtime;

    private Integer limitnum;

    private String flag;

    private Date limittime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getDetaildate() {
        return detaildate;
    }

    public void setDetaildate(Date detaildate) {
        this.detaildate = detaildate;
    }

    public Date getBegintime() {
        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Integer getLimitnum() {
        return limitnum;
    }

    public void setLimitnum(Integer limitnum) {
        this.limitnum = limitnum;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }

    public Date getLimittime() {
        return limittime;
    }

    public void setLimittime(Date limittime) {
        this.limittime = limittime;
    }
}