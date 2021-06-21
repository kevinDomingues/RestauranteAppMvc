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
@Table(name = "PEDIDO")
@NamedQueries({
    @NamedQuery(name = "Pedido.findAll", query = "SELECT p FROM Pedido p"),
    @NamedQuery(name = "Pedido.findByCodpedido", query = "SELECT p FROM Pedido p WHERE p.codpedido = :codpedido"),
    @NamedQuery(name = "Pedido.findByDatahora", query = "SELECT p FROM Pedido p WHERE p.datahora = :datahora"),
    @NamedQuery(name = "Pedido.findByValortotal", query = "SELECT p FROM Pedido p WHERE p.valortotal = :valortotal")})
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODPEDIDO")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer codpedido;
    @Basic(optional = false)
    @Column(name = "DATAHORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datahora;
    @Basic(optional = false)
    @Column(name = "VALORTOTAL")
    private double valortotal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    private List<Linhapedido> linhapedidoList;
    @JoinColumn(name = "ID_ENTIDADE", referencedColumnName = "ID_ENTIDADE")
    @ManyToOne(optional = false)
    private Entidade idEntidade;
    @JoinColumn(name = "ID_ESTADO", referencedColumnName = "ID_ESTADO")
    @ManyToOne(optional = false)
    private Estado idEstado;

    public Pedido() {
    }

    public Pedido(Integer codpedido) {
        this.codpedido = codpedido;
    }

    public Pedido(Integer codpedido, Date datahora, double valortotal) {
        this.codpedido = codpedido;
        this.datahora = datahora;
        this.valortotal = valortotal;
    }

    public Integer getCodpedido() {
        return codpedido;
    }

    public void setCodpedido(Integer codpedido) {
        this.codpedido = codpedido;
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

    public List<Linhapedido> getLinhapedidoList() {
        return linhapedidoList;
    }

    public void setLinhapedidoList(List<Linhapedido> linhapedidoList) {
        this.linhapedidoList = linhapedidoList;
    }

    public Entidade getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(Entidade idEntidade) {
        this.idEntidade = idEntidade;
    }

    public Estado getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Estado idEstado) {
        this.idEstado = idEstado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codpedido != null ? codpedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        if ((this.codpedido == null && other.codpedido != null) || (this.codpedido != null && !this.codpedido.equals(other.codpedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return idEntidade.getNome()+"\n\n"+idEntidade.getRua()+"\n\n"+valortotal+"\n\n"+datahora.getHours()+":"+datahora.getMinutes()+"h";
    }
    
}
