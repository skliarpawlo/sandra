/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Pawlo
 */
@Entity
@Table(name = "solutions", catalog = "ccCheck", schema = "")
@NamedQueries({
    @NamedQuery(name = "Solutions.findAll", query = "SELECT s FROM Solutions s"),
    @NamedQuery(name = "Solutions.findBySid", query = "SELECT s FROM Solutions s WHERE s.sid = :sid"),
    @NamedQuery(name = "Solutions.findByUid", query = "SELECT s FROM Solutions s WHERE s.uid = :uid"),
    @NamedQuery(name = "Solutions.findByPid", query = "SELECT s FROM Solutions s WHERE s.pid = :pid"),
    @NamedQuery(name = "Solutions.findByCount", query = "SELECT s FROM Solutions s WHERE s.count = :count"),
    @NamedQuery(name = "Solutions.findByIsdozd", query = "SELECT s FROM Solutions s WHERE s.isdozd = :isdozd")})
public class Solutions implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sid", nullable = false)
    private Integer sid;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "pid")
    private Integer pid;
    @Column(name = "count")
    private Integer count;
    @Column(name = "isdozd")
    private Integer isdozd;
    @Lob
    @Column(name = "intime", length = 65535)
    private String intime;
    @Lob
    @Column(name = "filepath", length = 65535)
    private String filepath;

    public Solutions() {
    }

    public Solutions( Integer _uid, Integer _pid, String _intime, String _filepath, Integer _count, boolean _isdozd )
    {
        uid = _uid;
        pid = _pid;
        intime = _intime;
        filepath = _filepath;
        count = _count;
        isdozd = ( _isdozd ) ? 1 : 0;

    }

    public Solutions(Integer sid) {
        this.sid = sid;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getIsdozd() {
        return isdozd;
    }

    public void setIsdozd(Integer isdozd) {
        this.isdozd = isdozd;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sid != null ? sid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Solutions)) {
            return false;
        }
        Solutions other = (Solutions) object;
        if ((this.sid == null && other.sid != null) || (this.sid != null && !this.sid.equals(other.sid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Solutions[sid=" + sid + "]";
    }

}
