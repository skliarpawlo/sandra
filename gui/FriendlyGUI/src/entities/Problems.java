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
@Table(name = "problems")
@NamedQueries({
    @NamedQuery(name = "Problems.findAll", query = "SELECT p FROM Problems p"),
    @NamedQuery(name = "Problems.findByPid", query = "SELECT p FROM Problems p WHERE p.pid = :pid"),
    @NamedQuery(name = "Problems.findByTimelimit", query = "SELECT p FROM Problems p WHERE p.timelimit = :timelimit")})
public class Problems implements Serializable {
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

    public Problems( String _title, String _filepath, String _comparor, String _solutionfile, String _inputfile, String _outputfile, Integer _timelimit )
    {
        title = _title;
        filepath = _filepath;
        comparor = _comparor;
        solutionfile = _solutionfile;
        inputfile = _inputfile;
        outputfile = _outputfile;
        timelimit = _timelimit;
    }

    public Problems(Integer pid) {
        this.pid = pid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getComparor() {
        return comparor;
    }

    public void setComparor(String comparor) {
        this.comparor = comparor;
    }

    public String getSolutionfile() {
        return solutionfile;
    }

    public void setSolutionfile(String solutionfile) {
        this.solutionfile = solutionfile;
    }

    public String getInputfile() {
        return inputfile;
    }

    public void setInputfile(String inputfile) {
        this.inputfile = inputfile;
    }

    public String getOutputfile() {
        return outputfile;
    }

    public void setOutputfile(String outputfile) {
        this.outputfile = outputfile;
    }

    public Integer getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(Integer timelimit) {
        this.timelimit = timelimit;
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
        return "entities.Problems[pid=" + pid + "]";
    }

}
