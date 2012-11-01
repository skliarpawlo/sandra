/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tests;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
import javax.persistence.Transient;

/**
 *
 * @author pawlo
 */
@Entity
@Table(name = "tests", catalog = "cccheck", schema = "")
@NamedQueries({
    @NamedQuery(name = "Tests.findAll", query = "SELECT t FROM Tests t"),
    @NamedQuery(name = "Tests.findByTid", query = "SELECT t FROM Tests t WHERE t.tid = :tid"),
    @NamedQuery(name = "Tests.findByPid", query = "SELECT t FROM Tests t WHERE t.pid = :pid"),
    @NamedQuery(name = "Tests.findByNum", query = "SELECT t FROM Tests t WHERE t.num = :num")})
public class Tests implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tid")
    private Integer tid;
    @Column(name = "pid")
    private Integer pid;
    @Column(name = "num")
    private Integer num;
    @Lob
    @Column(name = "testpath")
    private String testpath;
    @Lob
    @Column(name = "anspath")
    private String anspath;

    public Tests() {
    }

    public Tests(Integer tid) {
        this.tid = tid;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        Integer oldTid = this.tid;
        this.tid = tid;
        changeSupport.firePropertyChange("tid", oldTid, tid);
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        Integer oldPid = this.pid;
        this.pid = pid;
        changeSupport.firePropertyChange("pid", oldPid, pid);
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        Integer oldNum = this.num;
        this.num = num;
        changeSupport.firePropertyChange("num", oldNum, num);
    }

    public String getTestpath() {
        return testpath;
    }

    public void setTestpath(String testpath) {
        String oldTestpath = this.testpath;
        this.testpath = testpath;
        changeSupport.firePropertyChange("testpath", oldTestpath, testpath);
    }

    public String getAnspath() {
        return anspath;
    }

    public void setAnspath(String anspath) {
        String oldAnspath = this.anspath;
        this.anspath = anspath;
        changeSupport.firePropertyChange("anspath", oldAnspath, anspath);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tid != null ? tid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tests)) {
            return false;
        }
        Tests other = (Tests) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tests.Tests[tid=" + tid + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
