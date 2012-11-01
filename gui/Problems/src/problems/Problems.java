/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package problems;

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
@Table(name = "problems", catalog = "cccheck", schema = "")
@NamedQueries({
    @NamedQuery(name = "Problems.findAll", query = "SELECT p FROM Problems p"),
    @NamedQuery(name = "Problems.findByPid", query = "SELECT p FROM Problems p WHERE p.pid = :pid"),
    @NamedQuery(name = "Problems.findByTimelimit", query = "SELECT p FROM Problems p WHERE p.timelimit = :timelimit")})
public class Problems implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pid")
    private Integer pid;
    @Lob
    @Column(name = "title")
    private String title;
    @Lob
    @Column(name = "filepath")
    private String filepath;
    @Lob
    @Column(name = "comparor")
    private String comparor;
    @Lob
    @Column(name = "solutionfile")
    private String solutionfile;
    @Lob
    @Column(name = "inputfile")
    private String inputfile;
    @Lob
    @Column(name = "outputfile")
    private String outputfile;
    @Column(name = "timelimit")
    private Integer timelimit;

    public Problems() {
    }

    public Problems(Integer pid) {
        this.pid = pid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        Integer oldPid = this.pid;
        this.pid = pid;
        changeSupport.firePropertyChange("pid", oldPid, pid);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String oldTitle = this.title;
        this.title = title;
        changeSupport.firePropertyChange("title", oldTitle, title);
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        String oldFilepath = this.filepath;
        this.filepath = filepath;
        changeSupport.firePropertyChange("filepath", oldFilepath, filepath);
    }

    public String getComparor() {
        return comparor;
    }

    public void setComparor(String comparor) {
        String oldComparor = this.comparor;
        this.comparor = comparor;
        changeSupport.firePropertyChange("comparor", oldComparor, comparor);
    }

    public String getSolutionfile() {
        return solutionfile;
    }

    public void setSolutionfile(String solutionfile) {
        String oldSolutionfile = this.solutionfile;
        this.solutionfile = solutionfile;
        changeSupport.firePropertyChange("solutionfile", oldSolutionfile, solutionfile);
    }

    public String getInputfile() {
        return inputfile;
    }

    public void setInputfile(String inputfile) {
        String oldInputfile = this.inputfile;
        this.inputfile = inputfile;
        changeSupport.firePropertyChange("inputfile", oldInputfile, inputfile);
    }

    public String getOutputfile() {
        return outputfile;
    }

    public void setOutputfile(String outputfile) {
        String oldOutputfile = this.outputfile;
        this.outputfile = outputfile;
        changeSupport.firePropertyChange("outputfile", oldOutputfile, outputfile);
    }

    public Integer getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(Integer timelimit) {
        Integer oldTimelimit = this.timelimit;
        this.timelimit = timelimit;
        changeSupport.firePropertyChange("timelimit", oldTimelimit, timelimit);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pid != null ? pid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Problems)) {
            return false;
        }
        Problems other = (Problems) object;
        if ((this.pid == null && other.pid != null) || (this.pid != null && !this.pid.equals(other.pid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "problems.Problems[pid=" + pid + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
