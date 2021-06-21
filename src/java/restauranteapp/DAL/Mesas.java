/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restauranteapp.DAL;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author kevin
 */
@Entity
@Table(name = "MESAS")
@NamedQueries({
    @NamedQuery(name = "Mesas.findAll", query = "SELECT m FROM Mesas m"),
    @NamedQuery(name = "Mesas.findByIdMesa", query = "SELECT m FROM Mesas m WHERE m.idMesa = :idMesa"),
    @NamedQuery(name = "Mesas.findByCapacidade", query = "SELECT m FROM Mesas m WHERE m.capacidade = :capacidade")})
public class Mesas implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_MESA")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private BigDecimal idMesa;
    @Basic(optional = false)
    @Column(name = "CAPACIDADE")
    private BigInteger capacidade;
    @JoinTable(name = "RESERVAMESA", joinColumns = {
        @JoinColumn(name = "ID_MESA", referencedColumnName = "ID_MESA")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_RESERVA", referencedColumnName = "ID_RESERVA")})
    @ManyToMany
    private List<Reserva> reservaList;

    public Mesas() {
    }

    public Mesas(BigDecimal idMesa) {
        this.idMesa = idMesa;
    }

    public Mesas(BigDecimal idMesa, BigInteger capacidade) {
        this.idMesa = idMesa;
        this.capacidade = capacidade;
    }

    public BigDecimal getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(BigDecimal idMesa) {
        this.idMesa = idMesa;
    }

    public BigInteger getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(BigInteger capacidade) {
        this.capacidade = capacidade;
    }

    public List<Reserva> getReservaList() {
        return reservaList;
    }

    public void setReservaList(List<Reserva> reservaList) {
        this.reservaList = reservaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMesa != null ? idMesa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mesas)) {
            return false;
        }
        Mesas other = (Mesas) object;
        if ((this.idMesa == null && other.idMesa != null) || (this.idMesa != null && !this.idMesa.equals(other.idMesa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "restauranteapp.DAL.Mesas[ idMesa=" + idMesa + " ]";
    }
    
}
