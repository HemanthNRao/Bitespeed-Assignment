package com.bitespeed.backendtask.service;

import com.bitespeed.backendtask.dao.OrderDao;
import com.bitespeed.backendtask.entity.Order;
import com.bitespeed.backendtask.enums.PrecedenceType;
import com.bitespeed.backendtask.model.Contact;
import com.bitespeed.backendtask.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    public Contact insertOrder(Record record) {
        String email = record.getEmail();
        String phoneNumber = record.getPhoneNumber();
        Order existingOrder;
        existingOrder = getPerfectMatchingOrder(email, phoneNumber);
        if(existingOrder != null)
        {
            return prepareContactResponse(existingOrder);
        }
        List<Order> primaryOrders = getMatchingPrimaryOrders(email, phoneNumber);
        if(primaryOrders.size() == 0)
        {
            Order order = insertOrder(email, phoneNumber, 0, PrecedenceType.PRIMARY);
            return prepareContactResponse(order);
        }
        else
        {
            if (primaryOrders.size() > 1)
            {
                List<Order> orders1 = getLatestOrder(primaryOrders.get(0), primaryOrders.get(1));
                Order oldestOrder = orders1.get(0);
                Order recentOrder = orders1.get(1);
                recentOrder.setLinkerId(oldestOrder.getId());
                recentOrder.setLinkPrecedence(PrecedenceType.SECONDARY.toString());
                orderDao.insertOrder(recentOrder);
                orderDao.updatePrimaryOrder(recentOrder.getId(), oldestOrder.getId());
                return prepareContactResponse(oldestOrder);
            } else
            {
                Order primaryOrder = primaryOrders.get(0);
                insertOrder(email, phoneNumber, primaryOrder.getId(), PrecedenceType.SECONDARY);
                return prepareContactResponse(primaryOrder);
            }
        }
    }

    public Order getOrder(int id)
    {
        return orderDao.getOrder(id);
    }

    public List<Order> getOrders()
    {
        String listQuery = "from Order";
        return orderDao.executeQueryString(listQuery);
    }

    private List<Order> getMatchingPrimaryOrders(String  email, String phoneNumber)
    {
        return orderDao.getMatchingPrimaryOrders(email, phoneNumber);
    }

    private Order getPerfectMatchingOrder(String  email, String phoneNumber)
    {
        return orderDao.getPerfectMatchingOrders(email, phoneNumber);
    }

    private Order insertOrder(String  email, String phoneNumber, int linkerId, PrecedenceType precedenceType)
    {
        Long createdTime = Instant.now().toEpochMilli();
        Order.OrderBuilder orderBuilder = Order.builder()
                .email(email)
                .phoneNumber(phoneNumber)
                .linkPrecedence(precedenceType.toString())
                .createdAt(createdTime)
                .updatedAt(createdTime);
        if(linkerId != 0)
        {
            orderBuilder.linkerId(linkerId);
        }
        Order order = orderBuilder.build();
        return orderDao.insertOrder(order);
    }

    private List<Order> getLatestOrder(Order order1, Order order2)
    {
        List<Order> orders = new ArrayList<>();
        Date order1Date = new Date(order1.getCreatedAt());
        Date order2Date = new Date(order2.getCreatedAt());
        int comparisonResult = order1Date.compareTo(order2Date);
        if(comparisonResult<=0)
        {
            orders.add(order1);
            orders.add(order2);
        }
        else
        {
            orders.add(order2);
            orders.add(order1);
        }
        return orders;
    }

    private List<Order> getSecondaryOrders(int primaryOrderId)
    {
        return orderDao.getSecondaryOrders(primaryOrderId);
    }

    private Contact prepareContactResponse(Order primaryOrder)
    {
        Set<String> emails = new LinkedHashSet<>();
        Set<String> phoneNumbers = new LinkedHashSet<>();

        // Add primary email and phone number
        if (primaryOrder.getEmail() != null) {
            emails.add(primaryOrder.getEmail());
        }
        if (primaryOrder.getPhoneNumber() != null) {
            phoneNumbers.add(primaryOrder.getPhoneNumber());
        }

        List<Order> secondaryOrders = getSecondaryOrders(primaryOrder.getId());
        List<Integer> secondaryOrderIds = secondaryOrders.stream().map(Order::getId).toList();
        // Add secondary email and phone number
        for (Order secondaryOrder : secondaryOrders) {
            if (secondaryOrder.getEmail() != null) {
                emails.add(secondaryOrder.getEmail());
            }
            if (secondaryOrder.getPhoneNumber() != null) {
                phoneNumbers.add(secondaryOrder.getPhoneNumber());
            }
        }
        return Contact.builder()
                .primaryContactId(primaryOrder.getId())
                .emails(emails.stream().toList())
                .phoneNumbers(phoneNumbers.stream().toList())
                .secondaryContactIds(secondaryOrderIds)
                .build();
    }
}
