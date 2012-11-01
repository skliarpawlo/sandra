/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package problemcategoryassign;

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
 * @author Pawlo
 */
@Entity
@Table(name = "problem_category_assign", catalog = "cccheck", schema = "")
@NamedQueries({
    @NamedQuery(name = "ProblemCategoryAssign.findAll", query = "SELECT p FROM ProblemCategoryAssign p"),
    @NamedQuery(name = "ProblemCategoryAssign.findByAssignid", query = "SELECT p FROM ProblemCategoryAssign p WHERE p.assignid = :assignid"),
    @NamedQuery(name = "ProblemCategoryAssign.findByCatid", query = "SELECT p FROM ProblemCategoryAssign p WHERE p.catid = :catid"),
    @NamedQuery(name = "ProblemCategoryAssign.findByPid", query = "SELECT p FROM ProblemCategoryAssign p WHERE p.pid = :pid")})
public class ProblemCategoryAssign implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "assignid")
    private Integer assignid;
    @Column(name = "catid")
    private Integer catid;
    @Column(name = "pid")
    private Integer pid;

    public ProblemCategoryAssign() {
    }

    public ProblemCategoryAssign(Integer assignid) {
        this.assignid = assignid;
    }

    public Integer getAssignid() {
        return assignid;
    }

    public void setAssignid(Integer assignid) {
        Integer oldAssignid = this.assignid;
        this.assignid = assignid;
        changeSupport.firePropertyChange("assignid", oldAssignid, assignid);
    }

    public Integer getCatid() {
        return catid;
    }

    public void setCatid(Integer catid) {
        Integer oldCatid = this.catid;
        this.catid = catid;
        changeSupport.firePropertyChange("catid", oldCatid, catid);
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        Integer oldPid = this.pid;
        this.pid = pid;
        changeSupport.firePropertyChange("pid", oldPid, pid);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (assignid != null ? assignid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProblemCategoryAssign)) {
            return false;
        }
        ProblemCategoryAssign other = (ProblemCategoryAssign) object;
        if ((this.assignid == null && other.assignid != null) || (this.assignid != null && !this.assignid.equals(other.assignid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "problemcategoryassign.ProblemCategoryAssign[assignid=" + assignid + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
