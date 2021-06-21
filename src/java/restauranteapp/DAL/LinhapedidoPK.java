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
public class LinhapedidoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "CODPEDIDO")
    private int codpedido;
    @Basic(optional = false)
    @Column(name = "ID_PRODUTO")
    private int idProduto;

    public LinhapedidoPK() {
    }

    public LinhapedidoPK(int codpedido, int idProduto) {
        this.codpedido = codpedido;
        this.idProduto = idProduto;
    }

    public int getCodpedido() {
        return codpedido;
    }

    public void setCodpedido(int codpedido) {
        this.codpedido = codpedido;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codpedido;
        hash += (int) idProduto;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LinhapedidoPK)) {
            return false;
        }
        LinhapedidoPK other = (LinhapedidoPK) object;
        if (this.codpedido != other.codpedido) {
            return false;
        }
        if (this.idProduto != other.idProduto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "restauranteapp.DAL.LinhapedidoPK[ codpedido=" + codpedido + ", idProduto=" + idProduto + " ]";
    }
    
}
