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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author kevin
 */
@Entity
@Table(name = "ENTIDADE")
@NamedQueries({
    @NamedQuery(name = "Entidade.findAll", query = "SELECT e FROM Entidade e"),
    @NamedQuery(name = "Entidade.findByIdEntidade", query = "SELECT e FROM Entidade e WHERE e.idEntidade = :idEntidade"),
    @NamedQuery(name = "Entidade.findByUsername", query = "SELECT e FROM Entidade e WHERE e.username = :username"),
    @NamedQuery(name = "Entidade.findByNome", query = "SELECT e FROM Entidade e WHERE e.nome = :nome"),
    @NamedQuery(name = "Entidade.findByNif", query = "SELECT e FROM Entidade e WHERE e.nif = :nif"),
    @NamedQuery(name = "Entidade.findByRua", query = "SELECT e FROM Entidade e WHERE e.rua = :rua"),
    @NamedQuery(name = "Entidade.findByNporta", query = "SELECT e FROM Entidade e WHERE e.nporta = :nporta"),
    @NamedQuery(name = "Entidade.findByEmail", query = "SELECT e FROM Entidade e WHERE e.email = :email"),
    @NamedQuery(name = "Entidade.findByTelefone", query = "SELECT e FROM Entidade e WHERE e.telefone = :telefone"),
    @NamedQuery(name = "Entidade.findByPasswordp", query = "SELECT e FROM Entidade e WHERE e.passwordp = :passwordp"),
    @NamedQuery(name = "Entidade.findByNivelpermissao", query = "SELECT e FROM Entidade e WHERE e.nivelpermissao = :nivelpermissao")})
public class Entidade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_ENTIDADE")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idEntidade;
    @Basic(optional = false)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @Column(name = "NOME")
    private String nome;
    @Basic(optional = false)
    @Column(name = "NIF")
    private int nif;
    @Basic(optional = false)
    @Column(name = "RUA")
    private String rua;
    @Basic(optional = false)
    @Column(name = "NPORTA")
    private int nporta;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "TELEFONE")
    private Integer telefone;
    @Column(name = "PASSWORDP")
    private String passwordp;
    @Basic(optional = false)
    @Column(name = "NIVELPERMISSAO")
    private int nivelpermissao;
    @JoinColumn(name = "CODPOSTAL", referencedColumnName = "CODPOSTAL")
    @ManyToOne(optional = false)
    private Codpostais codpostal;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID_EMPRESA")
    @ManyToOne(optional = false)
    private Empresa idEmpresa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEntidade")
    private List<Pedido> pedidoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEntidade")
    private List<Reserva> reservaList;

    public Entidade() {
    }

    public Entidade(Integer idEntidade) {
        this.idEntidade = idEntidade;
    }

    public Entidade(Integer idEntidade, String username, String nome, int nif, String rua, int nporta, int nivelpermissao) {
        this.idEntidade = idEntidade;
        this.username = username;
        this.nome = nome;
        this.nif = nif;
        this.rua = rua;
        this.nporta = nporta;
        this.nivelpermissao = nivelpermissao;
    }

    public Integer getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(Integer idEntidade) {
        this.idEntidade = idEntidade;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNif() {
        return nif;
    }

    public void setNif(int nif) {
        this.nif = nif;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public int getNporta() {
        return nporta;
    }

    public void setNporta(int nporta) {
        this.nporta = nporta;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTelefone() {
        return telefone;
    }

    public void setTelefone(Integer telefone) {
        this.telefone = telefone;
    }

    public String getPasswordp() {
        return passwordp;
    }

    public void setPasswordp(String passwordp) {
        this.passwordp = passwordp;
    }

    public int getNivelpermissao() {
        return nivelpermissao;
    }

    public void setNivelpermissao(int nivelpermissao) {
        this.nivelpermissao = nivelpermissao;
    }

    public Codpostais getCodpostal() {
        return codpostal;
    }

    public void setCodpostal(Codpostais codpostal) {
        this.codpostal = codpostal;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public List<Pedido> getPedidoList() {
        return pedidoList;
    }

    public void setPedidoList(List<Pedido> pedidoList) {
        this.pedidoList = pedidoList;
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
        hash += (idEntidade != null ? idEntidade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entidade)) {
            return false;
        }
        Entidade other = (Entidade) object;
        if ((this.idEntidade == null && other.idEntidade != null) || (this.idEntidade != null && !this.idEntidade.equals(other.idEntidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "restauranteapp.DAL.Entidade[ idEntidade=" + idEntidade + " ]";
    }
    
}
