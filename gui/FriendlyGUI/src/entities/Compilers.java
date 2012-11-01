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
 * @author pawlo
 */
@Entity
@Table(name = "compilers")
@NamedQueries({
    @NamedQuery(name = "Compilers.findAll", query = "SELECT c FROM Compilers c"),
    @NamedQuery(name = "Compilers.findByCpid", query = "SELECT c FROM Compilers c WHERE c.cpid = :cpid")})
public class Compilers implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cpid")
    private Integer cpid;
    @Lob
    @Column(name = "extensions")
    private String extensions;
    @Lob
    @Column(name = "compilerpath")
    private String compilerpath;
    @Lob
    @Column(name = "params")
    private String params;

    public Compilers() {
    }

    public Compilers( String _exts, String _compiler, String _param )
    {
        extensions = _exts;
        compilerpath = _compiler;
        params = _param;
    }

    public Compilers(Integer cpid) {
        this.cpid = cpid;
    }

    public Integer getCpid() {
        return cpid;
    }

    public void setCpid(Integer cpid) {
        this.cpid = cpid;
    }

    public String getExtensions() {
        return extensions;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    public String getCompilerpath() {
        return compilerpath;
    }

    public void setCompilerpath(String compilerpath) {
        this.compilerpath = compilerpath;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cpid != null ? cpid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compilers)) {
            return false;
        }
        Compilers other = (Compilers) object;
        if ((this.cpid == null && other.cpid != null) || (this.cpid != null && !this.cpid.equals(other.cpid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Compilers[cpid=" + cpid + "]";
    }

}
