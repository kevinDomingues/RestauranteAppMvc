/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restauranteapp.DAL;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author kevin
 */
@Entity
@Table(name = "PRODUTOEMENTA")
@NamedQueries({
    @NamedQuery(name = "Produtoementa.findAll", query = "SELECT p FROM Produtoementa p"),
    @NamedQuery(name = "Produtoementa.findByIdProduto", query = "SELECT p FROM Produtoementa p WHERE p.idProduto = :idProduto"),
    @NamedQuery(name = "Produtoementa.findByNome", query = "SELECT p FROM Produtoementa p WHERE p.nome = :nome"),
    @NamedQuery(name = "Produtoementa.findByPreco", query = "SELECT p FROM Produtoementa p WHERE p.preco = :preco"),
    @NamedQuery(name = "Produtoementa.findByDataDisponibilidade", query = "SELECT p FROM Produtoementa p WHERE p.dataDisponibilidade = :dataDisponibilidade"),
    @NamedQuery(name = "Produtoementa.findByCategoria", query = "SELECT p FROM Produtoementa p WHERE p.categoria = :categoria"),
    @NamedQuery(name = "Produtoementa.findByDescricao", query = "SELECT p FROM Produtoementa p WHERE p.descricao = :descricao"),
    @NamedQuery(name = "Produtoementa.findByTaxa", query = "SELECT p FROM Produtoementa p WHERE p.taxa = :taxa")})
public class Produtoementa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PRODUTO")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idProduto;
    @Basic(optional = false)
    @Column(name = "NOME")
    private String nome;
    @Basic(optional = false)
    @Column(name = "PRECO")
    private double preco;
    @Basic(optional = false)
    @Column(name = "DATA_DISPONIBILIDADE")
    private String dataDisponibilidade;
    @Basic(optional = false)
    @Column(name = "CATEGORIA")
    private String categoria;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Basic(optional = false)
    @Column(name = "TAXA")
    private double taxa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "produtoementa")
    private List<Linhapedido> linhapedidoList;

    public Produtoementa() {
    }

    public Produtoementa(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public Produtoementa(Integer idProduto, String nome, double preco, String dataDisponibilidade, String categoria, double taxa) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.preco = preco;
        this.dataDisponibilidade = dataDisponibilidade;
        this.categoria = categoria;
        this.taxa = taxa;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getDataDisponibilidade() {
        return dataDisponibilidade;
    }

    public void setDataDisponibilidade(String dataDisponibilidade) {
        this.dataDisponibilidade = dataDisponibilidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getTaxa() {
        return taxa;
    }

    public void setTaxa(double taxa) {
        this.taxa = taxa;
    }

    public List<Linhapedido> getLinhapedidoList() {
        return linhapedidoList;
    }

    public void setLinhapedidoList(List<Linhapedido> linhapedidoList) {
        this.linhapedidoList = linhapedidoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProduto != null ? idProduto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produtoementa)) {
            return false;
        }
        Produtoementa other = (Produtoementa) object;
        if ((this.idProduto == null && other.idProduto != null) || (this.idProduto != null && !this.idProduto.equals(other.idProduto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "restauranteapp.DAL.Produtoementa[ idProduto=" + idProduto + " ]";
    }
    
}
