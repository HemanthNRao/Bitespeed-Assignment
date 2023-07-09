package com.bitespeed.backendtask.resource;

import com.bitespeed.backendtask.entity.Order;
import com.bitespeed.backendtask.model.Contact;
import com.bitespeed.backendtask.model.JsonResponse;
import com.bitespeed.backendtask.model.Record;
import com.bitespeed.backendtask.service.OrderService;
import com.bitespeed.backendtask.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderResource {

    @Autowired
    OrderService orderService;
    @PostMapping("/identify")
    public ResponseEntity<JsonResponse> insertOrder(@RequestBody Record record)
    {
        Contact contact = orderService.insertOrder(record);
        JsonResponse jsonResponse = JsonResponse.builder().contact(contact).build();
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    @GetMapping("/identify/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable  int id)
    {
        Order order = orderService.getOrder(id);
        if(order == null)
        {
            Utils.handleNotFound("Order not found with given ID");
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/identify")
    public ResponseEntity<List<Order>> getOrders()
    {
        List<Order> orders = orderService.getOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}
