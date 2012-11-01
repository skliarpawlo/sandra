/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package results;

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
@Table(name = "results", catalog = "cccheck", schema = "")
@NamedQueries({
    @NamedQuery(name = "Results.findAll", query = "SELECT r FROM Results r"),
    @NamedQuery(name = "Results.findByRid", query = "SELECT r FROM Results r WHERE r.rid = :rid"),
    @NamedQuery(name = "Results.findBySid", query = "SELECT r FROM Results r WHERE r.sid = :sid")})
public class Results implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rid")
    private Integer rid;
    @Column(name = "sid")
    private Integer sid;
    @Lob
    @Column(name = "results")
    private String results;

    public Results() {
    }

    public Results(Integer rid) {
        this.rid = rid;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        Integer oldRid = this.rid;
        this.rid = rid;
        changeSupport.firePropertyChange("rid", oldRid, rid);
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        Integer oldSid = this.sid;
        this.sid = sid;
        changeSupport.firePropertyChange("sid", oldSid, sid);
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        String oldResults = this.results;
        this.results = results;
        changeSupport.firePropertyChange("results", oldResults, results);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rid != null ? rid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Results)) {
            return false;
        }
        Results other = (Results) object;
        if ((this.rid == null && other.rid != null) || (this.rid != null && !this.rid.equals(other.rid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "results.Results[rid=" + rid + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
