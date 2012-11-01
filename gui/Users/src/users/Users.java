/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package users;

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
@Table(name = "users", catalog = "cccheck", schema = "")
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByUid", query = "SELECT u FROM Users u WHERE u.uid = :uid"),
    @NamedQuery(name = "Users.findByPriority", query = "SELECT u FROM Users u WHERE u.priority = :priority")})
public class Users implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "uid")
    private Integer uid;
    @Lob
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "password")
    private String password;
    @Lob
    @Column(name = "usercategory")
    private String usercategory;
    @Lob
    @Column(name = "fio")
    private String fio;
    @Column(name = "priority")
    private String priority;

    public Users() {
    }

    public Users(Integer uid) {
        this.uid = uid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        Integer oldUid = this.uid;
        this.uid = uid;
        changeSupport.firePropertyChange("uid", oldUid, uid);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        changeSupport.firePropertyChange("name", oldName, name);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        String oldPassword = this.password;
        this.password = password;
        changeSupport.firePropertyChange("password", oldPassword, password);
    }

    public String getUsercategory() {
        return usercategory;
    }

    public void setUsercategory(String usercategory) {
        String oldUsercategory = this.usercategory;
        this.usercategory = usercategory;
        changeSupport.firePropertyChange("usercategory", oldUsercategory, usercategory);
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        String oldFio = this.fio;
        this.fio = fio;
        changeSupport.firePropertyChange("fio", oldFio, fio);
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        String oldPriority = this.priority;
        this.priority = priority;
        changeSupport.firePropertyChange("priority", oldPriority, priority);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uid != null ? uid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "users.Users[uid=" + uid + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
