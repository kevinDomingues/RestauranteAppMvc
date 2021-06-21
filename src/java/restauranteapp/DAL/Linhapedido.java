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
@Table(name = "LINHAPEDIDO")
@NamedQueries({
    @NamedQuery(name = "Linhapedido.findAll", query = "SELECT l FROM Linhapedido l"),
    @NamedQuery(name = "Linhapedido.findByCodpedido", query = "SELECT l FROM Linhapedido l WHERE l.linhapedidoPK.codpedido = :codpedido"),
    @NamedQuery(name = "Linhapedido.findByIdProduto", query = "SELECT l FROM Linhapedido l WHERE l.linhapedidoPK.idProduto = :idProduto"),
    @NamedQuery(name = "Linhapedido.findByQuantidade", query = "SELECT l FROM Linhapedido l WHERE l.quantidade = :quantidade")})
public class Linhapedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LinhapedidoPK linhapedidoPK;
    @Basic(optional = false)
    @Column(name = "QUANTIDADE")
    private int quantidade;
    @JoinColumn(name = "CODPEDIDO", referencedColumnName = "CODPEDIDO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Pedido pedido;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID_PRODUTO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Produtoementa produtoementa;

    public Linhapedido() {
    }

    public Linhapedido(LinhapedidoPK linhapedidoPK) {
        this.linhapedidoPK = linhapedidoPK;
    }

    public Linhapedido(LinhapedidoPK linhapedidoPK, int quantidade) {
        this.linhapedidoPK = linhapedidoPK;
        this.quantidade = quantidade;
    }

    public Linhapedido(int codpedido, int idProduto) {
        this.linhapedidoPK = new LinhapedidoPK(codpedido, idProduto);
    }

    public LinhapedidoPK getLinhapedidoPK() {
        return linhapedidoPK;
    }

    public void setLinhapedidoPK(LinhapedidoPK linhapedidoPK) {
        this.linhapedidoPK = linhapedidoPK;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produtoementa getProdutoementa() {
        return produtoementa;
    }

    public void setProdutoementa(Produtoementa produtoementa) {
        this.produtoementa = produtoementa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (linhapedidoPK != null ? linhapedidoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Linhapedido)) {
            return false;
        }
        Linhapedido other = (Linhapedido) object;
        if ((this.linhapedidoPK == null && other.linhapedidoPK != null) || (this.linhapedidoPK != null && !this.linhapedidoPK.equals(other.linhapedidoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "restauranteapp.DAL.Linhapedido[ linhapedidoPK=" + linhapedidoPK + " ]";
    }
    
}
