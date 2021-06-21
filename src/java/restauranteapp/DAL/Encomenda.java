/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restauranteapp.DAL;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author kevin
 */
@Entity
@Table(name = "ENCOMENDA")
@NamedQueries({
    @NamedQuery(name = "Encomenda.findAll", query = "SELECT e FROM Encomenda e"),
    @NamedQuery(name = "Encomenda.findByIdEncomenda", query = "SELECT e FROM Encomenda e WHERE e.idEncomenda = :idEncomenda"),
    @NamedQuery(name = "Encomenda.findByDescricao", query = "SELECT e FROM Encomenda e WHERE e.descricao = :descricao"),
    @NamedQuery(name = "Encomenda.findByDatahora", query = "SELECT e FROM Encomenda e WHERE e.datahora = :datahora"),
    @NamedQuery(name = "Encomenda.findByValortotal", query = "SELECT e FROM Encomenda e WHERE e.valortotal = :valortotal")})
public class Encomenda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_ENCOMENDA")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idEncomenda;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Basic(optional = false)
    @Column(name = "DATAHORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datahora;
    @Basic(optional = false)
    @Column(name = "VALORTOTAL")
    private double valortotal;
    @JoinColumn(name = "ID_ESTADO", referencedColumnName = "ID_ESTADO")
    @ManyToOne(optional = false)
    private Estado idEstado;
    @JoinColumn(name = "ID_FORNECEDOR", referencedColumnName = "ID_FORNECEDOR")
    @ManyToOne(optional = false)
    private Fornecedor idFornecedor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "encomenda")
    private List<Linhaencomenda> linhaencomendaList;

    public Encomenda() {
    }

    public Encomenda(Integer idEncomenda) {
        this.idEncomenda = idEncomenda;
    }

    public Encomenda(Integer idEncomenda, Date datahora, double valortotal) {
        this.idEncomenda = idEncomenda;
        this.datahora = datahora;
        this.valortotal = valortotal;
    }

    public Integer getIdEncomenda() {
        return idEncomenda;
    }

    public void setIdEncomenda(Integer idEncomenda) {
        this.idEncomenda = idEncomenda;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDatahora() {
        return datahora;
    }

    public void setDatahora(Date datahora) {
        this.datahora = datahora;
    }
    
    public double getValortotal() {
        return valortotal;
    }

    public void setValortotal(double valortotal) {
        this.valortotal = valortotal;
    }

    public Estado getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Estado idEstado) {
        this.idEstado = idEstado;
    }

    public Fornecedor getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Fornecedor idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public List<Linhaencomenda> getLinhaencomendaList() {
        return linhaencomendaList;
    }

    public void setLinhaencomendaList(List<Linhaencomenda> linhaencomendaList) {
        this.linhaencomendaList = linhaencomendaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEncomenda != null ? idEncomenda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Encomenda)) {
            return false;
        }
        Encomenda other = (Encomenda) object;
        if ((this.idEncomenda == null && other.idEncomenda != null) || (this.idEncomenda != null && !this.idEncomenda.equals(other.idEncomenda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "restauranteapp.DAL.Encomenda[ idEncomenda=" + idEncomenda + " ]";
    }
    
}
