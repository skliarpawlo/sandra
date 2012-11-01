/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package needtotest;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author pawlo
 */
@Entity
@Table(name = "need_test", catalog = "cccheck", schema = "")
@NamedQueries({
    @NamedQuery(name = "NeedTest.findAll", query = "SELECT n FROM NeedTest n"),
    @NamedQuery(name = "NeedTest.findByNtid", query = "SELECT n FROM NeedTest n WHERE n.ntid = :ntid"),
    @NamedQuery(name = "NeedTest.findBySid", query = "SELECT n FROM NeedTest n WHERE n.sid = :sid")})
public class NeedTest implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ntid")
    private Integer ntid;
    @Column(name = "sid")
    private Integer sid;

    public NeedTest() {
    }

    public NeedTest(Integer ntid) {
        this.ntid = ntid;
    }

    public Integer getNtid() {
        return ntid;
    }

    public void setNtid(Integer ntid) {
        Integer oldNtid = this.ntid;
        this.ntid = ntid;
        changeSupport.firePropertyChange("ntid", oldNtid, ntid);
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        Integer oldSid = this.sid;
        this.sid = sid;
        changeSupport.firePropertyChange("sid", oldSid, sid);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ntid != null ? ntid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NeedTest)) {
            return false;
        }
        NeedTest other = (NeedTest) object;
        if ((this.ntid == null && other.ntid != null) || (this.ntid != null && !this.ntid.equals(other.ntid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "needtotest.NeedTest[ntid=" + ntid + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
