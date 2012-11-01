/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package problemcategories;

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
 * @author Pawlo
 */
@Entity
@Table(name = "problem_categories", catalog = "cccheck", schema = "")
@NamedQueries({
    @NamedQuery(name = "ProblemCategories.findAll", query = "SELECT p FROM ProblemCategories p"),
    @NamedQuery(name = "ProblemCategories.findByCatid", query = "SELECT p FROM ProblemCategories p WHERE p.catid = :catid"),
    @NamedQuery(name = "ProblemCategories.findByParentid", query = "SELECT p FROM ProblemCategories p WHERE p.parentid = :parentid")})
public class ProblemCategories implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "catid")
    private Integer catid;
    @Lob
    @Column(name = "name")
    private String name;
    @Column(name = "parentid")
    private Integer parentid;

    public ProblemCategories() {
    }

    public ProblemCategories(Integer catid) {
        this.catid = catid;
    }

    public Integer getCatid() {
        return catid;
    }

    public void setCatid(Integer catid) {
        Integer oldCatid = this.catid;
        this.catid = catid;
        changeSupport.firePropertyChange("catid", oldCatid, catid);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        changeSupport.firePropertyChange("name", oldName, name);
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        Integer oldParentid = this.parentid;
        this.parentid = parentid;
        changeSupport.firePropertyChange("parentid", oldParentid, parentid);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (catid != null ? catid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProblemCategories)) {
            return false;
        }
        ProblemCategories other = (ProblemCategories) object;
        if ((this.catid == null && other.catid != null) || (this.catid != null && !this.catid.equals(other.catid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "problemcategories.ProblemCategories[catid=" + catid + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
