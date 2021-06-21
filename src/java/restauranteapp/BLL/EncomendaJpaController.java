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
import restauranteapp.DAL.Estado;
import restauranteapp.DAL.Fornecedor;
import restauranteapp.DAL.Linhaencomenda;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import restauranteapp.BLL.exceptions.IllegalOrphanException;
import restauranteapp.BLL.exceptions.NonexistentEntityException;
import restauranteapp.DAL.Encomenda;

/**
 *
 * @author kevin
 */
public class EncomendaJpaController implements Serializable {

    public EncomendaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Encomenda encomenda) {
        if (encomenda.getLinhaencomendaList() == null) {
            encomenda.setLinhaencomendaList(new ArrayList<Linhaencomenda>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado idEstado = encomenda.getIdEstado();
            if (idEstado != null) {
                idEstado = em.getReference(idEstado.getClass(), idEstado.getIdEstado());
                encomenda.setIdEstado(idEstado);
            }
            Fornecedor idFornecedor = encomenda.getIdFornecedor();
            if (idFornecedor != null) {
                idFornecedor = em.getReference(idFornecedor.getClass(), idFornecedor.getIdFornecedor());
                encomenda.setIdFornecedor(idFornecedor);
            }
            List<Linhaencomenda> attachedLinhaencomendaList = new ArrayList<Linhaencomenda>();
            for (Linhaencomenda linhaencomendaListLinhaencomendaToAttach : encomenda.getLinhaencomendaList()) {
                linhaencomendaListLinhaencomendaToAttach = em.getReference(linhaencomendaListLinhaencomendaToAttach.getClass(), linhaencomendaListLinhaencomendaToAttach.getLinhaencomendaPK());
                attachedLinhaencomendaList.add(linhaencomendaListLinhaencomendaToAttach);
            }
            encomenda.setLinhaencomendaList(attachedLinhaencomendaList);
            em.persist(encomenda);
            if (idEstado != null) {
                idEstado.getEncomendaList().add(encomenda);
                idEstado = em.merge(idEstado);
            }
            if (idFornecedor != null) {
                idFornecedor.getEncomendaList().add(encomenda);
                idFornecedor = em.merge(idFornecedor);
            }
            for (Linhaencomenda linhaencomendaListLinhaencomenda : encomenda.getLinhaencomendaList()) {
                Encomenda oldEncomendaOfLinhaencomendaListLinhaencomenda = linhaencomendaListLinhaencomenda.getEncomenda();
                linhaencomendaListLinhaencomenda.setEncomenda(encomenda);
                linhaencomendaListLinhaencomenda = em.merge(linhaencomendaListLinhaencomenda);
                if (oldEncomendaOfLinhaencomendaListLinhaencomenda != null) {
                    oldEncomendaOfLinhaencomendaListLinhaencomenda.getLinhaencomendaList().remove(linhaencomendaListLinhaencomenda);
                    oldEncomendaOfLinhaencomendaListLinhaencomenda = em.merge(oldEncomendaOfLinhaencomendaListLinhaencomenda);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Encomenda encomenda) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encomenda persistentEncomenda = em.find(Encomenda.class, encomenda.getIdEncomenda());
            Estado idEstadoOld = persistentEncomenda.getIdEstado();
            Estado idEstadoNew = encomenda.getIdEstado();
            Fornecedor idFornecedorOld = persistentEncomenda.getIdFornecedor();
            Fornecedor idFornecedorNew = encomenda.getIdFornecedor();
            List<Linhaencomenda> linhaencomendaListOld = persistentEncomenda.getLinhaencomendaList();
            List<Linhaencomenda> linhaencomendaListNew = encomenda.getLinhaencomendaList();
            List<String> illegalOrphanMessages = null;
            for (Linhaencomenda linhaencomendaListOldLinhaencomenda : linhaencomendaListOld) {
                if (!linhaencomendaListNew.contains(linhaencomendaListOldLinhaencomenda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Linhaencomenda " + linhaencomendaListOldLinhaencomenda + " since its encomenda field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idEstadoNew != null) {
                idEstadoNew = em.getReference(idEstadoNew.getClass(), idEstadoNew.getIdEstado());
                encomenda.setIdEstado(idEstadoNew);
            }
            if (idFornecedorNew != null) {
                idFornecedorNew = em.getReference(idFornecedorNew.getClass(), idFornecedorNew.getIdFornecedor());
                encomenda.setIdFornecedor(idFornecedorNew);
            }
            List<Linhaencomenda> attachedLinhaencomendaListNew = new ArrayList<Linhaencomenda>();
            for (Linhaencomenda linhaencomendaListNewLinhaencomendaToAttach : linhaencomendaListNew) {
                linhaencomendaListNewLinhaencomendaToAttach = em.getReference(linhaencomendaListNewLinhaencomendaToAttach.getClass(), linhaencomendaListNewLinhaencomendaToAttach.getLinhaencomendaPK());
                attachedLinhaencomendaListNew.add(linhaencomendaListNewLinhaencomendaToAttach);
            }
            linhaencomendaListNew = attachedLinhaencomendaListNew;
            encomenda.setLinhaencomendaList(linhaencomendaListNew);
            encomenda = em.merge(encomenda);
            if (idEstadoOld != null && !idEstadoOld.equals(idEstadoNew)) {
                idEstadoOld.getEncomendaList().remove(encomenda);
                idEstadoOld = em.merge(idEstadoOld);
            }
            if (idEstadoNew != null && !idEstadoNew.equals(idEstadoOld)) {
                idEstadoNew.getEncomendaList().add(encomenda);
                idEstadoNew = em.merge(idEstadoNew);
            }
            if (idFornecedorOld != null && !idFornecedorOld.equals(idFornecedorNew)) {
                idFornecedorOld.getEncomendaList().remove(encomenda);
                idFornecedorOld = em.merge(idFornecedorOld);
            }
            if (idFornecedorNew != null && !idFornecedorNew.equals(idFornecedorOld)) {
                idFornecedorNew.getEncomendaList().add(encomenda);
                idFornecedorNew = em.merge(idFornecedorNew);
            }
            for (Linhaencomenda linhaencomendaListNewLinhaencomenda : linhaencomendaListNew) {
                if (!linhaencomendaListOld.contains(linhaencomendaListNewLinhaencomenda)) {
                    Encomenda oldEncomendaOfLinhaencomendaListNewLinhaencomenda = linhaencomendaListNewLinhaencomenda.getEncomenda();
                    linhaencomendaListNewLinhaencomenda.setEncomenda(encomenda);
                    linhaencomendaListNewLinhaencomenda = em.merge(linhaencomendaListNewLinhaencomenda);
                    if (oldEncomendaOfLinhaencomendaListNewLinhaencomenda != null && !oldEncomendaOfLinhaencomendaListNewLinhaencomenda.equals(encomenda)) {
                        oldEncomendaOfLinhaencomendaListNewLinhaencomenda.getLinhaencomendaList().remove(linhaencomendaListNewLinhaencomenda);
                        oldEncomendaOfLinhaencomendaListNewLinhaencomenda = em.merge(oldEncomendaOfLinhaencomendaListNewLinhaencomenda);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = encomenda.getIdEncomenda();
                if (findEncomenda(id) == null) {
                    throw new NonexistentEntityException("The encomenda with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encomenda encomenda;
            try {
                encomenda = em.getReference(Encomenda.class, id);
                encomenda.getIdEncomenda();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The encomenda with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Linhaencomenda> linhaencomendaListOrphanCheck = encomenda.getLinhaencomendaList();
            for (Linhaencomenda linhaencomendaListOrphanCheckLinhaencomenda : linhaencomendaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Encomenda (" + encomenda + ") cannot be destroyed since the Linhaencomenda " + linhaencomendaListOrphanCheckLinhaencomenda + " in its linhaencomendaList field has a non-nullable encomenda field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estado idEstado = encomenda.getIdEstado();
            if (idEstado != null) {
                idEstado.getEncomendaList().remove(encomenda);
                idEstado = em.merge(idEstado);
            }
            Fornecedor idFornecedor = encomenda.getIdFornecedor();
            if (idFornecedor != null) {
                idFornecedor.getEncomendaList().remove(encomenda);
                idFornecedor = em.merge(idFornecedor);
            }
            em.remove(encomenda);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Encomenda> findEncomendaEntities() {
        return findEncomendaEntities(true, -1, -1);
    }

    public List<Encomenda> findEncomendaEntities(int maxResults, int firstResult) {
        return findEncomendaEntities(false, maxResults, firstResult);
    }

    private List<Encomenda> findEncomendaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Encomenda.class));
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

    public Encomenda findEncomenda(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Encomenda.class, id);
        } finally {
            em.close();
        }
    }

    public int getEncomendaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Encomenda> rt = cq.from(Encomenda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
