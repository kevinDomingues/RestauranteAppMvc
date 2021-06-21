/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restauranteapp.DAL;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author kevin
 */
@Entity
@Table(name = "LINHAENCOMENDA")
@NamedQueries({
    @NamedQuery(name = "Linhaencomenda.findAll", query = "SELECT l FROM Linhaencomenda l"),
    @NamedQuery(name = "Linhaencomenda.findByIdEncomenda", query = "SELECT l FROM Linhaencomenda l WHERE l.linhaencomendaPK.idEncomenda = :idEncomenda"),
    @NamedQuery(name = "Linhaencomenda.findByIdStockproduto", query = "SELECT l FROM Linhaencomenda l WHERE l.linhaencomendaPK.idStockproduto = :idStockproduto"),
    @NamedQuery(name = "Linhaencomenda.findByQuantidade", query = "SELECT l FROM Linhaencomenda l WHERE l.quantidade = :quantidade")})
public class Linhaencomenda implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LinhaencomendaPK linhaencomendaPK;
    @Basic(optional = false)
    @Column(name = "QUANTIDADE")
    private int quantidade;
    @JoinColumn(name = "ID_ENCOMENDA", referencedColumnName = "ID_ENCOMENDA", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Encomenda encomenda;
    @JoinColumn(name = "ID_STOCKPRODUTO", referencedColumnName = "ID_STOCKPRODUTO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Stockproduto stockproduto;

    public Linhaencomenda() {
    }

    public Linhaencomenda(LinhaencomendaPK linhaencomendaPK) {
        this.linhaencomendaPK = linhaencomendaPK;
    }

    public Linhaencomenda(LinhaencomendaPK linhaencomendaPK, int quantidade) {
        this.linhaencomendaPK = linhaencomendaPK;
        this.quantidade = quantidade;
    }

    public Linhaencomenda(int idEncomenda, int idStockproduto) {
        this.linhaencomendaPK = new LinhaencomendaPK(idEncomenda, idStockproduto);
    }

    public LinhaencomendaPK getLinhaencomendaPK() {
        return linhaencomendaPK;
    }

    public void setLinhaencomendaPK(LinhaencomendaPK linhaencomendaPK) {
        this.linhaencomendaPK = linhaencomendaPK;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(Encomenda encomenda) {
        this.encomenda = encomenda;
    }

    public Stockproduto getStockproduto() {
        return stockproduto;
    }

    public void setStockproduto(Stockproduto stockproduto) {
        this.stockproduto = stockproduto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (linhaencomendaPK != null ? linhaencomendaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Linhaencomenda)) {
            return false;
        }
        Linhaencomenda other = (Linhaencomenda) object;
        if ((this.linhaencomendaPK == null && other.linhaencomendaPK != null) || (this.linhaencomendaPK != null && !this.linhaencomendaPK.equals(other.linhaencomendaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "restauranteapp.DAL.Linhaencomenda[ linhaencomendaPK=" + linhaencomendaPK + " ]";
    }
    
}
