package com.bitespeed.backendtask.dao;

import com.bitespeed.backendtask.entity.Order;

import com.bitespeed.backendtask.enums.PrecedenceType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderDao implements AbstractDao{
    private final Session session = HibernateFactory.getHibernateSession();


    public Order insertOrder(Order order)
    {
        Transaction trx = session.beginTransaction();
        Order insertedOrder = session.merge(order);
        System.out.println("Inserted order "+ insertedOrder.toString());
        trx.commit();
        return insertedOrder;
    }

    public Order getOrder(int id)
    {
        return session.get(Order.class, id);
    }

    public List<Order> executeQueryString(String queryString)
    {
        Query<Order> query = session.createQuery(queryString, Order.class);
        return query.list();
    }

    public List<Order> getMatchingPrimaryOrders(String email, String phoneNumber)
    {
        String hql;
        Query<Order> query;
        hql = "FROM Order WHERE (email = :email OR phoneNumber = :phoneNumber) AND linkPrecedence='PRIMARY'";
        query = session.createQuery(hql, Order.class);
        query.setParameter("email", email);
        query.setParameter("phoneNumber", phoneNumber);
        return query.list();
    }

    public Order getPerfectMatchingOrders(String email, String phoneNumber)
    {
        try {
            Query<Order> query;
            String hql;
            if(phoneNumber == null) {
                hql = "FROM Order WHERE email = :email";
                query = session.createQuery(hql, Order.class);
                query.setParameter("email", email);
            }
            else if(email == null) {
                hql = "FROM Order WHERE phoneNumber = :phoneNumber";
                query = session.createQuery(hql, Order.class);
                query.setParameter("phoneNumber", phoneNumber);
            }
            else {
                hql = "FROM Order WHERE (email = :email AND phoneNumber = :phoneNumber)";
                query = session.createQuery(hql, Order.class);
                query.setParameter("email", email);
                query.setParameter("phoneNumber", phoneNumber);
            }
            List<Order> orders = query.list();
            if (orders.size() == 1) {
                Order order = orders.get(0);
                if (order.getLinkPrecedence().equals(PrecedenceType.SECONDARY.toString())) {
                    return getOrder(order.getLinkerId());
                } else {
                    return order;
                }
            } else {
                return orders.stream()
                        .filter(order -> order.getLinkPrecedence().equals(PrecedenceType.PRIMARY.toString()))
                        .findFirst()
                        .orElseThrow(); // Throw an exception if no primary order is found
            }
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Order not found");
            return null;
        }
    }

    public void updatePrimaryOrder(int olderId, int newId)
    {
        // Implement not done completely
        String hql = "UPDATE Order SET linkerId = :newId WHERE linkerId = :olderId";
        Query query = session.createQuery(hql);
        query.setParameter("newId", newId);
        query.setParameter("olderId", olderId);
        Transaction transaction = session.beginTransaction();
        query.executeUpdate();
        transaction.commit();
    }

    public List<Order> getSecondaryOrders(int primaryOrderId)
    {
        String hql = "from Order where linkerId=:linkerId";
        Query<Order> orderQuery = session.createQuery(hql, Order.class);
        orderQuery.setParameter("linkerId", primaryOrderId);
        return orderQuery.list();
    }
}
