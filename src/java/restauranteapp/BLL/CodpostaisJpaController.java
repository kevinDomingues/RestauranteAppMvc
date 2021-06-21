/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restauranteapp.BLL;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import restauranteapp.DAL.Empresa;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import restauranteapp.BLL.exceptions.IllegalOrphanException;
import restauranteapp.BLL.exceptions.NonexistentEntityException;
import restauranteapp.BLL.exceptions.PreexistingEntityException;
import restauranteapp.DAL.Codpostais;
import restauranteapp.DAL.Fornecedor;
import restauranteapp.DAL.Entidade;

/**
 *
 * @author kevin
 */
public class CodpostaisJpaController implements Serializable {

    public CodpostaisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Codpostais codpostais) throws PreexistingEntityException, Exception {
        if (codpostais.getEmpresaList() == null) {
            codpostais.setEmpresaList(new ArrayList<Empresa>());
        }
        if (codpostais.getFornecedorList() == null) {
            codpostais.setFornecedorList(new ArrayList<Fornecedor>());
        }
        if (codpostais.getEntidadeList() == null) {
            codpostais.setEntidadeList(new ArrayList<Entidade>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Empresa> attachedEmpresaList = new ArrayList<Empresa>();
            for (Empresa empresaListEmpresaToAttach : codpostais.getEmpresaList()) {
                empresaListEmpresaToAttach = em.getReference(empresaListEmpresaToAttach.getClass(), empresaListEmpresaToAttach.getIdEmpresa());
                attachedEmpresaList.add(empresaListEmpresaToAttach);
            }
            codpostais.setEmpresaList(attachedEmpresaList);
            List<Fornecedor> attachedFornecedorList = new ArrayList<Fornecedor>();
            for (Fornecedor fornecedorListFornecedorToAttach : codpostais.getFornecedorList()) {
                fornecedorListFornecedorToAttach = em.getReference(fornecedorListFornecedorToAttach.getClass(), fornecedorListFornecedorToAttach.getIdFornecedor());
                attachedFornecedorList.add(fornecedorListFornecedorToAttach);
            }
            codpostais.setFornecedorList(attachedFornecedorList);
            List<Entidade> attachedEntidadeList = new ArrayList<Entidade>();
            for (Entidade entidadeListEntidadeToAttach : codpostais.getEntidadeList()) {
                entidadeListEntidadeToAttach = em.getReference(entidadeListEntidadeToAttach.getClass(), entidadeListEntidadeToAttach.getIdEntidade());
                attachedEntidadeList.add(entidadeListEntidadeToAttach);
            }
            codpostais.setEntidadeList(attachedEntidadeList);
            em.persist(codpostais);
            for (Empresa empresaListEmpresa : codpostais.getEmpresaList()) {
                Codpostais oldCodpostalOfEmpresaListEmpresa = empresaListEmpresa.getCodpostal();
                empresaListEmpresa.setCodpostal(codpostais);
                empresaListEmpresa = em.merge(empresaListEmpresa);
                if (oldCodpostalOfEmpresaListEmpresa != null) {
                    oldCodpostalOfEmpresaListEmpresa.getEmpresaList().remove(empresaListEmpresa);
                    oldCodpostalOfEmpresaListEmpresa = em.merge(oldCodpostalOfEmpresaListEmpresa);
                }
            }
            for (Fornecedor fornecedorListFornecedor : codpostais.getFornecedorList()) {
                Codpostais oldCodpostalOfFornecedorListFornecedor = fornecedorListFornecedor.getCodpostal();
                fornecedorListFornecedor.setCodpostal(codpostais);
                fornecedorListFornecedor = em.merge(fornecedorListFornecedor);
                if (oldCodpostalOfFornecedorListFornecedor != null) {
                    oldCodpostalOfFornecedorListFornecedor.getFornecedorList().remove(fornecedorListFornecedor);
                    oldCodpostalOfFornecedorListFornecedor = em.merge(oldCodpostalOfFornecedorListFornecedor);
                }
            }
            for (Entidade entidadeListEntidade : codpostais.getEntidadeList()) {
                Codpostais oldCodpostalOfEntidadeListEntidade = entidadeListEntidade.getCodpostal();
                entidadeListEntidade.setCodpostal(codpostais);
                entidadeListEntidade = em.merge(entidadeListEntidade);
                if (oldCodpostalOfEntidadeListEntidade != null) {
                    oldCodpostalOfEntidadeListEntidade.getEntidadeList().remove(entidadeListEntidade);
                    oldCodpostalOfEntidadeListEntidade = em.merge(oldCodpostalOfEntidadeListEntidade);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCodpostais(codpostais.getCodpostal()) != null) {
                throw new PreexistingEntityException("Codpostais " + codpostais + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Codpostais codpostais) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Codpostais persistentCodpostais = em.find(Codpostais.class, codpostais.getCodpostal());
            List<Empresa> empresaListOld = persistentCodpostais.getEmpresaList();
            List<Empresa> empresaListNew = codpostais.getEmpresaList();
            List<Fornecedor> fornecedorListOld = persistentCodpostais.getFornecedorList();
            List<Fornecedor> fornecedorListNew = codpostais.getFornecedorList();
            List<Entidade> entidadeListOld = persistentCodpostais.getEntidadeList();
            List<Entidade> entidadeListNew = codpostais.getEntidadeList();
            List<String> illegalOrphanMessages = null;
            for (Empresa empresaListOldEmpresa : empresaListOld) {
                if (!empresaListNew.contains(empresaListOldEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empresa " + empresaListOldEmpresa + " since its codpostal field is not nullable.");
                }
            }
            for (Fornecedor fornecedorListOldFornecedor : fornecedorListOld) {
                if (!fornecedorListNew.contains(fornecedorListOldFornecedor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Fornecedor " + fornecedorListOldFornecedor + " since its codpostal field is not nullable.");
                }
            }
            for (Entidade entidadeListOldEntidade : entidadeListOld) {
                if (!entidadeListNew.contains(entidadeListOldEntidade)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Entidade " + entidadeListOldEntidade + " since its codpostal field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Empresa> attachedEmpresaListNew = new ArrayList<Empresa>();
            for (Empresa empresaListNewEmpresaToAttach : empresaListNew) {
                empresaListNewEmpresaToAttach = em.getReference(empresaListNewEmpresaToAttach.getClass(), empresaListNewEmpresaToAttach.getIdEmpresa());
                attachedEmpresaListNew.add(empresaListNewEmpresaToAttach);
            }
            empresaListNew = attachedEmpresaListNew;
            codpostais.setEmpresaList(empresaListNew);
            List<Fornecedor> attachedFornecedorListNew = new ArrayList<Fornecedor>();
            for (Fornecedor fornecedorListNewFornecedorToAttach : fornecedorListNew) {
                fornecedorListNewFornecedorToAttach = em.getReference(fornecedorListNewFornecedorToAttach.getClass(), fornecedorListNewFornecedorToAttach.getIdFornecedor());
                attachedFornecedorListNew.add(fornecedorListNewFornecedorToAttach);
            }
            fornecedorListNew = attachedFornecedorListNew;
            codpostais.setFornecedorList(fornecedorListNew);
            List<Entidade> attachedEntidadeListNew = new ArrayList<Entidade>();
            for (Entidade entidadeListNewEntidadeToAttach : entidadeListNew) {
                entidadeListNewEntidadeToAttach = em.getReference(entidadeListNewEntidadeToAttach.getClass(), entidadeListNewEntidadeToAttach.getIdEntidade());
                attachedEntidadeListNew.add(entidadeListNewEntidadeToAttach);
            }
            entidadeListNew = attachedEntidadeListNew;
            codpostais.setEntidadeList(entidadeListNew);
            codpostais = em.merge(codpostais);
            for (Empresa empresaListNewEmpresa : empresaListNew) {
                if (!empresaListOld.contains(empresaListNewEmpresa)) {
                    Codpostais oldCodpostalOfEmpresaListNewEmpresa = empresaListNewEmpresa.getCodpostal();
                    empresaListNewEmpresa.setCodpostal(codpostais);
                    empresaListNewEmpresa = em.merge(empresaListNewEmpresa);
                    if (oldCodpostalOfEmpresaListNewEmpresa != null && !oldCodpostalOfEmpresaListNewEmpresa.equals(codpostais)) {
                        oldCodpostalOfEmpresaListNewEmpresa.getEmpresaList().remove(empresaListNewEmpresa);
                        oldCodpostalOfEmpresaListNewEmpresa = em.merge(oldCodpostalOfEmpresaListNewEmpresa);
                    }
                }
            }
            for (Fornecedor fornecedorListNewFornecedor : fornecedorListNew) {
                if (!fornecedorListOld.contains(fornecedorListNewFornecedor)) {
                    Codpostais oldCodpostalOfFornecedorListNewFornecedor = fornecedorListNewFornecedor.getCodpostal();
                    fornecedorListNewFornecedor.setCodpostal(codpostais);
                    fornecedorListNewFornecedor = em.merge(fornecedorListNewFornecedor);
                    if (oldCodpostalOfFornecedorListNewFornecedor != null && !oldCodpostalOfFornecedorListNewFornecedor.equals(codpostais)) {
                        oldCodpostalOfFornecedorListNewFornecedor.getFornecedorList().remove(fornecedorListNewFornecedor);
                        oldCodpostalOfFornecedorListNewFornecedor = em.merge(oldCodpostalOfFornecedorListNewFornecedor);
                    }
                }
            }
            for (Entidade entidadeListNewEntidade : entidadeListNew) {
                if (!entidadeListOld.contains(entidadeListNewEntidade)) {
                    Codpostais oldCodpostalOfEntidadeListNewEntidade = entidadeListNewEntidade.getCodpostal();
                    entidadeListNewEntidade.setCodpostal(codpostais);
                    entidadeListNewEntidade = em.merge(entidadeListNewEntidade);
                    if (oldCodpostalOfEntidadeListNewEntidade != null && !oldCodpostalOfEntidadeListNewEntidade.equals(codpostais)) {
                        oldCodpostalOfEntidadeListNewEntidade.getEntidadeList().remove(entidadeListNewEntidade);
                        oldCodpostalOfEntidadeListNewEntidade = em.merge(oldCodpostalOfEntidadeListNewEntidade);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = codpostais.getCodpostal();
                if (findCodpostais(id) == null) {
                    throw new NonexistentEntityException("The codpostais with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Codpostais codpostais;
            try {
                codpostais = em.getReference(Codpostais.class, id);
                codpostais.getCodpostal();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The codpostais with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Empresa> empresaListOrphanCheck = codpostais.getEmpresaList();
            for (Empresa empresaListOrphanCheckEmpresa : empresaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Codpostais (" + codpostais + ") cannot be destroyed since the Empresa " + empresaListOrphanCheckEmpresa + " in its empresaList field has a non-nullable codpostal field.");
            }
            List<Fornecedor> fornecedorListOrphanCheck = codpostais.getFornecedorList();
            for (Fornecedor fornecedorListOrphanCheckFornecedor : fornecedorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Codpostais (" + codpostais + ") cannot be destroyed since the Fornecedor " + fornecedorListOrphanCheckFornecedor + " in its fornecedorList field has a non-nullable codpostal field.");
            }
            List<Entidade> entidadeListOrphanCheck = codpostais.getEntidadeList();
            for (Entidade entidadeListOrphanCheckEntidade : entidadeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Codpostais (" + codpostais + ") cannot be destroyed since the Entidade " + entidadeListOrphanCheckEntidade + " in its entidadeList field has a non-nullable codpostal field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(codpostais);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Codpostais> findCodpostaisEntities() {
        return findCodpostaisEntities(true, -1, -1);
    }

    public List<Codpostais> findCodpostaisEntities(int maxResults, int firstResult) {
        return findCodpostaisEntities(false, maxResults, firstResult);
    }

    private List<Codpostais> findCodpostaisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Codpostais.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Codpostais findCodpostais(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Codpostais.class, id);
        } finally {
            em.close();
        }
    }

    public int getCodpostaisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Codpostais> rt = cq.from(Codpostais.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
