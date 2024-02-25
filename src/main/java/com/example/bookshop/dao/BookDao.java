package com.example.bookshop.dao;

import com.example.bookshop.dto.OrderItemInfo;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.BookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookDao extends JpaRepository<Book, BookId> {

    @Query("""
        select  new com.example.bookshop.dto.OrderItemInfo(o.id,o.totalAmount,ot.book,o.orderDate)
        from Customer c join c.orders o join o.orderItems ot
        where c.customerName=?1
        """)
    public OrderItemInfo fetchOrderItemInfo(String userName);
}
