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
import restauranteapp.DAL.Entidade;
import restauranteapp.DAL.Estado;
import restauranteapp.DAL.Mesas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import restauranteapp.BLL.exceptions.NonexistentEntityException;
import restauranteapp.BLL.exceptions.PreexistingEntityException;
import restauranteapp.DAL.Reserva;

/**
 *
 * @author kevin
 */
public class ReservaJpaController implements Serializable {

    public ReservaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reserva reserva) throws PreexistingEntityException, Exception {
        if (reserva.getMesasList() == null) {
            reserva.setMesasList(new ArrayList<Mesas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entidade idEntidade = reserva.getIdEntidade();
            if (idEntidade != null) {
                idEntidade = em.getReference(idEntidade.getClass(), idEntidade.getIdEntidade());
                reserva.setIdEntidade(idEntidade);
            }
            Estado idEstado = reserva.getIdEstado();
            if (idEstado != null) {
                idEstado = em.getReference(idEstado.getClass(), idEstado.getIdEstado());
                reserva.setIdEstado(idEstado);
            }
            List<Mesas> attachedMesasList = new ArrayList<Mesas>();
            for (Mesas mesasListMesasToAttach : reserva.getMesasList()) {
                mesasListMesasToAttach = em.getReference(mesasListMesasToAttach.getClass(), mesasListMesasToAttach.getIdMesa());
                attachedMesasList.add(mesasListMesasToAttach);
            }
            reserva.setMesasList(attachedMesasList);
            em.persist(reserva);
            if (idEntidade != null) {
                idEntidade.getReservaList().add(reserva);
                idEntidade = em.merge(idEntidade);
            }
            if (idEstado != null) {
                idEstado.getReservaList().add(reserva);
                idEstado = em.merge(idEstado);
            }
            for (Mesas mesasListMesas : reserva.getMesasList()) {
                mesasListMesas.getReservaList().add(reserva);
                mesasListMesas = em.merge(mesasListMesas);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReserva(reserva.getIdReserva()) != null) {
                throw new PreexistingEntityException("Reserva " + reserva + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reserva reserva) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reserva persistentReserva = em.find(Reserva.class, reserva.getIdReserva());
            Entidade idEntidadeOld = persistentReserva.getIdEntidade();
            Entidade idEntidadeNew = reserva.getIdEntidade();
            Estado idEstadoOld = persistentReserva.getIdEstado();
            Estado idEstadoNew = reserva.getIdEstado();
            List<Mesas> mesasListOld = persistentReserva.getMesasList();
            List<Mesas> mesasListNew = reserva.getMesasList();
            if (idEntidadeNew != null) {
                idEntidadeNew = em.getReference(idEntidadeNew.getClass(), idEntidadeNew.getIdEntidade());
                reserva.setIdEntidade(idEntidadeNew);
            }
            if (idEstadoNew != null) {
                idEstadoNew = em.getReference(idEstadoNew.getClass(), idEstadoNew.getIdEstado());
                reserva.setIdEstado(idEstadoNew);
            }
            List<Mesas> attachedMesasListNew = new ArrayList<Mesas>();
            for (Mesas mesasListNewMesasToAttach : mesasListNew) {
                mesasListNewMesasToAttach = em.getReference(mesasListNewMesasToAttach.getClass(), mesasListNewMesasToAttach.getIdMesa());
                attachedMesasListNew.add(mesasListNewMesasToAttach);
            }
            mesasListNew = attachedMesasListNew;
            reserva.setMesasList(mesasListNew);
            reserva = em.merge(reserva);
            if (idEntidadeOld != null && !idEntidadeOld.equals(idEntidadeNew)) {
                idEntidadeOld.getReservaList().remove(reserva);
                idEntidadeOld = em.merge(idEntidadeOld);
            }
            if (idEntidadeNew != null && !idEntidadeNew.equals(idEntidadeOld)) {
                idEntidadeNew.getReservaList().add(reserva);
                idEntidadeNew = em.merge(idEntidadeNew);
            }
            if (idEstadoOld != null && !idEstadoOld.equals(idEstadoNew)) {
                idEstadoOld.getReservaList().remove(reserva);
                idEstadoOld = em.merge(idEstadoOld);
            }
            if (idEstadoNew != null && !idEstadoNew.equals(idEstadoOld)) {
                idEstadoNew.getReservaList().add(reserva);
                idEstadoNew = em.merge(idEstadoNew);
            }
            for (Mesas mesasListOldMesas : mesasListOld) {
                if (!mesasListNew.contains(mesasListOldMesas)) {
                    mesasListOldMesas.getReservaList().remove(reserva);
                    mesasListOldMesas = em.merge(mesasListOldMesas);
                }
            }
            for (Mesas mesasListNewMesas : mesasListNew) {
                if (!mesasListOld.contains(mesasListNewMesas)) {
                    mesasListNewMesas.getReservaList().add(reserva);
                    mesasListNewMesas = em.merge(mesasListNewMesas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reserva.getIdReserva();
                if (findReserva(id) == null) {
                    throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reserva reserva;
            try {
                reserva = em.getReference(Reserva.class, id);
                reserva.getIdReserva();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.", enfe);
            }
            Entidade idEntidade = reserva.getIdEntidade();
            if (idEntidade != null) {
                idEntidade.getReservaList().remove(reserva);
                idEntidade = em.merge(idEntidade);
            }
            Estado idEstado = reserva.getIdEstado();
            if (idEstado != null) {
                idEstado.getReservaList().remove(reserva);
                idEstado = em.merge(idEstado);
            }
            List<Mesas> mesasList = reserva.getMesasList();
            for (Mesas mesasListMesas : mesasList) {
                mesasListMesas.getReservaList().remove(reserva);
                mesasListMesas = em.merge(mesasListMesas);
            }
            em.remove(reserva);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reserva> findReservaEntities() {
        return findReservaEntities(true, -1, -1);
    }

    public List<Reserva> findReservaEntities(int maxResults, int firstResult) {
        return findReservaEntities(false, maxResults, firstResult);
    }

    private List<Reserva> findReservaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reserva.class));
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

    public Reserva findReserva(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reserva.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reserva> rt = cq.from(Reserva.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
