package com.dzen03.lab4;

import org.jboss.ejb3.annotation.SecurityDomain;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.persistence.*;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/graph")
@SecurityDomain("other")
public class GraphResource {

    HashMap<Long, ArrayList<Point>> data = new HashMap<>();

    public GraphResource()
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Query q = em.createQuery("select c from Point c");

        List<Point> playerList = q.getResultList();
        for(Point p : playerList)
        {
            Long id = p.getOwner_id();
            if (this.data.containsKey(id))
            {
                this.data.get(id).add(p);
            }
            else
            {
                ArrayList<Point> add = new ArrayList<>();
                add.add(new Point(p.getX(), p.getY(), p.getR(), p.isInside(), p.getOwner_id()));
                this.data.put(id, add);
            }
        }

        tx.commit();
        em.close();
        emf.close();
    }

    @GET
    @Path("points/get")
    @Produces("application/json")
    public String getData(@DefaultValue("") @QueryParam("key") String key)
    {
        AuthLogic authLogic = new AuthLogic();

        Object[] auth = authLogic.isValid(key);

        boolean isValid = (boolean) auth[0];
        if (!isValid)
            return "{\"error\": true, \"message\": \"Invalid key\"}";

        Long id = (Long) auth[1];

        String result = "";

        try (Jsonb jsonb = JsonbBuilder.create())
        {
            result = jsonb.toJson(data.get(id));
        }
        catch (Exception ex)
        {
            result = "{\"error\": true, \"message\": \"Error creating JSON\"}";
        }

        return (result.equals("null") ? "[]" : result);
    }

//    public void setData(ArrayList<Point> data) {
//        this.data = data;
//    }

    public void addData(double x, double y, int r)
    {

    }

    @POST
    @Path("submit")
    @Produces("application/json")
    public String submit(@FormParam("x") String x, @FormParam("y") String y, @FormParam("r") String r, @DefaultValue("") @FormParam("key") String key)
    {
        AuthLogic authLogic = new AuthLogic();

        Object[] auth = authLogic.isValid(key);

        boolean isValid = (boolean) auth[0];
        if (!isValid)
            return "{\"error\": true, \"message\": \"Invalid key\"}";

        Long id = (Long) auth[1];
        
        double x_double;
        double y_double;
        double r_double;
        try
        {
            x_double = Double.parseDouble(x);
            y_double = Double.parseDouble(y);
            r_double = Double.parseDouble(r);
        }
        catch (NumberFormatException exception)
        {
            return "{\"error\": true, \"message\": \"Invalid input\"}";
        }

        Point point = new Point(x_double, y_double, r_double, checkIfInside(x_double, y_double, r_double), id);

        String result = "";

        try (Jsonb jsonb = JsonbBuilder.create())
        {
            result = jsonb.toJson(point);
        }
        catch (Exception ex)
        {
            result = "{\"error\": true, \"message\": \"Error creating JSON\"}";
        }

        addData(point);

        return result;
    }

    public void reset()
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.createQuery("delete from Point").executeUpdate();

        tx.commit();
        em.close();
        data.clear();
        emf.close();
    }

    public void addData(Point data)
    {
//        data.setInside(checkIfInside(data.getX(), data.getY(), data.getR()));
//        this.data.put(data.getOwner_id(), data);

        Long id = data.getOwner_id();
        if (this.data.containsKey(id))
        {
            this.data.get(id).add(data);
        }
        else
        {
            ArrayList<Point> add = new ArrayList<>();
            add.add(data);
            this.data.put(id, add);
        }
//        this.data.put(data.getOwner_id(), (this.data.containsKey(data.getOwner_id()) ? this.data.get(data.getOwner_id()).add(data) : add));
//        return data.isInside();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Point data1 = new Point(data.getX(), data.getY(), data.getR(), data.isInside(), data.getOwner_id());

        em.persist(data1);
        tx.commit();
        em.close();
        emf.close();
    }

    private boolean checkIfInside(double x, double y, double r)
    {
        if (x > 0)
        {
            if (y > 0)
                return x <= r && y <= r/2.;
            else
                return y*y <= r*r/4. - x*x;
        }
        else
        {
            if (y > 0)
                return y <= x + r;
            else
                return false;
        }
    }
}