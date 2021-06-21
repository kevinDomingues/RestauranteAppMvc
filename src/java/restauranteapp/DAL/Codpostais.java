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
@Table(name = "CODPOSTAIS")
@NamedQueries({
    @NamedQuery(name = "Codpostais.findAll", query = "SELECT c FROM Codpostais c"),
    @NamedQuery(name = "Codpostais.findByCodpostal", query = "SELECT c FROM Codpostais c WHERE c.codpostal = :codpostal"),
    @NamedQuery(name = "Codpostais.findByLocalidade", query = "SELECT c FROM Codpostais c WHERE c.localidade = :localidade")})
public class Codpostais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODPOSTAL")
    private String codpostal;
    @Basic(optional = false)
    @Column(name = "LOCALIDADE")
    private String localidade;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codpostal")
    private List<Empresa> empresaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codpostal")
    private List<Fornecedor> fornecedorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codpostal")
    private List<Entidade> entidadeList;

    public Codpostais() {
    }

    public Codpostais(String codpostal) {
        this.codpostal = codpostal;
    }

    public Codpostais(String codpostal, String localidade) {
        this.codpostal = codpostal;
        this.localidade = localidade;
    }

    public String getCodpostal() {
        return codpostal;
    }

    public void setCodpostal(String codpostal) {
        this.codpostal = codpostal;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public List<Empresa> getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List<Empresa> empresaList) {
        this.empresaList = empresaList;
    }

    public List<Fornecedor> getFornecedorList() {
        return fornecedorList;
    }

    public void setFornecedorList(List<Fornecedor> fornecedorList) {
        this.fornecedorList = fornecedorList;
    }

    public List<Entidade> getEntidadeList() {
        return entidadeList;
    }

    public void setEntidadeList(List<Entidade> entidadeList) {
        this.entidadeList = entidadeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codpostal != null ? codpostal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Codpostais)) {
            return false;
        }
        Codpostais other = (Codpostais) object;
        if ((this.codpostal == null && other.codpostal != null) || (this.codpostal != null && !this.codpostal.equals(other.codpostal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.codpostal;
    }
    
}
