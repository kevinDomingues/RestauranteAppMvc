/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restauranteapp.DAL;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author kevin
 */
@Embeddable
public class LinhaencomendaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ID_ENCOMENDA")
    private int idEncomenda;
    @Basic(optional = false)
    @Column(name = "ID_STOCKPRODUTO")
    private int idStockproduto;

    public LinhaencomendaPK() {
    }

    public LinhaencomendaPK(int idEncomenda, int idStockproduto) {
        this.idEncomenda = idEncomenda;
        this.idStockproduto = idStockproduto;
    }

    public int getIdEncomenda() {
        return idEncomenda;
    }

    public void setIdEncomenda(int idEncomenda) {
        this.idEncomenda = idEncomenda;
    }

    public int getIdStockproduto() {
        return idStockproduto;
    }

    public void setIdStockproduto(int idStockproduto) {
        this.idStockproduto = idStockproduto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idEncomenda;
        hash += (int) idStockproduto;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LinhaencomendaPK)) {
            return false;
        }
        LinhaencomendaPK other = (LinhaencomendaPK) object;
        if (this.idEncomenda != other.idEncomenda) {
            return false;
        }
        if (this.idStockproduto != other.idStockproduto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "restauranteapp.DAL.LinhaencomendaPK[ idEncomenda=" + idEncomenda + ", idStockproduto=" + idStockproduto + " ]";
    }
    
}
