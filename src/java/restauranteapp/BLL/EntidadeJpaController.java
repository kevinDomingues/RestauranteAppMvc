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
import restauranteapp.DAL.Codpostais;
import restauranteapp.DAL.Empresa;
import restauranteapp.DAL.Pedido;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import restauranteapp.BLL.exceptions.IllegalOrphanException;
import restauranteapp.BLL.exceptions.NonexistentEntityException;
import restauranteapp.DAL.Entidade;
import restauranteapp.DAL.Reserva;

/**
 *
 * @author kevin
 */
public class EntidadeJpaController implements Serializable {

    public EntidadeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Entidade entidade) {
        if (entidade.getPedidoList() == null) {
            entidade.setPedidoList(new ArrayList<Pedido>());
        }
        if (entidade.getReservaList() == null) {
            entidade.setReservaList(new ArrayList<Reserva>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Codpostais codpostal = entidade.getCodpostal();
            if (codpostal != null) {
                codpostal = em.getReference(codpostal.getClass(), codpostal.getCodpostal());
                entidade.setCodpostal(codpostal);
            }
            Empresa idEmpresa = entidade.getIdEmpresa();
            if (idEmpresa != null) {
                idEmpresa = em.getReference(idEmpresa.getClass(), idEmpresa.getIdEmpresa());
                entidade.setIdEmpresa(idEmpresa);
            }
            List<Pedido> attachedPedidoList = new ArrayList<Pedido>();
            for (Pedido pedidoListPedidoToAttach : entidade.getPedidoList()) {
                pedidoListPedidoToAttach = em.getReference(pedidoListPedidoToAttach.getClass(), pedidoListPedidoToAttach.getCodpedido());
                attachedPedidoList.add(pedidoListPedidoToAttach);
            }
            entidade.setPedidoList(attachedPedidoList);
            List<Reserva> attachedReservaList = new ArrayList<Reserva>();
            for (Reserva reservaListReservaToAttach : entidade.getReservaList()) {
                reservaListReservaToAttach = em.getReference(reservaListReservaToAttach.getClass(), reservaListReservaToAttach.getIdReserva());
                attachedReservaList.add(reservaListReservaToAttach);
            }
            entidade.setReservaList(attachedReservaList);
            em.persist(entidade);
            if (codpostal != null) {
                codpostal.getEntidadeList().add(entidade);
                codpostal = em.merge(codpostal);
            }
            if (idEmpresa != null) {
                idEmpresa.getEntidadeList().add(entidade);
                idEmpresa = em.merge(idEmpresa);
            }
            for (Pedido pedidoListPedido : entidade.getPedidoList()) {
                Entidade oldIdEntidadeOfPedidoListPedido = pedidoListPedido.getIdEntidade();
                pedidoListPedido.setIdEntidade(entidade);
                pedidoListPedido = em.merge(pedidoListPedido);
                if (oldIdEntidadeOfPedidoListPedido != null) {
                    oldIdEntidadeOfPedidoListPedido.getPedidoList().remove(pedidoListPedido);
                    oldIdEntidadeOfPedidoListPedido = em.merge(oldIdEntidadeOfPedidoListPedido);
                }
            }
            for (Reserva reservaListReserva : entidade.getReservaList()) {
                Entidade oldIdEntidadeOfReservaListReserva = reservaListReserva.getIdEntidade();
                reservaListReserva.setIdEntidade(entidade);
                reservaListReserva = em.merge(reservaListReserva);
                if (oldIdEntidadeOfReservaListReserva != null) {
                    oldIdEntidadeOfReservaListReserva.getReservaList().remove(reservaListReserva);
                    oldIdEntidadeOfReservaListReserva = em.merge(oldIdEntidadeOfReservaListReserva);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entidade entidade) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entidade persistentEntidade = em.find(Entidade.class, entidade.getIdEntidade());
            Codpostais codpostalOld = persistentEntidade.getCodpostal();
            Codpostais codpostalNew = entidade.getCodpostal();
            Empresa idEmpresaOld = persistentEntidade.getIdEmpresa();
            Empresa idEmpresaNew = entidade.getIdEmpresa();
            List<Pedido> pedidoListOld = persistentEntidade.getPedidoList();
            List<Pedido> pedidoListNew = entidade.getPedidoList();
            List<Reserva> reservaListOld = persistentEntidade.getReservaList();
            List<Reserva> reservaListNew = entidade.getReservaList();
            List<String> illegalOrphanMessages = null;
            for (Pedido pedidoListOldPedido : pedidoListOld) {
                if (!pedidoListNew.contains(pedidoListOldPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedido " + pedidoListOldPedido + " since its idEntidade field is not nullable.");
                }
            }
            for (Reserva reservaListOldReserva : reservaListOld) {
                if (!reservaListNew.contains(reservaListOldReserva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reserva " + reservaListOldReserva + " since its idEntidade field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codpostalNew != null) {
                codpostalNew = em.getReference(codpostalNew.getClass(), codpostalNew.getCodpostal());
                entidade.setCodpostal(codpostalNew);
            }
            if (idEmpresaNew != null) {
                idEmpresaNew = em.getReference(idEmpresaNew.getClass(), idEmpresaNew.getIdEmpresa());
                entidade.setIdEmpresa(idEmpresaNew);
            }
            List<Pedido> attachedPedidoListNew = new ArrayList<Pedido>();
            for (Pedido pedidoListNewPedidoToAttach : pedidoListNew) {
                pedidoListNewPedidoToAttach = em.getReference(pedidoListNewPedidoToAttach.getClass(), pedidoListNewPedidoToAttach.getCodpedido());
                attachedPedidoListNew.add(pedidoListNewPedidoToAttach);
            }
            pedidoListNew = attachedPedidoListNew;
            entidade.setPedidoList(pedidoListNew);
            List<Reserva> attachedReservaListNew = new ArrayList<Reserva>();
            for (Reserva reservaListNewReservaToAttach : reservaListNew) {
                reservaListNewReservaToAttach = em.getReference(reservaListNewReservaToAttach.getClass(), reservaListNewReservaToAttach.getIdReserva());
                attachedReservaListNew.add(reservaListNewReservaToAttach);
            }
            reservaListNew = attachedReservaListNew;
            entidade.setReservaList(reservaListNew);
            entidade = em.merge(entidade);
            if (codpostalOld != null && !codpostalOld.equals(codpostalNew)) {
                codpostalOld.getEntidadeList().remove(entidade);
                codpostalOld = em.merge(codpostalOld);
            }
            if (codpostalNew != null && !codpostalNew.equals(codpostalOld)) {
                codpostalNew.getEntidadeList().add(entidade);
                codpostalNew = em.merge(codpostalNew);
            }
            if (idEmpresaOld != null && !idEmpresaOld.equals(idEmpresaNew)) {
                idEmpresaOld.getEntidadeList().remove(entidade);
                idEmpresaOld = em.merge(idEmpresaOld);
            }
            if (idEmpresaNew != null && !idEmpresaNew.equals(idEmpresaOld)) {
                idEmpresaNew.getEntidadeList().add(entidade);
                idEmpresaNew = em.merge(idEmpresaNew);
            }
            for (Pedido pedidoListNewPedido : pedidoListNew) {
                if (!pedidoListOld.contains(pedidoListNewPedido)) {
                    Entidade oldIdEntidadeOfPedidoListNewPedido = pedidoListNewPedido.getIdEntidade();
                    pedidoListNewPedido.setIdEntidade(entidade);
                    pedidoListNewPedido = em.merge(pedidoListNewPedido);
                    if (oldIdEntidadeOfPedidoListNewPedido != null && !oldIdEntidadeOfPedidoListNewPedido.equals(entidade)) {
                        oldIdEntidadeOfPedidoListNewPedido.getPedidoList().remove(pedidoListNewPedido);
                        oldIdEntidadeOfPedidoListNewPedido = em.merge(oldIdEntidadeOfPedidoListNewPedido);
                    }
                }
            }
            for (Reserva reservaListNewReserva : reservaListNew) {
                if (!reservaListOld.contains(reservaListNewReserva)) {
                    Entidade oldIdEntidadeOfReservaListNewReserva = reservaListNewReserva.getIdEntidade();
                    reservaListNewReserva.setIdEntidade(entidade);
                    reservaListNewReserva = em.merge(reservaListNewReserva);
                    if (oldIdEntidadeOfReservaListNewReserva != null && !oldIdEntidadeOfReservaListNewReserva.equals(entidade)) {
                        oldIdEntidadeOfReservaListNewReserva.getReservaList().remove(reservaListNewReserva);
                        oldIdEntidadeOfReservaListNewReserva = em.merge(oldIdEntidadeOfReservaListNewReserva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entidade.getIdEntidade();
                if (findEntidade(id) == null) {
                    throw new NonexistentEntityException("The entidade with id " + id + " no longer exists.");
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
            Entidade entidade;
            try {
                entidade = em.getReference(Entidade.class, id);
                entidade.getIdEntidade();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entidade with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Pedido> pedidoListOrphanCheck = entidade.getPedidoList();
            for (Pedido pedidoListOrphanCheckPedido : pedidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Entidade (" + entidade + ") cannot be destroyed since the Pedido " + pedidoListOrphanCheckPedido + " in its pedidoList field has a non-nullable idEntidade field.");
            }
            List<Reserva> reservaListOrphanCheck = entidade.getReservaList();
            for (Reserva reservaListOrphanCheckReserva : reservaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Entidade (" + entidade + ") cannot be destroyed since the Reserva " + reservaListOrphanCheckReserva + " in its reservaList field has a non-nullable idEntidade field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Codpostais codpostal = entidade.getCodpostal();
            if (codpostal != null) {
                codpostal.getEntidadeList().remove(entidade);
                codpostal = em.merge(codpostal);
            }
            Empresa idEmpresa = entidade.getIdEmpresa();
            if (idEmpresa != null) {
                idEmpresa.getEntidadeList().remove(entidade);
                idEmpresa = em.merge(idEmpresa);
            }
            em.remove(entidade);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Entidade> findEntidadeEntities() {
        return findEntidadeEntities(true, -1, -1);
    }

    public List<Entidade> findEntidadeEntities(int maxResults, int firstResult) {
        return findEntidadeEntities(false, maxResults, firstResult);
    }

    private List<Entidade> findEntidadeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entidade.class));
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

    public Entidade findEntidade(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entidade.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntidadeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entidade> rt = cq.from(Entidade.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
