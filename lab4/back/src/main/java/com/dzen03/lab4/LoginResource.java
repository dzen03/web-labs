package com.dzen03.lab4;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.*;
import javax.ws.rs.*;
import java.util.List;

@Path("/auth")
public class LoginResource
{

    @POST
    @Path("/login")
    @Produces("text/plain")
    public String login(@FormParam("login") String login, @DefaultValue("") @FormParam("pass") String pass)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Query q = em.createQuery("select c from Auth c where c.login like ?1 and c.password like ?2")
                .setParameter(1, login)
                .setParameter(2, Auth.encrypt(pass));

        List<Auth> playerList = q.getResultList();

        em.close();
        emf.close();

        if (playerList.size() == 1)
            return playerList.get(0).getApi_key();
        else
            return "";
    }

    @POST
    @Path("/register")
    @Produces("text/plain")
    public String register(@FormParam("login") String login, @FormParam("pass") String pass)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Auth auth = new Auth(login, pass);

        em.persist(auth);
        tx.commit();
        em.close();
        emf.close();

        return auth.getApi_key();
    }

    @POST
    @Path("/check")
    @Produces("text/plain")
    public String check(@DefaultValue("") @FormParam("key") String key)
    {
        AuthLogic authLogic = new AuthLogic();
        if ((boolean) authLogic.isValid(key)[0])
            return key;
        return "";
    }


}