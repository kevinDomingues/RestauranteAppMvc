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
@Table(name = "STOCKPRODUTO")
@NamedQueries({
    @NamedQuery(name = "Stockproduto.findAll", query = "SELECT s FROM Stockproduto s"),
    @NamedQuery(name = "Stockproduto.findByIdStockproduto", query = "SELECT s FROM Stockproduto s WHERE s.idStockproduto = :idStockproduto"),
    @NamedQuery(name = "Stockproduto.findByNome", query = "SELECT s FROM Stockproduto s WHERE s.nome = :nome"),
    @NamedQuery(name = "Stockproduto.findByDescricao", query = "SELECT s FROM Stockproduto s WHERE s.descricao = :descricao"),
    @NamedQuery(name = "Stockproduto.findByQuantidade", query = "SELECT s FROM Stockproduto s WHERE s.quantidade = :quantidade"),
    @NamedQuery(name = "Stockproduto.findByPreco", query = "SELECT s FROM Stockproduto s WHERE s.preco = :preco"),
    @NamedQuery(name = "Stockproduto.findByTaxa", query = "SELECT s FROM Stockproduto s WHERE s.taxa = :taxa")})
public class Stockproduto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_STOCKPRODUTO")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idStockproduto;
    @Basic(optional = false)
    @Column(name = "NOME")
    private String nome;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Basic(optional = false)
    @Column(name = "QUANTIDADE")
    private int quantidade;
    @Basic(optional = false)
    @Column(name = "PRECO")
    private double preco;
    @Basic(optional = false)
    @Column(name = "TAXA")
    private double taxa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stockproduto")
    private List<Linhaencomenda> linhaencomendaList;

    public Stockproduto() {
    }

    public Stockproduto(Integer idStockproduto) {
        this.idStockproduto = idStockproduto;
    }

    public Stockproduto(Integer idStockproduto, String nome, int quantidade, double preco, double taxa) {
        this.idStockproduto = idStockproduto;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.taxa = taxa;
    }

    public Integer getIdStockproduto() {
        return idStockproduto;
    }

    public void setIdStockproduto(Integer idStockproduto) {
        this.idStockproduto = idStockproduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getTaxa() {
        return taxa;
    }

    public void setTaxa(double taxa) {
        this.taxa = taxa;
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
        hash += (idStockproduto != null ? idStockproduto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stockproduto)) {
            return false;
        }
        Stockproduto other = (Stockproduto) object;
        if ((this.idStockproduto == null && other.idStockproduto != null) || (this.idStockproduto != null && !this.idStockproduto.equals(other.idStockproduto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Nome produto: "+nome+"\n\nPreço sem Iva: "+preco+" , Taxa: "+taxa+"\n\nPreço com Iva: "+(preco+(preco*taxa))+"\n\nDescrição: "+descricao;
    }
    
}
